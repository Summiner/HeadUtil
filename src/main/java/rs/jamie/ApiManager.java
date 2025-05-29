package rs.jamie;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import rs.jamie.profile.PlayerProfile;
import rs.jamie.gson.UUIDDeserializer;
import rs.jamie.profile.PlayerTextures;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

class ApiManager {

    private final String sessionurl;
    private final boolean CAPES;
    private static final Gson gson = new GsonBuilder().registerTypeAdapter(UUID.class, new UUIDDeserializer()).create();

    public ApiManager(String sessionurl, boolean capes) {
        this.sessionurl = sessionurl;
        this.CAPES = capes;
    }

    public CompletableFuture<PlayerProfile> getProfile(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder().uri(new URI(sessionurl+uuid)).GET().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                return gson.fromJson(response.body(), PlayerProfile.class);
            } catch (Exception e) {
                return null;
            }
        });
    }

    public CompletableFuture<PlayerTextures> getTextures(PlayerProfile profile) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String skin = profile.getSkin();
                String cape = profile.getCape();
                String head = null;
                if(skin!=null) {
                    HttpClient client = HttpClient.newHttpClient();
                    HttpRequest request = HttpRequest.newBuilder().uri(new URI(skin)).GET().build();
                    HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
                    skin = Base64.getEncoder().encodeToString(response.body());
                    head = SkinCropper.cropImage(skin).join();
                }
                if(CAPES&&cape!=null) {
                    HttpClient client = HttpClient.newHttpClient();
                    HttpRequest request = HttpRequest.newBuilder().uri(new URI(cape)).GET().build();
                    HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
                    cape = Base64.getEncoder().encodeToString(response.body());
                } else cape = null;
                return new PlayerTextures(profile.id(), skin, head, cape);
            } catch (Exception e) {
                return null;
            }
        });
    }


}
