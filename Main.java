public class Main
{
    public static void main(String[] args){
        Engine.init();
        boolean active = true;

        while(active){
            try {
                active = Engine.tick();
                Thread.sleep(Engine.tickSpeed);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(0);
            }
        }
    }
}
