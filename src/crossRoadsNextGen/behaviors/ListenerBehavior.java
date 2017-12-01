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

    private double ns1 = 0;
    private double we1 = 0;
    private double ne1 = 0;
    private double wnes1 = 0;

    private double ns3 = 0;
    private double nesw3 = 0;
    private double we3 = 0;
    private double wnes3 = 0;
    private double defaultTime = 330;

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
                    if ("gneJ1".equals(crossagent.getId())) {
                        int n = (int) crossagent.getConn().do_job_get(Edge.getLastStepVehicleNumber("gneE2"));
                        int nLane = (int) crossagent.getConn().do_job_get(Lane.getLastStepVehicleNumber("gneE2_1"));
                        int s = (int) crossagent.getConn().do_job_get(Edge.getLastStepVehicleNumber("gneE7"));
                        int w = (int) crossagent.getConn().do_job_get(Edge.getLastStepVehicleNumber("gneE0"));
                        int wLane = (int) crossagent.getConn().do_job_get(Lane.getLastStepVehicleNumber("gneE0_1"));
                        int e = (int) crossagent.getConn().do_job_get(Edge.getLastStepVehicleNumber("gneE4"));
                        int eLane = (int) crossagent.getConn().do_job_get(Lane.getLastStepVehicleNumber("gneE4_1"));

                        double priN = crossagent.getFuzzyController().InterferenceAndDefuzzyPriority(n, (double) crossagent.getConn().do_job_get(Edge.getWaitingTime("gneE2")));
                        double priNLane = crossagent.getFuzzyController().InterferenceAndDefuzzyPriority(nLane * 2, (double) crossagent.getConn().do_job_get(Lane.getWaitingTime("gneE2_1")));
                        double priS = crossagent.getFuzzyController().InterferenceAndDefuzzyPriority(s, (double) crossagent.getConn().do_job_get(Edge.getWaitingTime("gneE7")));
                        double priW = crossagent.getFuzzyController().InterferenceAndDefuzzyPriority(w, (double) crossagent.getConn().do_job_get(Edge.getWaitingTime("gneE0")));
                        double priWLane = crossagent.getFuzzyController().InterferenceAndDefuzzyPriority(wLane * 2, (double) crossagent.getConn().do_job_get(Lane.getWaitingTime("gneE0_1")));
                        double priE = crossagent.getFuzzyController().InterferenceAndDefuzzyPriority(e, (double) crossagent.getConn().do_job_get(Edge.getWaitingTime("gneE4")));
                        double priELane = crossagent.getFuzzyController().InterferenceAndDefuzzyPriority(eLane * 2, (double) crossagent.getConn().do_job_get(Lane.getWaitingTime("gneE4_1")));

                        ns1 = crossagent.getFuzzyController().InterferenceAndDefuzzyTime(priN, priS);
                        ne1 = crossagent.getFuzzyController().InterferenceAndDefuzzyTime(priNLane, 4);
                        we1 = crossagent.getFuzzyController().InterferenceAndDefuzzyTime(priW, priE);
                        wnes1 = crossagent.getFuzzyController().InterferenceAndDefuzzyTime(priWLane, priELane);

                    } else if ("gneJ3".equals(crossagent.getId())) {

                        int n = (int) crossagent.getConn().do_job_get(Edge.getLastStepVehicleNumber("gneE15"));
                        int nLane = (int) crossagent.getConn().do_job_get(Lane.getLastStepVehicleNumber("gneE15_0"));
                        int s = (int) crossagent.getConn().do_job_get(Edge.getLastStepVehicleNumber("gneE12"));
                        int sLane = (int) crossagent.getConn().do_job_get(Lane.getLastStepVehicleNumber("gneE12_1"));
                        int w = (int) crossagent.getConn().do_job_get(Edge.getLastStepVehicleNumber("gneE5"));
                        int wLane = (int) crossagent.getConn().do_job_get(Lane.getLastStepVehicleNumber("gneE5_1"));
                        int e = (int) crossagent.getConn().do_job_get(Edge.getLastStepVehicleNumber("gneE10"));
                        int eLane = (int) crossagent.getConn().do_job_get(Lane.getLastStepVehicleNumber("gneE10_1"));

                        double priN = crossagent.getFuzzyController().InterferenceAndDefuzzyPriority(n, (double) crossagent.getConn().do_job_get(Edge.getWaitingTime("gneE15")));
                        double priNLane = crossagent.getFuzzyController().InterferenceAndDefuzzyPriority(nLane * 2, (double) crossagent.getConn().do_job_get(Lane.getWaitingTime("gneE15_0")));
                        double priS = crossagent.getFuzzyController().InterferenceAndDefuzzyPriority(s, (double) crossagent.getConn().do_job_get(Edge.getWaitingTime("gneE12")));
                        double priSLane = crossagent.getFuzzyController().InterferenceAndDefuzzyPriority(sLane * 2, (double) crossagent.getConn().do_job_get(Lane.getWaitingTime("gneE12_1")));
                        double priW = crossagent.getFuzzyController().InterferenceAndDefuzzyPriority(w, (double) crossagent.getConn().do_job_get(Edge.getWaitingTime("gneE5")));
                        double priWLane = crossagent.getFuzzyController().InterferenceAndDefuzzyPriority(wLane * 2, (double) crossagent.getConn().do_job_get(Lane.getWaitingTime("gneE5_1")));
                        double priE = crossagent.getFuzzyController().InterferenceAndDefuzzyPriority(e, (double) crossagent.getConn().do_job_get(Edge.getWaitingTime("gneE10")));
                        double priELane = crossagent.getFuzzyController().InterferenceAndDefuzzyPriority(eLane * 2, (double) crossagent.getConn().do_job_get(Lane.getWaitingTime("gneE10_1")));

                        ns3 = crossagent.getFuzzyController().InterferenceAndDefuzzyTime(priN, priS);
                        nesw3 = crossagent.getFuzzyController().InterferenceAndDefuzzyTime(priNLane, priSLane);
                        we3 = crossagent.getFuzzyController().InterferenceAndDefuzzyTime(priW, priE);
                        wnes3 = crossagent.getFuzzyController().InterferenceAndDefuzzyTime(priWLane, priELane);
                    }
                }
