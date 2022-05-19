/*
 * 版权属于：yitter(yitter@126.com)
 * 开源地址：https://github.com/yitter/idgenerator
 */
package com.hy.corecode.contract;

public interface IIdGenerator {
    long next() throws IdGeneratorException;
}
