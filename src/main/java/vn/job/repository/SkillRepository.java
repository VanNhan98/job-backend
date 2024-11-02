package vn.job.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.job.model.Company;
import vn.job.model.Skill;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> , JpaSpecificationExecutor<Skill> {
    boolean existsByName(String name);


}
