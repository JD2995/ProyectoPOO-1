import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

public class VentanaPer {
	//DECLARACIÓN DE ATRIBUTOS
	private Personas Lista[]= new Personas[120];
	private String nombreLista []= new String[120];	//Vector con los nombres de la lista
	private int indLista[]= new int[120];	//Vector con el índice de los nombres de Lista
	private JButton botonAgregar, botonEditar, botonEliminar;
	private JList list;
	private int cantPers;	//Cantidad de personas en el registro
	
	//DECLARACIÓN DE MÉTODOS
	
	/* Descripción: Función que ordena alfabeticamente una lista
	 * Entrada: Número de atributo a obtener
	 * 			Número de elementos en el vector
	 * Salida: Ninguna */
	void ordenarLista(int cant){
		int i=1;
		int indAux;
		String aux;
		
		if(cant == 0 || cant == 1){ return;}
		while(i<cant){
			if(i == 0){
				i++;
			}
			//Si el elemento anterior tiene un caracter menor que el elemento actual
			else if(nombreLista[i].charAt(0)< nombreLista[i-1].charAt(0) && i!= 0){
				//Cambia posiciones en lista con nombres
				aux= nombreLista[i];
				nombreLista[i]= nombreLista[i-1];
				nombreLista[i-1]= aux;
				//Cambia posiciones en lista con índices
				indAux= indLista[i];
				indLista[i]= indLista[i-1];
				indLista[i-1]= indAux;
				i--;
			}
			else{
				i++;
			}
		}
	}
	
	private void cargarNombre(){
		Personas temp= new Personas();
		int i= 0;
		while(true){
			//Obtiene en las clases sus atributos hasta que ya no encuentra
			//más en disco
			if(temp.Obtener(i+1, "Reg_Pers.txt") == false){break;}
			Lista[i]=temp;
			indLista[i]= i;
			nombreLista[i]= temp.getNombre();
			i++;
		}
		cantPers= i;
	}
	
	
	/*Descripción: Función que maneja la ventana para agregar una persona
	 * Entrada: Ninguna
	 * Salida:Ninguna
	 */
	private void ventanaPersona(){
		JFrame vAgregar= new JFrame("Agregar Persona");		//Creación de la ventana
		JTextField espacio1,espacio2,espacio3,espacio4,espacio5;	//Inicialización de los cuadros de entrada de texto
		JLabel label1,label2,label3,label4,label5;
		JButton boton;
		
		//Colocación de los labels
		label1= new JLabel("Nombre:");
		label1.setBounds(15, 15,100,30);
		label2= new JLabel("Apellido 1:");
		label2.setBounds(15,50,100,30);
		label3= new JLabel("Apellido 2:");
		label3.setBounds(15,85,100,30);
		label4= new JLabel("Teléfono: ");
		label4.setBounds(15,120,100,30);
		label5= new JLabel("E-mail:");
		label5.setBounds(15,155,100,30);
		//Colocación de los de cuadros de entrada
		espacio1= new JTextField();
		espacio1.setBounds(100,20,200,20);
		espacio2= new JTextField();
		espacio2.setBounds(100,55,200,20);
		espacio3= new JTextField();
		espacio3.setBounds(100,90,200,20);
		espacio4= new JTextField();
		espacio4.setBounds(100,125,200,20);
		espacio5= new JTextField();
		espacio5.setBounds(100,160,200,20);
		//Colocación y acción del botón
		boton= new JButton("Agregar");
		boton.setBounds(100,200,125,25);
		boton.setMnemonic(KeyEvent.VK_I);
		boton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Personas humano= new Personas();	//Instancia
				String palabra;
				
				palabra= espacio1.getText();
				humano.setNombre(palabra);
				palabra= espacio2.getText();
				humano.setApellido1(palabra);
				palabra= espacio3.getText();
				humano.setApellido2(palabra);
				palabra= espacio4.getText();
				humano.setTelefono(Integer.parseInt(palabra));
				palabra= espacio5.getText();
				humano.setCorreo(palabra);
				humano.setCategoria(0);
				humano.Agregar("Reg_Pers.txt");
				vAgregar.dispose();
				cargarNombre();
				ordenarLista(cantPers);
				list.updateUI();
				return;
			}
		});
		//Se agregan los elementos
		vAgregar.setLayout(null);
		vAgregar.add(label1);
		vAgregar.add(label2);
		vAgregar.add(label3);
		vAgregar.add(label4);
		vAgregar.add(label5);
		vAgregar.add(espacio1);
		vAgregar.add(espacio2);
		vAgregar.add(espacio3);
		vAgregar.add(espacio4);
		vAgregar.add(espacio5);
		vAgregar.add(boton);
		
		vAgregar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	//Finalizar la tarea cuando se cierre la ventana
		vAgregar.setSize(400,400);	//Tamaño de la ventana
		vAgregar.setVisible(true);
		vAgregar.setResizable(false);
	}
	
	/*Descripción: Función que coloca los atributos de los botones
	 * Entrada: Ninguna
	 * Salida: Ninguna
	 */
	private void colocarBotones(){
		botonAgregar= new JButton("Agregar");
		botonAgregar.setBounds(10,130,125,25);
		botonAgregar.setMnemonic(KeyEvent.VK_I);
		botonAgregar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				ventanaPersona();
			}
		});
		botonEditar= new JButton("Editar");
		botonEditar.setBounds(10,161,125,25);
		botonEliminar= new JButton("Eliminar");
		botonEliminar.setBounds(10,192,125,25);
	}
	
	public VentanaPer(){
		JFrame ventana= new JFrame("Lista de Personas");
		
		colocarBotones();
		cargarNombre();
		ordenarLista(cantPers);
		list= new JList(nombreLista);
		list.setSelectedIndex(0);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);		
		list.setBounds(150,130,500,260);
		list.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		ventana.setLayout(null);
		ventana.add(botonAgregar);
		ventana.add(botonEditar);
		ventana.add(botonEliminar);
		ventana.add(list);
		
		ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	//Finalizar la tarea cuando se cierre la ventana
		ventana.setSize(675,450);	//Tamaño de la ventana
		ventana.setVisible(true);
		ventana.setResizable(false);
	}
}
