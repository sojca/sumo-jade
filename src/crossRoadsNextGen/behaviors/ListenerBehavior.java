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
    private int defaultTime = 50;
    // first index is main road
    private final int[][] rules_ns_gnej1 = new int[][]{
        {0, -1, -3, -6},
        {1, 0, -1, 3},
        {3, 1, 0, -1},
        {6, 3, 1, 0}
    };

    private final int[][] rules_we_gnej1 = new int[][]{
        {0, 1, 3, 6},
        {-1, 0, 1, 3},
        {-3, -1, 0, 1},
        {-6, -3, -1, 0}
    };

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
                    int neLane = (int) crossagent.getConn().do_job_get(Lane.getLastStepVehicleNumber("gneE2_1"));
                    int s = (int) crossagent.getConn().do_job_get(Edge.getLastStepVehicleNumber("gneE7"));
                    int w = (int) crossagent.getConn().do_job_get(Edge.getLastStepVehicleNumber("gneE0"));
                    int e = (int) crossagent.getConn().do_job_get(Edge.getLastStepVehicleNumber("gneE4"));

                    double ns1 = crossagent.getFuzzyController().InterferenceAndDefuzzy((n + s) / 2, (w + e) / 2);
                    double we1 = crossagent.getFuzzyController().InterferenceAndDefuzzy((w + e) / 2, (n + s) / 2);
                    double ne = crossagent.getFuzzyController().InterferenceAndDefuzzy(neLane, (w + e) / 4);

                    n = (int) crossagent.getConn().do_job_get(Edge.getLastStepVehicleNumber("gneE15"));
                    //int neLane = (int) crossagent.getConn().do_job_get(Lane.getLastStepVehicleNumber("gneE2_1"));
                    s = (int) crossagent.getConn().do_job_get(Edge.getLastStepVehicleNumber("gneE12"));
                    w = (int) crossagent.getConn().do_job_get(Edge.getLastStepVehicleNumber("gneE5"));
                    e = (int) crossagent.getConn().do_job_get(Edge.getLastStepVehicleNumber("gneE10"));
                    
                    double ns3 = crossagent.getFuzzyController().InterferenceAndDefuzzy((n + s), (w + e) / 2);
                    double we3 = crossagent.getFuzzyController().InterferenceAndDefuzzy((w + e) / 2, (n + s) / 2);

                    updateLights(ns1, we1, ne, ns3, we3);
                }
            } catch (Exception ex) {
                Logger.getLogger(ListenerBehavior.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void updateLights(double ns1, double we1, double ne, double ns3, double we3) throws Exception {

        if ("gneJ1".equals(crossagent.getId())) {
            switch ((String) crossagent.getConn().do_job_get(Trafficlight.getRedYellowGreenState("gneJ1"))) {
                case ("rrGGrrrrGg"):
                    defaultTime += ns1;
                    System.out.println("NS1: " + ns1);
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
                    defaultTime += ne;
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
                    defaultTime += we1;
                    System.out.println("WE1: " + we1);

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
        else if ("gneJ3".equals(crossagent.getId())) {
            switch ((String) crossagent.getConn().do_job_get(Trafficlight.getRedYellowGreenState("gneJ3"))) {
                case ("rrrrGGgrrrrGGg"):
                    defaultTime += ns3;
                    if (crossagent.getDuration() > defaultTime) {
                        crossagent.resetDuration();
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ3", J3Constraints.NORTH_SOUTH_YELLOW.toString()));
                        defaultTime = 20;
                    }
                    break;
                case ("rrrryygrrrryyg"):
                    if (crossagent.getDuration() > defaultTime) {
                        crossagent.resetDuration();
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ3", J3Constraints.NORTH_SOUTH_CROSS_GREEN.toString()));
                        defaultTime = 60;
                    }
                    break;
                case ("rrrrrrGrrrrrrG"):
                    //defaultTime += ne;
                    if (crossagent.getDuration() > defaultTime) {
                        crossagent.resetDuration();
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ3", J3Constraints.NORTH_SOUTH_CROSS_YELLOW.toString()));
                        defaultTime = 20;
                    }
                    break;
                case ("rrrrrryrrrrrry"):
                    if (crossagent.getDuration() > defaultTime) {
                        crossagent.resetDuration();
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ3", J3Constraints.WEST_EAST_GREEN.toString()));
                        defaultTime = 330;
                    }
                    break;
                case ("GGGgrrrGGGgrrr"):
                    defaultTime += we3;

                    if (crossagent.getDuration() > defaultTime) {
                        crossagent.resetDuration();
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ3", J3Constraints.WEST_EAST_YELLOW.toString()));
                        defaultTime = 20;
                    }
                    break;

                case ("yyygrrryyygrrr"):
                    if (crossagent.getDuration() > defaultTime) {
                        crossagent.resetDuration();
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ3", J3Constraints.WEST_EAST_CROSS_GREEN.toString()));
                        defaultTime = 60;
                    }
                    break;
                case ("rrrGrrrrrrGrrr"):
                    if (crossagent.getDuration() > defaultTime) {
                        crossagent.resetDuration();
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ3", J3Constraints.WEST_EAST_CROSS_YELLOW.toString()));
                        defaultTime = 20;
                    }
                    break;
                case ("rrryrrrrrryrrr"):
                    if (crossagent.getDuration() > defaultTime) {
                        crossagent.resetDuration();
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ3", J3Constraints.NORTH_SOUTH_GREEN.toString()));
                        defaultTime = 330;
                    }
                    break;
            }

        }
    }
}
