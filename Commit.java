// source for compression: http://stackoverflow.com/questions/6173920/zlib-compression-using-deflate-and-inflate-classes-in-java
import java.util.zip.*;
import java.util.List;

import java.security.MessageDigest;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Arrays;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Map;


import java.io.*;

public class Commit {
	public static void main(String message) {
        
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
                return;
            } catch(ClassNotFoundException c)
            {
                System.out.println("Stage class not found");
                c.printStackTrace();
                return;
            }
        //staged contains all staged files


        //No files are staged, abort
        if (staged.isEmpty()) {
            System.out.println("No changes added to the commit.");
            return;
        }

        //No commit message, abort
        else if (message == null) {
            System.out.println("Please enter a commit message.");
            return;
        }

        //Continue with commit process
        else {
            try{ 
                //read HEAD.txt to find out which branch we are on
                commitMessage(message);

                BufferedReader headTxtFile = new BufferedReader(new FileReader(".gitlet/HEAD.txt"));
                String headTxtString = headTxtFile.readLine();
                try {
                    //Read branch head to find out location of most recent snapshot
                    BufferedReader headObjectLocationFile = new BufferedReader(new FileReader(".gitlet/" + headTxtString + ".txt"));
                    String headObjectLocationString = headObjectLocationFile.readLine();
                    try {
                        //access snapshot file
                        String snapshotObjectFolder = headObjectLocationString.substring(0,2);
                        String snapshotObjectFileName = headObjectLocationString.substring(2,40);

                        
                        //creates a new snapshot using staged files and old snapshot
                         
                        String snapshotLocation = createNewSnapshot(headObjectLocationString, staged, headTxtString);
                        clearRemoved();
                        clearStaged();
                        Logger.logCommit(headTxtString.substring(15, headTxtString.length()), message, snapshotLocation);

                    } catch (Exception e) {
                        System.out.println(e);
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            } catch (Exception e) {
                System.out.println(e);
            }

        }
    }


	private static void clearStaged() {
		//clear staged.ser file
        ArrayList<String> staged = new ArrayList<String>();
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

    private static void clearRemoved() {
        //clear staged.txt file
         ArrayList<String> removed = new ArrayList<String>();
            try {
                FileOutputStream fileOut = new FileOutputStream(".gitlet/removed.ser");
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(removed);
                out.close();
                fileOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }   
    }

    private static String createNewSnapshot(String headObjectString, ArrayList<String>alreadyStaged, String headTextString) {
        String returnString = "temp";
        try {
                //load removed into HashSet in order to not include in next snapshot
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


                double random = Math.random();
                //just a temporary file for creating a random hash
                new File(Double.toString(random)).createNewFile();
                BufferedWriter writer = new BufferedWriter(new FileWriter(Double.toString(random), false));
                writer.write(Double.toString(random));
                writer.close();
          

            

            String randomHash = hasher.sha1(new File(Double.toString(random)));
            returnString = randomHash;
            new File(Double.toString(random)).delete();
            

            String firstTwo = randomHash.substring(0,2);
            String last38 = randomHash.substring(2, 40);
            writeNewHead(randomHash, headTextString);

            new File(".gitlet/objects/" + firstTwo).mkdir();
            
            TreeMap<String, String> snapShot = new TreeMap<String, String>();
            //deserialize old snapshot
            TreeMap<String,String> oldSnapShot = null;
            try {
                FileInputStream fileIn = new FileInputStream(".gitlet/objects/" + headObjectString.substring(0,2) +
                    "/" + headObjectString.substring(2,40) + ".ser");
                ObjectInputStream in = new ObjectInputStream(fileIn);
                oldSnapShot = (TreeMap) in.readObject();
                in.close();
                fileIn.close();
            }catch(IOException i) {
                i.printStackTrace();

            }catch(ClassNotFoundException c) {
                System.out.println("Employee class not found");
                c.printStackTrace();

            }
            //loop through old snapshot entries
            for(Map.Entry<String,String> entry : oldSnapShot.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (alreadyStaged.contains(key) && !removed.contains(key)) {
                    snapShot.put(key,hasher.sha1(new File(key)));
                    processLine(key);
                } else if (!removed.contains(key)) {
                    snapShot.put(key, value);
                }
            }
            //loop through the staged files and see if there are new files to add
            for (String file : alreadyStaged) {
                if (!snapShot.containsKey(file) && !removed.contains(file)) {
                    snapShot.put(file,hasher.sha1(new File(file)));
                    processLine(file);
                }
            }
            
           
            try {
                FileOutputStream fileOut = new FileOutputStream(".gitlet/objects/" + firstTwo + "/" + last38 + ".ser");
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(snapShot);
                out.close();
                fileOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

         } catch (Exception e) {
            System.out.println(e);
         }
         return returnString;



    }


    //reads the staged.txt file and returns each file as a String
    //     in a hashset instance variable
    

    public static void writeNewHead(String newHash, String headTxtString) {
        try {

                BufferedWriter writer = new BufferedWriter(new FileWriter(".gitlet/" + headTxtString + ".txt", false));
                writer.write(newHash);
                writer.close();
                
            } catch (IOException e) {
                //couldn't write commit message
            }
       

    }
    //testinghfhfsdsfhdffhws
	private static void commitMessage(String message) {
		try {
    			BufferedWriter commitWriter = new BufferedWriter(new FileWriter(".gitlet/COMMIT_MSG.txt", false));
    			commitWriter.write(message);
    			commitWriter.close();
    		} catch (IOException e) {
    			//couldn't write commit message
    		}
	}

	private static void processLine(String line) {
		try { 

        			/*   hash the file, and save the first 2 and  //
        			//   the last 38 characters as seperate String */
        			File file = new File(line);
        			String fileHash = hasher.sha1(file);
        			String firstTwo = fileHash.substring(0,2);
        			String last38 = fileHash.substring(2, 40);
        			
        			//create new compressed file
        			if (!(new File(".gitlet/objects/" + firstTwo).isDirectory())){
        				new File(".gitlet/objects/" + firstTwo).mkdir();
        			}
        			File compressed = new File(".gitlet/objects/" + firstTwo + "/" + last38);
        			compressor.compressFile(file, compressed);

        		} catch (Exception e) {
        			//error handling
        		}
		}

	    

}