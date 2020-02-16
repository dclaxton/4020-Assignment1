/* Author: Peyton White
 *  17 February 2020 */

package csci.apsu.tictactoe;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class PlaySosActivity extends AppCompatActivity implements View.OnClickListener {

    private int numMoves =0;
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

    GameState savegame;




    /* Array with Each Game piece's ID */
    private int[] id = { R.id.top_left_imageView, R.id.top_center_imageView, R.id.top_right_imageView,
            R.id.middle_left_imageView, R.id.middle_center_imageView, R.id.middle_right_imageView,
            R.id.bottom_left_imageView, R.id.bottom_center_imageView, R.id.bottom_right_imageView };


    /*HashMap for game Pieces */
    private HashMap<Integer, Integer> pieces = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_play_sos);
        savegame = new GameState(getApplicationContext());

 /*
            -setup our grid, set all slots to empty pieces to start off with
            -this also sets the eventhandler for the imageviews (pieces) in the grid
         */

        if (savegame.hasCurrentSaveGame()) {
            int index = 0;
            totalNumMoves = 0;
            numMoves = 0;
            player1Wins = 0;
            player2Wins = 0;
            char[] save = savegame.getGameState().toCharArray();

            //sets each piece to empty because SOS needs to be checked.
            for (int piece : id) {
                //findViewById(piece).setOnClickListener(this);
                Log.i("piece", piece + "");
                ((ImageView) findViewById(piece)).setImageResource(R.drawable.empty);
                pieces.put(piece, R.drawable.empty);
            }

            for (int piece : id) {
                Log.i("LINE2", "IS TRUE");
                findViewById(piece).setOnClickListener(this);
                if (save[index] == '0') {
                    ((ImageView) findViewById(piece)).setImageResource(R.drawable.empty);
                    pieces.put(piece, R.drawable.empty);
                } else if (save[index] == '1') {
                    ((ImageView) findViewById(piece)).setImageResource(R.drawable.piece_o);
                    pieces.put(piece, R.drawable.piece_o);
                    findViewById(piece).setClickable(false);
                    totalNumMoves++;
                    if(!isASos())
                    {
                        numMoves++;

                    }


                } else if (save[index] == '2') {
                    ((ImageView) findViewById(piece)).setImageResource(R.drawable.piece_s);
                    pieces.put(piece, R.drawable.piece_s);
                    findViewById(piece).setClickable(false);
                    totalNumMoves++;
                    if(!isASos())
                    {
                        numMoves++;

                    }
                }

                index++;
            }
            SwitchTurn();
            Log.i("num of moves file", numMoves + "");

        } else {
            for (int piece : id) {
                findViewById(piece).setOnClickListener(this);
                Log.i("piece", piece + "");
                ((ImageView) findViewById(piece)).setImageResource(R.drawable.empty);
                pieces.put(piece, R.drawable.empty);
            }
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

        int index = 0;
        for(int piece : id)
        {
            if(view.getId() == piece)
            {
                piecePlayed = findViewById(piece);
                if(gamePieceType.isChecked()) {
                    piecePlayed.setImageResource(R.drawable.piece_o);
                    pieces.put(view.getId(), R.drawable.piece_o);
                    savegame.saveGameState(index, '1');

                } else {
                    piecePlayed.setImageResource(R.drawable.piece_s);
                    pieces.put(view.getId(), R.drawable.piece_s);
                    savegame.saveGameState(index, '2');

                }
                piecePlayed.setClickable(false);
                totalNumMoves++;
            }
            index++;
        }
        if(totalNumMoves == 9 /*check for wins*/) {
            isASos();
            SwitchTurn();
            SetGridNotClickable();
            showResults();
            savegame.restartGame();
        } else if(totalNumMoves == id.length) {
            SetGridNotClickable();
            showResults();
            savegame.restartGame();
        }
        else if(!isASos()) {
            numMoves++;
            SwitchTurn();
        }
    }

    public void SwitchTurn() {
        //need to check for SOS patterns if one is completed then do not switch turns. else switch turns


        String turn = (numMoves % 2 == 0) ? "Player 1" : "Player 2";
        ((TextView) findViewById(R.id.player_turn_textView)).setText(turn);
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
        savegame.restartGame();

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
        Intent intent = new Intent(getBaseContext(), GameEndActivity.class);

        if (player1Wins > player2Wins)
        {
            intent.putExtra("Player 1", "Sos");
        }
        else if (player1Wins < player2Wins)
        {
            intent.putExtra("Player 2", "Sos");
        }

        startActivity(intent);
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

                if( ((TextView) findViewById(R.id.player_turn_textView)).getText().toString() == "Player 1")
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

                if( ((TextView) findViewById(R.id.player_turn_textView)).getText().toString() == "Player 1")
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

                if( ((TextView) findViewById(R.id.player_turn_textView)).getText().toString() == "Player 1")
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

                if( ((TextView) findViewById(R.id.player_turn_textView)).getText().toString() == "Player 1")
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

                if( ((TextView) findViewById(R.id.player_turn_textView)).getText().toString() == "Player 1")
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

                if( ((TextView) findViewById(R.id.player_turn_textView)).getText().toString() == "Player 1")
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

                if( ((TextView) findViewById(R.id.player_turn_textView)).getText().toString() == "Player 1")
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

                if( ((TextView) findViewById(R.id.player_turn_textView)).getText().toString() == "Player 1")
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







