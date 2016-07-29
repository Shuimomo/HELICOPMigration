package migration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.emf.common.util.EList;

import field.*;
import field.Field;
import field.FusionConnection;
import field.SimpleConnection;
import hilecopComponent.*;
import hilecopComponent.Connection;
import petriNet.Node;
import petriNet.PetriNetFactory;
import root.HilecopComponent;
import root.HilecopRoot;
import root.RootFactory;
import script.ScriptFactory;
import script.VHDLAction;
import script.VHDLCondition;
import script.VHDLElement;
import script.VHDLFunction;
import script.VHDLTime;

public class MigrationDuComposant {

	private NouveauComposant newcomposant;
	private HilecopRoot newroot;
	private AncienComposant originalcomposant;
	private String originalfolder;
	private EditorInstanceContainer historyinstance;

	public MigrationDuComposant(String locatenew, File fileancien, EditorInstanceContainer EIC) throws IOException{
		originalfolder = fileancien.getParent();
		originalcomposant = new AncienComposant(fileancien.getAbsolutePath());
		String name = originalcomposant.getRoot().getDesignFileName();
		newcomposant = new NouveauComposant(locatenew, name);
		newroot = newcomposant.getRoot();
		historyinstance = EIC;
		MigrationDuProjet.errors.add(newcomposant.getName()+"\n");
	}

	public void migeration(){
		System.out.println("Fiche "+newroot.getComponent().getName());
		this.migrationField();
		this.migrationInstance();
		this.migrationVHDL();
		this.migrationBasicNode();
		this.migrationRefNode();
		this.migrationArc();
		this.migrationConnection();
		System.out.println("");
	}


	public void migrationField(){
		EList<Port> listePort = originalcomposant.getPorts();
		//System.out.println("Number of Port :" + listePort.size());
		for(Port port : listePort){
			newroot.getComponent().getFields().add(convertPort(port));
			System.out.println("Port " + port.getName() +" is migrated");
		}

		ArrayList<Signal> listeSignal = originalcomposant.getSignals();
		//System.out.println("Number of Signal :" + listeSignal.size());
		for(Signal signal : listeSignal){
			newroot.getComponent().getFields().add(convertSignal(signal));
			System.out.println("Signal " + signal.getName() +" is migrated");
		}

		EList<Generic> listeGeneric = originalcomposant.getGenerics();
		//System.out.println("Number of Generic :" + listeGeneric.size());
		for(Generic generic :listeGeneric){
			newroot.getComponent().getFields().add(convertGeneric(generic));
			System.out.println("Generic " + generic.getName() +" is migrated");
		}

		ArrayList<Constant> listeConstant = originalcomposant.getConstants();
		//System.out.println("Number of Constant :" + listeConstant.size());
		for(Constant constant : listeConstant){
			newroot.getComponent().getFields().add(convertConstant(constant));
			System.out.println("Generic " + constant.getName() +" is migrated");
		}
	}

	public void migrationInstance(){
		EList<ComponentInstance> listeInstance = originalcomposant.getInstances();
		//System.out.println("Number of Instance : " + listeInstance.size());
		for(ComponentInstance instance : listeInstance){
			newroot.getComponent().getComponentInstances().add(convertInstance(instance));
			System.out.println("Instance " + instance.getName() +" is migrated");
		}
	}

	public void migrationVHDL(){
		EList<PNAction> listeVHDLAction = originalcomposant.getPNActions();
		//System.out.println("Number of PNAction : "+listeVHDLAction.size());
		for(PNAction action : listeVHDLAction){
			newroot.getComponent().getVHDLElements().add(convertVHDLAction(action));
		}

		EList<PNFunction> listeVHDLFunction = originalcomposant.getPNFunctions();
		//System.out.println("Number of PNFunction : "+listeVHDLFunction.size());
		for(PNFunction function : listeVHDLFunction){
			newroot.getComponent().getVHDLElements().add(convertVHDLFunction(function));
		}

		EList<PNCondition> listeVHDLCondition = originalcomposant.getPNConditions();
		//System.out.println("Number of PNCondition : "+listeVHDLCondition.size());
		for(PNCondition condition : listeVHDLCondition){
			newroot.getComponent().getVHDLElements().add(convertVHDLCondition(condition));
		}

		EList<PNTime> listeVHDLTime = originalcomposant.getPNTimes();
		System.out.println("Number of PNTime : "+listeVHDLTime.size());
		for(PNTime time : listeVHDLTime){
			newroot.getComponent().getVHDLElements().add(convertVHDLTime(time));
		}
	}

