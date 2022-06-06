package org.acme.kafka;



import org.acme.entity.DeviceEntity;
import org.acme.service.device.Device;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface AvroMapper {

  void updateEntityFromDomain(Device domain, @MappingTarget AvroDevice entity);

  void updateDomainFromEntity(AvroDevice entity, @MappingTarget Device domain);
}
