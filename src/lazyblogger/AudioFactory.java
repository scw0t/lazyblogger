package lazyblogger;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.TagException;

public class AudioFactory {

    private File folder;
    private String artist;
    private String album;
    private String year;
    private String genres;
    private String country;
    private String label;
    private String catNumber;

    public AudioFactory(File folder) {
        this.folder = folder;
        artist = "";
        album = "";
        year = "";
        genres = "";
        country = "";
        label = "";
        catNumber = "";
    }

    public LinkedList<File> getAudio() {
        String[] filter = {"mp3", "MP3"};

        return (LinkedList) FileUtils.listFiles(folder, filter, false);
    }

    public String getTrackList() {
        StringBuilder sb = new StringBuilder();
        try {
            int count = 0;
            for (File file : getAudio()) {
                count++;
                MP3File mpf = (MP3File) AudioFileIO.read(file);
                
                sb.append(count);
                sb.append(". ");
                sb.append(mpf.getTag().getFirst(FieldKey.TITLE));
                sb.append(" (");
                sb.append(secondsToStringWithoutHour(mpf.getAudioHeader().getTrackLength()));
                sb.append(")");
                sb.append("<br>");
                
                if (count == 1) {
                    artist = mpf.getTag().getFirst(FieldKey.ARTIST);
                    album = mpf.getTag().getFirst(FieldKey.ALBUM);
                    year = mpf.getTag().getFirst(FieldKey.YEAR);
                    genres = mpf.getTag().getFirst(FieldKey.GENRE);
                    country = mpf.getTag().getFirst(FieldKey.COUNTRY);
                    label = mpf.getTag().getFirst(FieldKey.RECORD_LABEL);
                    catNumber = mpf.getTag().getFirst(FieldKey.CATALOG_NO);
                }
                
            }
        } catch (CannotReadException 
                | IOException 
                | TagException 
                | ReadOnlyFileException 
                | InvalidAudioFrameException ex) {
            Logger.getLogger(AudioFactory.class.getName()).log(Level.SEVERE, null, ex);
        } 

        return sb.toString();
    }
    
    private String secondsToStringWithoutHour(int inSeconds) {
        int remainder = inSeconds % 3600;
        int minutes = remainder / 60;
        int seconds = remainder % 60;

        String disMinu = (minutes < 10 ? "0" : "") + minutes;
        String disSec = (seconds < 10 ? "0" : "") + seconds;

        return disMinu + ":" + disSec;
    }
    
    public File getFolderImage() {
        WildcardFileFilter imageFilter = new WildcardFileFilter("folder.jpg");
        LinkedList<File> imageList = (LinkedList) FileUtils.listFilesAndDirs(folder,
                imageFilter,
                DirectoryFileFilter.DIRECTORY);
        imageList.removeFirst();
        if (imageList.size() > 0 && imageList.getLast().getName().equals("folder.jpg")) {
            return imageList.getLast();
        } else {
            return null;
        }
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public String getYear() {
        return year;
    }

    public String getGenres() {
        return genres;
    }

    public String getCountry() {
        return country;
    }

    public String getLabel() {
        return label;
    }

    public String getCatNumber() {
        return catNumber;
    }

}
