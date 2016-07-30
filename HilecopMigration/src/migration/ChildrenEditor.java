package migration;

import org.eclipse.emf.common.util.EList;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;

import field.Field;
import hilecopComponent.Port;
import root.HilecopRoot;

/**
 * resoudre le probleme de la position du diagram_behavior
 * pour chaque diagram
 * @author shui
 */
public class ChildrenEditor {
	private Diagram newdiagram;
	private Diagram olddiagram;
	private HilecopRoot newroot;

	/**
	 * constructor
	 * @param diag-new
	 * @param diagram1-old
	 * @param root
	 */
	public ChildrenEditor(Diagram diag, Diagram diagram1, HilecopRoot root){
		newdiagram = diag;
		olddiagram = diagram1;
		newroot = root;
	}

	public Diagram getnewDiagram(){
		renewDiagram();
		return newdiagram;
	}

	/**
	 * change positions
	 */
	public void renewDiagram(){
		@SuppressWarnings("rawtypes")
		EList liste_children = olddiagram.getChildren();

		//TODO if child not exist in newroot?
		for(Object e : liste_children){
			View child = (View) e;
			View newchild = renewChild(child);
			newdiagram.insertChild(newchild);
		}
	}

	private View renewChild(View child) {

		//TODO ¨¤ compl¨¨ter pour chaque cas
		
		if(child.getElement() instanceof hilecopComponent.Port){
			Port port1 = (Port) child.getElement();
			
			//TODO si besoin, ajoute getPorts()...tous les get() au NouveauComposant, et utilise newcomp ici
			for(Field e : newroot.getComponent().getFields()){
				if(e instanceof field.VHDLPort){
					if(e.getName().equals(port1.getName())){
						child.setElement(e);
					}
				}
			}
			child.setType("2002");
		}
		return child;
	}
}
