package vclibs.communication;

//Clase almacenadora de constantes y funciones globales
public class Inf {
	
	//Constantes de estado y tipo de Conexión
	public static final String version = "20150228";
	public static final int NULL = 0;//estado, Desocupado
	public static final int WAITING = 1;//estado, En espera
	public static final int CONNECTED = 2;//estado, Conectado
	public static final int CLIENT = 1;//Tipo de conexión(tcon), Cliente
	public static final int SERVER = 2;//Tipo de conexión(tcon), Servidor
	
	//Constantes de Texto para la impresión del estado actual
	public static final String EN_ESPERA = "Awaiting";
	public static final String DATO_RECIBIDO = "Data received";
	public static final String CONECTADO = "Connection Established";
	public static final String EXCEPTION = "Process Exception";
	public static final String CONEXION_PERDIDA = "Connection Lost";
	public static final String ESPERA_DETENIDA = "Awaiting Stopt";
	public static final String ON_CANCELLED = "onCancelled";
	public static final String ON_POSTEXEC = "onPostexecute";

	public static final String IO_EXCEPTION = "IO Exception";
	public static final String SECURITY_EXCEPTION = "Security Exception";
	public static final String ILLEGALBLOCKINGMODE_EXCEPTION = "Illegal Blocking Mode Exception";
	public static final String IO_EXCEPTION_CLIENT = "Failed to Connect to desired IP and Port after 7sec";
	public static final String IO_EXCEPTION_SERVER = "Can't open desired port, please try another";
	 
	/**
	 * Función de impresión de información, depende de tcon y el texto
	 * @param tcon: tipo de conexión
	 * @param text: textp a imprimirse
	 */
	public static void println(int tcon, String text) {
		if(tcon == SERVER)
			System.out.println("Server - " + text);
		else if(tcon == CLIENT)
			System.out.println("Client - " + text);
	}
}