package org.acme.kafka;



import org.acme.service.device.Device;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface AvroMapper {

  void updateEntityFromDomain(Device domain, @MappingTarget AvroDevice entity);
}
