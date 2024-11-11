package vn.job.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.job.dto.response.error.ResponseData;
import vn.job.model.Skill;
import vn.job.model.Subscriber;
import vn.job.service.SubscriberService;

@RestController
@RequestMapping("/subscribers")
@Slf4j
@RequiredArgsConstructor
public class SubscriberController {
    private final SubscriberService subscriberService;

    @PostMapping("/add")
    public ResponseData<Subscriber> addSubscriber(@Valid @RequestBody Subscriber subscriber) {
        log.info("Request create subscriber={}", subscriber.getName());
        try {
            Subscriber currentSubscriber =  this.subscriberService.handleCreateSubscriber(subscriber);
            return new ResponseData<>(HttpStatus.CREATED.value(), "subscriber add successfully", currentSubscriber);
        }
        catch (Exception e) {
            log.error("errorMessage= {} ", e.getMessage(), e.getCause());
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "Add subscriber failed");
        }
    }

    @PutMapping("/update")
    public ResponseData<Subscriber> updateSubscriber(@RequestBody Subscriber subscriber) {
        log.info("Request update getId={}", subscriber.getId());
        try {
            Subscriber currentSubscriber =  this.subscriberService.handleUpdateSubscriber(subscriber);
            return new ResponseData<>(HttpStatus.CREATED.value(), "subscriber update successfully", currentSubscriber);
        }
        catch (Exception e) {
            log.error("errorMessage= {} ", e.getMessage(), e.getCause());
            return new ResponseData<>(HttpStatus.BAD_REQUEST.value(), "Update subscriber failed");
        }
    }
}
