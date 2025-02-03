import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

public class Main
{
    public static void errorFunc(){
        errorFunc2();
    }
    public static void errorFunc2(){
        throw new UnsupportedOperationException("Test");
    }
    public static void main(String[] args){
        Engine.init();
        boolean active = true;

        //HashMap<Object, Object> json = Engine.readJSONFile("blocs.json");
        //System.out.println(json.toString());

        Engine.log("TEST LOG", "This is a test of the log system.", new Exception());

        System.out.println(Bloc.getInstances().toString());

        for(int i = 0; i < 10000; i++){
            Character character = new Character();
        }

        /*
        while(active){
            try {
                active = Engine.tick();
                if(!active) break;
                Thread.sleep(Engine.tickSpeed);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
        */

        System.out.print("Main Done\n");
        Engine.done();
    }
}
