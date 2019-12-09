package util;
import lombok.experimental.UtilityClass;
import model.Tag;

import java.util.*;

@UtilityClass
public class TagParser {
    private final static int limit = 5;

    public static List<Tag> parse(String tagsString) {

        Set<String> tagSet = new HashSet<>(Arrays.asList(tagsString.split("(?U)\\W+")));
        List<Tag> tags = new ArrayList<>();

        int counter = 0;
        for(String tag : tagSet) {
            if(counter++ == limit) {
                break;
            }
            tags.add(new Tag(tag));
        }

        return tags;
    }
}