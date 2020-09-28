public class Block {

    private Header header;
    private Block previous;
    private Node root;

    public Block(Header header, Block previous, Node root){

        this.header = header;
        this.previous = previous;
        this.root = root;

    }
    public Block getPrevious(){
        return previous;
    }
    public Header getHeader(){
        return header;
    }
    public Node getRoot(){
        return root;
    }
}
