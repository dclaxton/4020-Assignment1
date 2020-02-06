package csci.apsu.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class Sos extends AppCompatActivity implements View.OnClickListener {

    private int numMoves;

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
                numMoves++;
            }
        }
        if(numMoves > 8 /*check for wins*/) {
            SetGridNotClickable();
            showResults("Player 1");
        } else if(numMoves == id.length) {
            SetGridNotClickable();
            showResults("Player 2");
        }
        SwitchTurn();
    }

    public void SwitchTurn() {
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
    }

    public void showResults(String w) {
        TextView textView;
        Button button;
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
}






