package test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.management.InstanceAlreadyExistsException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import field.Field;
import field.FusionConnection;
import field.SimpleConnection;
import field.VHDLGeneric;
import field.VHDLPort;
import field.VHDLSignal;
import hilecopComponent.BehaviourField;
import hilecopComponent.ComponentDescriptor;
import hilecopComponent.Connection;
import hilecopComponent.Constant;
import hilecopComponent.Generic;
import hilecopComponent.HilecopComponentDesignFile;
import hilecopComponent.PNAction;
import hilecopComponent.PNCondition;
import hilecopComponent.PNEntity;
import hilecopComponent.PNFunction;
import hilecopComponent.PNInterpretation;
import hilecopComponent.PNTime;
import hilecopComponent.PetriNetComponentBehaviour;
import hilecopComponent.Place;
import hilecopComponent.Port;
import hilecopComponent.RefPlace;
import hilecopComponent.RefTransition;
import hilecopComponent.Signal;
import hilecopComponent.Transition;
import root.ComponentInstance;
import root.HilecopComponent;
import root.HilecopRoot;
import root.InstanceHistory;
import script.VHDLAction;
import script.VHDLCondition;
import script.VHDLElement;
import script.VHDLTime;

public class Test {
	ArrayList<String> ErreurList;
	//private static int nbInstance=0;
	
	public Test()
	{
		ErreurList=new ArrayList<String>();
//		System.out.println("\u001B[31m"+"i m red"+"\u001B[0m");
	}
	
	public HilecopRoot open(String path)
	{
		System.out.println("Open "+path);
		ResourceSet ancienResourceSet = new ResourceSetImpl();
		ancienResourceSet.getPackageRegistry().put(root.RootPackage.eNS_URI,root.RootPackage.eINSTANCE);
		File fic = new File(path);
System.out.println(fic);		
		URI ancienfileURI = URI.createFileURI(path);
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("root",new XMIResourceFactoryImpl());
		Resource res2 = ancienResourceSet.getResource(ancienfileURI, true);
		HilecopRoot designfile = (HilecopRoot) res2.getContents().get(0);
		return designfile;
	}
	
