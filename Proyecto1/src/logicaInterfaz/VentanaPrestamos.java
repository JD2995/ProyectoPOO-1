package logicaInterfaz;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
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

public class VentanaPrestamos {
	//DEFINICI�N DE ATRIBUTOS
	private int cantPrestamos= 0;	//Contador de pr�stamos en la tabla
	private ModeloDatos nombreLista= new ModeloDatos();
	private JTable tabla= new JTable(nombreLista);	//Tabla con los datos de los articulos
	private ListSelectionModel cellSelectionModel= tabla.getSelectionModel();
	private Prestamo Lista[]= new Prestamo[120];
	private int indLista[]= new int[120]; 
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
		
	}
	
	/*Descripci�n: Funci�n que elimina del registro un prestamo ya que fue devuelto
	 * Entrada: N�mero de elemento en el registro a devolver
	 * Salida: Ninguna
	 */
	private void devolverArticulo(int numArtic){
		Prestamo pres1= new Prestamo();
		
		pres1.eliminar(indLista[numArtic]);
		cargarNombre();
	}
	
	/*Descripci�n: Funci�n que disminuye en 1 los dias disponibles para el prestamo
	 * Entrada: Ninguna
	 * Salida: Ninguna
	 */
	private void pasarDia(){
		int i=0;
		int dias;
		cargarNombre();
		
		while(i<cantPrestamos){
			dias= Lista[indLista[i]].getCantDias();
			dias--;
			Lista[indLista[i]].setCantDias(dias);
			Lista[indLista[i]].Editar(indLista[i]);
			i++;
		}
		cargarNombre();
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
		limpiarListas();	//Limpia el contenido de los arrays
		int i= 0;
		while(true){
			//Obtiene en las clases sus atributos hasta que ya no encuentra
			//m�s en disco
			if(temp.Obtener(i+1, "Prestamos.txt") == false){break;}
			Lista[i]= new Prestamo();
			Lista[i].setNumeroArticulo(temp.getNumeroArticulo());
			Lista[i].setNumeroPersona(temp.getNumeroPersona());
			Lista[i].setCantDias(temp.getCantDias());
			Lista[i].setTipoArticulo(temp.getTipoArticulo());
			indLista[i]= i;
			//Obteniendo el nombre del art�culo
			if(temp.getTipoArticulo().equals("Libro")) {
				tempArtic= new Libros();
				tempArtic.Obtener(temp.getNumeroArticulo(), "Libros.txt");
				palabra= tempArtic.getNombre();
			}
			else if(temp.getTipoArticulo().equals("Revista")){
				tempArtic= new Revista();
				tempArtic.Obtener(temp.getNumeroArticulo(), "Revistas.txt");
				palabra= tempArtic.getNombre();
			}
			else if(temp.getTipoArticulo().equals("Pelicula")){
				tempArtic= new Pelicula();
				tempArtic.Obtener(temp.getNumeroArticulo(), "Peliculas.txt");
				palabra= tempArtic.getNombre();
			}
			nombreLista.setValueAt(i, 0, palabra);
			nombreLista.setValueAt(i, 1, temp.getTipoArticulo());
			//Obteniendo el nombre de la persona
			tempPer= new Personas();
			tempPer.Obtener(temp.getNumeroPersona(), "Reg_Pers.txt");
			palabra= tempPer.getNombre()+" "+tempPer.getApellido1();
			nombreLista.setValueAt(i, 2, palabra);
			nombreLista.setValueAt(i, 3, Integer.toString(temp.getCantDias()));
			i++;
		}
		cantPrestamos= i;		
	}
	
	private void colocarBotones(){
		botonDevolver= new JButton("Devolver");
		botonDevolver.setBounds(10,130,125,25);
		botonDevolver.setMnemonic(KeyEvent.VK_I);
		botonDevolver.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				devolverArticulo(tabla.getSelectedRow());
			}
		});
		botonDia= new JButton("Pasar d�a");
		botonDia.setBounds(10,161,125,25);
		botonDia.setMnemonic(KeyEvent.VK_1);
		botonDia.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				pasarDia();
			}
		});
		botonBuscar= new JButton("Buscar");
		botonBuscar.setBounds(560,95,90,25);
		botonBuscar.setMnemonic(KeyEvent.VK_I);
		botonBuscar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				filtrarLista();
			}
		});
		rNombre= new JRadioButton("Nombre",true);
		rNombre.setBounds(150, 95, 70, 25);
		rArticulo= new JRadioButton("Apellido1");
		rArticulo.setBounds(220,95,80,25);
		rTipo= new JRadioButton("Apellido2");
		rTipo.setBounds(300,95,80,25);
		grupoBusqueda.add(rNombre);grupoBusqueda.add(rArticulo);grupoBusqueda.add(rTipo);
	}
	
	public class MiRender extends DefaultTableCellRenderer
	{
	   public Component getTableCellRendererComponent(JTable table,
	      Object value,
	      boolean isSelected,
	      boolean hasFocus,
	      int row,
	      int column)
	   {
	      super.getTableCellRendererComponent (table, value, isSelected, hasFocus, row, column);
	      if ( column == 3 && row<cantPrestamos){
	    	 if(Lista[indLista[row]].getCantDias()>0 && Lista[indLista[row]]!=null){
	    		 this.setOpaque(true);
		         this.setBackground(Color.GREEN);
		         this.setForeground(Color.BLACK); 
	    	 }
	    	 else if(Lista[indLista[row]].getCantDias()<0 && Lista[indLista[row]]!=null){
	    		 this.setOpaque(true);
		         this.setBackground(Color.YELLOW);
		         this.setForeground(Color.BLACK); 
	    	 }
	    	 else if(Lista[indLista[row]].getCantDias()<35 && Lista[indLista[row]]!=null){
	    		 this.setOpaque(true);
		         this.setBackground(Color.RED);
		         this.setForeground(Color.BLACK); 
	    	 }
	         
	      } else {
	         this.setBackground(Color.WHITE);
	         this.setForeground(Color.BLACK);
	      }

	      return this;
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
	VentanaPrestamos(){
		JFrame ventana= new JFrame("Consulta de Pr�stamos");
		
		colocarBotones();
		//Modificaci�n de los headers de la tabla
		JTableHeader th = tabla.getTableHeader();
		TableColumnModel tcm = th.getColumnModel();
		TableColumn tc = tcm.getColumn(0);
		tc.setHeaderValue("Nombre");
		tc = tcm.getColumn(1);
		tc.setHeaderValue("Tipo");
		tc = tcm.getColumn(2);
		tc.setHeaderValue("Persona");
		tc = tcm.getColumn(3);
		tc.setHeaderValue("Dias de pr�stamo");
		th.repaint();
		
		cargarNombre();
		if(cantPrestamos>0) tabla.setDefaultRenderer (Object.class, new MiRender());
		
		cuadroBusc= new JTextField();
		cuadroBusc.setBounds(380,95,170,25);
		
		JScrollPane barraDesplazamiento = new JScrollPane(tabla);
		barraDesplazamiento.setBounds(150,130,500,260);
		
		ventana.setLayout(null);
		ventana.add(barraDesplazamiento);
		ventana.add(botonDevolver);
		ventana.add(botonDia);
		ventana.add(rNombre);
		ventana.add(rArticulo);
		ventana.add(rTipo);
		ventana.add(cuadroBusc);
		
		ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	//Finalizar la tarea cuando se cierre la ventana
		ventana.setSize(675,450);	//Tama�o de la ventana
		ventana.setVisible(true);
		ventana.setResizable(false);
	}

}
