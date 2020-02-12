package csci.apsu.tictactoe;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class PlaySosActivity extends AppCompatActivity implements View.OnClickListener {

    private int numMoves;
    private int totalNumMoves = 0;
    private int player1Wins = 0;
    private int player2Wins = 0;

    private boolean topHorizontal = false;
    private boolean middleHorizontal = false;
    private boolean bottomHorizontal = false;

    private boolean leftVertical = false;
    private boolean middleVertical = false;
    private boolean rightVertical = false;

    private boolean diagonalFromLeft = false;
    private boolean diagonalFromRight = false;





    /* Array with Each Game piece's ID */
    private int[] id = { R.id.imageView1, R.id.imageView2, R.id.imageView3,
            R.id.imageView4, R.id.imageView5, R.id.imageView6,
            R.id.imageView7, R.id.imageView8, R.id.imageView9 };


    /*HashMap for game Pieces */
    private HashMap<Integer, Integer> pieces = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_play_sos);

 /*
            -setup our grid, set all slots to empty pieces to start off with
            -this also sets the eventhandler for the imageviews (pieces) in the grid
         */
        for(int piece : id)
        {
            findViewById(piece).setOnClickListener(this);
            Log.i("piece",piece + "");
            ((ImageView) findViewById(piece)).setImageResource(R.drawable.empty);
            pieces.put(piece, R.drawable.empty);
        }

        /* setup our switch to let the user select which piece they want to use when it's their turn. */
        Switch pieceSwitch = findViewById(R.id.gamePieceSwitch);
        pieceSwitch.setChecked(false); //piece_s = False, piece_o = True
        pieceSwitch.setTextOn("O");
        pieceSwitch.setTextOff("S");
        pieceSwitch.setShowText(true);

        /*
            - setup event handlers for when the game is over (buttons)
         */
        findViewById(R.id.menuBtn).setOnClickListener(this);
        findViewById(R.id.restartBtn).setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDiag = new AlertDialog.Builder(PlaySosActivity.this);
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

    @Override
    public void onClick(View view) {
        ImageView piecePlayed;
        Switch gamePieceType = findViewById(R.id.gamePieceSwitch);

        if(view.getId() == R.id.restartBtn) {
            restartGame();
        }
        else if (view.getId() == R.id.menuBtn) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        for(int piece : id)
        {
            if(view.getId() == piece)
            {
                piecePlayed = findViewById(piece);
                if(gamePieceType.isChecked()) {
                    piecePlayed.setImageResource(R.drawable.piece_o);
                    pieces.put(view.getId(), R.drawable.piece_o);

                } else {
                    piecePlayed.setImageResource(R.drawable.piece_s);
                    pieces.put(view.getId(), R.drawable.piece_s);
                }
                piecePlayed.setClickable(false);
                totalNumMoves++;
            }
        }
        if(totalNumMoves > 8 /*check for wins*/) {
            SetGridNotClickable();
            showResults();
        } else if(totalNumMoves == id.length) {
            SetGridNotClickable();
            showResults();
        }
        else if(!isASos()) {
            numMoves++;
            SwitchTurn();
        }
    }

    public void SwitchTurn() {
        //need to check for SOS patterns if one is completed then do not switch turns. else switch turns


        String turn = (numMoves % 2 == 0) ? "Player 1" : "Player 2";
        ((TextView) findViewById(R.id.playerTurnText)).setText(turn);
    }

    public void SetGridNotClickable() {
        for(int piece : id)
            findViewById(piece).setClickable(false);
    }

    public void restartGame() {
        /* Hide our end game stuff/buttons */
        findViewById(R.id.GameOverText).setVisibility(View.GONE);
        findViewById(R.id.ResultsTextView).setVisibility(View.GONE);
        findViewById(R.id.menuBtn).setVisibility(View.GONE);
        findViewById(R.id.restartBtn).setVisibility(View.GONE);

        /* empty our grid and hash */
        for(int piece : id)
        {
            findViewById(piece).setOnClickListener(this);
            ((ImageView) findViewById(piece)).setImageResource(R.drawable.empty);
            pieces.put(piece, R.drawable.empty);
        }

        /* change it to 0 squares played b/c new game */
        numMoves = 0;
        totalNumMoves = 0;
        leftVertical = false;
        rightVertical = false;
        middleVertical = false;

        topHorizontal = false;
        middleHorizontal = false;
        bottomHorizontal = false;


        diagonalFromLeft = false;
        diagonalFromRight = false;
        player2Wins = 0;
        player1Wins = 0;



    }

    public void showResults() {
        TextView textView;
        Button button;
        String w;
        if(player1Wins > player2Wins)
        {
            w = "Player 1";
        }
        else if(player1Wins < player2Wins)
        {
            w = "Player 2";
        }
        else
        {
            w = "Tie game";
        }
        /*
            - Shows the "GAME OVER" Text
         */
        textView = findViewById(R.id.GameOverText);
        textView.setText("GAME OVER");
        textView.setVisibility(View.VISIBLE);
        textView.bringToFront();

        /*
            - Sets the results textView to show who won
         */
        textView = findViewById(R.id.ResultsTextView);
        textView.setText("WINNER: " + w);
        textView.setVisibility(View.VISIBLE);
        textView.bringToFront();

        /*
            - Sets the buttons for the main menu and restart when the game ends
         */
        button = findViewById(R.id.menuBtn);
        button.setVisibility(View.VISIBLE);
        button.bringToFront();

        button = findViewById(R.id.restartBtn);
        button.setVisibility(View.VISIBLE);
        button.bringToFront();
    }

    public boolean isASos()
    {

        //check left vertical
        if(pieces.get(id[0]) == R.drawable.piece_s &&
                pieces.get(id[3]) == R.drawable.piece_o &&
                pieces.get(id[6]) == R.drawable.piece_s)
        {
            if(leftVertical == false)
            {

                if( ((TextView) findViewById(R.id.playerTurnText)).getText().toString() == "Player 1")
                {
                    player1Wins++;

                }
                else
                {
                    player2Wins++;
                }

                leftVertical = true;
                return true;
            }

        }

        //check middle vertical
        if(pieces.get(id[1]) == R.drawable.piece_s &&
                pieces.get(id[4]) == R.drawable.piece_o &&
                pieces.get(id[7]) == R.drawable.piece_s)
        {
            if(middleVertical == false)
            {

                if( ((TextView) findViewById(R.id.playerTurnText)).getText().toString() == "Player 1")
                {
                    player1Wins++;

                }
                else
                {
                    player2Wins++;
                }

                middleVertical = true;
                return true;
            }

        }

        //check right vertical
        if(pieces.get(id[2]) == R.drawable.piece_s &&
                pieces.get(id[5]) == R.drawable.piece_o &&
                pieces.get(id[8]) == R.drawable.piece_s)
        {
            if(rightVertical == false)
            {

                if( ((TextView) findViewById(R.id.playerTurnText)).getText().toString() == "Player 1")
                {
                    player1Wins++;

                }
                else
                {
                    player2Wins++;
                }

                rightVertical = true;
                return true;
            }

        }

        //check top horizontal
        if(pieces.get(id[0]) == R.drawable.piece_s &&
                pieces.get(id[1]) == R.drawable.piece_o &&
                pieces.get(id[2]) == R.drawable.piece_s)
        {
            if(topHorizontal == false)
            {

                if( ((TextView) findViewById(R.id.playerTurnText)).getText().toString() == "Player 1")
                {
                    player1Wins++;

                }
                else
                {
                    player2Wins++;
                }

                topHorizontal = true;
                return true;
            }

        }

        //check middle horizontal
        if(pieces.get(id[3]) == R.drawable.piece_s &&
                pieces.get(id[4]) == R.drawable.piece_o &&
                pieces.get(id[5]) == R.drawable.piece_s)
        {
            if(middleHorizontal == false)
            {

                if( ((TextView) findViewById(R.id.playerTurnText)).getText().toString() == "Player 1")
                {
                    player1Wins++;

                }
                else
                {
                    player2Wins++;
                }

                middleHorizontal = true;
                return true;
            }

        }

        //check bottom horizontal
        if(pieces.get(id[6]) == R.drawable.piece_s &&
                pieces.get(id[7]) == R.drawable.piece_o &&
                pieces.get(id[8]) == R.drawable.piece_s)
        {
            if(bottomHorizontal == false)
            {

                if( ((TextView) findViewById(R.id.playerTurnText)).getText().toString() == "Player 1")
                {
                    player1Wins++;

                }
                else
                {
                    player2Wins++;
                }

                bottomHorizontal = true;
                return true;
            }

        }

        //check diagoanl from left
        if(pieces.get(id[0]) == R.drawable.piece_s &&
                pieces.get(id[4]) == R.drawable.piece_o &&
                pieces.get(id[8]) == R.drawable.piece_s)
        {
            if(diagonalFromLeft == false)
            {

                if( ((TextView) findViewById(R.id.playerTurnText)).getText().toString() == "Player 1")
                {
                    player1Wins++;

                }
                else
                {
                    player2Wins++;
                }

                diagonalFromLeft = true;
                return true;
            }

        }

        //check diagoanl from right
        if(pieces.get(id[2]) == R.drawable.piece_s &&
                pieces.get(id[4]) == R.drawable.piece_o &&
                pieces.get(id[6]) == R.drawable.piece_s)
        {
            if(diagonalFromRight == false)
            {

                if( ((TextView) findViewById(R.id.playerTurnText)).getText().toString() == "Player 1")
                {
                    player1Wins++;

                }
                else
                {
                    player2Wins++;
                }

                diagonalFromRight = true;
                return true;
            }

        }







        return false;
    }

}