	public HilecopComponentDesignFile read(String path){
		ResourceSet ancienResourceSet = new ResourceSetImpl();
		ancienResourceSet.getPackageRegistry().put(hilecopComponent.HilecopComponentPackage.eNS_URI,hilecopComponent.HilecopComponentPackage.eINSTANCE);
		URI ancienfileURI = URI.createFileURI(path);
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("hilecopcomponent",new XMIResourceFactoryImpl());
		Resource res2 = ancienResourceSet.getResource(ancienfileURI, true);
		HilecopComponentDesignFile designfile = (HilecopComponentDesignFile) res2.getContents().get(0);
		return designfile;
	}
	
	
	public void TestField(HilecopRoot root, HilecopComponentDesignFile designfile)
	{

		HilecopComponent newHelicopComponent=root.getComponent();
	//RootFactory.eINSTANCE.createHilecopRoot();
	int cFieldsSize=newHelicopComponent.getFields().size();
	//size=getpublic+getprivate
	int designfileFieldsSize=designfile.getHilecopComponent().getPublicFields().size() + designfile.getHilecopComponent().getComponentBehaviour().getPrivateFields().size();
	ComponentDescriptor anciennComponent=designfile.getHilecopComponent();
	int i=0;
	// nb component???
	ErreurList.add("\n\t**********************************************************\n");
	ErreurList.add("\nComponent  "+newHelicopComponent.getName()+"\n");
	ErreurList.add("\tTestField\n");
	if(cFieldsSize != designfileFieldsSize)
	{
		System.out.println("nb of fields r nt matching");
		ErreurList.add("nb of fields r nt matching : new : "+cFieldsSize+" old : "+designfileFieldsSize+"\n");
	}
		System.out.println(cFieldsSize);
		System.out.println(designfileFieldsSize);
		while(i<cFieldsSize || i<designfileFieldsSize)
		{
			Field f=newHelicopComponent.getFields().get(i);
		
			if(f instanceof VHDLPort)
			{	
				VHDLPort port = (VHDLPort) f;
				Port oport=PortMatchbyName(anciennComponent, port.getName());
				
				if(oport==null){
					System.out.println("portname isnt found !!");
					ErreurList.add("port name isnt found !! \n");
				}
				else{
				if(port.getName().equals(oport.getName())){System.out.println(port.getName()+"port checked");} 
				else{
					System.out.println("portname is mismatched !!");
					ErreurList.add("\u001B[31m"+"i m red"+"\u001B[0m"+"portname is mismatched !!\n");
				}
				
				if(port.getType().equals(oport.getType())){}
				else{ 
					System.out.println("NP :"+port.getType());
					System.out.println("OP :"+oport.getType());
					System.out.println("port type is mismatched !!");
					ErreurList.add("port type is mismatched for :"+port.getName()+"\n");
				}
				if(TestPortMode(oport,port)){ System.out.println("ok");}
				else { 
					System.out.println("portMode type is mismatched");
					ErreurList.add("port type is mismatched\n");
					}
				
				if( port.getDefaultValue().trim().equals(oport.getDefaultValue().trim())){} else{
					System.out.println("port default value is mismatched");
					ErreurList.add("port default Value is mismateched : "+port.getName()+ " :"+port.getDefaultValue()+" old :"+oport.getDefaultValue()+"\n");
				}
				if( port.getInputConnections().size() == oport.getInputConnections().size()){} else{
					System.out.println("port Input connection size is mismatched");
					ErreurList.add("port Input connection size is mismatched \n");
				}
				if( port.getOutputConnections().size() == oport.getOutputConnections().size()){} else{
					System.out.println("port Output connection size is mismatched");
					ErreurList.add("port Output connection size is mismatched \n");
				}
			}}
			
			else if( f instanceof VHDLSignal )
			{
				
				VHDLSignal sig=(VHDLSignal) f;
				Signal s=SignalMatchbyName(anciennComponent, sig.getName());
				
				if(s==null){
					ErreurList.add("Signal not found\n");
				}
				else{
					if( sig.getName().equals(s.getName())){} else{ 
						System.out.println("signal name is mismatched");
						ErreurList.add( "signal name is mismatched\n");
					}
					if( sig.getType().equals(s.getType())){} else {
						System.out.println("signal type is mismatched");
						ErreurList.add("signal type is mismatched\n");
					}
					if(sig.getDefaultValue().equals(s.getDefaultValue())) {} else {
						System.out.println("signal default value is mismatched");
						ErreurList.add("signal default value is mismatched \n");
						}
					if( sig.getInputConnections().size() == s.getInputConnections().size()){} else{
						System.out.println("port Input connection size is mismatched");
						ErreurList.add("signal Input connection size is mismatched \n");
					}
					if( sig.getOutputConnections().size() == s.getOutputConnections().size()){} else{
						System.out.println("port Output connection size is mismatched");
						ErreurList.add("signal Output connection size is mismatched \n");
					}
				}
				
				//assert s.getInputConnections() == sig.getInputConnections():" "
				
			}
			
			
			else if(f instanceof Constant)
			{Constant c=(Constant) f;
				TestConstant(c,anciennComponent);
			}
			
			
			
			else if( f instanceof VHDLGeneric)
			{	
				
				VHDLGeneric g= (VHDLGeneric) f;
				Generic gen=GenericMatchbyName(anciennComponent, g.getName());
				if(gen==null)
				{
					System.out.println("generic nt found");
					ErreurList.add("generic nt found");
				}
				else{
				if( g.getName().equals( gen.getName())){} else{
					System.out.println("genereic Name is mismatched");
					ErreurList.add("genereic Name is mismatched");
				}
				if (g.getType().equals(gen.getType())) {} else {
					System.out.println("generic Type is mismatched");
					ErreurList.add("generic Type is mismatched");
				}
				if(g.getDefaultValue().equals(gen.getDefaultValue())){}  else
					{System.out.println("generic DefaultValue is mismatched");
					ErreurList.add( "generic DefaultValue is mismatched");
					}
				//??assert g.getInputConnections() == (Connection)gen.getInputConnections();
				}
			}
			
			
			else if( f instanceof petriNet.RefPlace)
			{
				petriNet.RefPlace rpl=(petriNet.RefPlace) f;
				RefPlace rp=RefPlaceMatchbyName(anciennComponent, f.getName());
				if(rp==null)
				{
					System.out.println("refplace is not found");
					ErreurList.add("refplace is not found");
				}
				else{
				if (rpl.getName().equals( rp.getName())){System.out.println(rpl.getName()+" checked"); }
				else 
				{
					System.out.println("RefPlace name is mismatching");
					ErreurList.add("RefPlace name is mismatching");
				}
					
				if(rpl.getPlace().getName().equals(rp.getPlace().getName())){}else
					{
					System.out.println("refPlace place is mismatching");
					ErreurList.add("refPlace place is mismatching");
					}
				if(rpl.getInputArcs().size()==rp.getInputArcs().size()){} else
					{
					System.out.println("refPlace inputArcs is mismatching");
					ErreurList.add( "refPlace inputArcs is mismatching");
					}
				if(rpl.getOutputArcs().size() == rp.getOutputArcs().size()){System.out.println("input arc ok");} else
					{System.out.println("refplace outputArcs is mismatching");
					ErreurList.add("refplace outputArcs is mismatching");
					}
//				type???
//				rp.get
				if(rpl.getMode().getValue() == rp.getMode().getValue()) {} else{
					System.out.println("refplace Mode is mismatching");
					ErreurList.add( "refplace Mode is mismatching");
				}
				}
			}
			
			else if(f instanceof petriNet.RefTransition)
			{
				
				petriNet.RefTransition rtr=(petriNet.RefTransition) f;
				RefTransition rt=RefTransitionMatchbyName(anciennComponent, rtr.getName());
				if(rt==null)
				{
					System.out.println("RefTransition is not found");
					ErreurList.add("RefTransition is not found");
				}
				else{
				if( rtr.getName().equals(rt.getName())){} else{
					System.out.println("RefTransition Name is Mismatched");
					ErreurList.add("RefTransition Name is Mismatched");
				}
				if( rtr.getInputArcs().size()== rt.getInputArcs().size()) {} else
					{System.out.println("RefTransition InputArcs r mismatched");
					ErreurList.add("RefTransition InputArcs r mismatched ");
					}
				if(rtr.getOutputArcs().size() == rt.getOutputArcs().size()) {} else
					{System.out.println("RefTransition InputArcs r mismatched ");
					ErreurList.add("RefTransition InputArcs r mismatched ");
					}
				if(rtr.getMode().getValue()== rt.getMode().getValue()){} else{
					System.out.println("RefTransition Mode is mismatched");
					ErreurList.add("RefTransition Mode is mismatched ");
				}
				}
			
			}
			i++;	
		}
	}
	
	
	public boolean  TestPortMode(Port ancienPort, VHDLPort nouveauPort)
	{
		if(ancienPort.getMode().equals(ancienPort.getMode().OUT) && nouveauPort.getMode().equals(nouveauPort.getMode().OUT))
		{
			return true;
		}
		else if(ancienPort.getMode().equals(ancienPort.getMode().IN) && nouveauPort.getMode().equals(nouveauPort.getMode().IN))
		{
			return true;
		}
		else if(ancienPort.getMode().equals(ancienPort.getMode().INOUT) && nouveauPort.getMode().equals(nouveauPort.getMode().INOUT))
		{
			return true;
		}
		
			return false;
	}
	
	
	public boolean TestConstant(Constant newConstant, ComponentDescriptor oldc)
	{ int PrivateFieldssize=oldc.getComponentBehaviour().getPrivateFields().size();
		
		int i=0;
		while(i<PrivateFieldssize)
		{BehaviourField bf=oldc.getComponentBehaviour().getPrivateFields().get(i);
			//Field f=newc.getFields().get(i);
		if(bf instanceof Constant )
			{Constant oc= (Constant) bf;
			Constant nc= ConstantMatchbyName(oldc, newConstant.getName());
				if(oc.equals(nc))
				{
				if( oc.getName()== newConstant.getName()){} else {
					System.out.println("ConstantName mismatched ");
					ErreurList.add("ConstantName mismatched ");
				}
				if( oc.getType()==newConstant.getType()){} else 
					{System.out.println("ConstantType mismatched ");
					//ErreurList.add("ConstantType mismatched  ");
					}
				if(oc.getDefaultValue()==newConstant.getDefaultValue()) {} else
					{
					System.out.println("ConstantDefaultValue mismatched");
					//ErreurList.add("ConstantDefaultValue mismatched ");
					}
					return true;
				}i++;
			}
			
		
		}
	
		return false;
	}
	
	
	public void TestPNStructure(HilecopRoot root, HilecopComponentDesignFile designfile){
		int pnobject=root.getComponent().getPNStructureObjects().size();
		
		PetriNetComponentBehaviour pcb=(PetriNetComponentBehaviour) designfile.getHilecopComponent().getComponentBehaviour();
	int oldPnobject=	pcb.getPNStructureObjects().size();
	ErreurList.add("\tTestPNStructure\n");
	
	if(pnobject != oldPnobject)
	{
		System.out.println("nb of PNElements r nt matching");
		ErreurList.add("nb of PNElements r nt matching : new : "+pnobject+" old : "+oldPnobject+"\n");
	}
	
	
	int i=0;
	while(i<pnobject && i<oldPnobject)
	{
		PNEntity oldentity=pcb.getPNStructureObjects().get(i);
		System.out.println("test pnobject name: "+oldentity.getName()+"\n");
		//ErreurList.add("test pnobject name: "+oldentity.getName()+"\n");
		if(oldentity instanceof Place)
		{
			Place place= (Place) oldentity;
			petriNet.Place p= PlaceMatchbyName(root.getComponent().getPNStructureObjects(), place.getName());
		
			System.out.println("\t test place: "+place.getName());
		//	ErreurList.add("\t test place: "+place.getName()+"\n");
			
			if(p==null)
			{
				System.out.println("place not found "+place.getName());
				ErreurList.add("place not found "+place.getName()+"\n");
			}
			else
			{
				if(place.getMarkupExpression().equals(""+p.getMarking()))
				{
					//???
				}
				else {
					ErreurList.add("\t test Markup Expression place: "+place.getName()+" "+p.getMarking()+" old :"+place.getMarkupExpression()+"\n");
				}
				
				if(place.getOutputArcs().size()== p.getOutputArcs().size())
				{
					System.out.println("outputarc in pnentity checked");
				}
				else
				{
					System.out.println("number of output arc error");
					ErreurList.add("Place : "+place.getName()+" number of output arc error: "+" new output arc size "+p.getOutputArcs().size()+" old output arc size "+place.getOutputArcs().size()+"\n");
				}
				
				
				if(place.getInputArcs().size()== p.getInputArcs().size())
				{
					System.out.println("inputarc in PNEntity is checked");
				}
				else
				{
					System.out.println("number of input arc error");
					ErreurList.add("Place : "+place.getName()+" number of input arc error: "+" new input arc size "+p.getInputArcs().size()+" old input arc size "+place.getInputArcs().size()+"\n");
				}
				
				
			
			}// end instanceof place
			
			if(oldentity instanceof Transition)
			{
				Transition oldtransition=(Transition) oldentity;
				petriNet.Transition transition=TransitionMatchbyName(root.getComponent().getPNStructureObjects(), oldtransition.getName());
				
				if(transition== null)
				{
					System.out.println("Transition Name is not found");
					ErreurList.add("Transition Name is not found\n");
				}
				else
				{
					if(oldtransition.getInputArcs().size()==transition.getInputArcs().size())
					{
						System.out.println("oldtransition is ok");
					}
					else
					{
						System.out.println("number of inputarcs size is mismatched");
						ErreurList.add("number of inputarcs size is mismatched "+"old input arc size "+oldtransition.getInputArcs().size()+" new input arc size "+transition.getInputArcs().size()+"\n");
					}
					if(oldtransition.getOutputArcs().size()==transition.getOutputArcs().size())
					{
						System.out.println("oldtransition is ok");
					}
					else
					{
						System.out.println("number of outputarcs size is mismatched");
						ErreurList.add("number of outputarcs size is mismatched "+"old output arc size "+oldtransition.getOutputArcs().size()+" new Output arc size "+transition.getOutputArcs().size()+"\n");
					}
				}
			}//end instanceof transition
			//TODO arc
		}
		i++;
	}
	}
	
