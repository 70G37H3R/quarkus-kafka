package org.acme.scheduler;

import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata;
import lombok.extern.slf4j.Slf4j;
import org.acme.kafka.AvroDevice;
import org.acme.service.device.DeviceService;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Slf4j
@Path("/job")
@ApplicationScoped
public class JobController implements Job {

    @Inject
    TaskBean task;


    public void execute(JobExecutionContext context)  {
        try {
            log.info("Job Scheduler Here");
            task.performTask();
        } catch (Exception e) {
            log.error("Failed to start JOB CONTROLLER, caused by ", e);
        }

    }

}
