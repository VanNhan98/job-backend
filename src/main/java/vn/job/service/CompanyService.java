package vn.job.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.job.dto.response.ResPagination;
import vn.job.exception.IdInvalidException;
import vn.job.model.Company;
import vn.job.model.User;
import vn.job.repository.CompanyRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompanyService {
    private  final CompanyRepository companyRepository;

    public Company handleCreateCompany(Company company) {
        log.info("---------------create company---------------");
        return this.companyRepository.save(company);
    }

    public Company handleUpdateCompany(Company reqcompany) {
        log.info("---------------update company---------------");
        Company company = handleGetCompanyById(reqcompany.getId());
        company.setName(reqcompany.getName());
        company.setLogo(reqcompany.getLogo());
        company.setAddress(reqcompany.getAddress());
        company.setDescription(reqcompany.getDescription());
        company.setUpdatedAt(reqcompany.getUpdatedAt());
        company.setUpdatedBy(reqcompany.getUpdatedBy());
        return this.companyRepository.save(company);
    }

    public void handleDeleteCompany(long id) {
        log.info("---------------delete company---------------");
        Company currentCompany = handleGetCompanyById(id);
        this.companyRepository.deleteById(currentCompany.getId());
        log.info("Delete successfully");
    }

    private Company handleGetCompanyById(long id) {
        return this.companyRepository.findById(id).orElseThrow(() -> new IdInvalidException("Company not found"));
    }


    public ResPagination handleGetAllCompanies(Specification<Company> spec, Pageable pageable) {
        Page<Company> pageCompany = this.companyRepository.findAll(spec, pageable);
        ResPagination rs = new ResPagination();
        ResPagination.Meta mt = new ResPagination.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageCompany.getTotalPages());
        mt.setTotal(pageCompany.getTotalElements());
        rs.setMeta(mt);
        rs.setResult(pageCompany.getContent());
        return rs;
    }

    public Company handleGetCompany(long id) {
        log.info("---------------get company---------------");
        return handleGetCompanyById(id);
    }
}
