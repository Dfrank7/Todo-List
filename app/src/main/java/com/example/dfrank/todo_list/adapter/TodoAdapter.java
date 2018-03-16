package com.example.dfrank.todo_list.adapter;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dfrank.todo_list.R;
import com.example.dfrank.todo_list.data.TodoContract;
import com.example.dfrank.todo_list.fragment.Dialog;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dfrank on 3/15/18.
 */

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.viewHolder>{
    private Context context;
    private Cursor cursor;

    public TodoAdapter(Context context){
        this.context = context;
        //this.cursor = cursor;
    }
    @Override
    public TodoAdapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.row, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(TodoAdapter.viewHolder holder, int position) {

        int idIndex = cursor.getColumnIndex(TodoContract.TodoEntry._ID);
        int descriptionIndex = cursor.getColumnIndex(TodoContract.TodoEntry.task);
        int priorityIndex = cursor.getColumnIndex(TodoContract.TodoEntry.priority);

        cursor.moveToPosition(position);

        int id = cursor.getInt(idIndex);
        String description = cursor.getString(descriptionIndex);
        int priority = cursor.getInt(priorityIndex);

        holder.task.setText(description);
        holder.itemView.setTag(id);

        String priorityString = "" + priority; // converts int to String
        holder.priorityView.setText(priorityString);

        GradientDrawable priorityCircle = (GradientDrawable) holder.priorityView.getBackground();
        // Get the appropriate background color based on the priority
        int priorityColor = getPriorityColor(priority);
        priorityCircle.setColor(priorityColor);




    }

    @Override
    public int getItemCount() {
        if (cursor == null) {
            return 0;
        }
        return cursor.getCount();
    }

    public Cursor swapCursor(Cursor mcursor){
        if (cursor==mcursor){
            return null;
        }
        Cursor temp = cursor;
        this.cursor = mcursor;

        if (mcursor!=null){
            notifyDataSetChanged();
        }
        return temp;
    }

    private int getPriorityColor(int priority) {
        int priorityColor = 0;

        switch(priority) {
            case 1: priorityColor = ContextCompat.getColor(context, R.color.materialRed);
                break;
            case 2: priorityColor = ContextCompat.getColor(context, R.color.materialOrange);
                break;
            case 3: priorityColor = ContextCompat.getColor(context, R.color.materialYellow);
                break;
            default: break;
        }
        return priorityColor;
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.taskDescription)  TextView task;
        @BindView(R.id.priorityTextView) TextView priorityView;
        public viewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);


            view.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if (position!=RecyclerView.NO_POSITION){
//                        Intent intent = new Intent(context, Dialog.class);
//                        view.getContext().startActivity(intent);
                    }
                }
            });
        }

    }
}
