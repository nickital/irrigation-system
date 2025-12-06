package com.nbb.aaa.flower;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class BlueToothSocket {
    static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private String address;
    BluetoothSocket socket;
    ConnectionThread connectionThread;
    static public final int handlerState = 0;
    Handler bluetoothIn = null;
    static BlueToothSocket singleBTSocket = null;

    private BlueToothSocket(String address) {
        this.address = address;
    }

    static public BlueToothSocket getInstance(String address) {
        if (singleBTSocket == null)
            singleBTSocket = new BlueToothSocket(address);

        return singleBTSocket;
    }

    /**
     * @return 1 - OK, 0 - non-enabled, -1 - doesn't exist
     */
    public int init() {
        int state = 0;
        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null)
            state = -1;
        else if (btAdapter.isEnabled())
            state = 1;
        Log.d("nick", "btSocket init state = " + state);
        return state;
    }

    public boolean activate() {
        if (socket != null) {
            Log.d("nick", "btSocket: Trying to restart active socket. Ignore.");
            return false;
        }

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!btAdapter.isEnabled()) {
            Log.d("nick", "Cannot activate non-active adapter. Ignore.");
            return false;
        }

        boolean state = false;
        Log.d("nick", "BTSocket: activate: before getRemoteDevice: address = '" + address + "'" );
        BluetoothDevice device = btAdapter.getRemoteDevice(address);
        Log.d("nick", "BTSocket: activate: after getRemoteDevice: address = '" + address + "'" );

        if (device != null) {
            try {
                socket = device.createRfcommSocketToServiceRecord(uuid);
                socket.connect();
                state = true;
            } catch (IOException e) {
                try {
                    e.printStackTrace();
                    if (socket != null)
                        socket.close();
                    socket = null;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                return false;
            }
            connectionThread = new ConnectionThread(socket);
            connectionThread.start();
        }

        Log.d("nick", "btSocket activate state = " + state);
        return state;
    }
    public void setHandler(Handler handler){
        this.bluetoothIn = handler;
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("nick", "btSocket close ");
    }

    public int write(String input) {//sends massage to arduino
        char endOfLine = '~';
        int retBytes = -1;
        Log.d("nick", "BlueToothSocket, Sending " + input);
        if (input != null && !input.isEmpty() && connectionThread != null)
            retBytes = connectionThread.write(input + endOfLine);

        return retBytes;
    }

    private class ConnectionThread extends Thread {
        InputStream inp;
        OutputStream oup;

        public ConnectionThread(BluetoothSocket socket) {
            try {
                inp = socket.getInputStream(); // read from arduino
                oup = socket.getOutputStream(); // write to arduino
            } catch (IOException e) {
            }
        }

        @Override
        public void run() {
            super.run();
            byte[] buffer = new byte[256];
            int bytes = 0;
            boolean bCanRead = true;

            while (bCanRead) {
                try {

                    bytes = inp.read(buffer);
                    String readMessage = new String(buffer, 0, bytes);
                    if(bluetoothIn != null)
                        bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                    else
                        Log.d("nick","BlueToothSocket, bluetoothIn = null");
                } catch (IOException e) {
                    e.printStackTrace();
                    bCanRead = false;
                }

            }
        }

        public int write(String input) {
            int retBytes = -1;
            Log.d("nick", "BlueToothSocket, write input = " + input);
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                oup.write(msgBuffer);
                retBytes = msgBuffer.length;
            } catch (IOException e) {
                //if you cannot write, close the application
            }

            return retBytes;
        }
    }
}

