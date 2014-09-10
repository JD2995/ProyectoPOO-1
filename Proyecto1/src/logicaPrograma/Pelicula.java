package logicaPrograma;

import java.io.File;
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
	public boolean Obtener(int num, String name) {
		return false;
	}
	
	/*Descripción: Función que guarda en un archivo de texto todos los atributos de las peliculas
	 * Entrada:	String con la ruta del archivo de registro a abrir
	 * Salida: Ninguna
	 */
	public void Agregar(String nombre){
		File archivo= new File(nombre);
		try{
			FileWriter escribir= new FileWriter(archivo,true);
			escribir.write(nombre+"_");
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
