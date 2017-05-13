package vclibs.communication;


import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Senders {

    final DataOutputStream outputSt;
    final boolean edebug;

    public Senders(DataOutputStream DoS, boolean debug) {
        outputSt = DoS;
        edebug = debug;
    }

    public int enviar(final String dato) throws IOException {
        int res = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    outputSt.writeBytes(dato);
                }catch (IOException e) {
                    if(edebug)
                        e.printStackTrace();
                }
            }
        }).start();
        byte[] temp = dato.getBytes();
        for(int i = 0; i < dato.length(); i++)
            res += temp[i];
        return res;
    }

    public int enviar_Int8(final int dato) throws IOException {
        int res = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    outputSt.writeByte(dato);
                }catch (IOException e) {
                    if(edebug)
                        e.printStackTrace();
                }
            }
        }).start();
        byte[] temp = ByteBuffer.allocate(4).putInt(dato).array();
        res += 0xff&temp[0];
        res += 0xff&temp[1];
        res += 0xff&temp[2];
        res += 0xff&temp[3];
//	      Log.d("enviar_Int8", (0xff&temp[0]) + " " + (0xff&temp[1]) + " " + (0xff&temp[2]) + " " + (0xff&temp[3]) + " " + res);
        return res;
    }

    public int enviar_ByteArray(final byte[] dato) throws IOException {
        int res = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for(byte dat: dato)
                        outputSt.writeByte(dat);
                }catch (IOException e) {
                    if(edebug)
                        e.printStackTrace();
                }
            }
        }).start();
        for(byte dat: dato)
            res += 0xff&dat;
//	      Log.d("enviar_Int8", (0xff&temp[0]) + " " + (0xff&temp[1]) + " " + (0xff&temp[2]) + " " + (0xff&temp[3]) + " " + res);
        return res;
    }

    public int enviar_Int16(final int dato) throws IOException {
        int res = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    outputSt.writeShort(dato);
                }catch (IOException e) {
                    if(edebug)
                        e.printStackTrace();
                }
            }
        }).start();
        byte[] temp = ByteBuffer.allocate(4).putInt(dato).array();
        res += 0xff&temp[0];
        res += 0xff&temp[1];
        res += 0xff&temp[2];
        res += 0xff&temp[3];
//		  Log.d("enviar_Int16", (0xff&temp[0]) + " " + (0xff&temp[1]) + " " + (0xff&temp[2]) + " " + (0xff&temp[3]) + " " + res);
        return res;
    }

    public int enviar_Int32(final int dato) throws IOException {
        int res = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    outputSt.writeInt(dato);
                }catch (IOException e) {
                    if(edebug)
                        e.printStackTrace();
                }
            }
        }).start();
        byte[] temp = ByteBuffer.allocate(4).putInt(dato).array();
        res += 0xff&temp[0];
        res += 0xff&temp[1];
        res += 0xff&temp[2];
        res += 0xff&temp[3];
//        Log.d("enviar_Int32", (0xff & temp[0]) + " " + (0xff & temp[1]) + " " + (0xff & temp[2]) + " " + (0xff & temp[3]) + " " + res);
        return res;
    }

    public int enviar_Int64(final long dato) throws IOException {
        int res = 0;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    outputSt.writeLong(dato);
                }catch (IOException e) {
                    if(edebug)
                        e.printStackTrace();
                }
            }
        }).start();
        byte[] temp = ByteBuffer.allocate(8).putLong(dato).array();
        res += 0xff & temp[0];
        res += 0xff & temp[1];
        res += 0xff & temp[2];
        res += 0xff & temp[3];
        res += 0xff & temp[4];
        res += 0xff & temp[5];
        res += 0xff & temp[6];
        res += 0xff & temp[7];
        return res;
    }

    public int enviar_Float(final float dato) throws IOException {
        int res = 0;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    outputSt.writeFloat(dato);
                }catch (IOException e) {
                    if(edebug)
                        e.printStackTrace();
                }
            }
        }).start();
        byte[] temp = ByteBuffer.allocate(4).putFloat(dato).array();
        res += 0xff&temp[0];
        res += 0xff&temp[1];
        res += 0xff&temp[2];
        res += 0xff&temp[3];
//        Log.d("enviar_Float", (0xff & temp[0]) + " " + (0xff & temp[1]) + " " + (0xff & temp[2]) + " " + (0xff & temp[3]) + " " + res);
        return res;
    }

    public int enviar_Double(final double dato) throws IOException {
        int res = 0;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    outputSt.writeDouble(dato);
                }catch (IOException e) {
                    if(edebug)
                        e.printStackTrace();
                }
            }
        }).start();
        byte[] temp = ByteBuffer.allocate(8).putDouble(dato).array();
        res += 0xff&temp[0];
        res += 0xff&temp[1];
        res += 0xff&temp[2];
        res += 0xff&temp[3];
        res += 0xff&temp[4];
        res += 0xff&temp[5];
        res += 0xff&temp[6];
        res += 0xff&temp[7];
        return res;
    }

//    public static byte [] long2ByteArray (long value) {
//        return ByteBuffer.allocate(8).putLong(value).array();
//    }
//
//    public static byte [] float2ByteArray (float value) {
//        return ByteBuffer.allocate(4).putFloat(value).array();
//    }

}
