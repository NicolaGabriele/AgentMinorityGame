package minoritiGame;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;


public class Manager extends Agent{


    protected void setup(){
        System.out.println("Manager is starting with M = "+Utility.M);
        System.out.println("creating all simple agents...");
        for(int i = 0; i < Utility.N; i++){
            ContainerController cc = null;
            AgentController ac = null;
            try{
                cc = getContainerController();
                ac = cc.createNewAgent(String.format("Agent%d",i),"minoritiGame.SimpleAgent",new Integer[]{i});
                ac.start();
            }catch(Exception e){
                System.out.println("error during agent creation");
            }
        }
        addBehaviour(new ManagerBehaviour(new Observer()));
        System.out.println("Manager say: ManagerBehaviour added");

    }//setup

    @Override
    protected void takeDown() {
        System.out.println("Manager says: Manager take down, bye");
    }

    private class ManagerBehaviour extends CyclicBehaviour{

        private int timeStep = 0;
        private Observer observer;
        public ManagerBehaviour(Observer observer){
            this.observer = observer;
        }

        @Override
        public void action() {
            if(timeStep == Utility.TIME_LIMIT){
                System.out.println("Manager says: Time limit reached");
                ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                for(int i = 0; i<Utility.N; i++)
                    msg.addReceiver(new AID(String.format("Agent%d", i), AID.ISLOCALNAME));
                msg.setContent("end");
                myAgent.send(msg);
                System.out.println("manager terminates");
                observer.printA();
                System.out.println("FS: "+observer.FS()+"\n"+
                                    "Nsc: "+observer.Nsc()+"\n"+"NUM: "+SimpleAgent.NUM);
                myAgent.doDelete();
            }


            //Time Limit doesn't reached

            //sending a request to all agents to take a decision
            ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
            for(int i = 0; i<Utility.N; i++)
                msg.addReceiver(new AID(String.format("Agent%d", i), AID.ISLOCALNAME));
            msg.setContent("please, take a decision");
            myAgent.send(msg);

            //read responses
            int numOption1 = 0, numOption2 = 0;
            while( numOption1 + numOption2 < Utility.N){
                MessageTemplate template = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
                ACLMessage response = myAgent.receive(template);
                if(response != null){
                    int choice = Integer.parseInt(response.getContent());
                    if (choice == 0)numOption1++;
                    else numOption2++;
                }else{ block();}
            }
            observer.notifyA(timeStep, numOption1);
            observer.updateFS(numOption1,numOption2);
            int Nsc = 0;
            for(boolean b: Utility.haveChanged)
                if(b)Nsc++;

            observer.updateNsc(Nsc);
            //send result to agents
            ACLMessage result = new ACLMessage(ACLMessage.INFORM);
            for(int i = 0; i<Utility.N; i++)
                result.addReceiver(new AID(String.format("Agent%d", i), AID.ISLOCALNAME));
            if(numOption1>numOption2)
                result.setContent(String.valueOf(1));
            else
                result.setContent(String.valueOf(0));
            myAgent.send(result);

            //update time step
            timeStep++;
        }//action

    }//CyclicBehaviour

}//Manager
