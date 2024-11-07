package vn.job.service;

import com.turkraft.springfilter.boot.Filter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import vn.job.dto.request.RequestUpdateResume;
import vn.job.dto.response.ResPagination;
import vn.job.dto.response.error.ResponseData;
import vn.job.dto.response.error.ResponseError;
import vn.job.dto.response.resume.ResCreateResume;
import vn.job.dto.response.resume.ResDetailResume;
import vn.job.dto.response.resume.ResUpdateResume;
import vn.job.exception.EntityAlreadyExistsException;
import vn.job.exception.IdInvalidException;
import vn.job.model.Company;
import vn.job.model.Job;
import vn.job.model.Resume;
import vn.job.model.User;
import vn.job.repository.JobRepository;
import vn.job.repository.ResumeRepository;
import vn.job.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;
    public ResCreateResume handleCreateResume(Resume resume) {
        log.info("-------------------Create Resume ----------------");
        if(!checkResumeExistByUserAndJob(resume)) {
            throw new EntityAlreadyExistsException("Resume not exists");
        }


        Resume currentResume =  this.resumeRepository.save(resume);

        ResCreateResume resResume = ResCreateResume.builder()
                .id(resume.getId())
                .createdBy(currentResume.getCreatedBy())
                .createdAt(currentResume.getCreatedAt())
                .build();
        return resResume;
    }

    public boolean checkResumeExistByUserAndJob(Resume resume) {

        if (resume.getUser() == null) {
            return false;
        }
        Optional<User> userOptional = this.userRepository.findById((resume.getUser().getId()));
        if (userOptional.isEmpty()) {
            return false;
        }

        if (resume.getJob() == null) {
            return false;
        }

        Optional<Job> jobOptional = this.jobRepository.findById((resume.getJob().getId()));
        if (jobOptional.isEmpty()) {
            return false;
        }
        return true;
    }


    public ResUpdateResume handleUpdateResume(RequestUpdateResume resume) {
        log.info("-------------------Update Resume ----------------");
        Resume currentResume = handleGetResumeById(resume.getId());
        currentResume.setId(resume.getId());
        currentResume.setStatus(resume.getStatus());

        Resume resumeDB =  this.resumeRepository.save(currentResume);

        ResUpdateResume resResume = ResUpdateResume.builder()
                .id(resumeDB.getId())
                .updatedBy(resumeDB.getUpdatedBy())
                .updatedAt(resumeDB.getUpdatedAt())
                .build();
        return resResume;
    }

    public Resume handleGetResumeById(long id) {
        return this.resumeRepository.findById(id).orElseThrow(() -> new IdInvalidException("Id not found"));
    }

    public void handleDeleteResume(long id) {
        handleGetResumeById(id);
        this.resumeRepository.deleteById(id);
    }

    public ResDetailResume handleGetResume(long id) {
        Resume resume = handleGetResumeById(id);
        ResDetailResume resDetailResume = new ResDetailResume();
        resDetailResume.setId(resume.getId());
        resDetailResume.setUrl(resume.getUrl());
        resDetailResume.setStatus(resume.getStatus());
        resDetailResume.setCreatedAt(resume.getCreatedAt());
        resDetailResume.setUpdatedAt(resume.getUpdatedAt());
        resDetailResume.setCreatedBy(resume.getCreatedBy());
        resDetailResume.setUpdatedBy(resume.getUpdatedBy());

        User user = resume.getUser();
        if(user != null) {
            resDetailResume.setUser(new ResDetailResume.UserResume(user.getId(), user.getUsername()));
        }

        Job job = resume.getJob();
        if(job!= null) {
            resDetailResume.setJob(new ResDetailResume.JobResume(job.getId(), job.getName()));
        }

        Company company = resume.getJob().getCompany();
        if(company!= null) {
            resDetailResume.setCompany(new ResDetailResume.CompanyResume(company.getId(), company.getName()));
        }

        return resDetailResume;
    }


    public ResPagination handleGetAllResume(Specification<Resume> spec, Pageable pageable) {
        Page<Resume> pageResume = this.resumeRepository.findAll(spec, pageable);
        ResPagination rs = new ResPagination();
        ResPagination.Meta mt = new ResPagination.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageResume.getTotalPages());
        mt.setTotal(pageResume.getTotalElements());
        rs.setMeta(mt);
        List<ResDetailResume> list = pageResume.getContent().stream()
                .map(resume -> this.handleGetResume(resume.getId()))
                .collect(Collectors.toList());
        rs.setResult(list);
        return rs;
    }
}
