import java.awt.Color;

public enum ColorSchemes{
    BLACK (new Color(0, 0, 0)),
    ORANGE (new Color(10, 10, 10), new Color(193, 59, 12), new Color(209, 90, 8), new Color(229, 132, 18), new Color(169, 42, 18), new Color(230, 230, 230), new Color(230, 230, 230)),
    RED (new Color(10, 5, 5), new Color(100, 0, 0), new Color(125, 30, 20), new Color(170, 60, 40), new Color(30, 30, 30), new Color(230, 230, 230), new Color(230, 230, 230)),
    INDIGO (new Color(10, 20, 25), new Color(15, 30, 50), new Color(25, 70, 80), new Color(20, 100, 100), new Color(20, 50, 80), new Color(230, 230, 230), new Color(230, 230, 230)),
    PURPLE (new Color(30, 15, 20), new Color(40, 30, 90), new Color(60, 45, 120), new Color(80, 60, 140), new Color(40, 10, 50), new Color(230, 230, 230), new Color(230, 230, 230)),
    GRAY (new Color(25, 20, 20), new Color(80, 80, 80), new Color(125, 120, 120), new Color(160, 150, 150), new Color(50, 50, 50), new Color(230, 230, 230), new Color(230, 230, 230)),
    BLUE (new Color(5, 10, 20), new Color(24, 49, 79), new Color(56, 78, 119), new Color(158, 214, 245), new Color(20, 30, 60), new Color(230, 230, 230), new Color(230, 230, 230)),
    GREEN (new Color(0, 5, 0), new Color(39, 59, 9), new Color(88, 100, 29), new Color(123, 144, 75), new Color(10, 56, 10), new Color(230, 230, 230), new Color(230, 230, 230)),
    //ORANGE (new Color(10, 10, 10), new Color(255, 80, 0), new Color(255, 117, 25), new Color(255, 148, 67), new Color(169, 42, 18), new Color(230, 230, 230), new Color(230, 230, 230)),
    YELLOW (new Color(10, 0, 0), new Color(178, 100, 0), new Color(225, 154, 0), new Color(255, 218, 71), new Color(35, 35, 35), new Color(230, 230, 230), new Color(230, 230, 230));

    final Color[] SPIKES;
    final Color BACKGROUND;
    final Color BORDER;
    final Color[] BACKGROUND_OBJECTS;
   
    private ColorSchemes(Color frontSpikes, Color middleSpikes, Color backSpikes, Color background, Color border, Color backgroundObject1, Color backgroundObject2) {
        SPIKES = new Color[]{frontSpikes, middleSpikes, backSpikes};
        BACKGROUND = background;
        BORDER = border;
        BACKGROUND_OBJECTS = new Color[]{backgroundObject1, backgroundObject2};
    }
    
    private ColorSchemes(Color background) {
        BACKGROUND = background;
        SPIKES = null;
        BORDER = null;
        BACKGROUND_OBJECTS = null;
    }
}
