package model;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class PhotoAlbum {

    private String name;
    private List<Photo> photoList;
    private LocalDate creationDate;
    private LocalDate modificationDate;
    private Map<String, Integer> tagMap;


    public PhotoAlbum(String name, List<Photo> photoList) {
        this.name = name;
        this.photoList = photoList;
    }

    public List<Photo> getAllPhotos(){
        return photoList;
    }

    public List<Photo> getByTag(String tag){
        // TODO
        return null;
    }

    public List<Photo> getByName(String name){
       // TODO
       return null;
    }

    public List<Photo> getByDateInterval(){
        // TODO
        return null;
    }

    public void AddToAlbum(Photo photo){
        photoList.add(photo);
        // TODO more
    }

}
