/**
 * @author shui
 */
package migration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MigrationDuProjet {
	public static ArrayList<String> errors;
	private String locate;
	private ArrayList<EditorInstanceContainer> listeEIC;
	private ArrayList<NouveauComposant> listecomp;

	public MigrationDuProjet(String lo){
		locate = lo;
		listeEIC = new ArrayList<EditorInstanceContainer>();
		listecomp = new ArrayList<NouveauComposant>();
		errors = new ArrayList<String>();
	}

	/**
	 * la migration du projet(s)
	 * @param liste_hilecopcomponent 
	 * @param liste_vhd 
	 * @throws IOException
	 */
	public void migrationHILECOP(ArrayList<File> liste_vhd, ArrayList<File> liste_hilecopcomponent) throws IOException{
		/* create an editor for each projet */
		ArrayList<String> listeprojet = new ArrayList<String>();
		Boolean exist = false;
		for(int i=0;i<liste_hilecopcomponent.size();i++){
			String projetname = liste_hilecopcomponent.get(i).getParentFile().getName();
			for(String e : listeprojet){
				if(e.equals(projetname)){
					exist = true;
				}
			}
			if(!exist){
				listeprojet.add(projetname);
			}
		}
		for(int i=0;i<listeprojet.size();i++){
			String nameofprojet = listeprojet.get(i);
			EditorInstanceContainer c = new EditorInstanceContainer(nameofprojet);
			listeEIC.add(c);
		}/*end*/

		/* migration du composant(root) */
		for(int i=0;i<liste_hilecopcomponent.size();i++){
			String projetname = liste_hilecopcomponent.get(i).getParentFile().getName();
			String pathnew = locate + "\\"+projetname;
			File f1=new File(pathnew);
			f1.mkdir();
			EditorInstanceContainer historyinstance = getEIC(projetname);
			if(historyinstance!=null){
				migrationComposant(pathnew, liste_hilecopcomponent.get(i),historyinstance);
			}
			//TODO else?
		}

		for(EditorInstanceContainer e : listeEIC){
			e.setallInstanceContainer(listecomp);
		}

		for(int i=0;i<liste_vhd.size();i++){
			String pathnew = locate + "\\"+liste_vhd.get(i).getParentFile().getName();
			String name = liste_vhd.get(i).getName();
			File f1=new File(pathnew);
			f1.mkdir();
			String pathancien = liste_vhd.get(i).getAbsolutePath();
			migrationVHD(pathancien, pathnew,name);
		}
		
		System.out.println(errors);
	}

	/**
	 * find the editorInstanceContainer for this project(folder)
	 * @param projetname
	 * @return
	 */
	private EditorInstanceContainer getEIC(String projetname) {
		for(EditorInstanceContainer e : listeEIC){
			if(e.getProjetName().equals(projetname)){
				return e;
			}
		}
		return null;
	}

	private void migrationComposant(String pathnew, File fileancien, EditorInstanceContainer historyinstance) throws IOException{
		MigrationDuComposant migtool = new MigrationDuComposant(pathnew,fileancien,historyinstance);
		migtool.migeration();
		migtool.save();
		listecomp.add(migtool.getNewComp());
	}

	private void migrationVHD(String path1, String path2, String name) throws IOException{
		@SuppressWarnings("resource")
		InputStream is = new FileInputStream (path1);
		OutputStream os=null;
		if(name.startsWith(".")){
			name = name.substring(1);
		}
		String filename = path2 + "\\" + name.replaceAll(".vhd", ".script_vhd");
		File file = new File(filename);
		file.createNewFile();
		try{
			os=new FileOutputStream(file);
			byte buffer[]=new byte[4*1024];


			int len = 0;
			while((len = is.read(buffer)) != -1){ 
				os.write(buffer,0,len);
			}

			os.flush();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try{
				os.close();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	/*
	private void migrationInterface(){
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getPackageRegistry().put(root.RootPackage.eNS_URI,root.RootPackage.eINSTANCE);
		Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("root",new XMIResourceFactoryImpl());
		String name = "0";
		String filename = path2 + "\\" + name + ".script_vhd";
		File file = new File(filename);
		file.createNewFile();
		URI newURI = URI.createFileURI(filename);
		Resource newres = resourceSet.createResource(newURI);

	//	Diagram 
	}
	 */
}
