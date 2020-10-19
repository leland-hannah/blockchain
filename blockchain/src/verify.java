import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.Arrays;

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
            System.out.println("Something went wrong" + e);
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
                System.out.println("Something went wrong"+e);
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
            System.out.println("Something went wrong"+e);
            return false;
        }
    
    }
    public static boolean verifyChain(Block block) throws IOException{

        if(!block.getHeader().getPrevHash().equals("0")){
            return verifyBlock(block) && verifyChain(block.getPrevious());
        }
        else{
            if(block.getPrevious() != null){
                return (block.getHeader().getPrevHash().equals(block.getPrevious().getHeader().getRootHash())) && verifyBlock(block);
            }
            return verifyBlock(block);
        }

    }

    public static boolean inchain(String s, Block block, Block og){
        ArrayList<String> list = new ArrayList<>();
        if(traverseTree(block.getRoot(), s)){
            proofMembership(block.getRoot(), s, list);
            ArrayList<String> blockList = new ArrayList<>();

            while(!og.getHeader().getRootHash().equals(list.get(0))){
                blockList.add(og.getHeader().getRootHash());
                og = og.getPrevious();
            }

            Collections.reverse(blockList);
            Collections.reverse(list);
            list.addAll(blockList);
            
            //System.out.println(Arrays.toString(list.toArray()));

            return true;
        }
        else if(!block.getHeader().getPrevHash().equals("0")){
            return inchain(s, block.getPrevious(), og);
        }
        else {
            return false;
        }

    }
    public static boolean traverseTree(Node current, String s){
        if(current instanceof LeafNode){
            return ((LeafNode) current).getData().equals(s);
        }
        InnerNode n = (InnerNode) current;
        if(n.getLeft() != null && n.getPrefix().compareTo(s) >= 0){
            return traverseTree(n.getLeft(), s);
        }
        else if (n.getRight() != null) {
            return traverseTree(n.getRight(), s);
        }
        return false;
    }
    public static List<String> proofMembership(Node current, String s, List<String> list){
        list.add(current.getHash());

        if(current instanceof LeafNode){
            return list;
        }
        InnerNode n = (InnerNode) current;

        if(n.getLeft() != null && n.getPrefix().compareTo(s) >= 0){
            if(n.getRight() != null)
                list.add(n.getRight().getHash());
            return proofMembership(n.getLeft(), s, list);
        }
        else if (n.getRight() != null){
            if(n.getLeft() != null)
                list.add(n.getLeft().getHash());
            return proofMembership(n.getRight(), s, list);
        }

        return null;

    }
}
