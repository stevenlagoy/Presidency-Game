import java.util.Scanner;

public class Main{

    public static int getInput(String[] responses){
        /* getInput
         * takes responses, a list of acceptable strings
         * only returns when an acceptable input is given
         * returns an integer corresponding to the index of the accepted string in the responses list
        */

        Scanner in = new Scanner(System.in);
        String input = "";
        while(true){
            System.out.print("> ");
            input = in.nextLine();
            for(int i = 0; i < responses.length; i++){
                if(responses[i].equals(input)) return i;
            }
        }
    }
    public static int getInput(){
        /* getInput
         * asks for an input until one is given
         * returns when input is recieved
        */

        Scanner in = new Scanner(System.in);
        String input = "";
        while(true){
            System.out.print("> ");
            input = in.nextLine();
            if(input != null) return 1;
        }
    }

    public static void main(String[] args){

        System.out.println("Race for the Presidency\n[NEW / CONTINUE]");
        
        System.out.println("Press enter to start");
        getInput();
        System.out.println("Start game");
    }
}