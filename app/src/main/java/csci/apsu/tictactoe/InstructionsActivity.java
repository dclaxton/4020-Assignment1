/*
    Authors: Daniel Davis, Dalton Claxton, Peyton White
    Date: 17 February 2020
    Description: An app that implements 3 different variations on the classic game of Tic-Tac-Toe
 */

package csci.apsu.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class InstructionsActivity extends AppCompatActivity implements View.OnClickListener {

    private static Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directions);

        TextView welcomeTextView = findViewById(R.id.welcome_textView);
        TextView directionsTextView = findViewById(R.id.directions_textView);

        intent = getIntent();

        if (intent.hasExtra("Wild")) {
            welcomeTextView.setText(R.string.wild_welcome_msg);
            directionsTextView.setText(R.string.wild_directions);
        } else if (intent.hasExtra("Sos")){
            welcomeTextView.setText(R.string.sos_welcome_msg);
            directionsTextView.setText(R.string.sos_directions);
        } else {
            welcomeTextView.setText(R.string.numerical_welcome_msg);
            directionsTextView.setText(R.string.numerical_directions);
        }

        findViewById(R.id.playBtn).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.playBtn) {
            if (intent.hasExtra("Wild")) {
                startActivity(new Intent(getApplicationContext(), PlayWildActivity.class));
            } else if (intent.hasExtra("Sos")) {
                startActivity(new Intent(getApplicationContext(), PlaySosActivity.class));
            } else {
                startActivity(new Intent(getApplicationContext(), PlayNumericalActivity.class));
            }
        }
    }
}
