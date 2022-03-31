package me.kekschen.hidetheplayer.commands;

import me.kekschen.hidetheplayer.HideThePlayer;
import org.bukkit.command.CommandSender;

public interface IHidePlayerMessages {
    default void sendPlayerVisbilitySyntax(CommandSender sender) {
        sender.sendMessage(HideThePlayer.PREFIX + "§c/hidetheplayer player hide <player> - §7Hide the player.");
        sender.sendMessage(HideThePlayer.PREFIX + "§c/hidetheplayer player show <player> - §7Show the player.");
    }
    default void sendPlayerNameSyntax(CommandSender sender) {
        sender.sendMessage(HideThePlayer.PREFIX + "§c/hidetheplayer name show <player> - §7Show the name of the player.");
        sender.sendMessage(HideThePlayer.PREFIX + "§c/hidetheplayer name hide <player> - §7Hide the name of the player.");
        sender.sendMessage(HideThePlayer.PREFIX + "§c/hidetheplayer name change <player> <username> - §7Change the name of the player.");
    }
    default void sendPlayerSkinSyntax(CommandSender sender) {
        sender.sendMessage(HideThePlayer.PREFIX + "§c/hidetheplayer skin <username> - §7Change your skin to the one specified.");
        sender.sendMessage(HideThePlayer.PREFIX + "§c/hidetheplayer skin <player> <username> - §7Change the skin of another player to the one specified.");
    }
}
