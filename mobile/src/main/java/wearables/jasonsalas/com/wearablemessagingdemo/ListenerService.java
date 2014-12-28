package wearables.jasonsalas.com.wearablemessagingdemo;

import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import com.temboo.Library.Fitbit.Foods.LogWater;
import com.temboo.core.TembooException;
import com.temboo.core.TembooSession;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ListenerService extends WearableListenerService {

    private static final String TAG = ListenerService.class.getSimpleName();
    private static final String MESSAGE_PATH = "/updatefitbit";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        if(messageEvent.getPath().equals(MESSAGE_PATH)) {
            updateFitbit(new String(messageEvent.getData()));
        }
    }

    private void updateFitbit(final String volume) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Instantiate the Choreo
					/*
						TODO: generate these values for your own app at https://www.temboo.com/account/applications/
					*/
                    TembooSession session = new TembooSession(<YOUR_APP_NAME>, <YOUR_APP_NAME>, <YOUR_APP_KEY>);
                    Log.i(TAG, "Temboo session created");

                    // Get an InputSet object for the choreo
                    final LogWater logWaterChoreo = new LogWater(session);
                    final LogWater.LogWaterInputSet logWaterInputs = logWaterChoreo.newInputSet();

                    // setup the value presets
					/* 
						TODO: generate the following string values yourself from https://www.temboo.com/library/Library/Fitbit/
					*/
                    final String ACCESS_TOKEN = "";
                    final String ACCESS_TOKEN_SECRET = "";
                    final String CONSUMER_KEY = "";
                    final String CONSUMER_SECRET = "";
                    final String UNIT = "fl oz";
                    final String USER_ID = "";

                    String currentDate = (new SimpleDateFormat("yyyy-MM-dd")).format(Calendar.getInstance().getTime());
                    Log.i(TAG, String.format("Current date: %s", currentDate));

                    // Set input values
                    logWaterInputs.set_AccessToken(ACCESS_TOKEN);
                    logWaterInputs.set_AccessTokenSecret(ACCESS_TOKEN_SECRET);
                    logWaterInputs.set_Amount(volume);
                    logWaterInputs.set_ConsumerKey(CONSUMER_KEY);
                    logWaterInputs.set_ConsumerSecret(CONSUMER_SECRET);
                    logWaterInputs.set_Date(currentDate);
                    logWaterInputs.set_Unit(UNIT);
                    logWaterInputs.set_UserID(USER_ID);

                    Log.i(TAG, "Attempting to connect to Temboo...");
                    LogWater.LogWaterResultSet logWaterResults = logWaterChoreo.execute(logWaterInputs);
                    Log.i(TAG, logWaterResults.get_Response());
                } catch (TembooException e) {
                    Log.i(TAG, "Error within runnable: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();
    }
}