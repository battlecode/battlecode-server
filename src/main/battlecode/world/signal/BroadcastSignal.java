package battlecode.world.signal;

import java.util.HashMap;

import battlecode.common.Team;
import battlecode.engine.signal.Signal;
import battlecode.world.InternalRobot;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Signifies that a robot has broadcast a message.
 *
 * @author Matt
 */
public class BroadcastSignal extends Signal {

    @JsonProperty("type")
    private String getTypeForJson() { return "Broadcast"; }

    private static final long serialVersionUID = 8603786984259160822L;

    /**
     * The ID of the robot that broadcasted the message.
     */
    public final int robotID;
    /**
     * The team of the robot that broadcasted the message.
     */
    public final Team robotTeam;
    public transient HashMap<Integer, Integer> broadcastMap;

    /**
     * Creates a signal for a robot broadcast.
     *
     * @param robot the robot that broadcast the message
     */
    public BroadcastSignal(InternalRobot robot, HashMap<Integer, Integer> broadcastMap) {
        this.robotID = robot.getID();
        this.robotTeam = robot.getTeam();
        this.broadcastMap = broadcastMap;
    }

    /**
     * Returns the ID of the robot that just broadcasted.
     *
     * @return the messaging robot's ID
     */
    public int getRobotID() {
        return robotID;
    }

    /**
     * Returns the team of the robot that just broadcasted.
     *
     * @return the messaging robot's Team
     */
    public Team getRobotTeam() {
        return robotTeam;
    }
}