//                System.out.println(ns1+ "      "+ we1+ "      "+ ne1+ "      "+ wnes1+ "      "+ ns3+ "      "+ we3+ "      "+ nesw3+ "      "+ wnes3);
                updateLights(ns1, we1, ne1, wnes1, ns3, we3, nesw3, wnes3);
                //updateLights(0,0,0,0,0,0,0,0);
            } catch (Exception ex) {
                Logger.getLogger(ListenerBehavior.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        informWorld();
    }

    private void updateLights(double ns1, double we1, double ne1, double wnes1, double ns3, double we3, double nesw3, double wnes3) throws Exception {
        if (J1Cons.ID.equals(crossagent.getId())) {
            switch ((String) crossagent.getConn().do_job_get(Trafficlight.getRedYellowGreenState(J1Cons.ID))) {
                case (J1Cons.NORTH_SOUTH_GREEN):
                    defaultTime += ns1;
                    defaultTime -= we1;

                    if (crossagent.getDuration() > defaultTime || crossagent.getDuration() > 900) {
                        crossagent.resetDuration();
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState(J1Cons.ID, J1Cons.NORTH_SOUTH_YELLOW));
                        defaultTime = 20;
                    }
                    break;
                case (J1Cons.NORTH_SOUTH_YELLOW):
                    if (crossagent.getDuration() > defaultTime) {
                        crossagent.resetDuration();
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState(J1Cons.ID, J1Cons.NORTH_SOUTH_CROSS_GREEN));
                        defaultTime = 60;
                    }
                    break;
                case (J1Cons.NORTH_SOUTH_CROSS_GREEN):
                    defaultTime += ne1;
                    defaultTime -= we1;
                    if (crossagent.getDuration() > defaultTime || crossagent.getDuration() > 120) {
                        crossagent.resetDuration();
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState(J1Cons.ID, J1Cons.NORTH_SOUTH_CROSS_YELLOW));
                        defaultTime = 20;
                    }
                    break;
                case (J1Cons.NORTH_SOUTH_CROSS_YELLOW):
                    if (crossagent.getDuration() > defaultTime) {
                        crossagent.resetDuration();
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState(J1Cons.ID, J1Cons.WEST_EAST_GREEN));
                        defaultTime = 330;
                    }
                    break;
                case (J1Cons.WEST_EAST_GREEN):
                    //            defaultTime += rules_we_gnej1[fuzzy_set[0]][fuzzy_set[1]];
                    defaultTime += we1;
                    defaultTime -= ns1;
