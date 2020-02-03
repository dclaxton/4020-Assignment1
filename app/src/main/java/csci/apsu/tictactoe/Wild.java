package csci.apsu.tictactoe;

/*
    -Wild tic-tac-toe
        -In wild tic-tac-toe, players can choose to place either X or O on each move.
         It can be played as a normal game where the player who makes three in a row wins or a
         misere game where they would lose.[ This game is also called your-choice
         tic-tac-toe or Devil's tic-tac-toe.
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class Wild extends AppCompatActivity implements View.OnClickListener {
    private boolean turn;
    private boolean selectedPiece; /* 0 is for "o", 1 is for "x" */
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
        setContentView(R.layout.activity_play_wild);

        for(int piece : id)
        {
            findViewById(piece).setOnClickListener(this);
            ((ImageView) findViewById(piece)).setImageResource(R.drawable.empty);
            pieces.put(piece, R.drawable.empty);
        }
    }

    @Override
    public void onClick(View view) {
        ImageView piecePlayed;
        Switch gamePieceType = findViewById(R.id.gamePieceSwitch);

        if(view.getId() == R.id.restartBtn) {
            //restartGame();
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
                    piecePlayed.setImageResource(R.drawable.piece_x);
                    pieces.put(view.getId(), R.drawable.piece_x);
                }
                piecePlayed.setClickable(false);
                numMoves++;
            }
        }
        if(numMoves > 4 /*check for wins*/) {
            //SetGridNotClickable();
            //showResults("Player 1");
        } else if(numMoves == id.length) {
            //SetGridNotClickable();
            //showResults("Player 2");
        }
        SwitchTurn();
    }

    public void SwitchTurn() {
        String turn = (numMoves % 2 == 0) ? "Player 1" : "Player 2";
        ((TextView) findViewById(R.id.playerTurnText)).setText(turn);
    }
}
