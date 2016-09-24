import java.awt.Graphics;

public class Spikes extends Obstacle{

    private Spike[] first = new Spike[2];
    private Spike[] last = new Spike[2];
    private int[] upTendancy = new int[2];
    private int upTendancyChange = 0, break1000M = 0;
    
    public Spikes(Game game, Spike initialTopSpike, Spike initialBottomSpike) {
        super(0, 0, 0, 0, game);
        first[0] = initialTopSpike;
        last[0] = initialTopSpike;
        first[1] = initialBottomSpike;
        last[1] = initialBottomSpike;
        initializeSpikes();
    }
    
    public Spike[] getFirst() {
        return first;
    }
    
    public Spike[] getLast() {
        return last;
    }
    
    public void initializeSpikes(){
        
        for (int x = 0; x < game.WINDOW_WIDTH / ((game.getGameMode().minSpikeWidth + game.getGameMode().maxSpikeWidth - 10 * (int) (last[0].getDepth() - 1)) / 2) + 10; x++) {
            for (int i = 0; i < first.length; i++) {
                double yChange = 0;
                if (last[i].getDepth() == 1) {
                    checkSpikeSeparation();
                    yChange = (Math.random() * game.getGameMode().maxYChange + game.getGameMode().minYChange) * ((int) (Math.random() * 10) >= this.upTendancy[i] ? 1 : -1) + 1;
                }
                double width = Math.random() * (game.getGameMode().maxSpikeWidth - 10 * (int) (last[i].getDepth() - 1)) + game.getGameMode().minSpikeWidth;
                double height = (Math.random() * (game.getGameMode().maxSpikeHeight - 5 * (int) (last[i].getDepth() - 1)) + game.getGameMode().minSpikeHeight) * ((int) (Math.random() * 10) == 0 ? 0 : 1);
                double spikePeakX = Math.random() * width;
                Spike newSpike = new Spike(last[i].getEndX(), last[i].getEndY(), width, height, game, last[i].getType(), yChange, spikePeakX, last[i].getDepth());
                last[i].setNext(newSpike);
                newSpike.setPrevious(last[i]);
                last[i] = newSpike;
            }
        }
    }
    public void checkSpikeSeparation() {
        if (this.last[1].getEndY() - this.last[0].getEndY() < game.getGameMode().minSpikeSeparation) {
            this.upTendancy[0] = 10;
            this.upTendancy[1] = 0;
        } else if (this.last[1].getEndY() - this.last[0].getEndY() > game.getGameMode().maxSpikeSeparation + 3000 * break1000M) {
            if (upTendancyChange > 0) {
                this.upTendancy[0] = (int) (Math.random() * 10);
                this.upTendancy[1] = 10;
            } else if (upTendancyChange < 0) {
                this.upTendancy[0] = 0;
                this.upTendancy[1] = (int) (Math.random() * 10);
            } else {
                this.upTendancy[0] = 0;
                this.upTendancy[1] = 10; 
            }
        } else {
            this.upTendancy[0] = game.getGameMode().topSpikesUpTendancy + (upTendancyChange > 0 ? upTendancyChange : 0) + 10 * break1000M;
            this.upTendancy[1] = game.getGameMode().bottomSpikesUpTendancy + (upTendancyChange >= 0 ? 0 : (10 - upTendancyChange)) - 20 * break1000M;
        }
        if (this.last[0].getEndY() < -300 - 300 * break1000M) {
            this.upTendancy[0] = 0;
        } else if (this.last[0].getEndY() > 625) {
            this.upTendancy[0] = 10;
        } else if (this.last[1].getEndY() > 975 + 300 * break1000M) {
            this.upTendancy[1] = 10;
        } else if (this.last[1].getEndY() < 50) {
            this.upTendancy[1] = 0;
        }
    }
    
    @Override
    public void update() {
        for (int i = 0; i < first.length; i++) {
            Spike spike = first[i];
            while (spike.getEndX() < 0) {
                double newWidth = Math.random() * (game.getGameMode().maxSpikeWidth - 10 * (spike.getDepth() - 1)) + game.getGameMode().minSpikeWidth;
                double newHeight = (Math.random() * game.getGameMode().maxSpikeHeight - 10 * (spike.getDepth() - 1) + game.getGameMode().minSpikeHeight) * ((int) (Math.random() * 10) == 0 ? 0 : 1);
                double spikePeakX = Math.random() * newWidth;
                if (spike.getDepth() > 1) {
                    spike.change(last[i].getEndX(), last[i].getEndY(), newWidth, newHeight, 0, spikePeakX);
                } else {
                    checkSpikeSeparation();
                    double yChange;
                    if (break1000M == 1 || this.last[1].getEndY() - this.last[0].getEndY() > 800) {
                        yChange = 10 * ((int) (Math.random() * 9 + 1) >= this.upTendancy[i] ? 1 : -1);
                    } else {
                        yChange = (Math.random() * game.getGameMode().maxYChange + game.getGameMode().minYChange) * ((int) (Math.random() * 9 + 1) >= this.upTendancy[i] ? 1 : -1);
                    }
                    spike.change(last[i].getEndX(), last[i].getEndY(), newWidth, newHeight, yChange, spikePeakX);
                }
                first[i] = spike.getNext();
                first[i].setPrevious(null);
                last[i].setNext(spike);
                spike.setPrevious(last[i]);
                spike.setNext(null);
                last[i] = spike;
                spike = first[i];
            }
            if (last[0].getDepth() == 1 && (int) (Math.random() * 100) > 97) {
                upTendancyChange = ((last[0].getEndY() + 100 > game.WINDOW_HEIGHT / 2) ? 1 : 0) * 10 - game.getGameMode().topSpikesUpTendancy;
            } else if ((last[0].getDepth() == 1 && upTendancyChange > 0 && game.WINDOW_HEIGHT / 2 - last[0].getEndY() > 500) || (last[0].getDepth() == 1 && upTendancyChange < 0 && last[1].getEndY() - game.WINDOW_HEIGHT / 2 > 500)) {
                upTendancyChange = 0;
            }
        }
        if ((int) (game.getDistance() % game.BREAK_DISTANCE) > game.BREAK_DISTANCE - 100 && break1000M == 0) {
            break1000M = 1;
        } else if ((int) ((game.getDistance() % game.BREAK_DISTANCE) / (game.BREAK_DISTANCE / 10)) == 0 && break1000M == 1) {
            break1000M = 0;
        }
        for (int i = 0; i < 2; i++) {
            Spike spike = first[i];
            while (spike != null) {
                spike.update();
                spike = spike.getNext();
            }
        }
    }
    
    @Override
    public void paint(Graphics g) {
        for (int i = 0; i < 2; i++) {
            Spike spike = first[i];
            while (spike != null) {
                spike.paint(g);
                spike = spike.getNext();
            }
        }
    }

    @Override
    public double getX() {
        return 0;
    }

    @Override
    public double getEndX() {
        return 0;
    }

    @Override
    public double getY() {
        return 0;
    }

    @Override
    public double getEndY() {
        return 0;
    }
}
