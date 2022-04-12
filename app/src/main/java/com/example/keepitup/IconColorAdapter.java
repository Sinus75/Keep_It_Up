package com.example.keepitup;

import static com.example.keepitup.MainActivity.db;
import static com.example.keepitup.MainActivity.setNotification;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class IconColorAdapter extends
        RecyclerView.Adapter<IconColorAdapter.ViewHolder> {

    private HabitDao habitDao = db.habitDao();
    public List<Integer> colors, icons;
    public Habit habit;
    public Dialog dialog;
    public HabitAdapter habitAdapter;
    public int habitPosition;

    public IconColorAdapter(List<Integer> colorList, List<Integer> iconList,
                            Habit currentHabit, Dialog currentDialog,
                            HabitAdapter adapter, int position){
        colors = colorList;
        icons = iconList;
        habit = currentHabit;
        dialog = currentDialog;
        habitAdapter = adapter;
        habitPosition = position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgIconColor;

        public ViewHolder(View itemView) {
            super(itemView);
            imgIconColor = itemView.findViewById(R.id.imageView_Icon_Color);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View thisView = inflater.inflate(R.layout.recycleritem_icon_color, parent, false);
        IconColorAdapter.ViewHolder viewHolder = new IconColorAdapter.ViewHolder(thisView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ImageView imgIconColor = holder.imgIconColor;
        int color = 0, icon = 0;
        if (position < colors.size()) color = colors.get(position);
        if (position < icons.size()) icon = icons.get(position);

        if (color != 0) imgIconColor.setBackgroundColor(color);
        else imgIconColor.setImageResource(icon);

        imgIconColor.setOnClickListener(view -> {
            if (position < colors.size()) habit.setColor(colors.get(position));
            if (position < icons.size()) habit.setImage(icons.get(position));
           habitDao.update(habit);
           dialog.dismiss();
           habitAdapter.notifyItemChanged(habitPosition);
           setNotification(habit, imgIconColor.getContext());
        });

    }

    @Override
    public int getItemCount() {
        return icons.size() + colors.size();
    }

}
