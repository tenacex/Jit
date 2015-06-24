import java.util.zip.*;
import java.io.*;

public class compressor {

    //compresses a file 
	public static void compressFile(File raw, File compressed)
        throws IOException
    {
        InputStream in = new FileInputStream(raw);
        OutputStream out =
            new DeflaterOutputStream(new FileOutputStream(compressed));
        shovelInToOut(in, out);
        in.close();
        out.close();
    }

    /**
     * Decompresses a zlib compressed file.
     */
    public static void decompressFile(File compressed, File raw)
        throws IOException
    {
        InputStream in =
            new InflaterInputStream(new FileInputStream(compressed));
        OutputStream out = new FileOutputStream(raw);
        shovelInToOut(in, out);
        in.close();
        out.close();
    }

    /**
     * Shovels all data from an input stream to an output stream.
     */
    private static void shovelInToOut(InputStream in, OutputStream out)
        throws IOException
    {
        byte[] buffer = new byte[1000];
        int len;
        while((len = in.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
    }
}