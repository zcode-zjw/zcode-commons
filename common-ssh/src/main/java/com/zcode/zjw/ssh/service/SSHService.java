package com.zcode.zjw.ssh.service;

import com.zcode.zjw.configs.common.ZcodeConfig;
import com.zcode.zjw.configs.config.ZcodeConfigurationSelector;
import com.zcode.zjw.configs.properties.SSHProperties;
import com.zcode.zjw.ssh.utils.CmdUtil;
import com.zcode.zjw.ssh.utils.SSHUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author zhangjiwei
 * @description SSH连接工具
 * @date 2023/2/5 下午1:12
 */
@Slf4j
@Service("sshService")
@Scope(value = "prototype")
public class SSHService extends SSHUtil {

    private ZcodeConfig zcodeConfig;

    @Resource
    private ZcodeConfigurationSelector zcodeConfigurationSelector;

    private boolean initFlag = false;

    //@PostConstruct
    @Override
    public void initSSHInfo() {
        zcodeConfig = zcodeConfigurationSelector.getZcodeConfig();
        SSHProperties ssh = zcodeConfig.getSsh();
        ip = ssh.getHost();
        port = ssh.getPort();
        username = ssh.getUsername();
        password = ssh.getPassword();
        Optional.ofNullable(ssh.getMaxConnections()).ifPresent(ssh::setMaxConnections);
        Optional.ofNullable(ssh.getMaxIdle()).ifPresent(ssh::setMaxConnections);
        Optional.ofNullable(ssh.getMinIdel()).ifPresent(ssh::setMaxConnections);
        Optional.ofNullable(ssh.getIncrementalConnections()).ifPresent(ssh::setMaxConnections);
    }

    @Override
    public String execCmd(String command) throws Exception {
        if (!initFlag) {
            initSSHInfo();
        }
        if ("local".equals(zcodeConfig.getNodeType())) {
            return CmdUtil.executeOnLinux(command);
        }
        return super.execCmd(command);
    }

    @Override
    public String execCmdAndClose(String command) {
        if (!initFlag) {
            initSSHInfo();
        }
        String shellCmdPrefix = "";
        if (!zcodeConfig.getNodeType().equals("local")) {
            shellCmdPrefix = "source /etc/profile && source ~/.bash_profile && ";
        }
        return super.execCmdAndClose(shellCmdPrefix + command);
    }

    @Override
    public String execCmdByShell(String... commands) throws Exception {
        if (!initFlag) {
            initSSHInfo();
        }
        if ("local".equals(zcodeConfig.getNodeType())) {
            StringBuilder tmpString = new StringBuilder();
            for (String cmd : commands) {
                tmpString.append(CmdUtil.executeOnLinux(cmd));
            }
            return tmpString.toString();
        }
        return super.execCmdByShell(commands);
    }

}
