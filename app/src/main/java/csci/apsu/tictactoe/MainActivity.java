/*
    Authors: Daniel Davis, Dalton Claxton, Peyton White
    Date: 17 February 2020
    Description: An app that implements 3 different variations on the classic game of Tic-Tac-Toe
 */

package csci.apsu.tictactoe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.start_wild_btn).setOnClickListener(this);
        findViewById(R.id.start_sos_btn).setOnClickListener(this);
        findViewById(R.id.start_numerical_btn).setOnClickListener(this);
    }

    // Loads the instructions for the clicked game button
    @Override
    public void onClick(View view) {
        // Load instructions for wild tic-tac-toe
        if (view.getId() == R.id.start_wild_btn) {
            intent = new Intent(getBaseContext(), InstructionsActivity.class);
            intent.putExtra("Wild", R.string.wild_welcome_msg);
        }

        // Load instructions for sos tic-tac-toe
        if (view.getId() == R.id.start_sos_btn) {
            intent = new Intent(getBaseContext(), InstructionsActivity.class);
            intent.putExtra("Sos", R.string.sos_welcome_msg);
    }

        // Load instructions for numerical tic-tac-toe
        if (view.getId() == R.id.start_numerical_btn) {
            intent = new Intent(getBaseContext(), InstructionsActivity.class);
            intent.putExtra("Numerical", R.string.numerical_welcome_msg);
        }

        startActivity(intent);
    }

    // Forces back button to go to home screen
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDiag = new AlertDialog.Builder(MainActivity.this);
        alertDiag.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent home = new Intent(Intent.ACTION_MAIN);
                home.addCategory(Intent.CATEGORY_HOME);
                home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(home);
            }
        });
        alertDiag.setNegativeButton("No", null);
        alertDiag.setMessage("Are you sure you want to exit?");
        alertDiag.setTitle("Tic-Tac-Toe");
        alertDiag.show();
    }
}
