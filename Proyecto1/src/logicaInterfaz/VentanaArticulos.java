package logicaInterfaz;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;

import javax.imageio.ImageIO;
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
import logicaPrograma.Prestamo;
import logicaPrograma.Revista;

public class VentanaArticulos {
	//DEFINICION DE ATRIBUTOS
	private int modo=0;		//0 si est� en Libros, 1 si est� en Revistas, 2 si est� en Pel�culas
	private Articulo Lista[]= new Articulo[120];	//Vector con articulos
	private int indLista[]= new int[120];	//Vector con el �ndice de los nombres de Lista
	private int cantArtic= 0;	//Contador de articulos en la tabla
	private Personas ListaPers[]= new Personas[120];	//Vector con personas para prestar
	private int indListaPers[]= new int[120];	//Vector con el �ndice de los nombres de las personas
	private int cantPers=0;
	private JFrame vArticulos = null;
	private ModeloDatos nombreLista= new ModeloDatos();
	private JTable tabla= new JTable(nombreLista);	//Tabla con los datos de los articulos
	private ListSelectionModel cellSelectionModel= tabla.getSelectionModel();
	private JButton botonAgregar, botonEditar, botonEliminar, botonBuscar, botonImportar, botonPrestar;
	private JTextField cuadroBusLibro;
	private JRadioButton rNomLibro, rGenLibro,rCalLibro;
	private ButtonGroup grupoBusqueda = new ButtonGroup();
	private JPanel cuadroImage= null;
	private Image img= null;
	private Image newimg= null;
	private ImageIcon iconImagen= null;
	private JLabel lImagen;
	private ModeloDatos nombreLis = new ModeloDatos(3);
	private JTable tablePrestar = new JTable(nombreLis);
	
	
	//DEFINICION DE METODOS
	
	/*Descripci�n: Lista que limpia el contenido de la listas nombreLista, indLista y Lista
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
	
	/* Descripci�n: Funci�n que ordena alfabeticamente una lista
	 * Entrada: N�mero de atributo a obtener
	 * 			N�mero de elementos en el vector
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
				//Cambia posiciones en lista con �ndices
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
	
	/*Descripci�n: M�todo que analiza si un art�culo est� prestado
	 * Entrada: Entero con el �ndice del art�culo en el registro
	 * Salida: True si est� prestado, false si no lo est�
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
	
	/*Descripci�n: M�todo que devuelve la cantidad de veces prestada de determinado articulo
	 * Entrada: Entero con el �ndice del art�culo en el registro
	 * Salida: Entero con la cantidad de prestamos del art�culo
	 */
	private int cantPrestado(int numArtic){
		int i=0;
		int cant=0;
		Prestamo tempPrestamo= new Prestamo();
		while(true){
			if(tempPrestamo.Obtener(i+1, "Prestamos.txt") == false) break;
			else if(tempPrestamo.getNumeroArticulo()==numArtic) {
				if(tempPrestamo.getTipoArticulo().equals("Libro") && modo == 0)cant++;
				else if(tempPrestamo.getTipoArticulo().equals("Revista") && modo == 1)cant++;
				else if(tempPrestamo.getTipoArticulo().equals("Pelicula") && modo == 2)cant++;
			}
			i++;
		}
		return cant;
	}
	
