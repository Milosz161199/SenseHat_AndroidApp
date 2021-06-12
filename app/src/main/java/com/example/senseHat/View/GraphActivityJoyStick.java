package com.example.senseHat.View;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.senseHat.Model.XYValue;
import com.example.senseHat.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.ArrayList;

public class GraphActivityJoyStick extends AppCompatActivity {

    private GraphView dataGraphJoy;
    private Button btnRefreshCounters;
    private TextView textViewCounterMiddle;

    private PointsGraphSeries<DataPoint> dataSeriesPoint;
    private final int dataGraphMaxX = 100;
    private final int dataGraphMinX = -100;
    private final int dataGraphMaxY = 100;
    private final int dataGraphMinY = -100;

    private ArrayList<XYValue> xyValueArray;
    public XYValue xy;

    public String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_joystick);
        initView();

        xyValueArray = new ArrayList<>();

        xy = new XYValue(45,50);
        xyValueArray.add(xy);
        text = "("+String.valueOf(xyValueArray.get(0).getX()) +", "+String.valueOf(xyValueArray.get(0).getY())+")";
        InitGraphView();

        btnRefreshCounters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * @param n Number of spaces.
     * @retval String with 'n' spaces.
     */
    private String Space(int n) {
        return new String(new char[n]).replace('\0', ' ');
    }


    public void initView(){
        dataGraphJoy = (GraphView) findViewById(R.id.dataGraphJoy);
        btnRefreshCounters = (Button) findViewById(R.id.btnRefreshCounters);
        textViewCounterMiddle = (TextView) findViewById(R.id.textViewCounterMiddle);
    }

    private void InitGraphView() {

        dataSeriesPoint = new PointsGraphSeries<>(new DataPoint[]{});

        dataGraphJoy.addSeries(dataSeriesPoint);

        dataSeriesPoint.appendData(new DataPoint(xyValueArray.get(0).getX(), xyValueArray.get(0).getY()), true, 1);

        Toast.makeText(this,text, Toast.LENGTH_SHORT).show();
        dataSeriesPoint.setTitle("Joy-stick");
        dataSeriesPoint.setColor(Color.MAGENTA);
        dataSeriesPoint.setShape(PointsGraphSeries.Shape.RECTANGLE);

        dataGraphJoy.getViewport().setScalable(false);
        dataGraphJoy.getViewport().setScrollable(true);

        dataGraphJoy.getViewport().setXAxisBoundsManual(true);
        dataGraphJoy.getViewport().setYAxisBoundsManual(true);
        dataGraphJoy.getViewport().setMinX(dataGraphMinX);
        dataGraphJoy.getViewport().setMaxX(dataGraphMaxX);
        dataGraphJoy.getViewport().setMinY(dataGraphMinY);
        dataGraphJoy.getViewport().setMaxY(dataGraphMaxY);

        // refresh chart
        dataGraphJoy.onDataChanged(true, true);

        dataGraphJoy.getLegendRenderer().setVisible(true);
        dataGraphJoy.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        dataGraphJoy.getLegendRenderer().setTextSize(30);

        dataGraphJoy.getGridLabelRenderer().setTextSize(20);
        dataGraphJoy.getGridLabelRenderer().setVerticalAxisTitle(Space(7) + "Counter_Y [-]");
        dataGraphJoy.getGridLabelRenderer().setHorizontalAxisTitle(Space(11) + "Counter_X [-]");
        dataGraphJoy.getGridLabelRenderer().setNumHorizontalLabels(9);
        dataGraphJoy.getGridLabelRenderer().setNumVerticalLabels(7);
        dataGraphJoy.getGridLabelRenderer().setPadding(35);
    }

}
