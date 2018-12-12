package com.example.kad0143.mygarageproject.DataAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kad0143.mygarageproject.Entity.CarEntity;
import com.example.kad0143.mygarageproject.R;

import java.util.List;

public class DataAdapter extends ArrayAdapter<CarEntity> {
    private Context context;
    private int layoutResourceId;
    private List<CarEntity> data = null;

    public DataAdapter(Context context, int layoutResourceId, List<CarEntity> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        EntryHolder holder = null;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new EntryHolder();
            holder.brandText = (TextView) row.findViewById(R.id.brandText);
            holder.modelText = (TextView) row.findViewById(R.id.modelText);
            holder.yearText = (TextView) row.findViewById(R.id.yearText);
            holder.engineText = (TextView) row.findViewById(R.id.engineText);
            holder.carImage = (ImageView) row.findViewById(R.id.carImage);
            row.setTag(holder);
        } else {
            holder = (EntryHolder) row.getTag();
        }
        CarEntity entry = data.get(position);
        holder.brandText.setText(entry.brand);
        holder.modelText.setText(entry.model);
        holder.yearText.setText(entry.year);
        holder.engineText.setText(entry.engine);
        holder.carImage.setImageBitmap(entry.image);

        return row;
    }

    static class EntryHolder {
        TextView brandText;
        TextView modelText;
        TextView yearText;
        TextView engineText;
        ImageView carImage;
    }
}