	/*Descripci�n: Funci�n que carga en los arrays la informaci�n encontrada en el registro
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
			//m�s en disco
			if(temp.Obtener(i+1, archivo) == false){break;}
			if(modo == 0) Lista[i]= new Libros();
			else if(modo == 1) Lista[i]= new Revista();
			else if(modo == 2) Lista[i]= new Pelicula();
			Lista[i].setNombre(temp.getNombre());
			Lista[i].setGenero(temp.getGenero());
			Lista[i].setCalificacion(temp.getCalificacion());
			Lista[i].setCantidad(temp.getCantidad());
			Lista[i].setNImagen(temp.getNImagen());
			Lista[i].setPrestado(isPrestado(i));
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
	
	
	//metodo que permite escribir unicamente letras
		public void soloLetras(JTextField a){
			a.addKeyListener(new KeyAdapter(){
				public void keyTyped(KeyEvent e){
					char c = e.getKeyChar();
					if (!Character.isLetter(c) && !Character.isSpaceChar(c)){
						int k = (int)c;
						if (k==8){
							e.consume();
						}
						else{
						Toolkit.getDefaultToolkit().beep();
						e.consume(); }
					}
				}		
			});
		}
	
	//metodo que permite escribir unicamente n�meros
	public void soloNumeros(JTextField a){
		a.addKeyListener(new KeyAdapter(){
			public void keyTyped(KeyEvent e){
				char c = e.getKeyChar();
				if (c<'0' || c>'9'){
					int k = (int)c;
					if (k==8){
						e.consume();
					}
					else {
					Toolkit.getDefaultToolkit().beep();
					e.consume(); }
				}
				
			}		
		});
	}
	
	class VentanaAgregar implements ActionListener{
		JLabel label1,label2,label3,label4,label5,label6,label7,label8;
		JLabel lImagen;
		JTextField espacio[]= new JTextField[7];
		JButton botonImagen, botonAgregar, botonCancelar;
		ImageIcon iconImagen= null;
		JPanel cuadroImage= new JPanel(new BorderLayout());
		String hileraImagen[]= new String[2];
		int numArtic;
		
		//Acciones a ejecutar
		public void actionPerformed(ActionEvent e){
			if("Imagen".equals(e.getActionCommand())){
				hileraImagen[0]= ventanaImagen();
				return;
			}
			else if("Agregar".equals(e.getActionCommand())){
				Articulo objeto= null;
				String palabra;
				Path FROM= null;
				Path TO= null;
				
				if(modo == 0)objeto= new Libros();
				else if(modo == 1) objeto= new Revista();
				else if(modo == 2) objeto= new Pelicula();
				
				palabra= espacio[0].getText();
				if(palabra.equals("")==true){
					new MensajeError("Error, no se pueden guardar los cambios","El espacio 1 no puede estar vaci�");
					vArticulos.dispose();
					return;
				}
				objeto.setNombre(palabra);
				palabra= espacio[1].getText();
				if(palabra.equals("")==true){
					new MensajeError("Error, no se pueden guardar los cambios","El espacio 2 no puede estar vaci�");
					vArticulos.dispose();
					return;
				}
				objeto.setGenero(palabra);
				palabra= espacio[2].getText();
				if(palabra.equals("")==true){
					new MensajeError("Error, no se pueden guardar los cambios","El espacio 3 no puede estar vaci�");
					vArticulos.dispose();
					return;
				}
				objeto.setCalificacion(Integer.parseInt(palabra));
				palabra= espacio[3].getText();
				if(palabra.equals("")==true){
					new MensajeError("Error, no se pueden guardar los cambios","El espacio 4 no puede estar vaci�");
					vArticulos.dispose();
					return;
				}
				objeto.setCantidad(Integer.parseInt(palabra));
				palabra= espacio[4].getText();
				if(palabra.equals("")==true){
					new MensajeError("Error, no se pueden guardar los cambios","El espacio 5 no puede estar vaci�");
					vArticulos.dispose();
					return;
				}
				if(modo == 0) ((Libros) objeto).setAutor(palabra);
				else if(modo == 1) ((Revista) objeto).setEditorial(palabra);
				else if(modo == 2) ((Pelicula) objeto).setDirector(palabra);
				if(modo == 0 || modo == 2){
					palabra= espacio[5].getText();
					if(palabra.equals("")==true){
						new MensajeError("Error, no se pueden guardar los cambios","El espacio 6 no puede estar vaci�");
						vArticulos.dispose();
						return;
					}
				}
				if(modo == 0) ((Libros) objeto).setEditorial(palabra);
				else if(modo == 2) ((Pelicula) objeto).setGenero(palabra);
				if(modo == 0){
					palabra= espacio[6].getText();
					if(palabra.equals("")==true){
						new MensajeError("Error, no se pueden guardar los cambios","El espacio 7 no puede estar vaci�");
						vArticulos.dispose();
						return;
					}
				}
				if(modo == 0) ((Libros) objeto).setEdicion(palabra);
				//Guardando en el proyecto la imagen de portada conseguida
				hileraImagen[1]= null;
				if(numArtic!=-1)hileraImagen[1]= Lista[indLista[numArtic]].getNImagen();
				if(hileraImagen[0]!= null){
					Calendar calendario= Calendar.getInstance();
					int segundos= calendario.get(Calendar.SECOND);
					FROM= Paths.get(hileraImagen[0]);
					TO= null;
					//Si es un articulo a editar
					if(numArtic!= -1) hileraImagen[1]=espacio[0].getText()+segundos;
					//Si es un articulo nuevo
					cargarNombre();
					ordenarLista(cantArtic);
					
					if(numArtic==-1) hileraImagen[1]=espacio[0].getText()+segundos;
					TO= Paths.get("covers",hileraImagen[1]);
					CopyOption [] options= new CopyOption[]{
						StandardCopyOption.REPLACE_EXISTING,
						StandardCopyOption.COPY_ATTRIBUTES
					};
					try {
						Files.copy(FROM, TO, options);
					} catch (IOException e1) {}
				}
				objeto.setNImagen(hileraImagen[1]);		//Coloca la direccion de la imagen
				//Realiza los cambios o inserci�n del nuevo elemento
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
		}
		
		//Constructor
		VentanaAgregar(int pnumArtic){
			numArtic= pnumArtic;
			
			//Colocaci�n del nombre de la ventana
			if(modo == 0 && numArtic == -1) vArticulos= new JFrame("Agregar Libro");
			else if(modo == 0 && numArtic != -1) vArticulos= new JFrame("Editar Libro");
			else if(modo == 1 && numArtic == -1) vArticulos= new JFrame("Agregar Revista");
			else if(modo == 1 && numArtic != -1) vArticulos= new JFrame("Editar Revista");
			else if(modo == 2 && numArtic == -1) vArticulos= new JFrame("Agregar Pel�cula");
			else if(modo == 2 && numArtic != -1) vArticulos= new JFrame("Editar Revista");
			
			//Colocaci�n de los labels
			label1= new JLabel("Nombre:");
			label1.setBounds(110,10,100,30);
			label1.setHorizontalAlignment(JLabel.RIGHT);
			label2= new JLabel("G�nero:");
			label2.setBounds(110,45,100,30);
			label2.setHorizontalAlignment(JLabel.RIGHT);
			label3= new JLabel("Calificaci�n:");
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
			label7= new JLabel();
			if(modo == 0) label7= new JLabel("Editorial:");
			else if(modo == 2) label7= new JLabel("G�nero:");
			label7.setBounds(110,220,100,30);
			label7.setHorizontalAlignment(JLabel.RIGHT);
			label8= new JLabel();
			if(modo == 0) label8= new JLabel("Edici�n: ");
			label8.setBounds(110,255,100,30);
			label8.setHorizontalAlignment(JLabel.RIGHT);
			
			//Colocaci�n de los cuadros de entrada
			espacio[0]= new JTextField();
			if(numArtic!= -1) espacio[0].setText(Lista[indLista[numArtic]].getNombre()); 
			espacio[0].setBounds(220,13,200,20);
			soloLetras(espacio[0]);
			espacio[1]= new JTextField();
			if(numArtic!= -1) espacio[1].setText(Lista[indLista[numArtic]].getGenero());
			espacio[1].setBounds(220,48,200,20);
			soloLetras(espacio[1]);
			espacio[2]= new JTextField();
			if(numArtic!= -1) espacio[2].setText(Integer.toString(Lista[indLista[numArtic]].getCalificacion()));
			espacio[2].setBounds(220,83,200,20);
			soloNumeros(espacio[2]);
			espacio[3]= new JTextField();
			if(numArtic!= -1) espacio[3].setText(Integer.toString(Lista[indLista[numArtic]].getCantidad()));
			espacio[3].setBounds(220,118,200,20);
			soloNumeros(espacio[3]);
			espacio[4]= new JTextField(); 
			if(numArtic!= -1 && modo == 0) espacio[4].setText(((Libros) Lista[indLista[numArtic]]).getAutor()); 
			if(numArtic!= -1 && modo == 1) espacio[4].setText(((Revista) Lista[indLista[numArtic]]).getEditorial());
			if(numArtic!= -1 && modo == 2) espacio[4].setText(((Pelicula) Lista[indLista[numArtic]]).getDirector()); 
			espacio[4].setBounds(220,188,200,20);
			soloLetras(espacio[4]);
			espacio[5]= null;
			if(modo == 0 || modo == 2){
				espacio[5]= new JTextField();
				if(numArtic!= -1 && modo == 0) espacio[5].setText(((Libros) Lista[indLista[numArtic]]).getEditorial());
				if(numArtic!= -1 && modo == 2) espacio[5].setText(((Pelicula) Lista[indLista[numArtic]]).getGenero());
				espacio[5].setBounds(220, 223, 200, 20);
				soloLetras(espacio[5]);
			}		
			espacio[6]= null;
			if(modo == 0) {
				espacio[6]= new JTextField();
				if(numArtic!= -1) espacio[6].setText(((Libros) Lista[indLista[numArtic]]).getEdicion());
				espacio[6].setBounds(220,258,200,20);
				
			}
			
			//Colocaci�n de botones
			hileraImagen[0]= null;
			botonImagen= new JButton("Explorar");
			botonImagen.setBounds(220,153,100,20);
			botonImagen.setActionCommand("Imagen");
			botonImagen.setMnemonic(KeyEvent.VK_1);
			botonImagen.addActionListener(this);
			if(numArtic == -1) botonAgregar= new JButton("Agregar");
			else botonAgregar= new JButton("Editar");
			botonAgregar.setMnemonic(KeyEvent.VK_I);
			botonAgregar.setActionCommand("Agregar");
			botonAgregar.addActionListener(this);
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
			//Inserci�n de los componentes al frame
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
			if(modo == 0 || modo == 2)vArticulos.add(espacio[5]);
			if(modo == 0) vArticulos.add(espacio[6]);
			vArticulos.add(botonImagen);
			vArticulos.add(botonAgregar);
			vArticulos.add(botonCancelar);
			
			vArticulos.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	//Finalizar la tarea cuando se cierre la ventana
			vArticulos.setSize(465,370);	//Tama�o de la ventana
			vArticulos.setVisible(true);
			vArticulos.setResizable(false);
		}
	}
		
	/*Descripci�n: M�todo que filtra la lista de acuerdo a la b�squeda que se realice
	 * Entrada: Ninguna
	 * Salida: Ninguna
	 */
	private void buscarLista(){
		int i=0;
		int j=0;
		String hilera= cuadroBusLibro.getText();
		//System.out.println(hilera);
		String palabra= null;
		cargarNombre();
		ordenarLista(cantArtic);
		//Si el cuadro de b�squeda no tiene escrito nada
		if(hilera.equals("")==true){
			return;
		}
		while(i<cantArtic){
			if(i == cantArtic) break;
			if(rNomLibro.isSelected()==true) palabra= Lista[i].getNombre();
			else if(rGenLibro.isSelected()==true) palabra= Lista[i].getGenero();
			else if(rCalLibro.isSelected()==true) palabra=Integer.toString(Lista[i].getCalificacion());
			
			if(hilera.startsWith(palabra)==true){
				nombreLista.setValueAt(j, 0, Lista[i].getNombre());
				nombreLista.setValueAt(j, 1, Lista[i].getGenero());
				nombreLista.setValueAt(j, 2, Integer.toString(Lista[i].getCalificacion()));
				nombreLista.setValueAt(j, 3, Integer.toString(Lista[i].getCantidad()));
				indLista[j]= i;
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
	
	//Clase para enviar mensaje de error
	class MensajeError{
		/*Descripci�n: Despliega una ventana diciendo un mensaje de error
		 * Entrada: String con la primera parte del mensaje de error
		 * 			String con la segunda parte del mensaje de erro
		 * Salida: Ninguna
		 */
		JFrame vEliminar= new JFrame("Mensaje de Error");
		JLabel mensaje;
		JLabel mensaje1;
		JButton aceptar= new JButton("Aceptar");
		
		MensajeError(String msg1, String msg2){
			mensaje= new JLabel(msg1);
			mensaje1= new JLabel(msg2);
			mensaje.setBounds(30,20,300,25);
			mensaje.setHorizontalAlignment(SwingConstants.CENTER);
			mensaje1.setBounds(30, 45, 300, 25);
			mensaje1.setHorizontalAlignment(SwingConstants.CENTER);
			aceptar.setBounds(135,80,90,25);
			aceptar.setMnemonic(KeyEvent.VK_I);
			aceptar.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					vEliminar.dispose();
					return;
				}
			});
			
			vEliminar.setLayout(null);
			vEliminar.add(mensaje);
			vEliminar.add(mensaje1);
			vEliminar.add(aceptar);
			
			vEliminar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	//Cerrar la ventana
			vEliminar.setSize(360,150);
			vEliminar.setVisible(true);
			vEliminar.setResizable(false);
		}
	}
	
