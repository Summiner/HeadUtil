package rs.jamie.cache;

import rs.jamie.profile.PlayerTextures;

import java.util.UUID;

public interface SkinCache {

    PlayerTextures getSkin(UUID uuid);

    void addSkin(UUID uuid, PlayerTextures profile);

}
