package com.example.d308project.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308project.R;
import com.example.d308project.entities.Vacation;

import java.util.ArrayList;
import java.util.List;

public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.ProductViewHolder> {

    private List<Vacation> mVacations;
    private final Context context; //declare context bc not in an activity, get from inflater
    private final LayoutInflater mInflater;

    public VacationAdapter(Context context) { //inflates list item layout
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private final TextView vacationItemView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            vacationItemView = itemView.findViewById(R.id.textvacationlistitem); //tells it where to put stuff

            itemView.setOnClickListener(new View.OnClickListener() { //what happens when you click an item
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final Vacation current = mVacations.get(position);

                    Intent intent = new Intent(context, VacationDetails.class); //go to detail screen
                    //what info to send
                    intent.putExtra("id", current.getVacationID());
                    intent.putExtra("name", current.getTitle());
                    intent.putExtra("price", current.getPrice());
                    intent.putExtra("hotel", current.getHotel());
                    intent.putExtra("start", current.getStartDate());
                    intent.putExtra("end", current.getEndDate());

                    //start activity, go to next screen
                    context.startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public VacationAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.vacation_list_item, parent, false);
        return new ProductViewHolder(itemView);
        //return null; //autopopulated
    }



    @Override
    public void onBindViewHolder(@NonNull VacationAdapter.ProductViewHolder holder, int position) {
        if (mVacations != null) {
            Vacation current = mVacations.get(position);
            String name = current.getTitle();
            holder.vacationItemView.setText(name);
        } else {
            holder.vacationItemView.setText("No vacation name");
        }
    }

    @Override
    public int getItemCount() {
        if (mVacations != null) {
            return mVacations.size();
        } else
            return 0; //app won't crash if there isn't anything
    }

    public void setVacations(List<Vacation> vacations) { //set vacations on recyclerview from activity housing it
        mVacations = vacations; //list at the top
        notifyDataSetChanged();
    }
}