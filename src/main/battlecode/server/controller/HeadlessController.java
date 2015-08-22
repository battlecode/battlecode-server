package battlecode.server.controller;

import java.io.IOException;

import battlecode.serial.MatchInfo;
import battlecode.server.Config;

/**
 * Adapts match parameters from a set of properties (i.e., the configuration
 * file or command line).
 */
public class HeadlessController extends Controller {

    /**
     * The match data for the headless match.
     */
    private final MatchInfo configInfo;

    /**
     * Creates a headless controller using config/command-line properties.
     *
     * @param options
     *            the properties to use for getting the map and teams.
     */
    public HeadlessController(Config options) {
        configInfo = new MatchInfo(options.get("bc.game.team-a"), options.get("bc.game.team-b"),
                options.get("bc.game.maps").split(","));
    }

    /**
     * Passes the parameters back to the server and sends the start
     * notification.
     * <p/>
     * {@inheritDoc}
     */
    public void start() throws IOException {
        this.setChanged();
        this.notifyObservers(configInfo);
        this.clearChanged();
    }

    public void finish() throws IOException {
        // Do nothing.
    }
}
