package com.easyandroidscreensharing;

import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

/**
 * Created by axel on 15.04.2015.
 */
public class WebSocketTransport {

    private final static String TAG = WebSocketTransport.class.getSimpleName();
    private URI _uri;
    private WebSocketClient _client;
    private boolean _reconnect = false;

    public WebSocketTransport( final String ip )
    {
        try {
            _uri = new URI("ws://" + ip + ":5001");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static WebSocketClient createClient( final URI uri )
    {
        Log.d( TAG, "create new websocket client: " + uri.toString()  );

        WebSocketClient client = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Log.i( TAG, "Opened");
            }

            @Override
            public void onMessage(String message) {
                Log.i( TAG, message);
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.i( TAG, reason);
            }

            @Override
            public void onError(Exception ex) {Log.i( TAG, ex.getMessage());
            }
        };

        return client;
    }

    public void connect()
    {
        try {
            _client = createClient(_uri);
            _client.connectBlocking();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sendBinary( byte[] data )
    {
        try {
       //     if( _client.getConnection().isOpen() ) {

               // if( data.length < 70856 ) {
                    _client.send(data);
                //}else{
                //    Log.i( TAG, "skip video chund: " + data.length );
                // }
            //     }else{
            //  connect();
            // }
        }catch( Exception e ){
            Log.e( TAG, "exception thrown sendBiary" );
        }
    }
}
