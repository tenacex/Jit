import java.io.*;
import java.util.TreeMap;
import java.util.Map;
import java.util.ArrayList;

public class Checkout {
	public static void main(String arg) {
		
			boolean isFileNameCheckout = isFileNameCheckoutMethod(arg);
			
			if (isFileNameCheckout) {
				restoreFileToHead(arg);
			} else {
				restoreToBranch(arg);
			}
		
	}

	public static void restoreToCommitID(String commitID, String fileName) {
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

            String oldFileLocation = snapshot.get(fileName);
            if (oldFileLocation == null) {
            	System.out.println("File does not exist in that commit.");
            } else {
            	String two = oldFileLocation.substring(0,2);
            	String thirtyEight = oldFileLocation.substring(2,40);
            	try {
            		compressor.decompressFile(new File(".gitlet/objects/" + two + "/" + thirtyEight), new File(fileName));
            	} catch(Exception e) {
            		System.out.println(e);
            	}
            }

        }


		
	}

	private static void restoreFileToHead(String fileName) {

		try {
			BufferedReader headTxtFile = new BufferedReader(new FileReader(".gitlet/HEAD.txt"));
            String headTxtString = headTxtFile.readLine();
            //Read branch head to find out location of most recent snapshot
			BufferedReader headObjectLocationFile = new BufferedReader(new FileReader(".gitlet/" + headTxtString + ".txt"));
            String headObjectLocationString = headObjectLocationFile.readLine();
            String firstTwo = headObjectLocationString.substring(0,2);
            String last38 = headObjectLocationString.substring(2, 40);

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

            String fileLocation = snapshot.get(fileName);
            String locationF2 = fileLocation.substring(0,2);
            String locationL38 = fileLocation.substring(2,40);
            compressor.decompressFile(new File(".gitlet/objects/" + locationF2 + "/" + locationL38), new File(fileName));
        } catch (Exception e) {
        	System.out.println(e);
        }

	}

	private static void restoreToBranch(String branchName) {
		writeHeadTxtFile(branchName);

		//loop through snapshot.ser and replace old files with new files
		try {
			BufferedReader headTxtFile = new BufferedReader(new FileReader(".gitlet/HEAD.txt"));
            String headTxtString = headTxtFile.readLine();
            //Read branch head to find out location of most recent snapshot
			BufferedReader headObjectLocationFile = new BufferedReader(new FileReader(".gitlet/" + headTxtString + ".txt"));
            String headObjectLocationString = headObjectLocationFile.readLine();
            String firstTwo = headObjectLocationString.substring(0,2);
            String last38 = headObjectLocationString.substring(2, 40);

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

            for (Map.Entry<String, String> entry : snapshot.entrySet()){
            	String fileName = entry.getKey();
            	String location = entry.getValue();
            	String locationF2 = location.substring(0,2);
            	String locationL38 = location.substring(2,40);
            	compressor.decompressFile(new File(".gitlet/objects/" + locationF2 + "/" + locationL38), new File(fileName));

            }

            
            
            
        } catch (Exception e) {
        	System.out.println(e);
        }

	

	}

	private static void writeHeadTxtFile(String branchName) {
		try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(".gitlet/HEAD.txt", false)))) {
            out.println("branches/heads/" + branchName);       
            } catch (Exception e) {
                System.out.println(e);
            }
	}


	//determines which type of checkout is being called
	private static boolean isFileNameCheckoutMethod(String arg) {
		if (new File(".gitlet/branches/heads/" + arg + ".txt").exists()) {
			return false;
		} else {
			return true;
		} 

	}
}