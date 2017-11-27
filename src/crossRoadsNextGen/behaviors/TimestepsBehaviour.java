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
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TimestepsBehaviour extends CyclicBehaviour {

    private WorldAgent worldagent;
    private final String[] vehicles = {"car", "car", "car", "car", "car", "car", "car", "car", "motorcycle", "motorcycle",
        "bus", "truck"};
    private final String[] westDirections = {"west_north", "west_north_2", "west_south", "west_south_2", "west_east"};
    private final String[] northDirections = {"north_south", "north_south_2", "north_north", "north_east"};
    private final String[] north2Directions = {"north_2_south_2"};
    private final String[] southDirections = {"south_north", "south_north_2", "south_south", "south_east"};
    private final String[] south2Directions = {"south_2_north", "south_2_north_2", "south_2_south", "south_2_east"};
    private final String[] eastDirections = {"east_north", "east_north_2", "east_south", "east_south_2"};
    private final Random rand = new Random();
    private int index = 0;

    @Override
    public void action() {
        worldagent = (WorldAgent) myAgent;

        //current simulation time
        int simtime;

        try {
            getService();
            simtime = (int) worldagent.getConn().do_job_get(Simulation.getCurrentTime());

            System.out.println("NEW STEP: " + simtime);

            informAgents(simtime);
            generateCars(simtime);

            worldagent.getConn().do_timestep();
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
        if (Math.random() < 0.005) {
            route = rand.nextInt(westDirections.length);
            vehicle = rand.nextInt(vehicles.length);
            worldagent.getConn().do_job_set(Vehicle.add("veh-w-" + index, vehicles[vehicle], westDirections[route], simtime, 0, 13.8,
                    (byte) 1));
        }
        if (Math.random() < 0.005) {
            route = rand.nextInt(northDirections.length);
            vehicle = rand.nextInt(vehicles.length);
            worldagent.getConn().do_job_set(Vehicle.add("veh-n-" + index, vehicles[vehicle], northDirections[route], simtime, 0, 13.8,
                    (byte) 1));
        }
        
        if (Math.random() < 0.001) {
            route = rand.nextInt(north2Directions.length);
            vehicle = rand.nextInt(vehicles.length);
            worldagent.getConn().do_job_set(Vehicle.add("veh-n2-" + index, vehicles[vehicle], north2Directions[route], simtime, 0, 13.8,
                    (byte) 0));
        }
        if (Math.random() < 0.005) {
            route = rand.nextInt(southDirections.length);
            vehicle = rand.nextInt(vehicles.length);
            worldagent.getConn().do_job_set(Vehicle.add("veh-s-" + index, vehicles[vehicle], southDirections[route], simtime, 0, 13.8,
                    (byte) 1));
        }
        if (Math.random() < 0.004) {
            route = rand.nextInt(south2Directions.length);
            vehicle = rand.nextInt(vehicles.length);
            worldagent.getConn().do_job_set(Vehicle.add("veh-s2-" + index, vehicles[vehicle], south2Directions[route], simtime, 0, 13.8,
                    (byte) 1));
        }
        if (Math.random() < 0.007) {
            route = rand.nextInt(eastDirections.length);
            vehicle = rand.nextInt(vehicles.length);
            worldagent.getConn().do_job_set(Vehicle.add("veh-e-" + index, vehicles[vehicle], eastDirections[route], simtime, 0, 13.8,
                    (byte) 1));
        }
        index++;
    }
}
