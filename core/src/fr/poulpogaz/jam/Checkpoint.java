package fr.poulpogaz.jam;

public class Checkpoint {

    private int currentStage;
    private int seqIndex;
    private int bossPhase;
    private float mapScroll;
    private int tick;
    private Score score;
    private double playerPower;

    public Checkpoint() {
        this.score = new Score();
    }

    public void clear() {
        seqIndex = 0;
        mapScroll = 0;
        tick = 0;
        bossPhase = -1;
        playerPower = Constants.PLAYER_MIN_POWER;
        score.clear();
    }

    public int getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(int currentStage) {
        this.currentStage = currentStage;
    }

    public int getSeqIndex() {
        return seqIndex;
    }

    public void setSeqIndex(int seqIndex) {
        this.seqIndex = seqIndex;
    }

    public int getBossPhase() {
        return bossPhase;
    }

    public void setBossPhase(int bossPhase) {
        this.bossPhase = bossPhase;
    }

    public float getMapScroll() {
        return mapScroll;
    }

    public void setMapScroll(float mapScroll) {
        this.mapScroll = mapScroll;
    }

    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    public Score getScore() {
        return score;
    }

    public void setScore(Score score) {
        this.score = score;
    }

    public double getPlayerPower() {
        return playerPower;
    }

    public void setPlayerPower(double playerPower) {
        this.playerPower = playerPower;
    }
}
