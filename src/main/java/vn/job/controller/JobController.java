package vn.job.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.job.service.JobService;

@RestController
@RequestMapping("jobs")
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;

}
