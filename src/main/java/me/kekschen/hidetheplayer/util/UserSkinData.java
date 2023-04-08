package me.kekschen.hidetheplayer.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class UserSkinData {
    private static final Map<UUID, Optional<UserSkinData>> SKIN_CACHE = new HashMap<>();

    private final String name;
    private final String value;
    private final String signature;

    UserSkinData(final String name, final String value, final String signature) {
        this.name = name;
        this.value = value;
        this.signature = signature;
    }

    public static Optional<UserSkinData> getUserSkinData(final String name) {
        final Optional<UUID> uuid = PlayerFetcher.getUUIDFromName(name);

        return uuid.flatMap(UserSkinData::getUserSkinData);
    }

    public static Optional<UserSkinData> getUserSkinData(final UUID uuid) {
        if (SKIN_CACHE.containsKey(uuid)) {
            return SKIN_CACHE.get(uuid);
        }

        final Optional<UserSkinData> userSkinData = PlayerFetcher.getUserSkin(uuid);

        SKIN_CACHE.put(uuid, userSkinData);

        return userSkinData;
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
