package battlecode.common;


/**
 * A RobotController allows contestants to make their robot sense and interact
 * with the game world. When a contestant's <code>RobotPlayer</code> is
 * constructed, it is passed an instance of <code>RobotController</code> that
 * controls the newly created robot.
 *
 * @author Teh Devs
 */
public interface RobotController {

    // *********************************
    // ****** QUERY METHODS ********
    // *********************************

    /**
     * Gets the robot's current action delay.
     * @return this robot's current action delay
     */
    public double getActionDelay();

    /**
     * Gets the robot's current health (hitpoints).
     * @return this robot's current health / hitpoints
     */
    public double getHealth();
    
    /**
     * Gets the robot's current location.
     * @return this robot's current location
     */
    public MapLocation getLocation();

    /**
     * @return the current map's width
     */
    public int getMapWidth();
   
    /**
     * @return the current map's height
     */
    public int getMapHeight();

    /**
     * Gets the Team of this robot. Equivalent to
     * <code>this.getRobot().getTeam()</code>.
     *
     * @return this robot's Team
     * @see battlecode.common.Team
     */
    public Team getTeam();

    /**
     * Use this method to access your robot.
     *
     * @return the Robot associated with this RobotController
     */
    public Robot getRobot();

    /**
     * Gets this robot's type (SOLDIER, HQ, etc.)
     *
     * @return this robot's type.
     */
    public RobotType getType();

	/**
     * Returns whether the robot is currently constructing a structure.
     * @return whether this robot is currently constructing a structure.
     */
    public boolean isConstructing();
	
	/**
     * Returns the RobotType of the structure the robot is constructing.
     * @return the RobotType of the structure this robot is constructing.
     */
    public RobotType getConstructingType();
	
	/**
     * Returns the number of turns left in the current construction.
     * @return the number of turns left in the current construction.
     */
    public int getConstructingRounds();

    // ***********************************
    // ****** SENSOR METHODS ********
    // ***********************************

    /**
     * Sense the milk level for a given team. If the team is not your team, then it gets rounded down to the nearest multiple of GameConstants.OPPONENT_MILK_SENSE_ACCURACY
     * @return the current milk total of the team (rounded if it's the opponent team)
     */
    public double senseTeamMilkQuantity(Team t);

	/**
     * Gets the team's current robot count (with weighting).
     * @return the total number of robots controlled (with weighting).
     */
    public int senseRobotCount();

    /**
     * Returns the object at the given location, or <code>null</code>
     * if there is no object there.
     *
     * @throws GameActionException if <code>loc</code> is not within sensor range (CANT_SENSE_THAT)
     */
    public GameObject senseObjectAtLocation(MapLocation loc) throws GameActionException;

    /** 
     * Returns all game objects of a given type nearby the robot
     * @see #senseNearbyGameObjects(Class, MapLocation, int, Team)
     */
    public <T extends GameObject> T[] senseNearbyGameObjects(Class<T> type);

    /**
     * Returns all game objects of a given type nearby the robot
     * @see #senseNearbyGameObjects(Class, MapLocation, int, Team)
     */
    public <T extends GameObject> T[] senseNearbyGameObjects(Class<T> type, int radiusSquared);
    
    /**
     * Returns all game objects of a given type nearby the robot of a given team
     * @see #senseNearbyGameObjects(Class, MapLocation, int, Team)
     */
    public <T extends GameObject> T[] senseNearbyGameObjects(Class<T> type, int radiusSquared, Team team);
    
    
    /**
     * Senses all game objects of a given type within a given search area specified by the parameters (constrainted by sensor range and distance)
     * @param type - type of game object to sense, eg: Robot.class
     * @param center - center of the given search radius
     * @param radiusSquared - return objects this distance away from the center
     * @param team - filter game objects by the given team. If null is passed, objects from all teams are returned
     * @return array of class type of game objects
     */
    public <T extends GameObject> T[] senseNearbyGameObjects(Class<T> type, MapLocation center, int radiusSquared, Team team);

