package com.msc.ms.authentification.log.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LogPassHistoryRequest {
private Integer idUser;
private String password;
}
