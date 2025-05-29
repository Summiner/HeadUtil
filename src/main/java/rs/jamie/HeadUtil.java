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

        /**
         * Set the player session server.
         * Defaults to official session server
         * @param sessionServer the url for the session server
         * @return this builder
         */
        public @NotNull Builder setSessionServer(@NotNull String sessionServer) {
            return this;
        }

        /**
         * Set the player session server.
         * Defaults to CAFFEINE
         * @param cacheType caching type to be used
         * @return this builder
         */
        public @NotNull Builder setCacheType(@NotNull SkinCacheType cacheType) {
            this.cacheType = cacheType;
            return this;
        }

        /**
         * Set the redis server url.
         * Defaults to redis://localhost
         * @param connectionUrl url used to connect to redis server
         * @return this builder
         */
        public @NotNull Builder setRedisServer(@NotNull String connectionUrl) {
            this.redis = connectionUrl;
            return this;
        }

        /**
         * Enable/Disable capes.
         * Defaults to false
         * @param capes enable/disable capes
         * @return this builder
         */
        public @NotNull Builder enableCapes(boolean capes) {
            this.capes = capes;
            return this;
        }

        /**
         * Get the HeadUtil Instance.
         * @return HeadUtil Instance
         */
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

    /**
     * Get the textures of a player
     * @param uuid UUID of the player you are accessing
     * @return CompletableFuture of PlayerTextures
     */
    public CompletableFuture<PlayerTextures> getPlayerTextures(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            PlayerTextures textures = cache.getSkin(uuid);
            if(textures!=null) return textures;
            textures = apiManager.getTextures(apiManager.getProfile(uuid).join()).join();
            cache.addSkin(uuid, textures);
            return textures;
        });
    }


}