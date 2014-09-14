package logicaPrograma;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class Prestamo {
	//DECLARACI�N DE ATRIBUTOS
	private int numeroPersona;		//Identificador de la persona en el registro f�sico
	private int numeroArticulo;		//Identificador del articulo en el registro f�sico
	private int cantDias;	//Cantidad de d�as del pr�stamo
	private String tipoArticulo;	//Identificador del tipo de Articulo: Libro,Revista o Pelicula
	
	
	//DECLARACI�N DE M�TODOS
	
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
	
	/*Descripcion: Funci�n que observa si en el registro existe un prestamo registrada con un dado n�mero
	 * Entrada: N�mero de prestamo a buscar
	 * Salida: True si existe el prestamo, false si no existe
	 */
	public boolean Existe_p(int num, String name){
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
				
				//Si encontr� el separador entre los prestamos
				if((char)Buffer[j] == ','){
					i++;	//Aumenta el n�mero de prestamos encontradas
				}
				//Si encontr� el n�mero de prestamo registrado
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
	
	/*Descripcion: Funci�n que devuelve en los atributos de la clase los datos de un pr�stamo
	 * Entrada:	N�mero del pr�stamo
	 * Salida: Retorna true si encontr� al pr�stamo, false si no existe
	 */
	public boolean Obtener(int num, String name){
		int i= 1;
		int j= 0;
		char Buffer[]= new char[512];	//Creaci�n del buffer de car�cteres
		int cant_chars= 0;		//Cantidad de car�cteres en el buffer
		File archivo= null;
		FileReader fr;
		//Si existe el n�mero de pr�stamo
		if(Existe_p(num,name) == false){
			return false;
		}
		try{
			archivo= new File(name);
			fr= new FileReader (archivo);
			cant_chars= fr.read(Buffer,0,512);		//Guarda en el buffer los car�cteres
			
			//Mientras haya car�cteres que leer
			while(cant_chars!= -1){
				//Si encontr� el n�mero de pr�stamo registrado
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
				//Si encontr� el separador entre pr�stamos
				if((char)Buffer[j] == ','){
					i++;	//Aumenta el n�mero de pr�stamos encontrados
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
	
	/*Descripci�n: Funci�n que guarda en un archivo de texto todos los atributos de la clase
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
	
	/*Descripci�n: Funci�n que elimina los datos de un pr�stamo del registro
	 * Entrada: N�mero en el registro del pr�stamo a eliminar
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
				//Si encontro el n�mero de pr�stamo a eliminar
				if(i == num){
					escribir.flush();	//Actualiza el registro en disco
					//Pasa atraves de los datos de la persona sin escribirla en el nuevo registro
					while(true){
						if(j == cant_chars){
							cant_chars= leer.read(Buffer,0,512);
							j=0;
						}
						//Si lleg� a los datos del siguiente pr�stamo
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
				if(Buffer[j] == ','){i++;}	//Si pas� a los datos de otra pr�stamo
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
	
	/*Descripcion: Funcion que cambia en el registro con lo que se encuentra en los atributos de la clase
	 * Entrada: N�mero de pr�stamo a realizar los cambios
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
			int i=1;	//Contador de pr�stamos
			int j=0;	//Indice para caracter en el Buffer
						
			cant_chars= leer.read(Buffer, 0, 512);
			//Mientras haya caracteres en el archivo
			while(cant_chars!= -1){
				//Si encontr� el n�mero de pr�stamo a editar
				if(i == num){
					escribir.flush();	//Actualiza en disco lo escrito
					Agregar("Hola.txt");	//Agrega los atributos del pr�stamo
					while(true){
						if(j == cant_chars){
							cant_chars= leer.read(Buffer,0,512);
							j=0;
						}
						//Si lleg� a los datos del siguiente pr�stamo
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
				if(Buffer[j] == ','){i++;}	//Si pas� a los datos de otro pr�stamo
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
}
