/*
    Authors: Daniel Davis, Dalton Claxton, Peyton White
    Date: 17 February 2020
    Description: An app that implements 3 different variations on the classic game of Tic-Tac-Toe
 */

package csci.apsu.tictactoe;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/*
    - This class is used for keeping track of a game instance
    - it will write to a file each turn and edit a value to what's in the
        respective position in th grid
      - For example, if "O" is there, put a 1. if "x" is there, put a 2, if it is empty put 0.
 */
public class GameState {
    private FileOutputStream fos;
    private InputStream inputStream;
    private String line;
    private Context c;
    private BufferedReader r;

    public GameState(Context context) {
        c = context;
        //Create a new output file if it doesn't already exist
        createNewGame(context);
    }

    /*
        - Index is the position in the grid, value is how we'll distinguish which piece needs to be
            saved there.
     */
    public void saveGameState(int index, char value) {
        line = getGameState();
        StringBuilder newLine = new StringBuilder(line);
        newLine.setCharAt(index, value);
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(c.openFileOutput("gamestate.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(newLine.toString());
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.i("GameState", "Failed to save game state");
            e.printStackTrace();
        }
    }

    /*
        - this will return a string at the index given in the file
            the file stores the values on the same line separated by a space
     */
    public String getGameState() {
        try {
            r = new BufferedReader(new InputStreamReader(c.openFileInput("gamestate.txt")));
            line = r.readLine();
            r.close();
            return line;
        } catch (IOException e) {
            Log.i("GameState", "Failed to read current game state");
            e.printStackTrace();
        }
        return null;
    }

    /*
        - This will check to see if there is a line in the file, and if it contains all 0's, if it does
        return false d/t a game is not saved.
     */
    public boolean hasCurrentSaveGame() {
        try {
            r = new BufferedReader(new InputStreamReader(c.openFileInput("gamestate.txt")));
            line = r.readLine(); //Why is this reading null
            //Log.i("LINE", "" + line);
            r.close();
            if (line == null)
                return false;
            if(line.contains("1") || line.contains("2") || line.contains("3") || line.contains("4")
                || line.contains("5") || line.contains("6") || line.contains("7") || line.contains("8")
                || line.contains("9"))
                return true;
        } catch (IOException e) {
            Log.e("getLetterAtIndex: ", "Failed to read file");
            e.printStackTrace();
        }
        return false;
    }

    /*
        - this is *supposed* to check to see if the file exists, if not, create it.
        - if not, open the input stream to check and see if a current game has been saved, if not
            make a new one.
     */
    private void createNewGame(Context c) {
        try {
            fos = c.openFileOutput("gamestate.txt", Context.MODE_PRIVATE | Context.MODE_APPEND);
            if (!this.hasCurrentSaveGame()) {
                line = "0";
                for (int i = 0; i < 8; i++) {
                    line += "0";
                }
                //Write the initial values to the file
                fos.write(line.getBytes());
                fos.close();
            } else {
                inputStream = c.openFileInput("gamestate.txt");
                fos = c.openFileOutput("gamestate.txt", Context.MODE_PRIVATE | Context.MODE_APPEND);
            }
        } catch (IOException e) {
            Log.i("Write", "WRITING EXCEPTION");
            e.printStackTrace();
        }
    }

    /*
    Sets the gamestate to 0.
     */
    public void restartGame() {
        try {
            fos = c.openFileOutput("gamestate.txt", Context.MODE_PRIVATE);
            line = "0";
            for (int i = 0; i < 8; i++) {
                line += "0";
            }
            //Write the initial values to the file
            fos.write(line.getBytes());
            fos.close();
        } catch (IOException e) {
            Log.i("Write", "WRITING EXCEPTION");
            e.printStackTrace();
        }
    }


}