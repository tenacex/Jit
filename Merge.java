import java.io.*;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Map;


public class Merge {
	public static void main(String mergingBranch) {
		String currentBranch = "temp";
		try {
            	BufferedReader headTxtFile = new BufferedReader(new FileReader(".gitlet/HEAD.txt"));
                String headTxtString = headTxtFile.readLine();
                currentBranch = headTxtString.substring(15, headTxtString.length());
            } catch(Exception e) {
            	System.out.println(e);
            }
        //load up global log
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
        TreeMap<Integer, ArrayList<String>> mergingBranchCommits = log.get(mergingBranch);
        TreeMap<Integer, ArrayList<String>> currentBranchCommits = log.get(currentBranch);
        
        ArrayList<String> currentBranchCommit = currentBranchCommits.get(0);
        ArrayList<String> mergingBranchCommit = mergingBranchCommits.get(0);
        int index = 1;
        boolean foundSnapshot = false;
        while(foundSnapshot == false) {
        	if (mergingBranchCommits.get(index) == null) {
        		index -= 1;
        		foundSnapshot = true;

        	}
        	else if (currentBranchCommits.get(index) == null) {
        		index -= 1;
        		foundSnapshot = true;
        	}
        	else if (currentBranchCommits.get(index).get(2).equals(mergingBranchCommits.get(index).get(2))) {
        		index += 1;
        	} else {
        		index -= 1;
        		foundSnapshot = true;
        	}
        }
        //split point snapshot
        ArrayList<String> splitPoint = currentBranchCommits.get(index);
        ArrayList<String> currentBranchHead = currentBranchCommits.get(currentBranchCommits.size() - 1);
        ArrayList<String> mergingBranchHead = mergingBranchCommits.get(mergingBranchCommits.size() - 1);
        
        String currentSnapshotLocation = currentBranchHead.get(2);
        String mergingSnapshotLocation = mergingBranchHead.get(2);
        String splitpointSnapshotLocation = splitPoint.get(2);

        String firstTwoSplit = splitpointSnapshotLocation.substring(0,2);
        String last38Split = splitpointSnapshotLocation.substring(2, 40);

        String firstTwoCurrent = currentSnapshotLocation.substring(0,2);
        String last38Current = currentSnapshotLocation.substring(2, 40);

        String firstTwoMerging = mergingSnapshotLocation.substring(0,2);
        String last38Merging = mergingSnapshotLocation.substring(2, 40);



        TreeMap<String, String> snapshotSplit = new TreeMap<String, String>();
        TreeMap<String, String> snapshotCurrent = new TreeMap<String, String>();
        TreeMap<String, String> snapshotMerging = new TreeMap<String, String>();



        try {       
                FileInputStream fileIn = new FileInputStream(".gitlet/objects/" + firstTwoSplit + "/" + last38Split + ".ser");
                ObjectInputStream in = new ObjectInputStream(fileIn);
                snapshotSplit = (TreeMap) in.readObject();
                in.close();
                fileIn.close();
        } catch(IOException i) {
                i.printStackTrace();
        } catch(ClassNotFoundException c) {
                System.out.println("remove class not found");
                c.printStackTrace();
        }

        try {       
                FileInputStream fileIn = new FileInputStream(".gitlet/objects/" + firstTwoCurrent + "/" + last38Current + ".ser");
                ObjectInputStream in = new ObjectInputStream(fileIn);
                snapshotCurrent = (TreeMap) in.readObject();
                in.close();
                fileIn.close();
        } catch(IOException i) {
                i.printStackTrace();
        } catch(ClassNotFoundException c) {
                System.out.println("remove class not found");
                c.printStackTrace();
        }

        try {       
                FileInputStream fileIn = new FileInputStream(".gitlet/objects/" + firstTwoMerging + "/" + last38Merging + ".ser");
                ObjectInputStream in = new ObjectInputStream(fileIn);
                snapshotMerging = (TreeMap) in.readObject();
                in.close();
                fileIn.close();
        } catch(IOException i) {
                i.printStackTrace();
        } catch(ClassNotFoundException c) {
                System.out.println("remove class not found");
                c.printStackTrace();
        }
        TreeMap<String, String> newCommit = new TreeMap<String, String>();

        for(Map.Entry<String,String> entry : snapshotCurrent.entrySet()) {
        	String currentFile = entry.getKey();
        	String currentFileLocation = entry.getValue();
        	//file hasn't changed since split in current branch
        	if (currentFileLocation.equals(snapshotSplit.get(currentFile))) {
        		//file hasn't changed in merging branch
        		if (currentFileLocation.equals(snapshotMerging.get(currentFile))) {
        			newCommit.put(currentFile, currentFileLocation);
        		} else {
        			//file has changed in merging branch
        			newCommit.put(currentFile, snapshotMerging.get(currentFile));
        			try {
        				compressor.decompressFile(new File(".gitlet/objects/" + currentFileLocation.substring(0,2)+ "/" 
        					+ currentFileLocation.substring(2,40)), new File(currentFile));
        			}catch(Exception e){
        				System.out.println(e);
        			}
        		}
        	} else {
        		//file has changed since split in current branch
        		//file hasn't changed in merging branch
        		if (currentFileLocation.equals(snapshotMerging.get(currentFile))) {
        			newCommit.put(currentFile, currentFileLocation);
        		} else {
        			newCommit.put(currentFile, currentFileLocation);
        			try {
        				compressor.decompressFile(new File(".gitlet/objects/" + snapshotMerging.get(currentFile).substring(0,2)+ "/" 
        				+ snapshotMerging.get(currentFile).substring(2,40)), new File(currentFile + ".conflict"));
        			}catch(Exception e) {
        				System.out.println(e);
        			}
        		}
        	}
        }

        for(Map.Entry<String,String> entry : snapshotMerging.entrySet()) {
        	String mergingFile = entry.getKey();
        	String mergingFileLocation = entry.getValue();
        	if (newCommit.get(mergingFile) == null) {
        		newCommit.put(mergingFile, mergingFileLocation);
        	}
        }

        double random = Math.random();
                //just a temporary file for creating a random hash
        try {
        	new File(Double.toString(random)).createNewFile();
        	BufferedWriter writer = new BufferedWriter(new FileWriter(Double.toString(random), false));
        	writer.write(Double.toString(random));
        	writer.close();
        }catch(Exception e){
        	System.out.println(e);
        }
        String headTxtString = "temp";
          try {
          	BufferedReader headTxtFile = new BufferedReader(new FileReader(".gitlet/HEAD.txt"));
        	headTxtString = headTxtFile.readLine();
    } catch(Exception e) {
    	System.out.println(e);
    }

            
    	String randomHash = "jifejfe";
    	try {
    		randomHash = hasher.sha1(new File(Double.toString(random)));
    	}catch(Exception e){
    		System.out.println(e);
    	}
        
        new File(Double.toString(random)).delete();
            

        String firstTwo = randomHash.substring(0,2);
        String last38 = randomHash.substring(2, 40);
        Commit.writeNewHead(randomHash, headTxtString);
        //make snapshot directory
        new File(".gitlet/objects/" + firstTwo).mkdir();
        //write out new snapshot
        try {
                FileOutputStream fileOut = new FileOutputStream(".gitlet/objects/" + firstTwo + "/" + last38 + ".ser");
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(newCommit);
                out.close();
                fileOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
       
        Logger.logCommit(headTxtString.substring(15, headTxtString.length()), "merge with " + mergingBranch, randomHash);


	}
}