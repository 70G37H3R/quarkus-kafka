package org.acme.service.device;
import org.acme.entity.DeviceEntity;
import org.acme.repository.DeviceRepository;
import org.acme.exception.ServiceException;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@ApplicationScoped
@AllArgsConstructor
@Slf4j
public class DeviceService {

    @Inject
    DeviceRepository deviceRepository;

    @Inject
    DeviceMapper deviceMapper;

    public List<Device> findAll() {
        log.info("Find all devices: {}");
        List<Device> deviceObject = this.deviceMapper.toDomainList(deviceRepository.findAll().list());
        if (Objects.isNull(deviceObject)) {
            throw new ServiceException("Table devices is empty");
        } else {
            return deviceObject;
        }
    }

    public Optional<Device> findById(@NonNull Integer deviceId) {
        log.info("Find device by id: {}", deviceId);
        Optional<Device> deviceObject = deviceRepository.findByIdOptional(deviceId).map(deviceMapper::toDomain);
        if (deviceObject.isEmpty()) {
            throw new ServiceException("Can not find device for ID: ", deviceId);
        } else {
            return deviceObject;
        }
    }

    public Optional<Device> findByMac(@NonNull String macAddress) {
        log.info("Finding Device By Mac Address: {}", macAddress);
        DeviceEntity entity = deviceRepository.find("macAddress", macAddress).firstResult();
        Optional<Device> deviceObject = Optional.ofNullable(entity).map(deviceMapper::toDomain);
        if (deviceObject.isEmpty()) {
            throw new ServiceException("Can not find device for MAC Address: ", macAddress);
        } else {
            return deviceObject;
        }
    }

    public void validateAddress(@Valid Device device) {
        log.info("Validate Mac address and IP address: {}", device.getMacAddress());
        DeviceEntity entityMac = deviceRepository.find("macAddress", device.getMacAddress()).firstResult();
        Optional<Device> deviceMac = Optional.ofNullable(entityMac).map(deviceMapper::toDomain);

        log.info("Validate Mac address and IP address: {}", device.getIpAddress());
        DeviceEntity entityIp = deviceRepository.find("ipAddress", device.getIpAddress()).firstResult();
        Optional<Device> deviceIp = Optional.ofNullable(entityIp).map(deviceMapper::toDomain);

        if ((deviceMac.isPresent()) && (deviceIp.isPresent())) {
            throw new ServiceException("IP address & MAC Address is already exists");
        }
    }

    public void validateUpdate(@Valid Device device) {
        this.validateId(device);
        this.validateAddress(device);
    }

    public void validateId(@Valid Device device) {

        log.info("Validate ID: {}", device.getId());
        if (Objects.isNull(device.getId())) {
            throw new ServiceException("ID is null");
        }
    }

    @Transactional
    public void save(@Valid Device device) {
        log.info("Create Device: {}", device);
        this.validateAddress(device);
        DeviceEntity entity = deviceMapper.toEntity(device);
        deviceRepository.persist(entity);
        deviceMapper.updateDomainFromEntity(entity, device);
    }

    @Transactional
    public void update(@NotNull @Valid Device device) {
        log.info("Updating Device: {}", device);
        this.validateId(device);
        DeviceEntity entity = deviceRepository.findByIdOptional(device.getId())
                    .orElseThrow(() -> new ServiceException("No Device found for ID[%s]", device.getId()));
        deviceMapper.updateEntityFromDomain(device, entity);
        deviceRepository.persist(entity);
        deviceMapper.updateDomainFromEntity(entity, device);

    }

    @Transactional
    public void updateStatus(@NonNull Integer deviceId) {
        log.info("Updating Device: {}", deviceId);

        DeviceEntity entity = deviceRepository.findByIdOptional(deviceId)
                .orElseThrow(() -> new ServiceException("No Device found for ID[%s]", deviceId));
        String[] arr={"UP", "DOWN", "WARNING"};
        Random r=new Random();
        int randomNumber=r.nextInt(arr.length);
        entity.setStatus(arr[randomNumber]);

        deviceRepository.persist(entity);
    }

    @Transactional
    public void deleteAll() {
        log.debug("Deleting Devices: {}");
        deviceRepository.deleteAll();
    }

}


