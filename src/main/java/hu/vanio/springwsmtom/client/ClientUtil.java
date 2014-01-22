/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hu.vanio.springwsmtom.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.activation.DataHandler;

/**
 * Client utilities
 * 
 * @author Gyula Szalai <gyula.szalai@vanio.hu>
 */
public class ClientUtil {
    
    /** Test content name */
    static public final String TEST_CONTENT_NAME = "spring-ws-logo";
    /** Test content URL */
    static public final URL TEST_CONTENT_URL = Thread.currentThread().getContextClassLoader().getResource("spring-ws-logo.png");
    
    /**
     * Saves the specified content to the specified file
     * @param content The content
     * @param outFile The output file
     * @throws IOException If an error occurs during saving
     */
    static public long saveContentToFile(DataHandler content, File outFile) throws IOException {
        long size = 0;
        byte[] buffer = new byte[1024];
        try (InputStream is = content.getInputStream()) {
            try (OutputStream outStream = new FileOutputStream(outFile)) {
                for (int readBytes; (readBytes = is.read(buffer, 0, buffer.length)) > 0;) {
                    size += readBytes;
                    outStream.write(buffer, 0, readBytes);
                }
            }
        }
        return size;
    }
}
