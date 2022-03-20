package me.kekschen.hidetheplayer.commands;

import me.kekschen.hidetheplayer.HideThePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public interface IHidePlayers {
    default boolean isHidden(Player watcher, Player subject) {
        return watcher.canSee(subject);
    }
    default void hidePlayer(Player p) {
        Plugin plugin = HideThePlayer.getPlugin(HideThePlayer.class);
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.hidePlayer(plugin, p);
        }
    }
    default void showPlayer(Player p) {
        Plugin plugin = HideThePlayer.getPlugin(HideThePlayer.class);
        for(Player player : Bukkit.getOnlinePlayers()) {
            player.showPlayer(plugin, p);
        }
    }
    default void hideAllPlayers() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            hidePlayer(player);
        }
    }
    default void showAllPlayers() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            showPlayer(player);
        }
    }
}
