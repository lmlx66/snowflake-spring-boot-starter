package com.hy.corecode.idgen;


import com.hy.corecode.contract.IIdGenerator;
import com.hy.corecode.contract.ISnowWorker;
import com.hy.corecode.contract.IdGeneratorException;
import com.hy.properties.IdGeneratorOptions;
import com.hy.corecode.core.SnowWorkerM1;
import com.hy.corecode.core.SnowWorkerM2;

/**
 * @author: 王富贵
 * @description: 构建配置类，检查参数是否合法，根据参数构建算法生成器
 * @createTime: 2022年05月19日 11:19:59
 */
public class YitIdGenerator implements IIdGenerator {
    private static ISnowWorker _SnowWorker = null;

    /**
     * 构造函数，检查参数是否都合法
     *
     * @throws IdGeneratorException
     */
    public YitIdGenerator(IdGeneratorOptions options) throws IdGeneratorException {
        // 1.BaseTime
        if (options.BaseTime < 315504000000L || options.BaseTime > System.currentTimeMillis()) {
            throw new IdGeneratorException("BaseTime error.");
        }

        // 2.WorkerIdBitLength
        if (options.WorkerIdBitLength <= 0) {
            throw new IdGeneratorException("WorkerIdBitLength error.(range:[1, 21])");
        }
        if (options.WorkerIdBitLength + options.SeqBitLength + options.DataCenterIdBitLength > 22) {
            throw new IdGeneratorException("error：WorkerIdBitLength + SeqBitLength + DataCenterIdBitLength <= 22");
        }

        // 3.WorkerId
        int maxWorkerIdNumber = (1 << options.WorkerIdBitLength) - 1;
        if (maxWorkerIdNumber == 0) {
            maxWorkerIdNumber = 63;
        }
        if (options.WorkerId < 0 || options.WorkerId > maxWorkerIdNumber) {
            throw new IdGeneratorException("WorkerId error. (range:[0, " + (maxWorkerIdNumber > 0 ? maxWorkerIdNumber : 63) + "]");
        }

        // 4. DataCenterId
        int maxDataCenterId = (1 << options.DataCenterIdBitLength) - 1;
        if (options.DataCenterId < 0 || options.DataCenterId > maxDataCenterId){
            throw new IdGeneratorException("DataCenterId error. (range:[0,"+ maxDataCenterId + "])");
        }


        // 5.SeqBitLength
        if (options.SeqBitLength < 2 || options.SeqBitLength > 21) {
            throw new IdGeneratorException("SeqBitLength error. (range:[2, 21])");
        }

        // 6.MaxSeqNumber
        int maxSeqNumber = (1 << options.SeqBitLength) - 1;
        if (maxSeqNumber == 0) {
            maxSeqNumber = 63;
        }
        if (options.MaxSeqNumber < 0 || options.MaxSeqNumber > maxSeqNumber) {
            throw new IdGeneratorException("MaxSeqNumber error. (range:[1, " + maxSeqNumber + "]");
        }

        // 7.MinSeqNumber
        if (options.MinSeqNumber < 5 || options.MinSeqNumber > maxSeqNumber) {
            throw new IdGeneratorException("MinSeqNumber error. (range:[5, " + maxSeqNumber + "]");
        }

        //判断是构建雪花漂移算法还是普通雪花算法
        switch (options.Method) {
            case 2:
                _SnowWorker = new SnowWorkerM2(options);
                break;
            case 1:
            default:
                _SnowWorker = new SnowWorkerM1(options);
                break;
        }

        if (options.Method == 1) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public long next() {
        return _SnowWorker.next();
    }
}