	public void migrationBasicNode(){
		ArrayList<hilecopComponent.Place> listePlace = originalcomposant.getPlaces();
		//System.out.println("Number of Place : " + listePlace.size());
		for(hilecopComponent.Place place : listePlace){
			newroot.getComponent().getPNStructureObjects().add(convertPlace(place));
			System.out.println("Place " + place.getName() +" is migrated");
		}

		ArrayList<Transition> listeTransition = originalcomposant.getTransitions();
		//System.out.println("Number of Transition : " + listeTransition.size());
		for(Transition transition : listeTransition){
			newroot.getComponent().getPNStructureObjects().add(convertTransition(transition));
			System.out.println("Transition " + transition.getName() +" is migrated");
		}
	}

	public void migrationRefNode(){
		EList<RefPlace> listeRefPlace = originalcomposant.getRefPlaces();
		//System.out.println("Number of RefPlace :" + listeRefPlace.size());
		for(RefPlace refplace : listeRefPlace){
			petriNet.RefPlace newrefplace = convertRefPlace(refplace);
			if(newrefplace != null){
				newroot.getComponent().getFields().add(newrefplace);
			}
			System.out.println("RefPlace " + refplace.getName() +" is migrated");
		}

		EList<RefTransition> listeRefTransition = originalcomposant.getRefTransitions();
		//System.out.println("Number of RefTransition :" + listeRefTransition.size());
		for(RefTransition refTransition : listeRefTransition){
			petriNet.RefTransition newreftransition = convertRefTransition(refTransition);
			if(newreftransition != null){
				newroot.getComponent().getFields().add(newreftransition);
			}
			System.out.println("RefTransition " + refTransition.getName() +" is migrated");
		}
	}

	public void migrationArc(){
		ArrayList<BasicArc> listeBasicArc = originalcomposant.getBasicArcs();
		//System.out.println("Number of BasicArc : " + listeBasicArc.size());
		for(BasicArc basicArc : listeBasicArc){
			petriNet.BasicArc newarc = convertBasicArc(basicArc);
			if(newarc!=null){
				newroot.getComponent().getPNStructureObjects().add(newarc);
				System.out.println("BasicArc " + basicArc.getName() +" is migrated");
			}
		}

		ArrayList<TestArc> listeTestArc = originalcomposant.getTestArcs();
		//System.out.println("Number of TestArc : " + listeTestArc.size());
		for(TestArc testArc : listeTestArc){
			petriNet.TestArc newarc = convertTestArc(testArc);
			if(newarc!=null){
				newroot.getComponent().getPNStructureObjects().add(newarc);
				System.out.println("TestArc " + testArc.getName() +" is migrated");
			}
		}

		ArrayList<InhibitorArc> listeInhibitorArc = originalcomposant.getInhibitorArcs();
		//System.out.println("Number of InhibitorArc : " + listeInhibitorArc.size());
		for(InhibitorArc inhibitorArc : listeInhibitorArc){
			petriNet.InhibitorArc newarc = convertInhibitorArc(inhibitorArc);
			if(newarc!=null){
				newroot.getComponent().getPNStructureObjects().add(newarc);
				System.out.println("InhibitorArc " + inhibitorArc.getName() +" is migrated");
			}
		}

		ArrayList<FusionArc> listeFusionArc = originalcomposant.getFusionArcs();
		//System.out.println("Number of FusionArc : " + listeFusionArc.size());
		for(FusionArc fusionArc : listeFusionArc){
			petriNet.FusionArc newarc = convertFusionArc(fusionArc);
			if(newarc!=null){
				newroot.getComponent().getPNStructureObjects().add(newarc);
				System.out.println("FusionArc " + fusionArc.getName() +" is migrated");
			}
		}
	}

