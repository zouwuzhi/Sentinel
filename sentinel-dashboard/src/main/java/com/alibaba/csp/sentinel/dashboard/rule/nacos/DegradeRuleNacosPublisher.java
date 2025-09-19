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

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.DegradeRuleEntity;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Eric Zhao
 * @since 1.4.0
 */
@Component("degradeRuleNacosPublisher")
public class DegradeRuleNacosPublisher extends NacosDatasourceAdapter<DegradeRuleEntity> {

    @Resource
    private Converter<List<DegradeRule>, String> converter;

    @Override
    protected String serialize(List<DegradeRuleEntity> rules) {
        return converter.convert(rules.stream().map(DegradeRuleEntity::toRule).collect(Collectors.toList()));
    }

    @Override
    protected String getDataIdPostFix() {
        return NacosConfigUtil.DEGRADE_DATA_ID_POSTFIX;
    }
}