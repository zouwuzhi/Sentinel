package com.alibaba.csp.sentinel.dashboard.rule.nacos.gateway;

import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.GatewayFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.discovery.MachineInfo;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosConfigUtil;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosDatasourceAdapter;
import com.alibaba.csp.sentinel.datasource.Converter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component("gatewayFlowRuleNacosProvider")
public class GatewayFlowRuleNacosProvider extends NacosDatasourceAdapter<GatewayFlowRuleEntity> {
    @Resource
    private Converter<String, List<GatewayFlowRule>> converter;


    @Override
    protected List<GatewayFlowRuleEntity> deSerialize(String source, MachineInfo machine) {
        List<GatewayFlowRule> convert = converter.convert(source);

        return convert.stream()
                .map(rule ->
                        GatewayFlowRuleEntity.fromGatewayFlowRule(machine.getApp(), machine.getIp(), machine.getPort(), rule))
                .collect(Collectors.toList());
    }

    @Override
    protected String getDataIdPostFix() {
        return NacosConfigUtil.GATEWAY_FLOW_DATA_ID_POSTFIX;
    }
}
