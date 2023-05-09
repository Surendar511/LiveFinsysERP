package com.finsyswork.erp;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class frm_graph extends AppCompatActivity {

    TextView txt0, txt1, txt2;
    private PieChart mChartpie;
    private BarChart mChartbar;
    private LineChart mChartline;
    ActionBar actionBar;
    LinearLayout llpie, llbar, llline;
    String charttype, barchartname = "Bar", linechartname = "Line", Piechartname = "Pie";
    private String TAG = "frm_graph";
    boolean ispie = false;
    ArrayList<team> fedlist;
    Button btnLine, btnBar, btnPie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frm_graph);
        actionBar = getSupportActionBar();
        actionBar.setTitle(fgen.rptheader);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //actionBar.setBackgroundDrawable(ContextCompat.getDrawable(frm_graph.this, R.drawable.side_nav_bar2));

        mChartpie = (PieChart) findViewById(R.id.piechart);
        mChartbar = (BarChart) findViewById(R.id.barchart);
        mChartline = (LineChart) findViewById(R.id.linechart);
        llpie = (LinearLayout) findViewById(R.id.llpie);
        llbar = (LinearLayout) findViewById(R.id.llbar);
        llline = (LinearLayout) findViewById(R.id.llline);
        btnLine = (Button) findViewById(R.id.btnLine);
        btnBar = (Button) findViewById(R.id.btnBar);
        btnPie = (Button) findViewById(R.id.btnPie);

        llline.setVisibility(View.GONE);
        llbar.setVisibility(View.GONE);
        llpie.setVisibility(View.GONE);

        page_Load();

        btnPie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_data();
                pie();
            }
        });
        btnLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_data();
                line();
            }
        });
        btnBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_data();
                bar();
            }
        });
    }

    public void page_Load() {
        Log.d(TAG, "Login");


        final MyProgressdialog progressDialog = new MyProgressdialog(frm_graph.this);
        progressDialog.show();

        // TODO: Implement your own authentication logic here.

        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        show_data();
                        progressDialog.dismiss();
                    }
                }, 100);
    }

    private void show_data() {
        fedlist = finapi.getApi(fgen.mcd, "DataGraph_api", fgen.btnid.trim(), fgen.cdt1, fgen.cdt2, fgen.hf1col1, "-");
        if (fedlist.size() > 0) {
            bar();
        }
    }

    @SuppressLint("WrongConstant")
    private void bar() {

        llbar.setVisibility(View.VISIBLE);
        llpie.setVisibility(View.GONE);
        llline.setVisibility(View.GONE);

        charttype = barchartname;
        mChartbar.setDrawBarShadow(false);
        mChartbar.setDrawValueAboveBar(true);
        mChartbar.setPinchZoom(false);

        XAxis xl = mChartbar.getXAxis();

        YAxis leftAxis = mChartbar.getAxisLeft();
        leftAxis.setValueFormatter(new MyAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.valueOf((int) value);
            }

        });
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(30f);
        leftAxis.setDrawLabels(true);
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true
        mChartbar.getAxisRight().setEnabled(false);

        //data
        float groupSpace = 0.04f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.46f; // x2 dataset
        //(0.46 + 0.02) * 2 + 0.04 = 1.00 -> interval per "group"

        int startYear = 1980;
        int endYear = 1985;

        set_BarData();
        Legend l = mChartbar.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        l.setYOffset(10);
        l.setStackSpace(30);

        //mChartbar.setOnChartValueSelectedListener(this);
        mChartbar.setDrawBarShadow(false);
        // mChartbar.setDrawValueAboveBar(true);
        mChartbar.getDescription().setEnabled(false);
        mChartbar.setPinchZoom(false);


        // mChartbar.setDrawGridBackground(false);
        mChartbar.animateXY(700, 700);
        //   mChartbar.getBarData().setBarWidth(barWidth);
        //   mChartbar.getXAxis().setAxisMinValue(0);
        //  mChartbar.groupBars(startYear, groupSpace, barSpace);
        mChartbar.invalidate();
    }

    private void set_BarData() {
        List<BarEntry> yVals1 = new ArrayList<BarEntry>();
        List<BarEntry> yVals2 = new ArrayList<BarEntry>();
        BarDataSet set1, set2, set3, set4, set5, set6, set7, set8, set9, set10;
        String mq = "";

        {
            // create 2 datasets with different types
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            for (int i = 0; i < fedlist.size(); i++) {
                team t = fedlist.get(i);
                float[] data1 = {10, 30};
                yVals1 = new ArrayList<BarEntry>();
                yVals1.add(new BarEntry(i, fgen.make_Float(t.getcol4())));
                //yVals1.add(new BarEntry(i, data1));
                if (t.getcol1().trim().length() > 10) {
                    mq = t.getcol3().substring(0, 8);
                } else {
                    mq = t.getcol3();
                }
                set1 = new BarDataSet(yVals1, mq);
                Random random = new Random(); // Probably really put this somewhere where it gets executed only once
                int red = random.nextInt(256);
                int green = random.nextInt(256);
                int blue = random.nextInt(256);
                set1.setColor(Color.rgb(red, green, blue));
                dataSets.add(set1);
            }

            BarData data = new BarData(dataSets);
            mChartbar.setData(data);
        }
    }

    @SuppressLint("WrongConstant")
    private void pie() {
        ispie = true;
        llpie.setVisibility(View.VISIBLE);
        llbar.setVisibility(View.GONE);
        llline.setVisibility(View.GONE);

        mChartpie.setUsePercentValues(true);
        charttype = Piechartname;
        //to show lable in Chart
        // mChartpie.getDescription().setEnabled(false);
        // mChartpie.setExtraOffsets(1, -10, 1, 1);
        mChartpie.setExtraTopOffset(-190);
        mChartpie.setDragDecelerationFrictionCoef(0.95f);
        // mChartpie.setCenterTextTypeface(mTfLight);
        mChartpie.setCenterText(generateCenterSpannableText());
        mChartpie.setDrawHoleEnabled(true);
        mChartpie.setHoleColor(Color.WHITE);
        mChartpie.setEntryLabelColor(Color.BLACK);
        mChartpie.setTransparentCircleColor(Color.WHITE);
        mChartpie.setTransparentCircleAlpha(110);
        mChartpie.setHoleRadius(58f);
        mChartpie.setTransparentCircleRadius(61f);

        mChartpie.setDrawCenterText(true);

        mChartpie.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChartpie.setRotationEnabled(false);
        mChartpie.setHighlightPerTapEnabled(true);


        // mChartpie.setUnit(" €");
        // mChartpie.setDrawUnitsInChart(true);

        // add a selection listener
        //mChartpie.setOnChartValueSelectedListener(this);

        setData_pie();

        mChartpie.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChartpie.spin(2000, 0, 360);


        // mChartpie.setUsePercentValues(!mChartpie.isUsePercentValuesEnabled());
        // mChartpie.invalidate();

        Legend l = mChartpie.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);
        l.setXEntrySpace(0f);
        l.setYEntrySpace(0f);
        l.setYOffset(180);
        l.setWordWrapEnabled(true);
        // entry label styling
        mChartpie.setEntryLabelColor(Color.WHITE);
        //  mChartpie.setEntryLabelTypeface(mTfRegular);
        mChartpie.setEntryLabelTextSize(12f);
        mChartpie.setDrawEntryLabels(false);
        mChartpie.invalidate();
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("Finsys\nERP");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 6, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 6, s.length() - 3, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 6, s.length() - 3, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 6, s.length() - 3, 0);
        return s;
    }

    private void setData_pie() {


        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        if (ispie) {

            double others = 0;
            for (int i = 0; i < fedlist.size(); i++) {
                team t1 = fedlist.get(i);
                if (i > 9) {
                    others = others + fgen.make_double(t1.getcol4());
                } else {
                    entries.add(new PieEntry(fgen.make_Float(t1.getcol4()), t1.getcol2(), t1.getcol3() + ""));
                }
            }
            if (fedlist.size() > 10) {
                entries.add(new PieEntry(fgen.make_Float(String.valueOf(others)), "Others", ""));
            }


        } else {


            for (int i = 0; i < fedlist.size(); i++) {
                team t = fedlist.get(i);
                entries.add(new PieEntry(fgen.make_Float(t.getcol4()), t.getcol2(), t.getcol3() + ""));
            }
        }
        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setSliceSpace(0f);
        dataSet.setSelectionShift(5f);


        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);
        //  data.setValueTypeface(mTfLight);
        mChartpie.setData(data);
        // undo all highlights
        mChartpie.highlightValues(null);
        mChartpie.invalidate();

    }

    public void line() {

        // no description text
        llline.setVisibility(View.VISIBLE);
        llbar.setVisibility(View.GONE);
        llpie.setVisibility(View.GONE);
        mChartline.setDrawGridBackground(false);

        // no description text
        mChartline.getDescription().setEnabled(false);

        // enable touch gestures
        mChartline.setTouchEnabled(false);

        // enable scaling and dragging
        mChartline.setDragEnabled(false);
        mChartline.setScaleEnabled(false);
        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChartline.setPinchZoom(false);

        // set an alternative background color
        // mChart.setBackgroundColor(Color.GRAY);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
//        MyMarkerView mv = new MyMarkerView(this, R.layout.custom_marker_view);
//        mv.setChartView(mChart); // For bounds control
//        mChart.setMarker(mv); // Set the marker to the chart

        // x-axis limit line
        LimitLine llXAxis = new LimitLine(10f, "Index 10");
        llXAxis.setLineWidth(4f);
        llXAxis.enableDashedLine(10f, 10f, 0f);
        llXAxis.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
        llXAxis.setTextSize(10f);
        XAxis xAxis = mChartline.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setLabelCount(12);
        //xAxis.setValueFormatter(new MyCustomXAxisValueFormatter());
        //xAxis.addLimitLine(llXAxis); // add x-axis limit line


        // Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

//        LimitLine ll1 = new LimitLine(150f, "Upper Limit");
//        ll1.setLineWidth(4f);
//        ll1.enableDashedLine(10f, 10f, 0f);
//        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
//        ll1.setTextSize(10f);
        // ll1.setTypeface(tf);

//        LimitLine ll2 = new LimitLine(-30f, "Lower Limit");
//        ll2.setLineWidth(4f);
//        ll2.enableDashedLine(10f, 10f, 0f);
//        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
//        ll2.setTextSize(10f);
        // ll2.setTypeface(tf);

//        YAxis leftAxis = mChartline.getAxisLeft();
//        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
//        leftAxis.addLimitLine(ll1);
//        leftAxis.addLimitLine(ll2);
//        leftAxis.setAxisMaximum(200f);
//        leftAxis.setAxisMinimum(-50f);
        //leftAxis.setYOffset(20f);
//        leftAxis.enableGridDashedLine(10f, 10f, 0f);
//        leftAxis.setDrawZeroLine(false);
//
//        // limit lines are drawn behind data (and not on top)
//        leftAxis.setDrawLimitLinesBehindData(true);

        //  mChartline.getAxisRight().setEnabled(false);

        //mChart.getViewPortHandler().setMaximumScaleY(2f);
        //mChart.getViewPortHandler().setMaximumScaleX(2f);

        // add data
        setData_line();

//        mChart.setVisibleXRange(20);
//        mChart.setVisibleYRange(20f, AxisDependency.LEFT);
//        mChart.centerViewTo(20, 50, AxisDependency.LEFT);

        mChartline.animateX(2500);
        //mChart.invalidate();

        // get the legend (only possible after setting data)
        Legend l = mChartline.getLegend();
        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
    }

    private void setData_line() {

        ArrayList<Entry> values = new ArrayList<Entry>();
        LineDataSet set1;
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        Random random = new Random(); //

        {
            for (int i = 0; i < fedlist.size(); i++) {
                team t = fedlist.get(i);
                float[] data1 = {10, 30};
                values.add(new Entry(i, fgen.make_Float(t.getcol4())));
                //yVals1.add(new BarEntry(i, data1));
                set1 = new LineDataSet(values, t.getcol3());

                int red = random.nextInt(256);
                int green = random.nextInt(256);
                int blue = random.nextInt(256);
                set1.setColor(Color.rgb(red, green, blue));
                dataSets.add(set1);
            }            // create a data object with the datasets
            LineData data = new LineData(dataSets);

            // set data
            mChartline.setData(data);


            // create a dataset and give it a type
            set1 = new LineDataSet(values, "DataSet 1");

            // set the line to be drawn like this "- - - - - -"
            set1.enableDashedLine(10f, 5f, 0f);
            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.BLACK);
            set1.setCircleColor(Color.BLACK);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            // set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = ContextCompat.getDrawable(this, R.color.colorPrimary);
                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(Color.BLACK);
            }

            set1.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
            dataSets.add(set1);
            // add the datasets

            // create a data object with the datasets
            data = new LineData(dataSets);

            mChartline.setData(data);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.backmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.btnBack:
                super.onBackPressed();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
