package vn.job.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.job.model.Company;
import vn.job.model.Skill;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> , JpaSpecificationExecutor<Company> {

}
