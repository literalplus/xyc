/*
 * Copyright (c) 2014 xxyy (Philipp Nowak; devnull@nowak-at.net). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing, copying and reverse-engineering is strictly prohibited without permission from the original author and may result in legal steps being taken.
 */

package io.github.xxyy.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
public final class FileHelper {
    private FileHelper() {

    }

    /**
     * Copies a file or directory {@code src} (and all its contents, if any)
     * to a new destination {@code dest}.
     *
     * @throws IOException If something went wrong while copying
     */
    public static void copyFolder(File src, File dest) throws IOException {
        if (src.isDirectory()) {
            FileHelper.mkdirsWithException(dest);

            String files[] = src.list();

            for (String file : files) {
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                FileHelper.copyFolder(srcFile, destFile);
            }

        } else {
            FileHelper.mkdirsWithException(dest.getParentFile());

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
    public static void deleteWithException(Path pathToDir) throws IOException {
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

    /**
     * Creates a directory and any necessary parent directories, throwing an exception if the operation failed.
     * If the passed File does not represent a directory, the same exception is thrown.
     * This is necessary because {@link java.io.File#mkdirs()} does not throw exceptions.
     * If you prefer checked exceptions, take a look at {@link Files#createDirectories(java.nio.file.Path, java.nio.file.attribute.FileAttribute[])}.
     *
     * @param file File representing the directory to create.
     * @return The passed file, for convenient construction
     * @throws java.lang.IllegalStateException If {@link java.io.File#mkdirs()} returned false.
     * @see java.io.File#mkdirs()
     */
    public static File mkdirsWithException(File file) {
        if (!file.exists() && !file.mkdirs()) {
            throw new IllegalStateException("Could not create directory: " + file.getAbsolutePath());
        }

        return file;
    }
}
