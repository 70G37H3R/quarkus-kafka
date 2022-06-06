package org.acme.channel.incoming;

import io.smallrye.reactive.messaging.kafka.Record;
import lombok.extern.slf4j.Slf4j;
import org.acme.kafka.AvroDevice;
import org.acme.kafka.AvroMapper;
import org.acme.service.device.Device;
import org.acme.service.device.DeviceService;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@Slf4j
@ApplicationScoped
public class IncomingChannel {

    @Inject
    AvroMapper avroMapper;

    @Inject
    DeviceService deviceService;

    @Incoming("demo-from-kafka")
    @Transactional
    public void consumeMsg(Record<String, AvroDevice> record) {
        AvroDevice avroDevice = record.value();
        log.info("Got a device from Kafka {} Key => [" + record.key() +"]" + "{} Values => [" + avroDevice.toString() +"]");
        try {

            Device deviceObject = new Device();
            avroMapper.updateDomainFromEntity(avroDevice, deviceObject);
            log.info("Test deviceObject {}" + deviceObject);
//        deviceService.findById(deviceObject.getId());
            deviceService.updateStatus(deviceObject);
        }
        catch (Exception e) {
            log.error("Error for receiving msg from kafka, caused by", e);
        }

    }
}
