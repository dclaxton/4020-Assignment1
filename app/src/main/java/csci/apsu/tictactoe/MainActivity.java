/*
    Authors: Daniel Davis, Dalton Claxton, Peyton White
    Description: App that has 3 different "Versions" of Tic-Tac-Toe
 */

package csci.apsu.tictactoe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.start_wild_btn).setOnClickListener(this);
        findViewById(R.id.start_sos_btn).setOnClickListener(this);
        findViewById(R.id.start_numerical_btn).setOnClickListener(this);

    }


    /* Let's Play WildCard Tic-Tac-Toe */
    @Override
    public void onClick(View view) {
        /* Play PlayWildActivity Tic-Tac-Toe */
        if(view.getId() == R.id.start_wild_btn)
            startActivity(new Intent(getApplicationContext(), PlayWildActivity.class));

        /* Let's Play SOS Tic-Tac-Toe */
        if(view.getId() == R.id.start_sos_btn)
            startActivity(new Intent(getApplicationContext(), PlaySosActivity.class));

        /* Load the instructions for numerical tic-tac-toe */
        if(view.getId() == R.id.start_numerical_btn)
            startActivity(new Intent(getApplicationContext(), InstructionsActivity.class));

        return;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDiag = new AlertDialog.Builder(MainActivity.this);
        alertDiag.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        alertDiag.setNegativeButton("No", null);
        alertDiag.setMessage("Are you sure you want to exit?");
        alertDiag.setTitle("Tic-Tac-Toe");
        alertDiag.show();
    }
}
