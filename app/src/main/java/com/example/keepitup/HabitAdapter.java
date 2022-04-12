package com.example.keepitup;

import static com.example.keepitup.MainActivity.cancelNotification;
import static com.example.keepitup.MainActivity.db;
import static com.example.keepitup.MainActivity.setNotification;
import static com.example.keepitup.MainActivity.setNotifications;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class HabitAdapter extends
        RecyclerView.Adapter<HabitAdapter.ViewHolder>{

    private List<Habit> habits;
    private HabitDao habitDao = db.habitDao();

    public HabitAdapter(List<Habit> habitList){habits = habitList;}

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout llEdit;
        public ImageView imgIcon, imgIconEdit, imgColorEdit, imgOpenClose, imgDelete;
        public TextView tvName;
        public EditText edtName, edtWeekDays, edtAmount, edtNotificationTime;
        public CheckBox cbNotification;


        public ViewHolder(View itemView) {
            super(itemView);
            llEdit              = itemView.findViewById(R.id.linearLayout_Edit);
            imgIcon             = itemView.findViewById(R.id.imageView_Icon);
            imgIconEdit         = itemView.findViewById(R.id.imageView_Icon_Edit);
            imgColorEdit        = itemView.findViewById(R.id.imageView_Color_Edit);
            imgOpenClose        = itemView.findViewById(R.id.imageView_OpenClose);
            imgDelete           = itemView.findViewById(R.id.imageView_Delete);
            tvName              = itemView.findViewById(R.id.textView_Name);
            edtName             = itemView.findViewById(R.id.editText_Name);
            edtWeekDays         = itemView.findViewById(R.id.editText_Week_Days);
            edtAmount           = itemView.findViewById(R.id.editText_Amount);
            edtNotificationTime = itemView.findViewById(R.id.editText_Notification_Time);
            cbNotification      = itemView.findViewById(R.id.checkBox_Notification);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View thisView = inflater.inflate(R.layout.recycleritem_habit, parent, false);
        ViewHolder viewHolder = new ViewHolder(thisView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Habit habit = habits.get(position);
        LinearLayout llEdit          = holder.llEdit;
        ImageView imgIcon            = holder.imgIcon;
        ImageView imgIconEdit        = holder.imgIconEdit;
        ImageView imgColorEdit       = holder.imgColorEdit;
        ImageView imgOpenClose       = holder.imgOpenClose;
        ImageView imgDelete          = holder.imgDelete;
        TextView tvName              = holder.tvName;
        EditText edtName             = holder.edtName;
        EditText edtWeekDays         = holder.edtWeekDays;
        EditText edtAmount           = holder.edtAmount;
        EditText edtNotificationTime = holder.edtNotificationTime;
        CheckBox cbNotification      = holder.cbNotification;


        putWeekDaysIntoEditText(habit.getDaysOfWeek(), edtWeekDays);
        imgIcon.setColorFilter(habit.getColor());
        imgIcon.setImageResource(habit.getImage());
        imgIconEdit.setImageResource(habit.getImage());
        imgColorEdit.setBackgroundColor(habit.getColor());
        tvName.setText(habit.getName());
        edtName.setText(habit.getName());
        llEdit.setVisibility(View.GONE);
        edtAmount.setText(String.valueOf(habit.getAmountPerDay()));
        edtNotificationTime.setText(habit.getNotificationTime().toString());
        cbNotification.setChecked(habit.isNotification());
        imgOpenClose.setImageResource(R.drawable.ic_down);

        cbNotification.setOnClickListener(view -> {
            Context context = view.getContext();
            habit.setNotification(cbNotification.isChecked());
            habitDao.update(habit);
            setNotification(habit, context);
        });

        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable text) {
                Context context = edtName.getContext();
                habit.setName(text.toString());
                tvName.setText(habit.getName());
                habitDao.update(habit);
                setNotification(habit, context);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
        });

        imgIconEdit.setOnClickListener(view -> {
            Context context = view.getContext();
            ArrayList<Integer> icons = MainActivity.getIcons();
            ArrayList<Integer> colors = new ArrayList<>();

            final Dialog d = new Dialog(context);
            d.setContentView(R.layout.dialog_icon_color);

            RecyclerView recyclerView = d.findViewById(R.id.recyclerView_Icon_Color);
            recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false));

            IconColorAdapter adapter = new IconColorAdapter(colors,icons,habit, d, this, position);
            recyclerView.setAdapter(adapter);

            d.show();
        });

        imgColorEdit.setOnClickListener(view -> {
            Context context = view.getContext();
            ArrayList<Integer> icons = new ArrayList<>();
            ArrayList<Integer> colors = MainActivity.getColors(context);

            final Dialog d = new Dialog(context);
            d.setContentView(R.layout.dialog_icon_color);

            RecyclerView recyclerView = d.findViewById(R.id.recyclerView_Icon_Color);
            recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false));

            IconColorAdapter adapter = new IconColorAdapter(colors,icons,habit,d, this, position);
            recyclerView.setAdapter(adapter);

            d.show();
        });

        imgOpenClose.setOnClickListener(view -> {
            if(llEdit.getVisibility() == View.VISIBLE) {
                llEdit.setVisibility(View.GONE);
                imgOpenClose.setImageResource(R.drawable.ic_down);
            }
            else {
                llEdit.setVisibility(View.VISIBLE);
                imgOpenClose.setImageResource(R.drawable.ic_up);
            }
        });

        edtAmount.setOnClickListener(view -> {
            final Dialog d = new Dialog(view.getContext());
            d.setContentView(R.layout.dialog_number);
            Button btnOk = d.findViewById(R.id.button_number_ok);
            NumberPicker np = d.findViewById(R.id.numberPicker);

            d.setTitle("NumberPicker");
            np.setWrapSelectorWheel(false);

            np.setMaxValue(20);
            np.setMinValue(1);

            btnOk.setOnClickListener(dialogView -> {
                int newAmountPerDay = np.getValue();
                edtAmount.setText(String.valueOf(newAmountPerDay));
                habit.setAmountPerDay(newAmountPerDay);
                habitDao.update(habit);
                d.dismiss();
            });

            d.show();
        });

        imgDelete.setOnClickListener(view -> {
            habitDao.delete(habit);
            habits.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, habits.size());
            cancelNotification(habit, view.getContext());
        });

        edtNotificationTime.setOnClickListener(view -> {
            TimePickerDialog.OnTimeSetListener timeSetListener = ((timePicker, hour, minute) ->{
                    edtNotificationTime.setText(String.format(Locale.getDefault(),
                            "%02d:%02d", hour , minute));
                    habit.setNotificationTime(LocalTime.of(hour, minute));
                    habitDao.update(habit);
                    setNotification(habit, view.getContext());
            });

            int hour = 0, minute = 0;
            TimePickerDialog timePickerDialog = new TimePickerDialog(view.getContext(),
                    timeSetListener, hour, minute, true);

            timePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, view.getResources().getString(R.string.ok), timePickerDialog);
            timePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, view.getResources().getString(R.string.cancel), timePickerDialog);

            timePickerDialog.setTitle(view.getResources().getString(R.string.habit_notification_time_choose));
            timePickerDialog.show();
        });

        edtWeekDays.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle(view.getResources().getString(R.string.week_days_choose));

            boolean[] daysOfWeek = habit.getDaysOfWeek();

            builder.setMultiChoiceItems(view.getResources().getStringArray(R.array.weekDays), daysOfWeek,
                    (dialogInterface, item, isChecked) -> {

                    });

            builder.setPositiveButton(R.string.ok,
                    (dialogInterface, listener) -> {
                        putWeekDaysIntoEditText(daysOfWeek, edtWeekDays);
                        habit.setDaysOfWeek(daysOfWeek);
                        habitDao.update(habit);
                        setNotification(habit, view.getContext());
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        });
    }

    public void putWeekDaysIntoEditText(boolean[] daysOfWeek, EditText edtWeekDays){
        String[] weekDays = edtWeekDays.getResources().getStringArray(R.array.weekDaysAbbreviated);
        String text = "";
        for (int i = 0; i < daysOfWeek.length; i++){
            if (daysOfWeek[i])text += weekDays[i] + ", ";
        }
        if(text.length() > 0) edtWeekDays.setText(text.substring(0, text.length() - 2));
        else edtWeekDays.setText(edtWeekDays.getResources().getString(R.string.week_days_never));
    }

    @Override
    public int getItemCount() {
        return habits.size();
    }
}