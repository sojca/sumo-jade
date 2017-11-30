package crossRoadsNextGen.behaviors;

import crossRoadsNextGen.CrossAgent;
import de.tudresden.sumo.cmd.Edge;
import de.tudresden.sumo.cmd.Lane;
import de.tudresden.sumo.cmd.Trafficlight;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ListenerBehavior extends CyclicBehaviour {

    private final int ZERO = 0;
    private final int SMALL = 1;
    private final int MEDIUM = 2;
    private final int LARGE = 3;
    private int defaultTime = 330;

    private CrossAgent crossagent;
    private boolean useFuzzy = true;

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
                if (useFuzzy) {

                    int n = (int) crossagent.getConn().do_job_get(Edge.getLastStepVehicleNumber("gneE2"));
                    int s = (int) crossagent.getConn().do_job_get(Edge.getLastStepVehicleNumber("gneE7"));
                    int w = (int) crossagent.getConn().do_job_get(Edge.getLastStepVehicleNumber("gneE0"));
                    int e = (int) crossagent.getConn().do_job_get(Edge.getLastStepVehicleNumber("gneE4"));

                    double priN = crossagent.getFuzzyController().InterferenceAndDefuzzyPriority(n, (double) crossagent.getConn().do_job_get(Edge.getTraveltime("gneE2")));
                    double priS = crossagent.getFuzzyController().InterferenceAndDefuzzyPriority(s, (double) crossagent.getConn().do_job_get(Edge.getTraveltime("gneE7")));
                    double priW = crossagent.getFuzzyController().InterferenceAndDefuzzyPriority(w, (double) crossagent.getConn().do_job_get(Edge.getTraveltime("gneE0")));
                    double priE = crossagent.getFuzzyController().InterferenceAndDefuzzyPriority(e, (double) crossagent.getConn().do_job_get(Edge.getTraveltime("gneE4")));

                    double ns1 = crossagent.getFuzzyController().InterferenceAndDefuzzyTime(priN, priS);
                    double we1 = crossagent.getFuzzyController().InterferenceAndDefuzzyTime(priW, priE);

                    n = (int) crossagent.getConn().do_job_get(Edge.getLastStepVehicleNumber("gneE15"));
                    s = (int) crossagent.getConn().do_job_get(Edge.getLastStepVehicleNumber("gneE12"));
                    w = (int) crossagent.getConn().do_job_get(Edge.getLastStepVehicleNumber("gneE5"));
                    e = (int) crossagent.getConn().do_job_get(Edge.getLastStepVehicleNumber("gneE10"));

                    priN = crossagent.getFuzzyController().InterferenceAndDefuzzyPriority(n, (double) crossagent.getConn().do_job_get(Edge.getTraveltime("gneE15")));
                    priS = crossagent.getFuzzyController().InterferenceAndDefuzzyPriority(s, (double) crossagent.getConn().do_job_get(Edge.getTraveltime("gneE12")));
                    priW = crossagent.getFuzzyController().InterferenceAndDefuzzyPriority(w, (double) crossagent.getConn().do_job_get(Edge.getTraveltime("gneE5")));
                    priE = crossagent.getFuzzyController().InterferenceAndDefuzzyPriority(e, (double) crossagent.getConn().do_job_get(Edge.getTraveltime("gneE10")));

                    double ns3 = crossagent.getFuzzyController().InterferenceAndDefuzzyTime(priN, priS);
                    double we3 = crossagent.getFuzzyController().InterferenceAndDefuzzyTime(priW, priE);

                    updateLights(ns1, we1, ns3, we3);
                }
            } catch (Exception ex) {
                Logger.getLogger(ListenerBehavior.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void updateLights(double ns1, double we1, double ns3, double we3) throws Exception {

        if ("gneJ1".equals(crossagent.getId())) {
            switch ((String) crossagent.getConn().do_job_get(Trafficlight.getRedYellowGreenState("gneJ1"))) {
                case ("rrGGrrrrGg"):
//                    defaultTime += ns1;
//                    defaultTime -= we1;

                    //System.out.println("NS1: " + ns1);
                    if (crossagent.getDuration() > defaultTime) {
                        crossagent.resetDuration();
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ1", J1Constraints.NORTH_SOUTH_YELLOW.toString()));
                        defaultTime = 20;
                    }
                    break;
                case ("rryyrrrryg"):
                    if (crossagent.getDuration() > defaultTime) {
                        crossagent.resetDuration();
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ1", J1Constraints.NORTH_SOUTH_CROSS_GREEN.toString()));
                        defaultTime = 60;
                    }
                    break;
                case ("rrrrrrrrrG"):
                    //  defaultTime += ne;
                    if (crossagent.getDuration() > defaultTime) {
                        crossagent.resetDuration();
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ1", J1Constraints.NORTH_SOUTH_CROSS_YELLOW.toString()));
                        defaultTime = 20;
                    }
                    break;
                case ("rrrrrrrrry"):
                    if (crossagent.getDuration() > defaultTime) {
                        crossagent.resetDuration();
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ1", J1Constraints.WEST_EAST_GREEN.toString()));
                        defaultTime = 330;
                    }
                    break;
                case ("GgrrGGGgrr"):
                    //            defaultTime += rules_we_gnej1[fuzzy_set[0]][fuzzy_set[1]];
//                    defaultTime += we1;
//                    defaultTime -= ns1;
//                    System.out.println("WE1: " + we1);

                    if (crossagent.getDuration() > defaultTime) {
                        crossagent.resetDuration();
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ1", J1Constraints.WEST_EAST_YELLOW.toString()));
                        defaultTime = 20;
                    }
                    break;

                case ("ygrryyygrr"):
                    if (crossagent.getDuration() > defaultTime) {
                        crossagent.resetDuration();
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ1", J1Constraints.WEST_EAST_CROSS_GREEN.toString()));
                        defaultTime = 60;
                    }
                    break;
                case ("rGrrrrrGrr"):
                    if (crossagent.getDuration() > defaultTime) {
                        crossagent.resetDuration();
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ1", J1Constraints.WEST_EAST_CROSS_YELLOW.toString()));
                        defaultTime = 20;
                    }
                    break;
                case ("ryrrrrryrr"):
                    if (crossagent.getDuration() > defaultTime) {
                        crossagent.resetDuration();
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ1", J1Constraints.NORTH_SOUTH_GREEN.toString()));
                        defaultTime = 330;
                    }
                    break;
            }

        } 
            //else if ("gneJ3".equals(crossagent.getId())) {
//            switch ((String) crossagent.getConn().do_job_get(Trafficlight.getRedYellowGreenState("gneJ3"))) {
//                case ("rrrrGGgrrrrGGg"):
////                    defaultTime += ns3;
//                    if (crossagent.getDuration() > defaultTime) {
//                        crossagent.resetDuration();
//                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ3", J3Constraints.NORTH_SOUTH_YELLOW.toString()));
//                        defaultTime = 20;
//                    }
//                    break;
//                case ("rrrryygrrrryyg"):
//                    if (crossagent.getDuration() > defaultTime) {
//                        crossagent.resetDuration();
//                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ3", J3Constraints.NORTH_SOUTH_CROSS_GREEN.toString()));
//                        defaultTime = 60;
//                    }
//                    break;
//                case ("rrrrrrGrrrrrrG"):
//                    //defaultTime += ne;
//                    if (crossagent.getDuration() > defaultTime) {
//                        crossagent.resetDuration();
//                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ3", J3Constraints.NORTH_SOUTH_CROSS_YELLOW.toString()));
//                        defaultTime = 20;
//                    }
//                    break;
//                case ("rrrrrryrrrrrry"):
//                    if (crossagent.getDuration() > defaultTime) {
//                        crossagent.resetDuration();
//                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ3", J3Constraints.WEST_EAST_GREEN.toString()));
//                        defaultTime = 330;
//                    }
//                    break;
//                case ("GGGgrrrGGGgrrr"):
////                    defaultTime += we3;
//                    if (crossagent.getDuration() > defaultTime) {
//                        crossagent.resetDuration();
//                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ3", J3Constraints.WEST_EAST_YELLOW.toString()));
//                        defaultTime = 20;
//                    }
//                    break;
//
//                case ("yyygrrryyygrrr"):
//                    if (crossagent.getDuration() > defaultTime) {
//                        crossagent.resetDuration();
//                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ3", J3Constraints.WEST_EAST_CROSS_GREEN.toString()));
//                        defaultTime = 60;
//                    }
//                    break;
//                case ("rrrGrrrrrrGrrr"):
//                    if (crossagent.getDuration() > defaultTime) {
//                        crossagent.resetDuration();
//                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ3", J3Constraints.WEST_EAST_CROSS_YELLOW.toString()));
//                        defaultTime = 20;
//                    }
//                    break;
//                case ("rrryrrrrrryrrr"):
//                    if (crossagent.getDuration() > defaultTime) {
//                        crossagent.resetDuration();
//                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ3", J3Constraints.NORTH_SOUTH_GREEN.toString()));
//                        defaultTime = 330;
//                    }
//                    break;
//            }

//        }
    }
}
