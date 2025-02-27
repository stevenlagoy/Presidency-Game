public class Main
{
    public static void main(String[] args){
        Engine.init();
        Engine.language = Engine.Language.EN;
        boolean active = true;
        
        // Initialize and run OpenGL window
        Engine.initOpenGL();
        Engine.runOpenGL();

        while (active) {
            try {
                active = Engine.tick();
                if (!active) break;
                Thread.sleep(Engine.tickSpeed);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }

        System.out.print("Main Done\n");
        Engine.done();
    }
}