	public void TestConnection(HilecopRoot root, HilecopComponentDesignFile designfile) {
		// TODO Auto-generated method stub
		//Connections TODO 
		//root.getComponent().getConnections()
		//designfile.getHilecopComponent().getComponentBehaviour().getConnections()
		// Taille ok + connection.inputFiedl.name + connection.output.name
		// if connection instanceof SimpleConnection alors tester connection.sourceSelectionExpression et connection.targetSelectionExpression
		
		ErreurList.add("\tTestConnection\n");
		
		int connectionSizeNew=root.getComponent().getConnections().size();
		int connectionSizeOld = 0;
		for(Connection c: designfile.getHilecopComponent().getComponentBehaviour().getConnections()) {
			if(c instanceof hilecopComponent.SimpleConnection) {
				hilecopComponent.SimpleConnection sc = (hilecopComponent.SimpleConnection)c;
				if(sc.getSourceField() != null && sc.getTargetField() != null) {
					connectionSizeOld++;
				}
			}
				else if(c instanceof hilecopComponent.FusionConnection) {
					hilecopComponent.FusionConnection fc = (hilecopComponent.FusionConnection)c;
					if(fc.getSourceField() != null && fc.getTargetField() != null) {
						connectionSizeOld++;
					}
				}
		}
	
		
		if(connectionSizeNew!=connectionSizeOld)
		{
			System.out.println("number of connections is mismatched");
			ErreurList.add("\t\t* number of connections is mismatched: new "+connectionSizeNew+" old "+connectionSizeOld+"\n");
		}
		
		int i=0;
		while(i<connectionSizeNew && i<connectionSizeOld)
		{
			
			field.Connection fConnection=root.getComponent().getConnections().get(i);
			

			if(fConnection instanceof SimpleConnection)
			{SimpleConnection sc=(SimpleConnection) fConnection;
				hilecopComponent.SimpleConnection hConnection =ConnectionMatchByName(designfile.getHilecopComponent().getComponentBehaviour().getConnections(), fConnection.getName());
				if(hConnection==null)
				{
					System.out.println("SimpleConnection is not found");
					ErreurList.add("\t\t* SimpleConnection is not found "+fConnection.getName()+"\n");
				}
				else
				{
					if(sc.getSourceSelectionExpression() == null || sc.getSourceSelectionExpression().equals(hConnection.getSourceSelectionExpression())){}
					else
					{
						System.out.println("SourceSelectionExpression is mismatched");
						ErreurList.add("\t\t* SourceSelectionExpression is mismatched : new SourceSelectionExpression "+sc.getSourceSelectionExpression()+" old SourceSelectionExpression "+hConnection.getSourceSelectionExpression()+"\n");
					}
					
					
					if( sc.getTargetSelectionExpression() == null || sc.getTargetSelectionExpression().equals(hConnection.getTargetSelectionExpression())){}
					else
					{
						System.out.println("TargetSelectionExpression is mismatched");
						ErreurList.add("\t\t* TargetSelectionExpression is mismatched : new TargetSelectionExpression "+sc.getTargetSelectionExpression()+" old TargetSelectionExpression "+hConnection.getTargetSelectionExpression()+"\n");
					}
					if(sc.getInputField().getName().equals(hConnection.getSourceField().getName()))
							{
								System.out.println("simpleConnection inputfield is checked");
							}
					else
					{
						System.out.println("InputField is mismatched");
						ErreurList.add("InputField is mismatched : new InputField "+sc.getInputField()+" old SourceField "+hConnection.getSourceField()+"\n");
					}
					
					if(sc.getOutputField().getName().equals(hConnection.getTargetField().getName())){System.out.println("simpleConnection Outputfield is checked");}
					else
					{
						System.out.println("OutputField in simpleConnection is mismatched");
						ErreurList.add("OutputField in simpleConnection is mismatched : new OutputField "+sc.getOutputField()+" old TargetField "+hConnection.getTargetField()+"\n");
					}
			
				}
				if(fConnection instanceof FusionConnection)
				{
					FusionConnection fc=(FusionConnection) fConnection;
					hilecopComponent.FusionConnection hFusionConnection=FusionConnectionMatchByName(designfile.getHilecopComponent().getComponentBehaviour().getConnections(), fConnection.getName());
					if(hFusionConnection==null)
					{
						System.out.println("fusionConnection is not found!");
						ErreurList.add(fConnection.getName()+" is fusionConnection is not found! ");
					}
					else
					{
						if(fc.getInputField().getName().equals(hFusionConnection.getSourceField().getName())){}
						else{
							System.out.println("InputField in FusionConnection is mismatched");
							ErreurList.add("InputField in FusionConnection is mismatched : new InputField "+fc.getInputField()+" old SourceField "+hFusionConnection.getSourceField()+"\n");
							
						}
						
				
						if(fc.getOutputField().getName().equals(hFusionConnection.getTargetField().getName())){}
						else
						{
							System.out.println("ouputField in FusionConnection is mismatched");
							ErreurList.add("ouputField in FusionConnection is mismatched : new ouputField "+fc.getOutputField()+" old TargetField "+hFusionConnection.getTargetField()+"\n");
						}
					}
				}
	
			}
			
			i++;
		}
		
		
		
		
	}
	
