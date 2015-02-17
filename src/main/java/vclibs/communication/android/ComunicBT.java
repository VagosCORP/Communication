package vclibs.communication.android;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import vclibs.communication.Eventos.OnComunicationListener;
import vclibs.communication.Eventos.OnConnectionListener;
import vclibs.communication.Inf;

public class ComunicBT extends AsyncTask<Void, byte[], Integer> {

	public final int NULL = 0;// estado
	public final int WAITING = 1;// estado
	public final int CONNECTED = 2;// estado
	public final int CLIENT = 1;// tcon
	public final int SERVER = 2;// tcon
	final byte[] EN_ESPERA = { 1 };
	final byte[] CONECTADO = { 2 };
	final byte[] IO_EXCEPTION = { 3 };
	final byte[] CONEXION_PERDIDA = { 4 };
	final byte[] DATO_RECIBIDOL = { 6 };
	final byte[] DATO_RECIBIDO = { 7 };
	final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	BluetoothSocket socket;
	BluetoothServerSocket serverSocket;
	DataInputStream inputSt;
	DataOutputStream outputSt;
	BluetoothDevice mDevice;
	BluetoothAdapter BTAdapter;
	TimeOut timeOut;
	boolean timeOutEnabled = false;
	Context context;
	public int tcon = NULL;
	boolean conectado = false;
	public int estado = NULL;
	public boolean Flag_LowLevel = true;
    public boolean Flag_LowLevelSig = false;
	public boolean debug = true;
	public boolean idebug = true;
	public boolean edebug = true;
	
	OnConnectionListener onConnListener;
	OnComunicationListener onCOMListener;

	public void setConnectionListener(OnConnectionListener connListener) {
		onConnListener = connListener;
	}
	public void setComunicationListener(OnComunicationListener comListener) {
		onCOMListener = comListener;
	}

	private void makeToast(String text) {
		if(idebug) {
//			Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
			if(tcon == SERVER)
				Log.d("Server",text);
			else if(tcon == CLIENT)
				Log.d("Client",text);
		}
	}

	private void wlog(String text) {
		if(debug) {
			if(tcon == SERVER)
				Log.d("Server",text);
			else if(tcon == CLIENT)
				Log.d("Client",text);
		}
	}

	public ComunicBT() {
		estado = NULL;
	}
	
	public ComunicBT(Context ui, BluetoothDevice btDev) {
		estado = NULL;
		tcon = CLIENT;
		context = ui;
		mDevice = btDev;
	}
	
	public ComunicBT(Context ui, BluetoothAdapter btAdap) {
		estado = NULL;
		tcon = SERVER;
		context = ui;
		BTAdapter = btAdap;
	}

	public void enviar(String dato) {
		try {
			if (estado == CONNECTED)
				outputSt.writeBytes(dato);
		} catch (IOException e) {
			wlog(e.getMessage());
			if(edebug)
				e.printStackTrace();
		}
	}

    public void enviar(int dato) {
        enviar_Int8(dato);
    }
    public void enviar_Int8(int dato) {
        try {
            if (estado == CONNECTED)
                outputSt.writeByte(dato);
        } catch (IOException e) {
            wlog(e.getMessage());
            if(edebug)
                e.printStackTrace();
        }
    }

    public void enviar_Int16(int dato) {
        try {
            if (estado == CONNECTED)
                outputSt.writeShort(dato);
        } catch (IOException e) {
            wlog(e.getMessage());
            if(edebug)
                e.printStackTrace();
        }
    }

    public void enviar_Int32(int dato) {
        try {
            if (estado == CONNECTED)
                outputSt.writeInt(dato);
        } catch (IOException e) {
            wlog(e.getMessage());
            if(edebug)
                e.printStackTrace();
        }
    }

    public void enviar_Int64(int dato) {
        try {
            if (estado == CONNECTED)
                outputSt.writeLong(dato);
        } catch (IOException e) {
            wlog(e.getMessage());
            if(edebug)
                e.printStackTrace();
        }
    }

    public void enviar(float dato) {
        try {
            if (estado == CONNECTED)
                outputSt.writeFloat(dato);
        } catch (IOException e) {
            wlog(e.getMessage());
            if(edebug)
                e.printStackTrace();
        }
    }

