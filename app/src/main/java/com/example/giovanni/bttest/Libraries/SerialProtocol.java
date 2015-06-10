package com.example.giovanni.bttest.Libraries;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by userk on 10/06/15.
 */
public class SerialProtocol {

    private static Context ctx;
    private static Activity activity;
    private static BluetoothAdapter mBluetoothAdapter;

    // Protocol
    static int version = 6;
    static int headerLength = 8;
    static int cmdLength = 14;
    static int footerLength = 1;
    static int crc = 8;
    static int finalTag = 10;
    private static final String TAG_NAME = "name";
    private static final String TAG_ID = "id";
    static final byte delimiter = 10;
    static public byte[] message;
    static public byte[] command;
    static public byte[] header;
    static public byte[] footer;
    static int readBufferPosition;
    static int counter;



    public SerialProtocol (Context context, Activity acti)
    {
        super();
        ctx = context;
        this.activity = acti;

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null)
        {
            Toast.makeText(ctx, "The device does not support Bluetooth. Attaccati al ca'..."
                    , Toast.LENGTH_SHORT).show();
        }
    }

    // Create Header of the message
    public byte[] createHeader(int src, int dest, int numCmd)
    {
        header = new byte[headerLength];
        header[0]=(byte)(src & 0xFF);
        header[1]=(byte)(dest & 0xFF);
        header[2]=(byte)(version & 0xFF);
        header[3]=(byte)(numCmd & 0xFF);
        header[4]=(byte)(headerLength & 0xFF);
        header[5]=(byte)(cmdLength & 0xFF);
        //Compute Total length of the message
        int totLen = headerLength + cmdLength*numCmd + footerLength;
        header[6]=(byte)(totLen & 0xFF);
        header[7]=(byte)(crc & 0xFF);

        return header;
    }

    public byte[] createFooter()
    {
        footer = new byte[footerLength];
        footer[0]=(byte)(finalTag & 0xFF);

        return footer;
    }

    public byte[] createCommand(int type, float value1, float value2, float value3, float value4)
    {
        command = new byte[cmdLength];
        command[0]=(byte)(type & 0xFF);
        byte[]  value1Byte = float2Bytes(value1);
        command[1]= value1Byte[0];
        command[2]= value1Byte[1];
        command[3]= value1Byte[2];
        command[4]= value1Byte[3];
        byte[]  value2Byte = float2Bytes(value2);
        command[5]= value2Byte[0];
        command[6]= value2Byte[1];
        command[7]= value2Byte[2];
        command[8]= value2Byte[3];
        byte[]  value3Byte = float2Bytes(value3);
        command[9]= value3Byte[0];
        command[10]= value3Byte[1];
        command[11]= value3Byte[2];
        command[12]= value3Byte[3];
        byte[]  value4Byte = float2Bytes(value4);
        command[13]= value4Byte[0];
        command[14]= value4Byte[1];
        command[15]= value4Byte[2];
        command[16]= value4Byte[3];

        return command;
    }

    public byte[] assembleMess(byte[] header, byte[] cmd, byte[] footer)
    {
        message = new byte[footerLength];

        return footer;
    }

    public static byte [] float2Bytes(float value)
    {
        return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putFloat(value).array();
    }
}
