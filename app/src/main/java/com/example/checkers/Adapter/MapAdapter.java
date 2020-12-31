package com.example.checkers.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.checkers.R;

public class MapAdapter extends RecyclerView.Adapter<MapAdapter.MyAdapter> {

    private final int[] images;
    private final String[] mapId;
    protected ItemListener mListener;

    public MapAdapter(int[] images, String[] names, ItemListener mListener) {
        this.images = images;
        this.mapId = names;
        this.mListener = mListener;
    }

    @Override
    public int getItemCount() {
        return mapId.length;
    }

    @NonNull
    @Override
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_layout,null);
        return new MyAdapter(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter holder, int position) {
        holder.image.setImageResource(images[position]);
        holder.text.setText(mapId[position]);
    }

    public interface ItemListener {
        void onItemClick(String idField);
    }

    public class MyAdapter extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView image;
        TextView text;
        public MyAdapter(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            image = itemView.findViewById(R.id.imageView);
            text = itemView.findViewById(R.id.textView);
            text.setVisibility(View.VISIBLE);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(text.getText().toString());
            }
        }
    }
}
