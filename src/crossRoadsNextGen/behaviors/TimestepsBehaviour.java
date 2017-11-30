package crossRoadsNextGen.behaviors;

import crossRoadsNextGen.WorldAgent;
import de.tudresden.sumo.cmd.Simulation;
import de.tudresden.sumo.cmd.Vehicle;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TimestepsBehaviour extends CyclicBehaviour {

    private WorldAgent worldagent;
    private final String[] vehicles = {"car", "car", "car", "car", "car", "car", "car", "car", "car", "car", "car", "car", "motorcycle",
        "motorcycle", "bus"};
    private final String[] westDirections = {"west_north", "west_north_2", "west_south", "west_south", "west_south_2", "west_south_2",
        "west_east", "west_east"};
    private final String[] northDirections = {"north_south", "north_south", "north_south_2", "north_east", "north_east"};
    private final String[] north2Directions = {"north_2_south_2"};
    private final String[] southDirections = {"south_north", "south_north", "south_south_2", "south_east", "south_east"};
    private final String[] south2Directions = {"south_2_north", "south_2_north", "south_2_north_2", "south_2_east", "south_2_east"};
    private final String[] eastDirections = {"east_north", "east_north", "east_north_2", "east_south", "east_south_2"};
    private final Random rand = new Random();
    private int index = 0;
    private double jam = 2;

    @Override
    public void action() {
        worldagent = (WorldAgent) myAgent;

        //current simulation time
        int simtime;

        try {
            worldagent.getConn().do_timestep();
            simtime = (int) worldagent.getConn().do_job_get(Simulation.getCurrentTime());
            generateCars(simtime);
            getService();
            informAgents(simtime);
            waitForResponses();
            System.out.println("SYNCED");
        } catch (Exception ex) {
            Logger.getLogger(TimestepsBehaviour.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Get and save crossroad references
     */
    private void getService() {
        ServiceDescription sd = new ServiceDescription();
        sd.setType("crossroad");

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.addServices(sd);

        try {
            worldagent.setCrossroads(DFService.search(worldagent, dfd));
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inform agents about new time step
     */
    private void informAgents(int simtime) {
        for (DFAgentDescription aid : worldagent.getCrossroads()) {
            String msg = Integer.toString(simtime);
            ACLMessage m = new ACLMessage(ACLMessage.INFORM);
            m.addReceiver(aid.getName());
            m.setContent(msg);
            worldagent.send(m);
        }
    }

    /**
     * Generate new cars into intersection
     */
    private void generateCars(int simtime) throws Exception {
        int route, vehicle;
        if (Math.random() < 0.016 * jam) {
            route = rand.nextInt(westDirections.length);
            vehicle = rand.nextInt(vehicles.length);
            worldagent.getConn().do_job_set(Vehicle.add("veh-w-" + index, vehicles[vehicle], westDirections[route], simtime, 0, 13.8,
                    (byte) 1));
        }
        if (Math.random() < 0.022 * jam) {
            route = rand.nextInt(northDirections.length);
            vehicle = rand.nextInt(vehicles.length);
            worldagent.getConn().do_job_set(Vehicle.add("veh-n-" + index, vehicles[vehicle], northDirections[route], simtime, 0, 13.8,
                    (byte) 1));
        }
        if (Math.random() < 0.002 * jam) {
            route = rand.nextInt(north2Directions.length);
            vehicle = rand.nextInt(vehicles.length);
            worldagent.getConn().do_job_set(Vehicle.add("veh-n2-" + index, vehicles[vehicle], north2Directions[route], simtime, 0, 13.8,
                    (byte) 0));
        }
        if (Math.random() < 0.016 * jam) {
            route = rand.nextInt(southDirections.length);
            vehicle = rand.nextInt(vehicles.length);
            worldagent.getConn().do_job_set(Vehicle.add("veh-s-" + index, vehicles[vehicle], southDirections[route], simtime, 0, 13.8,
                    (byte) 1));
        }
        if (Math.random() < 0.014 * jam) {
            route = rand.nextInt(south2Directions.length);
            vehicle = rand.nextInt(vehicles.length);
            worldagent.getConn().do_job_set(Vehicle.add("veh-s2-" + index, vehicles[vehicle], south2Directions[route], simtime, 0, 13.8,
                    (byte) 1));
        }
        if (Math.random() < 0.022 * jam) {
            route = rand.nextInt(eastDirections.length);
            vehicle = rand.nextInt(vehicles.length);
            worldagent.getConn().do_job_set(Vehicle.add("veh-e-" + index, vehicles[vehicle], eastDirections[route], simtime, 0, 13.8,
                    (byte) 1));
        }
        index++;
    }

    private void waitForResponses() {
        int count = 0;
        while (count < 2) {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                count ++;
            }
        }
    }
    
    public double getTime() {
        return index;
    }
}
