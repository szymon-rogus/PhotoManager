package model;

import javafx.scene.image.Image;

import java.time.LocalDate;
import java.util.List;

public class Photo {

    private String name;
    private Image image;
    private String describtion;
    private LocalDate date;
    private List<String> tags;


    public Photo(String name, Image image) {
        this.name = name;
        this.image = image;
    }
}