	private hilecopComponent.FusionConnection FusionConnectionMatchByName(EList<Connection> connections, String name) {
		// TODO Auto-generated method stub
		hilecopComponent.FusionConnection resFusionConnection=null;
		for(hilecopComponent.Connection fc:connections)
		{
			if(fc instanceof hilecopComponent.FusionConnection)
			{
				hilecopComponent.FusionConnection fusion=(hilecopComponent.FusionConnection) fc;
				
				if(name.equals("fusionconnection_"+fusion.getId()))
				{
					resFusionConnection=fusion;
					return resFusionConnection;
				}
			}
		}		
		return resFusionConnection;
	}

	private hilecopComponent.SimpleConnection ConnectionMatchByName(EList<hilecopComponent.Connection> connections, String name) {
		// TODO Auto-generated method stub
		hilecopComponent.SimpleConnection resConnection=null;
		
		for(hilecopComponent.Connection c: connections)
		{if(c instanceof hilecopComponent.SimpleConnection)
		{hilecopComponent.SimpleConnection sc=(hilecopComponent.SimpleConnection) c;
			if(name.equals("simpleconnection_"+c.getId()))
			{
				resConnection=sc;
				return resConnection;
			}
		}
		}
		
		return resConnection;
	}

	public void TestVHDLElement(HilecopRoot root, HilecopComponentDesignFile designfile)
	{PetriNetComponentBehaviour pcb=(PetriNetComponentBehaviour) designfile.getHilecopComponent().getComponentBehaviour();
	EList<VHDLElement> listeVHDL = root.getComponent().getVHDLElements();
	//ArrayList<VHDLAction> listeAction = new ArrayList<VHDLAction>();
	int listeVHDLSize=listeVHDL.size();
	int pcbSize=pcb.getInterpretation().getActions().size();
	pcbSize+=pcb.getInterpretation().getConditions().size();
	pcbSize+=pcb.getInterpretation().getFunctions().size();
	pcbSize+=pcb.getInterpretation().getTimes().size();
	int i=0;
	System.out.println("VHDLElement : "+listeVHDLSize+" = "+pcbSize);
	ErreurList.add("\tTestVHDLElement\n");
	
	if(listeVHDLSize != pcbSize)
	{
		System.out.println("nb of VHDLElements r nt matching");
		ErreurList.add("nb of VHDLElements r nt matching : new : "+listeVHDLSize+" old : "+pcbSize+"\n");
	}
	
	while(i<listeVHDLSize && i<pcbSize)
	{
		VHDLElement e =  root.getComponent().getVHDLElements().get(i);
		System.out.println("Test VHDLElement "+e.getName()+"\n");
		//ErreurList.add("Test VHDLElement "+e.getName()+"\n");
		
		//VHDLAction + VHDLCondition + VHDLFonction + VHDLTime
		// name + content? + Associated_action/condition/etc
		
		if(e instanceof VHDLAction){
			VHDLAction action=(VHDLAction)e;
			PNAction pnAction=ActionMatchbyName(pcb.getInterpretation(), e.getName());
			System.out.println("\t"+" action Name "+ pnAction.getName());
			
			if(pnAction == null)
			{
				System.out.println("Action is nt found");
				ErreurList.add("Action is nt found");
			}
			else
			{	if(action.getAssociated_actions().size() == pnAction.getEntityAction().size())
				{
					System.out.println("test Action checked");
				}

			else{
				System.out.println("number of actions mismatching");
				ErreurList.add("VHDLAction "+e.getName()+" : number of Associated actions mismatching "+action.getAssociated_actions().size()+" old : "+pnAction.getEntityAction().size()+"\n");
			}
			//content??
			//if(action.getContent().equals(pnAction.get))
			}
			if( e instanceof VHDLCondition)
			{
				VHDLCondition condition = (VHDLCondition) e;
				
				PNCondition pnCondition=ConditionMatchbyName(pcb.getInterpretation(), condition.getName());
				
				if(pnCondition== null)
				{
					System.out.println("condition is not found");
					ErreurList.add("condition is not found");
				}
				else
				{
					if(condition.getAssociated_conditions().size()== pnCondition.getEntityCondition().size())
					{
						System.out.println("test Condition ok");
					}
					else
					{
						System.out.println("number of conditions is mismatching");
						ErreurList.add("VHDLCondition "+e.getName()+" number of conditions is mismatching "+condition.getAssociated_conditions().size()+" old : "+pnCondition.getEntityCondition().size());
					}
				}
				
				//content??
				//if(e.getINOUTTypeParameters()==pnAction.getINOUTs())
				//System.out.println("VHDLAction : "+ e.getName()+" checked");
			}
			root.getComponent().getVHDLElements();
		}
		if(e instanceof VHDLTime)
		{VHDLTime time=(VHDLTime) e;
		
			PNTime pntime=TimeMatchbyName(pcb.getInterpretation()	, time.getName());
			
			if(pntime==null)
			{
				System.out.println("pntime is not found");
				ErreurList.add("\t\t* pntime is not found\n");
			}
			else
			{
				if(time.getAssociated_time().size()==pntime.getTimes().size())
				{
					System.out.println("\t\t* pntime is checked\n");
				}
				else
				{
					System.out.println("");
					ErreurList.add("\t\t* VHDLTime : "+time.getName()+" number of pntime is mismatched: pnTime"+pntime.getTimes().size()+" vhdltime "+time.getAssociated_time().size()+"\n");
				}
				
			}
		}
		
		
		i++;
	}
	}


	
	
	
	
