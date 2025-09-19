/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alibaba.csp.sentinel.dashboard.rule.nacos;

import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.*;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Properties;

/**
 * @author Eric Zhao
 * @since 1.4.0
 */
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(NacosConfig.class)
public class NacosConfiguration {


    /**
     * 授权规则实体编码器
     *
     * @return Converter
     */
    @Bean
    public Converter<List<GatewayFlowRule>, String> gatewayRuleEncoder() {
        return source -> JSON.toJSONString(source, true);
    }

    /**
     * 授权规则实体解码器
     *
     * @return Converter
     */
    @Bean
    public Converter<String, List<GatewayFlowRule>> gatewayRuleDecoder() {
        return s -> JSON.parseArray(s, GatewayFlowRule.class);
    }

    /**
     * 授权规则实体编码器
     *
     * @return Converter
     */
    @Bean
    public Converter<List<ApiDefinition>, String> apiRuleEncoder() {
        return source -> JSON.toJSONString(source, true);
    }

    /**
     * 授权规则实体解码器
     *
     * @return Converter
     */
    @Bean
    public Converter<String, List<ApiDefinition>> apiRuleDecoder() {
        return s -> JSON.parseArray(s, ApiDefinition.class);
    }


    /**
     * 授权规则实体编码器
     *
     * @return Converter
     */
    @Bean
    public Converter<List<AuthorityRule>, String> authorityRuleEncoder() {
        return source -> JSON.toJSONString(source, true);
    }

    /**
     * 授权规则实体解码器
     *
     * @return Converter
     */
    @Bean
    public Converter<String, List<AuthorityRule>> authorityRuleDecoder() {
        return s -> JSON.parseArray(s, AuthorityRule.class);
    }

    /**
     * 流控规则实体编码器
     *
     * @return Converter
     */
    @Bean
    public Converter<List<FlowRule>, String> flowRuleEncoder() {
        return source -> JSON.toJSONString(source, true);
    }

    /**
     * 流控规则实体解码器
     *
     * @return Converter
     */
    @Bean
    public Converter<String, List<FlowRule>> flowRuleDecoder() {
        return s -> JSON.parseArray(s, FlowRule.class);
    }

    /**
     * 降级规则实体编码器
     *
     * @return Converter
     */
    @Bean
    public Converter<List<DegradeRule>, String> degradeEncoder() {
        return source -> JSON.toJSONString(source, true);
    }

    /**
     * 降级规则实体解码器
     *
     * @return Converter
     */
    @Bean
    public Converter<String, List<DegradeRule>> degradeDecoder() {
        return s -> JSON.parseArray(s, DegradeRule.class);
    }

    /**
     * 热点参数规则实体编码器
     *
     * @return Converter
     */
    @Bean
    public Converter<List<ParamFlowRule>, String> paramFlowRuleEntityEncoder() {
        return source -> JSON.toJSONString(source, true);
    }

    /**
     * 热点参数规则实体解码器
     *
     * @return Converter
     */
    @Bean
    public Converter<String, List<ParamFlowRule>> paramFlowRuleEntityDecoder() {
        return s -> JSON.parseArray(s, ParamFlowRule.class);
    }

    /**
     * 系统参数规则实体编码器
     *
     * @return Converter
     */
    @Bean
    public Converter<List<SystemRule>, String> systemRuleEntityEncoder() {
        return source -> JSON.toJSONString(source, true);
    }

    /**
     * 系统参数规则实体解码器
     *
     * @return Converter
     */
    @Bean
    public Converter<String, List<SystemRule>> systemRuleEntityDecoder() {
        return s -> JSON.parseArray(s, SystemRule.class);
    }


    @Bean
    public ConfigService nacosConfigService(NacosConfig config) throws Exception {

        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, config.getServerAddr());
        properties.put(PropertyKeyConst.NAMESPACE, config.getNamespace());
        if (StringUtils.isNotBlank(config.getUsername()) &&
                StringUtils.isNotBlank(config.getPassword())) {
            properties.put(PropertyKeyConst.USERNAME, config.getUsername());
            properties.put(PropertyKeyConst.PASSWORD, config.getPassword());
        }

        if (StringUtils.isNotBlank(config.getSecretKey()) &&
                StringUtils.isNotBlank(config.getAccessKey())) {
            properties.put(PropertyKeyConst.SECRET_KEY, config.getSecretKey());
            properties.put(PropertyKeyConst.ACCESS_KEY, config.getAccessKey());
        }

        return ConfigFactory.createConfigService(properties);
    }
}
