package com.example.calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {
    private List<CalendarDate> dateList;
    private Context context;
    private OnDateClickListener listener;

    public interface OnDateClickListener {
        void onDateClicked(int position);
    }

    public CalendarAdapter(List<CalendarDate> dateList, Context context, OnDateClickListener listener) {
        this.dateList = dateList;
        this.context = context;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textDay, textExpense;//, textIncome;
        View container;

        public ViewHolder(View itemView) {
            super(itemView);
            container = itemView;
            textDay = itemView.findViewById(R.id.textDay);
            textExpense = itemView.findViewById(R.id.textExpense);
            //textIncome = itemView.findViewById(R.id.textIncome);
        }
    }

    @Override
    public CalendarAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_calendar_date, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CalendarDate date = dateList.get(position);

        if (date.day == 0) {
            holder.textDay.setText("");
            holder.textExpense.setText("");
            //holder.textIncome.setText("");
            holder.container.setBackgroundColor(Color.TRANSPARENT);
            holder.itemView.setOnClickListener(null);
        } else {
            holder.textDay.setText(String.valueOf(date.day));
            //holder.textIncome.setText(date.income);
            holder.textExpense.setText(date.getTotalExpenseString());

            if (date.isSelected) {
                holder.container.setBackgroundResource(R.drawable.selected_date_bg);
            } else {
                holder.container.setBackgroundColor(Color.TRANSPARENT);
            }

            holder.itemView.setOnClickListener(v -> listener.onDateClicked(position));
        }

        if (!date.isCurrentMonth) {
            holder.textDay.setAlpha(0.3f);
            holder.textExpense.setAlpha(0.3f);
            //holder.textIncome.setAlpha(0.3f);
        } else {
            holder.textDay.setAlpha(1f);
            holder.textExpense.setAlpha(1f);
            //holder.textIncome.setAlpha(1f);
        }
    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }
}
