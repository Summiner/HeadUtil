package rs.jamie.profile;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

public record PlayerProfile(UUID id, String name, List<ProfileProperty> properties) {

    public String getSkin() {
        String texture = getTextures();
        if(texture==null) return null;
        String data = new String(Base64.getDecoder().decode(texture));
        try {
            JsonObject jsonObject = JsonParser.parseString(data).getAsJsonObject();
            return jsonObject.getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString();
        } catch (Exception e) {
            return null;
        }
    }

    public String getCape() {
        String texture = getTextures();
        if(texture==null) return null;
        String data = new String(Base64.getDecoder().decode(texture));
        try {
            JsonObject jsonObject = JsonParser.parseString(data).getAsJsonObject();
            return jsonObject.getAsJsonObject("textures").getAsJsonObject("CAPE").get("url").getAsString();
        } catch (Exception e) {
            return null;
        }
    }

    public String getTextures() {
        for (ProfileProperty property : properties) {
            if (property.name().equals("textures")) return property.value();
        }
        return null;
    }

}
