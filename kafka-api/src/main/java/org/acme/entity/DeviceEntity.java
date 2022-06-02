package org.acme.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Entity(name = "Device")
@Table(name = "device")
@Data
public class DeviceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    @NotEmpty
    private String name;

    @Column(name = "ipAddress")
    @NotEmpty
    @Pattern(regexp="^(([0-9]|[1-9][0-9]|1[0-9]" +
            "{2}|2[0-4][0-9]|25[0-5])\\.)" +
            "{3}([0-9]|[1-9][0-9]|1[0-9]" +
            "{2}|2[0-4][0-9]|25[0-5])$",
            message="{invalid.ipAddress}")
    private String ipAddress;


    @Column(name = "macAddress")
    @NotEmpty
    @Pattern(regexp="^([0-9A-Fa-f]{2}[:-])" +
            "{5}([0-9A-Fa-f]{2})|" +
            "([0-9a-fA-F]{4}\\." +
            "[0-9a-fA-F]{4}\\." +
            "[0-9a-fA-F]{4})$",
            message="{invalid.macAddress}")
    private String macAddress;

    @Column(name = "status")
    @NotEmpty
    private String status;


    @Column(name = "type")
    @NotEmpty
    private String type;


    @Column(name = "version")
    @NotEmpty
    private String version;



}
