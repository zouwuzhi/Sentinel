package com.alibaba.csp.sentinel.dashboard.rule.nacos.gateway;

import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.ApiDefinitionEntity;
import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.GatewayFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.discovery.MachineInfo;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosConfigUtil;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosDatasourceAdapter;
import com.alibaba.csp.sentinel.datasource.Converter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component("gatewayApiRuleNacosProvider")
public class GatewayApiRuleNacosProvider extends NacosDatasourceAdapter<ApiDefinitionEntity> {
    @Resource
    private Converter<String, List<ApiDefinition>> converter;


    @Override
    protected List<ApiDefinitionEntity> deSerialize(String source, MachineInfo machine) {
        List<ApiDefinition> convert = converter.convert(source);

        return convert.stream()
                .map(rule ->
                        ApiDefinitionEntity.fromApiDefinition(machine.getApp(), machine.getIp(), machine.getPort(), rule))
                .collect(Collectors.toList());
    }

    @Override
    protected String getDataIdPostFix() {
        return NacosConfigUtil.GATEWAY_API_DATA_ID_POSTFIX;
    }
}
