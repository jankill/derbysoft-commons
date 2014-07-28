package com.derbysoft.common.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhupan panos.zhu@gmail.com
 * @version 1.8
 */
public abstract class IPUtils {

    private static final String DEFAULT_IP = "127.0.0.1";

    public static String getRemoteIP(HttpServletRequest req) {
        String ip = req.getHeader("X-Forwarded-For");
        if (StringUtils.isBlank(ip)) {
            return getRealIP(req);
        }
        String[] ips = StringUtils.split(ip, ',');
        if (ips != null) {
            for (String tmpIP : ips) {
                if (StringUtils.isBlank(tmpIP)) {
                    continue;
                }
                tmpIP = tmpIP.trim();
                if (isIP(tmpIP) && !tmpIP.startsWith("10.") && !tmpIP.startsWith("192.168.") && !"127.0.0.1".equals(tmpIP)) {
                    return tmpIP.trim();
                }
            }
        }
        return DEFAULT_IP;
    }

    private static String getRealIP(HttpServletRequest req) {
        String ip = req.getHeader("X-Real-IP");
        if (isIP(ip)) {
            return ip;
        }
        ip = req.getRemoteAddr();
        if (isIP(ip)) {
            return ip;
        }
        ip = req.getRemoteHost();
        if (isIP(ip)) {
            return ip;
        }
        return DEFAULT_IP;
    }

    public static boolean isIP(String address) {
        if (StringUtils.isEmpty(address)) {
            return false;
        }
        String[] ips = StringUtils.split(address, '.');
        if (ips.length != 4) {
            return false;
        }
        try {
            int ipa = Integer.parseInt(ips[0]);
            int ipb = Integer.parseInt(ips[1]);
            int ipc = Integer.parseInt(ips[2]);
            int ipd = Integer.parseInt(ips[3]);
            return ipa >= 0 && ipa <= 255 && ipb >= 0 && ipb <= 255 && ipc >= 0 && ipc <= 255 && ipd >= 0 && ipd <= 255;
        } catch (Exception e) {
            return false;
        }
    }

}
