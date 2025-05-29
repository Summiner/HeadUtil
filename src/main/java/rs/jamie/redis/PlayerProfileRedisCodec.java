package rs.jamie.redis;

import io.lettuce.core.codec.RedisCodec;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import rs.jamie.profile.PlayerTextures;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class PlayerProfileRedisCodec implements RedisCodec<UUID, PlayerTextures> {

    private static final Charset charSet = StandardCharsets.UTF_8;


    @Override
    public UUID decodeKey(ByteBuffer buffer) {
        return new UUID(buffer.getLong(), buffer.getLong());
    }

    @Override
    public PlayerTextures decodeValue(ByteBuffer buffer) {
        UUID uuid = new UUID(buffer.getLong(), buffer.getLong());
        int size = buffer.getInt();
        byte[] skin = new byte[size];
        buffer.get(skin, 0, size);
        size = buffer.getInt();
        byte[] head = new byte[size];
        buffer.get(head, 0, size);
        size = buffer.getInt();
        byte[] cape = new byte[size];
        buffer.get(cape, 0, size);
        return new PlayerTextures(uuid, new String(skin), new String(head), new String(cape));
    }

    @Override
    public ByteBuffer encodeKey(UUID uuid) {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());
        return buffer;
    }

    @Override
    public ByteBuffer encodeValue(PlayerTextures profile) {
        ByteBuf buf = Unpooled.buffer();
        buf.writeLong(profile.uuid().getMostSignificantBits());
        buf.writeLong(profile.uuid().getLeastSignificantBits());
        byte[] skin;
        if(profile.skin()==null) skin = new byte[]{};
        else skin = profile.skin().getBytes(charSet);
        buf.writeInt(skin.length);
        buf.writeBytes(skin);
        byte[] head;
        if(profile.head()==null) head = new byte[]{};
        else head = profile.head().getBytes(charSet);
        buf.writeInt(head.length);
        buf.writeBytes(head);
        byte[] cape;
        if(profile.cape()==null) cape = new byte[]{};
        else cape = profile.cape().getBytes(charSet);
        buf.writeInt(cape.length);
        buf.writeBytes(cape);
        return buf.nioBuffer();
    }
}
