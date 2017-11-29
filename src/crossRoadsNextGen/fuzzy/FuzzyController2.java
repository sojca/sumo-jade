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
    
    public double InterferenceAndDefuzzy(double countCarsFirst, double countCarsSecond) {
        countCarsFirst = (countCarsFirst > 22) ? 22 : countCarsFirst; 
        countCarsSecond = (countCarsSecond > 22) ? 22 : countCarsSecond; 
        
        fis.setVariable("main_road", countCarsFirst);
        fis.setVariable("second_road", countCarsSecond);
        fis.evaluate();

        Variable time = fis.getVariable("time");
//        JFuzzyChart.get().chart(time, time.getDefuzzifier(), true);
        
        return time.defuzzify();
    }
}
