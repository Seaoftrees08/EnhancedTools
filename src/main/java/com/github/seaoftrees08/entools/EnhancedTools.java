package com.github.seaoftrees08.entools;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class EnhancedTools extends JavaPlugin {

    private static EnhancedTools instance;

    @Override
    public void onEnable() {

        Objects.requireNonNull(getCommand("enhancedtools")).setExecutor(new Commands());
        new PlayerListeners(this);

        instance = this;
    }

    @Override
    public void onDisable() {
    }

    public static EnhancedTools inst(){
        return instance;
    }
}
