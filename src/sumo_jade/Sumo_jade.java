/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package sumo_jade;

import de.tudresden.sumo.cmd.Simulation;
import de.tudresden.sumo.cmd.Trafficlight;
import de.tudresden.sumo.cmd.Vehicle;
import it.polito.appeal.traci.SumoTraciConnection;
import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.ws.container.SumoTLSProgram;
import de.tudresden.ws.container.SumoTLSPhase;

/**
 *
 * @author juraj.sojcak
 */
public class Sumo_jade {
    static String sumo_bin = "c:\\Program Files (x86)\\DLR\\Sumo\\bin\\sumo-gui.exe";
    static final String config_file = "c:\\Users\\juraj.sojcak\\Documents\\NetBeansProjects\\sumo_jade\\src\\sumo_jade\\simulation\\config.sumo.cfg";
    
    public static void main(String[] args) {
         //start Simulation
        SumoTraciConnection conn = new SumoTraciConnection(sumo_bin, config_file);
         
        //set some options
        conn.addOption("step-length", "0.1"); //timestep 100 ms
         
        try{
             
            //start TraCI
            conn.runServer();
 
            //load routes and initialize the simulation
            conn.do_timestep();
            SumoTLSProgram logic = new SumoTLSProgram("0", 0, 0, 0);
             
            //add phases
            logic.add(new SumoTLSPhase(4000,  "rrrrrrrrrr"));
            logic.add(new SumoTLSPhase(4000,  "GGGGGGGGGG"));
            
            conn.do_job_set(Trafficlight.setCompleteRedYellowGreenDefinition("gneJ1", logic));
            logic = new SumoTLSProgram("0", 0, 0, 0);
             
            //add phases
            logic.add(new SumoTLSPhase(4000,  "rrrrrrrrrrrrrr"));
            logic.add(new SumoTLSPhase(4000,  "GGGGGGGGGGGGGG"));
            
            conn.do_job_set(Trafficlight.setCompleteRedYellowGreenDefinition("gneJ3", logic));
            for(int i=0; i<36000; i++){
             
                                //current simulation time
                int simtime = (int) conn.do_job_get(Simulation.getCurrentTime());
               
                //conn.do_job_set(Trafficlight.set);
                conn.do_job_set(Vehicle.add("veh"+i, "car", "s1", simtime, 0, 13.8, (byte) 1));
                conn.do_job_set(Vehicle.add("veh-"+i, "car", "s2", simtime, 0, 13.8, (byte) 1));
                conn.do_timestep();
            }
             
            //stop TraCI
            conn.close();
             
        }catch(Exception ex){ex.printStackTrace();}
    }
    
}
