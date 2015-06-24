import java.security.MessageDigest;
import java.util.Formatter;
import java.security.NoSuchAlgorithmException;
import java.io.*;

public class hasher {
	    
	    // generates sha-1 hash, source: http://www.javacreed.com/how-to-generate-sha1-hash-value-of-file/
  
    public static String sha1(final File file) throws NoSuchAlgorithmException, IOException {
    final MessageDigest messageDigest = MessageDigest.getInstance("SHA1");

    try (InputStream is = new BufferedInputStream(new FileInputStream(file))) {
      final byte[] buffer = new byte[1024];
      for (int read = 0; (read = is.read(buffer)) != -1;) {
        messageDigest.update(buffer, 0, read);
      }
    }

    // Convert the byte to hex format
    try (Formatter formatter = new Formatter()) {/Users/dylandove/Downloads/brd-master/proj2/hasher.java
      for (final byte b : messageDigest.digest()) {
        formatter.format("%02x", b);
      }
      return formatter.toString();
    }
  }

}

//jfdhsfj