	public void migrationConnection(){
		EList<Connection> listeconnection = originalcomposant.getConnections();
		System.out.println("Number of Connection : "+listeconnection.size());
		for(Connection c : listeconnection){
			if(c instanceof hilecopComponent.SimpleConnection){
				hilecopComponent.SimpleConnection sconnection = (hilecopComponent.SimpleConnection)c;
				SimpleConnection newsconnection = convertSimpleConnection(sconnection);
				if(newsconnection!=null){
					newroot.getComponent().getConnections().add(newsconnection);
				}
			}
			if(c instanceof hilecopComponent.FusionConnection){
				hilecopComponent.FusionConnection fconnection = (hilecopComponent.FusionConnection)c;
				FusionConnection newfconnection = convertFusionConnection(fconnection);
				if(newfconnection!=null){
					newroot.getComponent().getConnections().add(newfconnection);
				}
			}
		}
	}

	/*
	 * Field
	 */
	private VHDLPort convertPort(Port port){
		VHDLPort newport = FieldFactory.eINSTANCE.createVHDLPort(); 
		newport.setName(port.getName());
		setPortMode(newport, port);
		newport.setDefaultValue(port.getDefaultValue());
		newport.setType(port.getType());
		return newport;
	}

	private VHDLSignal convertSignal(Signal signal){
		VHDLSignal newsignal = FieldFactory.eINSTANCE.createVHDLSignal();
		newsignal.setName(signal.getName());
		newsignal.setType(signal.getType());
		newsignal.setDefaultValue(signal.getDefaultValue());
		return newsignal;
	}

	private VHDLGeneric convertGeneric(Generic generic){
		VHDLGeneric newgeneric = FieldFactory.eINSTANCE.createVHDLGeneric();
		newgeneric.setName(generic.getName());
		newgeneric.setType(generic.getType());
		newgeneric.setDefaultValue(generic.getDefaultValue());
		return newgeneric;
	}

	private VHDLConstant convertConstant(Constant constant){
		VHDLConstant newconstant = FieldFactory.eINSTANCE.createVHDLConstant();
		newconstant.setName(constant.getName());
		newconstant.setType(constant.getType());
		newconstant.setDefaultValue(constant.getDefaultValue());
		return newconstant;
	}

	private field.SimpleConnection convertSimpleConnection(hilecopComponent.SimpleConnection Sconnection){
		SimpleConnection newSconnection = FieldFactory.eINSTANCE.createSimpleConnection();
		String name = "simpleconnection_"+Sconnection.getId();
		newSconnection.setName(name);
		setField(newSconnection,Sconnection);
		newSconnection.setSourceSelectionExpression(Sconnection.getSourceSelectionExpression());
		newSconnection.setTargetSelectionExpression(Sconnection.getTargetSelectionExpression());
		if(setField(newSconnection,Sconnection)){
			return newSconnection;
		}
		else{
			return null;
		}
	}

	private field.FusionConnection convertFusionConnection(hilecopComponent.FusionConnection Fconnection){
		FusionConnection newFconnection = FieldFactory.eINSTANCE.createFusionConnection();
		String name = "fusionconnection_"+Fconnection.getId();
		newFconnection.setName(name);
		if(setField(newFconnection,Fconnection)){
			return newFconnection;
		}
		else{
			return null;
		}
	}

	private VHDLAction convertVHDLAction(PNAction pnaction){
		VHDLAction vhdlAction = ScriptFactory.eINSTANCE.createVHDLAction();
		setVHDLscript(vhdlAction,pnaction);
		return vhdlAction;
	}

	private VHDLCondition convertVHDLCondition(PNCondition pnCondition){
		VHDLCondition vhdlCondition = ScriptFactory.eINSTANCE.createVHDLCondition();
		setVHDLscript(vhdlCondition,pnCondition);
		return vhdlCondition;
	}

	private VHDLFunction convertVHDLFunction(PNFunction pnFunction){
		VHDLFunction vhdlFunction = ScriptFactory.eINSTANCE.createVHDLFunction();
		setVHDLscript(vhdlFunction,pnFunction);
		return vhdlFunction;
	}

	private VHDLTime convertVHDLTime(PNTime pntime){
		VHDLTime vhdlTime = ScriptFactory.eINSTANCE.createVHDLTime();
		setVHDLscript(vhdlTime,pntime);
		return vhdlTime;
	}

	private petriNet.Place convertPlace(hilecopComponent.Place place){
		petriNet.Place newplace = PetriNetFactory.eINSTANCE.createPlace();
		newplace.setName(place.getName());
		newplace.setMarking(Integer.parseInt(place.getMarkupExpression()));
		//add actions
		EList<PNEntityInterpretation> listeInterpretation = place.getInterpretation();
		for(PNEntityInterpretation e : listeInterpretation){
			if(e instanceof Action)
			{
				Action action =  (Action)e;
				setAction(newplace,action);
			}
		}
		return newplace;
	}

