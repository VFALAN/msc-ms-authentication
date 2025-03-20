package com.msc.ms.authentification.log.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
public class LogPasswordResponse implements Serializable {
    private Date expirationDate;
    private String username;
}