	public void TestInstance (HilecopRoot root, HilecopComponentDesignFile designfile)
	{
		//???
		int OldInstanceSize=designfile.getHilecopComponent().getComponentBehaviour().getComponentsInstances().size();
		int NewInstanceSize=root.getComponent().getComponentInstances().size();
		System.out.println("NB instances : "+OldInstanceSize+" "+NewInstanceSize);
		ErreurList.add("\tTestComponentInstance\n");
		if(OldInstanceSize!=NewInstanceSize)
		{
			ErreurList.add("Number of Instances is not matching :" +"new "+NewInstanceSize+" old "+OldInstanceSize+"\n" );
			
		}
		//designfile.getHilecopComponent().getComponentBehaviour().getComponentsInstances().get(0) instanceof Inst
		int i=0;
		while(i<OldInstanceSize || i< NewInstanceSize)
		{
			ComponentInstance instancenew= root.getComponent().getComponentInstances().get(i);
			hilecopComponent.ComponentInstance oldhistory=InstanceComponentMatchbyName(designfile.getHilecopComponent().getComponentBehaviour().getComponentsInstances(),instancenew.getName());
			
			if(oldhistory == null)
			{
				System.out.println("ComponentInstance is nt found");
				ErreurList.add("ComponentInstance is nt found");
			}
			else
			{
				if(instancenew.getInstanceOf().equals(oldhistory.getDescriptorName()))
				{
					System.out.println("InstanceOf is checked");
				}
				else
				{
					System.out.println("");
					ErreurList.add("ComponentInstance : "+instancenew.getName()+" instanceOf :"+instancenew.getInstanceOf()+" Old instanceOf : "+oldhistory.getDescriptorName());
				}
				
				if(instancenew.getFields().size() == oldhistory.getPublicFields().size())
				{
					System.out.println("ComponentInstance nb fields is checked");
				}
				else
				{
					System.out.println("");
					ErreurList.add("ComponentInstance : "+instancenew.getName()+" nb fields :"+instancenew.getFields().size()+" Old  : "+oldhistory.getPublicFields().size());
				}
			}
			i++;
		}
	}
	
	
	// recherche du nom d'un compnant dans l'ancien modele
	public Port PortMatchbyName(ComponentDescriptor hcd, String newPortName)
	{Port resPort=null;
		for(Port p:hcd.getPorts())
		{
			if(p.getName().equals(newPortName))
			{
				resPort=p;
				return resPort;
			}
		}
		return resPort;
	}
	
