import java.io.*;
import java.util.ArrayList;

public class Status {
	public static void main() {
		//call method to read staged files
		readStaged();
	}

	public static void readStaged() {
		//print out staged files
		System.out.println("=== Branches ===");

		try(BufferedReader br = new BufferedReader(new FileReader(".gitlet/HEAD.txt"))) {
    			String line = br.readLine();
    			int length = line.length();
    			String currentBranch = line.substring(15,length);
    			
    			File folder = new File(".gitlet/branches/heads");
    			File[] listOfFiles = folder.listFiles();
    			for (int i = 0; i < listOfFiles.length; i++) {
    				String file = listOfFiles[i].getName();
    				String printedFile = file.substring(0,file.length() - 4);
    				if (!printedFile.equals(currentBranch)) {
    					System.out.println(printedFile);
    				} else {
    					System.out.println("*" + printedFile);
    				}
    			}

		} catch (IOException e) {
				//process error
		}

		

		//check HEAD file for branch, NOT YET IMPLEMENTED!!!!!
		

		System.out.println("=== Staged Files ===");

		//read added
		ArrayList<String> staged = new ArrayList<String>();
      		try
      		{		
        		FileInputStream fileIn = new FileInputStream(".gitlet/staged.ser");
        		ObjectInputStream in = new ObjectInputStream(fileIn);
        		staged = (ArrayList) in.readObject();
        		in.close();
        		fileIn.close();
      		} catch(IOException i)
      		{
         		i.printStackTrace();
         		
      		} catch(ClassNotFoundException c)
      		{
         		System.out.println("Stage class not found");
         		c.printStackTrace();
         		
      		}

      		for (String line : staged) {
      			System.out.println(line);
      		}

		//Check removal, NOT YET IMPLEMENETED!!!!
		System.out.println("");
		System.out.println("=== Files Marked for Removal ===");
		
		ArrayList<String> removed = new ArrayList<String>();
                try {       
                    FileInputStream fileIn = new FileInputStream(".gitlet/removed.ser");
                    ObjectInputStream in = new ObjectInputStream(fileIn);
                    removed = (ArrayList) in.readObject();
                    in.close();
                    fileIn.close();
                } catch(IOException i) {
                    i.printStackTrace();
                } catch(ClassNotFoundException c) {
                    System.out.println("remove class not found");
                    c.printStackTrace();
                }
        for (String line : removed) {
        	System.out.println(line);
        }
	}
}