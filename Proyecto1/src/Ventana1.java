import java.awt.event.*;

import javax.swing.*;

public class Ventana1{
	private JButton Bpersonas, Blibros, Bprestamos, Btop, Brango;
	
	private void Hacer(){
		VentanaPer ventana2= new VentanaPer();
	}
	
	public Ventana1(String title){
		JFrame ventana= new JFrame(title);
		//Definición de los botones
		Bpersonas= new JButton("Consultar Personas");
		ImageIcon ImagePer= new ImageIcon("Personas.gif");
		Blibros= new JButton("Consultar Libros");
		ImageIcon ImageLibr= new ImageIcon("Libros.gif");
		Bprestamos= new JButton("Consultar Préstamos");
		ImageIcon ImagePrest= new ImageIcon("Prestamos.gif");
		Btop= new JButton("Consultar Top de préstamos");
		ImageIcon ImageTop= new ImageIcon("Top.gif");
		Brango= new JButton("Libros prestados dentro de un rango");
		ImageIcon ImageRango= new ImageIcon("Rango.gif");
		
		//Colocación de características de los botones
		Bpersonas.setBounds(20, 100, 500, 50);
		Bpersonas.setIcon(ImagePer);
		Bpersonas.setMnemonic(KeyEvent.VK_I);
		Bpersonas.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Hacer();
			}
		});
		Blibros.setBounds(20,152,500,50);
		Blibros.setIcon(ImageLibr);
		Bprestamos.setBounds(20,204,500,50);
		Bprestamos.setIcon(ImagePrest);
		Btop.setBounds(20,256,500,50);
		Btop.setIcon(ImageTop);
		Brango.setBounds(20,308,500,50);
		Brango.setIcon(ImageRango);
		ventana.setLayout(null);
		ventana.add(Blibros);
		ventana.add(Bpersonas);
		ventana.add(Bprestamos);
		ventana.add(Btop);
		ventana.add(Brango);
		
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//Finalizar la tarea cuando se cierre la ventana
		ventana.setSize(545,405);	//Tamaño de la ventana
		ventana.setVisible(true);
		ventana.setResizable(false);
		
	}
}