	private petriNet.RefPlace convertRefPlace(RefPlace refplace){
		petriNet.RefPlace newrefplace = PetriNetFactory.eINSTANCE.createRefPlace();
		newrefplace.setName(refplace.getName());
		setRefPlaceMode(newrefplace, refplace);
		//check refplace.place exist ou pas
		ArrayList<petriNet.Place> listeplace = newcomposant.getPlaces();

		if(refplace.getPlace()!=null){
			String placename = refplace.getPlace().getName();
			Boolean notfind = true;
			for(petriNet.Place place : listeplace){
				if(place.getName().equals(placename)){
					newrefplace.setPlace(place);
					notfind = false;
				}
			}
			if(notfind){
				MigrationDuProjet.errors.add("Error : Can't find Place "+ placename+" for refPlace "+newrefplace.getName()+"\n");
			}
			return newrefplace;
		}
		else{
			MigrationDuProjet.errors.add("DELETE Warning : RefPlace "+ refplace.getFieldName() +" has no place\n");
			return null;
		}
	}

	private petriNet.Transition convertTransition(Transition transition){
		petriNet.Transition newtransition = PetriNetFactory.eINSTANCE.createTransition();
		newtransition.setName(transition.getName());
		EList<PNEntityInterpretation> listeInterpretation = transition.getInterpretation();
		for(PNEntityInterpretation e : listeInterpretation){
			if(e instanceof Condition)
			{
				Condition condition =  (Condition)e;
				setCondition(newtransition,condition);
			}
			if(e instanceof Function)
			{
				Function function =  (Function)e;
				setFunction(newtransition,function);
			}
		}
		int nbtime = 0;
		for(PNEntityTimeBehaviour pntb : transition.getTemporalBehaviour()){
			//ne peut avoir qu'un dynamic time
			if((pntb instanceof Time)&&nbtime<2)
			{
				nbtime = nbtime+1;
				if(nbtime==1){
					Time time =  (Time)pntb;
					setTime(newtransition,time);
				}
				else{
					MigrationDuProjet.errors.add("Warning : Dans original composant transition " + transition.getName()+" has more than one Time\n");
				}
			}
		}
		return newtransition;
	}

	private petriNet.RefTransition convertRefTransition(RefTransition reftransition){
		petriNet.RefTransition newreftransition = PetriNetFactory.eINSTANCE.createRefTransition();
		newreftransition.setName(reftransition.getName());
		setRefTransitionMode(newreftransition, reftransition);		
		//check reftransition.transition exist ou pas
		ArrayList<petriNet.Transition> listetransition = newcomposant.getTransitions();
		if(reftransition.getTransition()!=null){
			String transitionname = reftransition.getTransition().getName();
			Boolean notfind = true;
			for(int i=0;i<listetransition.size();i++){
				if(listetransition.get(i).getName().equals(transitionname)){
					newreftransition.setTransition(listetransition.get(i));
					notfind = false;
				}
			}
			if(notfind){
				MigrationDuProjet.errors.add("Error : Can't find transition "+ transitionname +" for refTransition "+newreftransition.getName()+"\n");
			}
			return newreftransition;
		}
		else{
			MigrationDuProjet.errors.add("DELETE Warning : RefTransition "+ reftransition.getFieldName() +" has no transition\n");
			return null;
		}
	}

	private petriNet.BasicArc convertBasicArc(BasicArc barc){
		petriNet.BasicArc newBarc = PetriNetFactory.eINSTANCE.createBasicArc();
		newBarc.setName(barc.getName());
		newBarc.setRuleExpression(barc.getRuleExpression());
		if(setNode(newBarc,barc)){
			return newBarc;
		}
		else{
			return null;
		}
	}

	private petriNet.TestArc convertTestArc(TestArc tarc){
		petriNet.TestArc newTarc = PetriNetFactory.eINSTANCE.createTestArc();
		newTarc.setName(tarc.getName());
		newTarc.setRuleExpression(tarc.getRuleExpression());
		if(setNode(newTarc,tarc)){
			return newTarc;
		}
		else{
			return null;
		}
	}

