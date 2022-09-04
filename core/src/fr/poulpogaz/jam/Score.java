package fr.poulpogaz.jam;

import fr.poulpogaz.jam.entities.AbstractEnemy;
import fr.poulpogaz.jam.entities.Boss;

public class Score {

    private int nKill;
    private int nBossKill;
    private int nItem;
    private int timeSurvived;
    private boolean perfect;
    private int nContinue;

    public void clear() {
        nKill = 0;
        nBossKill = 0;
        nItem = 0;
        timeSurvived = 0;
        perfect = true;
        nContinue = 0;
    }

    public int getScore() {
        return getScore(false);
    }

    public int getScore(boolean countPerfect) {
        int score = (nKill * Constants.PLAYER_KILL +
                nBossKill * Constants.PLAYER_KILL_BOSS +
                nItem * Constants.PLAYER_PICK_SCORE_BLOCK +
                timeSurvived * Constants.PLAYER_SURVIVE) / (nContinue + 1);

        if (countPerfect) {
            score += Constants.PLAYER_PERFECT_GAME;
        }

        return score;
    }

    public void copy(Score score) {
        nKill = score.nKill;
        nBossKill = score.nBossKill;
        nItem = score.nItem;
        timeSurvived = score.timeSurvived;
        perfect = score.perfect;
        nContinue = score.nContinue;
    }

    public void addKill(AbstractEnemy e) {
        if (e instanceof Boss) {
            nBossKill++;
        } else {
            nKill++;
        }
    }

    public void addItem() {
        nItem++;
    }

    public void survive() {
        timeSurvived++;
    }

    public void setPerfect(boolean perfect) {
        this.perfect = perfect;
    }

    public void useContinue() {
        nContinue++;
    }

    public int getnKill() {
        return nKill;
    }

    public int getnBossKill() {
        return nBossKill;
    }

    public int getnItem() {
        return nItem;
    }

    public int getTimeSurvived() {
        return timeSurvived;
    }

    public boolean isPerfect() {
        return perfect;
    }

    public int getnContinue() {
        return nContinue;
    }

    @Override
    public String toString() {
        return "Score{" +
                "nKill=" + nKill +
                ", nBossKill=" + nBossKill +
                ", nItem=" + nItem +
                ", timeSurvived=" + timeSurvived +
                ", perfect=" + perfect +
                ", nContinue=" + nContinue +
                '}';
    }
}
