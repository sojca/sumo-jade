package crossRoadsNextGen.behaviors;

import crossRoadsNextGen.CrossAgent;
import de.tudresden.sumo.cmd.Edge;
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
        {0, -10, -20, -30},
        {10, 0, -10, -20},
        {20, 10, 0, -10},
        {30, 20, 10, 0}
    };

    private final int[][] rules_we_gnej1 = new int[][]{
        {0, 2, 4, 8},
        {-2, 0, 2, 4},
        {-4, -2, 0, 2},
        {-8, -4, -2, 0}
    };

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
                    int[] fuzzy_set = fuzzifier(crossagent.getId());

                    updateLights(fuzzy_set);
                }
            } catch (Exception ex) {
                Logger.getLogger(ListenerBehavior.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private int[] fuzzifier(String junctionID) throws Exception {
        // north (gneE2), south (gneE7) - main road
        double n = (double) crossagent.getConn().do_job_get(Edge.getWaitingTime("gneE2"));
        double s = (double) crossagent.getConn().do_job_get(Edge.getWaitingTime("gneE7"));
        double w = (double) crossagent.getConn().do_job_get(Edge.getWaitingTime("gneE0"));
        double e = (double) crossagent.getConn().do_job_get(Edge.getWaitingTime("gneE4"));
        int[] ret = new int[]{0, 0};

        if ((n + s) / 2 == 0) {
            ret[0] = ZERO;
        } else if ((n + s) / 2 < 30) {
            ret[0] = SMALL;
        } else if ((n + s) / 2 < 90) {
            ret[0] = MEDIUM;
        } else if ((n + s) / 2 >= 90) {
            ret[0] = LARGE;
        }

        if ((w + e) / 2 == 0) {
            ret[1] = ZERO;
        } else if ((w + e) / 2 < 30) {
            ret[1] = SMALL;
        } else if ((w + e) / 2 < 90) {
            ret[1] = MEDIUM;
        } else if ((w + e) / 2 >= 90) {
            ret[1] = LARGE;
        }
        System.out.println("NS: " + ret[0] + ", WE: " +  ret[1]);
        return ret;
    }

    private void updateLights(int[] fuzzy_set) throws Exception {

        if ("gneJ1".equals(crossagent.getId())) {
            switch ((String) crossagent.getConn().do_job_get(Trafficlight.getRedYellowGreenState("gneJ1"))) {
                case ("rrGGrrrrGg"):
                    defaultTime += rules_ns_gnej1[fuzzy_set[0]][fuzzy_set[1]];
                    if (crossagent.getDuration() > defaultTime) {
                        crossagent.resetDuration();
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ1", J1Constraints.NORTH_SOUTH_YELLOW.toString()));
                        defaultTime = 30;
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
                    if (crossagent.getDuration() > defaultTime) {
                        crossagent.resetDuration();
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ1", J1Constraints.NORTH_SOUTH_CROSS_YELLOW.toString()));
                        defaultTime = 30;
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
                    defaultTime += rules_we_gnej1[fuzzy_set[0]][fuzzy_set[1]];

                    if (crossagent.getDuration() > defaultTime) {
                        crossagent.resetDuration();
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ1", J1Constraints.WEST_EAST_YELLOW.toString()));
                        defaultTime = 30;
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
                        defaultTime = 30;
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
//        else if ("gneJ3".equals(crossagent.getId())) {
//            crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState("gneJ3", J3Constraints.NORTH_SOUTH_CROSS_GREEN.toString()));
//        }
    }
}
