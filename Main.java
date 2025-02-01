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

        Engine.log("TEST LOG", "This is a test of the log system.", new Exception());

        Bloc bloc = new Bloc("Millenial", "Generation");
        bloc = new Bloc("Evangelical", "Religion");
        bloc = new Bloc("European (White)", "Race & Ethnicity");
        bloc = new Bloc("Woman", "Presentation");

        Character character = new Character();
        System.out.println(character.getBirthday().toString());

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
        System.out.print("Main Done\n");
    }
}
