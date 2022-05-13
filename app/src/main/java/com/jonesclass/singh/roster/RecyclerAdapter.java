package com.jonesclass.singh.roster;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private List<Member> memberList;
    private DatabaseHelper databaseHelper;
    private Context context;

    public RecyclerAdapter(Context context, List<Member> memberList, DatabaseHelper databaseHelper) {
        this.memberList = memberList;
        this.context = context;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public RecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_members, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String name = memberList.get(position).getFirstName() + " " + memberList.get(position).getLastName();
        holder.nameTextView.setText(name);
        holder.activeCheckBox.setChecked(memberList.get(position).isActive());
    }

    @Override
    public int getItemCount() {
        return memberList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private CheckBox activeCheckBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textView_name);
            activeCheckBox = itemView.findViewById(R.id.checkBox_active);
        }
    }
}
