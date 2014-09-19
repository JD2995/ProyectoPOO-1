package logicaPrograma;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public abstract class Articulo {
	//DEFINICION DE ATRIBUTOS
	protected String nombre;
	protected String genero;
	protected int calificacion;
	protected int cantidad;
	protected String nImagen;	//Contiene la ruta de la imagen a cargar
	protected boolean isPrestado;
	
	//DEFINICION DE METODOS
	
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public int getCalificacion() {
		return calificacion;
	}
	public void setCalificacion(int calificacion) {
		this.calificacion = calificacion;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getGenero() {
		return genero;
	}
	public void setGenero(String genero) {
		this.genero = genero;
	}
	public void setNImagen(String image){
		this.nImagen= image;
	}
	public String getNImagen(){
		return this.nImagen;
	}
	
	public boolean getIsPrestado() {
		return isPrestado;
	}
	public void setPrestado(boolean isPrestado) {
		this.isPrestado = isPrestado;
	}
	
	public abstract boolean Obtener(int num,String name);
	
	public abstract void Agregar(String Nombre);
	
	public abstract boolean Cargar(String name);
	
	/*Descripcion: Funcion que cambia en el registro con lo que se encuentra en los atributos de los libros
	 * Entrada: N�mero de libro a realizar los cambios
	 * Salida: Ninguna
	*/
	public void Editar(int num, String name){
		File archivo= new File(name);
		File archivo1= new File("Hola.txt");
		num++;
		try{
			archivo1.createNewFile();
			FileWriter escribir= new FileWriter(archivo1,true);
			FileReader leer= new FileReader(archivo);
			char Buffer[]= new char[512];
			int cant_chars=0;	//Contador de caracteres en el buffer
			int i=1;	//Contador de libros
			int j=0;	//Indice para caracter en el Buffer
						
			cant_chars= leer.read(Buffer, 0, 512);
			//Mientras haya caracteres en el archivo
			while(cant_chars!= -1){
				//Si encontr� el n�mero de libro a editar
				if(i == num){
					escribir.flush();	//Actualiza en disco lo escrito
					Agregar("Hola.txt");	//Agrega los atributos del libro
					while(true){
						if(j == cant_chars){
							cant_chars= leer.read(Buffer,0,512);
							j=0;
						}
						//Si lleg� a los datos del siguiente libro
						if(Buffer[j] == ','){break;}
						j++;
					}
					escribir.flush();	//Actualiza en disco lo escrito
					j++;
					i++;
				}
				//Si ley� todos los caracteres del Buffer
				if(j == cant_chars){
					cant_chars= leer.read(Buffer,0,512);
					j=0;
				}
				if(Buffer[j] == ','){i++;}	//Si pas� a los datos de otro libro
				if(cant_chars!= -1){escribir.write(Buffer[j]);}
				j++;		
				
			}
			leer.close();
			escribir.close();
		}catch(Exception e){
			System.out.println("Me ca�");
			return;
		}
		archivo.delete();	//Elimina el archivo original
		archivo1.renameTo(archivo);		//Renombra el archivo auxiliar con el del original
	}
	
	/*Descripcion: Funci�n que observa si en el registro existe un articulo registrada con un dado n�mero
	 * Entrada: N�mero de persona a buscar
	 * Salida: True si existe el articulo, false si no existe
	 */
	public static boolean Existe_p(int num, String name){
		int i= 0;
		int j= 0;
		char Buffer[]= new char[512];	//Creaci�n del buffer de car�cteres
		int cant_chars= 0;		//Cantidad de car�cteres en el buffer
		File archivo= null;
		
		//Intenta abrir y leer el archivo
		try{
			archivo= new File(name);
			FileReader fr= new FileReader (archivo);
			cant_chars= fr.read(Buffer,0,512);		//Guarda en el buffer los car�cteres
			
			//Mientras haya car�cteres que leer
			while(cant_chars!= -1){
				//Si encontr� el separador entre articulos
				if((char)Buffer[j] == ','){
					i++;	//Aumenta el n�mero de articulos encontrados
				}
				//Si encontr� el n�mero de articulo registrado
				if(i == num){
					fr.close();
					return true;
				}
				j++;	//Aumenta el car�cter a revisar en el registro
				//Si ya analiza todos los caracteres en el Buffer
				if(j == cant_chars){
					cant_chars= fr.read(Buffer,0,512);
					j=0;
				}
			}
			fr.close();
		}
		//Retorna false si no existe el archivo
		catch(Exception e){
			return false;
		}
		return false;
	}
	
	/*Descripci�n: Funci�n que elimina los datos de un articulo del registro
	 * Entrada: N�mero en el registro del articulo a eliminar
	 * Salida: Ninguna
	 */
	public void eliminar(int num, String name){
		File archivo= new File(name);
		File archivo1= new File("Hola.txt");
		num++;
		try{
			archivo1.createNewFile();
			FileWriter escribir= new FileWriter(archivo1,true);
			FileReader leer= new FileReader(archivo);
			char Buffer[]= new char[512];
			int cant_chars=0;	//Contador de caracteres en el buffer
			int i=1;
			int j=0;
			
			cant_chars= leer.read(Buffer,0,512);
			//Mientras existan caracteres en el archivo
			while(cant_chars!= -1){
				//Si encontro el n�mero de persona a eliminar
				if(i == num){
					escribir.flush();	//Actualiza el registro en disco
					//Pasa atraves de los datos de la persona sin escribirla en el nuevo registro
					while(true){
						if(j == cant_chars){
							cant_chars= leer.read(Buffer,0,512);
							j=0;
						}
						//Si lleg� a los datos de la siguiente persona
						if(Buffer[j] == ','){break;}
						j++;
					}
					escribir.flush();	//Actualiza en disco lo escrito
					j++;
					i++;					
				}
				//Si ley� todos los caracteres del Buffer
				if(j == cant_chars){
					cant_chars= leer.read(Buffer,0,512);
					j=0;
				}
				if(Buffer[j] == ','){i++;}	//Si pas� a los datos de otra persona
				if(cant_chars!= -1){escribir.write(Buffer[j]);}
				j++;		
				
			}
			leer.close();
			escribir.close();
			leer= null;
			escribir= null;
			System.gc();
		}catch(Exception e){
			System.out.println("Me ca�");
			return;
		}
		archivo.delete();	//Elimina el archivo original
		archivo1.renameTo(archivo);		//Renombra el archivo auxiliar con el del original
	}
	
} // fin clase