	//Clase para mostrar la ventana de confirmaci�n de eliminaci�n
	class VentanaEliminar{
		JFrame vEliminar= new JFrame("Eliminar Articulo");
		JLabel mensaje= new JLabel("�Esta seguro de eliminar al articulo del registro?");
		JButton aceptar= new JButton("Aceptar");
		JButton cancelar= new JButton("Cancelar");
		Articulo articulo= new Libros();
		int numArtic;
		
		/*Descripci�n: Constructor que crea la ventana de eliminaci�n de una persona
		 * Entrada: Int n�mero de persona a eliminar
		 * Salida: Ninguna
		 */
		VentanaEliminar(int pnumArtic){
			numArtic= pnumArtic;
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
	}
	
	/*Descripci�n: M�todo que se encarga de mostrar la ventana para la b�squeda de la imagen a importar
	 * Entrada: Ninguna
	 * Salida: String con la direcci�n del archivo seleccionado
	 */
	private String ventanaImagen(){
		JFrame vImportar= new JFrame("Ventana");
		JFileChooser archivo= new JFileChooser();
		String hilera= null;
		
		//Muestra la ventana de b�squeda
		archivo.showOpenDialog(vImportar);
		try{
			hilera=archivo.getSelectedFile().getAbsolutePath(); //Intenta obtener la direcci�n como String
		}
		catch(Exception e){hilera= null;}
		vImportar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	//Cerrar la ventana
		return hilera;
	}
	
