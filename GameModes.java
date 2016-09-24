public enum GameModes{
    EASY (15, 3, 400, 800, 25, 60, 10, 40, 0, 30, 6, 4, "Noobs only please."),
    NORMAL (20, 5, 400, 700, 20, 60, 15, 45, 0, 40, 5, 5, "Nothing special here, it's pretty regular."),
    ROCKY (20, 3, 550, 800, 30, 60, 30, 80, 10, 90, 6, 4, "Lots of spikey, dangerously deadly spikes."),
    NARROW (15, 0, 200, 350, 30, 60, 10, 20, 0, 25, 3, 7, "It's too tight, lols."),
    DARK (20, 3, 450, 800, 10, 50, 15, 40, 0, 45, 5, 5, "It's so dark, we even gave your spaceship a flashlight."),
    FAST (40, 0, 450, 700, 20, 50, 20, 40, 10, 30, 5, 5, "Fast. Really, really, really fast."),
    IMPOSSIBLE (25, 5, 375, 600, 30, 50, 25, 45, 20, 45, 5, 5, "It's not impossible, you just suck.");
    
    final int speed;
    final int NUM_OF_ROCKS;
    final int minSpikeSeparation;
    final int maxSpikeSeparation;
    final int minSpikeWidth;
    final int maxSpikeWidth;
    final int minSpikeHeight;
    final int maxSpikeHeight;
    final int minYChange;
    final int maxYChange;
    final int topSpikesUpTendancy;
    final int bottomSpikesUpTendancy;
    final String description;
   
    
    private GameModes(int speed, int numOfRocks, int minSpikeSeparation, int maxSpikeSeparation, int minSpikeWidth, int maxSpikeWidth, int minSpikeHeight, int maxSpikeHeight, int minYChange, int maxYChange, int topSpikesUpTendancy, int bottomSpikesUpTendancy, String description) {
        this.speed = speed;
        this.NUM_OF_ROCKS = numOfRocks;
        this.minSpikeSeparation = minSpikeSeparation;
        this.maxSpikeSeparation = maxSpikeSeparation - minSpikeSeparation;
        this.minSpikeWidth = minSpikeWidth;
        this.maxSpikeWidth = maxSpikeWidth - minSpikeWidth;
        this.minSpikeHeight = minSpikeHeight;
        this.maxSpikeHeight = maxSpikeHeight - minSpikeHeight;
        this.minYChange = minYChange;
        this.maxYChange = maxYChange - minYChange;
        this.topSpikesUpTendancy = topSpikesUpTendancy;
        this.bottomSpikesUpTendancy = bottomSpikesUpTendancy;
        this.description = description;
    }
}