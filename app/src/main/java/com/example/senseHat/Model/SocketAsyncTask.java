package com.example.senseHat.Model;

import android.os.AsyncTask;

import com.example.senseHat.View.HomePageActivity;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketAsyncTask extends AsyncTask<Void, Void, Void>
{
    Socket socket;
    public static String CMD = "0";

    @Override
    protected Void doInBackground (Void... params){
        try {
            // take ip and socket number to connection
            InetAddress inetAddress = InetAddress.getByName(HomePageActivity.ipAddress);
            socket = new Socket(inetAddress, HomePageActivity.socketAddress);

            // create a data output stream from the output stream so we can send data through it
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF(CMD);
            dataOutputStream.flush(); // send the message
            dataOutputStream.close(); // close the output stream when we're done
            socket.close();
        }
        catch (UnknownHostException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
