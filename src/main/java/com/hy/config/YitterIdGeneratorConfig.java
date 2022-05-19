package com.hy.config;

import com.hy.corecode.idgen.YitIdGenerator;
import com.hy.properties.IdGeneratorProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;


/**
 * @author: 王富贵
 * @description: id生成自动配置类
 * @createTime: 2022年05月19日 11:11:27
 */
@EnableConfigurationProperties({IdGeneratorProperties.class})
public class YitterIdGeneratorConfig {

    @Autowired
    private IdGeneratorProperties idGeneratorProperties;

    /**
     * 自动装配的配置
     *
     * @return DefaultIdGenerator
     */
    @Bean()
    @ConditionalOnMissingBean(YitIdGenerator.class)//用户没有注入自己的bean才装配
    public YitIdGenerator getDefaultIdGenerator() {
        return new YitIdGenerator(idGeneratorProperties);
    }

}
