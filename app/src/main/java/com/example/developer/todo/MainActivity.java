package com.example.developer.todo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.developer.todo.adapter.RecordAdapter;
import com.example.developer.todo.database.DatabaseService;
import com.example.developer.todo.model.Record;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static String RECORD_LIST = "RECORD_LIST";

    private DatabaseBroadcastReceiver receiver;
    private final static String KEY_ACTION = "KEY_ACTION";
    private final static String ACTION_ACCESS_TO_RECORD_TABLE = "ACCESS_TO_RECORD_TABLE";

    private ListView lvRecord;

    private TextView tvStartDate;

    private Calendar calendar;

    private SimpleDateFormat sdfDate;

    private ImageView ivCalendar;
    private ImageView ivAdd;
    private ImageView ivCategory;
    private ImageView ivStatistic;

    private List<Record> recordList;
    private RecordAdapter recordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvStartDate=(TextView) findViewById(R.id.tvStartDate);
        sdfDate =  new SimpleDateFormat("dd.MM.yyyy");
        onBtnImgCalendarClick(null);

        receiver = new DatabaseBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RECORD_LIST);
        registerReceiver(receiver, intentFilter);

        lvRecord = (ListView) findViewById(R.id.lvRecord);
        lvRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long recordId = recordAdapter.getRecordList().get(position).getId();
                Intent intent = new Intent(MainActivity.this, AddRecordActivity.class);
                intent.putExtra("recordId", recordId);
                startActivity(intent);
            }
        });
        registerForContextMenu(lvRecord);

        recordList = new ArrayList<Record>();
        recordAdapter = new RecordAdapter(getApplicationContext(), recordList);
        lvRecord.setAdapter(recordAdapter);

        ivCalendar = (ImageView) findViewById(R.id.ivCalendar);
        ivCalendar.setImageResource(R.drawable.today);

        ivAdd = (ImageView) findViewById(R.id.ivAdd);
        ivAdd.setImageResource(R.drawable.add);

        ivCategory = (ImageView) findViewById(R.id.ivCategory);
        ivCategory.setImageResource(R.drawable.details);

        ivStatistic = (ImageView) findViewById(R.id.ivStatistic);
        ivStatistic.setImageResource(R.drawable.pie);

        setTitle(getString(R.string.app_title));

        intentToService();
    }

    public void onBtnImgCalendarClick(View view){
        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis() - 1000);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);

        tvStartDate.setText(sdfDate.format(calendar.getTime()));
    }
    public void onClickStartDate(View view) {
        new DatePickerDialog(this, dateDialogStart, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    DatePickerDialog.OnDateSetListener dateDialogStart = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int newYear, int monthOfYear,
                              int dayOfMonth) {
            calendar.set(Calendar.YEAR, newYear);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            calendar.set(Calendar.HOUR_OF_DAY,0);
            calendar.set(Calendar.MINUTE,0);
            tvStartDate.setText(sdfDate.format(calendar.getTime()));

            intentToService();

        }
    };

    public void intentToService(){
        Intent intent = new Intent(MainActivity.this, DatabaseService.class);
        intent.putExtra(KEY_ACTION, "getAll");
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        intent.putExtra("startDate", calendar.getTimeInMillis());
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        intent.putExtra("endDate", calendar.getTimeInMillis());
        intent.setAction(ACTION_ACCESS_TO_RECORD_TABLE);
        startService(intent);
    }

    public void onClickOpenCategoryList(View view){
        Intent intent=new Intent(this,AllCategoryActivity.class);
        startActivity(intent);
    }
    public void onClickInsertRecord(View view){
        Intent intent=new Intent(this,AddRecordActivity.class);
        intent.putExtra("chooseDate",calendar.getTimeInMillis());
        startActivity(intent);
    }

    public class DatabaseBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(RECORD_LIST)) {
                recordList.clear();
                recordList = (List<Record>) intent.getSerializableExtra("recordList");
                recordAdapter =new RecordAdapter(getApplicationContext(), recordList);
                lvRecord.setAdapter(recordAdapter);
                recordAdapter.notifyDataSetChanged();
            }

        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final RecordAdapter recordAdapter = (RecordAdapter) lvRecord.getAdapter();

        switch (item.getItemId()) {
            case R.id.item_edit: //изменение записи
                long recordId = recordAdapter.getRecordList().get(info.position).getId();
                Intent intent = new Intent(this, AddRecordActivity.class);
                intent.putExtra("recordId", recordId);
                startActivity(intent);
                break;
            case R.id.item_delete:  //удаление категории
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.dialog_message_record).setTitle(R.string.delete_record_title);
                builder.setPositiveButton(R.string.dialog_btn_delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        long recordId = recordAdapter.getRecordList().get(info.position).getId();
                        Intent intent = new Intent(getApplicationContext(), DatabaseService.class);
                        intent.putExtra(KEY_ACTION, "getAll");
                        calendar.set(Calendar.HOUR_OF_DAY,0);
                        calendar.set(Calendar.MINUTE,0);
                        intent.putExtra("startDate", calendar.getTimeInMillis());
                        calendar.set(Calendar.HOUR_OF_DAY,23);
                        calendar.set(Calendar.MINUTE,59);
                        intent.putExtra("endDate", calendar.getTimeInMillis());
                        intent.putExtra("recordId", recordId);
                        intent.putExtra(KEY_ACTION, "delete");
                        intent.setAction(ACTION_ACCESS_TO_RECORD_TABLE);
                        startService(intent);

                    }
                });
                builder.setNegativeButton(R.string.dialog_btn_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        intentToService();
    }
    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu_category, menu);
    }

    public void onClickViewStatistics(View view){
        Intent intent=new Intent(this,StatisticsActivity.class);
        startActivity(intent);
    }
}
