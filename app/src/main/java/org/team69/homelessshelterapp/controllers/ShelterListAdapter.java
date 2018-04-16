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
import java.util.List;
import java.util.Map;

/**
 * View adapter for detail list within shelterlist, allowing users to click directly on a shelter
 * and be taken to a specific detail view
 *
 * Created by TomStuff on 3/6/18.
 */

public class ShelterListAdapter extends RecyclerView.Adapter<ShelterListAdapter.ViewHolder> {
    private static List<Shelter> shelterList;
    private final String userID;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final Context context;
        private int position;


        /**
         * Creates view holder of individual text rows
         *
         * @param view    view that I want to manipulate, specifically this super view holder
         * @param adapter adapter that I'm using, this newly created class itself
         */
        public ViewHolder(View view, final ShelterListAdapter adapter) {
            super(view);
            context = itemView.getContext();
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Intent intent;
                    intent =  new Intent(context, DetailActivity.class);
                    intent.putExtra("shelterNum", position);
                    intent.putExtra("userID", adapter.userID);
                    context.startActivity(intent);
                }
            });
            textView = view.findViewById(R.id.text_row);
        }

        /**
         * Binds data to the individual text views, just adding the name of the shelter to the rows
         *
         * @param shelter  shelter that this row works for, just using it for name
         * @param position position within list, setting position within view holder instance
         */
        public void bindData(Shelter shelter, int position) {
            textView.setText(shelter.getName());
            this.position = position;
        }

    }

    /**
     * Creates shelterlist adapter for this nested class view holder. Carries through persistent
     * data, like userID and shelterlist
     *
     * @param shelterList List of shelters
     * @param userID      userID of currently logged in user
     */
    public ShelterListAdapter(Map<String, Shelter> shelterList, String userID) {
        this.userID = userID;
        ShelterListAdapter.shelterList = new ArrayList<>(shelterList.values());
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