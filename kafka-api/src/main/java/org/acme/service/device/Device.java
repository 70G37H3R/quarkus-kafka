package org.acme.service.device;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Set;



@Data
public class Device {

    private Integer id;

    @NotEmpty
    private String name;

    @NotEmpty
    @Pattern(regexp="^(([0-9]|[1-9][0-9]|1[0-9]" +
            "{2}|2[0-4][0-9]|25[0-5])\\.)" +
            "{3}([0-9]|[1-9][0-9]|1[0-9]" +
            "{2}|2[0-4][0-9]|25[0-5])$",
            message="{invalid.ipAddress}")
    private String ipAddress;

    @NotEmpty
    @Pattern(regexp="^([0-9A-Fa-f]{2}[:-])" +
            "{5}([0-9A-Fa-f]{2})|" +
            "([0-9a-fA-F]{4}\\." +
            "[0-9a-fA-F]{4}\\." +
            "[0-9a-fA-F]{4})$",
            message="{invalid.macAddress}")
    private String macAddress;

    @NotEmpty
    private String status;

    @NotEmpty
    private String type;

    @NotEmpty
    private String version;





}
