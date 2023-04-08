package me.kekschen.hidetheplayer.commands;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import me.kekschen.hidetheplayer.HideThePlayer;
import me.kekschen.hidetheplayer.util.UserSkinData;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Optional;

public interface ISkinInterchangeable {

    default void changePlayerSkin(CommandSender requester, Player player, String skinName) {
        final Optional<UserSkinData> wrappedSkinData = UserSkinData.getUserSkinData(skinName);

		if (!wrappedSkinData.isPresent()) {
			player.sendMessage(HideThePlayer.PREFIX + "§cDer Spieler §e" + skinName + " §ckonnte nicht gefunden werden.");
			return;
		}

		final UserSkinData userSkinData = wrappedSkinData.get();

        WrappedGameProfile profile = WrappedGameProfile.fromPlayer(player);
        profile.getProperties().remove("textures", profile.getProperties().get("textures").iterator().next());
        profile.getProperties().put("textures", new WrappedSignedProperty("textures", userSkinData.getValue(), userSkinData.getSignature()));
        HideThePlayer plugin = HideThePlayer.getPlugin(HideThePlayer.class);
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    p.hidePlayer(plugin, player);
                    p.showPlayer(plugin, player);
                }
                requester.sendMessage(HideThePlayer.PREFIX + "§aSuccessfully changed " +
                        (requester.getName().equals(player.getName()) ? "your" : "§e" + player.getName() + "§as") +
                        " skin to §e" + skinName + "§as skin.");
            }
        }.runTask(plugin);
    }

    default void changePlayerSkinAsync(CommandSender requester, Player player, String skinName) {
        requester.sendMessage(HideThePlayer.PREFIX + "§eLoading skin data...");
        new BukkitRunnable() {
            @Override
            public void run() {
                changePlayerSkin(requester, player, skinName);
            }
        }.runTaskAsynchronously(HideThePlayer.getPlugin(HideThePlayer.class));
    }
}
