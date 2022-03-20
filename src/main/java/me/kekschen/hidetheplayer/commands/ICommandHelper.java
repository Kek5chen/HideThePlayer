package me.kekschen.hidetheplayer.commands;

import me.kekschen.hidetheplayer.HideThePlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface ICommandHelper {
    default boolean handlePermission(CommandSender sender, String permission) {
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(HideThePlayer.PREFIX + "§cYou don't have permission to use this command.");
            return false;
        }
        return true;
    }
    default Player handlePlayer(CommandSender sender, String name) {
        Player player = Bukkit.getPlayer(name);
        if (player == null) {
            sender.sendMessage(HideThePlayer.PREFIX + "§cPlayer not found.");
        }
        return player;
    }
}