	private petriNet.InhibitorArc convertInhibitorArc(InhibitorArc iarc){
		petriNet.InhibitorArc newIarc = PetriNetFactory.eINSTANCE.createInhibitorArc();
		newIarc.setName(iarc.getName());
		newIarc.setRuleExpression(iarc.getRuleExpression());
		if(setNode(newIarc,iarc)){
			return newIarc;
		}
		else{
			return null;
		}
	}

	private petriNet.FusionArc convertFusionArc(FusionArc farc){
		petriNet.FusionArc newFarc = PetriNetFactory.eINSTANCE.createFusionArc();
		newFarc.setName(farc.getName());
		if(setNode(newFarc,farc)){
			return newFarc;
		}
		else{
			return null;
		}
	}

	private root.ComponentInstance convertInstance(ComponentInstance instance){
		root.ComponentInstance newinstance = RootFactory.eINSTANCE.createComponentInstance();
		newinstance.setName(instance.getName());
		newinstance.setInstanceOf(instance.getInstanceOf().getDescriptorName());

		/* find the compsant which this instance is instance of */
		String instanceof_name = instance.getInstanceOf().getName();
		AncienComposant refdcomp = this.getComposantInstanceOf(instanceof_name);
		/* And add "this composant - instanceof" to EditorInstanceContainer */
		historyinstance.add(instanceof_name,newcomposant.getName());

		/* Begin translation of elements of instance */
		EList<RefPlace> listerefplace = refdcomp.getRefPlaces();
		EList<RefTransition> listereftransition = refdcomp.getRefTransitions();

		if(!instance.getPorts().isEmpty()){
			for(Port port :instance.getPorts()){
				newinstance.getFields().add(convertPort(port));
			}
		}

		if(!instance.getRefPlaces().isEmpty()){
			for(RefPlace refplace : instance.getRefPlaces()){
				petriNet.RefPlace newrefplace = PetriNetFactory.eINSTANCE.createRefPlace();
				newrefplace.setName(refplace.getName());
				setRefPlaceMode(newrefplace, refplace);

				petriNet.Place place = PetriNetFactory.eINSTANCE.createPlace();
				place.setName("Not found");
				/* find this refplace in the composant */
				for(RefPlace e :listerefplace){
					if(e.getName().equals(newrefplace.getName())){
						place.setName(e.getPlace().getName());
					}
				}
				newrefplace.setPlace(place);
				newinstance.getFields().add(newrefplace);
				newinstance.getPNStructureObjects().add(place);
			}
		}

		if(!instance.getRefTransitions().isEmpty()){
			for(RefTransition reft : instance.getRefTransitions()){
				petriNet.RefTransition newreftransition = PetriNetFactory.eINSTANCE.createRefTransition();
				newreftransition.setName(reft.getName());
				setRefTransitionMode(newreftransition, reft);

				petriNet.Transition transition = PetriNetFactory.eINSTANCE.createTransition();
				transition.setName("Not found");
				for(RefTransition e :listereftransition){
					if(e.getName().equals(newreftransition.getName())){
						transition.setName(e.getTransition().getName());
					}
				}
				newreftransition.setTransition(transition);
				newinstance.getFields().add(newreftransition);
				newinstance.getPNStructureObjects().add(transition);
			}
		}
		return newinstance;
	}

	private AncienComposant getComposantInstanceOf(String name){
		String path = originalfolder+"\\"+name+".hilecopcomponent";
		//File fileancien = new File(path);
		AncienComposant o = new AncienComposant(path);
		return o;
	}


	/**
	 * Donner mode du port selon mode de l'oldprojet port
	 * @param newport
	 * @param port
	 */
	private void setPortMode(VHDLPort newport, Port port){
		if(port.getMode().getValue()==0){
			newport.setMode(PortMode.IN);
		}
		if(port.getMode().getValue()==1){
			newport.setMode(PortMode.OUT);
		}
		if(port.getMode().getValue()==2){
			newport.setMode(PortMode.INOUT);
		}
	}

