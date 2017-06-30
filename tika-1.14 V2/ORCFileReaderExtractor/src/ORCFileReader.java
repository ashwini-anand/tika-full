import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.exec.vector.VectorizedRowBatch;
import org.apache.orc.OrcFile;
import org.apache.orc.Reader;
import org.apache.hadoop.conf.Configuration;
import org.apache.orc.RecordReader;


/**
 * Created by asanand on 2/1/17.
 */
public class ORCFileReader {
    public static void main(String[] args) throws  Exception{
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.getLocal(conf);
        Reader reader = OrcFile.createReader(new Path("/Users/asanand/Downloads/ORCFiles/decimal.orc"), OrcFile.readerOptions(conf).filesystem(fs));
        System.out.println("size of metadata : "+reader.getMetadataSize());
        System.out.println(reader.getMetadataKeys().size());
        System.out.println(reader.getCompressionKind().toString());
        System.out.println(reader.getCompressionSize());
        System.out.println(reader.getContentLength());
        System.out.println(reader.getFileTail().toString());
        System.out.println(reader.getFileVersion().toString());
        System.out.println(reader.getNumberOfRows());
        System.out.println(reader.getWriterVersion().toString());
        for (String key: reader.getMetadataKeys()
             ) {
            System.out.println(key+" : "+reader.getMetadataValue(key));


        }
        RecordReader rows = reader.rows();
        VectorizedRowBatch batch = reader.getSchema().createRowBatch();
//        while(rows.nextBatch(batch)){
//            for(int r=0; r<batch.size;r++){
//                System.out.println(batch.toString());
//            }
//        }
        rows.close();
    }

}