	/**
     * Returns an array of all the robots that have broadcasted in the last round (unconstrained by sensor range or distance)
     * @return an array of all robots that have broadcasted in the last round.
     */
    public Robot[] senseBroadcastingRobots();
	
	/**
     * Returns an array of all the robots that have broadcasted in the last round, filtered by team (unconstrained by sensor range or distance)
	 * @param t - filter robots by the given team.
     * @return an array of all robots of the given team that have broadcasted in the last round.
     */
    public Robot[] senseBroadcastingRobots(Team t);
    
	/**
     * Returns an array of all the locations of the robots that have broadcasted in the last round (unconstrained by sensor range or distance)
     * @return an array of all the locations of the robots that have broadcasted in the last round.
     */
    public MapLocation[] senseBroadcastingRobotLocations();
	
	/**
     * Returns an array of all the locations of the robots that have broadcasted in the last round, filtered by team (unconstrained by sensor range or distance)
	 * @param t - filter robots by the given team.
     * @return an array of all the locations of the robots of the given team that have broadcasted in the last round.
     */
    public MapLocation[] senseBroadcastingRobotLocations(Team t);
    
    /**
     * Sense the location of the given object.
     *
     * @return the location of the given object.
     * @throws GameActionException if object is not within sensor range (CANT_SENSE_THAT)
     */
    public MapLocation senseLocationOf(GameObject o) throws GameActionException;

    /**
     * Sense the RobotInfo for the given robot.
     *
     * @return the RobotInfo for the given robot.
     * @throws GameActionException if robot is not within sensor range (CANT_SENSE_THAT)
     */
    public RobotInfo senseRobotInfo(Robot r) throws GameActionException;

    /**
	 * Returns true if the given object is within the robot's sensor range
     * @return whether the given object is within the robot's sensor range
     */
    public boolean canSenseObject(GameObject o);

    /**
     * Returns true if the given location is within the robot's sensor range
     * @return whether the given location is within the robot's sensor range
     */
    public boolean canSenseSquare(MapLocation loc);
    
    /**
     * Returns location of the allied team's HQ (unconstrained by sensor range or distance)
     * @return the team's HQ location
     */
    public MapLocation senseHQLocation();

    /**
     * Returns location of the enemy team's HQ (unconstrained by sensor range or distance)
     * @return the enemy team's HQ location
     */
    public MapLocation senseEnemyHQLocation();
    
	/**
	 * Returns the locations of PASTRS filtered by a team (unconstrained by sensor range or distance)
	 * @param t - filter PASTR locations by the given team
     * @return an array of all MapLocations of PASTR locations
     */
    public MapLocation[] sensePastrLocations(Team t);

	/**
	 * Gives a representation of the cow growths of each location of the map (unconstrained by sensor range or distance)
	 *
     * @return an array of arrays of doubles, where element [a][b] is the natural cow growth at MapLocation (a,b). Will be 0 on all VOID locations.
     */
    public double[][] senseCowGrowth();

	/**
     * Returns the number of cows currently at a given location. Returns 0 for locations off the map.
	 *
	 * @param loc - location to sense at (must be within sensor range)
	 * @return a double equal to how many cows are currently at the location
     */
    public double senseCowsAtLocation(MapLocation loc) throws GameActionException;

    // ***********************************
    // ****** MOVEMENT METHODS ********
    // ***********************************

    /**
     * Returns the number of rounds until the robot is active again
     * @return the number of rounds until this robot's action cooldown ends, or 0 if it is already active
     */
    public int roundsUntilActive();

    /**
     * Returns if the robot is active (able to move, construct, and attack)
     * @return true if this robot is active. If a robot is active, it can move, construct, and attack
     */
    public boolean isActive();

    /**
     * Move in the given direction if possible
	 * Creates a short-range noise at the destination location
     * @param dir
     * @throws GameActionException if the robot cannot move in this direction
     */
    public void move(Direction dir) throws GameActionException;

