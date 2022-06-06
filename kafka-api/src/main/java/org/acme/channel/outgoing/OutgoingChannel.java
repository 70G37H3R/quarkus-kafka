package org.acme.channel.outgoing;

import io.smallrye.reactive.messaging.kafka.api.OutgoingKafkaRecordMetadata;
import lombok.extern.slf4j.Slf4j;
import org.acme.kafka.AvroDevice;
import org.acme.service.device.DeviceService;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.OnOverflow;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Slf4j
@ApplicationScoped
public class OutgoingChannel {

    @Inject
    @OnOverflow(value = OnOverflow.Strategy.BUFFER, bufferSize = 1000)
    @Channel("demo")
    Emitter<AvroDevice> emitter;

    public void produceMsg(AvroDevice avroDevice) {
        log.info("Send a device to Kafka {}");
        OutgoingKafkaRecordMetadata<?> metadata = OutgoingKafkaRecordMetadata.builder()
                //.withTopic("manage_devices_event")
                .withKey(avroDevice.getMacAddress())
                .build();
        emitter.send(Message.of(avroDevice).addMetadata(metadata));
    }

}
