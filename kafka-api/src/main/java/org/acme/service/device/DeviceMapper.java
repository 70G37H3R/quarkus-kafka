package org.acme.service.device;
import org.acme.entity.DeviceEntity;

import org.acme.service.device.Device;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import java.util.List;

@Mapper(componentModel = "cdi")
public interface DeviceMapper {

    List<Device> toDomainList(List<DeviceEntity> entities);

    Device toDomain(DeviceEntity entity);

    @InheritInverseConfiguration(name = "toDomain")
    DeviceEntity toEntity(Device domain);

    void updateEntityFromDomain(Device domain, @MappingTarget DeviceEntity entity);

    void updateDomainFromEntity(DeviceEntity entity, @MappingTarget Device domain);

}