	private void setRefPlaceMode(petriNet.RefPlace newrefplace, RefPlace refplace){
		if(refplace.getMode().getValue()==0){
			newrefplace.setMode(PortMode.IN);
		}
		if(refplace.getMode().getValue()==1){
			newrefplace.setMode(PortMode.OUT);
		}
		if(refplace.getMode().getValue()==2){
			newrefplace.setMode(PortMode.INOUT);
		}
	}
	/**
	 * @param newrefTransition
	 * @param refTransition
	 */
	private void setRefTransitionMode(petriNet.RefTransition newrefTransition, RefTransition refTransition){
		if(refTransition.getMode().getValue()==0){
			newrefTransition.setMode(PortMode.IN);
		}
		if(refTransition.getMode().getValue()==1){
			newrefTransition.setMode(PortMode.OUT);
		}
		if(refTransition.getMode().getValue()==2){
			newrefTransition.setMode(PortMode.INOUT);
		}
	}

	private void setOperator(petriNet.Condition newcondition, Condition condition){
		if(condition.getOperator().equals(Operator.ID)){
			newcondition.setOperator(petriNet.Operator.ID);
		}
		if(condition.getOperator().equals(Operator.NOT)){
			newcondition.setOperator(petriNet.Operator.NOT);
		}
	}

	private void setAction(petriNet.Place newplace, Action action){
		petriNet.Action newaction = PetriNetFactory.eINSTANCE.createAction();
		newaction.setName(action.getName());
		String VHDLActionName = action.getAction().getName();
		ArrayList<VHDLAction> listeAction = newcomposant.getVHDLActions();
		for(VHDLAction vhdlaction : listeAction){
			if(vhdlaction.getName().equals(VHDLActionName)){
				newaction.setScript_action(vhdlaction);
				newplace.getActions().add(newaction);
			}
		}
	}

	private void setFunction(petriNet.Transition newTransition, Function Function){
		petriNet.Function newFunction = PetriNetFactory.eINSTANCE.createFunction();
		newFunction.setName(Function.getName());
		String VHDLFunctionName = Function.getFunction().getName();
		ArrayList<VHDLFunction> listeFunction = newcomposant.getVHDLFunctions();
		for(VHDLFunction vhdlfunction : listeFunction){
			if(vhdlfunction.getName().equals(VHDLFunctionName)){
				newFunction.setScript_function(vhdlfunction);
				newTransition.getFunctions().add(newFunction);
			}
		}
	}

	private void setCondition(petriNet.Transition newTransition, Condition condition){
		petriNet.Condition newcondition = PetriNetFactory.eINSTANCE.createCondition();
		newcondition.setName(condition.getName());
		setOperator(newcondition,condition);
		String VHDLConditionName = condition.getCondition().getName();
		ArrayList<VHDLCondition> listeCondition = newcomposant.getVHDLConditions();
		for(VHDLCondition vhdlcunction : listeCondition){
			if(vhdlcunction.getName().equals(VHDLConditionName)){
				newcondition.setScript_condition(vhdlcunction);
				newTransition.getConditions().add(newcondition);
			}
		}
	}

	private void setTime(petriNet.Transition newtransition, Time time){
		petriNet.Time newtime = PetriNetFactory.eINSTANCE.createTime();
		newtime.setTmin(time.getTmin());
		newtime.setTmax(time.getTmax());
		newtime.setDescription(time.getDescription());
		if(time.getDynamicTime()!=null){
			String VHDLTimeName = time.getDynamicTime().getName();
			ArrayList<VHDLTime> listeTime = newcomposant.getVHDLTimes();
			for(VHDLTime vhdltime : listeTime){
				if(vhdltime.getName().equals(VHDLTimeName)){
					newtime.setScript_time(vhdltime);
				}
			}
		}
		newtransition.setTime(newtime);
	}


	private void setVHDLscript(VHDLElement vhdl, PNInterpretationElement pn){
		vhdl.setName(pn.getName());
		String script = pn.getBehaviourInterpretation().getScript();
		int begin = script.indexOf("is begin");
		int fin = script.lastIndexOf("end");
		String content = script.substring(begin+8, fin);
		vhdl.setContent(content);
	}

	private Boolean setNode(petriNet.Arc newArc, Arc arc){
		hilecopComponent.Node nodeSource = arc.getSourceNode();
		hilecopComponent.Node nodeTarget = arc.getTargetNode();
		if(nodeSource != null && nodeTarget != null){
			if((findNode(nodeSource)!=null)&&(findNode(nodeTarget)!=null)){
				newArc.setSourceNode(findNode(nodeSource));
				newArc.setTargetNode(findNode(nodeTarget));
				return true;
			}
			else{
				MigrationDuProjet.errors.add("Error : Can not find Node for arc "+arc.getName()+"\n");
				return false;
			}
		}
		else{
			MigrationDuProjet.errors.add("DELETE Warning : Arc "+arc.getName()+" has a null node\n");
			return false;
		}
	}

