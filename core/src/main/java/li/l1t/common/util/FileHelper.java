/*
 * MIT License
 *
 * Copyright (C) 2013 - 2017 Philipp Nowak (https://github.com/xxyy) and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package li.l1t.common.util;

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
     * @param src  Source file or directory.
     * @param dest If src is a directory, destination folder of the source file. Destination file otherwise.
     * @throws IOException If something went wrong while copying
     */
    public static void copyFolder(File src, File dest) throws IOException {
        if (src.isDirectory()) {
            FileHelper.mkdirsWithException(dest);

            String[] files = src.list();
            if (files == null) {
                throw new IOException("src.list() returned null for " + src.getAbsolutePath());
            }

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
     * Creates a directory and any necessary parent directories, throwing an exception if the operation failed. If the
     * passed File does not represent a directory, the same exception is thrown. This is necessary because {@link
     * java.io.File#mkdirs()} does not throw exceptions. If you prefer checked exceptions, take a look at {@link
     * Files#createDirectories(java.nio.file.Path, java.nio.file.attribute.FileAttribute[])}.
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

    /**
     * Creates all non-existent parent directories of any file and the file itself.
     *
     * @param file the file
     * @return the file
     * @throws IOException if an error occurs creating the parent directories or the file itself
     */
    public static File createWithParents(File file) throws IOException {
        if (!file.exists()) {
            File parent = file.getAbsoluteFile().getParentFile();
            if (parent != null) {
                Files.createDirectories(parent.toPath());
            }
            Files.createFile(file.toPath());
        }
        return file;
    }
}
