package battlecode.world.signal;

import battlecode.engine.signal.Signal;
import battlecode.common.RobotInfo;
import battlecode.world.InternalRobot;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Signifies the RobotInfo associated with each robot.
 *
 * @author axc
 */
public class RobotInfoSignal extends Signal {

    @JsonProperty("type")
    private String getTypeForJson() { return "RobotInfo"; }

    private static final long serialVersionUID = 6617731214077155785L;

    private final int[] robotIDs;

    private final double[] coreDelays;
    private final double[] weaponDelays;
    private final double[] supplyLevels;
    //public final double health;
    //public final int xp;
    //public final int missileCount;

    public RobotInfoSignal(InternalRobot[] robots) {
        robotIDs = new int[robots.length];
        weaponDelays = new double[robots.length];
        coreDelays = new double[robots.length];
        supplyLevels = new double[robots.length];
        for (int i = 0; i < robots.length; i++) {
            robotIDs[i] = robots[i].getID();
            weaponDelays[i] = robots[i].getWeaponDelay();
            coreDelays[i] = robots[i].getCoreDelay();
            supplyLevels[i] = robots[i].getSupplyLevel();
        }
    }

    public int[] getRobotIDs() {
        return robotIDs;
    }

    public double[] getSupplyLevels() {
        return supplyLevels;
    }

    public double[] getCoreDelays() {
        return coreDelays;
    }

    public double[] getWeaponDelays() {
        return weaponDelays;
    }
}
