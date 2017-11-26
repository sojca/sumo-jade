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
import java.util.logging.Level;
import java.util.logging.Logger;

public class TimestepsBehaviour extends CyclicBehaviour {

    private WorldAgent worldagent;

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
        worldagent.getConn().do_job_set(Vehicle.add("veh" + index, "car", "s1", simtime, 0, 13.8, (byte) 1));
        worldagent.getConn().do_job_set(Vehicle.add("veh-" + index, "car", "s2", simtime, 0, 13.8, (byte) 1));
        index++;
    }
}
