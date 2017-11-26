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

    private final String stateJ1_1 = "rrGGrrrrGg";
    private final String stateJ1_2 = "rryyrrrryg";
    private final String stateJ1_3 = "rrrrrrrrrG";
    private final String stateJ1_4 = "rrrrrrrrry";
    private final String stateJ1_5 = "GgrrGGGgrr";
    private final String stateJ1_6 = "ygrryyygrr";
    private final String stateJ1_7 = "rGrrrrrGrr";
    private final String stateJ1_8 = "ryrrrrryrr";

    private final String stateJ3_1 = "GGGgrrrGGGgrrr";
    private final String stateJ3_2 = "yyygrrryyygrrr";
    private final String stateJ3_3 = "rrrGrrrrrrGrrr";
    private final String stateJ3_4 = "rrryrrrrrryrrr";
    private final String stateJ3_5 = "rrrrGGgrrrrGGg";
    private final String stateJ3_6 = "rrrryygrrrryyg";
    private final String stateJ3_7 = "rrrrrrGrrrrrrG";
    private final String stateJ3_8 = "rrrrrryrrrrrry";

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
                updateLights();
            } catch (Exception ex) {
                Logger.getLogger(ListenerBehavior.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void updateLights() throws Exception {
        if ("gneJ1".equals(crossagent.getId())) {
            crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ1", stateJ1_1));
        } else if ("gneJ3".equals(crossagent.getId())) {
            crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ3", stateJ3_1));
        }
    }
}
