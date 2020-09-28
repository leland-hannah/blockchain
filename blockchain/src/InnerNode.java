public class InnerNode extends Node{

    private Node leftChild;
    private Node rightChild;
    private String prefix;
    public InnerNode(String hash, Node leftChild, Node rightChild, String prefix){

        super(hash);
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        this.prefix = prefix;

    }
    public String getPrefix(){
        return prefix;
    }
    
    public Node getLeft() {
        return leftChild;
    }
    public Node getRight() {
        return rightChild;
    }
    
}
