package util;

import lombok.Getter;
import model.Album;
import model.Photo;

import java.util.ArrayList;
import java.util.List;

@Getter
public class AlbumChange {

    private final Album album;

    private final List<Photo> photosChanged = new ArrayList<>();

    public AlbumChange(Album album) {
        this.album = album;
    }

    public void addChangedPhoto(Photo photo) {
        photosChanged.add(photo);
    }
}
