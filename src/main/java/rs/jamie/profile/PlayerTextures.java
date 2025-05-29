package rs.jamie.profile;

import java.util.UUID;

/**
 * Player's Textures
 * @param uuid UUID of the player
 * @param skin Base64 PNG of the player's skin texture
 * @param head Base64 PNG of the player's head texture
 * @param cape Base64 PNG of the player's cape texture
 */
public record PlayerTextures(UUID uuid, String skin, String head, String cape) {
}
