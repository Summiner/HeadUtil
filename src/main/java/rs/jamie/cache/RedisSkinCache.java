package rs.jamie.cache;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.async.RedisStringAsyncCommands;
import rs.jamie.profile.PlayerTextures;
import rs.jamie.redis.PlayerProfileRedisCodec;

import java.util.UUID;

public class RedisSkinCache implements SkinCache {

    private final RedisStringAsyncCommands<UUID, PlayerTextures> redis;

    public RedisSkinCache(String url) {
         this.redis = RedisClient.create(url).connect(new PlayerProfileRedisCodec()).async();
    }

    @Override
    public PlayerTextures getSkin(UUID key) {
        try {
            return redis.get(key).get();
        } catch (Exception ignored) {
            return null;
        }
    }

    @Override
    public void addSkin(UUID uuid, PlayerTextures profile) {
        try {
            redis.setex(uuid, 1800, profile).get();
        } catch (Exception ignored) {
        }
    }
}
