public class LeafNode extends Node {
    
    private String data;
    public LeafNode(String hash, String data){
        super(hash);
        this.data = data;
    }
    public String getData(){
        return data;
    }

}
