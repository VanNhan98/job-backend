package vn.job.dto.response.job;

import lombok.Builder;
import lombok.Getter;
import vn.job.model.Company;
import vn.job.model.Job;
import vn.job.util.LevelEnum;

import java.util.Date;
import java.util.List;

@Getter
@Builder
public class ResCreateJob {

    private long id;
    
    private String name;

    private String location;

    private double salary;

    private int quantity;

    private LevelEnum level;

    private Date startDate;

    private Date endDate;

    private boolean active;

    private Company company;

    private List<String> skills;

    private Date createdAt;

    private String createdBy;
}
