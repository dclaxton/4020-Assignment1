/*
    Authors: Daniel Davis, Dalton Claxton, Peyton White
    Date: 17 February 2020
    Description: An app that implements 3 different variations on the classic game of Tic-Tac-Toe
 */

package csci.apsu.tictactoe;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedHashMap;

public class PlayNumericalActivity extends AppCompatActivity implements View.OnClickListener {

    // Constants for the board
    private static final int ROWS = 3, COLS = 3;
    private static int[][] board = new int[ROWS][COLS];
    private static LinkedHashMap moves = new LinkedHashMap();
    private static int turns_taken = 0;
    private static final int max_moves = 9;

    // Objects used frequently
    private View child;
    private TextView turn_textView;
    private RadioGroup number_radioGroup;
    GameState saveGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_numerical);

        initializeBoard();
    }

    // Forces the back button to go to the Instruction screen
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDiag = new AlertDialog.Builder(PlayNumericalActivity.this);
        alertDiag.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(getApplicationContext(), InstructionsActivity.class));
            }
        });
        alertDiag.setNegativeButton("No", null);
        alertDiag.setMessage("Are you sure you want to exit?");
        alertDiag.setTitle("Tic-Tac-Toe");
        alertDiag.show();
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.restartBtn) {
            saveGame.restartGame();
            startActivity(new Intent(getApplicationContext(), PlayNumericalActivity.class));
        }

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (view.getId() == board[i][j]) {
                    ImageView position_imageView = findViewById(board[i][j]);

                    for (Object key : moves.keySet() ) {
                        RadioButton number_radioButton = findViewById((int) key);

                        // Set the board square to the value of the selected radio button
                        if (number_radioButton.isChecked() && position_imageView.getTag() == null) {
                            position_imageView.setBackgroundResource(getResources().getIdentifier(
                                    "number_" + moves.get(key), "drawable", getPackageName()));
                            position_imageView.setTag(getResources().getIdentifier(
                                    "number_" + moves.get(key), "drawable", getPackageName()));

                            // Save the current position and number
                            saveGame.saveGameState(i + j + (i * 2), Character.forDigit((int) moves.get(key), 10));

                            // Replace the resource ID in the board array with the number it represents
                            board[i][j] = (int) moves.get(key);
                        }
                    }

                    if (position_imageView.getTag() != null)
                        changeTurn();
                }
            }
        }
    }

    // Set up the listeners and initial appearance of the board
    private void initializeBoard() {
        ViewGroup layout = findViewById(R.id.board_linearLayout);
        number_radioGroup = findViewById(R.id.number_choices_rg);
        turn_textView = findViewById(R.id.player_turn_textView);
        saveGame = new GameState(getApplicationContext());

        // Set up the board for Player 1
        changeMoves(R.string.player1_turn);
        findViewById(R.id.restartBtn).setOnClickListener(this);

        // Set up listeners (or a saved game if one exists)
        for (int i = 0; i < layout.getChildCount(); i++) {
            child = layout.getChildAt(i);
            ViewGroup rowLayout = findViewById(child.getId());

            for (int j = 0; j < rowLayout.getChildCount(); j++) {
                child = rowLayout.getChildAt(j);
                if (child instanceof ImageView) {
                    child.setOnClickListener(this);
                    checkForSavedGame(i, j);
                    board[i][j] = child.getId();
                }
            }
        }
    }

    // Checks for a Game State to load back into the board
    private void checkForSavedGame(int xAxis, int yAxis) {
        if (saveGame.hasCurrentSaveGame()) {
            char[] save = saveGame.getGameState().toCharArray();

            if (save[xAxis + yAxis + (xAxis * 2)] > '0') {
                child.setBackgroundResource(getResources().getIdentifier(
                        "number_" + save[xAxis + yAxis + (xAxis * 2)], "drawable", getPackageName()));
                child.setClickable(false);
            }
        } else {
            turns_taken = 0;
        }
    }

    // Indicates whose turn it is and checks for a winner
    private void changeTurn() {
        turns_taken++;

        if (isWinner()) {
            Intent intent = new Intent(getBaseContext(), GameEndActivity.class);

            if (turn_textView.getText().equals(getString(R.string.player1_turn))) {
                intent.putExtra("Player 1", "Numerical");
            } else {
                intent.putExtra("Player 2", "Numerical");
            }

            saveGame.restartGame();
            startActivity(intent);
        } else if (turns_taken >= max_moves) {
            saveGame.restartGame();
            startActivity(new Intent(getBaseContext(), GameEndActivity.class));
        } else if (turn_textView.getText().equals(getString(R.string.player1_turn))) {
            turn_textView.setText(R.string.player2_turn);
            changeMoves(R.string.player2_turn);
        } else {
            turn_textView.setText(R.string.player1_turn);
            changeMoves(R.string.player1_turn);
        }
    }

    // Changes the possible moves depending on the current player
    private void changeMoves(int player) {
        moves.clear();

        // Player 1
        if (player == R.string.player1_turn) {
            findViewById(R.id.fifth_rb).setVisibility(View.VISIBLE);

            for (int i = 0; i < number_radioGroup.getChildCount(); i++) {
                child = number_radioGroup.getChildAt(i);
                moves.put(child.getId(), (i * 2) + 1);
                ((RadioButton) child).setText("");
                ((RadioButton) child).append("" + ((i * 2) + 1));
            }
        }

        // Player 2
        else {
            findViewById(R.id.fifth_rb).setVisibility(View.GONE);

            for (int i = 0; i < number_radioGroup.getChildCount() - 1; i++) {
                child = number_radioGroup.getChildAt(i);
                moves.put(child.getId(), ((i * 2) + 2));
                ((RadioButton) child).setText("");
                ((RadioButton) child).append("" + ((i * 2) + 2));
            }
        }
    }

    // Algorithm that scans for a sum of 15 anywhere on the board
    public boolean isWinner() {
        // Check horizontal
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length - 2; col++) {
                if (board[row][col] + board[row][col + 1] +
                        board[row][col + 2] == 15) {
                    return true;
                }
            }
        }

        // Check vertical
        for (int row = 0; row < board.length - 2; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] + board[row + 1][col] +
                        board[row + 2][col] == 15) {
                    return true;
                }
            }
        }

        // Check diagonal
        for (int row = 0; row < board.length - 2; row++) {
            for (int col = 0; col < board[row].length - 2; col++) {
                if (board[row][col] + board[row + 1][col + 1] +
                        board[row + 2][col + 2] == 15) {
                    return true;
                }
            }
        }

        // Check reverse-diagonal
        for (int row = 0; row < board.length - 2; row++) {
            for (int col = 2; col < board[row].length; col++) {
                if (board[row][col] + board[row + 1][col - 1] +
                        board[row + 2][col - 2] == 15) {
                    return true;
                }
            }
        }

        return false;
    }
}
