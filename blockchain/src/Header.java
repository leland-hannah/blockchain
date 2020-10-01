import java.util.Date;
import java.time.Instant;
import java.util.Calendar;
import java.math.BigInteger;

public class Header {

    private String previousHash;
    private String rootHash;
    private int timeStamp;
    private BigInteger target;
    private String nonce;

    public Header(String previousHash, String rootHash){

        this.previousHash = previousHash;
        this.rootHash = rootHash;
        this.timeStamp = (int)Instant.now().getEpochSecond();
        //this.target = 0;
        //this.nonce = null;
        this.target = new BigInteger("01111111111111111111111111111" + 
        "111111111111111111111111111111111111111111111111111111111111111111111111" + 
        "1111111111111111111111111111111111111111111111111111111111111111111111111" + 
        "11111111111111111111111111111111111111111111111111111111111111111111111111" +
        "1111111");
        this.nonce = computeNonce(rootHash, target);

    }
    public String getPrevHash(){
        return previousHash;
    }
    public String getRootHash(){
        return rootHash;
    }
    public int getTimeStamp(){
        return timeStamp;
    }
    public BigInteger getTarget(){
        return target;
    }
    public String getNonce(){
        return nonce;
    }
    //from here down is new 
    public String computeNonce(String rootHash, BigInteger target){

        BigInteger binary = null;
        String non;
        do{

            non = Integer.toString((int)(Math.random()*Integer.MAX_VALUE));
            String concat = rootHash + non;

            try{
                String hash = MerkleTree.toHexString(MerkleTree.getSHA(concat));
                String bin = hexToBinary(hash);
                binary = new BigInteger(bin);
            }
            catch(Exception e){
                System.out.println("Something went wrong.");
            }

        }while(binary.compareTo(target) > 0);

        return non;

    }
    public static String hexToBinary(String hex) {
        int len = hex.length() * 4;
        String bin = new BigInteger(hex, 16).toString(2);
    
        //left pad the string result with 0s if converting to BigInteger removes them.
        if(bin.length() < len){
            int diff = len - bin.length();
            String pad = "";
            for(int i = 0; i < diff; ++i){
                pad = pad.concat("0");
            }
            bin = pad.concat(bin);
        }
        return bin;
    }
}
