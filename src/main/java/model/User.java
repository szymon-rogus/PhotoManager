package model;

import com.sun.istack.Nullable;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "USERS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "USER_ID")
    private Integer id;

    @Column(name = "NAME")
    @NonNull
    private String name;

    @Column(name = "PASSWORD")
    @NonNull
    private byte[] password;

    @Column(name = "EMAIL")
    @Nullable
    private String email;

    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Album> albums;

    @Column(name = "SALT")
    @NonNull
    private byte[] salt;

    public void addAlbum(Album album) {
        albums.add(album);
    }

    public void removeFromUser(Album album) {
        albums.remove(album);
    }

    public void renameAlbum(String oldName, String newName) {
        albums.stream()
                .filter(el -> el.getName().equals(oldName))
                .forEach(el -> el.setName(newName));
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
