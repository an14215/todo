package com.example.developer.todo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.developer.todo.adapter.StatisticsAdapter;
import com.example.developer.todo.model.StatisticCategory;

import java.util.List;

public class StatisticResultActivity extends AppCompatActivity {

    private ListView lvCategory;
    private TextView tvExistRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_result);

        lvCategory = (ListView) findViewById(R.id.lvCategory);
        tvExistRecord = (TextView)findViewById(R.id.tvExistRecord);

        List<StatisticCategory> statisticCategoryList = (List<StatisticCategory>) getIntent().getSerializableExtra("statisticCategoryList");
        StatisticsAdapter statisticsAdapter = new StatisticsAdapter(getApplicationContext(), statisticCategoryList);
        lvCategory.setAdapter(statisticsAdapter);

        if(statisticCategoryList.size()==0) {
            tvExistRecord.setVisibility(View.VISIBLE);
            tvExistRecord.setText(R.string.not_exist_record);
        }else  tvExistRecord.setVisibility(View.GONE);

        setTitle(getString(R.string.title_statistics));
    }

}
