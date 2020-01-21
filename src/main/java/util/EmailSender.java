package util;

import app.AppManager;
import com.sun.mail.smtp.SMTPTransport;
import model.Album;
import model.Photo;
import model.User;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;

public class EmailSender {

    private static final String SMTP_SERVER = "smtp.mail.com";
    private static final String USERNAME = "photomanager@mail.com";
    private static final String PASSWORD = "photomanager123";
    private static final String EMAIL_FROM = "photomanager@mail.com";
    private static final String EMAIL_SUBJECT = "There are new photos in your albums!";

    private static final Properties properties = System.getProperties();

    public EmailSender() {
        properties.put("mail.smtp.host", SMTP_SERVER);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "587");
    }

    public void sendEmails(List<AlbumChange> albumChanges) {
        final Map<User, List<AlbumChange>> emailContents = new HashMap<>();
        albumChanges.forEach(ac -> setAlbumChangeToUsers(ac, emailContents));
        final Set<User> usersToNotify = emailContents.keySet();
        usersToNotify.forEach(user -> sendEmail(user, emailContents.get(user)));
    }

    private void setAlbumChangeToUsers(AlbumChange albumChange, Map<User, List<AlbumChange>> emailContents) {
        final Album album = albumChange.getAlbum();
        final List<User> albumUsers = album.getUsers();
        albumUsers.forEach(u -> setAlbumChangeToUser(u, albumChange, emailContents));
    }

    private void setAlbumChangeToUser(User user, AlbumChange albumChange, Map<User, List<AlbumChange>> emailContents) {
        if (user.getEmail() != null && !AppManager.getSessionUser().equals(user)) {
            if (!emailContents.containsKey(user)) {
                final List<AlbumChange> userAlbumChanges = new ArrayList<>();
                userAlbumChanges.add(albumChange);
                emailContents.put(user, userAlbumChanges);
                return;
            }
            final List<AlbumChange> userAlbumChanges = emailContents.get(user);
            userAlbumChanges.add(albumChange);
        }
    }

    private void sendEmail(User user, List<AlbumChange> albumChanges) {
        final Session session = Session.getInstance(properties, null);
        final Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(EMAIL_FROM));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(user.getEmail(), false));
            message.setSubject(EMAIL_SUBJECT);
            final String text = prepareText(user, albumChanges);
            message.setText(text);
            message.setSentDate(new Date());
            final SMTPTransport t = (SMTPTransport) session.getTransport("smtp");
            t.connect(SMTP_SERVER, USERNAME, PASSWORD);
            t.sendMessage(message, message.getAllRecipients());
            t.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    private String prepareText(User user, List<AlbumChange> albumChanges) {
        String text = "Witaj " + user.getName() + "!\n\n" +
                "Dodano kilka nowych zdjęć do Twoich albumów:\n\n";
        for (AlbumChange albumChange : albumChanges) {
            text = text.concat("Album: " + albumChange.getAlbum().getName() + "\n\n");
            for (Photo photo : albumChange.getPhotosChanged()) {
                text = text.concat("- " + photo.getName() + "\n");
            }
            text = text.concat("\n\n");
        }
        return text;
    }
}
