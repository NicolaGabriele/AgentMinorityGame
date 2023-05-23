package arith;
import jade.core.Agent;
public class Calculator extends Agent{
	protected void setup(){
		System.out.println("Simple calculator");
		Object []args=getArguments();
		if( args==null||args.length!=3 ){
			System.out.println("Arguments missing or wrong in number!"); doDelete();
		}
		else{
			double a=Double.parseDouble((String)args[0]);
			double b=Double.parseDouble((String)args[1]);
			char op=((String)args[2]).charAt(0);
			double ris=0; boolean ok=true;
			switch( op ){
				case '+': ris=a+b; break;
				case '-': ris=a-b; break;
				case '*': ris=a*b; break;
				case '/': ris=a/b; break;
				default: System.out.println("Unknown operator!"); ok=false;
			}
			if(ok) System.out.println(""+a+op+""+b+"="+ris); 
			doDelete();
		}
	}
	protected void takeDown(){ 
		System.out.println("Calculator is taking down! Bye!"); 
	}
}//Calculator

