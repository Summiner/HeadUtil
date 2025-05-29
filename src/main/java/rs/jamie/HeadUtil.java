package rs.jamie;

import org.jetbrains.annotations.NotNull;
import rs.jamie.cache.CaffeineCache;
import rs.jamie.cache.RedisSkinCache;
import rs.jamie.cache.SkinCache;
import rs.jamie.profile.PlayerTextures;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class HeadUtil {

    private final ApiManager apiManager;
    private final SkinCache cache;

    private HeadUtil(ApiManager apiManager, SkinCache cache) {
        this.apiManager = apiManager;
        this.cache = cache;
    }

    public static class Builder {
        private SkinCacheType cacheType = SkinCacheType.CAFFEINE;
        private String sessionServer = "https://sessionserver.mojang.com/session/minecraft/profile/";
        private String redis = "redis://localhost";
        private boolean capes = false;

        public @NotNull Builder setSessionServer(@NotNull String sessionServer) {
            return this;
        }

        public @NotNull Builder setCacheManager(@NotNull SkinCacheType cacheType) {
            this.cacheType = cacheType;
            return this;
        }

        public @NotNull Builder setRedisServer(@NotNull String connectionUrl) {
            this.redis = connectionUrl;
            return this;
        }

        public @NotNull Builder enableCapes(boolean capes) {
            this.capes = capes;
            return this;
        }

        public @NotNull HeadUtil build() {
            SkinCache cache;
            if(cacheType == SkinCacheType.REDIS) cache = new RedisSkinCache(redis);
            else cache = new CaffeineCache();

            return new HeadUtil(new ApiManager(sessionServer, capes), cache);
        }

    }

    public enum SkinCacheType {

        REDIS,
        CAFFEINE;

    }

    public CompletableFuture<PlayerTextures> getPlayerSkin(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            PlayerTextures textures = cache.getSkin(uuid);
            if(textures!=null) return textures;
            textures = apiManager.getTextures(apiManager.getProfile(uuid).join()).join();
            cache.addSkin(uuid, textures);
            return textures;
        });
    }


}