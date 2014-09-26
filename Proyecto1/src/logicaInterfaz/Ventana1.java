package logicaInterfaz;
import java.awt.event.*;
import javax.swing.*;
import logicaPrograma.Prestamo;

public class Ventana1{
	private JButton Bpersonas, Blibros, Bprestamos, Btop, Brango;
	private boolean ventanaAbierta= false;
	public static boolean principio= false;
	String [] opciones= {"Libros","Revistas","Peliculas"};
	@SuppressWarnings({ "unchecked", "rawtypes" })
	JComboBox options= new JComboBox(opciones);
	
	private void consPers(){
		@SuppressWarnings("unused")
		VentanaPer ventana2= new VentanaPer();
		ventanaAbierta= false;
	}
	
	private void consArtics(){
		@SuppressWarnings("unused")
		VentanaArticulos ventana2= new VentanaArticulos(options.getSelectedIndex());
		ventanaAbierta= false;
	}
	
	private void consPrest(){
		@SuppressWarnings("unused")
		VentanaPrestamos ventana2= new VentanaPrestamos();
		ventanaAbierta= false;
	}
	
	private void consTop(){
		@SuppressWarnings("unused")
		VentanaTop ventana2= new VentanaTop();
		ventanaAbierta= false;
	}
	
	public Ventana1(String title){
		JFrame ventana= new JFrame(title);
		JLabel label1= new JLabel("Tipo de Articulo:");
		Prestamo prest= new Prestamo();
		
		//Definición de los botones
		Bpersonas= new JButton("Consultar Personas");
		ImageIcon ImagePer= new ImageIcon("Personas.gif");
		Blibros= new JButton("Consultar Artículos");
		ImageIcon ImageLibr= new ImageIcon("Libros.gif");
		Bprestamos= new JButton("Consultar Préstamos");
		ImageIcon ImagePrest= new ImageIcon("Prestamos.gif");
		Btop= new JButton("Consultar Top de préstamos");
		ImageIcon ImageTop= new ImageIcon("Top.gif");
		Brango= new JButton("Libros prestados dentro de un rango");
		ImageIcon ImageRango= new ImageIcon("Rango.gif");
		
		options.setSelectedIndex(0);
		options.setBounds(420,65,100,25);
		label1.setBounds(320,65,100,25);
		
		//Colocación de características de los botones
		Bpersonas.setBounds(20, 100, 500, 50);
		Bpersonas.setIcon(ImagePer);
		Bpersonas.setMnemonic(KeyEvent.VK_I);
		Bpersonas.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(ventanaAbierta==false){
					ventanaAbierta= true;
					consPers();
				}
			}
		});
		Blibros.setBounds(20,152,500,50);
		Blibros.setIcon(ImageLibr);
		Blibros.setMnemonic(KeyEvent.VK_I);
		Blibros.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(ventanaAbierta==false){
					ventanaAbierta= true;
					consArtics();
				}
			}
		});
		Bprestamos.setBounds(20,204,500,50);
		Bprestamos.setIcon(ImagePrest);
		Btop.setBounds(20,256,500,50);
		Btop.setIcon(ImageTop);
		Btop.setMnemonic(KeyEvent.VK_I);
		Btop.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(ventanaAbierta==false){
					ventanaAbierta= true;
					consTop();
				}
			}
		});
		Brango.setBounds(20,308,500,50);
		Bprestamos.setMnemonic(KeyEvent.VK_I);
		Bprestamos.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(ventanaAbierta==false){
					ventanaAbierta= true;
					consPrest();
				}
			}
		});
		Brango.setIcon(ImageRango);
		ventana.setLayout(null);
		ventana.add(Blibros);
		ventana.add(Bpersonas);
		ventana.add(Bprestamos);
		ventana.add(Btop);
		ventana.add(Brango);
		ventana.add(options);
		ventana.add(label1);
		
		ventana.setLocation(500, 200);
		
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//Finalizar la tarea cuando se cierre la ventana
		ventana.setSize(545,405);	//Tamaño de la ventana
		ventana.setVisible(true);
		ventana.setResizable(false);
		
		//Si existe al menos un préstamo
		if(prest.Existe_p(1, "Prestamos.txt")){
			principio= true;
			new VentanaPrestamos();
		}
	}
}

