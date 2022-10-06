package com.aurora.util;

import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.lionsoul.ip2region.Util;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@Component
public class IpUtil {

    public static String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Real-IP");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("x-forwarded-for");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if ("127.0.0.1".equals(ipAddress) || "0:0:0:0:0:0:0:1".equals(ipAddress)) {
                //根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    log.error("getIpAddress exception:", e);
                }
                assert inet != null;
                ipAddress = inet.getHostAddress();
            }
        }
        return StringUtils.substringBefore(ipAddress, ",");
    }

    private static DbSearcher searcher;
    private static Method method;

    /**
     * 在服务启动时加载 ip2region.db 到内存中
     */
    @PostConstruct
    private void initIp2regionResource() throws Exception {
        InputStream inputStream = new ClassPathResource("/ip/ip2region.db").getInputStream();
        //将 ip2region.db 转为 ByteArray
        byte[] dbBinStr = FileCopyUtils.copyToByteArray(inputStream);
        DbConfig dbConfig = new DbConfig();
        searcher = new DbSearcher(dbConfig, dbBinStr);
        //二进制方式初始化 DBSearcher，需要使用基于内存的查找算法 memorySearch
        method = searcher.getClass().getMethod("memorySearch", String.class);
    }

    /**
     * 获取ip地址的归属地
     */
    public static String getIpSource(String ipAddress) {
        if (ipAddress == null || !Util.isIpAddress(ipAddress)) {
            log.error("Error: Invalid ip address");
            return "";
        }
        try {
            DataBlock dataBlock = (DataBlock) method.invoke(searcher, ipAddress);
            String ipInfo = dataBlock.getRegion();
            if (!StringUtils.isEmpty(ipInfo)) {
                ipInfo = ipInfo.replace("|0", "");
                ipInfo = ipInfo.replace("0|", "");
                return ipInfo;
            }
        } catch (Exception e) {
            log.error("getCityInfo exception:", e);
        }
        return "";
    }

    public static String getIpProvince(String ipSource) {
        String[] strings = ipSource.split("\\|");
        if (strings[1].endsWith("省")) {
            return StringUtils.substringBefore(strings[1], "省");
        }
        return strings[1];
    }

    /**
     * 获取访问设备
     */
    public static UserAgent getUserAgent(HttpServletRequest request) {
        return UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
    }

}
