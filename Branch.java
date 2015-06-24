import java.io.*;
import java.util.TreeMap;
import java.util.ArrayList;
public class Branch {

	public static void createNew(String branchName) {
		//check if branch exists
		if (new File(".gitlet/branches/heads/" + branchName + ".txt").exists()) {
			System.out.println("A branch with that name already exists.");
		} else {
			//create new branch
			
			try {
				new File(".gitlet/branches/heads/" + branchName + ".txt").createNewFile();
				BufferedReader headTxtFile = new BufferedReader(new FileReader(".gitlet/HEAD.txt"));
                String headTxtString = headTxtFile.readLine();
                
                //Read branch head to find out location of most recent snapshot
                BufferedReader headObjectLocationFile = new BufferedReader(new FileReader(".gitlet/" + headTxtString + ".txt"));
                String headObjectLocationString = headObjectLocationFile.readLine();
			//write snapshot location to branch text file

				try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(".gitlet/branches/heads/" + branchName + ".txt", true)))) {
                    out.println(headObjectLocationString);
                    
                } catch (Exception e) {
                    System.out.println(e);
                }

            } catch (Exception e) {
            	System.out.println(e);
            }

            try {
              BufferedReader headTxtFile = new BufferedReader(new FileReader(".gitlet/HEAD.txt"));
                String headTxtString = headTxtFile.readLine();
                String branch = headTxtString.substring(15, headTxtString.length());
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
                
            	} catch(ClassNotFoundException c)
            {	
                System.out.println("Stage class not found");
                
            }

                
                TreeMap<Integer, ArrayList<String>> branchCommitLog = log.get(headTxtString.substring(15, headTxtString.length()));
                TreeMap<Integer, ArrayList<String>> input = new TreeMap<Integer, ArrayList<String>>();
                input.putAll(branchCommitLog);
                log.put(branchName, input);
                
                try {
                	FileOutputStream fileOut = new FileOutputStream(".gitlet/log.ser");
                	ObjectOutputStream out = new ObjectOutputStream(fileOut);
                	out.writeObject(log);
                	out.close();
                	fileOut.close();
            	} catch (IOException e) {
                	e.printStackTrace();
            	}

		} catch (Exception e) {
			System.out.println(e);
		}
	}
	}
}