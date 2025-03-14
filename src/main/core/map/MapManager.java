package main.core.map;
public class MapManager
{
    public static String[] mapModeNames = {"Default", "States", "Polling", "Population", "Conventions", "Districts", "Counties", "Travel"};
    /*
     * Default view - Switch between States, Districts, and Counties depending on zoom level.
     * States view - See states in different colors.
     * Districts view - See congressional districts in different colors.
     * Counties view - See counties and equivalents in different colors.
     * Polling view - Shade areas between Blue, White, and Red according to poll results.
     * Population view - Shade areas between Black and White depending on membership of selected bloc.
     * Conventions view - Shade states between Black and White depending on how soon their Convention is.
     * Travel view - Show roadways, airports, and other Routes between cities.
     */
    public static int mapMode; // Currently selected map mode / view. 0 is Default.
    public static double zoomLevel; // Number of horizontal pixels of the map currently on screen.
    public static double mapCameraX; // X-coordinate on the map which the camera is currently centered on. 0.0 is center of map.
    public static double mapCameraY; // Y-coordinate on the map which the camera is currently centered on. 0.0 is center of map.

    public static void init(){
        setMapMode(0);
        centerCamera();
        resetZoom();
    }
    public static void moveCamera(double x, double y){
        mapCameraX = x;
        mapCameraY = y;
    }
    public static void centerCamera(){
        mapCameraX = 0.0;
        mapCameraY = 0.0;
    }
    public static void resetZoom(){
        zoomLevel = 2560.0;
    }
    public static void setMapMode(int mode){
        mapMode = mode;
    }
}
