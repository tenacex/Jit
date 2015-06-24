public class Gitlet {
    public static void main(String[] args) {
    	/* calls the Init.java file main method */
    	if (args[0].equals("init")){
    		Init.main();

    	/* calls the Add.java file main method */
    	} else if (args[0].equals("add")) {
    		try {
                Add.main(args[1]);
            } catch(Exception e) {
                System.out.println("Please specify a file to add.");
            }
    		
        /* calls the Status.java file main method */
    	} else if (args[0].equals("status")) {
            Status.main();
        /* calls the Commit.java file main method */
        } else if (args[0].equals("commit")) {

            try {
                Commit.main(args[1]);
            } catch(ArrayIndexOutOfBoundsException e) {
                System.out.println("Please enter a commit message");
            }
        } else if (args[0].equals("rm")) {
            try {
                Remove.main(args[1]);
            } catch(ArrayIndexOutOfBoundsException e) {
                System.out.println("Please designate a file to remove from future commits.");
            }
        } else if (args[0].equals("checkout")) {
            try {
                Checkout.restoreToCommitID(args[1], args[2]);
            } catch(ArrayIndexOutOfBoundsException e) {
                try {
                    Checkout.main(args[1]);
                } catch (ArrayIndexOutOfBoundsException error) {
                    System.out.println("Please choose something to checkout to.");
                }
            }
        } else if (args[0].equals("branch")) {
            try {
                Branch.createNew(args[1]);
            } catch (ArrayIndexOutOfBoundsException error) {
                System.out.println("please select a branch name");
            }
        } else if (args[0].equals("log")) {
            Logger.branchLog();
        } else if (args[0].equals("find")) {
            Find.main(args[1]);
        } else if (args[0].equals("global-log")) {
            Logger.globalLog();
        } else if (args[0].equals("rm-branch")) {
            BranchRemove.main(args[1]);
        } else if(args[0].equals("merge")) {
            Merge.main(args[1]);
        } else if (args[0].equals("rebase")) {
            Rebase.normalRebase(args[1]);
        } else if (args[0].equals("i-rebase")) {
            Rebase.interactiveRebase(args[1]);
        }


    }
}