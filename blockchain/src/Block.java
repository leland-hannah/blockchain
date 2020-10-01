public class Block {

    private Header header;
    private Block previous;
    private Node root;
    private String fileName;

    public Block(Header header, Block previous, Node root, String fileName){

        this.header = header;
        this.previous = previous;
        this.root = root;
        this.fileName = fileName;

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
    public String getFileName(){
        return fileName;
    }
}
