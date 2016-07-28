/**
 * @author shui
 */
package migration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.gmf.runtime.diagram.core.services.ViewService;
import org.eclipse.gmf.runtime.notation.Diagram;

import petriNet.PNEntity;
import petriNet.Place;
import petriNet.Transition;
import root.*;
import script.VHDLAction;
import script.VHDLCondition;
import script.VHDLElement;
import script.VHDLFunction;
import script.VHDLTime;

public class NouveauComposant {
	private ResourceSet resourceSet;
	private Resource newres;
	private HilecopRoot newroot;
	private String composantname;

	/**
	 * constructor; create un nouveau fichier root
	 * @param path
	 * @param name
	 * @throws IOException
	 */
	public NouveauComposant(String path, String name) throws IOException{
		resourceSet = new ResourceSetImpl();
		resourceSet.getPackageRegistry().put(root.RootPackage.eNS_URI,root.RootPackage.eINSTANCE);
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("root",new XMIResourceFactoryImpl());
		String filename = path + "\\" + name + ".root";
		File file = new File(filename);
		file.createNewFile();
		URI newURI = URI.createFileURI(filename);
		newres = resourceSet.createResource(newURI);

		newroot = RootFactory.eINSTANCE.createHilecopRoot(name);

		composantname = name;

	}

	public HilecopRoot getRoot(){
		return newroot;
	}

	public String getName(){
		return composantname;
	}
	public ArrayList<VHDLAction> getVHDLActions(){
		EList<VHDLElement> listeVHDL = newroot.getComponent().getVHDLElements();
		ArrayList<VHDLAction> listeAction = new ArrayList<VHDLAction>();
		for(VHDLElement e: listeVHDL){
			if(e instanceof VHDLAction){
				listeAction.add((VHDLAction)e);
			}
		}
		return listeAction;
	}

	public ArrayList<VHDLCondition> getVHDLConditions(){
		EList<VHDLElement> listeVHDL = newroot.getComponent().getVHDLElements();
		ArrayList<VHDLCondition> listeCondition = new ArrayList<VHDLCondition>();
		for(VHDLElement e: listeVHDL){
			if(e instanceof VHDLCondition){
				listeCondition.add((VHDLCondition)e);
			}
		}
		return listeCondition;
	}

	public ArrayList<VHDLFunction> getVHDLFunctions(){
		EList<VHDLElement> listeVHDL = newroot.getComponent().getVHDLElements();
		ArrayList<VHDLFunction> listeFunction = new ArrayList<VHDLFunction>();
		for(VHDLElement e: listeVHDL){
			if(e instanceof VHDLFunction){
				listeFunction.add((VHDLFunction)e);
			}
		}
		return listeFunction;
	}

	public ArrayList<VHDLTime> getVHDLTimes(){
		EList<VHDLElement> listeVHDL = newroot.getComponent().getVHDLElements();
		ArrayList<VHDLTime> listeTime = new ArrayList<VHDLTime>();
		for(VHDLElement e: listeVHDL){
			if(e instanceof VHDLTime){
				listeTime.add((VHDLTime)e);
			}
		}
		return listeTime;
	}
	public ArrayList<petriNet.Place> getPlaces(){
		EList<petriNet.PNEntity> pn = newroot.getComponent().getPNStructureObjects();
		ArrayList<petriNet.Place> listeplace = new ArrayList<petriNet.Place>();
		for(PNEntity e : pn){
			if(e instanceof Place){
				listeplace.add((Place) e);
			}
		}
		return listeplace;
	}

	public ArrayList<petriNet.Transition> getTransitions(){
		EList<petriNet.PNEntity> pn = newroot.getComponent().getPNStructureObjects();
		ArrayList<petriNet.Transition> listeTransition = new ArrayList<petriNet.Transition>();
		for(PNEntity e : pn){
			if(e instanceof Transition){
				listeTransition.add((petriNet.Transition)e);
			}
		}
		return listeTransition;
	}

	/**
	 * save newcomp in file
	 * @throws IOException 
	 */
	public void save() throws IOException{
		newres.getContents().add(newroot);
		newres.save(null);
	}

	/**
	 * set list of InstanceContainerName 
	 * @param listofinstance
	 * @param projetname
	 */
	public void setInstanceContainer(ArrayList<String> listofinstance, String projetname){
		for(String ic : listofinstance){
			InstanceContainerName instancecontainer = RootFactory.eINSTANCE.createInstanceContainerName();
			instancecontainer.setName(ic);
			instancecontainer.setProjectName(projetname);
			newroot.getComponent().getListOfInstances().add(instancecontainer);
		}
		newres.getContents().clear();
		newres.getContents().add(newroot);
		try {
			newres.save(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}


