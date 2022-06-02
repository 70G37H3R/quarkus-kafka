package org.acme.repository;


import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import org.acme.entity.SettingEntity;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SettingRepository implements PanacheRepositoryBase<SettingEntity, Integer> {
}
