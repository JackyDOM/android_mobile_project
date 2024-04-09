package edu.rupp.firstite;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Find the button in the layout
        Button btnGeneralBook = rootView.findViewById(R.id.btnGenderalBook);
        Button btnComdyBook = rootView.findViewById(R.id.btnComdyBook);
        Button btnComicBook = rootView.findViewById(R.id.btnComicBook);

        // Set OnClickListener to the button Genenral-Book
        btnGeneralBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the MainActivity
                Intent intent = new Intent(getActivity(), BookGridActivity.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener to the button Comdy-Book
        btnComdyBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentComdyBook = new Intent(getActivity(), BookGridActivity.class);
                startActivity(intentComdyBook);
            }
        });

        // Set OnClickListener to the button Comic-Book
        btnComicBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentComicBook = new Intent(getActivity(), BookGridActivity.class);
                startActivity(intentComicBook);
            }
        });

        return rootView;
    }
}
