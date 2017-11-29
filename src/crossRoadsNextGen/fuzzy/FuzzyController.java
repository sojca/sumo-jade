/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package crossRoadsNextGen.fuzzy;

import fuzzy4j.aggregation.AlgebraicProduct;
import fuzzy4j.aggregation.AlgebraicSum;
import fuzzy4j.flc.ControllerBuilder;
import fuzzy4j.flc.FLC;
import fuzzy4j.flc.InputInstance;
import fuzzy4j.flc.Term;
import fuzzy4j.flc.Variable;
import fuzzy4j.flc.defuzzify.Bisector;
import fuzzy4j.flc.defuzzify.Centroid;
import fuzzy4j.sets.FuzzyFunction;
import fuzzy4j.sets.Line;
import fuzzy4j.sets.LinearFunction;
import fuzzy4j.sets.Point;
import fuzzy4j.sets.PointsLinearFunction;
import fuzzy4j.sets.TriangularFunction;
import fuzzy4j.util.SimpleInterval;
import java.util.Map;

/**
 *
 * @author juraj.sojcak
 */
public class FuzzyController {

    // input variable
    private Variable countCarsNS;
    private Variable countCarsWE;

    // output variable
    private Variable timeRule;
    private FLC impl;

    public FuzzyController() {
        FuzzyFunction zeroCountCars = new TriangularFunction(-5, 0, 5);
        FuzzyFunction smallCountCars = new TriangularFunction(0, 5, 10);
        FuzzyFunction mediumCountCars = new TriangularFunction(5, 10, 15);
        FuzzyFunction largeCountCars = new PointsLinearFunction(1.0, new Point(0, 0), new Point(10, 0), new Point(15, 1));

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

        countCarsNS = Variable.input("car_count_ns", zeroTerm, smallTerm, mediumTerm, largeTerm).start(0);
        countCarsWE = Variable.input("car_count_we", zeroTerm, smallTerm, mediumTerm, largeTerm).start(0);

        timeRule = Variable.output("time_rule", decTerm, zTerm, incTerm).start(-6).end(6);

        impl = ControllerBuilder.newBuilder()
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
                .activationFunction(AlgebraicProduct.INSTANCE)
                .accumulationFunction(AlgebraicSum.INSTANCE)
                .defuzzifier(new Bisector())
                .create();

        InputInstance instance = new InputInstance().is(countCarsNS, 20).is(countCarsWE, 0);
        Map<Variable, Double> defuzzifiedValue = impl.apply(instance);

        System.out.println("normal.fuzzy = " + defuzzifiedValue.get(timeRule) + " cars no.:" + 20 + " cars no.:" + 0);
        instance = new InputInstance().is(countCarsNS, 20).is(countCarsWE, 10);
        defuzzifiedValue = impl.apply(instance);
        System.out.println("normal.fuzzy = " + defuzzifiedValue.get(timeRule) + " cars no.:" + 20 + " cars no.:" + 5);
        instance = new InputInstance().is(countCarsNS, 20).is(countCarsWE, 5);
        defuzzifiedValue = impl.apply(instance);
        System.out.println("normal.fuzzy = " + defuzzifiedValue.get(timeRule) + " cars no.:" + 20 + " cars no.:" + 10);
        instance = new InputInstance().is(countCarsNS, 20).is(countCarsWE, 19);
        defuzzifiedValue = impl.apply(instance);

        System.out.println("normal.fuzzy = " + defuzzifiedValue.get(timeRule) + " cars no.:" + 20 + " cars no.:" + 19);
    }

    public double InterferenceAndDefuzzy(double countCarsFirst, double countCarsSecond) {

        InputInstance instance = new InputInstance().is(countCarsNS, countCarsFirst).is(countCarsWE, countCarsSecond);

        Map<Variable, Double> defuzzifiedValue = impl.apply(instance);
        //System.out.println("normal.fuzzy = " + crisp.get(timeRule) + " cars no.:" + (n + s) / 2);

        return defuzzifiedValue.get(timeRule);
    }

}
