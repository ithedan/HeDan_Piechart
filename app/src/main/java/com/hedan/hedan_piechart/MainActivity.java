package com.hedan.hedan_piechart;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hedan.piechart_library.PieChartBean;
import com.hedan.piechart_library.PieChart_View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private PieChart_View pieView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pieView = (PieChart_View) findViewById(R.id.pie_view);
        ArrayList<PieChartBean> lists = new ArrayList<>();
        lists.add(new PieChartBean(Color.parseColor("#ee3c5d"), 300, "苹果"));
        lists.add(new PieChartBean(Color.parseColor("#ffc12c"), 50, "香蕉"));
        lists.add(new PieChartBean(Color.parseColor("#1fde94"), 599, "哈密瓜"));
        lists.add(new PieChartBean(Color.parseColor("#f5a623"), 500, "橘子"));
        lists.add(new PieChartBean(Color.parseColor("#fa734e"), 1000, "桃子"));
        lists.add(new PieChartBean(Color.parseColor("#947ddf"), 300, "葡萄"));
        lists.add(new PieChartBean(Color.parseColor("#ee3c5d"), 50, "火龙果"));
        lists.add(new PieChartBean(Color.parseColor("#f7964f"), 500, "芒果"));
        lists.add(new PieChartBean(Color.parseColor("#ff4639"), 400, "西红柿"));
        lists.add(new PieChartBean(Color.parseColor("#ff8053"), 300, "柠檬"));
        lists.add(new PieChartBean(Color.parseColor("#ee3c5d"), 5, "樱桃"));
        lists.add(new PieChartBean(Color.parseColor("#1fde94"), 500, "西瓜"));
        pieView.setData(lists);
    }

}
