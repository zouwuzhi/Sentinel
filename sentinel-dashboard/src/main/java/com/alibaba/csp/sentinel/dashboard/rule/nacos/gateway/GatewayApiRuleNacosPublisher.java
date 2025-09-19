package com.alibaba.csp.sentinel.dashboard.rule.nacos.gateway;

import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.ApiDefinitionEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.GatewayFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosConfigUtil;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosDatasourceAdapter;
import com.alibaba.csp.sentinel.datasource.Converter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component("gatewayApiRuleNacosPublisher")
public class GatewayApiRuleNacosPublisher extends NacosDatasourceAdapter<ApiDefinitionEntity> {
    @Resource
    private Converter<List<ApiDefinition>, String> converter;


    @Override
    protected String serialize(List<ApiDefinitionEntity> rules) {
        List<ApiDefinition> collect = rules.stream().map(ApiDefinitionEntity::toApiDefinition).collect(Collectors.toList());
        return converter.convert(collect);
    }

    @Override
    protected String getDataIdPostFix() {
        return NacosConfigUtil.GATEWAY_API_DATA_ID_POSTFIX;
    }
}
