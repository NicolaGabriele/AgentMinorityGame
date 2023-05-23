package arith;
import jade.content.lang.sl.SLCodec;
import jade.content.onto.basic.Action;
import jade.core.AID;
import jade.core.Agent;
import jade.core.PlatformID;
import jade.domain.mobility.MobileAgentDescription;
import jade.domain.mobility.MobilityOntology;
import jade.domain.mobility.MoveAction;
import jade.lang.acl.ACLMessage;


public class Mover extends Agent{
	protected void setup() {
		System.out.println("Setting up Mover ***");
		Object[] args=getArguments();
		if( args==null || args.length!=3 ) {
			System.out.println(" "+args.length+" "+"Wrong number of arguments. Mover gives up.");
			doDelete();
		}
		String agentName=(String)args[0];
		String destPlatform=(String)args[1];
		String destMTP=(String)args[2];

		AID aid=new AID( agentName, AID.ISLOCALNAME );
		AID remoteAMS = new AID("ams@"+destPlatform, AID.ISGUID);
		remoteAMS.addAddresses( destMTP );
		PlatformID dest = new PlatformID(remoteAMS);
		
		//register language and ontology
		getContentManager().registerLanguage(new SLCodec());
		getContentManager().registerOntology(MobilityOntology.getInstance());		
		
		MobileAgentDescription mad = new MobileAgentDescription();
		mad.setName(aid); //agent da migrare
		mad.setDestination(dest); //location destinazione
		MoveAction ma = new MoveAction();
		ma.setMobileAgentDescription(mad);
		sendRequest(new Action(aid, ma));
	}//setup
	
	void sendRequest(Action action) {
		ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
		request.setLanguage(new SLCodec().getName());
		request.setOntology(MobilityOntology.getInstance().getName());
		try {
			getContentManager().fillContent(request, action);
			request.addReceiver(action.getActor());
			send(request);
		}catch (Exception ex) { ex.printStackTrace(); }
	}//sendrequest
	
	protected void takeDown() {
		System.out.println("Mover "+getLocalName()+" is taking down. Bye.");
	}//takeDown
	
}//Mover
