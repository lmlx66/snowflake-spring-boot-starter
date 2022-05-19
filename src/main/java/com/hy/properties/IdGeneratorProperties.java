package com.hy.properties;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.annotation.Order;

/**
 * @author: 王富贵
 * @description: 雪花算法基础配置类
 * @createTime: 2022年05月19日 11:19:59
 */
@Data
@RefreshScope
@ConfigurationProperties("yitter")
public class IdGeneratorProperties {
    /**
     * 雪花计算方法
     * （1-漂移算法|2-传统算法），默认1
     */
    private short Method = 1;

    /**
     * 是否是单机架构
     * 默认为true单体
     */
    private Boolean Monomer = Boolean.TRUE;

    /**
     * 基础时间（ms单位）
     * 不能超过当前系统时间
     * 默认为2022-01-01 00:00:00
     */
    private long BaseTime = 1640966400000L;

    /**
     * 机器码
     * 必须由外部设定，最大值 2^WorkerIdBitLength-1
     */
    private short WorkerId = 0;

    /**
     * 机器码位长
     * 默认值6，取值范围 [1, 15]（要求：序列数位长+机器码位长不超过22）
     */
    private byte WorkerIdBitLength = 6;

    /**
     * 序列数位长
     * 默认值6，取值范围 [3, 21]（要求：序列数位长+机器码位长不超过22）
     */
    private byte SeqBitLength = 6;

    /**
     * 最大序列数（含）
     * 设置范围 [MinSeqNumber, 2^SeqBitLength-1]，默认值0，表示最大序列数取最大值（2^SeqBitLength-1]）
     */
    private short MaxSeqNumber = 0;

    /**
     * 最小序列数（含）
     * 默认值5，取值范围 [5, MaxSeqNumber]，每毫秒的前5个序列数对应编号是0-4是保留位，其中1-4是时间回拨相应预留位，0是手工新值预留位
     */
    private short MinSeqNumber = 5;

    /**
     * 最大漂移次数（含）
     * 默认2000，推荐范围500-10000（与计算能力有关）
     */
    private short TopOverCostCount = 2000;

}
