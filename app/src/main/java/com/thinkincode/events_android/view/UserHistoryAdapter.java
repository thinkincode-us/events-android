package com.thinkincode.events_android.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thinkincode.events_android.R;
import com.thinkincode.events_android.model.Event;

import java.util.List;

public class UserHistoryAdapter extends RecyclerView.Adapter <UserHistoryAdapter.ViewHolderUser>{
    private List<Event> listEvents;
    private LayoutInflater layoutInflater;

    UserHistoryAdapter(Context ctx, List<Event> listEvents) {
        this.listEvents = listEvents;
        this.layoutInflater= LayoutInflater.from(ctx);

    }

    @NonNull
    @Override
    public ViewHolderUser onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = layoutInflater.inflate(R.layout.content_entity,viewGroup,false);
        return new ViewHolderUser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderUser viewHolderUser, int position) {
        viewHolderUser.name.setText(listEvents.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return listEvents.size();
    }

    public class ViewHolderUser extends RecyclerView.ViewHolder implements View
            .OnClickListener{

        private TextView name;

        public ViewHolderUser(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView_name);

            name.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public interface  ItemClickLister {
        void onItemClick(View view,int position);
    }

}
