package wearables.jasonsalas.com.wearablemessagingdemo;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CommonWaterVolumesActivity extends Activity implements WearableListView.ClickListener {

//    private static final String TAG = CommonWaterVolumesActivity.class.getSimpleName();
    private static final String TAG = "WaterLogg";
    private WearableListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_water_volumes);

        listView = (WearableListView) findViewById(R.id.volumeListView);
        listView.setAdapter(new WaterVolumeListAdapter(CommonWaterVolumesActivity.this));
        listView.setClickListener(CommonWaterVolumesActivity.this);
    }

    private static ArrayList<String> listItems;
    static {
        listItems = new ArrayList<String>();
        listItems.add("8");
        listItems.add("12");
        listItems.add("16");
        listItems.add("20");
        listItems.add("34");
        listItems.add("40");
        listItems.add("51");
    }

    @Override
    public void onClick(WearableListView.ViewHolder holder) {
        String selection = holder.itemView.getTag().toString();
        Intent resultWaterVolume = new Intent();
        resultWaterVolume.putExtra("selectedWaterVolume", selection);
        setResult(RESULT_OK, resultWaterVolume);
        finish();
    }

    @Override
    public void onTopEmptyRegionClick() { }

    private class WaterVolumeListAdapter extends WearableListView.Adapter {
        private final LayoutInflater inflater;

        private WaterVolumeListAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
            TextView textView = (TextView) holder.itemView.findViewById(R.id.water_volume);
            textView.setText(listItems.get(position));
            holder.itemView.setTag(textView.getText());
        }

        @Override
        public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new WearableListView.ViewHolder(inflater.inflate(R.layout.water_volumes_layout, null));
        }

        @Override
        public int getItemCount() {
            return listItems.size();
        }
    }
}
