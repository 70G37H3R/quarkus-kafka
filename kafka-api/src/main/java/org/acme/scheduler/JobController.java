package org.acme.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.acme.kafka.AvroDevice;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
@Slf4j
@AllArgsConstructor
@Path("/job")
public class JobController implements Job {
    @Inject
    TaskBean taskBean;

    @Channel("demo")
    Emitter<AvroDevice> emitter;

    public void execute(JobExecutionContext context) throws JobExecutionException {
        taskBean.performTask();
    }

    @POST
    @Path("/emitter")
    public Response enqueueDevice(@NotNull AvroDevice device) {
        log.info("Sending device demo %s to Kafka", device.getId());
        emitter.send(device);
        return Response.accepted().build();
    }

}
