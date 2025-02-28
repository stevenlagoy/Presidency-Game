public interface ILogic {
    
    void init() throws Exception;

    void input();

    void update(float interval, MouseInput mouse);

    void render();
    
    void cleanup();

}
