package vclibs.communication.android;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.IllegalBlockingModeException;

import vclibs.communication.Eventos.OnComunicationListener;
import vclibs.communication.Eventos.OnConnectionListener;
import vclibs.communication.Inf;
import vclibs.communication.Senders;

//Clase de comunicación de Red para Android
public class Comunic extends AsyncTask<Void, byte[], Integer> {
	
	//Constantes deribadas de la Clase Inf
	public final String version = Inf.version;
	public final int NULL = Inf.NULL;//estado
	public final int WAITING = Inf.WAITING;//estado
	public final int CONNECTED = Inf.CONNECTED;//estado
	public final int CLIENT = Inf.CLIENT;//tcon
	public final int SERVER = Inf.SERVER;//tcon
	
	//Constantes para reportes de estado
	final byte[] EN_ESPERA = { 1 };
	final byte[] CONECTADO = { 2 };
	final byte[] EXCEPTION = { 3 };
	final byte[] CONEXION_PERDIDA = { 4 };
	final byte[] DATO_RECIBIDO = { 7 };
	final byte[] IO_EXCEPTION = { 8 };
	final byte[] SECURITY_EXCEPTION = { 9 };
	final byte[] ILLEGALBLOCKINGMODE_EXCEPTION = { 10 };
	
	InetSocketAddress isa;//Dirección a la cual conectarse
	int sPort;//Puerto de Servidor, valor por defecto: 2000
	Socket socket;//Medio de Conexión de Red
	ServerSocket serverSocket;//Medio de Conexión del Servidor
	DataInputStream inputSt;//Flujo de datos de entrada
	DataOutputStream outputSt;//Flujo de datos de salida
	boolean timeOutEnabled = false;
	Context context;//Contexto de la aplicación
	public int tcon = NULL;//Tipo de conexión actual
	boolean conectado = false;
	public int estado = NULL;//Estado actual
	
	//Variables para seleccionar qué imprimir en la Consola
	public boolean debug = true;
	public boolean idebug = true;
	public boolean edebug = true;
	
	public boolean Flag_LowLevel = true;
    public boolean Flag_LowLevelSig = false;

    public boolean littleEndian = false;

	//Eventos usados según el caso
	Senders senders;
	OnConnectionListener onConnListener;
	OnComunicationListener onCOMListener;

	/**
	 * Definir acciones ante eventos de conexión
	 * @param connListener: Instancia del Evento
	 */
	public void setConnectionListener(OnConnectionListener connListener) {
		onConnListener = connListener;
	}
	
	/**
	 * Definir acciones ante eventos de comunicación
	 * @param comListener: Instancia del Evento
	 */
	public void setComunicationListener(OnComunicationListener comListener) {
		onCOMListener = comListener;
	}


	/**
	 * Impresión de información y Toast referente al estado Actual
	 * @param text
	 */
	private void makeToast(String text) {
		if(idebug) {
			Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
			if(tcon == SERVER)
				Log.i("Server", text);
			else if(tcon == CLIENT)
				Log.i("Client", text);
		}
	}

	/**
	 * Impresión de información referente al estado Actual
	 * @param text
	 */
	private void iLog(String text) {
		if(idebug) {
//			Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
			if(tcon == SERVER)
				Log.i("Server", text);
			else if(tcon == CLIENT)
				Log.i("Client", text);
		}
	}

	/**
	 * Impresión de información de depuración
	 * @param text: Mensaje a imprimir
	 */
	private void dLog(String text) {
		if(debug) {
			if(tcon == SERVER)
				Log.d("Server",text);
			else if(tcon == CLIENT)
				Log.d("Client",text);
		}
	}

	//Constructor simple de la clase, solo inicialización de variables
	public Comunic() {
		estado = NULL;
	}

	/**
	 * Constructor de la clase para modo Cliente
	 * @param ui: Contexto de la aplicación
	 * @param ip: Dirección IP del servidor al cual conectarse
	 * @param port: Puerto del Servidor al cual conectarse
	 */
	public Comunic(Context ui, String ip, int port) {
		estado = NULL;
		tcon = CLIENT;
		context = ui;
		isa = new InetSocketAddress(ip, port);
	}

	/**
	 * Constructor de la clase para modo Servidor
	 * @param ui: Contexto de la aplicación
	 * @param port: Puerto a la espera de conexión
	 */
	public Comunic(Context ui, int port) {
		estado = NULL;
		tcon = SERVER;
		context = ui;
		sPort = port;
	}

