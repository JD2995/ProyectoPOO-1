package logicaInterfaz;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import logicaPrograma.Articulo;
import logicaPrograma.Libros;
import logicaPrograma.Pelicula;
import logicaPrograma.Prestamo;
import logicaPrograma.Revista;

class VentanaNoPrestado{
	//DEFINICIÓN DE ATRIBUTOS
	private int cantPrestamos= 0;	//Contador de préstamos en la tabla
	private int modo=0;		//Define el modo en que se encuentra la ventana: Libros,Revistas o Películas
	private ModeloDatos nombreLista= new ModeloDatos();
	private JTable tabla= new JTable(nombreLista);	//Tabla con los datos de los articulos
	private Articulo Lista[]= new Articulo[120];
	private int indLista[]= new int[120];
	private int cantLista[]= new int[120];	
	private JButton botonBuscar;
	private JRadioButton rNombre,rGenero,rCalific;
	private ButtonGroup grupoBusqueda = new ButtonGroup();
	private JTextField cuadroBusc;
	
	//DEFINICIÓN DE MÉTODOS
	
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
			nombreLista.setValueAt(i, 0, "");
			nombreLista.setValueAt(i, 1, "");
			nombreLista.setValueAt(i, 2, "");
			i++;
		}
		i=0;
		while(i<120){
			cantLista[i]=0;
			i++;
		}
	}
	
	/*Descripción: Método que analiza si un artículo está prestado
	 * Entrada: Entero con el índice del artículo en el registro
	 * Salida: True si está prestado, false si no lo está
	 */
	private boolean isPrestado(int numArtic){
		int i=0;
		Prestamo tempPrestamo= new Prestamo();
		while(true){
			if(tempPrestamo.Obtener(i+1, "Prestamos.txt") == false) break;
			if(tempPrestamo.getNumeroArticulo()==numArtic){
				if(tempPrestamo.getTipoArticulo().equals("Libro") && modo == 0)return true;
				else if(tempPrestamo.getTipoArticulo().equals("Revista") && modo == 1)return true;
				else if(tempPrestamo.getTipoArticulo().equals("Pelicula") && modo == 2)return true;
			}
			i++;
		}
		return false;
	}
	
	/* Descripción: Función que ordena alfabeticamente una lista
	 * Entrada: Número de atributo a obtener
	 * 			Número de elementos en el vector
	 * Salida: Ninguna */
	private void ordenarLista(int cant){
		int i=1;
		int indAux;
		String aux1,aux2,aux3;
		
		if(cant == 0 || cant == 1){ return;}
		while(i<cant){
			if(i == 0){
				i++;
			}
			//Si el elemento anterior tiene un caracter menor que el elemento actual
			else if(((String) nombreLista.getValueAt(i, 0)).charAt(0)< ((String) nombreLista.getValueAt(i-1, 0)).charAt(0) && i!= 0){
				//Cambia posiciones en lista con nombres
				aux1= (String) nombreLista.getValueAt(i, 0);
				aux2= (String) nombreLista.getValueAt(i, 1);
				aux3= (String) nombreLista.getValueAt(i, 2);
				nombreLista.setValueAt(i, 0, (String) nombreLista.getValueAt(i-1, 0));
				nombreLista.setValueAt(i, 1, (String) nombreLista.getValueAt(i-1, 1));
				nombreLista.setValueAt(i, 2, (String) nombreLista.getValueAt(i-1, 2));
				nombreLista.setValueAt(i-1, 0, aux1);
				nombreLista.setValueAt(i-1, 1, aux2);
				nombreLista.setValueAt(i-1, 2, aux3);
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
	
	/*Descripción: Función que carga en los arrays la información encontrada en el registro
	 * Entrada: Ninguna
	 * Salida: Ninguna
	 */
	private void cargarNombre(){
		Articulo temp= null;
		String archivo= null;
		limpiarListas();	//Limpia el contenido de los arrays
		int i= 0;
		int j=0;
		if(modo == 0){
			archivo= "Libros.txt"; 
			temp= new Libros();
		}
		else if(modo == 1) {
			archivo= "Revistas.txt"; 
			temp= new Revista();
		}
		else if(modo == 2){
			archivo= "Peliculas.txt"; 
			temp= new Pelicula();
		}
		while(true){
			//Obtiene en las clases sus atributos hasta que ya no encuentra
			//más en disco
			if(temp.Obtener(i+1, archivo) == false){break;}
			//Si el artículo se encuentra prestado
			if(isPrestado(i) == true) i++;
			//Si el artículo no se encuentra prestado
			else{
				if(modo == 0) Lista[j]= new Libros();
				else if(modo == 1) Lista[j]= new Revista();
				else if(modo == 2) Lista[j]= new Pelicula();
				Lista[j].setNombre(temp.getNombre());
				Lista[j].setGenero(temp.getGenero());
				Lista[j].setCalificacion(temp.getCalificacion());
				Lista[j].setCantidad(temp.getCantidad());
				Lista[j].setNImagen(temp.getNImagen());
				Lista[j].setPrestado(isPrestado(j));
				if(modo == 0){
					((Libros) Lista[j]).setAutor(((Libros)temp).getAutor());
					((Libros) Lista[j]).setEditorial(((Libros)temp).getEditorial());
					((Libros) Lista[j]).setEdicion(((Libros)temp).getEdicion());
				}
				else if(modo == 1){
					((Revista) Lista[j]).setEditorial(((Revista)temp).getEditorial());
				}
				else if(modo == 2){
					((Pelicula) Lista[j]).setDirector(((Pelicula)temp).getDirector());
					((Pelicula) Lista[j]).setGenero(((Pelicula)temp).getGenero());
				}
				indLista[j]= j;
				nombreLista.setValueAt(j, 0, temp.getNombre());
				nombreLista.setValueAt(j, 1, temp.getGenero());
				nombreLista.setValueAt(j, 2, (Integer.toString(temp.getCalificacion())));
				i++;
				j++;
			}
		}
		cantPrestamos= j;	
		ordenarLista(cantPrestamos);
	}
	
	/*Descripción: Método que filtra la lista de acuerdo a la búsqueda que se realice
	 * Entrada: Ninguna
	 * Salida: Ninguna
	 */
	private void buscarLista(){
		int i=0;
		int j=0;
		String hilera= cuadroBusc.getText();
		//System.out.println(hilera);
		String palabra= null;
		cargarNombre();
		ordenarLista(cantPrestamos);
		//Si el cuadro de búsqueda no tiene escrito nada
		if(hilera.equals("")==true){
			return;
		}
		while(i<cantPrestamos){
			if(i == cantPrestamos) break;
			if(rNombre.isSelected()==true) palabra= Lista[i].getNombre();
			else if(rGenero.isSelected()==true) palabra= Lista[i].getGenero();
			else if(rCalific.isSelected()==true) palabra=Integer.toString(Lista[i].getCalificacion());
			
			if(hilera.startsWith(palabra)==true){
				nombreLista.setValueAt(j, 0, Lista[i].getNombre());
				nombreLista.setValueAt(j, 1, Lista[i].getGenero());
				nombreLista.setValueAt(j, 2, Integer.toString(Lista[i].getCalificacion()));
				indLista[j]= i;
				j++;
			}
			i++;
		}
		cantPrestamos= j;
		while(j<120){
			nombreLista.setValueAt(j, 0, "");
			nombreLista.setValueAt(j, 1, "");
			nombreLista.setValueAt(j, 2, "");
			indLista[j]=0;
			j++;
		}
	}
	
	//Clase para los objetos de la tabla de préstamos
		@SuppressWarnings("serial")
		class ModeloDatos extends AbstractTableModel{
			Object datos[][]= new Object[120][3];

			public int getColumnCount() {
				return datos[0].length;
			}
			public int getRowCount() {
				
				return datos.length;
			}
			public Object getValueAt(int fil, int col) {
				
				return (datos[fil][col]);
			}
			public void setValueAt(int fil, int col,String hilera){
				datos[fil][col]= hilera;
				fireTableDataChanged();
			}
		}
		
	//Constructor
	VentanaNoPrestado(int mode){
		JFrame ventana= null;
		ImageIcon logo= new ImageIcon("logo.png");
		JLabel iLogo= new JLabel(logo);
		BufferedImage img= null;
		modo= mode;
		if(modo == 0) ventana= new JFrame("Consultar Libros no prestádos");
		else if(modo == 1) ventana= new JFrame("Consultar Revistas no prestádos");
		else if(modo == 2) ventana= new JFrame("Consultar Películas no prestádos");
		
		try{
			img= ImageIO.read(new File("logo.png"));
		}
		catch(IOException e){
			
		}
		
		//Colocación de los botones
		rNombre = new JRadioButton("Nombre",true);
		rNombre.setBounds(20,90,70,25);
		rGenero = new JRadioButton("Género");
		rGenero.setBounds(90,90,70,25);
		rCalific = new JRadioButton("Calificación");
		rCalific.setBounds(160,90,95,25);
		grupoBusqueda.add(rCalific);grupoBusqueda.add(rGenero);grupoBusqueda.add(rNombre);
		botonBuscar= new JButton("Buscar");
		botonBuscar.setBounds(450,90,90,25);
		botonBuscar.setMnemonic(KeyEvent.VK_I);
		botonBuscar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				buscarLista();
			}
		});
		
		//Cuadro de entrada de texto para busqueda
		cuadroBusc = new JTextField();
		cuadroBusc.setBounds(270,90,170,25);
		
		//Modificación de los headers de la tabla
		JTableHeader th = tabla.getTableHeader();
		TableColumnModel tcm = th.getColumnModel();
		TableColumn tc = tcm.getColumn(0);
		tc.setHeaderValue("Nombre");
		tc = tcm.getColumn(1);
		tc.setHeaderValue("Género");
		tc = tcm.getColumn(2);
		tc.setHeaderValue("Calificación");
		th.repaint();
		
		cargarNombre();
		
		//Colocación del logo
		iLogo.setBounds(20,15,70,65);
		
		JScrollPane barraDesplazamiento = new JScrollPane(tabla);
		barraDesplazamiento.setBounds(20,125,500,260);
		
		ventana.setLayout(null);
		ventana.setIconImage(img);
		ventana.add(barraDesplazamiento);
		ventana.add(iLogo);
		ventana.add(rNombre);
		ventana.add(rGenero);
		ventana.add(rCalific);
		ventana.add(botonBuscar);
		ventana.add(cuadroBusc);
		
		ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	//Finalizar la tarea cuando se cierre la ventana
		ventana.setSize(550,430);	//Tamaño de la ventana
		ventana.setVisible(true);
		ventana.setResizable(false);
	}
}