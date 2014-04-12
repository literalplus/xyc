package io.github.xxyy.common.util;

import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Class that provides some static utilities to work
 * with files.
 *
 * @author <a href="http://xxyy.github.io/">xxyy</a>
 */
public abstract class FileHelper {
    /**
     * Copies a file or directory {@code src} (and all its contents, if any)
     * to a new destination {@code dest}.
     *
     * @throws IOException If something went wrong while copying
     */
    public static void copyFolder(File src, File dest) throws IOException {
        if (src.isDirectory()) {
            if (!dest.exists()) {
                assert dest.mkdirs();
            }

            String files[] = src.list();

            for (String file : files) {
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                FileHelper.copyFolder(srcFile, destFile);
            }

        } else {
            if(!src.exists()){
                return; //This seems to be a thing now
            }

            if (!dest.getParentFile().exists()) {
                assert dest.getParentFile().mkdirs();
            }

            try (InputStream in = new FileInputStream(src);
                 OutputStream out = new FileOutputStream(dest)) {

                byte[] buf = new byte[1024];

                int length;
                while ((length = in.read(buf)) > 0) {
                    out.write(buf, 0, length);
                }

                in.close();
                out.close();
            }
        }
    }

    /**
     * Deletes a file or directory and all its contents.
     *
     * @param pathToDir a {@link Path} pointing to the directory to be deleted.
     * @throws java.io.IOException If a file could not be deleted.
     */
    public static void deleteAll(Path pathToDir) throws IOException {
        Files.walkFileTree(pathToDir, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if (exc == null) {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
                throw exc;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

        });
    }
}
