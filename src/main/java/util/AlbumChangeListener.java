package util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AlbumChangeListener {

    private final List<AlbumChange> albumChanges = new ArrayList<>();

    private final EmailSender emailSender = new EmailSender();

    public void addAlbumChange(AlbumChange albumChange) {
        List<AlbumChange> matchingAlbumChanges = albumChanges.stream()
                .filter(ac -> ac.getAlbum().equals(albumChange.getAlbum()))
                .collect(Collectors.toList());
        if (matchingAlbumChanges.isEmpty()) {
            albumChanges.add(albumChange);
            sendEmails();
            return;
        }
        matchingAlbumChanges.get(0).getPhotosChanged().addAll(albumChange.getPhotosChanged());
        sendEmails();
    }

    public void sendEmails() {
        emailSender.sendEmails(albumChanges);
    }
}
