package util;
import lombok.experimental.UtilityClass;
import model.Tag;

import java.util.*;

@UtilityClass
public class TagParser {
    private final static int limit = 5;

    public static List<Tag> parse(String tagsString) {
        List<String> tags = new ArrayList<>(Arrays.asList(tagsString.split("(?U)\\W+")));
        List<String> tagsUnique = new ArrayList<>();
        for (int i = 0; i < tags.size(); i++) {
            if (!(tagsUnique.contains(tags.get(i)))) {
                tagsUnique.add(tags.get(i));
            }
        }
        tags.clear();

        int counter = 0;
        List<Tag> tagList = new ArrayList<>();
        for(String tag : tagsUnique) {
            if(tag.equals("")) {
                continue;
            }
            if(counter++ == limit) {
                break;
            }
            tagList.add(new Tag(tag));
        }

        return tagList;
    }

    public static String parseAsString(String tagsString) {
        List<String> tagList = new ArrayList<>(Arrays.asList(tagsString.split("(?U)\\W+")));
        String tags = "";

        for(String tag : tagList) {
            tags += tag + " ";
        }

        return tags.substring(0, tags.length() - 1);
    }

    public static String parseAsStringGetLast(String tagsString) {
        List<String> tags = new ArrayList<>(Arrays.asList(tagsString.split("(?U)\\W+")));
        return tags.get(tags.size()-1);
    }
}