	/**
	 * Función de envio de Texto
	 * @param dato
	 */
	public int enviar(String dato) {
//		Log.d("Comunic", "Enviar String: " + dato);
        int res = 0;
        try {
			if (estado == CONNECTED)
				res = senders.enviar(dato);
		} catch (IOException e) {
			dLog(e.getMessage());
			if(edebug)
				e.printStackTrace();
		}
        return res;
	}

	/**
	 * función de envio numérico, 1 Byte (rango de 0 a 255)
	 * @param dato
	 */
	public int enviar(int dato) {
		return enviar_Int8(dato);
    }

	public int enviar_ByteArray(byte[] dato) {
		int res = 0;
		try {
			if(estado == CONNECTED)
				res = senders.enviar_ByteArray(dato);
		}catch (IOException e) {
			dLog(e.getMessage());
			if(edebug)
				e.printStackTrace();
		}
		return res;
	}

	public int enviar_Int8(int dato) {
		int res = 0;
		try {
			if(estado == CONNECTED)
				res = senders.enviar_Int8(dato);
		}catch (IOException e) {
			dLog(e.getMessage());
			if(edebug)
				e.printStackTrace();
		}
		return res;
	}

	public int enviar_Int16(int dato) {
		int res = 0;
		try {
			if(estado == CONNECTED)
				res = senders.enviar_Int16(dato, littleEndian);
		}catch (IOException e) {
			dLog(e.getMessage());
			if(edebug)
				e.printStackTrace();
		}
		return res;
	}

	public int enviar_Int32(int dato) {
		int res = 0;
		try {
			if(estado == CONNECTED)
				res = senders.enviar_Int32(dato, littleEndian);
		}catch (IOException e) {
			dLog(e.getMessage());
			if(edebug)
				e.printStackTrace();
		}
		return res;
	}

	public int enviar_Int64(long dato) {
		int res = 0;
		try {
			if(estado == CONNECTED)
				res = senders.enviar_Int64(dato, littleEndian);
		}catch (IOException e) {
			dLog(e.getMessage());
			if(edebug)
				e.printStackTrace();
		}
		return res;
	}

	public int enviar_Float(float dato) {
		int res = 0;
		try {
			if(estado == CONNECTED)
				res = senders.enviar_Float(dato, littleEndian);
		}catch (IOException e) {
			dLog(e.getMessage());
			if(edebug)
				e.printStackTrace();
		}
		return res;
	}

	public int enviar_Double(double dato) {
		int res = 0;
		try {
			if(estado == CONNECTED)
				res = senders.enviar_Double(dato, littleEndian);
		}catch (IOException e) {
			dLog(e.getMessage());
			if(edebug)
				e.printStackTrace();
		}
		return res;
	}

    //Función de finalización de Conexión
	public void Cortar_Conexion() {
		try {
            if (estado == CONNECTED && socket != null) {
				socket.close();
                cancel(true);// socket = null;
			}
		} catch (IOException e) {
			dLog(e.getMessage());
			if(edebug)
				e.printStackTrace();
		}
	}

	//Función de finalización de Espera a conexión del servidor
	public void Detener_Espera() {
		try {
			if (estado == WAITING) {
				// cancel(true);
				if (serverSocket != null)
					serverSocket.close();
				iLog(Inf.ESPERA_DETENIDA);
			}
		} catch (IOException e) {
			dLog(e.getMessage());
			if(edebug)
				e.printStackTrace();
		}
	}
	
	//Función de finalización de actividad actual
	public void Detener_Actividad() {
		Cortar_Conexion();
		Detener_Espera();
	}

	//Acciones anteriores al inicio del hilo de ejecusión secundario
	@Override
	protected void onPreExecute() {
		estado = NULL;
		socket = null;
		serverSocket = null;
		conectado = false;
		super.onPreExecute();
	}