//                    System.out.println("WE1: " + we1);

                    if (crossagent.getDuration() > defaultTime || crossagent.getDuration() > 900) {
                        crossagent.resetDuration();
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState(J1Cons.ID, J1Cons.WEST_EAST_YELLOW));
                        defaultTime = 20;
                    }
                    break;

                case (J1Cons.WEST_EAST_YELLOW):
                    if (crossagent.getDuration() > defaultTime) {
                        crossagent.resetDuration();
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState(J1Cons.ID, J1Cons.WEST_EAST_CROSS_GREEN));
                        defaultTime = 60;
                    }
                    break;
                case (J1Cons.WEST_EAST_CROSS_GREEN):
                    defaultTime += wnes1;
                    defaultTime -= ns1;
                    if (crossagent.getDuration() > defaultTime || crossagent.getDuration() > 120) {
                        crossagent.resetDuration();
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState(J1Cons.ID, J1Cons.WEST_EAST_CROSS_YELLOW));
                        defaultTime = 20;
                    }
                    break;
                case (J1Cons.WEST_EAST_CROSS_YELLOW):
                    if (crossagent.getDuration() > defaultTime) {
                        crossagent.resetDuration();
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState(J1Cons.ID, J1Cons.NORTH_SOUTH_GREEN));
                        defaultTime = 330;
                    }
                    break;
            }

        } else if (J3Cons.ID.equals(crossagent.getId())) {
            switch ((String) crossagent.getConn().do_job_get(Trafficlight.getRedYellowGreenState(J3Cons.ID))) {
                case (J3Cons.NORTH_SOUTH_GREEN):
                    defaultTime += ns3;
                    defaultTime -= we3;
                    if (crossagent.getDuration() > defaultTime || crossagent.getDuration() > 900) {
                        crossagent.resetDuration();
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState(J3Cons.ID, J3Cons.NORTH_SOUTH_YELLOW));
                        defaultTime = 20;
                    }
                    break;
                case (J3Cons.NORTH_SOUTH_YELLOW):
                    if (crossagent.getDuration() > defaultTime) {
                        crossagent.resetDuration();
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState(J3Cons.ID, J3Cons.NORTH_SOUTH_CROSS_GREEN));
                        defaultTime = 60;
                    }
                    break;
                case (J3Cons.NORTH_SOUTH_CROSS_GREEN):
                    defaultTime += nesw3;
                    defaultTime -= we3;
                    if (crossagent.getDuration() > defaultTime || crossagent.getDuration() > 120) {
                        crossagent.resetDuration();
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState(J3Cons.ID, J3Cons.NORTH_SOUTH_CROSS_YELLOW));
                        defaultTime = 20;
                    }
                    break;
                case (J3Cons.NORTH_SOUTH_CROSS_YELLOW):
                    if (crossagent.getDuration() > defaultTime) {
                        crossagent.resetDuration();
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState(J3Cons.ID, J3Cons.WEST_EAST_GREEN));
                        defaultTime = 330;
                    }
                    break;
                case (J3Cons.WEST_EAST_GREEN):
                    defaultTime += we3;
                    defaultTime -= ns3;
                    if (crossagent.getDuration() > defaultTime || crossagent.getDuration() > 900) {
                        crossagent.resetDuration();
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState(J3Cons.ID, J3Cons.WEST_EAST_YELLOW));
                        defaultTime = 20;
                    }
                    break;

                case (J3Cons.WEST_EAST_YELLOW):
                    if (crossagent.getDuration() > defaultTime) {
                        crossagent.resetDuration();
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState(J3Cons.ID, J3Cons.WEST_EAST_CROSS_GREEN));
                        defaultTime = 60;
                    }
                    break;
                case (J3Cons.WEST_EAST_CROSS_GREEN):
                    defaultTime += wnes3;
                    System.out.println(defaultTime + "   " + wnes3);
                    defaultTime -= ns3;
                    System.out.println(defaultTime + "   " + ns3);

                    if (crossagent.getDuration() > defaultTime || crossagent.getDuration() > 120) {
                        crossagent.resetDuration();
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState(J3Cons.ID, J3Cons.WEST_EAST_CROSS_YELLOW));
                        defaultTime = 20;
                    }
                    break;
                case (J3Cons.WEST_EAST_CROSS_YELLOW):
                    if (crossagent.getDuration() > defaultTime) {
                        crossagent.resetDuration();
                        crossagent.getConn().do_job_set(Trafficlight.setRedYellowGreenState(J3Cons.ID, J3Cons.NORTH_SOUTH_GREEN));
                        defaultTime = 330;
                    }
                    break;
            }

        }
    }

    private void informWorld() {
        ACLMessage m = new ACLMessage(ACLMessage.INFORM);
        m.addReceiver(crossagent.getWorld());
        crossagent.send(m);
    }
}
