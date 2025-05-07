package com.example.calendar;

import android.app.AlertDialog;
import android.content.Context;
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
    private OnTransactionEnteredListener listener;

    public interface OnTransactionEnteredListener {
        void onTransactionEntered(int position, String income, String expense);
    }


    public CalendarAdapter(List<CalendarDate> dateList, Context context, OnTransactionEnteredListener listener) {
        this.dateList = dateList;
        this.context = context;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textDay, textExpense, textIncome;

        public ViewHolder(View itemView) {
            super(itemView);
            textDay = itemView.findViewById(R.id.textDay);
            textExpense = itemView.findViewById(R.id.textExpense);
            textIncome = itemView.findViewById(R.id.textIncome);
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
            holder.textIncome.setText("");
            holder.textExpense.setText("");
        } else {
            holder.textDay.setText(String.valueOf(date.day));
            holder.textIncome.setText(date.income);
            holder.textExpense.setText(date.expense);

            holder.itemView.setOnClickListener(v -> {
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                EditText inputIncome = new EditText(context);
                inputIncome.setHint("수입 (예: 10000)");
                inputIncome.setInputType(InputType.TYPE_CLASS_NUMBER);
                layout.addView(inputIncome);

                EditText inputExpense = new EditText(context);
                inputExpense.setHint("지출 (예: 5000)");
                inputExpense.setInputType(InputType.TYPE_CLASS_NUMBER);
                layout.addView(inputExpense);

                new AlertDialog.Builder(context)
                        .setTitle(date.day + "일 내역 입력")
                        .setView(layout)
                        .setPositiveButton("확인", (dialog, which) -> {
                            String income = inputIncome.getText().toString();
                            String expense = inputExpense.getText().toString();
                            listener.onTransactionEntered(position,
                                    income.isEmpty() ? "" : "+₩" + income,
                                    expense.isEmpty() ? "" : "-₩" + expense);
                        })
                        .setNegativeButton("취소", null)
                        .show();

            });
        }


    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }
}
