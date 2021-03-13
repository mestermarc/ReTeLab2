package hu.bme.mit.yakindu.analysis.workhere;


import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.junit.Test;
import org.yakindu.sct.model.sgraph.State;
import org.yakindu.sct.model.sgraph.Statechart;
import org.yakindu.sct.model.sgraph.Transition;
import org.yakindu.sct.model.stext.stext.EventDefinition;
import org.yakindu.sct.model.stext.stext.VariableDefinition;

import hu.bme.mit.model2gml.Model2GML;
import hu.bme.mit.yakindu.analysis.modelmanager.ModelManager;

public class Main {
	@Test
	public void test() {
		main(new String[0]);
	}
	
	public static void main(String[] args) {
		ModelManager manager = new ModelManager();
		Model2GML model2gml = new Model2GML();
		boolean printed = false;
		// Loading model
		EObject root = manager.loadModel("model_input/example.sct");
		
		// Import rész:
		
		System.out.println("package hu.bme.mit.yakindu.analysis.workhere;\r\n" + 
				"\r\n" + 
				"import java.io.BufferedReader;\r\n" + 
				"import java.io.IOException;\r\n" + 
				"import java.io.InputStreamReader;\r\n" + 
				"\r\n" + 
				"import hu.bme.mit.yakindu.analysis.example.ExampleStatemachine;\r\n" + 
				"import hu.bme.mit.yakindu.analysis.example.IExampleStatemachine;\r\n" + 
				"import hu.bme.mit.yakindu.analysis.RuntimeService;\r\n" + 
				"import hu.bme.mit.yakindu.analysis.TimerService;\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"public class RunStatechart {\r\n" + 
				"	\r\n" + 
				"	public static void main(String[] args) throws IOException {\r\n" + 
				"		ExampleStatemachine s = new ExampleStatemachine();\r\n" + 
				"		s.setTimer(new TimerService());\r\n" + 
				"		RuntimeService.getInstance().registerStatemachine(s, 200);\r\n" + 
				"		s.init();\r\n" + 
				"		s.enter();\r\n" + 
				"		s.runCycle();\r\n" + 
				"		\r\n" +  
				"        BufferedReader reader = new BufferedReader( \r\n" + 
				"            new InputStreamReader(System.in)); \r\n" + 
				"        String line = reader.readLine();\r\n" + 
				"        \r\n" + 
				"        while((line.equals(\"exit\")) == false) {\r\n" + 
				"        	switch(line) {");
		
		
		// Reading model
		Statechart s = (Statechart) root;
		TreeIterator<EObject> iterator = s.eAllContents();
		while (iterator.hasNext()) {
			EObject content = iterator.next();
			if(content instanceof State) {
				State state = (State) content;
				//System.out.println(state.getName());
				
				//2. feladat:
				EList<Transition> next_state_trans = (state.getOutgoingTransitions());
				if (next_state_trans.isEmpty()) {
					//System.out.println(state.getName()+" is a trap state!");
				}else {
					next_state_trans.forEach((Transition temp)->{
						String target_name = temp.getTarget().getName();
						if (target_name==null) {
							String name_advice="from_"+state.getName()+"_"+temp.getSpecification();
							//System.out.println("Nincs neve egy állapotnak, a következő nevet javaslom:"+name_advice);
							temp.getTarget().setName(name_advice);
						}
						
						//System.out.println(state.getName()+" -> "+target_name);
					});
				}
				
				
				
				
			}
			//4. feladat:
								
			if(content instanceof EventDefinition) {
				
				EventDefinition ed = (EventDefinition) content;
				
				System.out.println("			case ("+"\""+ed.getName()+"\"):");
				String first_cap = ed.getName().substring(0,1).toUpperCase()+ed.getName().substring(1);
				
				System.out.println("				s.raise"+first_cap+"();");
				
				System.out.println("				s.runCycle();\r\n"+"				break;");
			}
			
			if(content instanceof VariableDefinition) {
				//State state = (State) content;
				
				if(! printed) {
					System.out.println("			default:\r\n"+
				"				System.out.println(\"Invalid input\");\r\n"+
							"			}\n"+
							"	print(s);\r\n"+
				"	line = reader.readLine();\r\n" + 
							"     }\r\n" + 
							"     print(s);\r\n" + 
							"     System.exit(0);\r\n" + 
							"        \r\n" + 
							"	}");
					System.out.println("	public static void print(IExampleStatemachine s){");
					printed=true;
				}
				VariableDefinition vd = (VariableDefinition) content;
				
				//System.out.println("Változó: \n"+vd.getName());
				char tmp = vd.getName().toUpperCase().charAt(0);
				String first_cap = vd.getName().substring(0,1).toUpperCase()+vd.getName().substring(1);
				System.out.println("		System.out.println(\""+tmp+" = \" + s.getSCInterface().get"+first_cap+"())");
			}
				
		}

		System.out.println("	}");
		System.out.println("}");
		// Transforming the model into a graph representation
		String content = model2gml.transform(root);
		// and saving it
		manager.saveFile("model_output/graph.gml", content);
	}
}
