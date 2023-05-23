package arith;
import jade.core.AID;
import jade.core.Agent;
import jade.core.PlatformID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.util.*;
public class ReentrantCalculator extends Agent{
	private String DOUBLE="\\-?(\\d+|(\\d+)?\\.\\d+)([eE][\\-\\+]?\\d{1,3})?[DdFf]?";

	protected void setup(){
		addBehaviour( new RCBehaviour() ); //install behaviour object
	}//setup

	protected void takeDown(){
		System.out.println("Reentrant Calculator is taking down.");
	}//takeDown
	private class RCBehaviour extends CyclicBehaviour{
		public void action(){
			ACLMessage msg = myAgent.receive();
			if ( msg != null ) {
				if( msg.getPerformative()!=ACLMessage.REQUEST ){
					System.out.println("REQUEST expected.");
				}
				else if( msg.getContent().charAt(0)=='.' ){
					System.out.println("Calculator terminates. Bye"); myAgent.doDelete();
				}	
				else if( msg.getContent().matches(DOUBLE+"\\s+"+DOUBLE+"\\s+"+"[\\+\\-\\*/]") ){
					StringTokenizer st=new StringTokenizer(msg.getContent()," ");
					double a=Double.parseDouble(st.nextToken()), b=Double.parseDouble(st.nextToken());
					char op=st.nextToken().charAt(0);
					double ris=0;
					switch( op ){
						case '+': ris=a+b; break;
						case '-': ris=a-b; break;
						case '*': ris=a*b; break;
						default : ris=a/b; 
					}	System.out.println(""+a+op+b+"="+ris);
				}
				else 
					System.out.println("Sorry:"+msg.getContent()+" is not a valid command for calculator.");
			}	
			else { block(); 	}			
		}//action
	}//RCBehaviour
}//ReentrantCalculator

