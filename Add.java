import java.io.*;
import java.util.ArrayList;
import java.util.TreeMap;

public class Add {


	//hellohdhd
	//the main method of add, begins adding arg
	public static void main(String arg) {

		/*do stuff to added.txt. Source:
		http://stackoverflow.com/questions/5868369/how-to-read-a-large-text-file-line-by-line-using-java */

		
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

          if (new File(arg).exists() == false) {
            System.out.println("File does not exist.");
          } else if (hasFileChanged(arg) == false) {
            System.out.println("File has not been modified since the last commit.");
          }

          else if (removed.contains(arg)) {
            removed.remove(arg);
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

      		else if (!staged.contains(arg)) {

      			staged.add(arg);
      		}
      	
          
      	try {
      		FileOutputStream fileOut = new FileOutputStream(".gitlet/staged.ser");
        	ObjectOutputStream out = new ObjectOutputStream(fileOut);
        	out.writeObject(staged);
        	out.close();
        	fileOut.close();
        } catch (IOException e) {
        	e.printStackTrace();
        }

		

		
	} // hadfsd
//checks if file haschanged
	public static boolean hasFileChanged(String fileName) {
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

                
                TreeMap<Integer, ArrayList<String>> branchCommitLog = log.get(branch);
                
                int branchCommitLogSize = branchCommitLog.size();
               //line below failing
                
                

                  ArrayList<String> info = branchCommitLog.get(branchCommitLogSize - 1);
                  
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
                  
                
            } catch(Exception e) {
              System.out.println(e);
            }
            if (snapshot.get(fileName) != null) {
             
              String fileLocation = snapshot.get(fileName);
              
              if (hasher.sha1(new File(fileName)).equals(fileLocation)) {

                return false;
              }


            }
            






   

	} catch(Exception e) {
    System.out.println(e);
  }

	//check in objects folder for a folder collision
        			
  return true;      			

}

}
