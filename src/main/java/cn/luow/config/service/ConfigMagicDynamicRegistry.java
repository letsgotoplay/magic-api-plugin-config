package cn.luow.config.service;

import cn.luow.config.model.ConfigInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.ssssssss.magicapi.core.config.MagicConfiguration;
import org.ssssssss.magicapi.core.event.FileEvent;
import org.ssssssss.magicapi.core.event.GroupEvent;
import org.ssssssss.magicapi.core.service.AbstractMagicDynamicRegistry;
import org.ssssssss.magicapi.core.service.MagicResourceStorage;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ConfigMagicDynamicRegistry extends AbstractMagicDynamicRegistry<ConfigInfo> {

    @Resource
    private ContextRefresher contextRefresher;
    @Resource
    private ConfigurableEnvironment environment;

    public ConfigMagicDynamicRegistry(MagicResourceStorage magicResourceStorage) {
        super(magicResourceStorage);
    }

    @EventListener(condition = "#event.type == 'config'")
    public void onFileEvent(FileEvent event) {
        processEvent(event);
    }

    @EventListener(condition = "#event.type == 'config'")
    public void onGroupEvent(GroupEvent event) {
        processEvent(event);
    }

    @Override
    public boolean register(ConfigInfo entity) {
        unregister(entity);
        return super.register(entity);
    }

    @Override
    protected boolean register(MappingNode<ConfigInfo> mappingNode) {
        ConfigInfo info = mappingNode.getEntity();
        String scriptName = MagicConfiguration.getMagicResourceService().getScriptName(info);
        String groupPath = MagicConfiguration.getMagicResourceService().getGroupPath(info.getGroupId()).replace("_", "");
        String path = info.getPath().replace("_", "");
        String propName = String.format("%s_%s", groupPath, path);
        if (info.isEnabled()) {
            try {
                Yaml yaml = new Yaml();
                String yamlStr = info.getScript();
                Map map = this.getPropMap(yaml.load(yamlStr));
                MapPropertySource propertySource = new MapPropertySource(propName, map);
                environment.getPropertySources().addFirst(propertySource);
                contextRefresher.refresh();
            } catch (Exception e) {
                log.error("配置文件加载出错", e);
            } finally {
                log.info("配置文件:[{}]加载完毕", scriptName);
            }
        }else{
            unregister(mappingNode);
        }
        return true;
    }

    @Override
    protected void unregister(MappingNode<ConfigInfo> mappingNode) {
        ConfigInfo info = mappingNode.getEntity();
        log.debug("取消注册配置文件:[{}, {}]", info.getName(), info.getPath());
        String groupPath = MagicConfiguration.getMagicResourceService().getGroupPath(info.getGroupId()).replace("_","");
        String path = info.getPath().replace("_","");
        String propName = String.format("%s_%s", groupPath,path);
        PropertySource propertySource = environment.getPropertySources().get(propName);
        if(propertySource!=null){
            environment.getPropertySources().remove(propName);
        }
        contextRefresher.refresh();
    }



    private Map<String,Object> getPropMap(Map<String,Object> yamlMap){
        Map<String,Object> propMap = new HashMap();
        for (Map.Entry entry: yamlMap.entrySet()) {
            String key = (String)entry.getKey();
            Object value = entry.getValue();
            if(value instanceof Map){
                Map<String ,Object> childProMap = getPropMap((Map<String, Object>) value);
                for (Map.Entry e:childProMap.entrySet()) {
                    propMap.put(key+"."+e.getKey(),e.getValue());
                }
            }else{
                propMap.put(key,value);
            }
        }
        return propMap;
    }
}
