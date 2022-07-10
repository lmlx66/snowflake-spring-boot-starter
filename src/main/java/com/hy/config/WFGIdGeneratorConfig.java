package com.hy.config;

import com.hy.corecode.idgen.WFGIdGenerator;
import com.hy.properties.IdGeneratorOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;


/**
 * @author: 王富贵
 * @description: id生成自动配置类
 * @createTime: 2022年05月19日 11:11:27
 */
@EnableConfigurationProperties({IdGeneratorOptions.class})
public class WFGIdGeneratorConfig {

    @Autowired
    private IdGeneratorOptions idGeneratorOptions;

    /**
     * 自动装配的配置
     *
     * @return DefaultIdGenerator
     */
    @Bean()
    @ConditionalOnMissingBean(WFGIdGenerator.class)//用户没有注入自己的bean才装配
    public WFGIdGenerator getDefaultIdGenerator() {
        return new WFGIdGenerator(idGeneratorOptions);
    }
}