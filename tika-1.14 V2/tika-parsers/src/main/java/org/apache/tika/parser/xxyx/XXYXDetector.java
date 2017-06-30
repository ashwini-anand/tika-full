package org.apache.tika.parser.xxyx;

import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.detect.Detector;
import org.apache.tika.io.LookaheadInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.commons.io.IOUtils;

import java.nio.charset.StandardCharsets;


public class XXYXDetector implements Detector, XXYX {
    private static final long serialVersionUID = -1709652690773421147L;

    private static int numOfOccurances =0;
    private static final int LOOK_AHEAD_SIZE = 1024;
    private static final int THRESHOLD_OCCURANCE = 2;
    private static final String TYPE_IDENTIFIER = "xxyxxxyx";

    public static int getNumOfOccurances() {
        return numOfOccurances;
    }

    public MediaType detect(InputStream stream, Metadata metadata)
            throws IOException {

        MediaType type = MediaType.OCTET_STREAM;

        InputStream truncStream = null;
        try {
            truncStream = new LookaheadInputStream(stream, LOOK_AHEAD_SIZE);
            String fileData = IOUtils.toString(truncStream, StandardCharsets.UTF_8);
            numOfOccurances =0;
            int lastIndex = 0;
            while (lastIndex != -1) {
                lastIndex = fileData.indexOf(TYPE_IDENTIFIER, lastIndex);
                if (lastIndex != -1) {
                    numOfOccurances++;
                    lastIndex += TYPE_IDENTIFIER.length();
                }

            }
            if (numOfOccurances >= THRESHOLD_OCCURANCE) {
                type = MediaType.text(FILE_TYPE);
            }
        } catch (Exception e) {
            System.out.println("----Exception in XXYXDetector----------------------");
            e.printStackTrace();
        } finally {
            truncStream.close();
        }
        return type;
    }
}


