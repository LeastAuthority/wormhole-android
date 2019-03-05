package com.leastauthority.wormhole;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WormholeActivity extends AppCompatActivity {

    static {
        System.loadLibrary("magic_wormhole_io_blocking");
    }

    private static native String receive(String server, String appid, String code);
    private static native void send(long ptr, String code, String msg);
    private static native void init();
    private static native long connect(String appid, String server);
    private static native String getcode(long ptr);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wormhole);

        WormholeActivity.init();
    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        EditText messageText = (EditText) findViewById(R.id.message);
        EditText codeText = (EditText) findViewById(R.id.code);

        String message = messageText.getText().toString();

        String s = "ws://relay.magic-wormhole.io:4000/v1";
        String appId = "lothar.com/wormhole/text-or-file-xfer";

        if (message.isEmpty()) {
            Context context = getApplicationContext();
            CharSequence text = "Please enter the message";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        } else {
            long w = WormholeActivity.connect(appId, s);
            String code = WormholeActivity.getcode(w);

            codeText.setText(code);
            WormholeActivity.send(w, code, message);
        }

        //System.out.println("received: " + Wormhole.send(s, appId, "foobar"));
    }

    // called when the user taps the Receive button
    public void receiveMessage(View view) {

        EditText messageText = (EditText) findViewById(R.id.message);
        EditText codeText = (EditText) findViewById(R.id.code);

        String code = codeText.getText().toString();

        String s = "ws://relay.magic-wormhole.io:4000/v1";
        String appId = "lothar.com/wormhole/text-or-file-xfer";

        if (code.isEmpty()) {
            Context context = getApplicationContext();
            CharSequence text = "Please enter the code";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        } else {
            String rxText = WormholeActivity.receive(s, appId, code);
            messageText.setText(rxText);
        }
    }
}
