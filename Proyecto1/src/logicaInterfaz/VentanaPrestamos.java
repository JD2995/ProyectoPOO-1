package logicaInterfaz;

import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
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
import javax.swing.SwingConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import logicaPrograma.Articulo;
import logicaPrograma.EnviarEmail;
import logicaPrograma.Libros;
import logicaPrograma.Pelicula;
import logicaPrograma.Personas;
import logicaPrograma.Prestamo;
import logicaPrograma.Revista;

public class VentanaPrestamos {
	//DEFINICIÓN DE ATRIBUTOS
	private int cantPrestamos= 0;	//Contador de préstamos en la tabla
	private ModeloDatos nombreLista= new ModeloDatos();
	private JTable tabla= new JTable(nombreLista);	//Tabla con los datos de los articulos
	private Prestamo Lista[]= new Prestamo[120];
	private int indLista[]= new int[120]; 
	private JButton botonDevolver, botonDia,botonBuscar, botonTolerancia;
	private JTextField cuadroBusc, toler1, toler2;
	private JRadioButton rNombre, rArticulo,rTipo;
	private ButtonGroup grupoBusqueda = new ButtonGroup();
	private int tolerancia1= 5;		//Número con la tolerancia amarilla
	private int tolerancia2= 10;	//Número con la tolerancia roja
	
	
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
			nombreLista.setValueAt(i, 3, "");
			i++;
		}
	}
	
	/*Descripción: Método que abre una ventana preguntando si se desea enviar los recordatorios
	 * Entrada: Ninguna
	 * Salida: Ninguna
	 */
	private void mensajeEnviar(){
		JFrame vEnviar= new JFrame("Envio de recordatorios");
		JLabel mensaje= new JLabel("¿Desea enviar un mensaje de recordatorio?");
		JButton aceptar= new JButton("Aceptar");
		JButton cancelar= new JButton("Cancelar");
		
		
		mensaje.setBounds(30,20,300,25);
		mensaje.setHorizontalAlignment(SwingConstants.CENTER);
		aceptar.setBounds(85,50,90,25);
		aceptar.setMnemonic(KeyEvent.VK_I);
		aceptar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				vEnviar.dispose();
				enviarCorreo();		//Envia correos a las personas
				return;
			}
		});
		cancelar.setBounds(195,50,90,25);
		cancelar.setMnemonic(KeyEvent.VK_I);
		cancelar.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				vEnviar.dispose();
				return;
			}
		});
		
		vEnviar.setLayout(null);
		vEnviar.add(mensaje);
		vEnviar.add(aceptar);
		vEnviar.add(cancelar);
		
		vEnviar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	//Cerrar la ventana
		vEnviar.setSize(360,120);
		vEnviar.setLocation(300, 220);
		vEnviar.setVisible(true);
		vEnviar.setResizable(false);
	}
	
	class MensajeError{
		/*Descripción: Despliega una ventana diciendo un mensaje de error
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
	
	/*Descripción: Método que filtra la lista de elementos dependiendo de la búsqueda realizada
	 * Entrada: Ninguna
	 * Salida: Ninguna
	 */
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
		//Si el cuadro de búsqueda no tiene escrito nada
		if(hilera.equals("")==true){
			return;
		}
		while(i<cantPrestamos){
			if(i == cantPrestamos) break;
			//Si la búsqueda es de nombre de la persona
			if(rNombre.isSelected()==true){
				tempPer= new Personas();
				tempPer.Obtener(Lista[i].getNumeroPersona()+1, "Reg_Pers.txt");
				palabra= tempPer.getNombre()+" "+tempPer.getApellido1();
			}
			//Si la búsqueda es de nombre del artículo
			else if(rArticulo.isSelected()==true){
				if(Lista[i].getTipoArticulo().equals("Libro")) {
					tempArtic= new Libros();
					tempArtic.Obtener(Lista[i].getNumeroArticulo()+1, "Libros.txt");
				}
				else if(Lista[i].getTipoArticulo().equals("Revista")) {
					tempArtic= new Revista();
					tempArtic.Obtener(Lista[i].getNumeroArticulo()+1, "Revistas.txt");
				}
				else if(Lista[i].getTipoArticulo().equals("Pelicula")){
					tempArtic= new Pelicula();
					tempArtic.Obtener(Lista[i].getNumeroArticulo()+1, "Peliculas.txt");
				}
				palabra= tempArtic.getNombre();
			}
			//Si la búsqueda es de tipo del artículo
			else if(rTipo.isSelected()==true){
				palabra= Lista[i].getTipoArticulo();
			}
			if(hilera.startsWith(palabra)==true){
				//Obtención y carga del nombre del artículo
				if(Lista[i].getTipoArticulo().equals("Libro")) {
					tempArtic= new Libros();
					tempArtic.Obtener(Lista[i].getNumeroArticulo()+1, "Libros.txt");
				}
				else if(Lista[i].getTipoArticulo().equals("Revista")) {
					tempArtic= new Revista();
					tempArtic.Obtener(Lista[i].getNumeroArticulo()+1, "Revistas.txt");
				}
				else if(Lista[i].getTipoArticulo().equals("Pelicula")){
					tempArtic= new Pelicula();
					tempArtic.Obtener(Lista[i].getNumeroArticulo()+1, "Peliculas.txt");
				}
				aux= tempArtic.getNombre();
				nombreLista.setValueAt(j, 0, aux);
				//Obtención y carga del tipo de Artículo
				nombreLista.setValueAt(j, 1, Lista[i].getTipoArticulo());
				//Obtención y carga del nombre de Persona
				tempPer= new Personas();
				tempPer.Obtener(Lista[i].getNumeroPersona()+1, "Reg_Pers.txt");
				aux= tempPer.getNombre()+" "+tempPer.getApellido1();
				nombreLista.setValueAt(j, 2, aux);
				//Obtención y carga de la cantidad de días del préstamo
				nombreLista.setValueAt(j, 3, Integer.toString(Lista[i].getCantDias()));
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
			nombreLista.setValueAt(j, 3, "");
			indLista[j]=0;
			j++;
		}
		ordenarLista(cantPrestamos);
	}
	
	class MensajeEspera{
		JFrame vEspera= new JFrame("Espera un momento");
		JLabel mensaje= new JLabel("Espere, enviando recordatorios a:");
		JLabel mensaje1;
		JButton aceptar= new JButton("Espere");
		
		public void cerrarVentana(){
			vEspera.dispose();
		}
		
		MensajeEspera(String nombre){
			mensaje1= new JLabel(nombre);
			mensaje.setBounds(30,20,300,25);
			mensaje.setHorizontalAlignment(SwingConstants.CENTER);
			mensaje1.setBounds(30, 45, 300, 25);
			mensaje1.setHorizontalAlignment(SwingConstants.CENTER);
			aceptar.setBounds(135,80,90,25);
			
			vEspera.setLayout(null);
			vEspera.add(mensaje);
			vEspera.add(mensaje1);
			vEspera.add(aceptar);
			
			vEspera.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	//Cerrar la ventana
			vEspera.setSize(360,150);
			vEspera.setVisible(true);
			vEspera.setResizable(false);
		}
	}
	
	/*Descripción: Función que envia un email a las personas que han sobrepasado los dias de tolerancia
	 * Entrada: Ninguna
	 * Salida: Ninguna
	 */
	private void enviarCorreo(){
		Personas per= new Personas();
		EnviarEmail envio= null;
		MensajeEspera vEspera= null;
		int i=0;
		String mensaje1,mensaje2,mensaje3,mensaje4,cuerpo;
		String direccion;
		
		cargarNombre();	//Recarga la lista de prestamos
		
		//Mientras haya prestamos
		while(i<cantPrestamos){			
			//Si el prestamo rebaso el primer nivel de tolerancia
			if((Lista[indLista[i]].getCantDias()<=(0-tolerancia1))){
				//Partes del mensaje
				mensaje1= "Estimado ";
				mensaje2= "\n\nSoy parte de la biblioteca a la cual usted obtuvo el préstamo de un artículo."+
				" Me dirigo a usted debido a la devolución de dicho articulo.\nEl articulo \"";
				mensaje3= "\" el cual usted pidió prestado en nuestro sistema de préstamo ha rebasado la cantidad de tiempo"+
				" y días de tolerancia por exactamente ";
				mensaje4= ". Por lo tanto, le pedimos muy amablemente, que devuelva dicho artículo lo antes posible."+
				"\n\nLe saluda cordialmente,\nAdministrador del Gestor de préstamos";
				
				mensaje1+= (String) nombreLista.getValueAt(i, 2)+":";	//Añade el nombre de la persona
				mensaje2+= (String) nombreLista.getValueAt(i, 0);	//Añade el nombre del artículo
				mensaje3+= (Lista[indLista[i]].getCantDias()-(0-tolerancia1))*-1;	//Añade la cantidad de días pasados de la tolerancia
				cuerpo= mensaje1+mensaje2+mensaje3+mensaje4;
				per.Obtener(Lista[indLista[i]].getNumeroPersona()+1, "Reg_Pers.txt");	//Obtiene los atributos de la persona
				direccion= per.getCorreo();		//Obtiene dirreccion del email de la persona
				vEspera= new MensajeEspera((String) nombreLista.getValueAt(i, 2));	//Abre una ventana de espera
				envio= new EnviarEmail("gesbiblio2014.2@gmail.com",direccion,"Recordatorio de Préstamo",cuerpo);
				envio.enviarMensaje();	//Envia el mensaje
				vEspera.cerrarVentana();
			}
			i++;
		}
		
	}
	
	/*Descripción: Función que elimina del registro un prestamo ya que fue devuelto
	 * Entrada: Número de elemento en el registro a devolver
	 * Salida: Ninguna
	 */
	private void devolverArticulo(int numArtic){
		Prestamo pres1= new Prestamo();
		
		pres1.eliminar(indLista[numArtic]);
		cargarNombre();
	}
	
	/*Descripción: Función que disminuye en 1 los dias disponibles para el prestamo
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
	
	/*Descripción: Función que ordena la lista de prestamos dependiendo de la cantidad de días del prestamo
	 * Entrada: Entero con la cantidad de elementos en la lista
	 * Salida: Ninguna
	 */
	private void ordenarLista(int cant){
		int i=1;
		int indAux;
		String aux1,aux2,aux3,aux4;
		
		if(cant == 0 || cant == 1){ return;}
		while(i<cant){
			if(i == 0){
				i++;
			}
			//Si el elemento anterior tiene mayor cantidad de dias de prestamos que el siguiente elemento
			else if((Integer.parseInt((String) nombreLista.getValueAt(i, 3)) < Integer.parseInt((String) nombreLista.getValueAt(i-1, 3))) && i!=0){
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
		Prestamo temp= new Prestamo();
		Personas tempPer= new Personas();
		Articulo tempArtic= null;
		String palabra= null;
		limpiarListas();	//Limpia el contenido de los arrays
		int i= 0;
		while(true){
			//Obtiene en las clases sus atributos hasta que ya no encuentra
			//más en disco
			if(temp.Obtener(i+1, "Prestamos.txt") == false){break;}
			Lista[i]= new Prestamo();
			Lista[i].setNumeroArticulo(temp.getNumeroArticulo());
			Lista[i].setNumeroPersona(temp.getNumeroPersona());
			Lista[i].setCantDias(temp.getCantDias());
			Lista[i].setTipoArticulo(temp.getTipoArticulo());
			indLista[i]= i;
			//Obteniendo el nombre del artículo
			if(temp.getTipoArticulo().equals("Libro")) {
				tempArtic= new Libros();
				tempArtic.Obtener(temp.getNumeroArticulo()+1, "Libros.txt");
				palabra= tempArtic.getNombre();
			}
			else if(temp.getTipoArticulo().equals("Revista")){
				tempArtic= new Revista();
				tempArtic.Obtener(temp.getNumeroArticulo()+1, "Revistas.txt");
				palabra= tempArtic.getNombre();
			}
			else if(temp.getTipoArticulo().equals("Pelicula")){
				tempArtic= new Pelicula();
				tempArtic.Obtener(temp.getNumeroArticulo()+1, "Peliculas.txt");
				palabra= tempArtic.getNombre();
			}
			nombreLista.setValueAt(i, 0, palabra);
			nombreLista.setValueAt(i, 1, temp.getTipoArticulo());
			//Obteniendo el nombre de la persona
			tempPer= new Personas();
			tempPer.Obtener(temp.getNumeroPersona()+1, "Reg_Pers.txt");
			palabra= tempPer.getNombre()+" "+tempPer.getApellido1();
			nombreLista.setValueAt(i, 2, palabra);
			nombreLista.setValueAt(i, 3, Integer.toString(temp.getCantDias()));
			i++;
		}
		cantPrestamos= i;		
		ordenarLista(cantPrestamos);
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
	
	private void colocarBotones(){
		botonDevolver= new JButton("Devolver");
		botonDevolver.setBounds(10,130,125,25);
		ImageIcon imageDevolver= new ImageIcon("devolver.gif");
		botonDevolver.setIcon(imageDevolver);
		botonDevolver.setMnemonic(KeyEvent.VK_I);
		botonDevolver.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				devolverArticulo(tabla.getSelectedRow());
			}
		});
		botonDia= new JButton("Pasar día");
		botonDia.setBounds(10,161,125,25);
		ImageIcon imageDia= new ImageIcon("day.gif");
		botonDia.setIcon(imageDia);
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
		botonTolerancia= new JButton("Cambiar");
		botonTolerancia.setBounds(10, 316, 125, 25);
		botonTolerancia.setMnemonic(KeyEvent.VK_I);
		botonTolerancia.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				int x1= Integer.parseInt(toler1.getText());
				int x2= Integer.parseInt(toler2.getText());
				
				if(x1>=x2){
					new MensajeError("Error, el número de tolerancia Pasiva debe","ser menor que el número de Tolerancia Excedida");
				}
				else{
					tolerancia1= Integer.parseInt(toler1.getText());
					tolerancia2= Integer.parseInt(toler2.getText());
					cargarNombre();
					mensajeEnviar();
				}
			}
		});
		rNombre= new JRadioButton("Persona",true);
		rNombre.setBounds(150, 95, 80, 25);
		rArticulo= new JRadioButton("Articulo");
		rArticulo.setBounds(225,95,80,25);
		rTipo= new JRadioButton("Tipo");
		rTipo.setBounds(305,95,70,25);
		grupoBusqueda.add(rNombre);grupoBusqueda.add(rArticulo);grupoBusqueda.add(rTipo);
	}
	
	@SuppressWarnings("serial")
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
	    	  //Si la cantidad de dias es mayor que la tolerancia 1
	    	 if(Lista[indLista[row]].getCantDias()>(0-tolerancia1) && Lista[indLista[row]]!=null){
	    		 this.setOpaque(true);
		         this.setBackground(Color.GREEN);
		         this.setForeground(Color.BLACK); 
	    	 }
	    	 //Si la cantidad de dias es menor que la tolerancia 1 pero mayor que la tolerancia 2
	    	 else if((Lista[indLista[row]].getCantDias()<=(0-tolerancia1) && (Lista[indLista[row]].getCantDias()>(0-tolerancia2))) && Lista[indLista[row]]!=null){
	    		 this.setOpaque(true);
		         this.setBackground(Color.YELLOW);
		         this.setForeground(Color.BLACK); 
	    	 }
	    	 //Si la cantidad de dias es menor que la tolerancia 2
	    	 else if(Lista[indLista[row]].getCantDias()<=(0-tolerancia2) && Lista[indLista[row]]!=null){
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
	
	//Clase para los objetos de la tabla de préstamos
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
		JFrame ventana= new JFrame("Consulta de Préstamos");
		JLabel label1,label2;
		ImageIcon logo= new ImageIcon("logo.png");
		JLabel iLogo= new JLabel(logo);
		BufferedImage img= null;
		
		try{
			img= ImageIO.read(new File("logo.png"));
		}
		catch(IOException e){
			
		}
		
		colocarBotones();
		//Modificación de los headers de la tabla
		JTableHeader th = tabla.getTableHeader();
		TableColumnModel tcm = th.getColumnModel();
		TableColumn tc = tcm.getColumn(0);
		tc.setHeaderValue("Nombre");
		tc = tcm.getColumn(1);
		tc.setHeaderValue("Tipo");
		tc = tcm.getColumn(2);
		tc.setHeaderValue("Persona");
		tc = tcm.getColumn(3);
		tc.setHeaderValue("Dias de préstamo");
		th.repaint();
		
		cargarNombre();
		if(cantPrestamos>0) tabla.setDefaultRenderer (Object.class, new MiRender());
		
		cuadroBusc= new JTextField();
		cuadroBusc.setBounds(380,95,170,25);
		
		label1= new JLabel("Tolerancia Pasiva:");
		label1.setBounds(10,192,125,25);
		toler1= new JTextField();
		toler1.setText("5");
		toler1.setBounds(10, 223, 125, 25);
		soloNumeros(toler1);
		label2= new JLabel("Tolerancia Excedida:");
		label2.setBounds(10,254,125,25);
		toler2= new JTextField();
		toler2.setText("10");
		toler2.setBounds(10, 285, 125, 25);
		soloNumeros(toler2);
		
		JScrollPane barraDesplazamiento = new JScrollPane(tabla);
		barraDesplazamiento.setBounds(150,130,500,260);
		
		//Colocación del logo
		iLogo.setBounds(20,15,70,65);
		
		ventana.setLayout(null);
		ventana.setIconImage(img);
		ventana.add(barraDesplazamiento);
		ventana.add(botonDevolver);
		ventana.add(botonDia);
		ventana.add(botonBuscar);
		ventana.add(botonTolerancia);
		ventana.add(rNombre);
		ventana.add(rArticulo);
		ventana.add(rTipo);
		ventana.add(cuadroBusc);
		ventana.add(label1);
		ventana.add(label2);
		ventana.add(toler1);
		ventana.add(toler2);
		ventana.add(iLogo);
		
		ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);	//Finalizar la tarea cuando se cierre la ventana
		ventana.setSize(675,450);	//Tamaño de la ventana
		ventana.setVisible(true);
		ventana.setResizable(false);
		
		//Si la ventana fue abierta al inicio de la aplicación
		if(Ventana1.principio == true){
			Ventana1.principio= false;
			mensajeEnviar();
		}
	}

}
