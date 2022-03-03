package cn.luow.config.service;

import cn.luow.config.model.ConfigInfo;
import org.ssssssss.magicapi.core.service.AbstractPathMagicResourceStorage;

public class ConfigMagicResourceStorage extends AbstractPathMagicResourceStorage<ConfigInfo> {
    @Override
    public String folder() {
        return "config";
    }

    @Override
    public Class<ConfigInfo> magicClass() {
        return ConfigInfo.class;
    }

    @Override
    public void validate(ConfigInfo entity) {
    }

    @Override
    public String buildMappingKey(ConfigInfo info) {
        return buildMappingKey(info, magicResourceService.getGroupPath(info.getGroupId()));
    }
}
