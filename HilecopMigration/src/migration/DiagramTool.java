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
import org.eclipse.gmf.runtime.notation.NotationPackage;
import root.HilecopRoot;

public class DiagramTool {
	private String fileDiagram;
	private String fileDiagramBehavior;
	private HilecopRoot newroot;
	private File ancienbehavior;
	
	public DiagramTool(String path, NouveauComposant newcomp, File f_behavior){
		fileDiagram = path + "\\" + newcomp.getName()+".interface_diagram";
		fileDiagramBehavior = path + "\\" + newcomp.getName() +".behavior_diagram";
		newroot = newcomp.getRoot();
		ancienbehavior = f_behavior;
		System.out.println("Begin creating diagrams for "+newcomp.getName());
	}

	/**
	 * create interface diagram
	 */
	public void createGMFDiagram() {
		File file = new File(fileDiagram);
		try {
			file.createNewFile();
			URI newURI = URI.createFileURI(fileDiagram);
			ResourceSet rs = new ResourceSetImpl();
			Resource r_interface = rs.createResource(newURI);
			//System.out.println(rootInterface.diagram.edit.parts.HilecopRootEditPart.MODEL_ID);
			//System.out.println(rootInterface.diagram.part.RootDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT);
			Diagram diagram = ViewService.createDiagram(newroot,
					rootInterface.diagram.edit.parts.HilecopRootEditPart.MODEL_ID,
					rootInterface.diagram.part.RootDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT);
			r_interface.getContents().add(diagram);
			r_interface.save(rootInterface.diagram.part.RootDiagramEditorUtil.getSaveOptions());
			System.out.println("Auto-create Interface Diagram finished");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * create behavior diagram
	 */
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
			Diagram diagram = ViewService.createDiagram(newroot.getComponent(),
					rootBehavior.diagram.edit.parts.HilecopComponentEditPart.MODEL_ID,
					rootBehavior.diagram.part.RootDiagramEditorPlugin.DIAGRAM_PREFERENCES_HINT);
			System.out.println("Auto-create Behavior Diagram finished");
			
			//read ancien fichier behaviordiagram
			String path = ancienbehavior.getAbsolutePath();
			ResourceSet rs1 = new ResourceSetImpl();
			rs1.getPackageRegistry().put(hilecopComponent.HilecopComponentPackage.eNS_URI,hilecopComponent.HilecopComponentPackage.eINSTANCE);
			rs1.getPackageRegistry().put(NotationPackage.eNS_URI, NotationPackage.eINSTANCE);
			URI uri = URI.createFileURI(path);
			rs1.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());
			Resource r_behavior1 = rs1.getResource(uri, true);
			Diagram diagram1 = (Diagram) r_behavior1.getContents().get(0);
			
			ChildrenEditor childrenTool = new ChildrenEditor(diagram,diagram1,newroot);
			
			r_behavior.getContents().add(childrenTool.getnewDiagram());
			System.out.println("Set Position Behavior Diagram finished");
			
			r_behavior.save(rootBehavior.diagram.part.RootDiagramEditorUtil.getSaveOptions());

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
