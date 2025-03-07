package com.msc.ms.authentification.log.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class LogPasswordResponse {
    private Date expirationDate;
    private String username;
}
