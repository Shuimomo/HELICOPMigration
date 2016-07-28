package migration;

import java.io.File;
import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.gmf.runtime.diagram.core.services.ViewService;
import org.eclipse.gmf.runtime.notation.Diagram;

import hilecopComponent.HilecopComponentDesignFile;
import root.HilecopRoot;

public class GererDiagram {
	private String fileDiagram;
	private String fileDiagramBehavior;
	private HilecopRoot newroot;

	public GererDiagram(String path, String name, HilecopRoot root){
		fileDiagram = path + "\\" + name+".interface_diagram";
		fileDiagramBehavior = path + "\\" + name +".behavior_diagram";
		newroot = root;
		System.out.println("Begin DiagramBehavior for "+name);
	}

	
	public void createGMFDiagram() {
		File file = new File(fileDiagram);
		try {
			file.createNewFile();
			URI newURI = URI.createFileURI(fileDiagram);
			ResourceSet rs = new ResourceSetImpl();
			Resource r_interface = rs.createResource(newURI);
			System.out.println(rootInterface.diagram.edit.parts.HilecopRootEditPart.MODEL_ID);
			System.out.println(rootInterface.diagram.part.RootDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT);
			System.out.println(newroot.getComponent());
			System.out.println(ViewService.getInstance());
			Diagram diagram = ViewService.createDiagram(newroot,
					rootInterface.diagram.edit.parts.HilecopRootEditPart.MODEL_ID,
					rootInterface.diagram.part.RootDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT);
			System.out.println("Diagram : "+diagram);
			System.out.println("R_interface :" +r_interface);
			r_interface.getContents().add(diagram);
			r_interface.save(rootInterface.diagram.part.RootDiagramEditorUtil.getSaveOptions());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void createGMFDiagramBehavior() {
		File file = new File(fileDiagramBehavior);
		try {
			file.createNewFile();
			URI newURI = URI.createFileURI(fileDiagramBehavior);
			ResourceSet rs = new ResourceSetImpl();
			Resource r_behavior = rs.createResource(newURI);
			//System.out.println(rootBehavior.diagram.part.RootDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT);
			//System.out.println(newroot.getComponent());
			//System.out.println(ViewService.getInstance());
			System.out.println(newroot.getComponent().getName());
			System.out.println(newroot.getComponent().getFields().size());
			Diagram diagram = ViewService.createDiagram(newroot.getComponent(),
					rootBehavior.diagram.edit.parts.HilecopComponentEditPart.MODEL_ID,
					rootBehavior.diagram.part.RootDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT);
			System.out.println("Diagram : "+diagram);
			System.out.println("R_interface :" +r_behavior);
			System.out.println(diagram.getChildren().size());
			r_behavior.getContents().add(diagram);
			r_behavior.save(rootBehavior.diagram.part.RootDiagramEditorUtil.getSaveOptions());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void readDiagramBehavior(String path){
		ResourceSet rs = new ResourceSetImpl();
		URI uri = URI.createFileURI(path);
		Resource r_behavior = rs.getResource(uri, true);
		Diagram designfile = (Diagram) r_behavior.getContents().get(0);
		
	}
}
