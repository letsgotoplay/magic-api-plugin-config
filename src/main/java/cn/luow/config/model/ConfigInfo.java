package cn.luow.config.model;

import org.ssssssss.magicapi.core.config.MagicConfiguration;
import org.ssssssss.magicapi.core.model.MagicEntity;
import org.ssssssss.magicapi.core.model.PathMagicEntity;

public class ConfigInfo extends PathMagicEntity {

    /**
     * 是否启用
     */
    private boolean enabled;
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    @Override
    public MagicEntity simple() {
        ConfigInfo info = new ConfigInfo();
        super.simple(info);
        return info;
    }
    @Override
    public ConfigInfo copy() {
        ConfigInfo info = new ConfigInfo();
        super.copyTo(info);
        info.setEnabled(this.enabled);
        return info;
    }

}
