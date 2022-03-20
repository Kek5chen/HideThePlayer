package me.kekschen.hidetheplayer.commands;

import me.kekschen.hidetheplayer.HideThePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class HideThePlayerCommand implements CommandExecutor, IHidePlayers, IUsernames, IHidePlayerMessages, ICommandHelper {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!handlePermission(sender, "rwm.hidetheplayer")) return true;
        if (args.length == 0) {
            sendPlayerVisbilitySyntax(sender);
            sendPlayerNameSyntax(sender);
            return true;
        }
        else if(args.length == 1) {
            if (args[0].equalsIgnoreCase("player")) sendPlayerVisbilitySyntax(sender);
            else if(args[0].equalsIgnoreCase("name"))  sendPlayerNameSyntax(sender);
        }
        else if(args.length == 2) {
            if (args[0].equalsIgnoreCase("player")) sendPlayerVisbilitySyntax(sender);
            else if(args[0].equalsIgnoreCase("name")) sendPlayerNameSyntax(sender);
        } else if(args.length == 3) {
            Player p;
            if((p = handlePlayer(sender, args[2])) == null) return true;
            if (args[0].equalsIgnoreCase("player")) {
                if (args[1].equalsIgnoreCase("hide")) {
                    if (args[2].equalsIgnoreCase("*")) {
                        hideAllPlayers();
                        sender.sendMessage(HideThePlayer.PREFIX + "§aAll players are now hidden.");
                    }
                    else {
                        hidePlayer(p);
                        sender.sendMessage(HideThePlayer.PREFIX + "§aPlayer §e" + p.getName() + "§a is now hidden.");
                    }
                } else if (args[1].equalsIgnoreCase("show")) {
                    if (args[2].equalsIgnoreCase("*")) {
                        showAllPlayers();
                        sender.sendMessage(HideThePlayer.PREFIX + "§aAll players are now shown.");
                    }
                    else {
                        showPlayer(p);
                        sender.sendMessage(HideThePlayer.PREFIX + "§aPlayer §e" + p.getName() + "§a is now shown.");
                    }
                } else sendPlayerVisbilitySyntax(sender);
            } else if(args[0].equalsIgnoreCase("name")) {
                if (args[1].equalsIgnoreCase("hide")) {
                    try {
                        setUsername(p, "");
                        sender.sendMessage(HideThePlayer.PREFIX + "§aPlayer §e" + p.getName() + "§a's name is now hidden.");
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                } else if (args[1].equalsIgnoreCase("show")) {
                    try {
                        setUsername(p, p.getName());
                        sender.sendMessage(HideThePlayer.PREFIX + "§aPlayer §e" + p.getName() + "§a's name is now shown.");
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                } else {
                    sendPlayerNameSyntax(sender);
                }
            }
        } else if(args.length == 4) {
            Player p;
            if((p = handlePlayer(sender, args[2])) == null) return true;
            if(args[0].equalsIgnoreCase("name")) {
                if(args[1].equalsIgnoreCase("change")) {
                    try {
                        setUsername(p, args[3]);
                        sender.sendMessage(HideThePlayer.PREFIX + "§aPlayer §e" + p.getName() + "§a's name is now §e" + args[3] + "§a.");
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                sendPlayerNameSyntax(sender);
            }
        }
        return true;
    }
}
