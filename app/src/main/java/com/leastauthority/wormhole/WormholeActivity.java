package com.leastauthority.wormhole;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import wormhole.Client;

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

        Client client = new Client();
        WormholeActivity.init();
    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        EditText messageText = (EditText) findViewById(R.id.message);
        EditText codeText = (EditText) findViewById(R.id.code);

        String message = messageText.getText().toString();

        String s = "ws://relay.magic-wormhole.io:4000/v1";
        String appId = "lothar.com/wormhole/text-or-file-xfer";

        long w = WormholeActivity.connect(appId, s);
        String code = WormholeActivity.getcode(w);

        // TODO: This sets the code, but is never shown on the screen until
        // we exit this function. However, we need to show it on the screen
        // in order for the rest of the function to finish. :-(
        // So, currently this is not functional. If we println the code, it
        // shows up on the logcat which can be used on the other side to
        // receive the message.
        codeText.setText(code);

        if (message.isEmpty()) {
            Context context = getApplicationContext();
            CharSequence text = "Please enter the message";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
        } else {
            //WormholeActivity.send(w, code, message);
            new MsgSendTask().execute(Long.toString(w), code, message);
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

    private class MsgSendTask extends AsyncTask<String, Integer, Void> {

        //@Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //@Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        //@Override
        // protected void onPostExecute(String result) {
        //     super.onPostExecute(result);
        // }

        //@Override
        protected Void doInBackground(String... params) {
            long w = Long.parseLong(params[0]);
            String code = params[1];
            String msg = params[2];

            WormholeActivity.send(w, code, msg);
            // TODO: catch any exceptions and return an appropriate
            // value to the caller, so that a message can be displayed
            // to the user.
            return null;
        }
    }
}
