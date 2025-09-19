package com.alibaba.csp.sentinel.dashboard.rule.nacos;

import com.alibaba.csp.sentinel.dashboard.discovery.AppManagement;
import com.alibaba.csp.sentinel.dashboard.discovery.MachineInfo;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRulePublisher;
import com.alibaba.csp.sentinel.util.AssertUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.nacos.api.config.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public abstract class NacosDatasourceAdapter<T> implements DynamicRuleProvider<List<T>>, DynamicRulePublisher<List<T>> {
    private final Logger logger = LoggerFactory.getLogger(NacosDatasourceAdapter.class);
    @Resource
    private ConfigService configService;
    @Resource
    private NacosConfig config;
    @Resource
    private AppManagement appManagement;


    protected Optional<MachineInfo> getMachines(String appName) {
        if (StringUtil.isBlank(appName)) {
            return Optional.empty();
        }
        List<MachineInfo> list = appManagement.getDetailApp(appName).getMachines()
                .stream()
                .filter(MachineInfo::isHealthy)
                .sorted((e1, e2) -> Long.compare(e2.getLastHeartbeat(), e1.getLastHeartbeat())).collect(Collectors.toList());
        if (list.isEmpty()) {
            return Optional.empty();
        } else {
            MachineInfo machine = list.get(0);
            return Optional.of(machine);
        }
    }


    @Override
    public List<T> getRules(String appName) throws Exception {
        AssertUtil.notEmpty(appName, "app name cannot be empty");

        String dataId = getDataId(appName);
        String groupId = config.getGroupId();
        String rules = configService.getConfig(dataId, groupId, 3000);
        if (!StringUtil.isEmpty(rules)) {
            return getMachines(appName)
                    .map(machine -> deSerialize(rules, machine))
                    .orElse(Collections.emptyList());
        }
        return Collections.emptyList();
    }

    @Override
    public void publish(String appName, List<T> rules) throws Exception {
        AssertUtil.notEmpty(appName, "app name cannot be empty");
        if (rules == null) {
            return;
        }
        String dataId = getDataId(appName);
        String groupId = config.getGroupId();
        configService.publishConfig(dataId, groupId, serialize(rules), "JSON");
    }

    protected List<T> deSerialize(String source, MachineInfo machine) {
        throw new UnsupportedOperationException();
    }

    protected String serialize(List<T> rules) {
        throw new UnsupportedOperationException();
    }

    protected abstract String getDataIdPostFix();

    private String getDataId(String appName) {
        return appName + (getDataIdPostFix());
    }

}
