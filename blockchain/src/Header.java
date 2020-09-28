import java.util.Date;
import java.time.Instant;
import java.util.Calendar;

public class Header {

    private String previousHash;
    private String rootHash;
    private int timeStamp;
    private int target;
    private String nonce;

    public Header(String previousHash, String rootHash){

        this.previousHash = previousHash;
        this.rootHash = rootHash;
        this.timeStamp = (int)Instant.now().getEpochSecond();
        this.target = 0;
        this.nonce = null;

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
    public int getTarget(){
        return target;
    }
    public String getNonce(){
        return nonce;
    }
    
}
