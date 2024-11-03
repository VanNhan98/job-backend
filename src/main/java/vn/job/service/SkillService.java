package vn.job.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.job.dto.response.ResPagination;
import vn.job.exception.EntityAlreadyExistsException;
import vn.job.exception.IdInvalidException;
import vn.job.model.Company;
import vn.job.model.Job;
import vn.job.model.Skill;
import vn.job.repository.SkillRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SkillService {
    private final SkillRepository skillRepository;

    public Skill handleCreateSkill(Skill skill) {
        log.info("---------------add skill---------------");
        if(skill.getName() != null && isNameExist(skill.getName())) {
            throw new EntityAlreadyExistsException("Skill name already exists");
        }
        return this.skillRepository.save(skill);
    }

    public boolean isNameExist(String name) {
        return this.skillRepository.existsByName(name);
    }

    public Skill handleUpdateSkill( Skill skill) {
        log.info("---------------update skill---------------");
        Skill skillDB = handleGetSkillById(skill.getId());
        if(skill.getName() != null && isNameExist(skill.getName())) {
            throw new EntityAlreadyExistsException("Skill name already exists");
        }
        skillDB.setName(skill.getName());
        return this.skillRepository.save(skillDB);
    }

    private Skill handleGetSkillById(long id) {
        return this.skillRepository.findById(id).orElseThrow(() -> new IdInvalidException("Skill not found"));
    }


    public ResPagination handleGetAllSkills(Specification<Skill> spec, Pageable pageable) {
        log.info("---------------get list skill---------------");
        Page<Skill> pageSkill = this.skillRepository.findAll(spec, pageable);
        ResPagination rs = new ResPagination();
        ResPagination.Meta mt = new ResPagination.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageSkill.getTotalPages());
        mt.setTotal(pageSkill.getTotalElements());
        rs.setMeta(mt);
        rs.setResult(pageSkill.getContent());
        return rs;
    }

    public void handleDeleteSkill(long id) {
        log.info("---------------delete skill---------------");
        Skill skillDB = handleGetSkillById(id);
        if(skillDB.getJobs() != null) {
            List<Job> jobs = skillDB.getJobs();
            for (Job job : jobs) {
                job.getSkills().remove(skillDB);
            }
        }
        this.skillRepository.delete(skillDB);
    }
}
