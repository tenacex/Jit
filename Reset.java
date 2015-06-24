import java.io.*;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Map;

public class Reset{
		
		public static void restoreToCommitID(String commitID) {
		String branch = "temp";
		try {
            	BufferedReader headTxtFile = new BufferedReader(new FileReader(".gitlet/HEAD.txt"));
                String headTxtString = headTxtFile.readLine();
                branch = headTxtString.substring(15, headTxtString.length());


        } catch(Exception e) {
        	System.out.println(e);
        }

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
        //commit history of current branch
        TreeMap<Integer, ArrayList<String>> commitIDLog = log.get(branch);
        ArrayList<String> info = commitIDLog.get(Integer.parseInt(commitID));

        if (info == null) {
        	System.out.println("No commit with that id exists.");
        } else {
        	String snapshotLocation = info.get(2);
        	String firstTwo = snapshotLocation.substring(0,2);
            String last38 = snapshotLocation.substring(2, 40);

        	TreeMap<String, String> snapshot = new TreeMap<String, String>();


            try {       
                FileInputStream fileIn = new FileInputStream(".gitlet/objects/" + firstTwo + "/" + last38 + ".ser");
                ObjectInputStream in = new ObjectInputStream(fileIn);
                snapshot = (TreeMap) in.readObject();
                in.close();
                fileIn.close();
            } catch(IOException i) {
                i.printStackTrace();
            } catch(ClassNotFoundException c) {
                System.out.println("remove class not found");
                c.printStackTrace();
            }
            //change all files to this commit id
            
            for(Map.Entry<String, String> entry : snapshot.entrySet()) {
            	String fileName = entry.getValue();
            	String oldFileLocation = entry.getKey();
            	String two = oldFileLocation.substring(0,2);
            	String thirtyEight = oldFileLocation.substring(2,40);
            	try {
            		compressor.decompressFile(new File(".gitlet/objects/" + two + "/" + thirtyEight), new File(fileName));
            	} catch(Exception e) {
            		System.out.println(e);
            	}


            }

            try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(".gitlet/branches/heads/" + branch + ".txt", false)))) {
                    out.println(snapshotLocation);
                    
                    
                } catch (Exception e) {
                    System.out.println(e);
                }
            
           
            	
            	
            

        }


		
	}
}