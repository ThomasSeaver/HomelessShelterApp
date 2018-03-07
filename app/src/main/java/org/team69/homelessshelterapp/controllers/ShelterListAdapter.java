package org.team69.homelessshelterapp.controllers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.team69.homelessshelterapp.R;
import org.team69.homelessshelterapp.model.Shelter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by TomStuff on 3/6/18.
 */

public class ShelterListAdapter extends RecyclerView.Adapter<ShelterListAdapter.ViewHolder> {
    private static ArrayList<Shelter> shelterList;
    private HashMap<String, String> theMap;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private final Context context;
        private int position;


        public ViewHolder(View view, final ShelterListAdapter adapter) {
            super(view);
            context = itemView.getContext();
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    final Intent intent;
                    intent =  new Intent(context, DetailActivity.class);
                    intent.putExtra("shelterList", shelterList);
                    intent.putExtra("shelterNum", position);
                    intent.putExtra("map", adapter.theMap);
                    context.startActivity(intent);
                }
            });
            textView = view.findViewById(R.id.text_row);
        }

        public void bindData(Shelter shelter, int position) {
            textView.setText(shelter.getName());
            this.position = position;
        }

    }

    public ShelterListAdapter(HashMap<String, Shelter> shelterList, HashMap<String, String> theMap) {
        this.theMap = theMap;
        this.shelterList = new ArrayList<>(shelterList.values());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_text_row, viewGroup, false);

        return new ViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.bindData(shelterList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return shelterList.size();
    }
}