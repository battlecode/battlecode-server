package battlecode.common;

import static battlecode.common.RobotLevel.IN_AIR;
import static battlecode.common.RobotLevel.ON_GROUND;

public enum RobotType {
    //          LEVEL,    MXE MXF  COS MD MVC SR  SA AR AR  AA AD  AP,AIR  GROUND
    ARCHON(ON_GROUND, 150, 300, 0, 6, 0, 36, 360, 0, 0, 0, 5, 0, false, false),
    SOLDIER(ON_GROUND, 40, 100, 120, 6, .4, 10, 180, 0, 5, 90, 5, 6, true, true),
    SCOUT(IN_AIR, 20, 50, 80, 4, .1, 25, 360, 0, 5, 360, 5, 1.5, true, true),
    DISRUPTER(ON_GROUND, 70, 100, 180, 9, 1.2, 16, 180, 0, 10, 90, 5, 1.7, true, true),
    SCORCHER(ON_GROUND, 70, 100, 270, 8, 1.8, 10, 135, 1, 10, 180, 5, 9, false, true),
    TOWER(ON_GROUND, 250, 0, 200, 0, 0, -1, 0, 0, 0, 0, 0, 0, false, false);

    /**
     * The robot's level.
     */
    public final RobotLevel level;

    /**
     * The maximum amount of energon the robot can have.
     */
    public final double maxEnergon;

    /**
     * The maximum amount of flux the robot can have.
     */
    public final double maxFlux;

    /**
     * The amount of flux needed to spawn the robot.
     */
    public final double spawnCost;

    /**
     * The number of turns it takes the robot to move orthogonally.
     */
    public final int moveDelayOrthogonal;

    /**
     * The number of turns it takes the robot to move diagonally.
     * Equal to <code>(int)Math.round(moveDelayOrthogonal*Math.sqrt(2.))</code>.
     */
    public final int moveDelayDiagonal;

    /**
     * The cost in flux to move the robot one square.
     */
    public final double moveCost;

    /**
     * The square of the maximum distance that the robot can sense.
     */
    public final int sensorRadiusSquared;

    /**
     * The range of angles that the robot can sense.
     */
    public final double sensorAngle;

    /**
     * The square of the maximum distance that the robot can attack.
     */
    public final int attackRadiusMaxSquared;

    /**
     * The square of the minimum distance that the robot can attack.
     */
    public final int attackRadiusMinSquared;

    /**
     * The range of angles that this robot can attack.
     */
    public final double attackAngle;

    /**
     * The number of turns that it takes this robot to attack.
     */
    public final int attackDelay;

    /**
     * The amount of damage that this robot does when it attacks.
     */
    public final double attackPower;

    /**
     * Whether or not the robot can attack air units.
     */
    public final boolean canAttackAir;

    /**
     * Whether or not the robot can attack ground units.
     */
    public final boolean canAttackGround;

    public final double sensorCosHalfTheta;
    public final double attackCosHalfTheta;

    /**
     * Returns true if the robot can attack robots at the given level.
     */
    public boolean canAttack(RobotLevel level) {
        switch (level) {
            case ON_GROUND:
                return canAttackGround;
            case IN_AIR:
                return canAttackAir;
            default:
                return false;
        }
    }

    /**
     * Returns <code>true</code> if this robot's level is
     * <code>RobotLevel.IN_AIR</code>.
     */
    public boolean isAirborne() {
        return level == RobotLevel.IN_AIR;
    }

    RobotType(RobotLevel level,
              double maxEnergon,
              double maxFlux,
              double spawnCost,
              int moveDelayOrthogonal,
              double moveCost,
              int sensorRadiusSquared,
              double sensorAngle,
              int attackRadiusMinSquared,
              int attackRadiusMaxSquared,
              double attackAngle,
              int attackDelay,
              double attackPower,
              boolean canAttackAir,
              boolean canAttackGround) {
        this.level = level;
        this.maxEnergon = maxEnergon;
        this.maxFlux = maxFlux;
        this.spawnCost = spawnCost;
        this.moveDelayOrthogonal = moveDelayOrthogonal;
        this.moveDelayDiagonal = (int) Math.round(moveDelayOrthogonal * Math.sqrt(2.));
        this.moveCost = moveCost;
        this.sensorRadiusSquared = sensorRadiusSquared;
        this.sensorAngle = sensorAngle;
        this.attackRadiusMaxSquared = attackRadiusMaxSquared;
        this.attackRadiusMinSquared = attackRadiusMinSquared;
        this.attackAngle = attackAngle;
        this.attackDelay = attackDelay;
        this.attackPower = attackPower;
        this.canAttackAir = canAttackAir;
        this.canAttackGround = canAttackGround;
        this.sensorCosHalfTheta = Math.cos(sensorAngle * Math.PI / 360.);
        this.attackCosHalfTheta = Math.cos(attackAngle * Math.PI / 360.);
    }

}

