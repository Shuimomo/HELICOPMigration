package migration;

import java.util.ArrayList;

/**
 * un par composant;
 * editor
 * @author shui
 */
public class ListInstanceContainer {
	private String compname;
	private ArrayList<String> listofinstance;

	public ListInstanceContainer(String instanceofcomp) {
		compname = instanceofcomp;
		listofinstance = new ArrayList<String>();
	}

	public String getCompName() {
		return compname;
	}

	public void addInstance(String comp) {
		listofinstance.add(comp);
	}

	public void setInstanceContainer(String projetname, ArrayList<NouveauComposant> listecomp) {
		for(NouveauComposant c : listecomp){
			if(c.getName().equals(compname)){
				c.setInstanceContainer(listofinstance,projetname);	
			}
		}
		
	}

}
