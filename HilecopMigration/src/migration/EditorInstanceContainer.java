/**
 * un par projet
 */
package migration;

import java.util.ArrayList;

public class EditorInstanceContainer {
	private String projet;
	private ArrayList<ListOfInstanceContainer> history;
	
	public EditorInstanceContainer(String projetname){
		projet = projetname;
		history = new ArrayList<ListOfInstanceContainer>();
	}

	public String getProjetName(){
		return projet;
	}

	public void add(String instanceofcomp, String comp) {
		Boolean exist = false;
		for(ListOfInstanceContainer e : history){
			if(e.getCompName().equals(instanceofcomp)){
				e.addInstance(comp);
				exist = true;
			}
		}
		if(!exist){
			ListOfInstanceContainer ic = new ListOfInstanceContainer(instanceofcomp);
			ic.addInstance(comp);
			history.add(ic);
		}
	}
	
	public void setallInstanceContainer(ArrayList<NouveauComposant> listecomp){
		for(ListOfInstanceContainer ic : history){
			ic.setInstanceContainer(projet,listecomp);
			}		
		}
	}

