package edu.rupp.firstite.author_screen;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import edu.rupp.firstite.Home_screen.HomeFragment;
import edu.rupp.firstite.R;

public class AuthorInformationActivity extends AppCompatActivity {
    private static final int MAX_LINES_COLLAPED = 5;
    private boolean isExpanded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_information);

        // Find the ImageView by id
        ImageView BackToAuthorScreen = findViewById(R.id.BackToAuthorScreen);

        BackToAuthorScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the HomeFragment
                navigateToAuthorFragment();
            }
        });

        String authorImageUrl = getIntent().getStringExtra("author_image_url");
        String authorgender = getIntent().getStringExtra("author_gender");
        String authorName = getIntent().getStringExtra("author_name");
        String authorDecs = getIntent().getStringExtra("author_decs");

        ImageView imageView = findViewById(R.id.authorImageView);
        TextView textView = findViewById(R.id.gender);
        TextView textView1 = findViewById(R.id.name);
        TextView textView2 = findViewById(R.id.idAuthor);
        Picasso.get().load(authorImageUrl).into(imageView);

        TextView showMoreTextView = findViewById(R.id.showMoreTextView);

        // Create a SpannableString for "Name: " with a different color
        SpannableString spannableNameLabel = new SpannableString("Name: ");
        spannableNameLabel.setSpan(new ForegroundColorSpan(Color.GREEN), 0, spannableNameLabel.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView1.setText(spannableNameLabel);
        textView1.append(authorName);

        // Create a SpannableString for "Gender: " with a different color
        SpannableString spannableGenderLabel = new SpannableString("Gender: ");
        spannableGenderLabel.setSpan(new ForegroundColorSpan(Color.GREEN), 0, spannableGenderLabel.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Set the styled text to the TextView
        textView.setText(spannableGenderLabel);
        textView.append(authorgender); // Append the gender text after the styled label

        // Set author's gender with label in the TextView
        // Create a SpannableString for "Name: " with a different color
        SpannableString spannableDecsLabel = new SpannableString("Description: ");
        spannableDecsLabel.setSpan(new ForegroundColorSpan(Color.GREEN), 0, spannableDecsLabel.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView2.setText(spannableDecsLabel);
        textView2.append(authorDecs);
        textView2.setMaxLines(MAX_LINES_COLLAPED);

        // Set click listener for "Show more" button
        showMoreTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    // Collapse the description
                    textView2.setMaxLines(MAX_LINES_COLLAPED);
                    showMoreTextView.setText("Show more");
                } else {
                    // Expand the description
                    textView2.setMaxLines(Integer.MAX_VALUE); // Show all lines
                    showMoreTextView.setText("Show less");
                }
                isExpanded = !isExpanded;
            }
        });
    }

    // Method to navigate to the HomeFragment
    private void navigateToAuthorFragment() {
        // Create a new instance of the AuthorFragment
        AuthorFragment authorFragment = new AuthorFragment();

        // Replace the current fragment with the AuthorFragment
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, authorFragment)
                .addToBackStack(null)
                .commit();

        finish();
    }
}