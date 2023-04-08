package me.kekschen.hidetheplayer.commands;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.kekschen.hidetheplayer.HideThePlayer;
import me.kekschen.hidetheplayer.util.UserSkinData;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

public interface ISkinInterchangeable {
	HashMap<String, UserSkinData> skinDataCache = new HashMap<>();
	default void changePlayerSkin(CommandSender requester, Player player, String skinName) {
		// send get request to https://api.ashcon.app/mojang/v2/user/{skinName} and parse the body
		UserSkinData userSkinData = skinDataCache.getOrDefault(skinName, null);
		if(userSkinData == null) {
			URL url;
			try {
				// url encode the skin name
				url = new URL("https://api.ashcon.app/mojang/v2/user/" + URLEncoder.encode(skinName, "UTF-8"));
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");
				if (conn.getResponseCode() != 200) {
					if (conn.getResponseCode() == 404) {
						player.sendMessage(HideThePlayer.PREFIX + "§cDer Spieler §e" + skinName + " §ckonnte nicht gefunden werden.");
						return;
					}
					throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
				}
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				String output;
				StringBuilder sb = new StringBuilder();
				while ((output = br.readLine()) != null) {
					sb.append(output);
				}
				conn.disconnect();
				userSkinData = UserSkinData.parseUserSkinData(sb.toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (userSkinData == null) {
				requester.sendMessage(HideThePlayer.PREFIX + "§cThe player §e" + skinName + "§c does not exist.");
				return;
			}
			skinDataCache.put(skinName, userSkinData);
		}

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
