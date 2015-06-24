import java.io.*;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Map;

public class Logger {

    public static void globalLog() {
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
        for(Map.Entry<String,TreeMap<Integer, ArrayList<String>>> entry : log.entrySet()) {
            String branchName = entry.getKey();
            TreeMap<Integer, ArrayList<String>> commits = entry.getValue();
            
            int branchCommitLogSize = commits.size();
                while (branchCommitLogSize > 0) {
                    ArrayList<String> info = commits.get(branchCommitLogSize - 1);
                    System.out.println("====");
                    System.out.println("commit " + Integer.toString(branchCommitLogSize-1));
                    System.out.println(info.get(0));
                    System.out.println(info.get(1));
                    System.out.println("");
                    branchCommitLogSize -= 1;
                }
        }
    }

	public static void branchLog() {
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
            try {
            	BufferedReader headTxtFile = new BufferedReader(new FileReader(".gitlet/HEAD.txt"));
                String headTxtString = headTxtFile.readLine();
                String branch = headTxtString.substring(15, headTxtString.length());
                TreeMap<Integer, ArrayList<String>> branchCommitLog = log.get(branch);
                
                int branchCommitLogSize = branchCommitLog.size();
                while (branchCommitLogSize > 0) {
                	ArrayList<String> info = branchCommitLog.get(branchCommitLogSize - 1);
                	System.out.println("====");
                	System.out.println("commit " + Integer.toString(branchCommitLogSize-1));
                	System.out.println(info.get(0));
                	System.out.println(info.get(1));
                	System.out.println("");
                	branchCommitLogSize -= 1;
                }
            } catch(Exception e) {
            	System.out.println(e);
            }
            

	}
	public static void logCommit(String currentBranch, String commitMessage, String snapshot) {
		//load up old log
        
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

            //log treemap already has a key by the name of currentbranch
            
            if (log.containsKey(currentBranch)) {

            	TreeMap<Integer, ArrayList<String>> branch = log.get(currentBranch);
                
            	Integer lastCommitID = branch.lastEntry().getKey();
            	ArrayList<String> inputArray = new ArrayList<String>();
            	Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
                inputArray.add(sdf.format(date));
            	inputArray.add(commitMessage);
            	inputArray.add(snapshot);
            	branch.put(lastCommitID + 1, inputArray);
                

                

            } else {
                //new commit array
            	ArrayList<String> inputArray = new ArrayList<String>();
            	Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
                inputArray.add(sdf.format(date));
            	inputArray.add(commitMessage);
            	inputArray.add(snapshot);

            	TreeMap<Integer, ArrayList<String>> inputTreeMap = new TreeMap<Integer, ArrayList<String>>();
                try {
                    BufferedReader headTxtFile = new BufferedReader(new FileReader(".gitlet/HEAD.txt"));
                    String headTxtString = headTxtFile.readLine();
            	inputTreeMap.putAll(log.get(headTxtString.substring(15,headTxtString.length())));
                inputTreeMap.put(log.get((headTxtString.substring(15,headTxtString.length()))).lastEntry().getKey() + 1,inputArray);
                
            	log.put(currentBranch, inputTreeMap);
                
            }catch(Exception e) {
                System.out.println(e);
            }

            }
            


           
            //save .ser file
            try {

            FileOutputStream fileOutputlog = new FileOutputStream(".gitlet/log.ser");
            ObjectOutputStream outputlog = new ObjectOutputStream(fileOutputlog);
            outputlog.writeObject(log);
            outputlog.close();
            fileOutputlog.close();
        } catch(Exception e) {
        	System.out.println(e);
        }
        

	}


}