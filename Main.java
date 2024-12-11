public class Main
{
    public static void main(String[] args){
        Engine.init();
        boolean active = true;

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
