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
import com.example.d308project.ui.VacationDetails;

import java.util.List;

public class VacationAdapter
        extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {

    private List<Vacation> mVacations;
    private final Context context;
    private final LayoutInflater mInflater;

    public VacationAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    class VacationViewHolder extends RecyclerView.ViewHolder {

        private final TextView vacationItemView;

        public VacationViewHolder(@NonNull View itemView) {
            super(itemView);

            vacationItemView =
                    itemView.findViewById(R.id.textVacationlistItem);

            itemView.setOnClickListener(v -> {

                int position = getAdapterPosition();

                if (mVacations != null) {

                    Vacation current = mVacations.get(position);

                    Intent intent =
                            new Intent(context,
                                    VacationDetails.class);

                    intent.putExtra("vacationID",
                            current.getVacationID());

                    context.startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public VacationViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View itemView =
                mInflater.inflate(
                        R.layout.vacation_list_item,
                        parent,
                        false);

        return new VacationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(
            @NonNull VacationViewHolder holder,
            int position) {

        if (mVacations != null) {

            Vacation current = mVacations.get(position);

            holder.vacationItemView.setText(current.getTitle());

        } else {

            holder.vacationItemView.setText("No Vacation");
        }
    }

    @Override
    public int getItemCount() {
        return (mVacations != null)
                ? mVacations.size()
                : 0;
    }

    public void setVacations(List<Vacation> vacations) {
        mVacations = vacations;
        notifyDataSetChanged();
    }
}