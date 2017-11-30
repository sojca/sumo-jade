/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package crossRoadsNextGen.fuzzy;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;

/**
 *
 * @author juraj.sojcak
 */
public class FuzzyController2 {

    private FIS fis;
    public FuzzyController2() {
        System.out.println("asdasdadassdasdasdsadasdsada");
        String fileName = "src\\fcl\\crossroad.fcl";
        fis = FIS.load(fileName, true);
        
        if (fis == null) {
            System.err.println("Can't load file: '" + fileName + "'");
            return;
        }
    }
    
    public double InterferenceAndDefuzzy(double countCarsFirst) {
        countCarsFirst = (countCarsFirst > 15) ? 15 : countCarsFirst; 
        
        fis.setVariable("main_road", countCarsFirst);
        fis.evaluate();

        Variable time = fis.getVariable("time");
        
        return time.defuzzify();
    }
    
    public double InterferenceAndDefuzzyPriority(double countCars, double travelTime) {
        countCars = (countCars > fis.getVariable("count_cars").getUniverseMax()) ? 
                fis.getVariable("count_cars").getUniverseMax() : countCars; 
        
        travelTime = (travelTime > fis.getVariable("travel_time").getUniverseMax()) ? 
                fis.getVariable("travel_time").getUniverseMax() : travelTime; 
        
        fis.setVariable("count_cars", countCars);
        fis.setVariable("travel_time", travelTime);
        fis.evaluate();

        Variable priority = fis.getVariable("priority_out");
        
        return priority.defuzzify();
    }
    
    public double InterferenceAndDefuzzyTime(double priority1, double priority2) {
        
        fis.setVariable("priority_in1", priority1);
        fis.setVariable("priority_in2", priority2);
        fis.evaluate();

        Variable time = fis.getVariable("time");
        
        return time.defuzzify();
    }
}
