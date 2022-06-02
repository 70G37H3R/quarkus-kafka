package org.acme.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;


@Entity(name = "Setting")
@Table(name = "setting")
@Data
public class SettingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "timeout")
    private int timeout;

    @Column(name = "retries")
    private int retries;

    @Column(name = "polling")
    @NotEmpty
    private int polling;

}
