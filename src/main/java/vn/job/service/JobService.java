package vn.job.service;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.job.dto.response.ResPagination;
import vn.job.dto.response.job.ResCreateJob;
import vn.job.dto.response.job.ResUpdateJob;
import vn.job.exception.IdInvalidException;
import vn.job.model.Company;
import vn.job.model.Job;
import vn.job.model.Skill;
import vn.job.repository.CompanyRepository;
import vn.job.repository.JobRepository;
import vn.job.repository.SkillRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobService {
    private final JobRepository jobRepository;

    private final SkillRepository skillRepository;

    private final CompanyRepository companyRepository;

    public ResCreateJob handleCreateJob(Job job) {
        log.info("-------------------Create Job ----------------");

        if (job.getSkills() != null) {
            List<Long> reqSkill = job.getSkills().stream().map(item -> item.getId()).collect(Collectors.toList());
            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkill);
            job.setSkills(dbSkills);
        }

        // save
        Job currentJob = this.jobRepository.save(job);

        List<String> listSkills = null;
        if (currentJob.getSkills() != null) {
            listSkills = currentJob.getSkills().stream()
                    .map(item -> item.getName())
                    .collect(Collectors.toList());
        }


        if (job.getCompany() != null) {
            Company company = this.companyRepository.findById(job.getCompany().getId()).orElse(null);
            job.setCompany(company);
        }


        // response view
        ResCreateJob resJob = ResCreateJob.builder()
                .id(currentJob.getId())
                .name(currentJob.getName())
                .location(currentJob.getLocation())
                .salary(currentJob.getSalary())
                .quantity(currentJob.getQuantity())
                .level(currentJob.getLevel())
                .startDate(currentJob.getStartDate())
                .endDate(currentJob.getEndDate())
                .active(currentJob.isActive())
                .company(currentJob.getCompany())
                .skills(listSkills)
                .createdAt(currentJob.getCreatedAt())
                .createdBy(currentJob.getCreatedBy())
                .build();
        return resJob;
    }

    public ResUpdateJob handleUpdateJob(Job reqJob) {
        log.info("-------------------Update Job ----------------");
        Job job = handleGetJobById(reqJob.getId());

        if (job.getSkills() != null) {
            List<Long> reqSkill = job.getSkills().stream().map(item -> item.getId()).collect(Collectors.toList());
            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkill);
            job.setSkills(dbSkills);
        }


        if (job.getCompany() != null) {
            Company company = this.companyRepository.findById(job.getCompany().getId()).orElse(null);
            job.setCompany(company);
        }


        // update
        job.setId(reqJob.getId());
        job.setName(reqJob.getName());
        job.setLocation(reqJob.getLocation());
        job.setSalary(reqJob.getSalary());
        job.setQuantity(reqJob.getQuantity());
        job.setLevel(reqJob.getLevel());
        job.setStartDate(reqJob.getStartDate());
        job.setEndDate(reqJob.getEndDate());
        job.setActive(reqJob.isActive());
        job.setCompany(reqJob.getCompany());
        job.setSkills(reqJob.getSkills());
        job.setUpdatedAt(reqJob.getUpdatedAt());
        job.setUpdatedBy(reqJob.getUpdatedBy());

        // save
        Job currentJob = this.jobRepository.save(job);

        List<String> listSkills = null;
        if (currentJob.getSkills() != null) {
            listSkills = currentJob.getSkills().stream()
                    .map(item -> item.getName())
                    .collect(Collectors.toList());
        }

        // response view
        ResUpdateJob resJob = ResUpdateJob.builder()
                .id(currentJob.getId())
                .name(currentJob.getName())
                .location(currentJob.getLocation())
                .salary(currentJob.getSalary())
                .quantity(currentJob.getQuantity())
                .level(currentJob.getLevel())
                .startDate(currentJob.getStartDate())
                .endDate(currentJob.getEndDate())
                .active(currentJob.isActive())
                .skills(listSkills)
                .updatedAt(currentJob.getUpdatedAt())
                .updatedBy(currentJob.getUpdatedBy())
                .build();
        return resJob;
    }


    public void handleDeleteJob(long id) {
        log.info("-------------------Delete Job ----------------");
        Job currentJob = handleGetJobById(id);
        this.jobRepository.delete(currentJob);

    }

    public Job handleGetJob(long id) {
        log.info("-------------------Get Job ----------------");
        return handleGetJobById(id);

    }


    public Job handleGetJobById(long id) {
        return this.jobRepository.findById(id).orElseThrow(() -> new IdInvalidException("Id not found"));
    }

    public ResPagination handleGetAllJobs(Specification<Job> spec, Pageable pageable) {
        Page<Job> pageJob = this.jobRepository.findAll(spec, pageable);
        ResPagination rs = new ResPagination();
        ResPagination.Meta mt = new ResPagination.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageJob.getTotalPages());
        mt.setTotal(pageJob.getTotalElements());
        rs.setMeta(mt);
        rs.setResult(pageJob.getContent());
        return rs;
    }
}
