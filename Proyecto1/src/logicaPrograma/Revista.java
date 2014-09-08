package logicaPrograma;

import java.io.File;
import java.io.FileWriter;

public class Revista extends Articulo{
	//DEFINICION DE ATRIBUTOS
	String editorial;

	
	//DEFINICION DE METODOS
	public String getEditorial() {
		return editorial;
	}
	public void setEditorial(String editorial) {
		this.editorial = editorial;
	}

	public boolean Obtener(int num, String name) {
		return false;
	}
	
	/*Descripción: Función que guarda en un archivo de texto todos los atributos de las revistas
	 * Entrada:	String con la ruta del archivo de registro a abrir
	 * Salida: Ninguna
	 */
	public void Agregar(String nombre){
		File archivo= new File(nombre);
		try{
			FileWriter escribir= new FileWriter(archivo,true);
			escribir.write(nombre+"_");
			escribir.write(genero+"_");
			escribir.write(calificacion+"_");
			escribir.write(cantidad+"_");
			escribir.write(nImagen+",");
			escribir.write(editorial+"_");
			escribir.close();
		}
		catch(Exception e){
			System.out.println("ERROR");
		}
	}
}
