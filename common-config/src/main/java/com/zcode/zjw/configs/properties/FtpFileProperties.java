package com.zcode.zjw.configs.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FtpFileProperties {
    private String host;
    private Integer port;
    private String username;

    private String password;

    private Integer poolEvictInterval;

}
