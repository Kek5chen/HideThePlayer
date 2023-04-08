package me.kekschen.hidetheplayer.util;

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

    private PlayerFetcher() {}

    public static Optional<UUID> getUUIDFromName(final String name) throws IOException {
        if (UUID_MAP.containsKey(name)) {
            return Optional.of(UUID_MAP.get(name));
        }

        final HttpURLConnection connection;
        try {
            connection = (HttpURLConnection) new URL(MOJANG_URL + name).openConnection();
        } catch (final MalformedURLException ignored) {
            return Optional.empty();
        }

        connection.setRequestMethod("GET");

        if (connection.getResponseCode() == 400 || connection.getResponseCode() == 404) {
            return Optional.empty();
        }

        // Only happens when the Mojang API breaks down
        if(connection.getResponseCode() != 200) {
            return Optional.empty();
        }

        final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        final JsonParser parser = new JsonParser();

        final JsonObject object = parser.parse(in).getAsJsonObject();
        in.close();

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
}
