package org.acme.repository;
import org.acme.entity.DeviceEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DeviceRepository implements PanacheRepositoryBase<DeviceEntity, Integer> {

}
