
import java.io.*;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Init {
	public static void main() {
		/* creating directory source http://stackoverflow.com/questions/3634853/how-to-create-a-directory-in-java */
		File dir = new File(".gitlet");
		if (!dir.exists()) {
			boolean result = false;
			//make .gitlet folder
			try {
        		dir.mkdir();
        		//put nested branches folder inside .gitlet
        		new File(".gitlet/branches").mkdir(); 
        		//put nested heads and remotes folders inside branches
        		new File(".gitlet/branches/heads").mkdir();
        		new File(".gitlet/branches/remotes").mkdir();
        		//put nested origin and skeleton folders inside remotes
        		new File(".gitlet/branches/remotes/origin").mkdir();
        		new File(".gitlet/branches/remotes/skeleton").mkdir();
        		//put nested objects folder inside .gitlet
        		new File(".gitlet/objects").mkdir(); 
        		//put nested logs folder inside .gitlet
        		new File(".gitlet/logs").mkdir(); 
                //put nested refs folder inside .gitlet
                new File(".gitlet/refs").mkdir();
                //add heads and remotes folders to refs
                new File(".gitlet/refs/heads").mkdir();
                new File(".gitlet/refs/remotes").mkdir();
        		//attempt creating txt files
        		try { 
                    //write staged ser
                    
                    ArrayList<String> staged = new ArrayList<String>();
                    FileOutputStream fileOut = new FileOutputStream(".gitlet/staged.ser");
                    ObjectOutputStream out = new ObjectOutputStream(fileOut);
                    out.writeObject(staged);
                    out.close();
                    fileOut.close();
                    //write removed ser
                    ArrayList<String> removed = new ArrayList<String>();
                    FileOutputStream fileOutput = new FileOutputStream(".gitlet/removed.ser");
                    ObjectOutputStream output = new ObjectOutputStream(fileOutput);
                    output.writeObject(removed);
                    output.close();
                    fileOutput.close();

                    TreeMap<String, String> snapshot = new TreeMap<String, String>();
                    new File(".gitlet/objects/12").mkdir();
                    FileOutputStream fileOutputsnap = new FileOutputStream(".gitlet/objects/12/34567890123456789012345678901234567890.ser");
                    ObjectOutputStream outputsnap = new ObjectOutputStream(fileOutputsnap);
                    outputsnap.writeObject(snapshot);
                    outputsnap.close();
                    fileOutputsnap.close();

                    ArrayList<String> logArray = new ArrayList<String>();
                    Date date = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
                    logArray.add(sdf.format(date));
                    logArray.add("initial commit");
                    logArray.add("1234567890123456789012345678901234567890");

                    
                    TreeMap<Integer, ArrayList<String>>  commitID = new TreeMap<Integer, ArrayList<String>>();
                    commitID.put(0,logArray);

                    TreeMap<String, TreeMap<Integer, ArrayList<String>>> finalLog = new TreeMap<String, TreeMap<Integer, ArrayList<String>>>();
                    finalLog.put("master", commitID);
                    

                    //write out 

                    
                   
                    FileOutputStream fileOutputlog = new FileOutputStream(".gitlet/log.ser");
                    ObjectOutputStream outputlog = new ObjectOutputStream(fileOutputlog);
                    outputlog.writeObject(finalLog);
                    outputlog.close();
                    fileOutputlog.close();

        			new File(".gitlet/COMMIT_MSG.txt").createNewFile();
                    new File(".gitlet/HEAD.txt").createNewFile();
        			new File(".gitlet/MERSE_MSG.txt").createNewFile();
        		
                    new File(".gitlet/branches/heads/master.txt").createNewFile();
                    
        		} catch (IOException e) {
        			e.printStackTrace();
        		}
        		result = true;
    		} 
    		catch(SecurityException se){
        		//handle security exception
    		}        
    		if(result) {    
    			try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(".gitlet/HEAD.txt", true)))) {
                    out.println("branches/heads/master");
                } catch (Exception e) {
                    System.out.println(e);
                }

                try(PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(".gitlet/branches/heads/master.txt", true)))) {
                    out.println("1234567890123456789012345678901234567890");
                    
                    
                } catch (Exception e) {
                    System.out.println(e);
                }




                Writer writer = null;
    			

       
                //write initial commit to COMMIT_MSG.txt
				


                try {
    				writer = new BufferedWriter(new OutputStreamWriter(
          			new FileOutputStream(".gitlet/COMMIT_MSG.txt"), "utf-8"));
    				writer.write("initial commit");
				} catch (IOException ex) {
                	// report
				} finally {
   					try {writer.close();} catch (Exception ex) {}
				}
        		System.out.println(".gitlet directory created");  
    		}
    		//already initialized a .gitlet folder
		} else {
			System.out.println("A gitlet version control system already exists in the current directory");
		}
	}
}