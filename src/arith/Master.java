package arith;
import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class Master extends Agent{
	protected void setup(){
		System.out.println("Dynamic creation of a Calculator agent");
		Object[] args=getArguments();
		if( args==null || args.length!=3 ){
			System.out.println("Wrong number of arguments to Master which now shuts down.");
			doDelete();
		}
		String opnd1=(String)args[0], opnd2=(String)args[1], oper=(String)args[2];
		ContainerController cc=null;
		AgentController ac=null;
  		try{
  			 cc=getContainerController();
 			 ac=cc.createNewAgent( "c", "arith.Calculator", new Object[]{opnd1,opnd2,oper} ) ;
 		 	 ac.start();
 	 	}catch( Exception e ){
 		 	 System.out.println("Error during creation of calculator agent!");   
 	 	}
 	 	doDelete();
	}//setup
}//Master

