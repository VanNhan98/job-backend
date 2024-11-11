package vn.job.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class ResMailJob {


    private String name;
    private double salary;
    private CompanyEmail company;
    private List<SkillEmail> skills;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CompanyEmail {
        private String name;

    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SkillEmail {
        private String name;
    }
}
