package me.kekschen.hidetheplayer;

import me.kekschen.hidetheplayer.commands.HideThePlayerCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class HideThePlayer extends JavaPlugin {

    public static final String PREFIX = "§8[§5HideThePlayer§8] §r";

    @Override
    public void onEnable() {
        Bukkit.getPluginCommand("hidetheplayer").setExecutor(new HideThePlayerCommand());
    }

    @Override
    public void onDisable() {

    }
}
