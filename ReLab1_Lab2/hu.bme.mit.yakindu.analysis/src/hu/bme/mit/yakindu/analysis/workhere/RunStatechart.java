package hu.bme.mit.yakindu.analysis.workhere;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;
import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;
import hu.bme.mit.yakindu.analysis.RuntimeService;
import hu.bme.mit.yakindu.analysis.TimerService;


public class RunStatechart {
	
	public static void main(String[] args) throws IOException {
		ExampleStatemachine s = new ExampleStatemachine();
		s.setTimer(new TimerService());
		RuntimeService.getInstance().registerStatemachine(s, 200);
		s.init();
		s.enter();
		s.runCycle();
		
        BufferedReader reader = new BufferedReader( 
            new InputStreamReader(System.in)); 
        String line = reader.readLine();
        
        while((line.equals("exit")) == false) {
        	switch(line) {
			case ("start"):
				s.raiseStart();
				s.runCycle();
				break;
			case ("feher"):
				s.raiseFeher();
				s.runCycle();
				break;
			case ("fekete"):
				s.raiseFekete();
				s.runCycle();
				break;
			default:
				System.out.println("Invalid input");
			}
	print(s);
	line = reader.readLine();
     }
     print(s);
     System.exit(0);
        
	}
	public static void print(IExampleStatemachine s){
		System.out.println("W = " + s.getSCInterface().getWhiteTime());
		System.out.println("B = " + s.getSCInterface().getBlackTime());
	}
}