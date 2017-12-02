package com.example.developer.todo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.developer.todo.R;
import com.example.developer.todo.database.DatabaseHelper;
import com.example.developer.todo.model.Category;
import com.example.developer.todo.model.Record;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class RecordAdapter extends BaseAdapter {

    private Context context;
    private List<Record> recordList;
    private LayoutInflater inflater;

    private Calendar c;

    public RecordAdapter(Context context, List<Record> recordList) {
        this.context = context;
        this.recordList = recordList;
        inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        c = Calendar.getInstance();
    }


    @Override
    public int getCount() {
        return recordList.size();
    }

    @Override
    public Object getItem(int i) {
        return recordList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.item_record, viewGroup, false);
        final Record record = getRecord(i);
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        Category category = dbHelper.getCategory(record.getIdCategory());

        ((TextView) convertView.findViewById(R.id.tvDescription)).setText(record.getDescription());
        ((ImageView) convertView.findViewById(R.id.ivCategory)).setImageResource(context.getResources().getIdentifier(category.getIcon(), "drawable", context.getPackageName()));
        ((ImageView) convertView.findViewById(R.id.ivCategory)).setTag(category.getIcon());


        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date startDate = new Date(record.getTimeStart());
        String strStartDate = sdf.format(startDate);

        sdf = new SimpleDateFormat("HH");
        Date duration = new Date(record.getDuration());
        String strHour = sdf.format(duration);
        sdf = new SimpleDateFormat("mm");
        String strMinute = sdf.format(duration);
        String strDuration ="";
        if(!strHour.equals("00")) {
            strHour = strHour.replaceFirst("^0", "");
            strDuration = strDuration + strHour + context.getResources().getString(R.string.hours);
        }
        if(!strMinute.equals("00")) {
            strMinute = strMinute.replaceFirst("^0", "");
            strDuration = strDuration + " " + strMinute + context.getResources().getString(R.string.minutes);
        }
        ((TextView) convertView.findViewById(R.id.tvTime)).setText(strStartDate);
        ((TextView) convertView.findViewById(R.id.tvDuration)).setText(category.getTitle()+" "+strDuration.trim());

        return convertView;

    }
    public Record getRecord(int position){
        return ((Record) getItem(position));
    }

    public List<Record> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<Record> recordList) {
        this.recordList = recordList;
    }

}