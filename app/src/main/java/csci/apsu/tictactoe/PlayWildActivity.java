/* Author: Dalton Claxton
 *  17 February 2020 */

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

public class PlayWildActivity extends AppCompatActivity implements View.OnClickListener {
    private int numMoves;
    GameState savegame;

    /* Array with Each Game piece's ID */
    private int[] id = {R.id.top_left_imageView, R.id.top_center_imageView, R.id.top_right_imageView,
            R.id.middle_left_imageView, R.id.middle_center_imageView, R.id.middle_right_imageView,
            R.id.bottom_left_imageView, R.id.bottom_center_imageView, R.id.bottom_right_imageView};

    /*
    - My plan is to convert the array to a 2D array and use matrix methods (nested for loops) for finding a
        horizontal, vertical, or diagonal match, thus determining a winner
    */
    private int[][] matrix =
            {{R.id.top_left_imageView, R.id.top_center_imageView, R.id.top_right_imageView},
                    {R.id.middle_left_imageView, R.id.middle_center_imageView, R.id.middle_right_imageView},
                    {R.id.bottom_left_imageView, R.id.bottom_center_imageView, R.id.bottom_right_imageView}};

    /*HashMap for game Pieces */
    private HashMap<Integer, Integer> pieces = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_wild);


        /*GameState object to save the state of our current game*/
        savegame = new GameState(getApplicationContext());
        /*
            -setup our grid, this will get the state of the game and setup the grid and who's turn
            it is from the last saved state.
         */
        if (savegame.hasCurrentSaveGame()) {
            int index = 0;
            char[] save = savegame.getGameState().toCharArray();

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
                    numMoves++;

                } else if (save[index] == '2') {
                    ((ImageView) findViewById(piece)).setImageResource(R.drawable.piece_x);
                    pieces.put(piece, R.drawable.piece_x);
                    findViewById(piece).setClickable(false);
                    numMoves++;
                }
                index++;
                SwitchTurn();
            }
        } else {
            for (int piece : id) {
                findViewById(piece).setOnClickListener(this);
                ((ImageView) findViewById(piece)).setImageResource(R.drawable.empty);
                pieces.put(piece, R.drawable.empty);
            }
        }

        /* setup our switch to let the user select which piece they want to use when it's their turn. */
        Switch pieceSwitch = findViewById(R.id.gamePieceSwitch);
        pieceSwitch.setChecked(false); //piece_x = False, piece_o = True
        pieceSwitch.setTextOn("O");
        pieceSwitch.setTextOff("X");
        pieceSwitch.setShowText(true);

        /*
            - setup event handlers for when the game is over (buttons)
         */
        findViewById(R.id.menuBtn).setOnClickListener(this);
        findViewById(R.id.restartBtn).setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDiag = new AlertDialog.Builder(PlayWildActivity.this);
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

        if (view.getId() == R.id.restartBtn) {
            restartGame();
        } else if (view.getId() == R.id.menuBtn) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        /*
            - Handle each turn, and save it when it is played
         */
        int index = 0;
        for (int piece : id) {
            if (view.getId() == piece) {
                piecePlayed = findViewById(piece);
                if (gamePieceType.isChecked()) {
                    piecePlayed.setImageResource(R.drawable.piece_o);
                    pieces.put(view.getId(), R.drawable.piece_o);
                    savegame.saveGameState(index, '1');
                } else {
                    piecePlayed.setImageResource(R.drawable.piece_x);
                    pieces.put(view.getId(), R.drawable.piece_x);
                    savegame.saveGameState(index, '2');
                }
                piecePlayed.setClickable(false);
                numMoves++;
            }
            index++;
        }
        if (CheckForDiagonalWin() || CheckForVerticleOrHorizontalWin()) {
            SetGridNotClickable();
            showResults("Player 1");
            savegame.restartGame();
        } else if (numMoves >= id.length) {
            SetGridNotClickable();
            showResults("Player 2");
            savegame.restartGame();
        }
        SwitchTurn();
    }

    public void SwitchTurn() {
        String turn = (numMoves % 2 == 0) ? "Player 1" : "Player 2";
        ((TextView) findViewById(R.id.player_turn_textView)).setText(turn);
    }

    /*
        - Sets the image resource not clickable
     */
    public void SetGridNotClickable() {
        for (int piece : id)
            findViewById(piece).setClickable(false);
    }

    /*
        Hides the end game texts, resets the savegame state and resets the grid
     */
    public void restartGame() {
        /* Hide our end game stuff/buttons */
        findViewById(R.id.GameOverText).setVisibility(View.GONE);
        findViewById(R.id.ResultsTextView).setVisibility(View.GONE);
        findViewById(R.id.menuBtn).setVisibility(View.GONE);
        findViewById(R.id.restartBtn).setVisibility(View.GONE);
        savegame.restartGame();

        /* empty our grid and hash */
        for (int piece : id) {
            findViewById(piece).setOnClickListener(this);
            ((ImageView) findViewById(piece)).setImageResource(R.drawable.empty);
            pieces.put(piece, R.drawable.empty);
        }

        /* change it to 0 squares played b/c new game */
        numMoves = 0;
    }

    /*
        - Bring up the end game text, and show buttons to go back to menu or restart game
     */
    public void showResults(String w) {
        Intent intent = new Intent(getBaseContext(), GameEndActivity.class);

        if (w.equals("Player 1"))
        {
            intent.putExtra("Player 1", "Wild");
        }
        else if (w.equals("Player 2"))
        {
            intent.putExtra("Player 2", "Wild");
        }

        startActivity(intent);
    }


    public boolean CheckForVerticleOrHorizontalWin() {
        int piecesInARow = 0, pieceBeforeCurrent = 0;
        //Check for Horizontal win
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix.length; j++) {
                ImageView piece = findViewById(matrix[i][j]);
                if(pieces.get(piece.getId()) != R.drawable.empty && pieceBeforeCurrent == pieces.get(piece.getId())) {
                    piecesInARow++;
                } else {
                    piecesInARow = 0;
                    pieceBeforeCurrent = pieces.get(piece.getId());
                }
                if(piecesInARow == 2) {
                    return true;
                }
            }
        }
        //Check for Vertical Win
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix.length; j++) {
                ImageView piece = findViewById(matrix[j][i]);
                if(pieces.get(piece.getId()) != R.drawable.empty && pieceBeforeCurrent == pieces.get(piece.getId())) {
                    piecesInARow++;
                } else {
                    piecesInARow = 0;
                    pieceBeforeCurrent = pieces.get(piece.getId());
                }
                if(piecesInARow == 2) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean CheckForDiagonalWin() {
        int piecesInARow = 0, pieceBeforeCurrent = 0;
        //Check TopLeft/BottomRight Diagonal
        for(int i = 0; i < matrix.length; i++) {
            ImageView piece = findViewById(matrix[i][i]);
            if(pieces.get(piece.getId()) != R.drawable.empty && pieceBeforeCurrent == pieces.get(piece.getId())) {
                piecesInARow++;
            } else {
                piecesInARow = 0;
                pieceBeforeCurrent = pieces.get(piece.getId());
            }
            if(piecesInARow == 2) {
                return true;
            }
        }

        //Check TopRight/BottomLeft Diagonal
        for(int i = 2; i > -1; i--) {
            ImageView piece = findViewById(matrix[i][2 - i]);
            if(pieces.get(piece.getId()) != R.drawable.empty && pieceBeforeCurrent == pieces.get(piece.getId())) {
                piecesInARow++;
            } else {
                piecesInARow = 0;
                pieceBeforeCurrent = pieces.get(piece.getId());
            }
            if(piecesInARow == 2) {
                return true;
            }
        }
        return false;
    }
}