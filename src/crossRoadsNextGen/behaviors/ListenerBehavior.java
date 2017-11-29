package crossRoadsNextGen.behaviors;

import crossRoadsNextGen.CrossAgent;
import de.tudresden.sumo.cmd.Edge;
import de.tudresden.sumo.cmd.Trafficlight;
import fuzzy4j.aggregation.AlgebraicProduct;
import fuzzy4j.aggregation.AlgebraicSum;
import fuzzy4j.flc.ControllerBuilder;
import fuzzy4j.flc.FLC;
import fuzzy4j.flc.InputInstance;
import fuzzy4j.flc.Term;
import fuzzy4j.flc.Variable;
import fuzzy4j.flc.defuzzify.Centroid;
import fuzzy4j.sets.ConstantFunction;
import fuzzy4j.sets.FuzzyFunction;
import fuzzy4j.sets.LinearFunction;
import fuzzy4j.sets.TriangularFunction;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.Map;
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
                    FuzzyFunction zeroCountCars = new TriangularFunction(-5, 0, 5);
                    FuzzyFunction smallCountCars = new TriangularFunction(0, 5, 10);
                    FuzzyFunction mediumCountCars = new TriangularFunction(5, 10, 15);
                    FuzzyFunction largeCountCars = new TriangularFunction(10, 15, 20);

                    FuzzyFunction decrementTime = new TriangularFunction(-12, -6, 0);
                    FuzzyFunction zTime = new TriangularFunction(-6, 0, 6);
                    FuzzyFunction incrementTime = new TriangularFunction(0, 6, 12);

                    Term zeroTerm = Term.term("zero", zeroCountCars);
                    Term smallTerm = Term.term("small", smallCountCars);
                    Term mediumTerm = Term.term("medium", mediumCountCars);
                    Term largeTerm = Term.term("large", largeCountCars);

                    Term decTerm = Term.term("dec", decrementTime);
                    Term zTerm = Term.term("zero", zTime);
                    Term incTerm = Term.term("inc", incrementTime);

                    Variable countCarsNS = Variable.input("car_count_ns", zeroTerm, smallTerm, mediumTerm, largeTerm).start(0);
                    Variable countCarsWE = Variable.input("car_count_we", zeroTerm, smallTerm, mediumTerm, largeTerm).start(0);

                    Variable timeRule = Variable.output("time_rule", decTerm, zTerm, incTerm).start(-6).end(6);

                    FLC impl = ControllerBuilder.newBuilder()
                            .when().var(countCarsNS).is(zeroTerm).and().var(countCarsWE).is(zeroTerm).then().var(timeRule).is(zTerm)
                            .when().var(countCarsNS).is(zeroTerm).and().var(countCarsWE).is(smallTerm).then().var(timeRule).is(zTerm)
                            .when().var(countCarsNS).is(zeroTerm).and().var(countCarsWE).is(mediumTerm).then().var(timeRule).is(decTerm)
                            .when().var(countCarsNS).is(zeroTerm).and().var(countCarsWE).is(largeTerm).then().var(timeRule).is(decTerm)
                            .when().var(countCarsNS).is(smallTerm).and().var(countCarsWE).is(zeroTerm).then().var(timeRule).is(incTerm)
                            .when().var(countCarsNS).is(smallTerm).and().var(countCarsWE).is(smallTerm).then().var(timeRule).is(zTerm)
                            .when().var(countCarsNS).is(smallTerm).and().var(countCarsWE).is(mediumTerm).then().var(timeRule).is(decTerm)
                            .when().var(countCarsNS).is(smallTerm).and().var(countCarsWE).is(largeTerm).then().var(timeRule).is(decTerm)
                            .when().var(countCarsNS).is(mediumTerm).and().var(countCarsWE).is(zeroTerm).then().var(timeRule).is(incTerm)
                            .when().var(countCarsNS).is(mediumTerm).and().var(countCarsWE).is(smallTerm).then().var(timeRule).is(incTerm)
                            .when().var(countCarsNS).is(mediumTerm).and().var(countCarsWE).is(mediumTerm).then().var(timeRule).is(zTerm)
                            .when().var(countCarsNS).is(mediumTerm).and().var(countCarsWE).is(largeTerm).then().var(timeRule).is(decTerm)
                            .when().var(countCarsNS).is(largeTerm).and().var(countCarsWE).is(zeroTerm).then().var(timeRule).is(incTerm)
                            .when().var(countCarsNS).is(largeTerm).and().var(countCarsWE).is(smallTerm).then().var(timeRule).is(incTerm)
                            .when().var(countCarsNS).is(largeTerm).and().var(countCarsWE).is(mediumTerm).then().var(timeRule).is(incTerm)
                            .when().var(countCarsNS).is(largeTerm).and().var(countCarsWE).is(largeTerm).then().var(timeRule).is(zTerm)
                            //                            .activationFunction(AlgebraicProduct.INSTANCE)
                            //                            .accumulationFunction(AlgebraicSum.INSTANCE)
                            .defuzzifier(new Centroid())
                            .create();
                    int n = (int) crossagent.getConn().do_job_get(Edge.getLastStepVehicleNumber("gneE2"));
                    int s = (int) crossagent.getConn().do_job_get(Edge.getLastStepVehicleNumber("gneE7"));
                    int w = (int) crossagent.getConn().do_job_get(Edge.getLastStepVehicleNumber("gneE0"));
                    int e = (int) crossagent.getConn().do_job_get(Edge.getLastStepVehicleNumber("gneE4"));

                    InputInstance instance = new InputInstance().is(countCarsNS, (n + s) / 2).is(countCarsWE, (w + e) / 2);

                    Map<Variable, Double> crisp = impl.apply(instance);
                    System.out.println("normal.fuzzy = " + crisp.get(timeRule) + " cars no.:" + (n + s) / 2);
                    int[] fuzzy_set = fuzzifier(crossagent.getId());
                    if (crisp.get(timeRule) == null) {
                        updateLights(6);
                    } else {
                        updateLights(crisp.get(timeRule));

                    }
                }
            } catch (Exception ex) {
                Logger.getLogger(ListenerBehavior.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private int[] fuzzifier(String junctionID) throws Exception {
        // north (gneE2), south (gneE7) - main road
        int n = (int) crossagent.getConn().do_job_get(Edge.getLastStepVehicleNumber("gneE2"));
        int s = (int) crossagent.getConn().do_job_get(Edge.getLastStepVehicleNumber("gneE7"));
        int w = (int) crossagent.getConn().do_job_get(Edge.getLastStepVehicleNumber("gneE0"));
        int e = (int) crossagent.getConn().do_job_get(Edge.getLastStepVehicleNumber("gneE4"));
        int[] ret = new int[]{0, 0};

        if ((n + s) / 2 < 4) {
            ret[0] = ZERO;
        } else if ((n + s) / 2 < 10) {
            ret[0] = SMALL;
        } else if ((n + s) / 2 < 20) {
            ret[0] = MEDIUM;
        } else if ((n + s) / 2 >= 20) {
            ret[0] = LARGE;
        }

        if ((w + e) / 2 < 4) {
            ret[1] = ZERO;
        } else if ((w + e) / 2 < 10) {
            ret[1] = SMALL;
        } else if ((w + e) / 2 < 20) {
            ret[1] = MEDIUM;
        } else if ((w + e) / 2 >= 20) {
            ret[1] = LARGE;
        }
        //    System.out.println("NS: " + ret[0] + ", WE: " + ret[1]);
        return ret;
    }

    private void updateLights(double d) throws Exception {

        if ("gneJ1".equals(crossagent.getId())) {
            switch ((String) crossagent.getConn().do_job_get(Trafficlight.getRedYellowGreenState("gneJ1"))) {
                case ("rrGGrrrrGg"):
                    defaultTime += d;
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
                    //            defaultTime += rules_we_gnej1[fuzzy_set[0]][fuzzy_set[1]];

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
