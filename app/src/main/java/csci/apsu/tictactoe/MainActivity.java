/*
    Authors: Daniel Davis, Dalton Claxton, Peyton White
    Description: App that has 3 different "Versions" of Tic-Tac-Toe
 */

package csci.apsu.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.play_wild_btn).setOnClickListener(this);

    }


    /* Let's Play WildCard Tic-Tac-Toe */
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.play_wild_btn)
            startActivity(new Intent(getApplicationContext(), Wild.class));
        return;
    }
}
