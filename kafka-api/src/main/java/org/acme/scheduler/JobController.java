package org.acme.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.acme.kafka.AvroDevice;
import org.acme.service.device.DeviceService;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Path;

@Slf4j
@Path("/job")
@ApplicationScoped
public class JobController implements Job {

    @Inject
    DeviceService deviceService;

    @Channel("demo")
    Emitter<AvroDevice> emitter;

    public void execute(JobExecutionContext context)  {
        try {
            log.info("Job Scheduler Here");
            publicMsg();
        } catch (Exception e) {
            log.error("Failed to start JOB CONTROLLER, caused by ", e);
        }

    }

     public void publicMsg() {
        AvroDevice avroDevice = buildRandomDevice();
        emitter.send(avroDevice);
    }

    public AvroDevice buildRandomDevice() {
        System.out.println("Cron expression configured in jobcontroller class");
        log.info("Building custom device info");
        //random device ID
//        int lowerbound = 999999;
//        int upperbound = 1000011;
//        int deviceId = (int)Math.floor(Math.random()*(upperbound-lowerbound+1)+lowerbound);
        int deviceId = 1000000;
        AvroDevice avroDevice =  deviceService.buildCustomDeviceInfo(deviceId);
//        AvroDevice avroDevice = new AvroDevice();
//        avroDevice.setIpAddress("1.1.1.1");
//        avroDevice.setMacAddress("mac");

        return avroDevice;
    }

}
