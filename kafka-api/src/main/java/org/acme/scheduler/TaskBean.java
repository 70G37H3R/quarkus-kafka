package org.acme.scheduler;

import io.quarkus.runtime.StartupEvent;
import lombok.extern.slf4j.Slf4j;
import org.acme.channel.outgoing.OutgoingChannel;
import org.acme.kafka.AvroDevice;
import org.acme.service.device.DeviceService;
import org.acme.service.setting.SettingService;
import org.quartz.*;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@Slf4j
@ApplicationScoped
public class TaskBean {

    @Inject
    DeviceService deviceService;

    @Inject
    SettingService settingService;

    @Inject
    org.quartz.Scheduler quartz;

    @Inject
    OutgoingChannel outgoingChannel;

    public void onStart(@Observes StartupEvent event) {
        try {
            //Get polling attribute of Setting class
            int polling = settingService.getPolling(10);
            log.info("Polling = " +  polling);
            JobDetail job = JobBuilder.newJob(JobController.class)
                    .withIdentity("myJob", "myGroup")
                    .build();
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("myTrigger", "myGroup")
                    .startNow()
                    .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInSeconds(polling)
                            .repeatForever()
                    )
                    .build();
            quartz.scheduleJob(job, trigger);
        } catch (Exception e) {
            log.error("Failed to start TASK BEAN, caused by ", e);
        }
    }

    public void performTask(){
        outgoingChannel.produceMsg(this.createMsg());
    }

    public AvroDevice createMsg() {
        System.out.println("Cron expression configured in jobcontroller class");
        log.info("mapping device info into kafka topic");
        //random device ID
        int lowerbound = 1000000;
        int upperbound = 1000009;
        int deviceId = (int)Math.floor(Math.random()*(upperbound-lowerbound+1)+lowerbound);
//        int deviceId = 1000000;
        AvroDevice avroDevice =  deviceService.createAvroContent(deviceId);
        return avroDevice;
    }





}