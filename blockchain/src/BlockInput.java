import java.util.*;

public class BlockInput {

    public static void main(String [] args){

        Scanner s = new Scanner(System.in);

        String fileName = "";
        while(!fileName.equals("done")){

            System.out.println("Enter the name of a file to add to the block. If you have no more files to add, type \"done\", all lowercase");
            fileName = s.next();

        }

    }
    
}
