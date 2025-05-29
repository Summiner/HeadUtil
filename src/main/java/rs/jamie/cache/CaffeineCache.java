package rs.jamie.cache;

import com.github.benmanes.caffeine.cache.AsyncCache;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import rs.jamie.profile.PlayerTextures;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CaffeineCache implements SkinCache {

    AsyncCache<UUID, PlayerTextures> cache = Caffeine.newBuilder()
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .buildAsync();

    @Override
    public PlayerTextures getSkin(UUID uuid) {
        CompletableFuture<PlayerTextures> future = cache.getIfPresent(uuid);
        return future==null?null:future.join();
    }

    @Override
    public void addSkin(UUID uuid, PlayerTextures profile) {
        cache.put(uuid, CompletableFuture.completedFuture(profile));
    }
}
