package com.hy.corecode.core;


import com.hy.corecode.contract.IdGeneratorException;
import com.hy.properties.IdGeneratorOptions;

/**
 * @author: 王富贵
 * @description: 传统雪花漂移算法核心代码
 * @createTime: 2022年05月19日 11:19:59
 */
public class SnowWorkerM2 extends SnowWorkerM1 {

    //调用父类构造
    public SnowWorkerM2(IdGeneratorOptions options) {
        super(options);
    }

    @Override
    public long next() {
        synchronized (_SyncLock) {
            long currentTimeTick = GetCurrentTimeTick();

            //如果最后一次生成与当前时间相同
            if (_LastTimeTick == currentTimeTick) {
                //如果当前使用到的序列号已经大于最大序列号，就是用预留的插
                if (_CurrentSeqNumber++ > MaxSeqNumber) {
                    _CurrentSeqNumber = MinSeqNumber;
                    currentTimeTick = GetNextTimeTick();
                }
            } else {
                _CurrentSeqNumber = MinSeqNumber;
            }

            //如果发生了时间回拨
            if (currentTimeTick < _LastTimeTick) {
                throw new IdGeneratorException("Time error for {0} milliseconds", _LastTimeTick - currentTimeTick);
            }

            _LastTimeTick = currentTimeTick;

            //位移并返回
            return ShiftStitchingResult(currentTimeTick);
        }

    }
}
