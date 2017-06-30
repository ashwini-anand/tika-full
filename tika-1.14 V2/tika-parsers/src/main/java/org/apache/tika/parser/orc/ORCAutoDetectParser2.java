package org.apache.tika.parser.orc;

import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TemporaryResources;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.apache.tika.parser.CompositeParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.sax.SecureContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by asanand on 2/6/17.
 */
public class ORCAutoDetectParser2 extends CompositeParser{

    public void parse(
            InputStream stream, ContentHandler handler,
            Metadata metadata, ParseContext context)
            throws IOException, SAXException, TikaException {
        TemporaryResources tmp = new TemporaryResources();
        try {
            TikaInputStream tis = TikaInputStream.get(stream, tmp);

            // Automatically detect the MIME type of the document
            ORCDetector orcDetector = new ORCDetector();
            MediaType type = orcDetector.detect(tis, metadata);
            metadata.set(Metadata.CONTENT_TYPE, type.toString());
            metadata.set("X-Parsed-By",ORCParser.class.toString());

            // TIKA-216: Zip bomb prevention
            SecureContentHandler sch =
                    handler != null ? new SecureContentHandler(handler, tis) : null;
            try {
                // Parse the document
                super.parse(tis, sch, metadata, context);
            } catch (SAXException e) {
                // Convert zip bomb exceptions to TikaExceptions
                sch.throwIfCauseOf(e);
                throw e;
            }
        } finally {
            tmp.dispose();
        }
    }

}
