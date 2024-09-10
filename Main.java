import java.util.Scanner;

public class Main{

    public static void main(String[] args){

        System.out.println("Race for the Presidency\n[NEW / CONTINUE]");
        
        while(!goodIn){
            System.out.print("> ");
            input = in.nextLine();
            switch(input){
                case "NEW":
                case "N":
                    goodIn = true;
                    break;
                case "CONTINUE":
                case "CONT":
                case "C":
                    goodIn = false;
                    break;
            }
        }
        System.out.println("Start game");
    }

    public int getInput(String[] responses){
        /* getInput
         * takes responses, a list of acceptable strings
         * only returns when an acceptable input is given
         * returns an integer corresponding to the index of the accepted string in the responses list
        */

        Scanner in = new Scanner(System.in);
        String input = "";
        while(true){
            input = in.nextLine();
            responses.;
        }
    }
}