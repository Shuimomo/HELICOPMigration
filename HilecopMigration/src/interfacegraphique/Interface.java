/**
 * @author Dhivya
 */
package interfacegraphique;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import hilecopComponent.HilecopComponentDesignFile;
import migration.MigrationDuProjet;
import root.HilecopRoot;
import test.Test;


@SuppressWarnings("serial")
public class Interface extends JFrame implements ActionListener {

	private JFrame frame;
	private JFileChooser fc=new JFileChooser();
	private JTextField text1=new JTextField();
	private JTextField text2=new JTextField();
	private JLabel label1;
	private JLabel label2;
	private JButton b1=new JButton("Browse");
	private JButton b2=new JButton("Browse");
	private JButton migrer=new JButton("Migrate");
	private JTextPane jt;
	private JScrollPane scroll;
	
	private ArrayList<File> listeFichier;
	
	FileExplorer f = new FileExplorer();
	
	/**
	 * constructor
	 */
	public Interface()
	{
		frame = new JFrame();
		//frame.getContentPane().setBackground(SystemColor.yellow);
		frame.getContentPane().setForeground(SystemColor.desktop);
		frame.setSize(500,350);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setTitle("Hilecop Migration Tool");
		frame.getContentPane().setLayout(null );

		label1=new JLabel("choose an old project location");
		label1.setBounds(74, 40, 200, 14);
		frame.getContentPane().add(label1);

		label2 = new JLabel("choose a new project location");
		label2.setBounds(74, 102, 200, 14);
		frame.getContentPane().add(label2);
		frame.setIconImage(new ImageIcon("img/icon1.png").getImage());

		text1.setBounds(74, 62, 200, 20);
		frame.getContentPane().add(text1);
		text1.setColumns(10);

		b1.setBounds(300,62,100,25);
		frame.getContentPane().add(b1);

		b1.addActionListener(this);

		text2.setBounds(74, 120, 200, 20);

		frame.getContentPane().add(text2);
		text2.setColumns(10);

		b2.setBounds(300,120,100,25);
		frame.getContentPane().add(b2);
		b2.addActionListener(this);

		migrer.setBounds(150,200,100,25);
		frame.getContentPane().add(migrer);
		migrer.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					migration();
				} catch (IOException e1) {
					e1.printStackTrace();
				}}
		});
		
		 jt=new JTextPane();
		jt.setBounds(50,200,200,200);
		
		 scroll=new JScrollPane(jt,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setBounds(150, 252, 400, 300);
		scroll.setVisible(false);
		frame.getContentPane().add(scroll);

		frame.setVisible(true);
	}


	/***getters and setters***/
	public JFileChooser getChemin() {
		return fc;
	}
	public JTextField getText1() {
		return text1;
	}
	public JTextField getText2() {
		return text2;
	}
	public JLabel getLabel1() {
		return label1;
	}
	public JLabel getLabel2() {
		return label2;
	}
	public JButton getB1() {
		return b1;
	}
	public JButton getB2() {
		return b2;
	}
	public JButton getStart() {
		return migrer;
	}

	public void setText1(String s) {
		text1.setText(s);
	}
	public void setText2(String s) {
		text2.setText(s);
	}

	/***Actions of buttons b1 and b2***/
	public void actionPerformed(ActionEvent e){
		

		int input = 0;
		
		fc.setCurrentDirectory(new java.io.File("C:/"));
		fc.setDialogTitle("Choose a file");
		fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		int result = fc.showOpenDialog(b1);
		if ( result == JFileChooser.APPROVE_OPTION){
			input=JOptionPane.showConfirmDialog(null, fc.getSelectedFile().getAbsolutePath(),"confirm", JOptionPane.YES_NO_OPTION);
		
			if(input==0){
				if(e.getSource()==b1){
					
					
					setText1(fc.getSelectedFile().getAbsolutePath());
					
					
					// attente du curseur durant l'exploration des dossiers
					 
					Thread thread=new Thread()
					{public void run()
						{
								//b1.setEnabled(false);
								for (Component c : frame.getContentPane().getComponents()) c.setEnabled(false);
								frame.getContentPane().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
								f = new FileExplorer();
								f.parcours(backToSlash(text1.getText()));
								
								System.out.println("Ruuning...");
							
						}
						
					};
					//debut du thread
					thread.start();
					
					//Fin de thread
					try {
						thread.join();
						//b1.setEnabled(true);
						for (Component c : frame.getContentPane().getComponents()) c.setEnabled(true);
						frame.getContentPane().setCursor(null);
						System.out.println("done!");
						
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
					
					frame.setSize(600, 600);
					scroll.setVisible(true);
					jt.removeAll();
					jt.validate();
					jt.repaint();
					jt=new JTextPane();
					
					jt.setBounds(50,200,200,200);
					JScrollPane scroll=new JScrollPane(jt,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
					scroll.setBounds(150, 252, 400, 300);
					
					frame.getContentPane().add(scroll);
					
//					listeFichier=f.parcours(backToSlash(text1.getText()));
					listeFichier = f.getListeFichiers();
					AddTextJpanel(jt, listeFichier);
					
					frame.setVisible(true);
				}
				else{
					setText2(fc.getSelectedFile().getAbsolutePath());
				}
			}
			else System.out.println("CANCEL");
		
		}
		else {
			System.out.println("Cancel browse");
		}
		
				
		
	}//end actionperformed
	
	

	/**
	 * action for button "migrate"
	 * @throws IOException 
	 */
	public void migration() throws IOException{
		if(text1.getText().equals("")){
			JOptionPane.showMessageDialog(null,"old project location is Empty");
		}
		else if(text2.getText().equals("")){
			JOptionPane.showMessageDialog(null,"new project location is Empty");
		}
		else{
			String locate = text2.getText();
			/**
			 * TODO mieux structurer l'utilisation de f
			 */
			ArrayList<File> liste_vhd = f.getVhd();
			ArrayList<File> liste_hilecopcomponent = f.getHilecopComponent();
			MigrationDuProjet migprojet = new MigrationDuProjet(locate);
			migprojet.migrationHILECOP(liste_vhd,liste_hilecopcomponent);
			JOptionPane.showMessageDialog(null,"Migration is done!");
			
		Test test = new Test();
			String project_name ="";
			for(File fic : f.getListeFichiers()) {
				if(fic.isDirectory()) {
					project_name = fic.getName();
				}
				else if (fic.isFile()) {
					HilecopComponentDesignFile designFile = test.read(fic.getAbsolutePath());
					System.out.println(fic.getName() + " from project : "+project_name);
					File fic_root = new File(locate+"\\"+project_name+"\\"+fic.getName().replaceFirst(".hilecopcomponent", ".root"));
					if(fic_root.exists()) {
						System.out.println("Test du fichier : "+fic_root.getName());
						HilecopRoot root = test.open(fic_root.getAbsolutePath());
						test.TestField(root, designFile);
						test.TestVHDLElement(root, designFile);
						test.TestPNStructure(root, designFile);
						test.TestInstance(root, designFile);
						test.TestConnection(root, designFile);
						test.ErrorOutput(test.getErreurList(),locate);
					}
					
				}
			}
			
			JOptionPane.showMessageDialog(null,"Validation is done!");
		}
	}




	public String backToSlash(String s) {
		return s.replace("\\", "/");
	}


/**
 * Ajout de la contenue de ArrayList au JTextPane
 * @param jta
 * @param f
 */
	public void AddTextJpanel(JTextPane jta, ArrayList<File>f){	
	
		//System.out.println("times clicked in methode "+clicked);
		if(f.isEmpty())
		{
			appendToPane(jta,"Expected file is not found in this Directory",Color.BLACK);
		}
		else
		{
		for(int i=0;i<f.size();i++)
		{
			if(f.get(i).isDirectory())
			{
				appendToPane(jta,"[projet]: "+f.get(i).getName()+"\n",Color.RED);
			}
			else
			{appendToPane(jta,"\t"+f.get(i).getName()+"\n",Color.blue);}

		}
		}
		
	}
	
	/**
	 *Ajoute couleur aux lignes choisies
	 * @param tp
	 * @param msg
	 * @param c
	 */
	private void appendToPane(JTextPane tp, String msg, Color c)
    {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

      
       aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
    }//fin de la methode appendToPane
}//fin de la classe

