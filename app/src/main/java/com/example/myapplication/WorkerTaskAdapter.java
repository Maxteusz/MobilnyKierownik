package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Locale;

import static android.Manifest.permission.CALL_PHONE;

public class WorkerTaskAdapter extends RecyclerView.Adapter<WorkerTaskAdapter.ViewHolder> {

    ArrayList<Task> TasksArray;
    ArrayList<Task> filterTaskArray;
    Context context;

    public WorkerTaskAdapter(Context context, ArrayList<Task> TasksArray) {

        this.context = context;
        this.TasksArray = TasksArray;
        this.filterTaskArray = TasksArray;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.task_card, parent, false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task currentTask = filterTaskArray.get(position);
        holder.nameText.setText(currentTask.getWorker());
        holder.resourceText.setText(currentTask.getResource());
        holder.numberText.setText(currentTask.getNumber());
        holder.startDateText.setText((currentTask.getStartTime().toString()));
        holder.taskCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DocumentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("Numer Zlecenia", holder.numberText.getText().toString());
                intent.putExtra("WorkerID", currentTask.getWorkerId());
                context.startActivity(intent);
            }
        });
        holder.personCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String numberPhone = "";
                if (TasksArray.size() > 0) {
                    numberPhone = getPhoneNumber(currentTask.getWorkerId());
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + numberPhone));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return filterTaskArray.size();
    }

    public void filterByNumber(String text) {

        text = text.toLowerCase(Locale.getDefault());
        if (text.length() == 0) {
            filterTaskArray = TasksArray;
        } else {
            ArrayList<Task> filteredList = new ArrayList<>();
            for (Task task : TasksArray) {
                if (task.getNumber().toLowerCase(Locale.getDefault()).contains(text))
                    filteredList.add(task);
            }
            filterTaskArray = filteredList;
        }
        notifyDataSetChanged();
    }

    public void filterByResource(String text) {

        text = text.toLowerCase(Locale.getDefault());
        if (text.length() == 0) {
            filterTaskArray = TasksArray;
        } else {
            ArrayList<Task> filteredList = new ArrayList<>();
            for (Task task : TasksArray) {
                if (task.getResource().toLowerCase(Locale.getDefault()).contains(text))
                    filteredList.add(task);
            }
            filterTaskArray = filteredList;
        }
        notifyDataSetChanged();
    }

    public void filterByWorkerName(String text) {

        text = text.toLowerCase(Locale.getDefault());
        if (text.length() == 0) {
            filterTaskArray = TasksArray;
        } else {
            ArrayList<Task> filteredList = new ArrayList<>();
            for (Task task : TasksArray) {
                if (task.getWorker().toLowerCase(Locale.getDefault()).contains(text))
                    filteredList.add(task);
            }
            filterTaskArray = filteredList;
        }
        notifyDataSetChanged();

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, resourceText, numberText, startDateText;
        CardView taskCard, personCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.name_text);
            resourceText = itemView.findViewById(R.id.resource_text);
            numberText = itemView.findViewById(R.id.number_text);
            startDateText = itemView.findViewById(R.id.startDate_text);
            taskCard = itemView.findViewById(R.id.TaskWorkerCard);
            personCard = itemView.findViewById(R.id.materialCardView4);

        }
    }

    public String getPhoneNumber(int idWorker) {
        SqlConnection connection = new SqlConnection();
        Connection con = connection.CONN();
        String phoneNumber = "";
        if (con != null) {
            Statement statement = null;
            try {
                statement = con.createStatement();
                ResultSet rs = null;
                rs = statement.executeQuery("select PRE_HDKTelefon1 from cdn.Pracetaty where Pre_PraId = " + idWorker);
                if (rs.next()) {
                    phoneNumber = rs.getString(1);
                }
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return phoneNumber;
    }
}
