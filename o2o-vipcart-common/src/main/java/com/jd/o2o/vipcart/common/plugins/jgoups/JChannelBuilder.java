package com.jd.o2o.vipcart.common.plugins.jgoups;

import com.jd.o2o.vipcart.common.domain.exception.BaseMsgException;
import com.jd.o2o.vipcart.common.domain.exception.O2OException;
import org.apache.commons.lang.math.NumberUtils;
import org.jgroups.JChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 通道协议构造器
 * Created by liuhuiqing on 2016/5/27.
 */
public class JChannelBuilder {
    private static final Logger LOGGER = LoggerFactory.getLogger(JChannelBuilder.class);
    private static Integer tcpPort = 7800;
//    private static Integer udpPort = 45588;

    /**
     * 通过xml配置文件创建通道
     * @param xmlFileName
     * @return
     */
    public static JChannel buildChannelByFile(String xmlFileName) {
        InputStream inputStream = loadResource(xmlFileName);
        if (inputStream == null) {
            String msg = "JChannel创建找不到文件"+xmlFileName;
            LOGGER.warn(msg);
            throw new O2OException(msg);
        }
        try {
            return new JChannel(inputStream);
        } catch (Exception e) {
            String msg =String.format("通过资源文件[%s]，创建jgroup通道出现异常", xmlFileName);
            LOGGER.warn(msg, e);
            throw new O2OException(msg);
        }
    }

    /**
     * 使用默认配置创建TCP通道
     * 集群成员不使用多播总线，而是会创建一个网状的TCP连接。
     * 比如，在将一条消息发生给一个具有10个成员的集群时，UDP只需要发送一条IP多播数据报，而TCP需要将该消息发送9次。TCP将该消息发送给第一个成员、第二个成员、等等 (不包括它自己，因为该消息会在内部进行环回)
     * TCP发送一条集群消息的开销比UDP的开销要大，利用TCP发送一条集群消息的开销和集群大小成正比
     *
     * 好处是可以集群可夸多个子网
     * @param initialHosts
     * @return
     */
    public static JChannel buildTCPChannel(String initialHosts) {
        try {
            return new JChannel(getDefaultTCPConfig(initialHosts));
        } catch (Exception e) {
            throw new O2OException("JChannel创建失败", e);
        }
    }

    /**
     * 使用默认配置创建UDP通道
     * UDP使用IP多播将消息发送给集群中的所有成员，使用UDP datagrams 发送单播消息 (发给单个成员)。在启动UDP协议时，它会打开一个单播和多播socket：单播socket用于发送/接收单播消息，而多播socket用于发送/接收多播消息。信道的物理地址是单播socket的地址和端口号。
     * 使用UDP作为传输协议协议栈通常用于成员属于相同子网的集群。如果跨越多个子网运行，那么管理员必须确保IP多播能够跨越这些子网。通常会出现禁止IP多播跨越多个子网的情况。在这种情况下，协议栈必须使用不带IP多播功能的UDP或者其它传输协议，比如TCP。
     * @return
     */
    public static JChannel buildUDPChannel() {
        try {
            return buildChannelByFile("jgroup/udp.xml");
//            return new JChannel(getDefaultUDPConfig());
        } catch (Exception e) {
            throw new BaseMsgException("JChannel创建失败" + e.getMessage());
        }
    }

//    private static String getDefaultUDPConfig() {
//        StringBuilder PROPS = new StringBuilder();
//        PROPS.append("UDP(mcast_port=").append(udpPort).append(";ip_ttl=4;tos=8;ucast_recv_buf_size=5M;ucast_send_buf_size=5M;mcast_recv_buf_size=5M;mcast_send_buf_size=5M;max_bundle_size=64K;max_bundle_timeout=30;enable_diagnostics=true;thread_naming_pattern=cl;timer_type=new3;timer.min_threads=2;timer.max_threads=4;timer.keep_alive_time=3000;timer.queue_max_size=500;thread_pool.enabled=true;thread_pool.min_threads=2;thread_pool.max_threads=8;thread_pool.keep_alive_time=5000;thread_pool.queue_enabled=true;thread_pool.queue_max_size=10000;thread_pool.rejection_policy=discard;oob_thread_pool.enabled=true;oob_thread_pool.min_threads=1;oob_thread_pool.max_threads=8;oob_thread_pool.keep_alive_time=5000;oob_thread_pool.queue_enabled=false;oob_thread_pool.queue_max_size=100;oob_thread_pool.rejection_policy=discard);")
//                .append("PING;")
//                .append("MERGE3(max_interval=30000;min_interval=10000);")
//                .append("FD_SOCK;")
//                .append("FD_ALL;")
//                .append("VERIFY_SUSPECT(timeout=1500);")
//                .append("BARRIER;")
//                .append("pbcast.NAKACK2(xmit_interval=500;xmit_table_num_rows=100;xmit_table_msgs_per_row=2000;xmit_table_max_compaction_time=30000;max_msg_batch_size=500;use_mcast_xmit=false;discard_delivered_msgs=true);")
//                .append("UNICAST3(xmit_interval=500;xmit_table_num_rows=100;xmit_table_msgs_per_row=2000;xmit_table_max_compaction_time=60000;conn_expiry_timeout=0;max_msg_batch_size=500);")
//                .append("pbcast.STABLE(stability_delay=1000;desired_avg_gossip=50000;max_bytes=4M);")
//                .append("pbcast.GMS(print_local_addr=true;join_timeout=2000;view_bundling=true);")
//                .append("UFC(max_credits=2M;min_threshold=0.4);")
//                .append("MFC(max_credits=2M;min_threshold=0.4);")
//                .append("FRAG2(frag_size=60K);")
//                .append("RSVP(resend_interval=2000;timeout=10000);")
//                .append("pbcast.STATE_TRANSFER;");
//        return PROPS.toString();
//    }

