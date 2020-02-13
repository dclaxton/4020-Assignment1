package csci.apsu.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class InstructionsActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numerical_directions);

        findViewById(R.id.play_numerical_btn).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        /* Play Numerical Tic-Tac-Toe */
        if(view.getId() == R.id.play_numerical_btn)
            startActivity(new Intent(getApplicationContext(), PlayNumericalActivity.class));
    }
}
