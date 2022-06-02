package org.acme.service.setting;

import org.acme.entity.SettingEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface SettingMapper {

    List<Setting> toDomainList(List<SettingEntity> entities);

    Setting toDomain(SettingEntity entity);

    @InheritInverseConfiguration(name = "toDomain")
    SettingEntity toEntity(Setting domain);

    void updateEntityFromDomain(Setting domain, @MappingTarget SettingEntity entity);

    void updateDomainFromEntity(SettingEntity entity, @MappingTarget Setting domain);

}