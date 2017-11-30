/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package crossRoadsNextGen.fuzzy;

/**
 *
 * @author juraj.sojcak
 */
public interface IFuzzyController {
    
    public double InterferenceAndDefuzzyPriority(double count, double time);
    
    public double InterferenceAndDefuzzyTime(double p1, double p2);
}
