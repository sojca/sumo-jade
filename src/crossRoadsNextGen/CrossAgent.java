package crossRoadsNextGen;

import crossRoadsNextGen.behaviors.ListenerBehavior;
import jade.core.Agent;
import it.polito.appeal.traci.SumoTraciConnection;
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

    @Override
    protected void setup() {
        Object args[] = getArguments();

        if (args.length != 2) {
            System.err.println("ERROR: Incorrect number of arguments for CrossAgent!");
        }

        id = String.valueOf(args[0]);
        System.err.println(id);
        conn = (SumoTraciConnection) args[1];

        duration = 0;

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
}
