package csci.apsu.tictactoe;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PlayNumericalActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int ROWS = 6, COLS = 6;
    public static int[][] board = new int[ROWS][COLS];
    public static int moves = 0;

    public TextView turn_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_numerical);
    }

    @Override
    public void onClick(View view) {

    }

    private void initializeBoard() {
        View child;
        ViewGroup layout = findViewById(R.id.board_linearLayout);
        turn_textView = findViewById(R.id.player_turn_textView);
        moves = 0;

        for (int i = 0; i < layout.getChildCount(); i++) {
            child = layout.getChildAt(i);
            if (child instanceof LinearLayout) {
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
            for (int col = 4; col < board[row].length; col++) {
                if (board[row][col] + board[row + 1][col - 1] +
                        board[row + 2][col - 2] == 15) {
                    return true;
                }
            }
        }

        return false;
    }
}
