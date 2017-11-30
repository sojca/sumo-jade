package crossRoadsNextGen;

import crossRoadsNextGen.behaviors.ListenerBehavior;
import crossRoadsNextGen.fuzzy.FuzzyController;
import crossRoadsNextGen.fuzzy.FuzzyController2;
import crossRoadsNextGen.fuzzy.IFuzzyController;
import de.tudresden.sumo.cmd.Trafficlight;
import jade.core.Agent;
import it.polito.appeal.traci.SumoTraciConnection;
import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.domain.FIPANames;

/**
 * Agent implementation of single crossroad
 */
public class CrossAgent extends Agent {

    private String id;
    private SumoTraciConnection conn;
    private int duration;
    private IFuzzyController fuzzyController;
    private AID world;
    
    @Override
    protected void setup() {
        Object args[] = getArguments();

        if (args.length != 3) {
            System.err.println("ERROR: Incorrect number of arguments for CrossAgent!");
        }

        id = String.valueOf(args[0]);
        conn = (SumoTraciConnection) args[1];
        world = (AID) args[2];
        duration = 0;
        fuzzyController = new FuzzyController2();
        
        registerService();

        addBehaviour(new ListenerBehavior());
    }

    public void timestep(int duration) {
        this.duration++;
    }

    /**
     * Directory facilitator registration
     */
    private void registerService() {
        try {
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName(getAID());
            ServiceDescription sd = new ServiceDescription();
            sd.setName("magic crossroad");
            sd.setType("crossroad");
            sd.addLanguages(FIPANames.ContentLanguage.FIPA_SL);
            dfd.addServices(sd);
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    public SumoTraciConnection getConn() {
        return conn;
    }

    public void setConn(SumoTraciConnection conn) {
        this.conn = conn;
    }

    public String getId() {
        return id;
    }

    public int getDuration() {
        return duration;
    }

    public void resetDuration() {
        this.duration = 0;
    }

    public void incrementDuration() {
        this.duration++;
    }
    public IFuzzyController getFuzzyController(){
        return this.fuzzyController;
    }

    public AID getWorld() {
        return world;
    }
}
