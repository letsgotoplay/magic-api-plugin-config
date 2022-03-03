package cn.luow.config.web;

import org.ssssssss.magicapi.core.config.MagicConfiguration;
import org.ssssssss.magicapi.core.web.MagicController;
import org.ssssssss.magicapi.core.web.MagicExceptionHandler;

public class MagicConfigController extends MagicController implements MagicExceptionHandler {
    public MagicConfigController(MagicConfiguration configuration) {
        super(configuration);
    }
}
