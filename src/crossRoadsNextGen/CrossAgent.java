/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package crossRoadsNextGen;

import jade.core.Agent;
import jade.core.behaviours.WakerBehaviour;

/**
 *
 * @author juraj.sojcak
 */
public class CrossAgent extends Agent{

    @Override
    protected void setup() {
        //Object args[] = getArguments();
        
        //System.out.println(args[0]);
        addBehaviour(new WakerBehaviour(this, 1000) {

            @Override
            protected void onWake() {
                System.out.println("AHOJ SVET");
            }
            
        });
        
    }
    
}
