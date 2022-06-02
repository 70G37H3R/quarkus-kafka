package org.acme.service.setting;


import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.acme.entity.SettingEntity;
import org.acme.exception.ServiceException;
import org.acme.repository.SettingRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ApplicationScoped
@AllArgsConstructor
@Slf4j
public class SettingService {

    @Inject
    SettingRepository settingRepository;

    @Inject
    SettingMapper settingMapper;

    public List<Setting> findAll() {
        log.info("Find all devices: {}");
        List<Setting> settingObject = this.settingMapper.toDomainList(settingRepository.findAll().list());
        if (Objects.isNull(settingObject)) {
            throw new ServiceException("Table setting is empty");
        } else {
            return settingObject;
        }
    }

    public Optional<Setting> findById(@NonNull Integer settingId) {
        log.info("Find setting by id: {}", settingId);
        Optional<Setting> settingObject = settingRepository.findByIdOptional(settingId).map(settingMapper::toDomain);
        if (settingObject.isEmpty()) {
            throw new ServiceException("Can not find setting for ID: ", settingId);
        } else {
            return settingObject;
        }
    }

    public int getPolling(@NonNull Integer settingId) {

        SettingEntity entity = settingRepository.findByIdOptional(settingId)
                .orElseThrow(() -> new ServiceException("No Setting found for ID[%s]", settingId));
        return entity.getPolling();
    }

    @Transactional
    public void save(@Valid Setting setting) {
        log.info("Create Setting: {}", setting);

        SettingEntity entity = settingMapper.toEntity(setting);
        settingRepository.persist(entity);
        settingMapper.updateDomainFromEntity(entity, setting);
    }

    public void validateId(@Valid Setting setting) {

        log.info("Validate ID: {}", setting.getId());
        if (Objects.isNull(setting.getId())) {
            throw new ServiceException("ID is null");
        }
    }

    @Transactional
    public void update(@NotNull @Valid Setting setting) {
        log.info("Updating Device: {}", setting);
        validateId(setting);
        SettingEntity entity = settingRepository.findByIdOptional(setting.getId())
                .orElseThrow(() -> new ServiceException("No Setting found for ID[%s]", setting.getId()));
        settingMapper.updateEntityFromDomain(setting, entity);
        settingRepository.persist(entity);
        settingMapper.updateDomainFromEntity(entity, setting);

    }

    @Transactional
    public void deleteAll() {
        log.debug("Deleting Setting: {}");
        settingRepository.deleteAll();
    }








}
