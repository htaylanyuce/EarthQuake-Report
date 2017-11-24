package com.rapuncat.earthquakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.rapuncat.earthquakereport.R.color.magnitude10plus;
import static com.rapuncat.earthquakereport.R.color.magnitude5;

public class EarthQuakeAdapter extends ArrayAdapter<EarthQuake> {


    private static final String LOCATION_SEPARATOR = " of ";


    public EarthQuakeAdapter(Context context, List<EarthQuake> earthquakes) {
        super(context, 0, earthquakes);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.earthquake_list, parent, false);
        }

        EarthQuake currentEarthquake = getItem(position);

        TextView magnitudeView = (TextView) listItemView.findViewById(R.id.magnitude);
        String formattedMagnitude = formatMagnitude(currentEarthquake.getMagnitude());
        magnitudeView.setText(formattedMagnitude);
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getMagnitude(),listItemView);
        magnitudeCircle.setColor(magnitudeColor);

        String originalLocation = currentEarthquake.getLocation();

        String primaryLocation;
        String locationOffset;

        if (originalLocation.contains(LOCATION_SEPARATOR)) {
            String[] parts = originalLocation.split(LOCATION_SEPARATOR);
            locationOffset = parts[0] + LOCATION_SEPARATOR;
            primaryLocation = parts[1];
        } else {
            locationOffset = getContext().getString(R.string.near_the);
            primaryLocation = originalLocation;
        }

        TextView primaryLocationView = (TextView) listItemView.findViewById(R.id.location);
        primaryLocationView.setText(primaryLocation);



        Date dateObject = new Date(currentEarthquake.getTimeInMilliseconds());

        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        String formattedDate = formatDate(dateObject);
        dateView.setText(formattedDate);

        TextView timeView = (TextView) listItemView.findViewById(R.id.time);
        String formattedTime = formatTime(dateObject);
        timeView.setText(formattedTime);

        return listItemView;
    }

    private int getMagnitudeColor(double magnitude,View listItemView) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                listItemView.setBackgroundResource(R.color.magnitude1);
               // magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                listItemView.setBackgroundResource(R.color.magnitude2);
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                listItemView.setBackgroundResource(R.color.magnitude3);
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                listItemView.setBackgroundResource(R.color.magnitude4);
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                listItemView.setBackgroundResource(magnitude5);
                magnitudeColorResourceId = magnitude5;
                break;
            case 6:
                listItemView.setBackgroundResource(R.color.magnitude6);
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                listItemView.setBackgroundResource(R.color.magnitude7);
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                listItemView.setBackgroundResource(R.color.magnitude8);
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                listItemView.setBackgroundResource(R.color.magnitude9);
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                listItemView.setBackgroundResource(magnitude10plus);
                magnitudeColorResourceId = magnitude10plus;
                break;
        }

        return ContextCompat.getColor(getContext(),R.color.colorPrimaryDark);
    }


    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }


    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }


    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }



}