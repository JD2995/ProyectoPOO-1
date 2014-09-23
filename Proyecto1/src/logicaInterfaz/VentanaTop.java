package logicaInterfaz;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import logicaPrograma.Articulo;
import logicaPrograma.Libros;
import logicaPrograma.Pelicula;
import logicaPrograma.Personas;
import logicaPrograma.Prestamo;
import logicaPrograma.Revista;

class VentanaTop{
	//DEFINICI�N DE ATRIBUTOS
	private int cantPrestamos= 0;	//Contador de pr�stamos en la tabla
	private int top= 10;	//Contador de elementos a mostrar
	private ModeloDatos nombreLista= new ModeloDatos();
	private JTable tabla= new JTable(nombreLista);	//Tabla con los datos de los articulos
	private Prestamo Lista[]= new Prestamo[120];
	private int indLista[]= new int[120];
	private int cantLista[]= new int[120];
	private JButton botonDevolver, botonDia,botonBuscar;
	private JTextField cuadroBusc;
	private JRadioButton rNombre, rArticulo,rTipo;
	private ButtonGroup grupoBusqueda = new ButtonGroup();
	
	
	//DEFINICI�N DE M�TODOS
	
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
	
	//No implementado aun
	private void filtrarLista(){
		int i=0;
		int j=0;
		String hilera= cuadroBusc.getText();
		String palabra= null;
		String aux= null;
		Personas tempPer= null;
		Articulo tempArtic= null;
		
		cargarNombre();
		//ordenarLista(cantPrestamos);
		//Si el cuadro de b�squeda no tiene escrito nada
		if(hilera.equals("")==true){
			return;
		}
		while(i<cantPrestamos){
			if(i == cantPrestamos) break;
			//Si la b�squeda es de nombre de la persona
			if(rNombre.isSelected()==true){
				tempPer= new Personas();
				tempPer.Obtener(Lista[i].getNumeroPersona(), "Reg_Pers.txt");
				palabra= tempPer.getNombre()+" "+tempPer.getApellido1();
			}
			//Si la b�squeda es de nombre del art�culo
			else if(rArticulo.isSelected()==true){
				if(Lista[i].getTipoArticulo().equals("Libro")) {
					tempArtic= new Libros();
					tempArtic.Obtener(Lista[i].getNumeroArticulo(), "Libros.txt");
				}
				else if(Lista[i].getTipoArticulo().equals("Revista")) {
					tempArtic= new Revista();
					tempArtic.Obtener(Lista[i].getNumeroArticulo(), "Revistas.txt");
				}
				else if(Lista[i].getTipoArticulo().equals("Pelicula")){
					tempArtic= new Pelicula();
					tempArtic.Obtener(Lista[i].getNumeroArticulo(), "Peliculas.txt");
				}
				palabra= tempArtic.getNombre();
			}
			//Si la b�squeda es de tipo del art�culo
			else if(rTipo.isSelected()==true) palabra= Lista[i].getTipoArticulo();
			if(hilera.startsWith(palabra)==true){
				//Obtenci�n y carga del nombre del art�culo
				if(Lista[i].getTipoArticulo().equals("Libro")) {
					tempArtic= new Libros();
					tempArtic.Obtener(Lista[i].getNumeroArticulo(), "Libros.txt");
				}
				else if(Lista[i].getTipoArticulo().equals("Revista")) {
					tempArtic= new Revista();
					tempArtic.Obtener(Lista[i].getNumeroArticulo(), "Revistas.txt");
				}
				else if(Lista[i].getTipoArticulo().equals("Pelicula")){
					tempArtic= new Pelicula();
					tempArtic.Obtener(Lista[i].getNumeroArticulo(), "Peliculas.txt");
				}
				aux= tempArtic.getNombre();
				nombreLista.setValueAt(j, 0, aux);
				//Obtenci�n y carga del tipo de Art�culo
				nombreLista.setValueAt(j, 1, Lista[i].getTipoArticulo());
				//Obtenci�n y carga del nombre de Persona
				tempPer= new Personas();
				tempPer.Obtener(Lista[i].getNumeroPersona(), "Reg_Pers.txt");
				aux= tempPer.getNombre()+" "+tempPer.getApellido1();
				nombreLista.setValueAt(j, 2, aux);
				//Obtenci�n y carga de la cantidad de d�as del pr�stamo
				nombreLista.setValueAt(j, 3, Integer.toString(Lista[i].getCantDias()));
				indLista[j]= i;
				//System.out.println(Lista);
				j++;
			}
			i++;
		}
		cantPrestamos= j;
		while(j<120){
			nombreLista.setValueAt(j, 0, "");
			nombreLista.setValueAt(j, 1, "");
			nombreLista.setValueAt(j, 2, "");
			nombreLista.setValueAt(j, 3, "");
			indLista[j]=0;
			j++;
		}
	}
	
