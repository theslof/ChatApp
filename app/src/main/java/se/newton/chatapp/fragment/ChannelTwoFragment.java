package se.newton.chatapp.fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import se.newton.chatapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChannelTwoFragment extends Fragment {


    public ChannelTwoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.channel_two, container, false);
    }

}