	/*Descripci�n: M�todo que maneja la ventana para la importanci�n de registros externos
	 * Entrada: Ninguna
	 * Salida: Ninguna
	 */
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
	
	/*Descripci�n: M�todo que carga las personas en la lista para prestar un art�culo
	 * Entrada: Ninguna
	 * Salida: Ninguna
	 */
	private void CargarPersonas(){
		Personas temp= new Personas();
		int i= 0;
		while(true){
			//Obtiene en las clases sus atributos hasta que ya no encuentra
			//m�s en disco
			if(temp.Obtener(i+1, "Reg_Pers.txt") == false){break;}
			ListaPers[i]= new Personas();
			ListaPers[i].setNombre(temp.getNombre());
			ListaPers[i].setApellido1(temp.getApellido1());
			ListaPers[i].setApellido2(temp.getApellido2());
			ListaPers[i].setCorreo(temp.getCorreo());
			ListaPers[i].setTelefono(temp.getTelefono());
			ListaPers[i].setCategoria(temp.getCategoria());
			indListaPers[i]= i;
			nombreLis.setValueAt(i, 0, temp.getNombre());
			nombreLis.setValueAt(i, 1, temp.getApellido1());
			nombreLis.setValueAt(i, 2, temp.getApellido2());
			i++;
		}
		cantPers= i;
	}
	
