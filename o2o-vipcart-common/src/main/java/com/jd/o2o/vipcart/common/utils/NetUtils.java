package com.jd.o2o.vipcart.common.utils;

import org.apache.commons.lang.StringUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 *  网络工具类
 * Created by liuhuiqing on 2017/4/24.
 */
public class NetUtils {
    public static String getIP() {
        String localIP = null;// 本地IP，如果没有配置外网IP则返回它
        String netIP = null;// 外网IP
        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            boolean finded = false;// 是否找到外网IP
            while (netInterfaces.hasMoreElements() && !finded) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> address = ni.getInetAddresses();
                while (address.hasMoreElements()) {
                    ip = address.nextElement();
                    if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {// 外网IP
                        netIP = ip.getHostAddress();
                        finded = true;
                        break;
                    } else if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {// 内网IP
                        localIP = ip.getHostAddress();
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (StringUtils.isNotEmpty(netIP)) {
            return netIP;
        } else {
            return localIP;
        }
    }

    public static void main(String[] args) throws Exception {
        System.err.println(getIP());
    }
}
