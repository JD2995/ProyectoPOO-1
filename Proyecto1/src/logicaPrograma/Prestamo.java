package logicaPrograma;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class Prestamo {
	//DECLARACIÓN DE ATRIBUTOS
	private int numeroPersona;		//Identificador de la persona en el registro físico
	private int numeroArticulo;		//Identificador del articulo en el registro físico
	private int cantDias;	//Cantidad de días del préstamo
	private String tipoArticulo;	//Identificador del tipo de Articulo: Libro,Revista o Pelicula
	
	
	//DECLARACIÓN DE MÉTODOS
	
	public int getNumeroPersona() {
		return numeroPersona;
	}
	public void setNumeroPersona(int numeroPersona) {
		this.numeroPersona = numeroPersona;
	}
	public int getNumeroArticulo() {
		return numeroArticulo;
	}
	public void setNumeroArticulo(int numeroArticulo) {
		this.numeroArticulo = numeroArticulo;
	}
	public int getCantDias() {
		return cantDias;
	}
	public void setCantDias(int cantDias) {
		this.cantDias = cantDias;
	}
	public String getTipoArticulo() {
		return tipoArticulo;
	}
	public void setTipoArticulo(String tipoArticulo) {
		this.tipoArticulo = tipoArticulo;
	}
	
	/*Descripcion: Función que observa si en el registro existe un prestamo registrada con un dado número
	 * Entrada: Número de prestamo a buscar
	 * Salida: True si existe el prestamo, false si no existe
	 */
	public boolean Existe_p(int num, String name){
		int i= 0;
		int j= 0;
		char Buffer[]= new char[512];	//Creación del buffer de carácteres
		int cant_chars= 0;		//Cantidad de carácteres en el buffer
		File archivo= null;
		//Intenta abrir y leer el archivo
		try{
			archivo= new File(name);
			FileReader fr= new FileReader (archivo);
			cant_chars= fr.read(Buffer,0,512);		//Guarda en el buffer los carácteres
			
			//Mientras haya carácteres que leer
			while(cant_chars!= -1){
				
				//Si encontró el separador entre los prestamos
				if((char)Buffer[j] == ','){
					i++;	//Aumenta el número de prestamos encontradas
				}
				//Si encontró el número de prestamo registrado
				if(i == num){
					fr.close();
					return true;
				}
				j++;	//Aumenta el carácter a revisar en el registro
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
	
	/*Descripcion: Función que devuelve en los atributos de la clase los datos de un préstamo
	 * Entrada:	Número del préstamo
	 * Salida: Retorna true si encontró al préstamo, false si no existe
	 */
	public boolean Obtener(int num, String name){
		int i= 1;
		int j= 0;
		char Buffer[]= new char[512];	//Creación del buffer de carácteres
		int cant_chars= 0;		//Cantidad de carácteres en el buffer
		File archivo= null;
		FileReader fr;
		//Si existe el número de préstamo
		if(Existe_p(num,name) == false){
			return false;
		}
		try{
			archivo= new File(name);
			fr= new FileReader (archivo);
			cant_chars= fr.read(Buffer,0,512);		//Guarda en el buffer los carácteres
			
			//Mientras haya carácteres que leer
			while(cant_chars!= -1){
				//Si encontró el número de préstamo registrado
				if(i == num){
					String palabra= "";
					i=0;
					while(i!=4){
						if(j == cant_chars){
							cant_chars= fr.read(Buffer,0,512);
							j=0;
						}
						if(Buffer[j] == '_' || Buffer[j] == ','){
							if(i == 0){numeroPersona= Integer.parseInt(palabra);}
							else if(i == 1){numeroArticulo= Integer.parseInt(palabra);}
							else if(i == 2){cantDias= Integer.parseInt(palabra);}
							else if(i == 3){tipoArticulo= palabra;}
							i++;
							j++;
							palabra= "";
						}
						palabra+= Buffer[j];
						j++;
					}
					fr.close();
					return true;
				}
				//Si encontró el separador entre préstamos
				if((char)Buffer[j] == ','){
					i++;	//Aumenta el número de préstamos encontrados
				}
				j++;	//Aumenta el carácter a revisar en el registro
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
		return true;
	}
	
	/*Descripción: Función que guarda en un archivo de texto todos los atributos de la clase
	 * Entrada:	String con la ruta del archivo de registro a abrir
	 * Salida: Ninguna
	 */
	public void Agregar(String nombre){
		File archivo= new File(nombre);
		try{
			FileWriter escribir= new FileWriter(archivo,true);
			escribir.write(numeroPersona+"_");
			escribir.write(numeroArticulo+"_");
			escribir.write(cantDias+"_");
			escribir.write(tipoArticulo+",");
			escribir.close();
		}
		catch(Exception e){
			System.out.println("ERROR");
		}
	}
	
	/*Descripción: Función que elimina los datos de un préstamo del registro
	 * Entrada: Número en el registro del préstamo a eliminar
	 * Salida: Ninguna
	 */
	public void eliminar(int num){
		File archivo= new File("Prestamos.txt");
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
				//Si encontro el número de préstamo a eliminar
				if(i == num){
					escribir.flush();	//Actualiza el registro en disco
					//Pasa atraves de los datos de la persona sin escribirla en el nuevo registro
					while(true){
						if(j == cant_chars){
							cant_chars= leer.read(Buffer,0,512);
							j=0;
						}
						//Si llegó a los datos del siguiente préstamo
						if(Buffer[j] == ','){break;}
						j++;
					}
					escribir.flush();	//Actualiza en disco lo escrito
					j++;
					i++;					
				}
				//Si leyó todos los caracteres del Buffer
				if(j == cant_chars){
					cant_chars= leer.read(Buffer,0,512);
					j=0;
				}
				if(Buffer[j] == ','){i++;}	//Si pasó a los datos de otra préstamo
				if(cant_chars!= -1){escribir.write(Buffer[j]);}
				j++;		
				
			}
			leer.close();
			escribir.close();
			leer= null;
			escribir= null;
			System.gc();
		}catch(Exception e){
			System.out.println("Me caí");
			return;
		}
		archivo.delete();	//Elimina el archivo original
		archivo1.renameTo(archivo);		//Renombra el archivo auxiliar con el del original
	}
	
	/*Descripcion: Funcion que cambia en el registro con lo que se encuentra en los atributos de la clase
	 * Entrada: Número de préstamo a realizar los cambios
	 * Salida: Ninguna
	*/
	public void Editar(int num){
		File archivo= new File("Prestamos.txt");
		File archivo1= new File("Hola.txt");
		num++;
		try{
			archivo1.createNewFile();
			FileWriter escribir= new FileWriter(archivo1,true);
			FileReader leer= new FileReader(archivo);
			char Buffer[]= new char[512];
			int cant_chars=0;	//Contador de caracteres en el buffer
			int i=1;	//Contador de préstamos
			int j=0;	//Indice para caracter en el Buffer
						
			cant_chars= leer.read(Buffer, 0, 512);
			//Mientras haya caracteres en el archivo
			while(cant_chars!= -1){
				//Si encontró el número de préstamo a editar
				if(i == num){
					escribir.flush();	//Actualiza en disco lo escrito
					Agregar("Hola.txt");	//Agrega los atributos del préstamo
					while(true){
						if(j == cant_chars){
							cant_chars= leer.read(Buffer,0,512);
							j=0;
						}
						//Si llegó a los datos del siguiente préstamo
						if(Buffer[j] == ','){break;}
						j++;
					}
					escribir.flush();	//Actualiza en disco lo escrito
					j++;
					i++;
				}
				//Si leyó todos los caracteres del Buffer
				if(j == cant_chars){
					cant_chars= leer.read(Buffer,0,512);
					j=0;
				}
				if(Buffer[j] == ','){i++;}	//Si pasó a los datos de otro préstamo
				if(cant_chars!= -1){escribir.write(Buffer[j]);}
				j++;		
				
			}
			leer.close();
			escribir.close();
		}catch(Exception e){
			System.out.println("Me caí");
			return;
		}
		archivo.delete();	//Elimina el archivo original
		archivo1.renameTo(archivo);		//Renombra el archivo auxiliar con el del original
	}
}