	/*Descripci�n: M�todo para ordenar alfab�ticamente a las personas en la lista
	 * Entrada: Entero con la cantidad de personas
	 * Salida: Ninguna
	 */
	private void ordenarPers(int cant){
		int i=1;
		int indAux;
		String aux1,aux2,aux3;
		
		if(cant == 0 || cant == 1){ return;}
		while(i<cant){
			if(i == 0){
				i++;
			}
			//Si el elemento anterior tiene un caracter menor que el elemento actual
			else if(((String) nombreLis.getValueAt(i, 0)).charAt(0)< ((String) nombreLis.getValueAt(i-1, 0)).charAt(0) && i!= 0){
				//Cambia posiciones en lista con nombres
				aux1= (String) nombreLis.getValueAt(i, 0);
				aux2= (String) nombreLis.getValueAt(i, 1);
				aux3= (String) nombreLis.getValueAt(i, 2);
				nombreLis.setValueAt(i, 0, (String) nombreLis.getValueAt(i-1, 0));
				nombreLis.setValueAt(i, 1, (String) nombreLis.getValueAt(i-1, 1));
				nombreLis.setValueAt(i, 2, (String) nombreLis.getValueAt(i-1, 2));
				nombreLis.setValueAt(i-1, 0, aux1);
				nombreLis.setValueAt(i-1, 1, aux2);
				nombreLis.setValueAt(i-1, 2, aux3);
				//Cambia posiciones en lista con �ndices
				indAux= indListaPers[i];
				indListaPers[i]= indListaPers[i-1];
				indListaPers[i-1]= indAux;
				i--;
			}
			else{
				i++;
			}
		}
	}
	
