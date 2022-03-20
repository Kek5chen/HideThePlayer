package me.kekschen.hidetheplayer.commands;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import me.kekschen.hidetheplayer.HideThePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public interface IUsernames {
    default void setUsername(Player player, String username) throws InvocationTargetException {
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        PlayerInfoData pid = new PlayerInfoData(WrappedGameProfile.fromPlayer(player), 1,
                EnumWrappers.NativeGameMode.SURVIVAL,
                WrappedChatComponent.fromText(""));
        PacketContainer packet = manager.createPacket(PacketType.Play.Server.PLAYER_INFO);
        packet.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
        packet.getPlayerInfoDataLists().write(0, Collections.singletonList(pid));

        Plugin plugin = HideThePlayer.getPlugin(HideThePlayer.class);
        for(Player p : Bukkit.getOnlinePlayers())
        {
            if(p.equals(player))
            {
                continue;
            }
            manager.sendServerPacket(p, packet);
            p.hidePlayer(plugin, player);
        }

        manager.addPacketListener(
                new PacketAdapter(plugin, PacketType.Play.Server.PLAYER_INFO)
                {
                    @Override
                    public void onPacketSending(PacketEvent event)
                    {

                        if(event.getPacket().getPlayerInfoAction().read(0) != EnumWrappers.PlayerInfoAction.ADD_PLAYER)
                        {
                            return;
                        }

                        PlayerInfoData pid = event.getPacket().getPlayerInfoDataLists().read(0).get(0);

                        if(pid.getProfile().getUUID() != player.getUniqueId())
                        {
                            return;
                        }

                        PlayerInfoData newPid = new PlayerInfoData(pid.getProfile().withName(username), pid.getPing(), pid.getGameMode(),
                                WrappedChatComponent.fromText(username));
                        event.getPacket().getPlayerInfoDataLists().write(0, Collections.singletonList(newPid));
                        manager.removePacketListener(this);
                    }
                }
        );

        for(Player p : Bukkit.getOnlinePlayers())
        {
            if(p.equals(player))
            {
                continue;
            }
            p.showPlayer(plugin, player);
        }

        packet = manager.createPacket(PacketType.Play.Server.PLAYER_INFO);
        packet.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.UPDATE_DISPLAY_NAME);
        pid = new PlayerInfoData(new WrappedGameProfile(player.getUniqueId(), player.getName()), 1,
                EnumWrappers.NativeGameMode.SURVIVAL,
                WrappedChatComponent.fromText(player.getName()));
        packet.getPlayerInfoDataLists().write(0, Collections.singletonList(pid));
        for(Player p : Bukkit.getOnlinePlayers()) {
            manager.sendServerPacket(p, packet);
        }
    }
}
