package wearables.jasonsalas.com.wearablemessagingdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class WearableMainActivity extends Activity {

    private static final String TAG = "WaterLogg";
    private static final long CONNECTION_TIMEOUT = 30;
    private static final int SPEECH_REQUEST_CODE = 8675309;
    private static final int LIST_REQUEST_CODE = 007;
    private static final int CONFIRMATION_CODE = 5150;
    private static final String MESSAGE_PATH = "/updatefitbit";

    private GoogleApiClient client;
    private String nodeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wearable_main);

        client = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();

        getConnectedNodeId();


        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                setupWidgets();
            }
        });
    }


    private void setupWidgets() {
        findViewById(R.id.btn_voice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent speechRecognizer = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speechRecognizer.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                startActivityForResult(speechRecognizer, SPEECH_REQUEST_CODE);
            }
        });

        findViewById(R.id.btn_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent listViewIntent = new Intent(WearableMainActivity.this, CommonWaterVolumesActivity.class);
                startActivityForResult(listViewIntent, LIST_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, String.format("REQUEST CODE: %s", requestCode));
        Log.i(TAG, String.format("RESULT CODE: %s", resultCode));
        switch(requestCode) {
            case SPEECH_REQUEST_CODE:
                if(resultCode == RESULT_OK) {
                    ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String spokenText = (results.size() > 0) ? results.get(0) : "No speech data available";
                    Log.i(TAG, String.format("SPOKEN TEXT: %s", spokenText));

                    // send the data to the handheld to post to Fitbit via Temboo
                    byte[] message = spokenText.getBytes();
                    sendWaterVolume(message);
                    displayConfirmation();
                }
                break;
            case LIST_REQUEST_CODE:
                if(resultCode == RESULT_OK) {
                    String listValue = data.getStringExtra("selectedWaterVolume");
                    byte[] message = listValue.getBytes();
                    sendWaterVolume(message);
                    displayConfirmation();
                }
                break;
            case CONFIRMATION_CODE:
                Log.i(TAG, "closing activity...");
                // finish();
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /*
    * run the confirmation animation sequence
    * */
    private void displayConfirmation() {
        Intent intent = new Intent(this, ConfirmationActivity.class);
        intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE, ConfirmationActivity.SUCCESS_ANIMATION);
        intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, getString(R.string.confirmation_message));
        startActivityForResult(intent, CONFIRMATION_CODE);
    }

    /*
    * returns the first Node ID found of any connected devices
    * */
    private void getConnectedNodeId() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                client.blockingConnect(CONNECTION_TIMEOUT, TimeUnit.SECONDS);

                NodeApi.GetConnectedNodesResult result = Wearable.NodeApi.getConnectedNodes(client).await();
                List<Node> nodes = result.getNodes();
                if(nodes.size() > 0) {
                    nodeId = nodes.get(0).getId();
                }

                client.disconnect();
            }
        }).start();
    }

    /*
    * sends the speech data to the connected device
    * */
    private void sendWaterVolume(final byte[] volume) {
       if(nodeId != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    client.blockingConnect(CONNECTION_TIMEOUT, TimeUnit.SECONDS);
                    Wearable.MessageApi.sendMessage(client, nodeId, MESSAGE_PATH, volume);
                    client.disconnect();
                }
            }).start();
        }
    }
}