	//Función del hilo de ejecución secundario
	@Override
	protected Integer doInBackground(Void... params) {
		Integer resX = 0;
		try {
			if (tcon == CLIENT) {
				socket = new Socket();
//				if(socket != null) {
				publishProgress(EN_ESPERA);
                socket.connect(isa,7000);//reintentar por 7 segundos
//				} else
//					socket = null;
			} else if (tcon == SERVER) {
				serverSocket = new ServerSocket(sPort);
//				if (serverSocket != null) {
                publishProgress(EN_ESPERA);
                resX = (int)EN_ESPERA[0];
                socket = serverSocket.accept();
                serverSocket.close();
                serverSocket = null;
//				} else
//					socket = null;
			}
			if (socket != null && socket.isConnected()) {
				inputSt = new DataInputStream(socket.getInputStream());
				outputSt = new DataOutputStream(socket.getOutputStream());
				senders = new Senders(outputSt, edebug);
				conectado = true;
				publishProgress(CONECTADO);
				while (socket.isConnected() && conectado && !isCancelled()) {
					byte[] buffer = new byte[1024];
					int len = inputSt.read(buffer);
					if (len != -1) {
						byte[] blen = (len + "").getBytes();
						publishProgress(DATO_RECIBIDO, blen, buffer);
					}else
						publishProgress(CONEXION_PERDIDA);
				}
				conectado = false;
				inputSt.close();
				outputSt.close();
				if (socket != null)
					socket.close();
			}
		} catch (IOException e) {
			dLog(Inf.IO_EXCEPTION);
			resX = (int) IO_EXCEPTION[0];
			dLog(e.getMessage());
			if(edebug)
				e.printStackTrace();
		} catch (SecurityException e) {
			dLog(Inf.SECURITY_EXCEPTION);
			resX = (int) SECURITY_EXCEPTION[0];
			dLog(e.getMessage());
			if(edebug)
				e.printStackTrace();
		} catch (IllegalBlockingModeException e) {
			dLog(Inf.ILLEGALBLOCKINGMODE_EXCEPTION);
			resX = (int) ILLEGALBLOCKINGMODE_EXCEPTION[0];
			dLog(e.getMessage());
			if(edebug)
				e.printStackTrace();
		} finally {
			publishProgress(EXCEPTION);
		}
		return resX;
	}

	//Función para inicializar temporizador de corte de conexión
	public void EnTimeOut(final long ms) {
		if(!timeOutEnabled) {
			final int sender = tcon;
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						Thread.sleep(ms);
					} catch (InterruptedException e) {
						if(edebug)
							e.printStackTrace();
					}
				}

				@Override
				protected void finalize() throws Throwable {
					if (timeOutEnabled && estado == CONNECTED && tcon == sender) {
						Cortar_Conexion();
					}
					timeOutEnabled = false;
					super.finalize();
				}
			}).run();
		}
	}
	
	//Reporte de estado al hilo de ejecución principal
	@Override
	protected void onProgressUpdate(byte[]... values) {
		byte[] orden = values[0];
		if (orden == EN_ESPERA) {
			estado = WAITING;
			iLog(Inf.EN_ESPERA);
		} else if (orden == DATO_RECIBIDO) {
			int len = Integer.parseInt(new String(values[1]));
			byte[] buffer = values[2];
			String rcv = new String(buffer, 0, len);
			int[] nrcv = new int[len];
            byte[] brcv = new byte[len];
            if(Flag_LowLevelSig) {
                for(int i = 0; i < len; i++) {
                    nrcv[i] = buffer[i];
                    brcv[i] = buffer[i];
                }
            }else if(Flag_LowLevel) {
				for(int i = 0; i < len; i++) {
					nrcv[i] = 0xFF & buffer[i];
                    brcv[i] = buffer[i];
				}
			}
			if (onCOMListener != null)
				onCOMListener.onDataReceived(len, rcv, nrcv, brcv);
			iLog(Inf.DATO_RECIBIDO);
			dLog(rcv);
		} else if (orden == CONECTADO) {
			estado = CONNECTED;
			if (onConnListener != null)
				onConnListener.onConnectionstablished();
			iLog(Inf.CONECTADO);
		} else if (orden == EXCEPTION) {
			iLog(Inf.EXCEPTION);
			estado = NULL;
		} else if (orden == CONEXION_PERDIDA) {
			iLog(Inf.CONEXION_PERDIDA);
			Cortar_Conexion();
		}
		super.onProgressUpdate(values);
	}

	//Acciones ante cancelación de Actividad del hilo
	@Override
	protected void onCancelled() {
		dLog(Inf.ON_CANCELLED);
        onPostExecute(1);
		super.onCancelled();
	}

	//Acciones ante la finalización de acciones del hilo
	@Override
	protected void onPostExecute(Integer result) {
		estado = NULL;
		if (onConnListener != null)
			onConnListener.onConnectionfinished();
		if(result == (int)EN_ESPERA[0])
			makeToast("Cortado por problema de creación de Socket");
		if(result == (int) IO_EXCEPTION[0]) {
			if(tcon == SERVER)
				makeToast(Inf.IO_EXCEPTION_SERVER);
			else if(tcon == CLIENT)
				makeToast(Inf.IO_EXCEPTION_CLIENT);
		} if(result == (int) SECURITY_EXCEPTION[0])
			makeToast(Inf.SECURITY_EXCEPTION);
		if(result == (int) ILLEGALBLOCKINGMODE_EXCEPTION[0])
			makeToast(Inf.ILLEGALBLOCKINGMODE_EXCEPTION);
		dLog(Inf.ON_POSTEXEC);
		super.onPostExecute(result);
	}
}