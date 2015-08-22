package battlecode.server;

import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import battlecode.common.MapLocation;
import battlecode.common.RobotType;
import battlecode.common.Team;
import battlecode.common.TerrainTile;
import battlecode.engine.signal.Signal;
import battlecode.serial.DominationFactor;
import battlecode.serial.ExtensibleMetadata;
import battlecode.serial.GameStats;
import battlecode.serial.MatchFooter;
import battlecode.serial.MatchHeader;
import battlecode.serial.MatchInfo;
import battlecode.serial.RoundDelta;
import battlecode.serial.RoundStats;
import battlecode.serial.notification.PauseNotification;
import battlecode.serial.notification.ResumeNotification;
import battlecode.serial.notification.RunNotification;
import battlecode.serial.notification.StartNotification;
import battlecode.server.serializer.JavaSerializer;
import battlecode.server.serializer.Serializer;
import battlecode.server.serializer.XStreamSerializer;
import battlecode.world.GameMap;
import battlecode.world.GameWorld;
import battlecode.world.InternalRobot;
import battlecode.world.signal.AttackSignal;
import battlecode.world.signal.BashSignal;
import battlecode.world.signal.BroadcastSignal;
import battlecode.world.signal.BuildSignal;
import battlecode.world.signal.BytecodesUsedSignal;
import battlecode.world.signal.CastSignal;
import battlecode.world.signal.ControlBitsSignal;
import battlecode.world.signal.DeathSignal;
import battlecode.world.signal.HealthChangeSignal;
import battlecode.world.signal.IndicatorDotSignal;
import battlecode.world.signal.IndicatorLineSignal;
import battlecode.world.signal.IndicatorStringSignal;
import battlecode.world.signal.LocationOreChangeSignal;
import battlecode.world.signal.MatchObservationSignal;
import battlecode.world.signal.MineSignal;
import battlecode.world.signal.MissileCountSignal;
import battlecode.world.signal.MovementOverrideSignal;
import battlecode.world.signal.MovementSignal;
import battlecode.world.signal.RobotInfoSignal;
import battlecode.world.signal.SelfDestructSignal;
import battlecode.world.signal.SpawnSignal;
import battlecode.world.signal.TeamOreSignal;
import battlecode.world.signal.XPSignal;

/**
 * Created by james on 7/28/15.
 */
public class SerializerTest {
    static final Map<GameMap.MapProperties, Integer> properties = new HashMap<>();

    static {
        properties.put(GameMap.MapProperties.HEIGHT, 3);
        properties.put(GameMap.MapProperties.WIDTH, 3);
        properties.put(GameMap.MapProperties.MAX_ROUNDS, 2000);
        properties.put(GameMap.MapProperties.SEED, 12345);
    }

    static final TerrainTile[][] tiles = new TerrainTile[][] {
            new TerrainTile[] { TerrainTile.NORMAL, TerrainTile.OFF_MAP, TerrainTile.VOID },
            new TerrainTile[] { TerrainTile.NORMAL, TerrainTile.UNKNOWN, TerrainTile.VOID },
            new TerrainTile[] { TerrainTile.NORMAL, TerrainTile.VOID, TerrainTile.VOID }, };

    static final int[][] ores = new int[][] { new int[] { 0, 1, 2 }, new int[] { 3, 4, 5 }, new int[] { 6, 7, 8 }, };

    static final GameMap gameMap = new GameMap(properties, tiles, ores, "Test Map");

    static final long[][] teamMemories = new long[][] { new long[] { 1, 2, 3, 4, 5 }, new long[] { 1, 2, 3, 4, 5 }, };

    static final GameWorld gameWorld = new GameWorld(gameMap, "Team 1", "Team 2", teamMemories);

    static final InternalRobot robot = new InternalRobot(gameWorld, RobotType.DRONE, new MapLocation(0, 0), Team.A,
            false, 0);

    // An array with a sample object from every type of thing we could ever want
    // to serialize / deserialize.
    static final Object[] serializeableObjects = new Object[] { PauseNotification.INSTANCE, ResumeNotification.INSTANCE,
            RunNotification.forever(), StartNotification.INSTANCE,
            new MatchInfo("Team 1", "Team 2", new String[] { "Map 1", "Map 2" }),
            new MatchHeader(gameMap, teamMemories, 0, 3),
            new RoundDelta(new Signal[] { new AttackSignal(robot, new MapLocation(1, 1)),
                    new BashSignal(robot, new MapLocation(1, 1)),
                    new BroadcastSignal(robot, new HashMap<Integer, Integer>()), new BuildSignal(robot, robot, 2), // This
                                                                                                                   // is
                                                                                                                   // technically
                                                                                                                   // incorrect
                                                                                                                   // but
                                                                                                                   // whatever
                    new BytecodesUsedSignal(new InternalRobot[] { robot }),
                    new CastSignal(robot, new MapLocation(-75, -75)), new ControlBitsSignal(0, 0),
                    new DeathSignal(robot), new HealthChangeSignal(new InternalRobot[] { robot }),
                    new IndicatorDotSignal(robot, new MapLocation(0, 0), 0, 0, 0),
                    new IndicatorLineSignal(robot, new MapLocation(0, 0), new MapLocation(1, 1), 0, 0, 0),
                    new IndicatorStringSignal(robot, 0, "Test Indicator String"),
                    new LocationOreChangeSignal(new MapLocation(0, 0), -1.0), new MatchObservationSignal(robot, "test"),
                    new MineSignal(new MapLocation(0, 0), 0), new MissileCountSignal(0, 1),
                    new MovementOverrideSignal(0, new MapLocation(10000, 10000)),
                    new MovementSignal(robot, new MapLocation(0, 0), true),
                    new RobotInfoSignal(new InternalRobot[] { robot }),
                    new SelfDestructSignal(robot, new MapLocation(0, 0), 1000), new SpawnSignal(robot, robot, 1000),
                    new TeamOreSignal(new double[] { 10000, 21293 }), new XPSignal(0, 1000) }),
            new MatchFooter(Team.A, teamMemories), new RoundStats(100, 100), new GameStats(),
            DominationFactor.BARELY_BARELY_BEAT, new ExtensibleMetadata() };

    @Test
    public void testJavaRoundTrip() {
        testRoundTrip(new JavaSerializer());
    }

    @Test
    public void testXStreamRoundTrip() {
        testRoundTrip(new XStreamSerializer());
    }

    public void testRoundTrip(final Serializer serializer) {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        for (final Object message : serializeableObjects) {
            output.reset();
            try {
                serializer.serialize(output, message);
            } catch (final IOException e) {
                fail("Couldn't serialize object of class: " + message.getClass().getCanonicalName() + ": " + e);
            }

            final ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
            final Object result;
            try {
                result = serializer.deserialize(input);
            } catch (final IOException e) {
                fail("Couldn't deserialize object of class: " + message.getClass().getCanonicalName() + ": " + e);
                return; // To satisfy "might not have been initialized"
            }

            // TODO assertEquals(message, result);
            // For this to work, we'll need to override Object.equals on any
            // class we want to serialize.
        }
    }
}
