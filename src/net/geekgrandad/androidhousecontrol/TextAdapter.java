package net.geekgrandad.androidhousecontrol;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class TextAdapter extends BaseAdapter {

    private Context context;
    private DisplayMetrics dm;
    private String[] texts = {
    		"Master\nBedroom", "Second\nBedroom", "Landing\n", "Bathroom\n", "Living\nRoom", 
    		"Kitchen\n", "Hall\n", "Dining\nRoom", "Utility\nRoom"};
    private float[] temps;

    public TextAdapter(Context context, DisplayMetrics dm, float[] temps) {
        this.context = context;
        this.dm = dm;
        this.temps = temps;
    }

    public int getCount() {
        return 9;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv;
        if (convertView == null) {
            tv = new TextView(context);
            tv.setLayoutParams(new GridView.LayoutParams(dm.widthPixels / 3, (dm.heightPixels - 220)/3));
            tv.setBackgroundColor(Color.YELLOW);
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
        }
        else {
            tv = (TextView) convertView;
        }

        tv.setTextSize(18);
        tv.setText(texts[position] + "\n\n" +temps[position]);
        return tv;
    }
}