	private Node findNode(hilecopComponent.Node node){
		String name = node.getName();
		Boolean notfind = true;
		Node newnode = null;
		ArrayList<petriNet.Place> listePlace = newcomposant.getPlaces();
		ArrayList<petriNet.Transition> listeTransition = newcomposant.getTransitions();
		EList<root.ComponentInstance> listeinstancenew = newroot.getComponent().getComponentInstances();

		if(node.eContainer() instanceof ComponentInstance){
			//System.out.println("This Node is in a Instance");
			ComponentInstance instance = (ComponentInstance) node.eContainer();
			for(int i=0;i<listeinstancenew.size()&&notfind;i++){
				/* trouve instance */
				if(listeinstancenew.get(i).getName().equals(instance.getName())){
					HilecopComponent instancenew = listeinstancenew.get(i);
					EList<Field> pn = instancenew.getFields();
					for(Field e : pn){
						if(e instanceof petriNet.RefPlace){
							petriNet.RefPlace refplace =(petriNet.RefPlace) e;
							if(refplace.getName().equals(name)){
								newnode = (Node) refplace;
								notfind = false;
							}
						}
						if(e instanceof petriNet.RefTransition){
							petriNet.RefTransition reftransition =(petriNet.RefTransition) e;
							if(reftransition.getName().equals(name)){
								newnode = (Node) reftransition;
								notfind = false;
							}
						}
					}
				}
			}
		}
		else{
			//System.out.println(listePlace.size());
			for(int i=0;(i<listePlace.size())&&notfind;i++){
				//System.out.println("Begin looking for "+name +" in Place");
				if(listePlace.get(i).getName().equals(name)){
					newnode = listePlace.get(i);
					notfind = false;
				}
			}
			for(int i=0;(i<listeTransition.size())&&notfind;i++){
				//System.out.println("Begin looking for "+name +" in Transition");
				if(listeTransition.get(i).getName().equals(name)){
					newnode = listeTransition.get(i);
					notfind = false;
				}
			}
		}
		return newnode;
	}

	private Boolean setField(field.Connection newconnection, hilecopComponent.BasicConnection connection){
		hilecopComponent.Field fieldInput = connection.getSourceField();
		hilecopComponent.Field fieldOutput = connection.getTargetField();
		if(fieldInput!=null&&fieldOutput!=null){
			if((findField(fieldInput)!=null)&&(findField(fieldOutput)!=null)){
				newconnection.setInputField(findField(fieldInput));
				newconnection.setOutputField(findField(fieldOutput));
				return true;
			}
			else{
				MigrationDuProjet.errors.add("Error : Can not find Field for connection " + connection.getId()+"\n");
				return false;
			}
		}
		else{
			MigrationDuProjet.errors.add("DELETE Warning : Connection " + connection.getId() + " has a null field\n");
			return false;
		}
	}

	private Field findField(hilecopComponent.Field field){
		String name = field.getName();
		Boolean notfind = true;
		Field newfield = null;
		EList<field.Field> listefield = newroot.getComponent().getFields();
		EList<root.ComponentInstance> listeinstancenew = newroot.getComponent().getComponentInstances();

		if(field.eContainer() instanceof ComponentInstance){
			ComponentInstance instance = (ComponentInstance) field.eContainer();
			for(int i=0;i<listeinstancenew.size()&&notfind;i++){
				/* trouve instance */
				if(listeinstancenew.get(i).getName().equals(instance.getName())){
					HilecopComponent instancenew = listeinstancenew.get(i);
					EList<Field> listeinstancefield = instancenew.getFields();
					for(Field e : listeinstancefield){
						if(e instanceof field.VHDLPort){
							field.VHDLPort port =(VHDLPort) e;
							if(port.getName().equals(name)){
								newfield =port;
								notfind = false;
							}
						}
					}
				}
			}
		}
		else{
			for(int i=0;i<listefield.size()&&notfind;i++){
				if(listefield.get(i).getName().equals(name)&&notfind){
					newfield = listefield.get(i);
					notfind = false;
				}
			}
		}
		return newfield;
	}

	public NouveauComposant getNewComp(){
		return newcomposant;
	}

	public void save() throws IOException {
		newcomposant.save();
	}
}