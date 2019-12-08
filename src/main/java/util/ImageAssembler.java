package util;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ImageAssembler {

    public static String getExtension(String path) {
        if(path.contains(".")) {
            return path.split("\\.")[1];
        }
        return null;
    }
}
