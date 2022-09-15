package model;

import app.AppManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ALBUM")
@Getter
@Setter
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

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<User> users;

    public Album(@NonNull String name, List<Photo> photoList) {
        this.name = name;
        this.nameProperty = new SimpleStringProperty(name);
        this.photoList = photoList;
    }

    public Album(@NonNull String name) {
        this.name = name;
        this.nameProperty = new SimpleStringProperty(name);
        this.photoList = new ArrayList<>();
        this.users = new ArrayList<>();
        final User user = AppManager.getSessionUser();
        users.add(user);
        user.addAlbum(this);

    }

    public void addToAlbum(Photo photo) {
        photoList.add(photo);
    }

    public void removeFromAlbum(Photo photo) {
        photoList.remove(photo);
    }

}
