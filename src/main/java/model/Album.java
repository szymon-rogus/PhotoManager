package model;

import com.sun.istack.Nullable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "ALBUM")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PHOTO_ID")
    private Integer id;

    @Column(name = "NAME")
    @NonNull
    private String name;

    @Transient
    private StringProperty nameProperty;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Photo> photoList;

    @Column(name = "CREATION_DATE")
    @CreationTimestamp
    private Date creationDate;

    @Column(name = "LAST_MODIFIED")
    @UpdateTimestamp
    private Date modificationDate;

    @OneToMany
    @MapKey(name = "name")
    @Nullable
    private Map<String, Tag> tagMap;

    public Album(@NonNull String name, List<Photo> photoList) {
        this.name = name;
        this.nameProperty = new SimpleStringProperty(name);
        this.photoList = photoList;
    }

    public Album(@NonNull String name) {
        this.name = name;
        this.nameProperty = new SimpleStringProperty(name);
        this.photoList = new ArrayList<>();
    }

    public List<Photo> getByTag(String tag) {
        // TODO
        return null;
    }

    public List<Photo> getByName(String name) {
        // TODO
        return null;
    }

    public List<Photo> getByDateInterval() {
        // TODO
        return null;
    }

    public void addToAlbum(Photo photo) {
        photoList.add(photo);
        // TODO more
    }

}
