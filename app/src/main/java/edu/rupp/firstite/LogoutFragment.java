package edu.rupp.firstite;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LogoutFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_logout, container, false);

        TextView textViewUsername = view.findViewById(R.id.textViewUsername);
        String username = getActivity().getIntent().getStringExtra("username");

        if (username != null) {
            textViewUsername.setText("Welcome, " + username);
        } else {
            textViewUsername.setText("Welcome");
        }

        return view;
    }
}