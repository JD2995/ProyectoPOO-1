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
	
	/*Descripci�n: Funci�n que guarda en un archivo de texto todos los atributos de las revistas
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
			escribir.write(nImagen+"_");
			escribir.write(editorial+",");
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
