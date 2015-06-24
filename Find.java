import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.io.*;

public class Find {
	public static void main(String message) {
		TreeMap<String, TreeMap<Integer, ArrayList<String>>> log = null;
		try
            {       
                FileInputStream fileIn = new FileInputStream(".gitlet/log.ser");
                ObjectInputStream in = new ObjectInputStream(fileIn);
                log = (TreeMap) in.readObject();
                in.close();
                fileIn.close();
            } catch(Exception i)
            {
                i.printStackTrace();
                return;
            } 
        for(Map.Entry<String,TreeMap<Integer, ArrayList<String>>> entry : log.entrySet()) {
        	String branch = entry.getKey();
  			TreeMap<Integer, ArrayList<String>> commitChain = entry.getValue();
  			for (Map.Entry<Integer, ArrayList<String>> commitEntry : commitChain.entrySet()){
  				Integer commitID = commitEntry.getKey();
  				ArrayList<String> info = commitEntry.getValue();
  				if (info.get(1).equals(message)) {
  					System.out.println(commitID);
  				}
  			}
        }
	}
}