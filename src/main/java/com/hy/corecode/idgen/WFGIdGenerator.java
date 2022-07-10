package com.hy.corecode.idgen;

import com.hy.corecode.contract.IdGeneratorException;
import com.hy.properties.IdGeneratorOptions;

/**
 * @author: 王富贵
 * @description: 继承YitIdGenerator,实际上就是搞个别名
 * @createTime: 2022年07月10日 19:54:03
 */
public class WFGIdGenerator extends YitIdGenerator{

    /**
     * 构造函数，检查参数是否都合法
     *
     * @param options
     * @throws IdGeneratorException
     */
    public WFGIdGenerator(IdGeneratorOptions options) throws IdGeneratorException {
        super(options);
    }
}
