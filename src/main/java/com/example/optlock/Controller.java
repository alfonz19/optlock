package com.example.optlock;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/optlock")
public class Controller {

    private final EntityManager entityManager;
    private final TransactionTemplate transactionTemplate;

    public Controller(EntityManager entityManager, TransactionTemplate transactionTemplate) {
        this.entityManager = entityManager;
        this.transactionTemplate = transactionTemplate;
    }

    @Transactional
    @PostMapping
    public String createNew() {
        SampleEntity sampleEntity = SampleEntity.builder().valueA("1").valueB("2").build();
        entityManager.persist(sampleEntity);
        log.info("creating new entity {}", sampleEntity);
        return sampleEntity.getId().toString();
    }

//    @Transactional
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{id}")
    public String getById(@PathVariable("id") String idString) {
        SampleEntity sampleEntity = this.entityManager.find(SampleEntity.class, UUID.fromString(idString));
        log.info("fetching entity by id {}: {}", idString, sampleEntity);

        return sampleEntity==null?"null":sampleEntity.toString();
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(path = "/{id}")
    public void acceptExistingResponseTypes(
            @PathVariable("id") String idString,
            @RequestParam(value = "a") String valueA,
            @RequestParam(value = "b") String valueB,
            @RequestParam(value = "busyWait") boolean busyWait) {

        log.info("Updating entity id='{}' to: a={}, b={}", idString, valueA, valueB);
        UUID id = UUID.fromString(idString);

        try {
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    try {
                        SampleEntity sampleEntity = entityManager.find(SampleEntity.class, id);

                        if (sampleEntity == null) {
                            log.warn("Requested entity having id '{}' does not exist", id);
                            return;
                        }

                        log.info("Before action: {}", sampleEntity);

                        sampleEntity.setValueA(valueA);
                        sampleEntity.setValueB(valueB);

                        log.info("After action: {}", sampleEntity);

                        if (busyWait) {
                            log.info("Started busywait for 120s");
                            Instant terminateAt = Instant.now().plus(Duration.ofSeconds(120));

                            while (Instant.now().isBefore(terminateAt)) {
                                //no-op
                                //just to avoid thread re-scheduling.
                            }
                            log.info("Busy-wait finished.");
                        }
                    } catch (RuntimeException e) {
                        log.info("Exception! {}", e.getClass());
                        e.printStackTrace();
                    }
                }
            });
        } catch (RuntimeException e) {
            log.info("!!!");
            e.printStackTrace();
        }


    }

}