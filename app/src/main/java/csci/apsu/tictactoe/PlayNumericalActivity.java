package csci.apsu.tictactoe;

import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class PlayNumericalActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int ROWS = 6, COLS = 6;
    public static int[][] board = new int[ROWS][COLS];
    public static int moves = 0;

    @Override
    public void onClick(View view) {

    }
}
