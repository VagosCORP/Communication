package vclibs.communication;


import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Senders {

    DataOutputStream outputSt;

    public Senders(DataOutputStream DoS) {
        outputSt = DoS;
    }

    public int enviar_Int8(int dato) throws IOException {
        int res = 0;
        try {
            outputSt.writeByte(dato);
            byte[] temp = ByteBuffer.allocate(4).putInt(dato).array();
            res += 0xff&temp[0];
            res += 0xff&temp[1];
            res += 0xff&temp[2];
            res += 0xff&temp[3];
//			  Log.d("enviar_Int8", (0xff&temp[0]) + " " + (0xff&temp[1]) + " " + (0xff&temp[2]) + " " + (0xff&temp[3]) + " " + res);
        }catch (IOException e) {
            throw e;
        }
        return res;
    }

    public int enviar_Int16(int dato) throws IOException {
        int res = 0;
        try {
            outputSt.writeShort(dato);
            byte[] temp = ByteBuffer.allocate(4).putInt(dato).array();
            res += 0xff&temp[0];
            res += 0xff&temp[1];
            res += 0xff&temp[2];
            res += 0xff&temp[3];
//			  Log.d("enviar_Int16", (0xff&temp[0]) + " " + (0xff&temp[1]) + " " + (0xff&temp[2]) + " " + (0xff&temp[3]) + " " + res);
        }catch (IOException e) {
            throw e;
        }
        return res;
    }

    public int enviar_Int32(int dato) throws IOException {
        int res = 0;
        try {
            outputSt.writeInt(dato);
            byte[] temp = ByteBuffer.allocate(4).putInt(dato).array();
            res += 0xff&temp[0];
            res += 0xff&temp[1];
            res += 0xff&temp[2];
            res += 0xff&temp[3];
//            Log.d("enviar_Int32", (0xff & temp[0]) + " " + (0xff & temp[1]) + " " + (0xff & temp[2]) + " " + (0xff & temp[3]) + " " + res);
        }catch (IOException e) {
            throw e;
        }
        return res;
    }

    public int enviar_Int64(long dato) throws IOException {
        int res = 0;
        try {
            outputSt.writeLong(dato);
            byte[] temp = ByteBuffer.allocate(8).putLong(dato).array();
            res += 0xff & temp[0];
            res += 0xff & temp[1];
            res += 0xff & temp[2];
            res += 0xff & temp[3];
            res += 0xff & temp[4];
            res += 0xff & temp[5];
            res += 0xff & temp[6];
            res += 0xff & temp[7];
        }catch (IOException e) {
            throw e;
        }
        return res;
    }

    public int enviar_Float(float dato) throws IOException {
        int res = 0;
        try {
            outputSt.writeFloat(dato);
            byte[] temp = ByteBuffer.allocate(4).putFloat(dato).array();
            res += 0xff&temp[0];
            res += 0xff&temp[1];
            res += 0xff&temp[2];
            res += 0xff&temp[3];
//            Log.d("enviar_Float", (0xff & temp[0]) + " " + (0xff & temp[1]) + " " + (0xff & temp[2]) + " " + (0xff & temp[3]) + " " + res);
        } catch (IOException e) {
            throw e;
        }
        return res;
    }

    public int enviar_Double(double dato) throws IOException {
        int res = 0;
        try {
            outputSt.writeDouble(dato);
            byte[] temp = ByteBuffer.allocate(8).putDouble(dato).array();
            res += 0xff&temp[0];
            res += 0xff&temp[1];
            res += 0xff&temp[2];
            res += 0xff&temp[3];
            res += 0xff&temp[4];
            res += 0xff&temp[5];
            res += 0xff&temp[6];
            res += 0xff&temp[7];
        } catch (IOException e) {
            throw e;
        }
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