	private void ordenamientoBurbuja(){
		int i=0;
		int temp;
		while(i<cantPrestamos){
			int j=0;
			while(j<cantPrestamos-1){
				if(cantLista[j]<cantLista[j+1]){
					temp= cantLista[j];
					cantLista[j]= cantLista[j+1];
					cantLista[j+1]= temp;
					temp= indLista[j];
					indLista[j]= indLista[j+1];
					indLista[j+1]= temp;
				}
				j++;
			}
			i++;
		}
	}
	
	/*Descripci�n: Funci�n que analiza si elemento se encuentra repetido dentro de la lista
	 *Entrada: Instancia de Prestamo, Entero con la cantidad de elementos en la Lista
	 *Salida: -1 si encontr� que no est� repetido, si no el indice donde encontr� la repetici�n 
	 */
	private int estaRepetido(Prestamo temp, int cant){
		int i=0;
		while(i<cant){
			if(Lista[i].getNumeroArticulo() == temp.getNumeroArticulo()){
				if(Lista[i].getTipoArticulo().equals(temp.getTipoArticulo()))
					return i;
			}
			i++;
		}
		return -1;
	}
	
	/*Descripci�n: Funci�n que carga en los arrays la informaci�n encontrada en el registro
	 * Entrada: Ninguna
	 * Salida: Ninguna
	 */
	private void cargarNombre(){
		Prestamo temp= new Prestamo();
		Personas tempPer= new Personas();
		Articulo tempArtic= null;
		String palabra= null;
		int bool=0;
		limpiarListas();	//Limpia el contenido de los arrays
		int i= 0;
		while(true){
			//Obtiene en las clases sus atributos hasta que ya no encuentra m�s en disco
			if(temp.Obtener(i+1, "Prestamos.txt") == false){break;}
			bool= estaRepetido(temp,i);
			//No encontr� el elemento repetido
			if(bool == -1){
				Lista[i]= new Prestamo();
				Lista[i].setCantDias(temp.getCantDias());
				Lista[i].setNumeroArticulo(temp.getNumeroArticulo());
				Lista[i].setNumeroPersona(temp.getNumeroPersona());
				Lista[i].setTipoArticulo(temp.getTipoArticulo());
				cantLista[i]++;
				indLista[i]= i;
			}
			//Si encontr� el elemento repetido
			else{
				cantLista[bool]++;
			}
			i++;
		}		
		cantPrestamos= i;
		ordenamientoBurbuja();	//Ordena de mayor a menor la lista
		i=0;
		//Coloca en la tabla los elementos a mostrar
		while(i<top){
			nombreLista.setValueAt(i, 0, Integer.toString(i+1));
			if(Lista[indLista[i]].getTipoArticulo().equals("Libro")) {
				tempArtic= new Libros();
				tempArtic.Obtener(Lista[indLista[i]].getNumeroArticulo()+1, "Libros.txt");
				palabra= tempArtic.getNombre();
			}
			else if(Lista[indLista[i]].getTipoArticulo().equals("Revista")){
				tempArtic= new Revista();
				tempArtic.Obtener(Lista[indLista[i]].getNumeroArticulo()+1, "Revistas.txt");
				palabra= tempArtic.getNombre();
			}
			else if(Lista[indLista[i]].getTipoArticulo().equals("Pelicula")){
				tempArtic= new Pelicula();
				tempArtic.Obtener(Lista[indLista[i]].getNumeroArticulo()+1, "Peliculas.txt");
				palabra= tempArtic.getNombre();
			}
			nombreLista.setValueAt(i, 1, palabra);
			nombreLista.setValueAt(i, 2, Lista[indLista[i]].getTipoArticulo());
			nombreLista.setValueAt(i, 3, Integer.toString(cantLista[i]));
			i++;
		}
		
	}
	
	//Clase para los objetos de la tabla de pr�stamos
		@SuppressWarnings("serial")
		class ModeloDatos extends AbstractTableModel{
			Object datos[][]= new Object[120][4];

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
	VentanaTop(){
		JFrame ventana= new JFrame("Top de art�culos prestados");
		
		//colocarBotones();
		//Modificaci�n de los headers de la tabla
		JTableHeader th = tabla.getTableHeader();
		TableColumnModel tcm = th.getColumnModel();
		TableColumn tc = tcm.getColumn(0);
		tc.setHeaderValue("N�mero");
		tc = tcm.getColumn(1);
		tc.setHeaderValue("Nombre");
		tc = tcm.getColumn(2);
		tc.setHeaderValue("Tipo Art�culo");
		tc = tcm.getColumn(3);
		tc.setHeaderValue("Cantidad de pr�stamos");
		th.repaint();
		
		cargarNombre();
		
		cuadroBusc= new JTextField();
		cuadroBusc.setBounds(380,95,170,25);
		
		JScrollPane barraDesplazamiento = new JScrollPane(tabla);
		barraDesplazamiento.setBounds(150,130,500,260);
		
		ventana.setLayout(null);
		ventana.add(barraDesplazamiento);
		
		ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	//Finalizar la tarea cuando se cierre la ventana
		ventana.setSize(675,450);	//Tama�o de la ventana
		ventana.setVisible(true);
		ventana.setResizable(false);
	}
}