	/**
     * Sneak in the given direction if possible
     * @param dir
     * @throws GameActionException if the robot cannot move in this direction
     */
    public void sneak(Direction dir) throws GameActionException;

    /**
     * Tells whether this robot can move in the given direction. Takes into
     * account only the map terrain and positions of other robots. Does not take
     * into account this robot's type or whether this robot is currently active.
     * Returns false for the OMNI and NONE directions.
     *
     * @return true if there are no robots or walls preventing this robot from
     *         moving in the given direction; false otherwise
     */
    public boolean canMove(Direction dir);

    // ***********************************
    // ****** ATTACK METHODS *******
    // ***********************************

    /**
     * Returns whether the given location is within the robot's attack range. Does not take into account whether the robot is currently attacking.
     * @return true if the given location is within this robot's attack range.
     * Does not take into account whether the robot is currently attacking
     */
    public boolean canAttackSquare(MapLocation loc);

    /**   
     * Attacks the given location
	 * Also applies to NOISETOWER, but does not deal damage in that case
	 * Creates a long-range noise at the targeted location
     * @throws GameActionException if the robot cannot attack the given square
     */
    public void attackSquare(MapLocation loc) throws GameActionException;

	/**
     * NOISETOWER only
     * 'Attacks' the given location
     * Does not deal damage, but creates a short-range noise at the targeted location
     * @throws GameActionException if the robot cannot attack the given square
     */
    public void attackSquareLight(MapLocation loc) throws GameActionException;

    // ***********************************
    // ****** BROADCAST METHODS *******
    // ***********************************
    
    /**
     * Broadcasts a message to the global message board.
     * The data is not written until the end of the robot's turn.
     * @param channel - the channel to write to, from 0 to <code>BROADCAST_MAX_CHANNELS</code>
     * @param data - one int's worth of data to write
     * @throws GameActionException if the channel is invalid
     */
    public void broadcast(int channel, int data) throws GameActionException;

    /**
     * Retrieves the message stored at the given radio channel.
     * @param channel - radio channel to query, from 0 to <code>BROADCAST_MAX_CHANNELS</code>
     * @return data currently stored on the channel
     * @throws GameActionException  if the channel is invalid
     */
    public int readBroadcast(int channel) throws GameActionException;

    // ***********************************
    // ****** OTHER ACTION METHODS *******
    // ***********************************

    /**
     * HQ ONLY.
     * Queues a spawn action to be performed at the end of this robot's turn.
     * When the action is executed, a new robot will be created adjacent to the HQ
     * in the given direction.  The square must not already be occupied.
     * The new robot is created and starts executing bytecodes immediately
     *
     * @param dir the direction to spawn robot in
     * @throws GameActionException   if this robot is not the HQ (CANT_DO_THAT_BRO)
     * @throws GameActionException   if this robot is currently inactive (NOT_ACTIVE)
     * @throws GameActionException   if location is already occupied (CANT_MOVE_THERE)
     * @throws GameActionException   if the maximum robot count has already been reached (CANT_DO_THAT_BRO)
     */
    public void spawn(Direction dir) throws GameActionException;
   
    /**
     * After a delay, kills the soldier and spawns a robot of the given building type
     * @param type
     * @throws GameActionException if it's not a soldier constructing or if the soldier is not constructing a structure
     */
    public void construct(RobotType type) throws GameActionException;
    
    /**
     * Ends the current round.
     * Never fails.
     */
    public void yield();

    /**
     * Kills your robot and ends the current round. Never fails.
     */
    //public void suicide();

	/**
     * Kills your robot and deals area damage within distanceSquared 2 equal to <code>GameConstants.SELF_DESTRUCT_BASE_DAMAGE</code> plus the robot's current hp multiplied by <code>GameConstants.SELF_DESTRUCT_DAMAGE_FACTOR</code>.
	 * Destroys all cows in all effected squares and creates a long-range noise at the robot's former location
     * @throws GameActionException if it's not a soldier
     */
    public void selfDestruct() throws GameActionException;

    /**
     * Causes your team to lose the game. It's like typing "gg."
     */
    public void resign();

