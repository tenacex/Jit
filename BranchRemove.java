import java.io.*;

public class BranchRemove{
	public static void main(String branchToRemove) {

		
		try {
            BufferedReader headTxtFile = new BufferedReader(new FileReader(".gitlet/HEAD.txt"));
            String headTxtString = headTxtFile.readLine();
            String branch = headTxtString.substring(15, headTxtString.length());

            //checks if current branch is branch to remove
            if (branch.equals(branchToRemove)) {
            	System.out.println("Cannot remove the current branch.");
            //branch doesn't exist
            } else if (!(new File(".gitlet/branches/heads/" + branchToRemove + ".txt").exists())) {
				System.out.println("A branch with that name does not exist.");
			}
			//remove branch
			else {
				File toDelete = new File(".gitlet/branches/heads/" + branchToRemove + ".txt");
				toDelete.delete();
			}


        } catch (Exception e) {
        	System.out.println(e);
        }
	}
}