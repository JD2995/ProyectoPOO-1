/* Primer Proyecto Programado
 * Alumno= Javier Rivas Lozano, Carne= 2013051762
 * Programación Orientada a Objetos
 */
package logicaPrograma;
import java.io.*;

//Clase con atributos y metodos del registro de Personas

public class Personas {
	//DECLARACIÓN DE ATRIBUTOS
	
	private String Nombre;	//Nombre de la persona
	private String Apellido1;	//Primer apellido de la persona
	private String Apellido2;	//Segundo apellido de la persona
	private int Telefono;	//Número de teléfono de la persona
	private String Correo;		//Direccion de correo electrónico
	private int Categoria;	//0 es estudiante. 1 es colega y 2 es familiar
	private Articulo articuloPrestado;
	private boolean poseeArticulo;
	
	
	//DECLARACIÓN DE MÉTODOS
	public String getNombre(){
		return Nombre;
	}
	public void setNombre(String cNombre){
		Nombre= cNombre;
	}
	public String getApellido1(){
		return Apellido1;
	}
	public void setApellido1(String cApellido){
		Apellido1= cApellido;
	}
	public String getApellido2(){
		return Apellido2;
	}
	public void setApellido2(String cApellido){
		Apellido2= cApellido;
	}
	public int getTelefono(){
		return Telefono;
	}
	public void setTelefono(int num){
		Telefono= num;
	}
	public String getCorreo(){
		return Correo;
	}
	public void setCorreo(String cCorreo){
		Correo= cCorreo;
	}
	public int getCategoria(){
		return Categoria;
	}
	public void setCategoria(int num){
		Categoria= num;
	}
	public Articulo getArticuloPrestado() {
		return articuloPrestado;
	}
	public void setArticuloPrestado(Articulo articuloPrestado) {
		this.articuloPrestado = articuloPrestado;
	}
	public boolean isPoseeArticulo() {
		return poseeArticulo;
	}
	public void setPoseeArticulo(boolean poseeArticulo) {
		this.poseeArticulo = poseeArticulo;
	}
	
	/*Descripcion: Función que observa si en el registro existe una persona registrada con un dado número
	 * Entrada: Número de persona a buscar
	 * Salida: True si existe la persona, false si no existe
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
				
				//Si encontró el separador entre personas
				if((char)Buffer[j] == ','){
					i++;	//Aumenta el número de personas encontradas
				}
				//Si encontró el número de persona registrada
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
	
	/*Descripcion: Función que devuelve en los atributos de la clase los datos de una persona
	 * Entrada:	Número de la persona
	 * Salida: Retorna true si encontró a la persona, false si no existe
	 */
	public boolean Obtener(int num, String name){
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
				
				//Si encontró el número de persona registrada
				if(i == num){
					String palabra= "";
					i=0;
					while(i!=6){
						if(j == cant_chars){
							cant_chars= fr.read(Buffer,0,512);
							j=0;
						}
						if(Buffer[j] == '_' || Buffer[j] == ','){
							if(i == 0){Nombre= palabra;}
							else if(i == 1){Apellido1= palabra;}
							else if(i == 2){Apellido2= palabra;}
							else if(i == 3){Telefono= Integer.parseInt(palabra);}
							else if(i == 4){Correo= palabra;}
							else if(i == 5){Categoria= Integer.parseInt(palabra);}
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
				//Si encontró el separador entre personas
				if((char)Buffer[j] == ','){
					i++;	//Aumenta el número de personas encontradas
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
			escribir.write(Nombre+"_");
			escribir.write(Apellido1+"_");
			escribir.write(Apellido2+"_");
			escribir.write(Telefono+"_");
			escribir.write(Correo+"_");
			escribir.write(Categoria+",");
			escribir.close();
		}
		catch(Exception e){
			System.out.println("ERROR");
		}
	}
	

	/*Descripción: Función que elimina los datos de una persona del registro
	 * Entrada: Número en el registro de la persona a eliminar
	 * Salida: Ninguna
	 */
	public void eliminar(int num){
		File archivo= new File("Reg_Pers.txt");
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
				//Si encontro el número de persona a eliminar
				if(i == num){
					escribir.flush();	//Actualiza el registro en disco
					//Pasa atraves de los datos de la persona sin escribirla en el nuevo registro
					while(true){
						if(j == cant_chars){
							cant_chars= leer.read(Buffer,0,512);
							j=0;
						}
						//Si llegó a los datos de la siguiente persona
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
				if(Buffer[j] == ','){i++;}	//Si pasó a los datos de otra persona
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
	 * Entrada: Número de persona a realizar los cambios
	 * Salida: Ninguna
	*/
	public void Editar(int num){
		File archivo= new File("Reg_Pers.txt");
		File archivo1= new File("Hola.txt");
		num++;
		try{
			archivo1.createNewFile();
			FileWriter escribir= new FileWriter(archivo1,true);
			FileReader leer= new FileReader(archivo);
			char Buffer[]= new char[512];
			int cant_chars=0;	//Contador de caracteres en el buffer
			int i=1;	//Contador de personas
			int j=0;	//Indice para caracter en el Buffer
						
			cant_chars= leer.read(Buffer, 0, 512);
			//Mientras haya caracteres en el archivo
			while(cant_chars!= -1){
				//Si encontró el número de persona a editar
				if(i == num){
					escribir.flush();	//Actualiza en disco lo escrito
					Agregar("Hola.txt");	//Agrega los atributos de la persona
					while(true){
						if(j == cant_chars){
							cant_chars= leer.read(Buffer,0,512);
							j=0;
						}
						//Si llegó a los datos de la siguiente persona
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
				if(Buffer[j] == ','){i++;}	//Si pasó a los datos de otra persona
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
	
	/*Descripción: Función que une los datos de personas de un archivo al del
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
				Agregar("Reg_Pers.txt");
				i++;
			}
		}
		catch(Exception e){
			return false;
		}
		return true;
	}
}
