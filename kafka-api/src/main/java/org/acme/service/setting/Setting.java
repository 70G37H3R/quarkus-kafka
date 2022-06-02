package org.acme.service.setting;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class Setting {

    private Integer id;

    private int timeout;

    private int retries;

    @NotEmpty
    private int polling;

}

