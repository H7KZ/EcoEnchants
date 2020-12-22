package com.willfp.eco.util.bukkit.logging;

import com.willfp.eco.util.StringUtils;
import com.willfp.eco.util.injection.PluginDependent;
import com.willfp.eco.util.plugin.AbstractEcoPlugin;

public class EcoLogger extends PluginDependent implements Logger {
    public EcoLogger(AbstractEcoPlugin plugin) {
        super(plugin);
    }

    @Override
    public void info(String message) {
        this.getPlugin().getLogger().info(StringUtils.translate(message));
    }

    @Override
    public void warn(String message) {
        this.getPlugin().getLogger().warning(StringUtils.translate(message));
    }

    @Override
    public void error(String message) {
        this.getPlugin().getLogger().severe(StringUtils.translate(message));
    }
}
