package csci.apsu.tictactoe;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class PlayNumericalActivity extends AppCompatActivity implements View.OnClickListener {

    // Constants for the board
    private static final int ROWS = 3, COLS = 3;
    private static int[][] board = new int[ROWS][COLS];
    private static SparseIntArray moves = new SparseIntArray();
    private static int turns_taken = 0;
    private static final int max_moves = 9;

    // Objects used frequently
    private static Intent intent;
    private View child;
    private TextView turn_textView;
    private RadioGroup number_radioGroup;
    private RadioButton number_radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_numerical);

        initializeBoard();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.restartBtn) {
            intent = new Intent(getApplicationContext(), PlayNumericalActivity.class);
            startActivity(intent);
        }

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (view.getId() == board[i][j]) {
                    ImageView position_imageView = findViewById(board[i][j]);

                    for (int k = 0; k < moves.size(); k++) {
                        number_radioButton = findViewById(moves.keyAt(k));

                        // Set the board square to the value of the selected radio button
                        if (number_radioButton.isChecked() && position_imageView.getTag() == null) {
                            position_imageView.setBackgroundResource(getResources().getIdentifier(
                                    "number_" + moves.valueAt(k), "drawable", getPackageName()));
                            position_imageView.setTag(getResources().getIdentifier(
                                    "number_" + moves.valueAt(k), "drawable", getPackageName()));

                            // Replace the resource ID in the board array with the number it represents
                            board[i][j] = moves.valueAt(k);
                        }
                    }

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
        turns_taken = 0;

        // Set up the board for Player 1
        changeMoves(R.string.player1_turn);

        Button replayButton = findViewById(R.id.replayButton);
        //replayButton.setOnClickListener(this);

        for (int i = 0; i < layout.getChildCount(); i++) {
            child = layout.getChildAt(i);
            ViewGroup rowLayout = findViewById(child.getId());

            for (int j = 0; j < rowLayout.getChildCount(); j++) {
                child = rowLayout.getChildAt(j);
                if (child instanceof ImageView) {
                    child.setOnClickListener(this);
                    board[i][j] = child.getId();
                }
            }
        }
    }

    // Indicates whose turn it is and checks for a winner
    private void changeTurn() {
        turns_taken++;

        if (isWinner()) {
            intent = new Intent(getBaseContext(), GameEndActivity.class);

            if (turn_textView.getText().equals(getString(R.string.player1_turn))) {
                intent.putExtra("Player 1", R.string.player1_wins);
            } else {
                intent.putExtra("Player 2", R.string.player2_wins);
            }

            startActivity(intent);
        } else if (turns_taken >= max_moves) {
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

        if (player == R.string.player1_turn) {
            number_radioButton = findViewById(R.id.fifth_rb);
            number_radioButton.setVisibility(View.VISIBLE);

            for (int i = 0; i < number_radioGroup.getChildCount(); i++) {
                child = number_radioGroup.getChildAt(i);
                ((RadioButton) child).setText("");
                ((RadioButton) child).append("" + ((i * 2) + 1));
                moves.put(child.getId(), (i * 2) + 1);
            }
        } else {
            number_radioButton = findViewById(R.id.fifth_rb);
            number_radioButton.setVisibility(View.GONE);

            for (int i = 0; i < number_radioGroup.getChildCount() - 1; i++) {
                child = number_radioGroup.getChildAt(i);
                ((RadioButton) child).setText("");
                ((RadioButton) child).append("" + ((i * 2) + 2));
                moves.put(child.getId(), ((i * 2) + 2));
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
