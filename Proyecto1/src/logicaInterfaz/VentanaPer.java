package logicaInterfaz;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Toolkit;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import logicaPrograma.Personas;

public class VentanaPer {
	//DECLARACIÓN DE ATRIBUTOS
	private Personas Lista[]= new Personas[120];
	private int indLista[]= new int[120];	//Vector con el índice de los nombres de Lista
	private JButton botonAgregar, botonEditar, botonEliminar, botonBuscar, botonImportar;
	private JRadioButton rNombre, rApellido1, rApellido2, rColega, rFamiliar, rEstudiante;
	private ButtonGroup categoria = new ButtonGroup();
	private ButtonGroup grupo= new ButtonGroup();
	private JTextField cuadroBusc;
	private ModeloDatos nombreLista= new ModeloDatos();		//Modelo con los nombres y apellidos de las personas
	private JTable tabla= new JTable(nombreLista);	//Tabla con los datos de los articulos
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
			nombreLista.setValueAt(i, 0, "");
			nombreLista.setValueAt(i, 1, "");
			nombreLista.setValueAt(i, 2, "");
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
		Personas temp= new Personas();
		limpiarListas();	//Limpia el contenido de los arrays
		int i= 0;
		while(true){
			//Obtiene en las clases sus atributos hasta que ya no encuentra
			//más en disco
			if(temp.Obtener(i+1, "Reg_Pers.txt") == false){break;}
			Lista[i]= new Personas();
			Lista[i].setNombre(temp.getNombre());
			Lista[i].setApellido1(temp.getApellido1());
			Lista[i].setApellido2(temp.getApellido2());
			Lista[i].setCorreo(temp.getCorreo());
			Lista[i].setTelefono(temp.getTelefono());
			Lista[i].setCategoria(temp.getCategoria());
			indLista[i]= i;
			nombreLista.setValueAt(i, 0, temp.getNombre());
			nombreLista.setValueAt(i, 1, temp.getApellido1());
			nombreLista.setValueAt(i, 2, temp.getApellido2());
			i++;
		}
		cantPers= i;		
	}
	
	//metodo que permite escribir unicamente letras
	public void soloLetras(JTextField a){
		a.addKeyListener(new KeyAdapter(){
			public void keyTyped(KeyEvent e){
				char c = e.getKeyChar();
				if (!Character.isLetter(c)){
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
	
	//metodo que permite escribir unicamente números
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
	
	
	
	/*Descripción: Función que maneja la ventana para agregar una persona
	 * Entrada: Un int, si es 0 significa que va a agregar una nueva personas, sino es el número de persona a editar
	 * Salida: Ninguna
	 */
	private void ventanaPersona(int numPer){
		JFrame vAgregar;		//Creación de la ventana
		JTextField espacio1,espacio2,espacio3,espacio4,espacio5;	//Inicialización de los cuadros de entrada de texto
		JLabel label1,label2,label3,label4,label5,label6;
		JButton boton;
		
		if(numPer==-1) vAgregar= new JFrame("Agregar Persona");
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
		label6 = new JLabel("Categoria:");
		label6.setBounds(12,190,100,30);
		
		//Radio buttons para elegir la categoria de la persona a registrar
		
		rColega= new JRadioButton("Colega",true);
		rColega.setBounds(80, 195, 70, 25);
		rEstudiante= new JRadioButton("Estudiante");
		rEstudiante.setBounds(150, 195, 90, 25);
		rFamiliar= new JRadioButton("Familiar");
		rFamiliar.setBounds(240, 195, 80, 25);
		categoria.add(rColega); categoria.add(rEstudiante); categoria.add(rFamiliar);
		System.out.println(categoria);
		if (numPer!=-1){int cate = Lista[indLista[numPer]].getCategoria();
			if (cate==0){rColega.setSelected(true);}
			else if (cate==1){rEstudiante.setSelected(true);}
			else if (cate==2){rFamiliar.setSelected(true);}
			
		}
		//Colocación de los de cuadros de entrada
		
		espacio1= new JTextField();
		espacio1.setBounds(100,20,200,20);
		soloLetras(espacio1);
		if(numPer!=-1) espacio1.setText(Lista[indLista[numPer]].getNombre());
		espacio2= new JTextField();
		espacio2.setBounds(100,55,200,20);
		soloLetras(espacio2);
		if(numPer!=-1) espacio2.setText(Lista[indLista[numPer]].getApellido1());
		espacio3= new JTextField();
		espacio3.setBounds(100,90,200,20);
		soloLetras(espacio3);
		if(numPer!=-1) espacio3.setText(Lista[indLista[numPer]].getApellido2());
		espacio4= new JTextField();
		espacio4.setBounds(100,125,200,20);
		soloNumeros(espacio4);
		if(numPer!=-1) espacio4.setText(Integer.toString(Lista[indLista[numPer]].getTelefono()));
		espacio5= new JTextField();
		espacio5.setBounds(100,160,200,20);
		if(numPer!=-1) espacio5.setText(Lista[indLista[numPer]].getCorreo());
		
		//Colocación y acción del botón
		if(numPer==-1) boton= new JButton("Agregar");
		else boton= new JButton("Editar");
		boton.setBounds(100,235,125,25);
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
				if (rColega.isSelected()==true){ humano.setCategoria(0);	
				}
				if (rEstudiante.isSelected()==true){ humano.setCategoria(1);	
				}
				else if (rFamiliar.isSelected()==true){ humano.setCategoria(2);	
				}
				if(numPer==-1)humano.Agregar("Reg_Pers.txt");
				else humano.Editar(indLista[numPer]);
				vAgregar.dispose();
				cargarNombre();
				ordenarLista(cantPers);
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
		

		vAgregar.add(rColega);
		vAgregar.add(rEstudiante);
		vAgregar.add(rFamiliar);
		
		vAgregar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	//Finalizar la tarea cuando se cierre la ventana
		vAgregar.setSize(350,310);	//Tamaño de la ventana
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
	
	private void ventanaImportar(){
		Personas humano= new Personas();
		
		JFrame vImportar= new JFrame("Ventana");
		JFileChooser archivo= new JFileChooser();
		archivo.showOpenDialog(vImportar);
		try{humano.Cargar(archivo.getSelectedFile().getAbsolutePath());}
		catch(Exception e){}
		cargarNombre();
		ordenarLista(cantPers);
		vImportar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	//Cerrar la ventana
	}
	
	/*Descripción: Función que filtra las listas con el string dado en cuadroBusc
	 * Entrada: Ninguna
	 * Salida: Ninguna
	 */
	private void filtrarLista(){
		int i=0;
		int j=0;
		String hilera= cuadroBusc.getText();
		//System.out.println(hilera);
		String palabra= null;
		cargarNombre();
		ordenarLista(cantPers);
		//Si el cuadro de búsqueda no tiene escrito nada
		if(hilera.equals("")==true){
			
			return;
		}
		while(i<cantPers){
			if(i == cantPers) break;
			if(rNombre.isSelected()==true) palabra= Lista[i].getNombre();
			else if(rApellido1.isSelected()==true) palabra= Lista[i].getApellido1();
			else if(rApellido2.isSelected()==true) palabra= Lista[i].getApellido2();
			//System.out.println(palabra);
			if(hilera.startsWith(palabra)==true){
				nombreLista.setValueAt(j, 0, Lista[i].getNombre());
				nombreLista.setValueAt(j, 1, Lista[i].getApellido1());
				nombreLista.setValueAt(j, 2, Lista[i].getApellido2());
				indLista[j]= i;
				//System.out.println(Lista);
				j++;
			}
			i++;
		}
		cantPers= j;
		while(j<120){
			nombreLista.setValueAt(j, 0, "");
			nombreLista.setValueAt(j, 1, "");
			nombreLista.setValueAt(j, 2, "");
			indLista[j]=0;
			j++;
		}
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
				ventanaPersona(-1);
			}
		});
		botonEditar= new JButton("Editar");
		botonEditar.setBounds(10,161,125,25);
		ImageIcon imageChange= new ImageIcon("change.gif");
		botonEditar.setIcon(imageChange);
		botonEditar.setMnemonic(KeyEvent.VK_I);
		botonEditar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ventanaPersona(tabla.getSelectedRow());
			}
		});
		botonEliminar= new JButton("Eliminar");
		botonEliminar.setBounds(10,192,125,25);
		ImageIcon imageDelete= new ImageIcon("delete.gif");
		botonEliminar.setIcon(imageDelete);
		botonEliminar.setMnemonic(KeyEvent.VK_I);
		botonEliminar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ventanaEliminar(tabla.getSelectedRow());
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
		botonImportar= new JButton("Importar");
		botonImportar.setBounds(10,223,125,25);
		ImageIcon imageImportar= new ImageIcon("importar.gif");
		botonImportar.setIcon(imageImportar);
		botonImportar.setMnemonic(KeyEvent.VK_I);
		botonImportar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				ventanaImportar();
			}
		});
		rNombre= new JRadioButton("Nombre",true);
		rNombre.setBounds(150, 95, 70, 25);
		rApellido1= new JRadioButton("Apellido1");
		rApellido1.setBounds(220,95,80,25);
		rApellido2= new JRadioButton("Apellido2");
		rApellido2.setBounds(300,95,80,25);
		grupo.add(rNombre);grupo.add(rApellido1);grupo.add(rApellido2);
	}
	
	@SuppressWarnings("serial")
	//Clase para los objetos de la tabla de articulos
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
	
	public VentanaPer(){
		JFrame ventana= new JFrame("Lista de Personas");
		JLabel contPers;
		
		
		//Modificación de los headers de la tabla
		JTableHeader th = tabla.getTableHeader();
		TableColumnModel tcm = th.getColumnModel();
		TableColumn tc = tcm.getColumn(0);
		tc.setHeaderValue("Nombre");
		tc = tcm.getColumn(1);
		tc.setHeaderValue("Apellido 1");
		tc = tcm.getColumn(2);
		tc.setHeaderValue("Apellido 2");
		th.repaint();
		
		colocarBotones();
		cargarNombre();
		contPers= new JLabel(cantPers+" personas encontradas");
		ordenarLista(cantPers);
		JScrollPane barraDesplazamiento = new JScrollPane(tabla);
		barraDesplazamiento.setBounds(150,130,500,260); 
		
		//Cuadro de entrada de texto para busqueda
		cuadroBusc= new JTextField();
		cuadroBusc.setBounds(380,95,170,25);
		
		contPers.setBounds(0,440,150,25);
		
		ventana.setLayout(null);
		ventana.add(contPers);
		ventana.add(botonAgregar);
		ventana.add(botonEditar);
		ventana.add(botonEliminar);
		ventana.add(botonBuscar);
		ventana.add(botonImportar);
		ventana.add(rNombre);
		ventana.add(rApellido1);
		ventana.add(rApellido2);
		ventana.add(barraDesplazamiento);
		ventana.add(cuadroBusc);
		
		ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	//Finalizar la tarea cuando se cierre la ventana
		ventana.setSize(675,450);	//Tamaño de la ventana
		ventana.setVisible(true);
		ventana.setResizable(false);
	}
}