    public void win();

    // ***********************************
    // ******** MISC. METHODS *********
    // ***********************************
    
    /**
     * Puts a hat on the robot. You require the BATTLECODE-HATS DLC. You also cannot be moving while putting on your hat. This costs milk (GameConstants.HAT_MILK_COST). The HQ's first hat is free.
     * @throws GameActionException if you have action delay or if you do not have enough milk
     */
    public void wearHat() throws GameActionException;

    /**
     * Sets one of this robot's 'indicator strings' for debugging purposes.
     * These strings are displayed in the client. This method has no effect on
     * gameplay (aside from the number of bytecodes executed to call this
     * method).
     *
     * @param stringIndex the index of the indicator string to set. Must satisfy
     *                    <code>stringIndex >= 0 && stringIndex < GameConstants.NUMBER_OF_INDICATOR_STRINGS</code>
     * @param newString   the value to which the indicator string should be set
     */
    public void setIndicatorString(int stringIndex, String newString);

    /**
     * Senses the terrain at the given location.
     */
    public TerrainTile senseTerrainTile(MapLocation loc);

    /**
     * Gets this robot's 'control bits' for debugging purposes. These bits can
     * be set manually by the user, so a robot can respond to them.
     *
     * @return this robot's control bits
     */
    public long getControlBits();

    /**
     * Adds a custom observation to the match file, such that when it is analyzed, this observation will appear.
     *
     * @param observation the observation you want to inject into the match file
     */
    public void addMatchObservation(String observation);

    /**
     * Sets the team's "memory", which is saved for the next game in the
     * match. The memory is an array of {@link GameConstants#TEAM_MEMORY_LENGTH}
     * longs.  If this method is called more than once with the same index
     * in the same game, the last call is what is saved for the
     * next game.
     *
     * @param index the index of the array to set
     * @param value the data that the team should remember for the next game
     * @throws java.lang.ArrayIndexOutOfBoundsException
     *          if {@code index} is less
     *          than zero or greater than or equal to {@link GameConstants#TEAM_MEMORY_LENGTH}
     * @see #getTeamMemory
     * @see #setTeamMemory(int, long, long)
     */
    public void setTeamMemory(int index, long value);

    /**
     * Sets this team's "memory". This function allows for finer control
     * than {@link #setTeamMemory(int, long)} provides.  For example,
     * if {@code mask == 0xFF} then only the eight least significant bits of
     * the memory will be set.
     *
     * @param index the index of the array to set
     * @param value the data that the team should remember for the next game
     * @param mask  indicates which bits should be set
     * @throws java.lang.ArrayIndexOutOfBoundsException
     *          if {@code index} is less
     *          than zero or greater than or equal to {@link GameConstants#TEAM_MEMORY_LENGTH}
     * @see #getTeamMemory
     * @see #setTeamMemory(int, long)
     */
    public void setTeamMemory(int index, long value, long mask);

    /**
     * Returns the team memory from the  last game of the match.
     * The return value is an array of length {@link GameConstants#TEAM_MEMORY_LENGTH}.
     * If setTeamMemory was not called in the last game, or there was no last game, the
     * corresponding long defaults to 0.
     *
     * @return the team memory from the the last game of the match
     * @see #setTeamMemory(int, long)
     * @see #setTeamMemory(int, long, long)
     */
    public long[] getTeamMemory();

    /**
     * If breakpoints are enabled, calling this method causes the game engine to
     * pause execution at the end of this round, until the user decides to
     * resume execution.
     */
    public void breakpoint();

    /**
     * Draws a dot on the game map, for debugging purposes.
     * Press V in the client to toggle which team's indicator dots are displayed.
     */
    public void setIndicatorDot(MapLocation loc, int red, int green, int blue);

    /**
     * Draws a line on the game map, for debugging purposes.
     * Press V in the client to toggle which team's indicator lines are displayed.
     */
    public void setIndicatorLine(MapLocation from, MapLocation to, int red, int green, int blue);
   
}
