package model;

import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "PHOTOS")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PHOTO_ID")
    private Integer id;

    @Column(name = "NAME")
    @NonNull
    private String name;

    @Column(name = "IMAGE")
    @NonNull
    private byte[] image;

    @Column(name = "DESCRIPTION")
    @Nullable
    private String description;

    @Column(name = "DATE")
    @Nullable
    private Date date;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Tag> tags;


    public Photo(@NonNull String name,@NonNull byte[] image) {
        this.name = name;
        this.image = image;
    }
}
