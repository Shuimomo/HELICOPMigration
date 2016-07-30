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
	 * @param liste_behavior 
	 * @throws IOException
	 */
	public void migrationHILECOP(ArrayList<File> liste_vhd, ArrayList<File> liste_hilecopcomponent, ArrayList<File> liste_behavior) throws IOException{
		/* creer an editor for each projet */
		ArrayList<String> listeprojet = new ArrayList<String>();
		Boolean exist = false;
		for(File f : liste_hilecopcomponent){
			String projetname = f.getParentFile().getName();
			for(String e : listeprojet){
				if(e.equals(projetname)){
					exist = true;
				}
			}
			if(!exist){
				listeprojet.add(projetname);
			}
		}
		for(String nameofprojet : listeprojet){
			EditorInstanceContainer c = new EditorInstanceContainer(nameofprojet);
			listeEIC.add(c);
		}/*end*/

		for(File f : liste_hilecopcomponent){
			
			String projetname = f.getParentFile().getName();
			String pathnew = locate + "\\"+projetname;
			File f1=new File(pathnew);
			f1.mkdir();
			EditorInstanceContainer historyinstance = getEIC(projetname);
			if(historyinstance!=null){
				/* migrer le composant(root) */
				NouveauComposant newcomp = migrationComposant(pathnew, f ,historyinstance);
				
				/* migrer les diagrammes */
				File f_behavior = getBehavior(newcomp.getName(),liste_behavior);
				gererdiagram(pathnew, newcomp,f_behavior);
				//TODO si besoin, ajouter fichier xmi f_instance
			}
		}

		/* gerer InstanceContainer pour tous */
		for(EditorInstanceContainer e : listeEIC){
			e.setallInstanceContainer(listecomp);
		}

		/* migrer la fichier vhd */
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

	/**
	 * migrate .root by calling MigrationDuComposant
	 * @param pathnew
	 * @param fileancien
	 * @param historyinstance
	 * @return
	 * @throws IOException
	 */
	private NouveauComposant migrationComposant(String pathnew, File fileancien, EditorInstanceContainer historyinstance) throws IOException{
		MigrationDuComposant migtool = new MigrationDuComposant(pathnew,fileancien,historyinstance);
		migtool.migeration();
		migtool.save();
		listecomp.add(migtool.getNewComp());
		return migtool.getNewComp();
	}

	/**
	 * find file .hilecopcomponentB_diagram for component given
	 * @param compname
	 * @param liste_behavior
	 * @return
	 */
	private File getBehavior(String compname, ArrayList<File> liste_behavior){
		String subname = compname+".hilecopcomponentB_diagram";
		//String mocname = "."+name+".hilecopcomponentB_diagram";
		//TODO faut verifier
		
		for(File f : liste_behavior){
			if(f.getName().contains(subname)){
				return f;
			}
		}
		return null;
	}
	/**
	 * migrate diagrams by calling GererDiagram
	 * @param newcomp
	 * @param f_behavior
	 */
	private void gererdiagram(String path, NouveauComposant newcomp, File f_behavior){
		DiagramTool newdiag = new DiagramTool(path, newcomp, f_behavior);
		newdiag.createGMFDiagram();
		newdiag.createGMFDiagramBehavior();		
	}

	/**
	 * migrate file vhd
	 * @param path1
	 * @param path2
	 * @param name
	 * @throws IOException
	 */
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
}
