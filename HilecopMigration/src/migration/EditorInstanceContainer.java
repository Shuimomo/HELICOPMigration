package migration;

import java.util.ArrayList;

/**
 * un par projet. contient plusieurs ListInstanceContainer
 * @author shui
 */
public class EditorInstanceContainer {
	private String projet;
	private ArrayList<ListInstanceContainer> history;
	
	public EditorInstanceContainer(String projetname){
		projet = projetname;
		history = new ArrayList<ListInstanceContainer>();
	}

	public String getProjetName(){
		return projet;
	}

	public void add(String instanceofcomp, String comp) {
		Boolean exist = false;
		for(ListInstanceContainer e : history){
			if(e.getCompName().equals(instanceofcomp)){
				e.addInstance(comp);
				exist = true;
			}
		}
		if(!exist){
			ListInstanceContainer ic = new ListInstanceContainer(instanceofcomp);
			ic.addInstance(comp);
			history.add(ic);
		}
	}
	
	public void setallInstanceContainer(ArrayList<NouveauComposant> listecomp){
		for(ListInstanceContainer ic : history){
			ic.setInstanceContainer(projet,listecomp);
			}		
		}
	}

