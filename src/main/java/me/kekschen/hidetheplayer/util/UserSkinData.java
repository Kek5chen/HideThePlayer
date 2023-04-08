package me.kekschen.hidetheplayer.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class UserSkinData {
    private final String name;
    private final String value;
    private final String signature;

    private UserSkinData(final String name, final String value, final String signature) {
        this.name = name;
        this.value = value;
        this.signature = signature;
    }

    public static UserSkinData parseUserSkinData(String skinData) {
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(skinData).getAsJsonObject();
        try {
            final String name = json.get("username").getAsString();
            final String value = json.get("textures").getAsJsonObject().get("raw").getAsJsonObject().get("value").getAsString();
            final String signature = json.get("textures").getAsJsonObject().get("raw").getAsJsonObject().get("signature").getAsString();

            return new UserSkinData(name, value, signature);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getSignature() {
        return signature;
    }
}
