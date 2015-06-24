import java.io.*;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Scanner;

public class Rebase {
	public static void normalRebase(String mergingBranch) {
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
        ArrayList<String> currentBranchHead = currentBranchCommits.get(currentBranchCommits.size() - 1); //put this onto merging branch head
        ArrayList<String> mergingBranchHead = mergingBranchCommits.get(mergingBranchCommits.size() - 1);
        int size = mergingBranchCommits.size();
        while (currentBranchCommits.get(index) != null) {
        	ArrayList<String> input = currentBranchCommits.get(index);
        	mergingBranchCommits.put(size, input);
        	index += 1;
        }
        log.put(currentBranch, mergingBranchCommits);
        log.remove(mergingBranch);
        BranchRemove.main(mergingBranch);

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

	public static void interactiveRebase(String mergingBranch) {

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
        ArrayList<String> currentBranchHead = currentBranchCommits.get(currentBranchCommits.size() - 1); //put this onto merging branch head
        ArrayList<String> mergingBranchHead = mergingBranchCommits.get(mergingBranchCommits.size() - 1);
        int size = mergingBranchCommits.size();
        Scanner in = new Scanner(System.in);
        while (currentBranchCommits.get(index) != null) {
        	ArrayList<String> input = currentBranchCommits.get(index);
        	System.out.println("Currently replaying:");
        	System.out.println("====");
            System.out.println("commit " + Integer.toString(index));
            System.out.println(input.get(0));
            System.out.println(input.get(1));
            System.out.println("");
            System.out.println("Would you like to (c)ontinue, (s)kip this commit, or change this commit's (m)essage?");
            String s = in.nextLine();
            if (s.equals("c")) {
            	mergingBranchCommits.put(size, input);
            } else if (s.equals("s")) {

            } else if (s.equals("m")) {
            	System.out.println("Please enter a new message for this commit.");
            	String c = in.nextLine();
            	input.set(1, c);
            	mergingBranchCommits.put(size, input);
            } 
        	
        	index += 1;
        }
        log.put(currentBranch, mergingBranchCommits);
        log.remove(mergingBranch);
        BranchRemove.main(mergingBranch);

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