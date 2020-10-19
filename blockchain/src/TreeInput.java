
import java.io.*;
import java.util.*;
import java.io.File; // Import the File class
import java.io.IOException; // Import the IOException class to handle errors

public class TreeInput {

    public static void main(String[] args) throws IOException {

        Scanner s = new Scanner(System.in);
        String fileName = "";
        ArrayList<String> strings;
        String previousHash = "0";
        Block previousBlock = null;
        String newFile;
        String firstFile = "";
        Block block = null;
        boolean printTree = false;

        while (!fileName.equals("done")) {

            System.out.println(
                    "Enter the name of a file to add to the blockchain. If you have no more files to add, type \"done\", all lowercase");
            String input = s.next();
            if (input.equals("done")) {
                break;
            }
            fileName = input;
            if (previousBlock == null) {
                firstFile = fileName;
            }
            strings = readFile(fileName);
            if (strings != null) {
                Node root = MerkleTree.buildTree(strings);

                Header header = new Header(previousHash, root.getHash());

                block = new Block(header, previousBlock, root, fileName);
                badBlock.badNonce(new Block(new Header(block.getHeader().getPrevHash(), block.getHeader().getRootHash()), previousBlock, root, fileName));
                badBlock.incorrectHash(new Block(new Header(block.getHeader().getPrevHash(), block.getHeader().getRootHash()), previousBlock, root, fileName));
                previousBlock = block;
                previousHash = root.getHash();
            }

        }

        if (firstFile.indexOf('.') != -1) {
            newFile = firstFile.substring(0, firstFile.indexOf('.'));
        } else {
            newFile = firstFile;
        }
        newFile = newFile + ".out.txt";
        File myObj = new File(newFile);

        FileWriter myWriter = null;
        try {
            myWriter = new FileWriter(myObj);

            if(verify.verifyChain(block)){
                System.out.println("Valid chain");
            }
            else{
                System.out.println("Invalid chain");
            }

            String test = "lazy";
            if(verify.inchain(test, block, block)){
                System.out.println("inchain test for " + test + " passed");
            }
            else{
                System.out.println("inchain test for " + test + " failed");
            }

            test = "apple";
            if(verify.inchain(test, block, block)){
                System.out.println("inchain test for " + test + " passed");
            }
            else{
                System.out.println("inchain test for " + test + " failed");
            }

            while (block != null) {

                System.out.println("Would you like to print the Merkle Tree for " + block.getFileName() + "? respond with yes or no");
                String input = s.next();
                while(!input.equals("yes") && !input.equals("no")) {
                    System.out.println("Invalid input. Type \"yes\" or \"no\".");
                    input = s.next();
                }
                if(verify.verifyBlock(block)){
                    System.out.println("Block for " + block.getFileName() + " is valid");
                }
                else {
                    System.out.println("Block for " + block.getFileName() + "isn't valid");
                }
                if (input.equals("yes")) {
                    printTree = true;
                    printBlock(block, printTree, myWriter);
                    block = block.getPrevious();
                }
                else if(input.equals("no")){
                    printTree = false;
                    printBlock(block, printTree, myWriter);
                    block = block.getPrevious();
                }

            }
        } catch (IOException e) {
            System.out.println("Error writing to file");
        } finally {
            myWriter.close();
        }
        s.close();

    }

    public static ArrayList<String> readFile(String filename) throws IOException {

        ArrayList<String> strings = new ArrayList<>();

        String text;
        File file = null;
        try {
            file = new File(filename);
            BufferedReader br = new BufferedReader(new FileReader(file));
            try {
                while ((text = br.readLine()) != null) {
                    strings.add(text);
                }
            } catch (Exception e) {
                System.out.println("No such file");
            } finally {
                br.close();
            }

            Collections.sort(strings);
            return strings;
        } catch (Exception e) {
            System.out.println("File not found");
            return null;
        }

    }

    public static void printTree(Node root, FileWriter myWriter) throws IOException {

        if (root == null) {
            return;
        }
        int id = 1;
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty()) {

            Node node = queue.remove();

            if ((node instanceof InnerNode)) {
                if (((InnerNode) node).getLeft() != null) {
                    int childid = id * 2;
                    myWriter.write("Node ID: " + id + "\n");
                    myWriter.write("Left Child ID: " + childid + "\n");
                    myWriter.write("Edge Left <= " + ((InnerNode) node).getPrefix() + "\n");
                    myWriter.write("Node Hash: " + node.getHash() + "\n");
                    queue.add(((InnerNode) node).getLeft());
                }
                if (((InnerNode) node).getRight() != null) {
                    int childid = id * 2 + 1;
                    myWriter.write("Edge Right > " + ((InnerNode) node).getPrefix() + "\n");
                    myWriter.write("Right Child ID: " + childid + "\n");
                    queue.add(((InnerNode) node).getRight());
                }
            } else if ((node instanceof LeafNode)) {
                int childid = id * 2;
                myWriter.write("Node ID: " + id + "\n");
                myWriter.write("String: " + ((LeafNode) node).getData() + "\n");
                myWriter.write("Node Hash: " + node.getHash() + "\n");
            }
            myWriter.write("\n");
            myWriter.write("\n");
            id++;
        }
    }

    public static void printBlock(Block block, boolean printTree, FileWriter myWriter) throws IOException {
        
        myWriter.write("BEGIN BLOCK\n");
        myWriter.write("BEGIN HEADER\n");
        myWriter.write("hash of prev block: " + block.getHeader().getPrevHash() + "\n");
        myWriter.write("hash of the root: " + block.getHeader().getRootHash() + "\n");
        myWriter.write("time stamp: " + block.getHeader().getTimeStamp() + "\n");
        myWriter.write("target: " + block.getHeader().getTarget() + "\n");
        myWriter.write("nonce: " + block.getHeader().getNonce() + "\n");
        myWriter.write("END HEADER\n\n");

        if (printTree) {
            printTree(block.getRoot(), myWriter);
        }

        myWriter.write("END BLOCK\n\n");

    }

}