	public Signal SignalMatchbyName(ComponentDescriptor hcd, String newSignalName)
	{
		Signal resSignal=null;
		
		for(BehaviourField f:hcd.getComponentBehaviour().getPrivateFields())
		{
			if(f instanceof Signal) {
				Signal s = (Signal) f;
				if(s.getName().equals(newSignalName))
				{
					resSignal=s;
					return resSignal;
				}
			}
		}
		return resSignal;
	}

	public Generic GenericMatchbyName(ComponentDescriptor hcd, String newGenericName)
	{
		Generic ResGeneric=null;
		
		for(Generic g:hcd.getGenerics())
		{
			if(g.getName().equals(newGenericName))
			{
				ResGeneric=g;
				return ResGeneric;
			}
		}
		return ResGeneric;
	}

	public RefPlace RefPlaceMatchbyName(ComponentDescriptor hcd, String newRefPlaceName)
	{
		RefPlace ResRefPlace=null;
		for(RefPlace rp:hcd.getRefPlaces())
		{
			if(rp.getName().equals(newRefPlaceName))
			{
				ResRefPlace=rp;
				return ResRefPlace;
			}
		}
		
		return ResRefPlace;
	}
	
public RefTransition RefTransitionMatchbyName(ComponentDescriptor hcd, String newRefTransitionName)
{
	RefTransition ResRefTransition=null;
	for(RefTransition r:hcd.getRefTransitions())
	{
		if(r.getName().equals(newRefTransitionName))
		{
			ResRefTransition=r;
			return ResRefTransition;
		}
	}return ResRefTransition;
}

public Constant ConstantMatchbyName(ComponentDescriptor hcd, String newConstantName)
{
	Constant ResConstant=null;
	for(BehaviourField bf:hcd.getComponentBehaviour().getPrivateFields())
	{
		if(bf instanceof Constant)
		{Constant c=(Constant) bf;
			if(c.getName().equals(newConstantName))
			{
				ResConstant=c;
				return ResConstant;
			}
		}
	}return ResConstant;
}
	
