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
            switch ((String) crossagent.getConn().do_job_get(Trafficlight.getRedYellowGreenState("gneJ1"))) {
                case ("rrGGrrrrGg"):
                    if (crossagent.getDuration() > 330) {
                        crossagent.resetDuration();
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ1", J1Constraints.NORTH_SOUTH_YELLOW.toString()));
                    }
                    break;
                case ("rryyrrrryg"):
                    if (crossagent.getDuration() > 30) {

                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ1", J1Constraints.NORTH_SOUTH_CROSS_GREEN.toString()));
                    }
                    break;

                case ("rrrrrrrrrG"):
                    if (crossagent.getDuration() > 60) {

                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ1", J1Constraints.NORTH_SOUTH_CROSS_YELLOW.toString()));
                    }
                    break;
                case ("rrrrrrrrry"):
                    if (crossagent.getDuration() > 30) {

                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ1", J1Constraints.WEST_EAST_GREEN.toString()));
                    }
                    break;
                case ("GgrrGGGgrr"):
                    if (crossagent.getDuration() > 330) {
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ1", J1Constraints.WEST_EAST_YELLOW.toString()));
                    }
                    break;

                case ("ygrryyygrr"):
                    if (crossagent.getDuration() > 30) {
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ1", J1Constraints.WEST_EAST_CROSS_GREEN.toString()));
                    }
                    break;
                case ("rGrrrrrGrr"):
                    if (crossagent.getDuration() > 60) {
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ1", J1Constraints.WEST_EAST_CROSS_YELLOW.toString()));
                    }
                    break;
                case ("ryrrrrryrr"):
                    if (crossagent.getDuration() > 30) {
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ1", J1Constraints.NORTH_SOUTH_GREEN.toString()));
                    }
                    break;
            }

        } 
//        else if ("gneJ3".equals(crossagent.getId())) {
//            crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ3", J3Constraints.NORTH_SOUTH_CROSS_GREEN.toString()));
//        }
    }
}
