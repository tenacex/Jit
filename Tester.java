import java.util.TreeMap;
import java.io.*;
import java.util.ArrayList;
public class Tester {
	public static void main(String[] args) {
		 TreeMap<String, TreeMap<Integer, ArrayList<String>>> log = null;
            try
            {       
                FileInputStream fileIn = new FileInputStream(".gitlet/log.ser");
                ObjectInputStream in = new ObjectInputStream(fileIn);
                log = (TreeMap) in.readObject();
                in.close();
                fileIn.close();
            } catch(IOException i)
            {
                i.printStackTrace();
                return;
            } catch(ClassNotFoundException c)
            {
                System.out.println("Stage class not found");
                
            }
      System.out.println(log);
	}
}