    public void enviar(double dato) {
        try {
            if (estado == CONNECTED)
                outputSt.writeDouble(dato);
        } catch (IOException e) {
            wlog(e.getMessage());
            if(edebug)
                e.printStackTrace();
        }
    }

	public void Cortar_Conexion() {
		try {
			DisTimeOut();
			if (estado == CONNECTED && socket != null) {
				socket.close();
				cancel(true);// socket = null;
			}
		} catch (IOException e) {
			wlog(e.getMessage());
			if(edebug)
				e.printStackTrace();
		}
	}

	public void Detener_Espera() {
		try {
			if (estado == WAITING) {
				// cancel(true);
				if (serverSocket != null)
					serverSocket.close();
				makeToast("Espera detenida");
			}
		} catch (IOException e) {
			wlog(e.getMessage());
			if(edebug)
				e.printStackTrace();
		}
	}

	public void Detener_Actividad() {
		Cortar_Conexion();
		Detener_Espera();
	}

	@Override
	protected void onPreExecute() {
		estado = NULL;
		socket = null;
		serverSocket = null;
		conectado = false;
		super.onPreExecute();
	}

	@Override
	protected Integer doInBackground(Void... params) {
		try {
			if (tcon == CLIENT) {
				socket = mDevice.createRfcommSocketToServiceRecord(myUUID);
//				if (socket != null) {
                socket.connect();
//				} else
//					socket = null;
			} else if (tcon == SERVER) {
				serverSocket = BTAdapter.listenUsingRfcommWithServiceRecord(
						"sas", myUUID);
				if (serverSocket != null) {
					publishProgress(EN_ESPERA);
					socket = serverSocket.accept();
					serverSocket.close();
					serverSocket = null;
				} else
					socket = null;
			}
			if (socket != null/* && socket.isConnected() */) {
				inputSt = new DataInputStream(socket.getInputStream());
				outputSt = new DataOutputStream(socket.getOutputStream());
				conectado = true;
				publishProgress(CONECTADO);
				while (/* socket.isConnected() && */conectado
						&& !isCancelled()) {
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
			wlog("IO Exception");
			publishProgress(IO_EXCEPTION);
			wlog(e.getMessage());
			if(edebug)
				e.printStackTrace();
		}
		return null;
	}

	public void EnTimeOut(final long ms) {
		if (!timeOutEnabled) {
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

	public void DisTimeOut() {
		if (timeOutEnabled) {
			timeOut.cancel(true);
		}
	}

	@Override
	protected void onProgressUpdate(byte[]... values) {
		byte[] orden = values[0];
		if (orden == EN_ESPERA) {
			estado = WAITING;
			makeToast(Inf.EN_ESPERA);
		} else if (orden == DATO_RECIBIDO) {
			int len = Integer.parseInt(new String(values[1]));
			byte[] buffer = values[2];
			String rcv = new String(buffer, 0, len);
			int[] nrcv = new int[len];
            if(Flag_LowLevelSig) {
                for(int i = 0; i < len; i++) {
                    nrcv[i] = buffer[i];
                }
            }else if(Flag_LowLevel) {
				for(int i = 0; i < len; i++) {
					nrcv[i] = 0xFF & buffer[i];
				}
			}
			if (onCOMListener != null)
				onCOMListener.onDataReceived(rcv, nrcv);
			makeToast(Inf.DATO_RECIBIDO);
		} else if (orden == CONECTADO) {
			estado = CONNECTED;
			if (onConnListener != null)
				onConnListener.onConnectionstablished();
			makeToast(Inf.CONECTADO);
		} else if (orden == IO_EXCEPTION) {
			// makeToast("IO Exception");
			estado = NULL;
		} else if (orden == CONEXION_PERDIDA) {
			makeToast(Inf.CONEXION_PERDIDA);
			Cortar_Conexion();
		}
		super.onProgressUpdate(values);
	}

	@Override
	protected void onCancelled() {
		makeToast("onCancelled");
		onPostExecute(1);
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(Integer result) {
		estado = NULL;
		if (onConnListener != null)
			onConnListener.onConnectionfinished();
		makeToast("onPostexecute");
		super.onPostExecute(result);
	}
}