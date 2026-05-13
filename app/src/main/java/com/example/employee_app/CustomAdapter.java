package com.example.employee_app;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Random;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    //Custome Adapter: Implementation for RecyclerView
    ArrayList<Staff> staffArrayList;
    Context context;

    public CustomAdapter(ArrayList<Staff> staffArrayList, Context context) {
        this.staffArrayList = staffArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.entity_staff, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Staff staff = staffArrayList.get(position);

        // Gi-update: getName -> getFname + getLname | getPosition -> getDivision
        String fullName = staff.getFname() + " " + staff.getLname();
        holder.tv_name.setText(fullName);
        holder.tv_position.setText(staff.getDivision() + "");
        holder.tv_title.setText(staff.getFname().toUpperCase().charAt(0) + "");

        Random random = new Random();
        int red = random.nextInt(133) + 134;
        int green = random.nextInt(133) + 134;
        int blue = random.nextInt(133) + 134;
        holder.tv_title.setBackgroundColor(Color.rgb(red, green, blue));

        holder.img_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_dialog(position);
            }
        });

        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_dialog(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return staffArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title, tv_name, tv_position;
        ImageView img_update, img_delete;

        public MyViewHolder(View itemview) {
            super(itemview);
            this.tv_title = (TextView) itemview.findViewById(R.id.tv_title);
            this.tv_position = (TextView) itemview.findViewById(R.id.tv_position);
            this.tv_name = (TextView) itemview.findViewById(R.id.tv_name);
            this.img_update = (ImageView) itemview.findViewById(R.id.img_update);
            this.img_delete = (ImageView) itemview.findViewById(R.id.img_delete);
        }
    }

    public void update_dialog(int position_of_update) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Update");

        LayoutInflater li = LayoutInflater.from(context);
        View view_update = li.inflate(R.layout.update_staff, null);
        builder.setView(view_update);

        EditText edt_fname = (EditText) view_update.findViewById(R.id.edt_name); // Assuming same ID in XML
        EditText edt_position = (EditText) view_update.findViewById(R.id.edt_position);

        // Gi-update: getName -> getFname | getPosition -> getDivision
        edt_fname.setText(staffArrayList.get(position_of_update).getFname() + "");
        edt_position.setText(staffArrayList.get(position_of_update).getDivision() + "");

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (edt_fname.getText().toString().trim().isEmpty() || edt_position.getText().toString().trim().isEmpty()) {
                    Toast.makeText(context, "Enter Value", Toast.LENGTH_SHORT).show();
                } else {
                    StaffRepository staffRepository = new StaffRepository(context);
                    String updated_fname = edt_fname.getText().toString().trim();
                    String updated_position = edt_position.getText().toString().trim();

                    // Gi-update: getId -> getEmp_id
                    Staff staff_update = new Staff(
                            staffArrayList.get(position_of_update).getEmp_id(),
                            updated_fname,
                            staffArrayList.get(position_of_update).getLname(),
                            updated_position
                    );

                    staffRepository.UpdateTask(staff_update);

                    // Gi-update: setName -> setFname | setPosition -> setDivision
                    staffArrayList.get(position_of_update).setFname(updated_fname);
                    staffArrayList.get(position_of_update).setDivision(updated_position);

                    notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    public void delete_dialog(int position_of_delete) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Employee");
        builder.setMessage("Confirm Deletion by pressing Yes");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StaffRepository staffRepository = new StaffRepository(context);
                staffRepository.DeleteTask(staffArrayList.get(position_of_delete));
                staffArrayList.remove(position_of_delete);
                notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}