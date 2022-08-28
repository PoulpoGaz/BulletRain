package fr.poulpogaz.jam.stage;

import fr.poulpogaz.jam.patterns.BulletPattern;
import fr.poulpogaz.jam.patterns.MovePattern;

public record EnemyAction(int duration, MovePattern movePattern, BulletPattern bulletPattern) {

}
