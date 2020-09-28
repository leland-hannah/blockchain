import java.util.*;
import java.security.MessageDigest;
import java.math.BigInteger;
import java.nio.charset.*;
import java.security.NoSuchAlgorithmException;

public class MerkleTree {

    private Node root;

    public MerkleTree(Node root){
        this.root = root;
    }
    public static Node buildTree(ArrayList<String> strings){

        ArrayList<Node> nodes = new ArrayList<>();        
        for(int i = 0; i < strings.size(); i++){

            String hash;
            try {
                hash = toHexString(getSHA(strings.get(i)));
                nodes.add(new LeafNode(hash, strings.get(i)));
            } catch (NoSuchAlgorithmException e) {
                System.out.println("No such algorithm");
            }

        }

        ArrayList<Node> leafNodes = new ArrayList<>(nodes);

        int x = 0;
        while(!isPower2(nodes.size())){

            try{
                //creates parent hash 
                String newHash = toHexString(getSHA(nodes.get(x).getHash() + nodes.get(x + 1).getHash()));

                String prefix = getMaxLeft(nodes.get(x));
                
                Node node = new InnerNode(newHash, nodes.get(x), nodes.get(x + 1), prefix);
                nodes.set(x, node);
                nodes.remove(x + 1);
            }
            catch (NoSuchAlgorithmException e){
                System.out.println("No such algorithm");
            }

            x++;

        }

        while(nodes.size() > 1){

            ArrayList<Node> newNodes = new ArrayList<>();

            for(int i = 0; i < nodes.size() - 1; i+=2){

                String newHash;
                try{
                    newHash = toHexString(getSHA(nodes.get(i).getHash() + nodes.get(i + 1).getHash()));

                    String prefix = getMaxLeft(nodes.get(i));

                    Node node = new InnerNode(newHash, nodes.get(i), nodes.get(i + 1), prefix);
                    newNodes.add(node);
                }
                catch (NoSuchAlgorithmException e){
                    System.out.println("No such algorithm");
                }

            }
            if(nodes.size() % 2 == 1){
                newNodes.add(nodes.get(nodes.size() - 1));
            }

            nodes = newNodes;

        }
        

        
        return nodes.get(0);

    }
    
    public static String getMaxLeft(Node n) {
        if(n instanceof LeafNode) {
            return ((LeafNode) n).getData();
        }
        while(!(((InnerNode) n).getRight() instanceof LeafNode)) {
            n = ((InnerNode)n).getRight();
        }
        return ((LeafNode)((InnerNode)n).getRight()).getData();
    }
    
    public static byte[] getSHA(String input) throws NoSuchAlgorithmException 
    {  
        // Static getInstance method is called with hashing SHA  
        MessageDigest md = MessageDigest.getInstance("SHA-256");  
  
        // digest() method called  
        // to calculate message digest of an input  
        // and return array of byte 
        return md.digest(input.getBytes(StandardCharsets.UTF_8));  
    } 
    
    public static String toHexString(byte[] hash) 
    { 
        // Convert byte array into signum representation  
        BigInteger number = new BigInteger(1, hash);  
  
        // Convert message digest into hex value  
        StringBuilder hexString = new StringBuilder(number.toString(16));  
  
        // Pad with leading zeros 
        while (hexString.length() < 32)  
        {  
            hexString.insert(0, '0');  
        }  
  
        return hexString.toString();  
    } 

    public static boolean isPower2(int num){

        return (int)(Math.ceil((Math.log(num) / Math.log(2)))) == (int)(Math.floor((Math.log(num) / Math.log(2))));

    }
    
}
