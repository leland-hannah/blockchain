
import java.io.*;
import java.util.*;
import java.io.File;  // Import the File class
import java.io.IOException;  // Import the IOException class to handle errors

public class TreeInput {

    public static void main(String [] args) throws IOException {

        Scanner s = new Scanner(System.in);
        String fileName = "";
        ArrayList<String> strings;
        String previousHash = "0";
        Block previousBlock = null;
        String newFile;
        String firstFile = "";
        Block block = null;

        while(!fileName.equals("done")){

            System.out.println("Enter the name of a file to add to the blockchain. If you have no more files to add, type \"done\", all lowercase");
            fileName = s.next();
            if(previousBlock == null){
                firstFile = fileName;
            }
            strings = readFile(fileName);
            Node root = MerkleTree.buildTree(strings);

            Header header = new Header(previousHash, root.getHash());

            block = new Block(header, previousBlock, root);

            previousBlock = block;
            previousHash = root.getHash();

        }
        
        if(firstFile.indexOf('.') != -1) {
         newFile = firstFile.substring(0, firstFile.indexOf('.'));
        }
        else{
            newFile = firstFile;
        }
        newFile = newFile + ".out.txt";
        File myObj = new File(newFile);
        
        while(block != null){

            printBlock(block, false, myObj);
            block = block.getPrevious();

        }

        s.close();
    
    }
    public static ArrayList<String> readFile(String filename) throws IOException {

        ArrayList<String> strings = new ArrayList<>();

        String text;
        BufferedReader br = new BufferedReader(new FileReader(filename));
        try {
            while ((text = br.readLine()) != null) {   
                strings.add(text);
            } 
        } 
        catch(Exception e){
            System.out.println("No such file");
        }
        finally {
            br.close();
        }

        Collections.sort(strings);
        return strings;

    }
    
    public static void printTree(Node root, File file) throws IOException {
        
        FileWriter myWriter = null;
        try {
            myWriter = new FileWriter(file);
            
        
            if (root == null) {
                return;
            }
            int id = 1;
            Queue<Node> queue = new LinkedList<>();
            queue.add(root);
       

            while (!queue.isEmpty()) {
                    
                Node node = queue.remove();          
            
                if ((node instanceof InnerNode)) {
                    if (((InnerNode)node).getLeft() != null) {
                        int childid = id * 2;
                        myWriter.write("Node ID: " + id + "\n");
                        myWriter.write("Left Child ID: " + childid + "\n");
                        myWriter.write("Edge Left <= " + ((InnerNode)node).getPrefix() + "\n");
                        myWriter.write("Node Hash: " + node.getHash() + "\n");
                        queue.add(((InnerNode)node).getLeft());
                    }
                    if(((InnerNode)node).getRight() != null) {
                        int childid = id * 2 + 1;
                        myWriter.write("Edge Right > " + ((InnerNode)node).getPrefix() + "\n");
                        myWriter.write("Right Child ID: " + childid + "\n");
                        queue.add(((InnerNode)node).getRight());
                    }
                }
                else if((node instanceof LeafNode)) {
                    int childid = id * 2;
                    myWriter.write("Node ID: " + id + "\n");
                    myWriter.write("String: " + ((LeafNode)node).getData() + "\n");
                    myWriter.write("Node Hash: " + node.getHash() + "\n");
                }
                myWriter.write("\n");
                myWriter.write("\n");
                id++;
            }
        }
        catch(IOException e){
            System.out.println("Error writing to file");
        }
        finally{
            myWriter.close();
        }
    }    
    public static void printBlock(Block block, boolean printTree, File file) throws IOException{
        FileWriter myWriter = null;
        try {
            myWriter = new FileWriter(file);

            myWriter.write("BEGIN BLOCK\n");
            myWriter.write("BEGIN HEADER\n");
            myWriter.write("hash of prev block: " + block.getHeader().getPrevHash() + "\n");
            myWriter.write("hash of the root: " + block.getHeader().getRootHash() + "\n");
            myWriter.write("time stamp: " + block.getHeader().getTimeStamp() + "\n");
            myWriter.write("target: " + block.getHeader().getTarget() + "\n");
            myWriter.write("nonce: " + block.getHeader().getNonce() + "\n");
            myWriter.write("END HEADER\n");

            if(printTree){
                printTree(block.getRoot(), file);
            }

            myWriter.write("END BLOCK\n\n");

        }

        catch(IOException e){
            System.out.println("Error writing to file");
        }
        finally{
            myWriter.close();
        }
    }
    
}