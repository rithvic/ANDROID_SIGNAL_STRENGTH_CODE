package com.quadrobay.qbayApps.networkproject;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

public class Customadapter  extends RecyclerView.Adapter<Customadapter.custom>{


    List<Sensor> jsondata;
    Context context;

    public Customadapter(Context context, List<Sensor> json) {

        this.jsondata=json;
        this.context=context;
    }

    @Override
    public Customadapter.custom onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.sensor_layout,parent,false);

        custom custom=new custom(view);
        return custom;

    }

    @Override
    public void onBindViewHolder(Customadapter.custom holder, int position) {

        if (jsondata != null || jsondata.size() !=0) {
            String sensorstr = jsondata.get(position).toString();
            sensorstr = sensorstr.replace(" ", "");
            try {
                JSONObject sonseinfo = new JSONObject(sensorstr);
                Iterator<?> keys = sonseinfo.keys();
                while (keys.hasNext()) {
                    String key = (String) keys.next();
                    String value = sonseinfo.getString(key);
                    if (key == null || "".equals(key) || value == null || "".equals(value)) {

                    } else {

                        if ("Sensorname".equals(key)) {
                            holder.text1.setText(value);
                        } else {
                            holder.text2.setText(key);
                            holder.text3.setText(value);

                        }

                    }
                    Log.e("", "");
                }

                Log.e("", "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



    }

    @Override
    public int getItemCount() {
        return jsondata.size();
    }
    class custom extends RecyclerView.ViewHolder{
        TextView text1;
        TextView text2,text3;
        public custom(View itemView) {
            super(itemView);
            text1 = (TextView) itemView.findViewById(R.id.tex11);
            text2 = (TextView) itemView.findViewById(R.id.tex22);
            text3 = (TextView) itemView.findViewById(R.id.tex33);
        }
    }
}