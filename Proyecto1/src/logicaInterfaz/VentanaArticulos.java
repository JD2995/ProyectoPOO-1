package logicaInterfaz;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import logicaPrograma.Articulo;
import logicaPrograma.Pelicula;
import logicaPrograma.Libros;
import logicaPrograma.Personas;
import logicaPrograma.Revista;

public class VentanaArticulos {
	//DEFINICION DE ATRIBUTOS
	private int modo=0;		//0 si está en Libros, 1 si está en Revistas, 2 si esté en Películas
	private Articulo Lista[]= new Articulo[120];
	private int indLista[]= new int[120];	//Vector con el índice de los nombres de Lista
	private int cantArtic= 0;	//Contador de articulos en la tabla
	private JFrame vArticulos = null;
	private ModeloDatos nombreLista= new ModeloDatos();
	private JTable tabla= new JTable(nombreLista);	//Tabla con los datos de los articulos
	private ListSelectionModel cellSelectionModel= tabla.getSelectionModel();
	private JButton botonAgregar, botonEditar, botonEliminar, botonBuscar, botonImportar;
	private JTextField cuadroBusLibro;
	private JRadioButton rNomLibro, rGenLibro,rCalLibro;
	private ButtonGroup grupoBusqueda = new ButtonGroup();
	private JPanel cuadroImage= null;
	private Image img= null;
	private Image newimg= null;
	private ImageIcon iconImagen= null;
	private JLabel lImagen;
	