    private static String getDefaultTCPConfig(String initialHosts) {
        StringBuilder PROPS = new StringBuilder();
        //protocol TCP
        String[] ports = initialHosts.split("[\\[\\]]+");
        Integer port = tcpPort;
        if(ports.length>1){
            port = NumberUtils.toInt(ports[1], tcpPort);
        }

        PROPS.append("TCP(bind_addr=").append(getIP()).append(";bind_port=").append(port)
                .append(";loopback=loopback=false;recv_buf_size=5M;send_buf_size=640K;max_bundle_size=64K;max_bundle_timeout=30;use_send_queues=true;sock_conn_timeout=300")
                .append(";timer_type=new3;timer.min_threads=4;timer.max_threads=10;timer.keep_alive_time=3000;timer.queue_max_size=500")
                .append(";thread_pool.enabled=true;thread_pool.min_threads=1;thread_pool.max_threads=10;thread_pool.keep_alive_time=5000;thread_pool.queue_enabled=false;thread_pool.queue_max_size=100;thread_pool.rejection_policy=discard")
                .append(";oob_thread_pool.enabled=true;oob_thread_pool.min_threads=1;oob_thread_pool.max_threads=8;oob_thread_pool.keep_alive_time=5000;oob_thread_pool.queue_enabled=false;oob_thread_pool.queue_max_size=100; oob_thread_pool.rejection_policy=discard):");
        //protocol TCPPING
        PROPS.append("TCPPING(initial_hosts=").append(initialHosts).append(";timeout=3000;port_range=1;num_initial_members=10):");
        //protocol MERGE2
        PROPS.append("MERGE2(min_interval=10000;max_interval=30000):");
        //protocol FD_SOCK
        PROPS.append("FD_SOCK:");
        //protocol FD
        PROPS.append("FD(timeout=3000;max_tries=3):");
        //protocol VERIFY_SUSPECT
        PROPS.append("VERIFY_SUSPECT(timeout=1500):");
        //protocol BARRIER
        PROPS.append("BARRIER:");
        //protocol NAKACK2
        PROPS.append("pbcast.NAKACK2(use_mcast_xmit=false;discard_delivered_msgs=true):");
        //protocol UNICAST3
        PROPS.append("UNICAST3:");
        PROPS.append("RSVP:");
        //protocol STABLE
        PROPS.append("pbcast.STABLE(stability_delay=1000;desired_avg_gossip=50000; max_bytes=4M):");
        //protocol GMS
        PROPS.append("pbcast.GMS(print_local_addr=true;join_timeout=3000;view_bundling=true):");
        //protocol MFC
        PROPS.append("MFC(max_credits=2M;min_threshold=0.4):");
        //protocol FRAG2
        PROPS.append("FRAG2(frag_size=60K):");
        //protocol STATE_TRANSFER
        PROPS.append("pbcast.STATE_TRANSFER");
        return PROPS.toString();
    }

    private static String getIP() {
        Enumeration<NetworkInterface> netInterfaces;
        try {
            netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = netInterfaces.nextElement();
                Enumeration<InetAddress> nii = ni.getInetAddresses();
                while (nii.hasMoreElements()) {
                    ip = nii.nextElement();
                    if (ip.isSiteLocalAddress()
                            && !ip.isLoopbackAddress()
                            && ip.getHostAddress().indexOf(":") == -1) {
                        return ip.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            LOGGER.error("getIP error", e);
            throw new O2OException("get ip error", e);
        }
        return null;
    }

    private static InputStream loadResource(String fileName) {
        return ClassLoader.getSystemClassLoader().getResourceAsStream(fileName);
    }

    public static void main(String[] args) {
        File file = new File(ClassLoader.getSystemClassLoader().getResource("").getPath());
        for (File file1 : file.listFiles())
            System.err.print(file1.getName());
    }

}
