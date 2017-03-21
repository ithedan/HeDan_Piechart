package com.hedan.piechart_library;

/**
 * Created by DengXiao on 2017/3/13.
 */

public class PieChartBean {
    private int pieColor;
    private float pieValue;
    private float percentage;

    public float getPercentage() {
        return percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    private String pieString;


    public PieChartBean(int pieColor, float pieValue, String pieString) {
        this.pieColor = pieColor;
        this.pieValue = pieValue;
        this.pieString = pieString;
    }
    public PieChartBean(int pieColor, float pieValue) {
        this.pieColor = pieColor;
        this.pieValue = pieValue;
    }

    public float getPieValue() {
        return pieValue;
    }

    public void setPieValue(float pieValue) {
        this.pieValue = pieValue;
    }

    public int getPieColor() {
        return pieColor;
    }

    public void setPieColor(int pieColor) {
        this.pieColor = pieColor;
    }


    public String getPieString() {
        return pieString;
    }

    public void setPieString(String pieString) {
        this.pieString = pieString;
    }
}
