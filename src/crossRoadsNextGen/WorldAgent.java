package crossRoadsNextGen;

import crossRoadsNextGen.behaviors.TimestepsBehaviour;
import de.tudresden.sumo.cmd.Trafficlight;
import de.tudresden.ws.container.SumoStringList;
import it.polito.appeal.traci.SumoTraciConnection;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

/**
 * World agent managing crossroads and simulation time steps
 */
public class WorldAgent extends Agent {

    static String sumo_bin = "c:/Program Files (x86)/DLR/Sumo/bin/sumo-gui.exe";
    static String config_file = "src/sumo_jade/simulation/config.sumo.cfg";
    static String simfile;
    private SumoTraciConnection conn;

    private DFAgentDescription[] crossroads; // DFAgentDescription

    @Override
    protected void setup() {

        AgentContainer c = getContainerController();
        try {
            conn = new SumoTraciConnection(sumo_bin, config_file);
            conn.addOption("step-length", "0.1");  // timestep 100 ms

            // Load routes and initialize the simulation
            conn.runServer();
            conn.do_timestep();

            SumoStringList trafficLights = (SumoStringList) conn.do_job_get(Trafficlight.getIDList());

            // Initiate arrays of crossroads
            crossroads = new DFAgentDescription[trafficLights.size()];

            // Create crossroads as agents
            AgentController crossAgent;
            for (int i = 0; i < trafficLights.size(); i++) {
                String id = trafficLights.get(i);
                crossAgent = c.createNewAgent("crossAgent" + id,
                        "crossRoadsNextGen.CrossAgent",
                        new Object[]{id, conn, getAID()});
                crossAgent.start();
            }

            addBehaviour(new TimestepsBehaviour());

            // Stop TraCI
            //conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public SumoTraciConnection getConn() {
        return conn;
    }

    public DFAgentDescription[] getCrossroads() {
        return crossroads;
    }

    public void setCrossroads(DFAgentDescription[] crossroads) {
        this.crossroads = crossroads;
    }
}
