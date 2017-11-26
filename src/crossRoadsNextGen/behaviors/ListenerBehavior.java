package crossRoadsNextGen.behaviors;

import crossRoadsNextGen.CrossAgent;
import de.tudresden.sumo.cmd.Trafficlight;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ListenerBehavior extends CyclicBehaviour {

    private CrossAgent crossagent;
    private boolean defaultBehavior = false;

    @Override
    public void action() {
        crossagent = (CrossAgent) myAgent;

        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        ACLMessage msg = myAgent.receive(mt);

        if (msg == null) {
            block();
        } else {
            // Parse message
            crossagent.timestep(Integer.parseInt(msg.getContent()));
            try {
                if (!defaultBehavior) {
                    updateLights();
                }
            } catch (Exception ex) {
                Logger.getLogger(ListenerBehavior.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void updateLights() throws Exception {
        if ("gneJ1".equals(crossagent.getId())) {
            crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ1", J1Constraints.NORTH_SOUTH_CROSS_GREEN.toString()));
        } else if ("gneJ3".equals(crossagent.getId())) {
            crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ3", J3Constraints.NORTH_SOUTH_CROSS_GREEN.toString()));
        }
    }
}
