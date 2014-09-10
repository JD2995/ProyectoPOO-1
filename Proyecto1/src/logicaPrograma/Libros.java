/* Primer Proyecto Programado
 * Alumno= Javier Rivas Lozano, Carne= 2013051762
 * Programación Orientada a Objetos
 */
package logicaPrograma;
import java.io.*;

public class Libros extends Articulo{
	//DECLARACIÓN DE ATRIBUTOS
	private String autor;
	private String editorial;
	private String edicion;
	
	//DECLARACIÓN DE MÉTODOS
	
	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getEditorial() {
		return editorial;
	}

	public void setEditorial(String editorial) {
		this.editorial = editorial;
	}

	public String getEdicion() {
		return edicion;
	}

	public void setEdicion(String edicion) {
		this.edicion = edicion;
	}

	/*Descripción: Función que devuelve en los atributos de la clase los datos de un libro
	 * Entrada:	Número del libro
	 * Salida: Retorna true si encontró al libro, false si no existe
	 */
	public boolean Obtener(int num,String name){
		int i= 1;
		int j= 0;
		char Buffer[]= new char[512];	//Creación del buffer de carácteres
		int cant_chars= 0;		//Cantidad de carácteres en el buffer
		File archivo= null;
		FileReader fr;
		//Si existe el número de persona
		if(Existe_p(num,name) == false){
			return false;
		}
		try{
			archivo= new File(name);
			fr= new FileReader (archivo);
			cant_chars= fr.read(Buffer,0,512);		//Guarda en el buffer los carácteres
			
			//Mientras haya carácteres que leer
			while(cant_chars!= -1){
				
				//Si encontró el número de libro registrado
				if(i == num){
					String palabra= "";
					i=0;
					while(i!=8){
						if(j == cant_chars){
							cant_chars= fr.read(Buffer,0,512);
							j=0;
						}
						if(Buffer[j] == '_' || Buffer[j] == ','){
							if(i == 0){nombre= palabra;}
							else if(i == 1)genero= palabra;
							else if(i == 2)calificacion= Integer.parseInt(palabra);
							else if(i == 3)cantidad= Integer.parseInt(palabra);
							else if(i == 4)nImagen= palabra;
							else if(i == 5)autor= palabra;
							else if(i == 6)editorial= palabra;
							else if(i == 7)edicion= palabra;
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
		//Retorna false si no existe el archivo
		catch(Exception e){
			return false;
		}
		return true;
	}
	
	/*Descripción: Función que guarda en un archivo de texto todos los atributos de los libros
	 * Entrada:	String con la ruta del archivo de registro a abrir
	 * Salida: Ninguna
	 */
	public void Agregar(String name){
		File archivo= new File(name);
		try{
			FileWriter escribir= new FileWriter(archivo,true);
			escribir.write(nombre+"_");
			escribir.write(genero+"_");
			escribir.write(calificacion+"_");
			escribir.write(cantidad+"_");
			escribir.write(nImagen+"_");
			escribir.write(autor+"_");
			escribir.write(editorial+"_");
			escribir.write(edicion+",");
			escribir.close();
		}
		catch(Exception e){
			System.out.println("ERROR");
		}
	}
	
	/*Descripción: Función que une los datos de libros de un archivo al del
	 * archivo de Registro
	 * Entrada: String con la ruta del archivo
	 * Salida: True si se ejecutó con éxito, False si hubo algún error
	 */
	public boolean Cargar(String name){
		int i=1;
		
		try{
			while(true){
				if(Existe_p(i,name) == false){break;}
				Obtener(i,name);
				Agregar("Libros.txt");
				i++;
			}
		}
		catch(Exception e){
			return false;
		}
		return true;
	}
}
