package com.example.nickkouthong.starsnstuff;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private int debug;
    public static final String TAG ="RecyclerAdapter";
    public ArrayList<String> neoNames = new ArrayList<>();
    private ArrayList<String> neoSizes = new ArrayList<>();
    private ArrayList<String> dangers = new ArrayList<>();
    private ImageView image;

    private Context mContext;

    //called
    public RecyclerViewAdapter(Context context,ArrayList<String> names, ArrayList<String> sizes, ArrayList<String> mDangers) {
        this.neoNames = clone(names);
        this.neoSizes = clone(sizes);
        this.dangers = clone(mDangers);
        this.mContext = context;
        this.debug = this.neoNames.size();

        Log.d(TAG, "sucessful adapter initialization");
    }
    private ArrayList<String> clone(ArrayList<String> temp) {
        ArrayList<String> updated = new ArrayList<>();
        for(String s: temp) {
            updated.add(s);
        }
        return updated;

    }

    //not called
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG, "On create viewholder called");
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        Log.d(TAG, "sucessful viewholder returned");
        return holder;
    }

    //not called
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Log.d(TAG, "onBindViewHolder: called");
        //add photos here
        //Glide.with(mContext).asBitmap().load(mImages.get(i)).into(viewHolder.image);
        Log.d(TAG,"bind viw size = "+neoNames.size());
        viewHolder.mName.setText(neoNames.get(i));
        viewHolder.mSize.setText(neoSizes.get(i));
        viewHolder.hazard.setText(dangers.get(i));
        if(dangers.get(i) == "Is potentially dangerous") {
            viewHolder.view.setImageResource(R.drawable.caution);
        }else {
            viewHolder.view.setImageResource(R.drawable.asteroid);
        }



    }


    @Override
    public int getItemCount() {
        Log.d(TAG, "get item count called");
        return neoNames.size();
        //return 12;
        //return debug;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mSize;
        TextView mName;
        TextView hazard;
        ImageView view;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView){
            super(itemView);
            mSize = (TextView)itemView.findViewById(R.id.missDistance);
            mName = (TextView)itemView.findViewById(R.id.neoname);
            hazard = (TextView)itemView.findViewById(R.id.danger);
            view = (ImageView)itemView.findViewById(R.id.image);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

}