	public PNFunction FunctionnMatchbyName(PNInterpretation pi, String newPNFunctionName)
	{PNFunction resFunction=null;
	//PetriNetComponentBehaviour pcb=(PetriNetComponentBehaviour)hcd.getComponentBehaviour();
		for(PNFunction pnf: pi.getFunctions())
		{
			if(pnf.getName().equals(newPNFunctionName))
				{
					resFunction=pnf;
					return resFunction;
				}
		}

		return resFunction;
		
	}
	
	public PNAction ActionMatchbyName(PNInterpretation pi, String newPNActionName)
	{PNAction resAction=null;
	//PetriNetComponentBehaviour pcb=(PetriNetComponentBehaviour)hcd.getComponentBehaviour();
		for(PNAction pnf: pi.getActions())
		{
			if(pnf.getName().equals(newPNActionName))
				{
					resAction=pnf;
					return resAction;
				}
		}

		return resAction;
		
	}
	
	
	public PNCondition ConditionMatchbyName(PNInterpretation pi, String newPNActionName)
	{PNCondition resCondition=null;
	//PetriNetComponentBehaviour pcb=(PetriNetComponentBehaviour)hcd.getComponentBehaviour();
		for(PNCondition pnf: pi.getConditions())
		{
			if(pnf.getName().equals(newPNActionName))
				{
					resCondition=pnf;
					return resCondition;
				}
		}

		return  resCondition;
		
	}
	
	public PNTime TimeMatchbyName(PNInterpretation pi, String newPNTimeName)
	{PNTime resCondition=null;
	//PetriNetComponentBehaviour pcb=(PetriNetComponentBehaviour)hcd.getComponentBehaviour();
		for(PNTime pnf:pi.getTimes() )
		{
			if(pnf.getName().equals(newPNTimeName))
				{
					resCondition=pnf;
					return resCondition;
				}
		}

		return  resCondition;
		
	}
	
	public petriNet.Place PlaceMatchbyName(EList <petriNet.PNEntity> entity,String place)
	{
		petriNet.Place resPlace=null;
		
		for(petriNet.PNEntity e:entity)
		{if(e instanceof petriNet.Place)
		{petriNet.Place e1=(petriNet.Place) e;
			if(e1.getName().equals(place))
			{
				resPlace=e1;
				return resPlace;
			}
		}
		}
		return resPlace;
	}
	
	
	
	public petriNet.Transition TransitionMatchbyName(EList <petriNet.PNEntity> entity,String NameTransition)
	{
		petriNet.Transition resTransition=null;
		
		for(petriNet.PNEntity e:entity)
		{if(e instanceof petriNet.Transition)
		{petriNet.Transition e1=(petriNet.Transition) e;
			if(e1.getName().equals(NameTransition))
			{
				resTransition=e1;
				return resTransition;
			}
		}
		}
		return resTransition;
	}
	
	public hilecopComponent.ComponentInstance InstanceComponentMatchbyName(EList<hilecopComponent.ComponentInstance> cmpInstance, String InstanceName)
	{hilecopComponent.ComponentInstance resInstance=null;
	
	for(hilecopComponent.ComponentInstance cp: cmpInstance)
	{
			if(cp.getName().equals(InstanceName))
			{
				resInstance=cp;
				return resInstance;
			}
	}
		return resInstance;
	}
	
	

	
	
	
	
	
//les parcours
	public void ParcoursField(HilecopComponentDesignFile designfile)

	{
		int designfileFieldsSize=designfile.getHilecopComponent().getPublicFields().size();
		int s=designfile.getHilecopComponent().getComponentBehaviour().getPrivateFields().size();
		int tSize=designfileFieldsSize+s;
		
		System.out.println(tSize);
		
					
					for(Port p:designfile.getHilecopComponent().getPorts() )
					{System.out.println("\n"+"PORT ");
						System.out.println(p.getName());
						System.out.println(p.getType());
						System.out.println(p.getMode());
						System.out.println(p.getDefaultValue());
					}
				
					for(Signal s1:designfile.getHilecopComponent().getSignals() )
					{System.out.println("\n"+"Signal ");
						System.out.println(s1.getName());
						System.out.println(s1.getType());
						System.out.println(s1.getDefaultValue());
					}
				
					
					for(BehaviourField b: designfile.getHilecopComponent().getComponentBehaviour().getPrivateFields())
					{ 
						if(b instanceof Constant)
						{Constant cb=(Constant) b;
						System.out.println("\n"+"Constant");
							System.out.println(cb.getName());
							System.out.println(cb.getType());
							System.out.println(cb.getDefaultValue());
						}
					}
					
					for(Generic g:designfile.getHilecopComponent().getGenerics())
					{System.out.println("\n"+"Generic ");
						System.out.println(g.getName());
						System.out.println(g.getDefaultValue());
						System.out.println(g.getType());
						
					}
				
					for(RefPlace rf:designfile.getHilecopComponent().getRefPlaces())
					{System.out.println("\n"+"RefPlace ");
						System.out.println(rf.getName());
						System.out.println(rf.getPlace());
						System.out.println(rf.getMode());
						//type
					}
				
					for(RefTransition rt: designfile.getHilecopComponent().getRefTransitions())
					{System.out.println("\n"+"RefTransition ");
						System.out.println(rt.getName());
						System.out.println(rt.getMode());
						
						
					}
		
		
	}
	
	

