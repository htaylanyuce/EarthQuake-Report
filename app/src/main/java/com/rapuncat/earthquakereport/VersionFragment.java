package com.rapuncat.earthquakereport;

/**
 * Created by taylan on 24.11.2017.
 */

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class VersionFragment extends Fragment {

    public VersionFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_version, container,
                false);
        // Get position of argument
        String name = getArguments().getString("name");

        TextView textView = (TextView) rootView.findViewById(R.id.textView);
        textView.setText("Android version " + name + " is selected");
        getActivity().setTitle(name);
        return rootView;
    }
}
