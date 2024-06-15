package com.zcode.zjw.schedule.quartz.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.stream.Stream;

/**
 * 获取系统IP地址工具类
 *
 * @author zhangjiwei
 * @since 2022年04月28日
 */
public class IPUtil {

    private static final Logger log = LoggerFactory.getLogger(IPUtil.class);

    /**
     * 获取ip
     *
     * @return
     */
    public static String getIP() {
        String netIP = null;
        String localIP = null;
        try {
            // 此类表示由名称组成的网络接口和分配给此接口的IP地址列表
            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
            boolean finded = false;
            while (enumeration.hasMoreElements()) {
                NetworkInterface networkInterface = enumeration.nextElement();
                Enumeration<InetAddress> addressEnumeration = networkInterface.getInetAddresses();
                while (addressEnumeration.hasMoreElements() && !finded) {
                    InetAddress inetAddress = addressEnumeration.nextElement();
                    // 检查InetAddress是否是站点本地地址的实用程序, 是否是回环地址
                    if (inetAddress.isSiteLocalAddress() && !inetAddress.isLoopbackAddress()
                            && inetAddress.getHostAddress().indexOf(":") == -1) {
                        localIP = inetAddress.getHostAddress();
                        finded = true;
                        break;
                    }
                    if (!inetAddress.isSiteLocalAddress() && !inetAddress.isLoopbackAddress()
                            && inetAddress.getHostAddress().indexOf(":") == -1) {
                        netIP = inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            log.error("获取IP异常", e);
        }
        return netIP != null && !"".equals(netIP) ? netIP : localIP;
    }

    /**
     * 判断ip集合是否有本机的ip
     *
     * @param ips
     * @return
     */
    public static boolean isRunnable(String ips) {
        if (StringUtils.isNotBlank(ips)) {
            return Stream.of(ips.split(",")).anyMatch(ip -> ip.equals(getIP()));
        }
        return false;
    }

}
