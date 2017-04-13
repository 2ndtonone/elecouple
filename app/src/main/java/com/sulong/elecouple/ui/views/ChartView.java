package com.sulong.elecouple.ui.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by ydh on 2015/9/10.
 */
public class ChartView extends LinearLayout {

    public LinkedHashMap<String, Double> pointList = new LinkedHashMap<>();

    public XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();

    public XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

    public XYSeriesRenderer r = new XYSeriesRenderer();//(类似于一条线对象)

    public XYSeries series = new XYSeries("the first line");

    public GraphicalView mChartView;


    public ChartView(Context context) {
        super(context);
    }

    public ChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setPointList(LinkedHashMap<String, Double> map) {
        pointList.clear();
        pointList = map;
    }

    public void repaint() {
        Set<Map.Entry<String, Double>> set = pointList.entrySet();
        Iterator<Map.Entry<String, Double>> iterator = set.iterator();
        int i = 0;
        ArrayList<Double> doubleArrayList = new ArrayList<>();
        series.clear();
        while (iterator.hasNext()) {
            Map.Entry<String, Double> entry = iterator.next();
            mRenderer.addXTextLabel(i, entry.getKey());
            series.add(i, entry.getValue());
            doubleArrayList.add(entry.getValue());
            i++;
        }
        double[] arrayList = getDoubleList(doubleArrayList);
        if (arrayList == null) {
            return;
        }
        mRenderer.setYAxisMin(arrayList[1]);//设置y轴最小值是0
        mRenderer.setYAxisMax(arrayList[0]);
        mChartView.repaint();
    }


    public void lineView(int lineColor, int shadeColor) {
        //设置图表的X轴的当前方向
        mRenderer.setOrientation(XYMultipleSeriesRenderer.Orientation.HORIZONTAL);
        mRenderer.setShowLegend(false); //不显示图例文本
        mRenderer.setChartTitleTextSize(30);//设置图表标题文字的大小
        mRenderer.setLabelsTextSize(25);//设置标签的文字大小
        mRenderer.setLabelsColor(Color.parseColor("#666666"));
        mRenderer.setPointSize(10f);//设置点的大小

        mRenderer.setYLabels(7);//设置Y轴刻度个数（貌似不太准确）
        mRenderer.setYLabelsAlign(Paint.Align.RIGHT);
        mRenderer.setYLabelsPadding(15);
        mRenderer.setXLabelsAlign(Paint.Align.CENTER);
        mRenderer.setShowGrid(true);//显示网格
        mRenderer.setGridColor(Color.parseColor("#cccccc"));

        mRenderer.setPanEnabled(false, false);//设置x,y坐标轴不会因用户划动屏幕而移动
        Set<Map.Entry<String, Double>> set = pointList.entrySet();
        Iterator<Map.Entry<String, Double>> iterator = set.iterator();
        int i = 0;
        ArrayList<Double> doubleArrayList = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<String, Double> entry = iterator.next();
            mRenderer.addXTextLabel(i, entry.getKey());
            series.add(i, entry.getValue());
            doubleArrayList.add(entry.getValue());
            i++;
        }
        double[] arrayList = getDoubleList(doubleArrayList);
        mRenderer.setYAxisMin(arrayList[1]);//设置y轴最小值是0
        mRenderer.setYAxisMax(arrayList[0]);
        mRenderer.setXLabels(0);//设置只显示如1月，2月等替换后的东西，不显示1,2,3等
        mDataset.addSeries(series);

        //top, left, bottom, right
        mRenderer.setMargins(new int[]{70, 70, 30, 40}); // 设置4边留白
        mRenderer.setMarginsColor(Color.argb(0, 0xff, 0, 0));// 设置4边留白透明

        r.setColor(lineColor);//设置颜色
        r.setPointStyle(PointStyle.CIRCLE);//设置点的样式
        r.setFillPoints(true);//填充点（显示的点是空心还是实心）
        r.setDisplayChartValues(true);//将点的值显示出来
        r.setChartValuesSpacing(15);//显示的点的值与图的距离
        r.setChartValuesTextSize(25);//点的值的文字大小
        r.setAnnotationsTextAlign(Paint.Align.RIGHT);
//        r.setFillBelowLine(true);//是否填充折线图的下方
//        r.setFillBelowLineColor(Color.GREEN);//填充的颜色，如果不设置就默认与线的颜色一致
        XYSeriesRenderer.FillOutsideLine fillOutsideLine = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL);
        fillOutsideLine.setColor(shadeColor);
        r.addFillOutsideLine(fillOutsideLine);
        r.setLineWidth(3);//设置线宽
        mRenderer.addSeriesRenderer(r);

        mChartView = ChartFactory.getLineChartView(getContext(), mDataset, mRenderer);
        mChartView.repaint();
        addView(mChartView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * @param doubleArrayList
     * @return double[0] max    doule[1]min
     */

    public double[] getDoubleList(ArrayList<Double> doubleArrayList) {
        int size;
        if (doubleArrayList == null || (size = doubleArrayList.size()) == 0) {
            return null;
        }
        double[] doubles = new double[2];
        doubles[0] = doubleArrayList.get(0);
        doubles[1] = doubles[0];
        for (int i = 0; i < size; i++) {
            double value = doubleArrayList.get(i);
            if (value > doubles[0]) // 判断最大值
                doubles[0] = value;
            if (value < doubles[1]) // 判断最小值
                doubles[1] = value;
        }
        if (doubles[0] == doubles[1]) {
            doubles[0] += 40;//避免Y轴无刻度
        }
        return doubles;
    }

    public void show(int lineColor, int shadeColor) {
        lineView(lineColor, shadeColor);
    }

}
