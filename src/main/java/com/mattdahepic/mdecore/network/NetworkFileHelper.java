package com.mattdahepic.mdecore.network;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.function.BiConsumer;

public class NetworkFileHelper {
    /**
     * Gets the contents of a plain text file located at url
     * !!! DO NOT FORGET TO CLOSE THE READER WHEN FINISHED USING !!!
     * @param url The url of a raw plain text file to process
     * @return The contents of the file in a BufferedReader
     * @throws IOException
     */
    public static BufferedReader getPlainTextFile (String url) throws IOException {
        URL loc = new URL(url);
        return new BufferedReader(new InputStreamReader(loc.openStream()));
    }

    /**
     * Runs a function provided on every line of a plain text file located at url
     * @param url The url of a raw plain text file to process
     * @param processLine a {@link BiConsumer} where the integer is the line number and the String is the contents of the line
     * @throws IOException
     */
    public static void processPlainTextFile (String url, @Nonnull BiConsumer<Integer, String> processLine) throws IOException {
        BufferedReader in = getPlainTextFile(url);
        int line = 0;
        String string;
        while ((string = in.readLine()) != null) {
            processLine.accept(line,string);
            line++;
        }
        in.close();
    }
}
