package cn.luow.config.service;

import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.stereotype.Component;
import org.ssssssss.magicapi.modules.spring.EnvModule;
import org.ssssssss.script.annotation.Comment;
import org.ssssssss.script.functions.ExtensionMethod;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Component
public class EnvFunctionExtension implements ExtensionMethod {
    public final static String envKey = "MAGIC_DEFAULT_ENV";
    private static Map<String,Object> envMap = new HashMap<>();
    @Resource
    private ContextRefresher contextRefresher;
    @Resource
    private ConfigurableEnvironment environment;
    @Override
    public Class<?> support() {
        return EnvModule.class;
    }

    @Comment("加入环境变量")
    public void set(EnvModule env ,@Comment(name="key",value="键") String key, @Comment(name="key",value="值") String value){
        envMap.put(key,value);
        MapPropertySource propertySource = new MapPropertySource(envKey, envMap);
        environment.getPropertySources().addFirst(propertySource);
    }

    @Comment("加入yaml格式环境变量")
    public void setYaml(EnvModule env , @Comment(name="yaml",value="yaml文本")String yaml, @Comment(name="propName",value = "自定义的配置文件名") String propName){
        Yaml yamlObj = new Yaml();
        Map map = this.getPropMap(yamlObj.load(yaml));
        MapPropertySource propertySource = new MapPropertySource(propName, map);
        environment.getPropertySources().addFirst(propertySource);
    }
    @Comment("获取配置文件列表")
    public MutablePropertySources getPropertySources(EnvModule env ){
        return environment.getPropertySources();
    }

    @Comment("删除默认的自定义配置文件内容（即通过set函数配置的键值对）")
    public Map empty(EnvModule env ){
         environment.getPropertySources().remove(envKey);
         Map<String,Object> map = envMap;
         envMap = new HashMap<>();
         return map;
    }
    @Comment("删除指定配置文件内容")
    public void empty(EnvModule env,String propName){
        environment.getPropertySources().remove(propName);
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
