/**un par composant*/
package migration;

import java.util.ArrayList;

public class ListOfInstanceContainer {
	private String compname;
	private ArrayList<String> listofinstance;

	public ListOfInstanceContainer(String instanceofcomp) {
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