	//Clase con la ventana de Prestar un art�culo
	class Prestar implements ActionListener{
		JFrame vPrestar = new JFrame("Prestar Articulo");
		JButton prestar= new JButton("Prestar");
		JLabel label= new JLabel("Cantidad disponible:");
		JLabel  cantidad= new JLabel();
		JLabel label2= new JLabel("D�as del prestamo:");
		JTextField inputDias= new JTextField();
		int cantDisponible;
		int numArtic;
		
		/*Descripci�n: Constructor de la ventana de prestar
		 * Entrada: Integer con el n�mero de ventana a prestar
		 * Salida: Ninguna
		 */
		Prestar(int pnumArtic){
			this.numArtic= pnumArtic;
			cantDisponible= Lista[indLista[numArtic]].getCantidad()-cantPrestado(indLista[numArtic]);
			//Modificaci�n de los headers de la tabla
			JTableHeader tableh = tablePrestar.getTableHeader();
			TableColumnModel tablecm = tableh.getColumnModel();
			TableColumn tablec = tablecm.getColumn(0);
			tablec.setHeaderValue("Nombre");
			tablec = tablecm.getColumn(1);
			tablec.setHeaderValue("Apellido 1");
			tablec = tablecm.getColumn(2);
			tablec.setHeaderValue("Apellido 2");
			tableh.repaint();
			
			CargarPersonas();
			ordenarPers(cantPers);
			
			label.setBounds(10,130,125,25);
			cantidad.setText(Integer.toString(cantDisponible));
			cantidad.setBounds(10, 150, 125, 25);
			cantidad.setForeground(Color.BLUE);
			cantidad.setHorizontalAlignment(SwingConstants.CENTER);
			JScrollPane barraDesplazamiento1 = new JScrollPane(tablePrestar);
			barraDesplazamiento1.setBounds(150,130,475,260);
			prestar.setBounds(10, 231, 125, 25);
			prestar.setMnemonic(KeyEvent.VK_I);
			prestar.addActionListener(this);
			label2.setBounds(10,175,125,25);
			inputDias.setBounds(10, 200, 125, 25);
			inputDias.setText("1");
			soloNumeros(inputDias);
			
			vPrestar.setLayout(null);
			vPrestar.add(barraDesplazamiento1);
			vPrestar.add(label);
			vPrestar.add(label2);
			vPrestar.add(prestar);
			vPrestar.add(cantidad);
			vPrestar.add(inputDias);
			
			
			vPrestar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	//Cerrar la ventana
			vPrestar.setSize(650,450);
			vPrestar.setVisible(true);
			vPrestar.setResizable(false);
		}
		
