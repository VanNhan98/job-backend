package vn.job.service;


import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.job.dto.response.ResMailJob;
import vn.job.exception.EntityAlreadyExistsException;
import vn.job.exception.IdInvalidException;
import vn.job.model.BaseEntity;
import vn.job.model.Job;
import vn.job.model.Skill;
import vn.job.model.Subscriber;
import vn.job.repository.JobRepository;
import vn.job.repository.SkillRepository;
import vn.job.repository.SubscriberRepository;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;

    private final SkillRepository skillRepository;

    private final JobRepository jobRepository;

    private final EmailService emailService;

    public Subscriber handleCreateSubscriber(Subscriber subscriber) {

        if(this.subscriberRepository.existsByEmail(subscriber.getEmail())) {
            throw new EntityAlreadyExistsException("Email already exists");
        }

        if(subscriber.getSkills() != null) {
            List<Long> reqSkill =  subscriber.getSkills().stream().map(BaseEntity::getId).toList();
            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkill);
            subscriber.setSkills(dbSkills);
        }
        return this.subscriberRepository.save(subscriber);
    }

    public Subscriber handleUpdateSubscriber(Subscriber subscriber) {
        Subscriber currentSubscriber = this.handleGetSubscriberById(subscriber.getId());

        if(subscriber.getSkills()!= null) {
            List<Long> reqSkill = subscriber.getSkills().stream().map(BaseEntity::getId).collect(Collectors.toList());
            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkill);
            subscriber.setSkills(dbSkills);
        }

        currentSubscriber.setSkills(subscriber.getSkills());
        return this.subscriberRepository.save(currentSubscriber);
    }

    private Subscriber handleGetSubscriberById(Long id) {
        return this.subscriberRepository.findById(id).orElseThrow(() -> new IdInvalidException("Id not found"));
    }


    public void sendSubscribersEmailJobs() throws MessagingException, UnsupportedEncodingException {
        List<Subscriber> listSubs = this.subscriberRepository.findAll();
        if (listSubs != null && listSubs.size() > 0) {
            for (Subscriber sub : listSubs) {
                List<Skill> listSkills = sub.getSkills();
                if (listSkills != null && !listSkills.isEmpty()) {
                    List<Job> listJobs = this.jobRepository.findBySkillsIn(listSkills);
                    if (listJobs != null && !listJobs.isEmpty()) {
                        List<ResMailJob> arr = listJobs.stream().map(
                                job -> this.convertJobToSendEmail(job)).collect(Collectors.toList());
                        this.emailService.sendSubscriberJob(
                                sub.getEmail(),
                                sub.getName(),
                                arr);
                    }
                }
            }
        }
    }

    private ResMailJob convertJobToSendEmail(Job job) {
        ResMailJob res = new ResMailJob();
        ResMailJob.CompanyEmail companyEmail = new ResMailJob.CompanyEmail();
        res.setName(job.getName());
        res.setSalary(job.getSalary());
        if (job.getCompany() != null) {
            companyEmail.setName(job.getCompany().getName());
            res.setCompany(companyEmail);
        }
        if (job.getSkills() != null) {
            List<Skill> skills = job.getSkills();
            List<ResMailJob.SkillEmail> s = skills.stream().map(skill -> new ResMailJob.SkillEmail(skill.getName()))
                    .collect(Collectors.toList());
            res.setSkills(s);
        }
        return res;

    }

}
