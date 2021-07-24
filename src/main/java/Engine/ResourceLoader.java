package Engine;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class ResourceLoader {
    public static String getBaseName(String fileName) {
        int index = fileName.lastIndexOf('.');
        if (index == -1) {
            return fileName;
        } else {
            return fileName.substring(0, index);
        }
    }

    public static List<String> filesInDirectory(String directory, boolean withExtension) {
        List<String> ret = new ArrayList<>();

        try {
            URI uri = ResourceLoader.class.getClassLoader().getResource(directory).toURI();

            Path path;
            FileSystem fs = null;
            if (uri.getScheme().equals("jar")) {
                fs = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());
                path = fs.getPath(directory);
            } else {
                path = Paths.get(uri);
            }

            Stream<Path> s = Files.walk(path, 1);
            Iterator<Path> i = s.iterator();
            if (i.hasNext()) {
                i.next();
            }

            while (i.hasNext()) {
                String fName = i.next().getFileName().toString();
                if (!withExtension) {
                    fName = ResourceLoader.getBaseName(fName);
                }

                ret.add(fName);
            }

            s.close();

            if (fs != null) {
                fs.close();
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static List<String> filesInDirectory(String directory) {
        return ResourceLoader.filesInDirectory(directory, true);
    }

    public static InputStream loadFile(String file) {
        return ResourceLoader.class.getClassLoader().getResourceAsStream(file);
    }
}
