package com.jd.o2o.vipcart.common.service.id;

/**
 * ID生成器
 * id生成组成结构 41bit(时间戳-第一位不用) + 10bit(工作机器id) + 12bit(序列号)
 * 1 41位的时间序列（精确到毫秒，41位的长度可以使用69年，最高位是符号位，始终为0）
 * 2 10位的机器标识（10位的长度最多支持部署1024个节点）
 * 3 12位的计数顺序号（12位的计数顺序号支持每个节点每毫秒产生4096个ID序号）
 * 优点：高性能，低延迟；独立的应用；按时间有序。 缺点：需要独立的开发和部署。
 * Created by liuhuiqing on 2017/4/20.
 */
public class SnowFlakeGenerator {
    private final long workerId;
    private final static long twepoch = 1288834974657L;
    private long sequence = 0L;
    private final static long workerIdBits = 10L; // 机器节点位数
    private final static long sequenceBits = 12L; // 序列号位数
    public final static long maxWorkerId = -1L ^ -1L << workerIdBits; // 1024台机器
    public final static long sequenceMask = -1L ^ -1L << sequenceBits; // 4096个序列号
    private final static long workerIdShift = sequenceBits;
    private final static long timestampLeftShift = sequenceBits + workerIdBits; // 时间戳需要左移22位
    private long lastTimestamp = -1L;

    public SnowFlakeGenerator(final long workerId) {
        if (workerId > this.maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format(
                    "workerId can't be greater than %d or less than 0",
                    this.maxWorkerId));
        }
        this.workerId = workerId;
    }

    public synchronized long nextId() {
        long timestamp = this.timeGen();
        if (this.lastTimestamp == timestamp) {
            this.sequence = (this.sequence + 1) & this.sequenceMask;
            // 超过序列号最大值，需要等下一个时间片再生成
            if (this.sequence == 0) {
                timestamp = this.tilNextMillis(this.lastTimestamp);
            }
        } else {
            this.sequence = 0;
        }
        if (timestamp < this.lastTimestamp) {
            try {
                throw new Exception(
                        String.format(
                                "Clock moved backwards. Refusing to generate id for %d milliseconds",
                                this.lastTimestamp - timestamp));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.lastTimestamp = timestamp;
        long nextId = ((timestamp - twepoch << timestampLeftShift))
                | (this.workerId << this.workerIdShift) | (this.sequence);
//        System.out.println("timestamp:" + timestamp + ",timestampLeftShift:"
//                + timestampLeftShift + ",nextId:" + nextId + ",workerId:"
//                + workerId + ",sequence:" + sequence);
        return nextId;
    }

    private long tilNextMillis(final long lastTimestamp) {
        long timestamp = this.timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = this.timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }


    public static void main(String[] args) {
        SnowFlakeGenerator worker2 = new SnowFlakeGenerator(1023);
        for (int i = 0; i < 100; i++) {
            System.out.println(worker2.nextId());
        }
    }
}
