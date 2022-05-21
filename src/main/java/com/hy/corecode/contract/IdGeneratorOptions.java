/*
 * 版权属于：yitter(yitter@126.com)
 * 开源地址：https://github.com/yitter/idgenerator
 */
package com.hy.corecode.contract;

import com.hy.properties.IdGeneratorProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: 王富贵
 * @description: 雪花算法配置参数
 * @createTime: 2022年05月19日 11:19:59
 * 雪花算法使用的参数
 * 参数说明，参考 README.md 的 “配置参数” 章节。
 */
@Data
@NoArgsConstructor
@ApiModel("雪花算法配置信息实体")
public class IdGeneratorOptions {

    /**
     * 雪花计算方法
     * （1-漂移算法|2-传统算法），默认1
     */
    @ApiModelProperty(value = "雪花计算方法", notes = "（1-漂移算法|2-传统算法），默认1")
    public short Method = 1;

    /**
     * 基础时间（ms单位）
     * 不能超过当前系统时间
     * 默认为2022-01-01 00:00:00
     */
    @ApiModelProperty(value = "基础时间（ms单位）", notes = "默认为2022-01-01 00:00:00，不能超过当前系统时间")
    public long BaseTime = 1640966400000L;

    /**
     * 机器码
     * 必须由外部设定，最大值 2^WorkerIdBitLength-1
     */
    @ApiModelProperty(value = "机器码", notes = "该实例机器码，必须唯一，必须由外部设定，最大值 2^WorkerIdBitLength-1")
    public short WorkerId = 0;

    /**
     * 机器码位长
     * 默认值6，取值范围 [1, 15]（要求：序列数位长+机器码位长不超过22）
     */
    @ApiModelProperty(value = "机器码位长", notes = "决定项目集群能使用id最大机器数， 默认值6，取值范围 [1, 15]（要求：序列数位长+机器码位长不超过22）")
    public byte WorkerIdBitLength = 6;

    /**
     * 序列数位长
     * 默认值6，取值范围 [3, 21]（要求：序列数位长+机器码位长不超过22）
     */
    @ApiModelProperty(value = "序列数位长", notes = "决定一毫秒能生成的最大id数，如果超过会阻塞，默认值6，取值范围 [3, 21]（要求：序列数位长+机器码位长不超过22）")
    public byte SeqBitLength = 6;

    /**
     * 最大序列数（含）
     * 设置范围 [MinSeqNumber, 2^SeqBitLength-1]，默认值0，表示最大序列数取最大值（2^SeqBitLength-1]）
     */
    @ApiModelProperty(value = " 最大序列数（含）", notes = "设置范围 [MinSeqNumber, 2^SeqBitLength-1]，默认值0，表示最大序列数取最大值（2^SeqBitLength-1]）")
    public short MaxSeqNumber = 0;

    /**
     * 最小序列数（含）
     * 默认值5，取值范围 [5, MaxSeqNumber]，每毫秒的前5个序列数对应编号是0-4是保留位，其中1-4是时间回拨相应预留位，0是手工新值预留位
     */
    @ApiModelProperty(value = "最小序列数（含）", notes = "默认值5，取值范围 [5, MaxSeqNumber]，每毫秒的前5个序列数对应编号是0-4是保留位，其中1-4是时间回拨相应预留位，0是手工新值预留位")
    public short MinSeqNumber = 5;

    /**
     * 最大漂移次数（含）
     * 默认2000，推荐范围500-10000（与计算能力有关）
     */
    @ApiModelProperty(value = "最大漂移次数（含）", notes = "默认2000，推荐范围500-10000（与计算能力有关）")
    public short TopOverCostCount = 2000;

    public IdGeneratorOptions(short workerId) {
        WorkerId = workerId;
    }
}
