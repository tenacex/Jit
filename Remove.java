import java.io.*;
import java.util.HashSet;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;
import java.util.ArrayList;

public class Remove {

	public static void main(String fileToRemove) {

        ArrayList<String> staged = new ArrayList<String>();
        try {       
            FileInputStream fileIn = new FileInputStream(".gitlet/staged.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            staged = (ArrayList) in.readObject();
            in.close();
            fileIn.close();
        } catch(IOException i) {
            i.printStackTrace();
        } catch(ClassNotFoundException c) {
            System.out.println("Stage class not found");
            c.printStackTrace();
        }

        //file has been staged, remove from staging area
        if (staged.contains(fileToRemove)) {
            staged.remove(fileToRemove);
            
            try {
                FileOutputStream fileOut = new FileOutputStream(".gitlet/staged.ser");
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(staged);
                out.close();
                fileOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

		else {
        	//File to remove is not in staged files, check last snapshot
        	try {
        		BufferedReader headTxtFile = new BufferedReader(new FileReader(".gitlet/HEAD.txt"));
            	String headTxtString = headTxtFile.readLine();
            
            	BufferedReader headObjectLocationFile = new BufferedReader(new FileReader(".gitlet/" + headTxtString + ".txt"));
            	String headObjectLocationString = headObjectLocationFile.readLine();
                    
            	String snapshotObjectFolder = headObjectLocationString.substring(0,2);
            	String snapshotObjectFileName = headObjectLocationString.substring(2,40);

                //snapshotobject location is where the snapshot is saved as a .ser
                TreeMap<String, String> snapshot = new TreeMap<String, String>();
                try {       
                    FileInputStream fileIn = new FileInputStream(".gitlet/objects/" + 
                        snapshotObjectFolder + "/" + snapshotObjectFileName + ".ser");

                    ObjectInputStream in = new ObjectInputStream(fileIn);
                    snapshot = (TreeMap) in.readObject();
                    in.close();
                    fileIn.close();
                } catch(IOException i) {
                    i.printStackTrace();
                
                } catch(ClassNotFoundException c) {
                    System.out.println("Stage class not found");
                    c.printStackTrace();
                }

                if (snapshot.containsKey(fileToRemove)) {
                    ArrayList<String> removed = new ArrayList<String>();
                    try {       
                        FileInputStream fileInput = new FileInputStream(".gitlet/removed.ser");
                        ObjectInputStream input = new ObjectInputStream(fileInput);
                        removed = (ArrayList) input.readObject();
                        input.close();
                        fileInput.close();
                    } catch(IOException i) {
                        i.printStackTrace();
                    } catch(ClassNotFoundException c) {
                        System.out.println("Stage class not found");
                        c.printStackTrace();
                    }
                    if (!removed.contains(fileToRemove)) {
                        removed.add(fileToRemove);
                    }

                    //write removed.ser
                    try {
                        FileOutputStream fileOut = new FileOutputStream(".gitlet/removed.ser");
                        ObjectOutputStream out = new ObjectOutputStream(fileOut);
                        out.writeObject(removed);
                        out.close();
                        fileOut.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    System.out.println("No reason to remove the file.");
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        } 
	}

	

}