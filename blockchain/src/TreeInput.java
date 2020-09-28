
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
            Node root = MerkleTree.buildTree(strings);

            Header header = new Header(previousHash, root.getHash());

            block = new Block(header, previousBlock, root);

            previousBlock = block;
            previousHash = root.getHash();

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

            while (block != null) {

                System.out.println("Would you like to print the Merkle Tree? respond with yes or no");
                if (s.next().equals("yes")) {
                    printTree = true;
                }
                printBlock(block, printTree, myWriter);
                block = block.getPrevious();
                printTree = false;

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
        File file = new File("/Users/hannahleland/Desktop/CSE 297/blockchain/blockchain/src/" + filename);
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