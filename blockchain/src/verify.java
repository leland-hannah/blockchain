import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

public class verify {

    public static boolean verifyBlock(Block block) throws IOException{
        
        try{
            String concat = block.getHeader().getRootHash() + block.getHeader().getNonce();
            String hash = MerkleTree.toHexString(MerkleTree.getSHA(concat));
            String bin = Header.hexToBinary(hash);
            BigInteger binary = new BigInteger(bin);
            if((binary).compareTo(block.getHeader().getTarget()) > 0){
                return false;
            }
        }
        catch (Exception e){
            System.out.println("Something went wrong");
            return false;
        }

        return checkNode(block.getRoot());

        
    }

    public static boolean checkNode(Node currentNode) throws IOException{
        if(currentNode instanceof LeafNode){
            try {
                String dataHash = MerkleTree.toHexString(MerkleTree.getSHA(((LeafNode) currentNode).getData()));
                return currentNode.getHash().compareTo(dataHash) == 0;
                
            }
            catch (Exception e){
                System.out.println("Something went wrong");
                return false;
            }
            
        }
        try{
            InnerNode curr = (InnerNode) currentNode;
            String concat = curr.getLeft().getHash() + curr.getRight().getHash();
            String hash = MerkleTree.toHexString(MerkleTree.getSHA(concat));
        
            if(!hash.equals(curr.getHash())){
                return false;
            }

            if(((InnerNode) currentNode).getLeft() != null && (((InnerNode) currentNode).getRight() != null)){
                return checkNode(((InnerNode) currentNode).getLeft()) && checkNode(((InnerNode) currentNode).getRight());
            }
            else if (((InnerNode) currentNode).getLeft() != null){
                return checkNode(((InnerNode) currentNode).getLeft());
            }
            else {
                return checkNode(((InnerNode) currentNode).getRight());
            }

        }
        catch (Exception e){
            System.out.println("Something went wrong");
            return false;
        }
    
    }
}
