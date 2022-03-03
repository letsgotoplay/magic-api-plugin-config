package cn.luow.config.starter;

import cn.luow.config.service.ConfigMagicDynamicRegistry;
import cn.luow.config.service.ConfigMagicResourceStorage;
import cn.luow.config.service.EnvFunctionExtension;
import cn.luow.config.web.MagicConfigController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.ssssssss.magicapi.core.config.MagicPluginConfiguration;
import org.ssssssss.magicapi.core.model.Plugin;
import org.ssssssss.magicapi.core.web.MagicControllerRegister;


public class MagicAPIConfConfiguration  implements MagicPluginConfiguration {


    @Override
    public Plugin plugin() {
        return new Plugin("配置文件", "MagicConfig","magic-config.1.0.0.iife.js");
    }


    @Override
    public MagicControllerRegister controllerRegister() {
        return (mapping, configuration) -> mapping.registerController(new MagicConfigController(configuration));
    }
    @Bean
    @ConditionalOnMissingBean
    public ConfigMagicResourceStorage configMagicResourceStorage() {
        return new ConfigMagicResourceStorage();
    }
    @Bean
    @ConditionalOnMissingBean
    public ConfigMagicDynamicRegistry configMagicDynamicRegistry(ConfigMagicResourceStorage configMagicResourceStorage) {

        return new ConfigMagicDynamicRegistry(configMagicResourceStorage);
    }
    @Bean
    public EnvFunctionExtension envFunctionExtension() {

        return new EnvFunctionExtension();
    }
}
