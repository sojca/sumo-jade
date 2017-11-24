/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package crossRoadsNextGen;


import de.tudresden.sumo.cmd.Simulation;
import de.tudresden.sumo.cmd.Trafficlight;
import de.tudresden.sumo.cmd.Vehicle;
import de.tudresden.ws.container.SumoStringList;
import it.polito.appeal.traci.SumoTraciConnection;
import jade.core.Agent;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

/**
 *
 * @author juraj.sojcak
 */
public class WorldAgent extends Agent{
    static String sumo_bin = "c:\\Program Files (x86)\\DLR\\Sumo\\bin\\sumo-gui.exe";
    static final String config_file = "src\\sumo_jade\\simulation\\config.sumo.cfg";
    
    @Override
    protected void setup() {
        AgentContainer c = getContainerController();
      
        SumoTraciConnection conn = new SumoTraciConnection(sumo_bin, config_file);
        conn.addOption("step-length", "0.1"); //timestep 100 ms

        try{

            conn.runServer();
            //load routes and initialize the simulation
            //Trafficlight.setPhase(sumo_bin, i)
                conn.do_timestep();
        
            SumoStringList trafficLights = (SumoStringList)conn.do_job_get(Trafficlight.getIDList());
            
            AgentController crossAgent;
            for(String id : trafficLights){
                crossAgent = c.createNewAgent("crossAgent" + id, 
                            "crossRoadsNextGen.CrossAgent", 
                            new Object[]{id});
                crossAgent.start();     
            }
            

            for(int i=0; i<36000; i++){
             
                                //current simulation time
                int simtime = (int) conn.do_job_get(Simulation.getCurrentTime());
                //System.out.println(conn.do_job_get(Lane.getWaitingTime("gneE5_1")) + " " +conn.do_job_get(Lane.getTraveltime("gneE5_1")));
                System.out.println(((SumoStringList)conn.do_job_get(Trafficlight.getIDList())).size());
                //conn.do_job_set(Trafficlight.set);
                conn.do_job_set(Vehicle.add("veh"+i, "car", "s1", simtime, 0, 13.8, (byte) 1));
                conn.do_job_set(Vehicle.add("veh-"+i, "car", "s2", simtime, 0, 13.8, (byte) 1));
                conn.do_job_set(Trafficlight.setPhase("gneJ3", 3));
                
                conn.do_timestep();
            }
             
            //stop TraCI
            conn.close();
            
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    
}
