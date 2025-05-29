package rs.jamie.gson;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.UUID;

public class UUIDDeserializer implements JsonDeserializer<UUID> {

    @Override
    public UUID deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String string = jsonElement.getAsString();
        return FastUuidSansHyphens.parseUuid(string);
    }

}