		public void actionPerformed(ActionEvent e){
			Prestamo temp= new Prestamo();
			if(cantDisponible == 0) new MensajeError("Error, el art�culo no puede ser prestado","No hay suficiente cantidad");
			else{
				temp.setNumeroArticulo(indLista[numArtic]);
				temp.setNumeroPersona(indListaPers[tablePrestar.getSelectedRow()]);
				if(modo == 0) temp.setTipoArticulo("Libro");
				else if(modo == 1) temp.setTipoArticulo("Revista");
				else if(modo == 2) temp.setTipoArticulo("Pelicula");
				temp.setCantDias(Integer.parseInt(inputDias.getText()));
				temp.Agregar("Prestamos.txt");
			}
			vPrestar.dispose();
		}
	}
	
	
	/*Descripci�n: Funcion que coloca los atributos de los botones
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
				new VentanaAgregar(-1);
			}
		});
		botonEditar= new JButton("Editar");
		botonEditar.setBounds(10,302,125,25);
		ImageIcon imageEditar= new ImageIcon("change.gif");
		botonEditar.setIcon(imageEditar);
		botonEditar.setMnemonic(KeyEvent.VK_I);
		botonEditar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				new VentanaAgregar(tabla.getSelectedRow());
			}
		});

		botonEliminar= new JButton("Eliminar");
		botonEliminar.setBounds(10,333,125,25);
		ImageIcon imageEliminar= new ImageIcon("delete.gif");
		botonEliminar.setIcon(imageEliminar);
		botonEliminar.setMnemonic(KeyEvent.VK_I);
		botonEliminar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(Lista[indLista[tabla.getSelectedRow()]].getIsPrestado() == true){
					new MensajeError("Error, el art�culo no puede ser eliminado","El art�culo se encuentra prestado");
				}
				else new VentanaEliminar(tabla.getSelectedRow());
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
		
		botonPrestar= new JButton("Prestar");
		botonPrestar.setBounds(10,395,125,25);
		//ImageIcon imageEditar= new ImageIcon("change.gif");
		//botonPrestar.setIcon(imageEditar);
		botonPrestar.setMnemonic(KeyEvent.VK_I);
		botonPrestar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				new Prestar(tabla.getSelectedRow());
			}
		});
		
		
	}
	
	//Clase para los objetos de la tabla de articulos
	@SuppressWarnings("serial")
	class ModeloDatos extends AbstractTableModel{
		Object datos[][]= null;

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
		
		ModeloDatos(){
			datos= new Object[120][4];
		}
		
		ModeloDatos(int num){
			if(num == 3) datos= new Object [120][3];
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
						if(hilera!= null) iconImagen= new ImageIcon("covers/"+hilera);
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
	VentanaArticulos(int mode){
		JFrame ventana= new JFrame("Lista de Art�culos");
		JScrollPane barraDesplazamiento = new JScrollPane(tabla);
		ImageIcon logo= new ImageIcon("logo.png");
		JLabel iLogo= new JLabel(logo);
		cuadroImage= new JPanel(new BorderLayout());
		cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		cellSelectionModel.addListSelectionListener(new ListenerTabla());
		BufferedImage img1= null;
		
		try{
			img1= ImageIO.read(new File("logo.png"));
		}
		catch(IOException e1){
			
		}
		
		modo= mode;
		barraDesplazamiento.setBounds(150,130,500,290); 

		//Modificaci�n de los headers de la tabla
		JTableHeader th = tabla.getTableHeader();
		TableColumnModel tcm = th.getColumnModel();
		TableColumn tc = tcm.getColumn(0);
		tc.setHeaderValue("Nombre");
		tc = tcm.getColumn(1);
		tc.setHeaderValue("G�nero");
		tc = tcm.getColumn(2);
		tc.setHeaderValue("Calificaci�n");
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
		
		//Colocaci�n del logo
		iLogo.setBounds(20,15,70,65);
		
		//radio buttons para la busqueda de libros
		rNomLibro = new JRadioButton("Nombre",true);
		rNomLibro.setBounds(130,90,70,25);
		rGenLibro = new JRadioButton("G�nero");
		rGenLibro.setBounds(200,90,70,25);
		rCalLibro = new JRadioButton("Calificaci�n");
		rCalLibro.setBounds(270,90,95,25);
		grupoBusqueda.add(rCalLibro);grupoBusqueda.add(rGenLibro);grupoBusqueda.add(rNomLibro);
		
		//Cuadro de entrada de texto para busqueda
		cuadroBusLibro = new JTextField();
		cuadroBusLibro.setBounds(380,90,170,25);
		
		//Inserci�n de los componentes a la ventana
		ventana.setLayout(null);
		ventana.add(barraDesplazamiento);
		ventana.add(botonAgregar);
		ventana.add(botonEditar);
		ventana.add(botonEliminar);
		ventana.add(botonImportar);
		ventana.add(botonPrestar);
		ventana.add(cuadroImage);
		ventana.add(cuadroBusLibro);
		ventana.add(botonBuscar);
		ventana.add(rNomLibro);
		ventana.add(rGenLibro);
		ventana.add(rCalLibro);
		ventana.add(iLogo);
		
		ventana.setIconImage(img1);
		ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	//Finalizar la tarea cuando se cierre la ventana
		ventana.setSize(675,480);	//Tama�o de la ventana
		ventana.setVisible(true);
		ventana.setResizable(false);
	}

}
