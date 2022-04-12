package com.example.keepitup;

import static com.example.keepitup.MainActivity.db;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HabitCheckAdapter extends
        RecyclerView.Adapter<HabitCheckAdapter.ViewHolder>{

    private List<Habit> habits;
    private HabitDao habitDao = db.habitDao();

    public HabitCheckAdapter(List<Habit> habitList){habits = habitList;}

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgIcon, imgAdd, imgRemove;
        public TextView tvName, tvCompleted;

        public ViewHolder(View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.imageView_HabitCheck_Icon);
            imgAdd = itemView.findViewById(R.id.imageView_HabitCheck_Add);
            imgRemove = itemView.findViewById(R.id.imageView_HabitCheck_Remove);
            tvName = itemView.findViewById(R.id.textView_HabitCheck_Name);
            tvCompleted = itemView.findViewById(R.id.textView_HabitCheck_Completed);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View thisView = inflater.inflate(R.layout.recycleritem_checklist, parent, false);
        ViewHolder viewHolder = new ViewHolder(thisView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Habit habit = habits.get(position);
        ImageView imgIcon    = holder.imgIcon;
        ImageView imgAdd     = holder.imgAdd;
        ImageView imgRemove  = holder.imgRemove;
        TextView tvName      = holder.tvName;
        TextView tvCompleted = holder.tvCompleted;

        int amountPerDay = habit.getAmountPerDay();
        if(habit.getCompleted() > amountPerDay){
            habit.setCompleted(amountPerDay);
            habitDao.update(habit);
        }
        int completed = habit.getCompleted();

        tvCompleted.setText(completed + "/" + amountPerDay);
        tvName.setText(habit.getName());
        imgIcon.setImageResource(habit.getImage());
        imgIcon.setColorFilter(habit.getColor());

        imgAdd.setOnClickListener(view -> {
            if(habit.getCompleted() < amountPerDay){
                addOrRemoveCompleted(habit, 1, tvCompleted);
            }
        });
        imgRemove.setOnClickListener(view -> {

            if(habit.getCompleted()>0){
                addOrRemoveCompleted(habit, -1, tvCompleted);
            }
        });
    }

    public void addOrRemoveCompleted(Habit habit, int numberIncreaseOrDecrease,
                                     TextView tvWhereUpdateText) {
        habit.setCompleted(habit.getCompleted()+numberIncreaseOrDecrease);
        tvWhereUpdateText.setText(habit.getCompleted()+ "/" + habit.getAmountPerDay());
        habitDao.update(habit);
    }

    @Override
    public int getItemCount() { return habits.size(); }
}
