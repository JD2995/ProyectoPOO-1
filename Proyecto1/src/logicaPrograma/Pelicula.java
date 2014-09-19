package logicaPrograma;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class Pelicula extends Articulo{
	//DEFINICION DE ATRIBUTOS
	String director;
	String genero;
	
	//DEFINICION DE METODOS
	public String getDirector() {
		return director;
	}
	public void setDirector(String director) {
		this.director = director;
	}
	public String getGenero() {
		return genero;
	}
	public void setGenero(String genero) {
		this.genero = genero;
	}

	/*Descripci�n: Funci�n que devuelve en los atributos de la clase los datos de una pelicula
	 * Entrada:	N�mero del libro
	 * Salida: Retorna true si encontr� al libro, false si no existe
	 */
	public boolean Obtener(int num,String name){
		int i= 1;
		int j= 0;
		char Buffer[]= new char[512];	//Creaci�n del buffer de car�cteres
		int cant_chars= 0;		//Cantidad de car�cteres en el buffer
		File archivo= null;
		FileReader fr;
		//Si existe el n�mero de pelicula
		if(Existe_p(num,name) == false){
			return false;
		}
		try{
			archivo= new File(name);
			fr= new FileReader (archivo);
			cant_chars= fr.read(Buffer,0,512);		//Guarda en el buffer los car�cteres
			
			//Mientras haya car�cteres que leer
			while(cant_chars!= -1){
				
				//Si encontr� el n�mero de pel�cula registrado
				if(i == num){
					String palabra= "";
					i=0;
					while(i!=7){
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
							else if(i == 5)director= palabra;
							else if(i == 6)genero= palabra;
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
				//Si encontr� el separador entre peliculas
				if((char)Buffer[j] == ','){
					i++;	//Aumenta el n�mero de peliculas encontrados
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
		return true;
	}
	
	/*Descripci�n: Funci�n que guarda en un archivo de texto todos los atributos de las peliculas
	 * Entrada:	String con la ruta del archivo de registro a abrir
	 * Salida: Ninguna
	 */
	public void Agregar(String nombre){
		File archivo= new File(nombre);
		try{
			FileWriter escribir= new FileWriter(archivo,true);
			escribir.write(super.nombre+"_");
			escribir.write(super.genero+"_");
			escribir.write(calificacion+"_");
			escribir.write(cantidad+"_");
			escribir.write(nImagen+"_");
			escribir.write(director+"_");
			escribir.write(genero+",");
			escribir.close();
		}
		catch(Exception e){
			System.out.println("ERROR");
		}
	}
	
	/*Descripci�n: Funci�n que une los datos de libros de un archivo al del
	 * archivo de Registro
	 * Entrada: String con la ruta del archivo
	 * Salida: True si se ejecut� con �xito, False si hubo alg�n error
	 */
	public boolean Cargar(String name){
		int i=1;
		
		try{
			while(true){
				if(Existe_p(i,name) == false){break;}
				Obtener(i,name);
				Agregar("Peliculas.txt");
				i++;
			}
		}
		catch(Exception e){
			return false;
		}
		return true;
	}
}
