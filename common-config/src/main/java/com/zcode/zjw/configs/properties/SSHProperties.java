package com.zcode.zjw.configs.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SSHProperties {
    private String host;
    private Integer port;
    private String username;

    private String password;

    private Integer maxConnections;

    private Integer maxIdle;

    private Integer minIdel;

    private Integer incrementalConnections;

}
