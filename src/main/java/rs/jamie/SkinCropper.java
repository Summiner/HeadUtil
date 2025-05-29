package rs.jamie;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.concurrent.CompletableFuture;

class SkinCropper {

    private static final Base64.Decoder decoder = Base64.getDecoder();

    public static CompletableFuture<String> cropImage(String base64Image) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                byte[] imageBytes = decoder.decode(base64Image);
                InputStream is = new ByteArrayInputStream(imageBytes);

                BufferedImage originalImage = ImageIO.read(is);
                BufferedImage croppedImage = originalImage.getSubimage(8, 8, 8, 8);

                ByteArrayOutputStream os = new ByteArrayOutputStream();
                ImageIO.write(croppedImage, "png", os);

                byte[] croppedBytes = os.toByteArray();
                return Base64.getEncoder().encodeToString(croppedBytes);
            } catch (Exception ignored) {
                return null;
            }
        });
    }

}
