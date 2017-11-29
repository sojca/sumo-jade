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

        // Error while loading?
        if (fis == null) {
            System.err.println("Can't load file: '" + fileName + "'");
            return;
        }
    }
    
    public double InterferenceAndDefuzzy(double countCarsFirst, double countCarsSecond) {

        fis.setVariable("main_road", countCarsFirst);
        fis.setVariable("second_road", countCarsSecond);
        fis.evaluate();

        Variable time = fis.getVariable("time");
//        JFuzzyChart.get().chart(time, time.getDefuzzifier(), true);
        
        return time.defuzzify();
    }
}
