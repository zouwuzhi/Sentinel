package com.alibaba.csp.sentinel.dashboard.rule.nacos.gateway;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.gateway.ApiDefinitionEntity;
import com.alibaba.csp.sentinel.dashboard.discovery.MachineInfo;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosConfigUtil;
import com.alibaba.csp.sentinel.dashboard.rule.nacos.NacosDatasourceAdapter;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("gatewayApiRuleNacosProvider")
public class GatewayApiRuleNacosProvider extends NacosDatasourceAdapter<ApiDefinitionEntity> {


    @Override
    protected List<ApiDefinitionEntity> deSerialize(String source, MachineInfo machine) {

        List<ApiDefinitionEntity> entities = JSON.parseArray(source, ApiDefinitionEntity.class);
        if (entities != null) {
            for (ApiDefinitionEntity entity : entities) {
                entity.setApp(machine.getApp());
                entity.setIp(machine.getIp());
                entity.setPort(machine.getPort());
            }
        }

        return entities;
    }

    @Override
    protected String getDataIdPostFix() {
        return NacosConfigUtil.GATEWAY_API_DATA_ID_POSTFIX;
    }
}
