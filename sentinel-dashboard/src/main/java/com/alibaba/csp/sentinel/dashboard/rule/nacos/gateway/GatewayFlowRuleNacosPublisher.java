package com.alibaba.csp.sentinel.dashboard.rule.nacos.gateway;

import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.GatewayFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosConfigUtil;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosDatasourceAdapter;
import com.alibaba.csp.sentinel.datasource.Converter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component("gatewayFlowRuleNacosPublisher")
public class GatewayFlowRuleNacosPublisher extends NacosDatasourceAdapter<GatewayFlowRuleEntity> {
    @Resource
    private Converter<List<GatewayFlowRule>, String> converter;


    @Override
    protected String serialize(List<GatewayFlowRuleEntity> rules) {
        List<GatewayFlowRule> collect = rules.stream().map(GatewayFlowRuleEntity::toGatewayFlowRule).collect(Collectors.toList());
        return converter.convert(collect);
    }

    @Override
    protected String getDataIdPostFix() {
        return NacosConfigUtil.GATEWAY_FLOW_DATA_ID_POSTFIX;
    }
}
