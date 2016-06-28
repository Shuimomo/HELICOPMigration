/**
 * @author Dhivya
 */
package interfacegraphique;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class FileExplorer{

	public ArrayList<File> liste_fichiers;

	public FileExplorer()
	{
		liste_fichiers = new ArrayList<File>();
	}

	
	/**
	 *parcours de repertoire
	 * @param path
	 * @return path de repetoires et des fichiers .helicopcomponent
	 */
	public ArrayList<File> parcours(String path)
	{	
		File file=new File (path);
		File[] Lrep=file.listFiles();
		int size=Lrep.length;
		for(int i=0;i<size;i++)
		{
			if(Lrep[i].isDirectory()==true)
			{
				parcours(Lrep[i].getAbsolutePath());
				int j=0;
				File[] Lfiles=Lrep[i].listFiles();
				if(Lfiles!=null){
					for(j=0;j<Lfiles.length;j++){
						if(Lfiles[j].getName().contains(".project")==true){
							if(test_projet_hilecop(Lfiles[j])){
								//test de contenu
								//System.out.println("repertoire:  "+Lrep[i].getName());

								liste_fichiers.add(Lrep[i]);
								for(int k=0;k<Lfiles.length;k++){
									if(Lfiles[k].getName().endsWith(".hilecopcomponent") && !(Lfiles[k].getName().equals(".metadata") )){
										System.out.println(Lfiles[k].getAbsolutePath());
										liste_fichiers.add(Lfiles[k]);	
									}
								}
							}
						}
					}
				}
			}
		}
		return liste_fichiers;
	}
	

	/**
	 * test de dossier s'il contient projet helicop
	 * @param File
	 * @ return true s'il le contient false sinon
	 * 
	 */
	private boolean test_projet_hilecop(File file) {
		BufferedReader reader;
		try
		{
			@SuppressWarnings("resource")
			InputStream is=new FileInputStream(file);
			reader=new BufferedReader(new InputStreamReader(is));

			String line=reader.readLine();
			while(line!=null)
			{

				if(line.contains("fr.demar.hilecop.component.editor.HilecopNature"))
				{
					return true;
				}
				line=reader.readLine();
			}
		}
		catch(IOException e)
		{
			//e.printStackTrace();
		}
		return false;
	}

	public void afficheArrayList(ArrayList<String> v)
	{
		for(int d=0;d<v.size();d++)
		{
			System.out.println(v.get(d));

		}
	}

}
