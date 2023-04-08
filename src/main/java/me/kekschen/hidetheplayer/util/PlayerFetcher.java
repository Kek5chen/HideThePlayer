package me.kekschen.hidetheplayer.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class PlayerFetcher {
    private static final Map<String, UUID> UUID_MAP = new HashMap<>();

    private static final String MOJANG_URL = "https://api.mojang.com/users/profiles/minecraft/";
    private static final String SKIN_URL_START = "https://sessionserver.mojang.com/session/minecraft/profile/";
    private static final String SKIN_URL_END = "?unsigned=false";

    private PlayerFetcher() {}

    public static Optional<UUID> getUUIDFromName(final String name) {
        if (UUID_MAP.containsKey(name)) {
            return Optional.of(UUID_MAP.get(name));
        }

        final JsonObject object;
        try {
            object = getJSONData(MOJANG_URL + name);
        } catch (final IOException exception) {
            return Optional.empty();
        }

        if (object == null) {
            return Optional.empty();
        }

        final UUID uuid;
        try {
            final String uuidString = object.get("id").getAsString()
                    .replaceFirst(
                            "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)",
                            "$1-$2-$3-$4-$5"
                    );

            uuid = UUID.fromString(uuidString);
        } catch (final IllegalArgumentException e) {
            return Optional.empty();
        }

        UUID_MAP.put(name, uuid);

        return Optional.of(uuid);
    }

    private static JsonObject getJSONData(final String url) throws IOException {
        final HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) new URL(url).openConnection();
        } catch (final MalformedURLException ignored) {
            return null;
        }

        connection.setRequestMethod("GET");

        if (connection.getResponseCode() == 400 || connection.getResponseCode() == 404) {
            return null;
        }

        // Only happens when the Mojang API breaks down
        if(connection.getResponseCode() != 200) {
            return null;
        }

        final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        final JsonParser parser = new JsonParser();

        final JsonObject object = parser.parse(in).getAsJsonObject();
        in.close();

        return object;
    }

    public static Optional<UserSkinData> getUserSkin(final UUID uuid) {
        final JsonObject object;
        try {
            object = getJSONData(SKIN_URL_START + uuid.toString().replace("-", "") + SKIN_URL_END);
        } catch (final IOException e) {
            return Optional.empty();
        }

        if (object == null) {
            return Optional.empty();
        }

        final String name = object.get("name").getAsString();

        final JsonArray properties = object.get("properties").getAsJsonArray();
        for (final JsonElement property : properties) {
            final JsonObject jsonObject = property.getAsJsonObject();
            if(!jsonObject.get("name").getAsString().equals("textures")) {
                continue;
            }

            final String value = jsonObject.get("value").getAsString();
            final String signature = jsonObject.get("signature").getAsString();

            return Optional.of(new UserSkinData(name, value, signature));
        }

        return Optional.empty();
    }
}
