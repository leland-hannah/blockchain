import java.io.*;
import java.util.*;
import java.io.File; // Import the File class
import java.io.IOException; // Import the IOException class to handle errors
import java.util.Date;
import java.time.Instant;
import java.util.Calendar;
import java.math.BigInteger;


public class badBlock {
    
    public static void badNonce(Block block) throws IOException{
        Block newB = new Block(block.getHeader(), block.getPrevious(), block.getRoot(), block.getFileName());
        newB.getHeader().setNonce(computeBadNonce(newB.getRoot().getHash(), newB.getHeader().getTarget()));
        if(!verify.verifyBlock(newB)){
            System.out.println("Bad Nonce test passes");
        }
        else {
            System.out.println("Bad Nonce test didn't pass");
        }

    }

    public static void incorrectHash(Block block) throws IOException{

        Node root = new InnerNode(block.getRoot().getHash(), ((InnerNode)block.getRoot()).getLeft(), ((InnerNode)block.getRoot()).getRight(), ((InnerNode)block.getRoot()).getPrefix());
        Block newB = new Block(block.getHeader(), block.getPrevious(), root, block.getFileName());
        newB.getRoot().setHash("x");
        if(!verify.verifyBlock(newB)){
            System.out.println("Bad Hash test passes");
        }
        else {
            System.out.println("Bad Hash test didn't pass");
        }

    }

    public static String computeBadNonce(String rootHash, BigInteger target){

        BigInteger binary = null;
        String non;
        do{

            non = Integer.toString((int)(Math.random()*Integer.MAX_VALUE));
            String concat = rootHash + non;

            try{
                String hash = MerkleTree.toHexString(MerkleTree.getSHA(concat));
                String bin = Header.hexToBinary(hash);
                binary = new BigInteger(bin);
            }
            catch(Exception e){
                System.out.println("Something went wrong.");
            }

        }while(binary.compareTo(target) <= 0);

        return non;

    }
}
