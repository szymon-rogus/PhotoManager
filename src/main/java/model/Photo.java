package model;

import com.sun.istack.Nullable;
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
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PHOTO_ID")
    private Integer id;

    @Column(name = "NAME")
    @NonNull
    private String name;

    @Lob
    @Column(name = "IMAGE", columnDefinition = "BLOB")
    @NonNull
    private byte[] image;

    @Column(name = "DESCRIPTION")
    @Nullable
    private String description;

    @Column(name = "LOCALIZATION")
    @Nullable
    private String localization;

    @Column(name = "DATE")
    @Nullable
    private Date date;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Tag> tags;

    public Photo(@NonNull String name, @NonNull byte[] image, String localization, String description, Date date, List<Tag> tags) {
        this.name = name;
        this.image = image;
        this.localization = localization;
        this.description = description;
        this.date = date;
        this.tags = tags;
    }
}