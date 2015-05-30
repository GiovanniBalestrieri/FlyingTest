package com.example.giovanni.bttest.Libraries;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.giovanni.bttest.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by userk on 06/04/15.
 */
public class BluAdapter extends BaseAdapter
{
    private static final String TAG_NAME = "name";
    private static final String TAG_ID = "id";
    private LayoutInflater inflater = null;
    private ArrayList<HashMap<String, String>> devicesList;
    private Context context;

    public BluAdapter(Context ctx, ArrayList<HashMap<String, String>> all)
    {
        super();
        context = ctx;
        devicesList = all;
        //inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater = LayoutInflater.from(ctx);
    }

    public int getCount()
    {
        return devicesList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return position;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    static class ViewHolder
    {
        public TextView nameDev;
        public TextView idDev;
        LinearLayout gtdTag;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        View vi = convertView;
        if (convertView == null)
        {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            vi = inflater.inflate(R.layout.blu_list_item, null);
            ViewHolder viewholder = new ViewHolder();
            viewholder.nameDev = (TextView) vi.findViewById(R.id.nameBluDev);
            viewholder.idDev = (TextView) vi.findViewById(R.id.idBluDev);
            vi.setTag(viewholder);
        }

        ViewHolder holder = (ViewHolder) vi.getTag();
        holder.nameDev.setText(devicesList.get(position).get(TAG_NAME));
        holder.idDev.setMaxLines(1);
        holder.idDev.setText(devicesList.get(position).get(TAG_ID));
        Log.e("BluAdapter report:", "Item " + Integer.toString(position) + " of " + Integer.toString(devicesList.size()));

        return vi;
    }


}
