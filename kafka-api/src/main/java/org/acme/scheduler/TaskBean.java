package org.acme.scheduler;


import io.quarkus.runtime.StartupEvent;
import lombok.extern.slf4j.Slf4j;
import org.acme.service.device.DeviceService;
import org.acme.service.setting.SettingService;
import org.quartz.*;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;


@ApplicationScoped
@Slf4j
public class TaskBean {

    @Inject
    DeviceService deviceService;

    @Inject
    SettingService settingService;

    @Inject
    org.quartz.Scheduler quartz;

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
            log.error("Failed to start job, caused by ", e);
        }
    }

    //return Device
    void performTask() {
        System.out.println("Cron expression configured in jobcontroller class");
        int lowerbound = 999999;
        int upperbound = 1000011;
        int deviceId = (int)Math.floor(Math.random()*(upperbound-lowerbound+1)+lowerbound);
        //get Device from deviceId


        //deviceService.updateStatus(deviceId);

    }


}