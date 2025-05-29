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
        private ApiManager apiManager;
        private SkinCache cache = new CaffeineCache();
        private String sessionServer = "https://sessionserver.mojang.com/session/minecraft/profile/";
        private boolean capes = false;

        public Builder() {
            apiManager = new ApiManager(sessionServer, capes);
        }

        public @NotNull Builder setSessionServer(@NotNull String sessionServer) {
            this.apiManager = new ApiManager(sessionServer, capes);
            return this;
        }

        public @NotNull Builder setCacheManager(@NotNull SkinCache cache) {
            this.cache = cache;
            return this;
        }

        public @NotNull Builder enableCapes(boolean capes) {
            this.capes = capes;
            this.apiManager = new ApiManager(sessionServer, capes);
            return this;
        }

        public @NotNull HeadUtil build() {
            return new HeadUtil(apiManager, cache);
        }

    }

    public enum SkinCacheType {

        REDIS {
            @Override
            public SkinCache get() {
                return new RedisSkinCache();
            }
        },
        CAFFEINE {
            @Override
            public SkinCache get() {
                return new CaffeineCache();
            }
        };

        public abstract SkinCache get();

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