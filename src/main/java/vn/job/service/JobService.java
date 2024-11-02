package vn.job.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.job.repository.JobRepository;

@Service
@RequiredArgsConstructor
public class JobService {
    private final JobRepository jobRepository;

}
