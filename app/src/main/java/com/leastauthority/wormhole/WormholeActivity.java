package com.leastauthority.wormhole;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class WormholeActivity extends AppCompatActivity {

    static {
        System.loadLibrary("magic_wormhole_io_blocking");
    }

    private static native String receive(String server, String appid, String code);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wormhole);
    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        // Do something in response to button
        //Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText messageText = (EditText) findViewById(R.id.message);
        String message = messageText.getText().toString();

        //String s = "ws://127.0.0.1:4000/v1";
        //String appId = "lothar.com/wormhole/text-or-file-xfer";

        //System.out.println("received: " + Wormhole.send(s, appId, "foobar"));
    }

    // called when the user taps the Receive button
    public void receiveMessage(View view) {

        EditText messageText = (EditText) findViewById(R.id.message);
        EditText codeText = (EditText) findViewById(R.id.code);

        String code = codeText.getText().toString();

        //Wormhole w = new Wormhole();
        String s = "ws://relay.magic-wormhole.io:4000/v1";
        String appId = "lothar.com/wormhole/text-or-file-xfer";

        String rxText = WormholeActivity.receive(s, appId, code);

        messageText.setText(rxText);
    }
}
