/**
 * *****************************************************************************
 *
 * @file Sense Hat/TableAdapter.java
 * @author Milosz Plutowski
 * @version V1.0
 * @date 15-06-2021
 * @brief Sense Hat: Adapter to building elements of dynamic list
 * *****************************************************************************
 */

package com.example.senseHat.ViewModel;

import android.content.Context;
//import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.senseHat.Model.MeasurementModel;
import com.example.senseHat.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder> {

    private Context context;
    private List<MeasurementModel> measurementList;
    private MeasurementModel measurementModel;

    public TableAdapter(Context context, List<MeasurementModel> measurementList) {

        this.measurementList = measurementList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_table_element, null);
        return new ViewHolder(view);
    }

    protected String numberOfMeasurement;

    @Override
    public void onBindViewHolder(@NonNull TableAdapter.ViewHolder holder, int position) {
        measurementModel = measurementList.get(position);
        numberOfMeasurement = String.valueOf(position+1) + ".";
        holder.numberTextView.setText(numberOfMeasurement);
        holder.nameTextView.setText(measurementModel.mName);
        holder.valueTextView.setText(String.valueOf(measurementModel.mValue));
        holder.unitTextView.setText(measurementModel.mUnit);
        holder.sensorTextView.setText(measurementModel.mSensor);

        //Date currentTime = Calendar.getInstance().getTime();

        //SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault());
        //String formattedDate = df.format(currentTime);

        holder.dateTextView.setText(measurementModel.mDate);

    }

    @Override
    public int getItemCount() {
        if (measurementList == null) {
            return 0;
        } else {
            return measurementList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView numberTextView;
        protected TextView nameTextView;
        protected TextView valueTextView;
        protected TextView unitTextView;
        protected TextView sensorTextView;
        protected TextView dateTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.numberTextView = (TextView) itemView.findViewById(R.id.singleElementNumberTextView);
            this.nameTextView = (TextView) itemView.findViewById(R.id.singleElementNameTextView);
            this.valueTextView = (TextView) itemView.findViewById(R.id.singleElementValueTextView);
            this.unitTextView = (TextView) itemView.findViewById(R.id.singleElementUnitTextView);
            this.sensorTextView = (TextView) itemView.findViewById(R.id.singleElementSensorTextView);
            this.dateTextView = (TextView) itemView.findViewById(R.id.singleElementDateTextView);
        }
    }
}