	//DEFINICION DE METODOS
	
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
			nombreLista.setValueAt(i, 3, "");
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
		String aux1,aux2,aux3,aux4;
		
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
				aux4= (String) nombreLista.getValueAt(i, 3);
				nombreLista.setValueAt(i, 0, (String) nombreLista.getValueAt(i-1, 0));
				nombreLista.setValueAt(i, 1, (String) nombreLista.getValueAt(i-1, 1));
				nombreLista.setValueAt(i, 2, (String) nombreLista.getValueAt(i-1, 2));
				nombreLista.setValueAt(i, 3, (String) nombreLista.getValueAt(i-1, 3));
				nombreLista.setValueAt(i-1, 0, aux1);
				nombreLista.setValueAt(i-1, 1, aux2);
				nombreLista.setValueAt(i-1, 2, aux3);
				nombreLista.setValueAt(i-1, 3, aux4);
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
			if(modo == 0) Lista[i]= new Libros();
			else if(modo == 1) Lista[i]= new Revista();
			else if(modo == 2) Lista[i]= new Pelicula();
			Lista[i].setNombre(temp.getNombre());
			Lista[i].setGenero(temp.getGenero());
			Lista[i].setCalificacion(temp.getCalificacion());
			Lista[i].setCantidad(temp.getCantidad());
			Lista[i].setNImagen(temp.getNImagen());
			if(modo == 0){
				((Libros) Lista[i]).setAutor(((Libros)temp).getAutor());
				((Libros) Lista[i]).setEditorial(((Libros)temp).getEditorial());
				((Libros) Lista[i]).setEdicion(((Libros)temp).getEdicion());
			}
			else if(modo == 1){
				((Revista) Lista[i]).setEditorial(((Revista)temp).getEditorial());
			}
			else if(modo == 2){
				((Pelicula) Lista[i]).setDirector(((Pelicula)temp).getDirector());
				((Pelicula) Lista[i]).setGenero(((Pelicula)temp).getGenero());
			}
			indLista[i]= i;
			nombreLista.setValueAt(i, 0, temp.getNombre());
			nombreLista.setValueAt(i, 1, temp.getGenero());
			nombreLista.setValueAt(i, 2, (Integer.toString(temp.getCalificacion())));
			nombreLista.setValueAt(i, 3, (Integer.toString(temp.getCantidad())));
			i++;
		}
		cantArtic= i;		
	}
	
	private void ventanaArticulos(int numArtic){
		JLabel label1,label2,label3,label4,label5,label6,label7,label8;
		JLabel lImagen;
		JTextField espacio[]= new JTextField[7];
		JButton botonImagen, botonAgregar, botonCancelar;
		ImageIcon iconImagen= null;
		JPanel cuadroImage= new JPanel(new BorderLayout());
		String hileraImagen[]= new String[2];
		
		//Colocación del nombre de la ventana
		if(modo == 0 && numArtic == -1) vArticulos= new JFrame("Agregar Libro");
		else if(modo == 0 && numArtic != -1) vArticulos= new JFrame("Editar Libro");
		else if(modo == 1 && numArtic == -1) vArticulos= new JFrame("Agregar Revista");
		else if(modo == 1 && numArtic != -1) vArticulos= new JFrame("Editar Revista");
		else if(modo == 2 && numArtic == -1) vArticulos= new JFrame("Agregar Película");
		else if(modo == 2 && numArtic != -1) vArticulos= new JFrame("Editar Revista");
		
		//Colocación de los labels
		label1= new JLabel("Nombre:");
		label1.setBounds(110,10,100,30);
		label1.setHorizontalAlignment(JLabel.RIGHT);
		label2= new JLabel("Género:");
		label2.setBounds(110,45,100,30);
		label2.setHorizontalAlignment(JLabel.RIGHT);
		label3= new JLabel("Calificación:");
		label3.setBounds(110,80,100,30);
		label3.setHorizontalAlignment(JLabel.RIGHT);
		label4= new JLabel("Cantidad:");
		label4.setBounds(110,115,100,30);
		label4.setHorizontalAlignment(JLabel.RIGHT);
		label5= new JLabel("nImagen:");
		label5.setBounds(110,150,100,30);
		label5.setHorizontalAlignment(JLabel.RIGHT);
		label6= null;
		if(modo == 0) label6= new JLabel("Autor:");
		else if(modo == 1) label6= new JLabel("Editorial:");
		else if(modo == 2) label6= new JLabel("Director:");
		label6.setBounds(110,185,100,30);
		label6.setHorizontalAlignment(JLabel.RIGHT);
		label7= null;
		if(modo == 0) label7= new JLabel("Editorial:");
		else if(modo == 2) label7= new JLabel("Género:");
		label7.setBounds(110,220,100,30);
		label7.setHorizontalAlignment(JLabel.RIGHT);
		label8= null;
		if(modo == 0) label8= new JLabel("Edición: ");
		label8.setBounds(110,255,100,30);
		label8.setHorizontalAlignment(JLabel.RIGHT);
		
		//Colocación de los cuadros de entrada
		espacio[0]= new JTextField();
		if(numArtic!= -1) espacio[0].setText(Lista[indLista[numArtic]].getNombre()); 
		espacio[0].setBounds(220,13,200,20);
		espacio[1]= new JTextField();
		if(numArtic!= -1) espacio[1].setText(Lista[indLista[numArtic]].getGenero());
		espacio[1].setBounds(220,48,200,20);
		espacio[2]= new JTextField();
		if(numArtic!= -1) espacio[2].setText(Integer.toString(Lista[indLista[numArtic]].getCalificacion()));
		espacio[2].setBounds(220,83,200,20);
		espacio[3]= new JTextField();
		if(numArtic!= -1) espacio[3].setText(Integer.toString(Lista[indLista[numArtic]].getCantidad()));
		espacio[3].setBounds(220,118,200,20);
		espacio[4]= new JTextField(); 
		if(numArtic!= -1 && modo == 0) espacio[4].setText(((Libros) Lista[indLista[numArtic]]).getAutor()); 
		if(numArtic!= -1 && modo == 1) espacio[4].setText(((Revista) Lista[indLista[numArtic]]).getEditorial());
		if(numArtic!= -1 && modo == 2) espacio[4].setText(((Pelicula) Lista[indLista[numArtic]]).getDirector()); 
		espacio[4].setBounds(220,188,200,20);
		espacio[5]= null;
		if(modo == 0 || modo == 1) espacio[5]= new JTextField();
		if(numArtic!= -1 && modo == 0) espacio[5].setText(((Libros) Lista[indLista[numArtic]]).getEditorial());
		if(numArtic!= -1 && modo == 2) espacio[5].setText(((Pelicula) Lista[indLista[numArtic]]).getGenero());
		espacio[5].setBounds(220, 223, 200, 20);
		espacio[6]= null;
		if(modo == 0) espacio[6]= new JTextField();
		if(numArtic!= -1 && modo == 0) espacio[6].setText(((Libros) Lista[indLista[numArtic]]).getEdicion());
		espacio[6].setBounds(220,258,200,20);
		//Colocación de botones
		botonImagen= new JButton("Explorar");
		botonImagen.setBounds(220,153,100,20);
		botonImagen.setMnemonic(KeyEvent.VK_1);
		botonImagen.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				hileraImagen[0]= ventanaImagen();
				return;
			}
		});
		if(numArtic == -1) botonAgregar= new JButton("Agregar");
		else botonAgregar= new JButton("Editar");
		botonAgregar.setMnemonic(KeyEvent.VK_I);
		botonAgregar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Articulo objeto= null;
				String palabra;
				
				if(modo == 0)objeto= new Libros();
				else if(modo == 1) objeto= new Revista();
				else if(modo == 2) objeto= new Pelicula();
				
				palabra= espacio[0].getText();
				objeto.setNombre(palabra);
				palabra= espacio[1].getText();
				objeto.setGenero(palabra);
				palabra= espacio[2].getText();
				objeto.setCalificacion(Integer.parseInt(palabra));
				palabra= espacio[3].getText();
				objeto.setCantidad(Integer.parseInt(palabra));
				palabra= espacio[4].getText();
				if(modo == 0) ((Libros) objeto).setAutor(palabra);
				else if(modo == 1) ((Revista) objeto).setEditorial(palabra);
				else if(modo == 2) ((Pelicula) objeto).setDirector(palabra);
				palabra= espacio[5].getText();
				if(modo == 0) ((Libros) objeto).setEditorial(palabra);
				else if(modo == 2) ((Pelicula) objeto).setGenero(palabra);
				palabra= espacio[6].getText();
				if(modo == 0) ((Libros) objeto).setEdicion(palabra);
				//Guardando en el proyecto la imagen de portada conseguida
				Path FROM= Paths.get(hileraImagen[0]);
				Path TO= null;
				//Si es un articulo a editar
				if(modo == 0 && numArtic!= -1) hileraImagen[1]="Libro"+indLista[numArtic]+"."+hileraImagen[0].substring(hileraImagen[0].length()-3, hileraImagen[0].length());
				else if(modo == 1 && numArtic!= -1) hileraImagen[1]="Revista"+indLista[numArtic];
				else if(modo == 2 && numArtic!= -1) hileraImagen[1]="Pelicula"+indLista[numArtic];
				//Si es un articulo nuevo
				cargarNombre();
				ordenarLista(cantArtic);
				if(modo == 0 && numArtic==-1) hileraImagen[1]="Libros"+cantArtic;
				else if(modo == 1 && numArtic==-1) hileraImagen[1]="Revista"+cantArtic;
				else if(modo == 2 && numArtic==-1) hileraImagen[1]= "Pelicula"+cantArtic;
				TO= Paths.get(hileraImagen[1]);
				CopyOption [] options= new CopyOption[]{
					StandardCopyOption.REPLACE_EXISTING,
					StandardCopyOption.COPY_ATTRIBUTES
				};
				try {
					Files.copy(FROM, TO, options);
				} catch (IOException e1) {}
				objeto.setNImagen(hileraImagen[1]);		//Coloca la direccion de la imagen
				//Realiza los cambios o inserción del nuevo elemento
				if(numArtic==-1){
					if(modo == 0)objeto.Agregar("Libros.txt");
					else if(modo == 1)objeto.Agregar("Revistas.txt");
					else if(modo == 2)objeto.Agregar("Peliculas.txt");
				}
				else{
					if(modo == 0)objeto.Editar(indLista[numArtic],"Libros.txt");
					if(modo == 1)objeto.Editar(indLista[numArtic],"Revistas.txt");
					if(modo == 2)objeto.Editar(indLista[numArtic],"Peliculas.txt");
				}
				
				vArticulos.dispose();
				cargarNombre();
				ordenarLista(cantArtic);
				return;
			}
		});
		botonCancelar= new JButton("Cancelar");
		botonAgregar.setBounds(200,295,100,25);
		botonCancelar.setBounds(320, 295, 100, 25);
		botonCancelar.setMnemonic(KeyEvent.VK_I);
		botonCancelar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				vArticulos.dispose();
				return;
			}
		});
		
		iconImagen= new ImageIcon();
		lImagen= new JLabel(iconImagen);
		cuadroImage.add(lImagen);
		cuadroImage.setBorder(BorderFactory.createLineBorder(Color.black));
		cuadroImage.setBounds(15, 15, 100, 150);
		//Inserción de los componentes al frame
		vArticulos.setLayout(null);
		vArticulos.add(cuadroImage);
		vArticulos.add(label1);
		vArticulos.add(label2);
		vArticulos.add(label3);
		vArticulos.add(label4);
		vArticulos.add(label5);
		vArticulos.add(label6);
		vArticulos.add(label7);
		vArticulos.add(label8);
		vArticulos.add(espacio[0]);
		vArticulos.add(espacio[1]);
		vArticulos.add(espacio[2]);
		vArticulos.add(espacio[3]);
		vArticulos.add(espacio[4]);
		vArticulos.add(espacio[5]);
		vArticulos.add(espacio[6]);
		vArticulos.add(botonImagen);
		vArticulos.add(botonAgregar);
		vArticulos.add(botonCancelar);
		
		vArticulos.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	//Finalizar la tarea cuando se cierre la ventana
		vArticulos.setSize(465,370);	//Tamaño de la ventana
		vArticulos.setVisible(true);
		vArticulos.setResizable(false);
	}
	
	
	private void buscarLista(){
		int i=0;
		int j=0;
		String hilera= cuadroBusLibro.getText();
		//System.out.println(hilera);
		String palabra= null;
		//Si el cuadro de búsqueda no tiene escrito nada
		if(hilera.equals("")==true){
			cargarNombre();
			ordenarLista(cantArtic);
			return;
		}
		while(i<cantArtic){
			if(i == cantArtic) break;
			if(rNomLibro.isSelected()==true) palabra= Lista[i].getNombre();
			else if(rGenLibro.isSelected()==true) palabra= Lista[i].getGenero();
			else if(rCalLibro.isSelected()==true) palabra=Integer.toString(Lista[i].getCalificacion());
			//System.out.println(palabra);
			if(hilera.startsWith(palabra)==true){
				nombreLista.setValueAt(j, 0, Lista[i].getNombre());
				nombreLista.setValueAt(j, 1, Lista[i].getGenero());
				nombreLista.setValueAt(j, 2, Lista[i].getCalificacion());
				nombreLista.setValueAt(j, 3, Lista[i].getCantidad());
				indLista[j]= i;
				//System.out.println(Lista);
				j++;
			}
			i++;
		}
		cantArtic= j;
		while(j<120){
			nombreLista.setValueAt(j, 0, "");
			nombreLista.setValueAt(j, 1, "");
			nombreLista.setValueAt(j, 2, "");
			nombreLista.setValueAt(j, 3, "");
			indLista[j]=0;
			j++;
		}
	}
	
	
	
	
	
	
	/*Descripción: Función que crea la ventana de eliminación de una persona
	 * Entrada: Int número de persona a eliminar
	 * Salida: Ninguna
	 */
	private void ventanaEliminar(int numArtic){
		JFrame vEliminar= new JFrame("Eliminar Articulo");
		JLabel mensaje= new JLabel("¿Esta seguro de eliminar al articulo del registro?");
		JButton aceptar= new JButton("Aceptar");
		JButton cancelar= new JButton("Cancelar");
		Articulo articulo= new Libros();
		
		mensaje.setBounds(30,20,300,25);
		aceptar.setBounds(85,50,90,25);
		aceptar.setMnemonic(KeyEvent.VK_I);
		aceptar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(modo == 0) articulo.eliminar(indLista[numArtic],"Libros.txt");
				else if(modo == 1) articulo.eliminar(indLista[numArtic],"Revistas.txt");
				else if(modo == 2) articulo.eliminar(indLista[numArtic],"Peliculas.txt");
				vEliminar.dispose();
				cantArtic--;
				cargarNombre();
				ordenarLista(cantArtic);
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
	
	private String ventanaImagen(){
		JFrame vImportar= new JFrame("Ventana");
		JFileChooser archivo= new JFileChooser();
		String hilera= null;
		
		archivo.showOpenDialog(vImportar);
		try{
			hilera=archivo.getSelectedFile().getAbsolutePath();
		}
		catch(Exception e){hilera= null;}
		vImportar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	//Cerrar la ventana
		return hilera;
	}
	
	private void ventanaImportar(){
		Articulo articulo= null;
		JFrame vImportar= new JFrame("Ventana");
		JFileChooser archivo= new JFileChooser();
		
		if(modo == 0) articulo= new Libros();
		else if(modo == 1) articulo= new Revista();
		else if(modo == 2) articulo= new Pelicula();
		archivo.showOpenDialog(vImportar);
		try{articulo.Cargar(archivo.getSelectedFile().getAbsolutePath());}
		catch(Exception e){}
		cargarNombre();
		ordenarLista(cantArtic);
		vImportar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	//Cerrar la ventana
	}
	
	/*Descripción: Funcion que coloca los atributos de los botones
	 *Entrada: Ninguna
	 *Salida: Ninguna
	 *Restricciones: Ninguna
	*/	
	private void colocarBotones(){
		botonAgregar= new JButton("Agregar");
		botonAgregar.setBounds(10,271,125,25);
		ImageIcon imageAgregar= new ImageIcon("add.gif");
		botonAgregar.setIcon(imageAgregar);
		botonAgregar.setMnemonic(KeyEvent.VK_I);
		botonAgregar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				ventanaArticulos(-1);
			}
		});
		botonEditar= new JButton("Editar");
		botonEditar.setBounds(10,302,125,25);
		ImageIcon imageEditar= new ImageIcon("change.gif");
		botonEditar.setIcon(imageEditar);
		botonEditar.setMnemonic(KeyEvent.VK_I);
		botonEditar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ventanaArticulos(tabla.getSelectedRow());
			}
		});

		botonEliminar= new JButton("Eliminar");
		botonEliminar.setBounds(10,333,125,25);
		ImageIcon imageEliminar= new ImageIcon("delete.gif");
		botonEliminar.setIcon(imageEliminar);
		botonEliminar.setMnemonic(KeyEvent.VK_I);
		botonEliminar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ventanaEliminar(tabla.getSelectedRow());
			}
		});
		botonImportar= new JButton("Importar");
		botonImportar.setBounds(10,364,125,25);
		ImageIcon imageImportar= new ImageIcon("importar.gif");
		botonImportar.setIcon(imageImportar);
		botonImportar.setMnemonic(KeyEvent.VK_I);
		botonImportar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ventanaImportar();
			}
		});
		botonBuscar= new JButton("Buscar");
		botonBuscar.setBounds(560,90,90,25);
		botonBuscar.setMnemonic(KeyEvent.VK_I);
		botonBuscar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				buscarLista();
			}
		});
	}
	
	//Clase para los objetos de la tabla de articulos
	@SuppressWarnings("serial")
	class ModeloDatos extends AbstractTableModel{
		Object datos[][]= new Object[120][4];

		public int getColumnCount() {
			return datos[0].length;
		}
		public int getRowCount() {
			// TODO Auto-generated method stub
			return datos.length;
		}
		public Object getValueAt(int fil, int col) {
			// TODO Auto-generated method stub
			return (datos[fil][col]);
		}
		public void setValueAt(int fil, int col,String hilera){
			datos[fil][col]= hilera;
			fireTableDataChanged();
		}
	}
	
	//Clase para el listener de la seleccion de un elemento en la tabla de articulos
	class ListenerTabla implements ListSelectionListener{
		ListSelectionModel cellSelectionModel= tabla.getSelectionModel();
		
		public void valueChanged(ListSelectionEvent e) {
			String hilera= null;
			int[] filaSelect= tabla.getSelectedRows();
			int[] columnasSelect= tabla.getSelectedColumns();
			for(int i=0; i<filaSelect.length; i++){
				for(int j=0; j<columnasSelect.length; j++){
					hilera= Lista[indLista[filaSelect[i]]].getNImagen();
					try{
						iconImagen= null;
						if(hilera!= null) iconImagen= new ImageIcon(hilera);
						else iconImagen= new ImageIcon("noImage.png");
						img= iconImagen.getImage();
						newimg= img.getScaledInstance(90, 135, Image.SCALE_DEFAULT);
						iconImagen= new ImageIcon(newimg);
						lImagen.setIcon(iconImagen);
						cuadroImage.add(lImagen);
						cuadroImage.updateUI();
					}
					catch(Exception e2){
						iconImagen= new ImageIcon("noImage.png");
					}
					cuadroImage.updateUI();
				}
			}
		}
	}
	
	//Constructor
	VentanaArticulos(){
		JFrame ventana= new JFrame("Lista de Artículos");
		JScrollPane barraDesplazamiento = new JScrollPane(tabla);
		barraDesplazamiento.setBounds(150,130,500,260); 
		cuadroImage= new JPanel(new BorderLayout());
		cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		cellSelectionModel.addListSelectionListener(new ListenerTabla());

		//Modificación de los headers de la tabla
		JTableHeader th = tabla.getTableHeader();
		TableColumnModel tcm = th.getColumnModel();
		TableColumn tc = tcm.getColumn(0);
		tc.setHeaderValue("Nombre");
		tc = tcm.getColumn(1);
		tc.setHeaderValue("Género");
		tc = tcm.getColumn(2);
		tc.setHeaderValue("Calificación");
		tc = tcm.getColumn(3);
		tc.setHeaderValue("Cantidad");
		th.repaint();
		
		colocarBotones();	//Coloca los atributos de los botones
		cargarNombre();		//Carga la tabla con los articulos
		ordenarLista(cantArtic);	//Ordena alfabeticamente la lista
		
		try{
			iconImagen= new ImageIcon("noImage.png");
			img= iconImagen.getImage();
			newimg= img.getScaledInstance(90, 135, Image.SCALE_DEFAULT);
			iconImagen= new ImageIcon(newimg);
			lImagen= new JLabel(iconImagen);
			cuadroImage.add(lImagen);
		}
		catch(Exception e){lImagen= null;}
		cuadroImage.setBorder(BorderFactory.createLineBorder(Color.black));
		cuadroImage.setBounds(45, 130, 90, 135);
		
		//radio buttons para la busqueda de libros
		rNomLibro = new JRadioButton("Nombre",true);
		rNomLibro.setBounds(130,90,70,25);
		rGenLibro = new JRadioButton("Género");
		rGenLibro.setBounds(200,90,70,25);
		rCalLibro = new JRadioButton("Calificación");
		rCalLibro.setBounds(270,90,95,25);
		grupoBusqueda.add(rCalLibro);grupoBusqueda.add(rGenLibro);grupoBusqueda.add(rNomLibro);
		
		//Cuadro de entrada de texto para busqueda
		cuadroBusLibro = new JTextField();
		cuadroBusLibro.setBounds(380,90,170,25);
		
		//Inserción de los componentes a la ventana
		ventana.setLayout(null);
		ventana.add(barraDesplazamiento);
		ventana.add(botonAgregar);
		ventana.add(botonEditar);
		ventana.add(botonEliminar);
		ventana.add(botonImportar);
		ventana.add(cuadroImage);
		ventana.add(cuadroBusLibro);
		ventana.add(botonBuscar);
		ventana.add(rNomLibro);
		ventana.add(rGenLibro);
		ventana.add(rCalLibro);
		
		
		ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	//Finalizar la tarea cuando se cierre la ventana
		ventana.setSize(675,450);	//Tamaño de la ventana
		ventana.setVisible(true);
		ventana.setResizable(false);
	}

}
