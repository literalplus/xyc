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
public abstract class FileHelper
{
    /**
     * Copies a file or directory <code>src</code> (and all its contents, if any)
     * to a new destination <code>dest</code>.
     * @throws IOException If something * @author <a href="http://xxyy.github.io/">xxyy</a>xxyy98@gmail.com>
     */
    public static void copyFolder(File src, File dest) throws IOException{
        if(src.isDirectory())
        {
            if(!dest.exists())
            {
               dest.mkdir();
            }
     
            String files[] = src.list();
     
            for (String file : files) 
            {
               File srcFile = new File(src, file);
               File destFile = new File(dest, file);
               FileHelper.copyFolder(srcFile,destFile);
            }
     
        }
        else
        {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dest); 
            byte[] buf = new byte[1024];
 
            int length;
            while ((length = in.read(buf)) > 0)
            {
               out.write(buf, 0, length);
            }
 
            in.close();
            out.close();
        }
    }
    
    /**
     * Deletes a file or directory and all its contents.
     * @param pathToDir a {@link Path} pointing to the directory to be deleted.
     * @throws IOExcept* @author <a href="http://xxyy.github.io/">xxyy</a>* @author xxyy98<xxyy98@gmail.com>
     */
    public static void deleteAll(Path pathToDir) throws IOException{
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
