/* Primer Proyecto Programado
 * Alumno= Javier Rivas Lozano, Carne= 2013051762
 * Programación Orientada a Objetos
 */

import java.io.*;

public class Libros {
	//DECLARACIÓN DE ATRIBUTOS
	String Titulo;
	String Autor;
	String Editorial;
	String Edicion;
	String N_Imagen;	//Contiene la ruta de la imagen a cargar
	float Calificacion;
	
	//DECLARACIÓN DE MÉTODOS
	
	/*Descripcion: Función que observa si en el registro existe unlibro registrada con un dado número
	 * Entrada: Número de persona a buscar
	 * Salida: True si existe la persona, false si no existe
	 */
	boolean Existe_p(int num, String name){
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
				
				//Si encontró el separador entre libros
				if((char)Buffer[j] == ','){
					i++;	//Aumenta el número de libro encontrados
				}
				//Si encontró el número de libro registrado
				if(i == num){
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
	
	/*Descripción: Función que devuelve en los atributos de la clase los datos de un libro
	 * Entrada:	Número del libro
	 * Salida: Retorna true si encontró al libro, false si no existe
	 */
	boolean Obtener(int num, String name){
		int i= 1;
		int j= 0;
		char Buffer[]= new char[512];	//Creación del buffer de carácteres
		int cant_chars= 0;		//Cantidad de carácteres en el buffer
		File archivo= null;
		//Si existe el número de libro
		if(Existe_p(num,name) == false){
			return false;
		}
		try{
			archivo= new File(name);
			FileReader fr= new FileReader (archivo);
			cant_chars= fr.read(Buffer,0,512);		//Guarda en el buffer los carácteres
			
			//Mientras haya carácteres que leer
			while(cant_chars!= -1){
				
				//Si encontró el número de libro en el registro
				if(i == num){
					String palabra= "";
					i=0;
					while(i!=5){
						if(Buffer[j] == '_' || Buffer[j] == ','){
							if(i == 0){Autor= palabra;}
							else if(i == 1){Editorial= palabra;}
							else if(i == 2){Edicion= palabra;}
							else if(i == 3){N_Imagen= palabra;}
							else if(i == 4){Calificacion= Integer.parseInt(palabra);}
							i++;
							j++;
							palabra= "";
						}
						if(j == cant_chars){
							cant_chars= fr.read(Buffer,0,512);
							j=0;
						}
						palabra+= Buffer[j];
						j++;
					}
					return true;
				}
				//Si encontró el separador entre libros
				if((char)Buffer[j] == ','){
					i++;	//Aumenta el número de libros encontrados
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
		//Retorna false si no existe en el archivo
		catch(Exception e){
			return false;
		}
		return true;
	}
	
	/*Descripción: Función que guarda en un archivo de texto todos los atributos de los libros
	 * Entrada:	String con la ruta del archivo de registro a abrir
	 * Salida: Ninguna
	 */
	void Agregar(String nombre){
		String hilera= null;
		File archivo= new File(nombre);
		try{
			FileWriter escribir= new FileWriter(archivo,true);
			escribir.write(Autor+"_");
			escribir.write(Editorial+"_");
			escribir.write(Edicion+"_");
			escribir.write(N_Imagen+"_");
			escribir.write(Calificacion+",");
			escribir.close();
		}
		catch(Exception e){
			System.out.println("ERROR");
		}
	}
	
	/*Descripcion: Funcion que cambia en el registro con lo que se encuentra en los atributos de los libros
	 * Entrada: Número de libro a realizar los cambios
	 * Salida: Ninguna
	*/
	void Editar(int num){
		File archivo= new File("Reg_Libros.txt");
		File archivo1= new File("Hola.txt");
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
				//Si encontró el número de libro a editar
				if(i == num){
					escribir.flush();	//Actualiza en disco lo escrito
					Agregar("Hola.txt");	//Agrega los atributos del libro
					while(true){
						if(j == cant_chars){
							cant_chars= leer.read(Buffer,0,512);
							j=0;
						}
						//Si llegó a los datos del siguiente libro
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
				if(Buffer[j] == ','){i++;}	//Si pasó a los datos de otro libro
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
	
	/*Descripción: Función que une los datos de libros de un archivo al del
	 * archivo de Registro
	 * Entrada: String con la ruta del archivo
	 * Salida: True si se ejecutó con éxito, False si hubo algún error
	 */
	boolean Cargar(String name){
		int i=1;
		
		try{
			while(true){
				if(Existe_p(i,name) == false){break;}
				Obtener(i,name);
				Agregar("Reg_Libros.txt");
				i++;
			}
		}
		catch(Exception e){
			return false;
		}
		return true;
	}
}
