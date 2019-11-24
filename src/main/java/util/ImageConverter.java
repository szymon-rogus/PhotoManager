package util;

import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import lombok.experimental.UtilityClass;

import java.io.ByteArrayInputStream;

@UtilityClass
public class ImageConverter {

    private static Image convertFromByteArray(byte[] byteArray) {
        return new Image(new ByteArrayInputStream(byteArray));
    }

    private static byte[] convertFromImage(Image image) {
        final int width = (int) image.getWidth();
        final int height = (int) image.getHeight();
        byte[] buffer = new byte[width * height * 4];
        image.getPixelReader().getPixels(0, 0, width, height, PixelFormat.getByteBgraInstance(), buffer, 0, width * 4);
        return buffer;
    }
}
