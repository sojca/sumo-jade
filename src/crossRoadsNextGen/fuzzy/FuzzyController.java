/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crossRoadsNextGen.fuzzy;

import fuzzy4j.flc.ControllerBuilder;
import fuzzy4j.flc.FLC;
import fuzzy4j.flc.InputInstance;
import fuzzy4j.flc.Term;
import fuzzy4j.flc.Variable;
import fuzzy4j.flc.defuzzify.Centroid;
import fuzzy4j.sets.FuzzyFunction;
import fuzzy4j.sets.GaussianFunction;
import fuzzy4j.sets.SigmoidFunction;
import java.util.Map;

/**
 *
 * @author juraj.sojcak
 */
public class FuzzyController implements IFuzzyController{

    // input variable
    private Variable p1;
    private Variable p2;
    private Variable countCars;
    private Variable travelTime;

    // output variable
    private Variable timeRule;
    private Variable priority;

    private FLC implTime;
    private FLC implPri;

    public FuzzyController() {

        // count cars fuzzy sets and terms
        SigmoidFunction zeroCars = new SigmoidFunction(-1, 3);
        FuzzyFunction smallCars = new GaussianFunction(5, 3);
        FuzzyFunction mediumCars = new GaussianFunction(10, 3);
        SigmoidFunction largeCars = new SigmoidFunction(1, 12);

        Term zeroCarsTerm = Term.term("zeroCars", zeroCars);
        Term smallCarsTerm = Term.term("smallCars", smallCars);
        Term mediumCarsTerm = Term.term("mediumCars", mediumCars);
        Term largeCarsTerm = Term.term("largeCars", largeCars);

        // waiting time of cars fuzzy sets and terms
        SigmoidFunction lowTime = new SigmoidFunction(-0.1, 100);
        FuzzyFunction mediumTime = new GaussianFunction(250, 100);
        SigmoidFunction highTime = new SigmoidFunction(0.05, 400);

        Term lowTimeTerm = Term.term("lowTime", lowTime);
        Term mediumTimeTerm = Term.term("mediumTime", mediumTime);
        Term highTimeTerm = Term.term("highTime", highTime);

        // priority fuzzy sets and terms
        SigmoidFunction low = new SigmoidFunction(-2, 2);
        FuzzyFunction medium = new GaussianFunction(3.5, 1.5);
        FuzzyFunction high = new GaussianFunction(6.5, 1.5);
        SigmoidFunction veryhigh = new SigmoidFunction(2, 8);
        
        Term lowTerm = Term.term("low", low);
        Term mediumTerm = Term.term("medium", medium);
        Term highTerm = Term.term("high", high);
        Term veryhighTerm = Term.term("veryhigh", veryhigh);
        
        // result time fuzzy sets and terms
        SigmoidFunction dec = new SigmoidFunction(-1, -4);
        FuzzyFunction decL = new GaussianFunction(-3, 2);
        FuzzyFunction zero = new GaussianFunction(0, 2);
        FuzzyFunction incL = new GaussianFunction(3, 2);
        SigmoidFunction inc = new SigmoidFunction(1, 4);

        Term decTerm = Term.term("dec", dec);
        Term decLTerm = Term.term("decL", decL);
        Term zeroTerm = Term.term("zero", zero);
        Term incLTerm = Term.term("incL", incL);
        Term incTerm = Term.term("inc", inc);


        // inference rules for fuzzying of count cars and travel time
        countCars = Variable.input("count_cars", zeroCarsTerm, smallCarsTerm, mediumCarsTerm, largeCarsTerm).start(zeroCars.support().min()).end(largeCars.support().max());
        travelTime = Variable.input("travel_time", lowTimeTerm, mediumTimeTerm, highTimeTerm).start(lowTime.support().min()).end(highTime.support().max());
        
        priority = Variable.output("priority", lowTerm, mediumTerm, highTerm, veryhighTerm).start(low.support().min()).end(veryhigh.support().max());
        
        implPri = ControllerBuilder.newBuilder()
                .when().var(countCars).is(zeroCarsTerm).and().var(travelTime).is(lowTimeTerm).then().var(priority).is(lowTerm)
                .when().var(countCars).is(smallCarsTerm).and().var(travelTime).is(lowTimeTerm).then().var(priority).is(mediumTerm)
                .when().var(countCars).is(mediumCarsTerm).and().var(travelTime).is(lowTimeTerm).then().var(priority).is(mediumTerm)
                .when().var(countCars).is(largeCarsTerm).and().var(travelTime).is(lowTimeTerm).then().var(priority).is(highTerm)
                .when().var(countCars).is(zeroCarsTerm).and().var(travelTime).is(mediumTimeTerm).then().var(priority).is(highTerm)
                .when().var(countCars).is(smallCarsTerm).and().var(travelTime).is(mediumTimeTerm).then().var(priority).is(highTerm)
                .when().var(countCars).is(mediumCarsTerm).and().var(travelTime).is(mediumTimeTerm).then().var(priority).is(veryhighTerm)
                .when().var(countCars).is(largeCarsTerm).and().var(travelTime).is(mediumTimeTerm).then().var(priority).is(veryhighTerm)
                .when().var(countCars).is(zeroCarsTerm).and().var(travelTime).is(highTimeTerm).then().var(priority).is(lowTerm)
                .when().var(countCars).is(smallCarsTerm).and().var(travelTime).is(highTimeTerm).then().var(priority).is(lowTerm)
                .when().var(countCars).is(mediumCarsTerm).and().var(travelTime).is(highTimeTerm).then().var(priority).is(mediumTerm)
                .when().var(countCars).is(largeCarsTerm).and().var(travelTime).is(highTimeTerm).then().var(priority).is(mediumTerm)
                .defuzzifier(new Centroid())
                .create();

        // inference rules for fuzzying of lanes/edges priorities 
        p1 = Variable.input("p1", lowTerm, mediumTerm, highTerm, veryhighTerm).start(low.support().min()).end(veryhigh.support().max());
        p2 = Variable.input("p2", lowTerm, mediumTerm, highTerm, veryhighTerm).start(low.support().min()).end(veryhigh.support().max());
        timeRule = Variable.output("time_rule", decTerm, decLTerm, zeroTerm, incLTerm, incTerm).start(dec.support().min()).end(inc.support().max());

        implTime = ControllerBuilder.newBuilder()
                .when().var(p1).is(lowTerm).and().var(p2).is(lowTerm).then().var(timeRule).is(decTerm)
                .when().var(p1).is(lowTerm).and().var(p2).is(mediumTerm).then().var(timeRule).is(decTerm)
                .when().var(p1).is(lowTerm).and().var(p2).is(highTerm).then().var(timeRule).is(decLTerm)
                .when().var(p1).is(lowTerm).and().var(p2).is(veryhighTerm).then().var(timeRule).is(incTerm)
                .when().var(p1).is(mediumTerm).and().var(p2).is(lowTerm).then().var(timeRule).is(decTerm)
                .when().var(p1).is(mediumTerm).and().var(p2).is(mediumTerm).then().var(timeRule).is(decLTerm)
                .when().var(p1).is(mediumTerm).and().var(p2).is(highTerm).then().var(timeRule).is(zeroTerm)
                .when().var(p1).is(mediumTerm).and().var(p2).is(veryhighTerm).then().var(timeRule).is(incTerm)
                .when().var(p1).is(highTerm).and().var(p2).is(lowTerm).then().var(timeRule).is(zeroTerm)
                .when().var(p1).is(highTerm).and().var(p2).is(mediumTerm).then().var(timeRule).is(incLTerm)
                .when().var(p1).is(highTerm).and().var(p2).is(highTerm).then().var(timeRule).is(incLTerm)
                .when().var(p1).is(highTerm).and().var(p2).is(veryhighTerm).then().var(timeRule).is(incTerm)
                .when().var(p1).is(veryhighTerm).and().var(p2).is(lowTerm).then().var(timeRule).is(incLTerm)
                .when().var(p1).is(veryhighTerm).and().var(p2).is(mediumTerm).then().var(timeRule).is(incTerm)
                .when().var(p1).is(veryhighTerm).and().var(p2).is(highTerm).then().var(timeRule).is(incTerm)
                .when().var(p1).is(veryhighTerm).and().var(p2).is(veryhighTerm).then().var(timeRule).is(incTerm)
                .defuzzifier(new Centroid())
                .create();
    }

    @Override
    public double InterferenceAndDefuzzyPriority(double count, double time) {
        InputInstance instance = new InputInstance().is(countCars, count).is(travelTime, time);
        Map<Variable, Double> defuzzifiedValue = implPri.apply(instance);
        return defuzzifiedValue.get(priority);
    }

    @Override
    public double InterferenceAndDefuzzyTime(double priority1, double priority2) {
        InputInstance instance = new InputInstance().is(p1, priority1).is(p2, priority2);
        Map<Variable, Double> defuzzifiedValue = implTime.apply(instance);

        return defuzzifiedValue.get(timeRule);
    }

}
