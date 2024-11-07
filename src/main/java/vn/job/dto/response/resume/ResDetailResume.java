package vn.job.dto.response.resume;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.job.util.StatusEnum;

import java.util.Date;

@Getter
@Setter
public class ResDetailResume {
    private long id;

    private String email;

    private String url;

    private StatusEnum status;

    private Date createdAt;

    private Date updatedAt;

    private String createdBy;

    private String updatedBy;

    private UserResume user;

    private JobResume job;

    private CompanyResume company;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class UserResume {
        private long id;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class JobResume {
        private long id;
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class CompanyResume {
        private long id;
        private String name;
    }
}
