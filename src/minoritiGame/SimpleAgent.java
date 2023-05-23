package minoritiGame;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Arrays;

public class SimpleAgent extends Agent {

    public static int NUM = 0;
    private int identifier, updateIndex=0, lastStrategy = 0;
    private int[] memory = new int[Utility.M];

    private int[][] spool = new int[Utility.S][(int)Math.pow(2,Utility.M)];;

    private int[] vs = new int[Utility.S]; // virtual score for strategies

    private int rs = 0; //real score for player


    private static Object o = new Object();

    public void setup(){
        synchronized (o){
            NUM++;
        }
        this.identifier = (Integer) getArguments()[0];
        //random inizializzation of memory
        for(int i = 0; i<Utility.M; i++)
            memory[i] = (Math.random()<0.5)?1:0;
        //random initialization of pool strategies
        for( int s=0; s<Utility.S; ++s )
            for( int t=0; t<spool[s].length; ++t )
                spool[s][t]=(Math.random()<0.5)?1:0;
        //System.out.println("Agent "+identifier+" start");
        addBehaviour(new AgentBehaviour());
    }//setup

    private class AgentBehaviour extends CyclicBehaviour{

        @Override
        public void action() {
            MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
            ACLMessage msg = myAgent.receive(template);
            if(msg != null){
               // System.out.println(String.format("Agent %d recived a message",identifier));
                String content = msg.getContent();
                if(content.equals("end")){
                    //System.out.println("Agent "+identifier+" ends, bye");
                    myAgent.doDelete();
                }else{

                    //send response
                    int strategy = 0;
                    int max = vs[strategy];
                    for(int j = 0; j<vs.length; j++)
                        if(max<vs[j]) {
                            strategy = j;
                            max = vs[j];
                        }

                    if(lastStrategy != strategy)
                        Utility.haveChanged[identifier] = true;
                    else Utility.haveChanged[identifier] = false;

                    int choice = spool[strategy][mem()];
                    lastStrategy = strategy;
                    ACLMessage response = new ACLMessage(ACLMessage.INFORM);
                    response.addReceiver(new AID("manager",AID.ISLOCALNAME));
                    response.setContent(String.valueOf(choice));
                    myAgent.send(response);

                    //recive result
                    ACLMessage result = myAgent.receive(MessageTemplate.MatchPerformative(ACLMessage.INFORM));
                    if(result != null){
                        int updateValue = Integer.parseInt(result.getContent());
                        for(int k = memory.length-1; k>0; k--)
                            memory[k] = memory[k-1];
                        memory[0] = updateValue;
                        if(choice == updateValue) {
                            vs[strategy]++;rs++;
                        }
                        else vs[strategy]--;
                    }else block();
                }// dont end
                //System.out.println("Agent"+identifier+" go away");
            }else block();
        }//action

        public int mem(){
            int v=0, p;
            for( int i=Utility.M-1; i>=0; --i ){ //for all bits of m, from M-1 (least significant bit) to 0 (most significant bit)
                p=(int)Math.pow(2,(Utility.M-1)-i);
                v=v+p*memory[i];
            }
            return v;
        }
    }//AgentBehaviour
}//SimpleAgent
