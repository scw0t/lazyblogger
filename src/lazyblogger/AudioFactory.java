package lazyblogger;

import java.io.File;
import java.util.LinkedList;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;

public class AudioFactory {

    private File folder;

    public AudioFactory(File folder) {
        this.folder = folder;
        init();
    }

    public void init() {

        WildcardFileFilter fileFilter = new WildcardFileFilter("*.mp3");

        LinkedList<File> list = (LinkedList) FileUtils.listFilesAndDirs(folder,
                fileFilter,
                DirectoryFileFilter.DIRECTORY);
        for (File file : list) {
            System.out.println(file.getName());
        }

    }

}