	public void ParcoursPnSructureObjects(HilecopComponentDesignFile designfile)
	{
		System.out.println("Parcours PNStructure...");
		
		PetriNetComponentBehaviour pcb=(PetriNetComponentBehaviour) designfile.getHilecopComponent().getComponentBehaviour();
		
		
			for(PNAction pns:pcb.getInterpretation().getActions())
			{
				System.out.println("\n"+"Action :");
				System.out.println(pns.getName());
				System.out.println(pns.getINOUTs());
				System.out.println(pns.getOUTs());
				System.out.println(pns.getSensibilityIN());
				System.out.println(pns.getSensibilityOUT());
				System.out.println(pns.getSpecificReset());
			}
			
			for(PNFunction pnf:pcb.getInterpretation().getFunctions())
			{
				System.out.println("\n"+"Function");
				System.out.println(pnf.getName());
				System.out.println(pnf.getINOUTs());
				System.out.println(pnf.getOUTs());
				System.out.println(pnf.getINOUTs());
			}
			
			for(PNCondition pnc:pcb.getInterpretation().getConditions())
			{	
				System.out.println("\n"+"Condition");
				System.out.println(pnc.getName());
				System.out.println(pnc.getINOUTs());
				System.out.println(pnc.getOUTs());
				System.out.println(pnc.getSensibilityIN());
				System.out.println(pnc.getSensibilityOUT());
				System.out.println(pnc.getINs());
			}
			for(PNTime pnt: pcb.getInterpretation().getTimes())
			{
				System.out.println(pnt.getName());
				System.out.println(pnt.getName());
				System.out.println(pnt.getINOUTs());
				System.out.println(pnt.getOUTs());
				System.out.println(pnt.getSensibilityIN());
				System.out.println(pnt.getSensibilityOUT());
				System.out.println(pnt.getINs());
			}
		
		
		
		
		}
	
	
	public void ParcoursRoot(HilecopRoot root)
	{
		HilecopComponent c=root.getComponent();
		//HilecopComponentDesignFile hc=(HilecopComponentDesignFile) designfile.getHilecopComponent();
		
		int cFieldsSize=c.getFields().size();
	
		
		for(Field field : c.getFields()) {
		
			if(field instanceof VHDLPort) {
				VHDLPort port = (VHDLPort) field;
				System.out.println("port mode :"+port.getMode());
				System.out.println("port Name :"+port.getName());
				System.out.println("port mode :"+port.getDefaultValue());
				System.out.println("port mode :"+port.getMode());
			}
		}
		

		
		int nbConnections=c.getConnections().size();
		
		if(nbConnections!=0)
		{
			for(int i=0;i<nbConnections;i++)
			{
				System.out.println("connection Name"+c.getConnections().get(i).getName()+"\n");
				System.out.println("connection InputField"+c.getConnections().get(i).getInputField().getName());
				
			}
		}
		
		int nbPNObjects=c.getPNStructureObjects().size();
		if(nbPNObjects!=0)
		{
			for(int i=0;i<nbPNObjects;i++)
			{
				System.out.println("connection Name"+c.getPNStructureObjects().get(i)+"\n");	
			}
		}
		
		int nbInstances=c.getListOfInstances().size();
		if(nbInstances!=0)
		{
			for(int i=0;i<nbInstances;i++)
			{
				System.out.println("connection Name"+c.getListOfInstances().get(i).getName()+"\n");	
				//System.out.println("connection Name"+c.getListOfInstances().get(i).get()+"\n");	
			}
		}
		
		int nb=c.getVHDLElements().size();
		//action???
		//c.getVHDLElements().get(0).get
		
	}

	
	public void ErrorOutput(ArrayList<String> al,String location) throws IOException
	{
		FileWriter writer = new FileWriter(location+"/output_log.txt"); 
		for(String str: al) {
		  writer.write(str);
		}
		writer.close();
		
	}
	
	public ArrayList<String> getErreurList() {
		return ErreurList;
	}

	public void setErreurList(ArrayList<String> erreurList) {
		ErreurList = erreurList;
	}

	public static void main(String[] args) throws IOException {
		
	Test t=new Test();
	HilecopComponentDesignFile designfile=t.read("C:/Users/su40209/Desktop/Hilecop_Workspace_Old_Hilecop/USR/CG1_executeur.hilecopcomponent");
	HilecopRoot root=t.open("C:/Users/su40209/Desktop/TestMigration/USR/CG1_executeur.root");
	//HilecopRoot root=t.open("C:/Users/su40209/Desktop/TestTest/CG1_executeur.root");
	
	t.TestField(root, designfile);
	
	t.TestPNStructure(root,designfile);
	t.TestVHDLElement(root, designfile);
	t.TestInstance(root, designfile);
	t.TestConnection(root, designfile);
	
	
	//t.ParcoursPnSructureObjects(t.read("C:/Users/dhivyalaptop/Desktop/stage/ex_workspace/Switch_Led/Switch_Led/Switch_Led/Switch_Led.hilecopcomponent"));
	
	//t.ErrorOutput(t.getErreurList());
	}

	
}
