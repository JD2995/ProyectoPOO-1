import java.awt.Color;
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
	
	/*Descripción: Lista que limpia el contenido de la listas nombreLista, indLista y Lista
	 * Entrada: Ninguna
	 * Salida: Ninguna
	 */
	private void limpiarListas(){
		int i=0;
		while(i<120){
			Lista[i]= null;
			i++;
		}
		i=0;
		while(i<120){
			indLista[i]=0;
			i++;
		}
		i=0;
		while(i<120){
			nombreLista[i]= "";
			i++;
		}
	}
	
	/* Descripción: Función que ordena alfabeticamente una lista
	 * Entrada: Número de atributo a obtener
	 * 			Número de elementos en el vector
	 * Salida: Ninguna */
	private void ordenarLista(int cant){
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
		String espacio="                    ";	//Espacio de 20
		limpiarListas();	//Limpia el contenido de las listas
		int i= 0;
		while(true){
			//Obtiene en las clases sus atributos hasta que ya no encuentra
			//más en disco
			if(temp.Obtener(i+1, "Reg_Pers.txt") == false){break;}
			Lista[i]=temp;
			indLista[i]= i;
			nombreLista[i]= temp.getNombre()+" "+temp.getApellido1()+" "+temp.getApellido2();
			i++;
		}
		cantPers= i;
	}
	
	
	/*Descripción: Función que maneja la ventana para agregar una persona
	 * Entrada: Un int, si es 0 significa que va a agregar una nueva personas, sino es el número de persona a editar
	 * Salida: Ninguna
	 */
	private void ventanaPersona(int numPer){
		JFrame vAgregar;		//Creación de la ventana
		JTextField espacio1,espacio2,espacio3,espacio4,espacio5;	//Inicialización de los cuadros de entrada de texto
		JLabel label1,label2,label3,label4,label5;
		JButton boton;
		
		if(numPer==0) vAgregar= new JFrame("Agregar Persona");
		else vAgregar= new JFrame("Editar Persona");
		
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
		if(numPer==0) boton= new JButton("Agregar");
		else boton= new JButton("Editar");
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
				if(numPer==0)humano.Agregar("Reg_Pers.txt");
				else humano.Editar(indLista[numPer]);
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
		vAgregar.setSize(350,270);	//Tamaño de la ventana
		vAgregar.setVisible(true);
		vAgregar.setResizable(false);
	}
	
	/*Descripción: Función que crea la ventana de eliminación de una persona
	 * Entrada: Int número de persona a eliminar
	 * Salida: Ninguna
	 */
	private void ventanaEliminar(int numPer){
		JFrame vEliminar= new JFrame("Eliminar Persona");
		JLabel mensaje= new JLabel("¿Esta seguro de eliminar a la persona del registro?");
		JButton aceptar= new JButton("Aceptar");
		JButton cancelar= new JButton("Cancelar");
		Personas humano= new Personas();
		
		mensaje.setBounds(30,20,300,25);
		aceptar.setBounds(85,50,90,25);
		aceptar.setMnemonic(KeyEvent.VK_I);
		aceptar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				humano.eliminar(indLista[numPer]);
				vEliminar.dispose();
				cantPers--;
				cargarNombre();
				ordenarLista(cantPers);
				list.updateUI();
				return;
			}
		});
		cancelar.setBounds(195,50,90,25);
		cancelar.setMnemonic(KeyEvent.VK_I);
		cancelar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				vEliminar.dispose();
				return;
			}
		});
		
		vEliminar.setLayout(null);
		vEliminar.add(mensaje);
		vEliminar.add(aceptar);
		vEliminar.add(cancelar);
		
		vEliminar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	//Cerrar la ventana
		vEliminar.setSize(360,120);
		vEliminar.setVisible(true);
		vEliminar.setResizable(false);
	}
	
	/*Descripción: Función que coloca los atributos de los botones
	 * Entrada: Ninguna
	 * Salida: Ninguna
	 */
	private void colocarBotones(){
		botonAgregar= new JButton("Agregar");
		botonAgregar.setBounds(10,130,125,25);
		ImageIcon imageAdd= new ImageIcon("add.gif");
		botonAgregar.setIcon(imageAdd);
		botonAgregar.setMnemonic(KeyEvent.VK_I);
		botonAgregar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				ventanaPersona(0);
			}
		});
		botonEditar= new JButton("Editar");
		botonEditar.setBounds(10,161,125,25);
		botonEditar.setMnemonic(KeyEvent.VK_I);
		botonEditar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ventanaPersona(list.getSelectedIndex());
			}
		});
		botonEliminar= new JButton("Eliminar");
		botonEliminar.setBounds(10,192,125,25);
		ImageIcon imageDelete= new ImageIcon("delete.gif");
		botonEliminar.setIcon(imageDelete);
		botonEliminar.setMnemonic(KeyEvent.VK_I);
		botonEliminar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ventanaEliminar(list.getSelectedIndex());
			}
		});
	}
	
	public VentanaPer(){
		JFrame ventana= new JFrame("Lista de Personas");
		JLabel contPers; 
		
		colocarBotones();
		cargarNombre();
		contPers= new JLabel(cantPers+" personas encontradas");
		ordenarLista(cantPers);
		list= new JList(nombreLista);
		list.setSelectedIndex(0);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);		
		list.setBounds(150,130,500,260);
		list.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		contPers.setBounds(0,440,150,25);
		
		ventana.setLayout(null);
		ventana.add(contPers);
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
