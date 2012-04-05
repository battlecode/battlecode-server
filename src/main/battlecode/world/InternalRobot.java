package battlecode.world;

import battlecode.common.*;
import battlecode.engine.GenericRobot;
import battlecode.engine.signal.Signal;
import battlecode.server.Config;
import battlecode.world.signal.DeathSignal;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class InternalRobot extends InternalObject implements Robot, GenericRobot {

    protected volatile double myEnergonLevel;
    private volatile double flux;
    protected volatile Direction myDirection;
    protected volatile boolean energonChanged = true;
    private volatile boolean fluxChanged = true;
    protected volatile long controlBits;
    // is this used ever?
    protected volatile boolean hasBeenAttacked = false;
    private static boolean upkeepEnabled = Config.getGlobalConfig().getBoolean("bc.engine.upkeep");
    /**
     * first index is robot type, second is direction, third is x or y
     */
    private static final Map<RobotType, int[][][]> offsets = GameMap.computeVisibleOffsets();
    /**
     * number of bytecodes used in the most recent round
     */
    private volatile int bytecodesUsed = 0;
    private List<Message> incomingMessageQueue;
    protected GameMap.MapMemory mapMemory;
    public final RobotType type;

    private volatile int turnsUntilMovementIdle;
    private volatile int turnsUntilAttackIdle;
    protected volatile boolean regen;
    private boolean broadcasted;
    private boolean upkeepPaid;

    private Signal movementSignal;

    private InternalRobotBuffs buffs;

    public InternalRobotBuffs getBuffs() {
        return buffs;
    }

    @SuppressWarnings("unchecked")
    public InternalRobot(GameWorld gw, RobotType type, MapLocation loc, Team t,
                         boolean spawnedRobot) {
        super(gw, loc, type.level, t);
        myDirection = Direction.values()[gw.getRandGen().nextInt(8)];
        this.type = type;

        myEnergonLevel = getMaxEnergon();

        incomingMessageQueue = new LinkedList<Message>();

        mapMemory = new GameMap.MapMemory(gw.getGameMap());
        saveMapMemory(null, loc, false);
        controlBits = 0;

        buffs = new InternalRobotBuffs(this);

        if (spawnedRobot) {
            turnsUntilMovementIdle = GameConstants.WAKE_DELAY;
            turnsUntilAttackIdle = GameConstants.WAKE_DELAY;
        }

        if (type == RobotType.ARCHON)
            gw.addArchon(this);

    }

    public void addAction(Signal s) {
        myGameWorld.visitSignal(s);
    }

    @Override
    public void processBeginningOfRound() {
        super.processBeginningOfRound();
        buffs.processBeginningOfRound();
        // towers don't get a turn, so regenerate them here
        if (type == RobotType.TOWER) {
            if (regen) {
                changeEnergonLevel(GameConstants.REGEN_AMOUNT);
            }
            regen = false;
        }
    }

    public void processBeginningOfTurn() {
        if (type == RobotType.ARCHON)
            archonProduction();
        if (regen) {
            changeEnergonLevel(GameConstants.REGEN_AMOUNT);
            regen = false;
        }
        if (upkeepEnabled && type != RobotType.ARCHON) {
            upkeepPaid = flux >= GameConstants.UNIT_UPKEEP;
            if (upkeepPaid)
                adjustFlux(-GameConstants.UNIT_UPKEEP);
        } else
            upkeepPaid = true;
    }

    public void archonProduction() {
        int d, dmin = GameConstants.PRODUCTION_PENALTY_R2;
        for (MapLocation l : myGameWorld.getArchons(getTeam())) {
            d = getLocation().distanceSquaredTo(l);
            if (d > 0 && d <= dmin)
                dmin = d;
        }
        double prod = GameConstants.MIN_PRODUCTION + (GameConstants.MAX_PRODUCTION - GameConstants.MIN_PRODUCTION) * Math.sqrt(((double) dmin) / GameConstants.PRODUCTION_PENALTY_R2);
        adjustFlux(prod);
    }

    @Override
    public void processEndOfTurn() {
        super.processEndOfTurn();
        if (movementSignal != null) {
            myGameWorld.visitSignal(movementSignal);
            movementSignal = null;
        }
        if (turnsUntilAttackIdle > 0)
            turnsUntilAttackIdle--;
        if (turnsUntilMovementIdle > 0)
            turnsUntilMovementIdle--;
        broadcasted = false;
    }

    @Override
    public void processEndOfRound() {
        super.processEndOfRound();
        buffs.processEndOfRound();
        if (type == RobotType.TOWER && !myGameWorld.towerToNode(this).connected(getTeam()))
            takeDamage(GameConstants.DISCONNECTED_NODE_DAMAGE);
    }

    public double getEnergonLevel() {
        return myEnergonLevel;
    }

    public double getFlux() {
        return flux;
    }

    public boolean payFlux(double amt) {
        if (amt < flux)
            return false;
        else {
            flux -= amt;
            return true;
        }
    }

    public void adjustFlux(double amt) {
        flux += amt;
        if (flux >= type.maxFlux)
            flux = type.maxFlux;
        fluxChanged = true;
    }

    public Direction getDirection() {
        return myDirection;
    }

    public void setRegen() {
        if (type != RobotType.TOWER || !myGameWorld.timeLimitReached())
            regen = true;
    }

    public boolean getRegen() {
        return regen;
    }

    public void takeDamage(double baseAmount) {
        if (baseAmount < 0) {
            changeEnergonLevel(-baseAmount);
        } else {
            changeEnergonLevelFromAttack(-baseAmount);
        }
    }

    public void takeDamage(double amt, InternalRobot source) {
        // uncomment this to test immortal base nodes
        //if(type==RobotType.TOWER&&myGameWorld.towerToNode(this).isPowerCore())
        //	return;
        if (type != RobotType.TOWER || myGameWorld.towerToNode(this).connected(source.getTeam()))
            takeDamage(amt);
    }

    public void changeEnergonLevelFromAttack(double amount) {
        hasBeenAttacked = true;
        changeEnergonLevel(amount * (buffs.getDamageReceivedMultiplier() + 1));
    }

    public void changeEnergonLevel(double amount) {
        myEnergonLevel += amount;
        if (myEnergonLevel > getMaxEnergon()) {
            myEnergonLevel = getMaxEnergon();
        }
        energonChanged = true;

        if (myEnergonLevel <= 0) {
            processLethalDamage();
        }
    }

    public void processLethalDamage() {
        myGameWorld.notifyDied(this);
    }

    public boolean clearEnergonChanged() {
        boolean wasChanged = energonChanged;
        energonChanged = false;
        return wasChanged;
    }

    public boolean clearFluxChanged() {
        boolean wasChanged = fluxChanged;
        fluxChanged = false;
        return wasChanged;
    }

    public double getMaxEnergon() {
        return type.maxEnergon;
    }

    public void activateMovement(Signal s, int delay) {
        movementSignal = s;
        turnsUntilMovementIdle = delay;
    }

    public void delayAttack(int delay) {
        turnsUntilAttackIdle += delay;
    }

    public void activateAttack(Signal s, int delay) {
        myGameWorld.visitSignal(s);
        turnsUntilAttackIdle = delay;
    }

    public void activateBroadcast(Signal s) {
        myGameWorld.visitSignal(s);
        broadcasted = true;
    }

    public int roundsUntilAttackIdle() {
        return turnsUntilAttackIdle;
    }

    public int roundsUntilMovementIdle() {
        return turnsUntilMovementIdle;
    }

    public boolean hasBroadcasted() {
        return broadcasted;
    }

    public void setLocation(MapLocation loc) {
        super.setLocation(loc);
        saveMapMemory(loc);
    }

    public void setDirection(Direction dir) {
        myDirection = dir;
        saveMapMemory(getLocation());
    }

    public void suicide() {
        (new DeathSignal(this)).accept(myGameWorld);
    }

    public void enqueueIncomingMessage(Message msg) {
        incomingMessageQueue.add(msg);
    }

    public Message dequeueIncomingMessage() {
        if (incomingMessageQueue.size() > 0) {
            return incomingMessageQueue.remove(0);
        } else {
            return null;
        }
        // ~ return incomingMessageQueue.poll();
    }

    public Message[] dequeueIncomingMessages() {
        Message[] result = incomingMessageQueue.toArray(new Message[incomingMessageQueue.size()]);
        incomingMessageQueue.clear();
        return result;
    }

    public GameMap.MapMemory getMapMemory() {
        return mapMemory;
    }

    public void saveMapMemory(MapLocation oldLoc, MapLocation newLoc,
                              boolean fringeOnly) {
        saveMapMemory(newLoc);
    }

    public void saveMapMemory(MapLocation newLoc) {
        int[][] myOffsets = offsets.get(type)[myDirection.ordinal()];
        mapMemory.rememberLocations(newLoc, myOffsets[0], myOffsets[1]);
    }

    public void setControlBits(long l) {
        controlBits = l;
    }

    public long getControlBits() {
        return controlBits;
    }

    public void setBytecodesUsed(int numBytecodes) {
        bytecodesUsed = numBytecodes;
    }

    public int getBytecodesUsed() {
        return bytecodesUsed;
    }

    public int getBytecodeLimit() {
        return upkeepPaid ? GameConstants.BYTECODE_LIMIT : 0;
    }

    public boolean hasBeenAttacked() {
        return hasBeenAttacked;
    }

    @Override
    public String toString() {
        return String.format("%s:%s#%d", getTeam(), type, getID());
    }

    public void freeMemory() {
        incomingMessageQueue = null;
        mapMemory = null;
        buffs = null;
        movementSignal = null;
    }
}
