//Written by Khris Pham
//Last edited May 27 2021

import java.util.Scanner;
import java.util.Arrays;

public class Game2048 {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    int[][] board;
    boolean[][] combined;
    boolean playing;
    boolean win;
    public static void main(String[] args){
        //new board
        Game2048 newGame = new Game2048();
        //generate the original numbers
        newGame.newNums();
        newGame.newNums();
        //game being played
        while(newGame.playing && !newGame.win){
            newGame.print();
            if(newGame.askToMove()){
                newGame.newNums();
            }
            newGame.checkPlaying();
        }
        //final board
        newGame.print();
        //win/lose message
        if(newGame.win){
            System.out.println("You win");
        }else{
            System.out.println("You lose");
        }
    }
    //new empty board
    //empty tiles will have 0, non-empty tiles will have the corresponding number
    public Game2048(){
        playing = true;
        board = new int[4][4];
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board.length; j++){
                board[i][j]=0;
            }
        }
        combined = new boolean[4][4];
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board.length; j++){
                combined[i][j]=false;
            }
        }
    }
    //generate a new number and put them in an empty spaces on the board
    //valid board (i.e. having at least one empty space) is checked with checkPlaying()
    //1 out 18 chance the board will get a number 4 instead of a number 2
    public void newNums() {
        int n = (int) (Math.random() * 4);
        int m = (int) (Math.random() * 4);
        while (board[n][m] != 0) {
            n = (int) (Math.random() * 4);
            m = (int) (Math.random() * 4);
        }
        int choice = (int)(Math.random()*18);
        if(choice == 17){
            board[n][m] = 4;
        }else {
            board[n][m] = 2;
        }
    }
    //ask for the player's move
    //'w' is left, 's' is down, 'a' is left, 'd' is right
    public boolean askToMove(){
        Scanner s = new Scanner(System.in);
        System.out.println("(w)Up (a)Left (s)Down (d)Right");
        String move = s.nextLine();
        if(move.equals("w")){
            return up(board);
        }else if(move.equals("s")){
            return down(board);
        }else if(move.equals("a")){
            return left(board);
        }else if(move.equals("d")){
            return right(board);
        }else{
            askToMove();
        }
        return false;
    }
    //function used when the user picks 'up'
    public boolean up(int[][] board){
        int move = 0;
        boolean combine = false;
        boolean changed = false;
        for(int r = 1; r < board.length; r++){
            for(int c = 0; c < board[0].length; c++){
                if(board[r][c] != 0) {
                    for (int k = 1; k < r+1; k++) {
                        if (board[r - k][c] == 0) {
                            move++;
                        }else if (board[r - k][c] == board[r][c]) {
                            combine = true;
                            move++;
                            break;
                        }else{
                            break;
                        }
                    }
                    //System.out.println(combine);
                    //System.out.println(move);
                    if (combine && !combined[r - move][c]) {
                        //System.out.println("|"+r+c+"|");
                        //System.out.println(move);
                        //print();
                        board[r - move][c] = board[r - move][c]*2;
                        board[r][c] = 0;
                        combined[r - move][c] = true;
                        //print();
                    } else if(combine && combined[r - move][c]){
                        board[r - move + 1][c] = board[r][c];
                        board[r][c] = 0;
                    }
                    if (move != 0 && !combine) {
                        board[r - move][c] = board[r][c];
                        board[r][c] = 0;
                    }
                    if (move!=0){
                        changed = true;
                    }
                    move = 0;
                    combine = false;
                }

            }

        }
        resetCombined();
        return changed;

    }
    //function used when the user picks 'down'
    public boolean down(int[][] board){
        int move = 0;
        boolean combine = false;
        boolean changed = false;
        for(int r = 2; r >= 0; r--){
            for(int c = 0; c < board[0].length; c++){
                if(board[r][c] != 0) {
                    for (int k = 1; k <= 3-r; k++) {
                        if (board[r + k][c] == 0) {
                            move++;
                        }else if (board[r + k][c] == board[r][c]) {
                            combine = true;
                            move++;
                            break;
                        }else{
                            break;
                        }
                    }
                    if (combine && !combined[r + move][c]) {
                        board[r + move][c] = board[r + move][c]*2;
                        board[r][c] = 0;
                        combined[r + move][c] = true;
                    } else if(combine && combined[r + move][c]) {
                        board[r + move - 1][c] = board[r][c];
                        board[r][c] = 0;
                    }
                    if (move != 0 && !combine) {
                        board[r + move][c] = board[r][c];
                        board[r][c] = 0;
                    }
                    if (move!=0){
                        changed = true;
                    }
                    move = 0;
                    combine = false;
                }

            }

        }
        resetCombined();
        return changed;
    }
    //function used when the user picks 'left'
    public boolean left(int[][] board){
        int move = 0;
        boolean combine = false;
        boolean changed = false;
        for(int r = 0; r < board.length; r++){
            for(int c = 1; c < board[0].length; c++){
                if(board[r][c] != 0) {
                    for (int k = 1; k < c+1; k++) {
                        if (board[r][c  - k] == 0) {
                            move++;
                        }else if (board[r][c  - k] == board[r][c]) {
                            combine = true;
                            move++;
                            break;
                        }else{
                            break;
                        }
                    }
                    if (combine && !combined[r][c - move]) {
                        board[r][c - move] = board[r][c - move]*2;
                        board[r][c] = 0;
                        combined[r][c - move] = true;
                    } else if(combine && combined[r][c - move]) {
                        board[r][c - move + 1] = board[r][c];
                        board[r][c] = 0;
                    }
                    if (move != 0 && !combine) {
                        board[r][c - move] = board[r][c];
                        board[r][c] = 0;
                    }
                    if (move!=0){
                        changed = true;
                    }
                    move = 0;
                    combine = false;
                }

            }

        }
        resetCombined();
        return changed;
    }
    //function used when the user picks 'right'
    public boolean right(int[][] board){
        int move = 0;
        boolean combine = false;
        boolean changed = false;
        for(int r = 0; r < board.length; r++){
            for(int c = 2; c >= 0; c--){
                if(board[r][c] != 0) {
                    for (int k = 1; k <= 3-c; k++) {
                        if (board[r][c + k] == 0) {
                            move++;
                        }else if (board[r][c + k] == board[r][c]) {
                            combine = true;
                            move++;
                            break;
                        }else{
                            break;
                        }
                    }
                    if (combine && !combined[r][c + move]) {
                        board[r][c + move] = board[r][c + move]*2;
                        board[r][c] = 0;
                        combined[r][c + move] = true;
                    } else if(combine && combined[r][c + move]) {
                        board[r][c + move - 1] = board[r][c];
                        board[r][c] = 0;
                    }
                    if (move != 0 && !combine) {
                        board[r][c + move] = board[r][c];
                        board[r][c] = 0;
                    }
                    if (move!=0){
                        changed = true;
                    }
                    move = 0;
                    combine = false;
                }

            }

        }
        resetCombined();
        return changed;
    }
    //prints the current board
    public void print(){
        System.out.println("-----------------------------");
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board.length; j++){
                System.out.print("| " + color(board[i][j]) + value(board[i][j]) + ANSI_RESET + space(board[i][j]));
            }
            System.out.print("|\n-----------------------------\n");
        }
    }
    //prints the appropriate spaces needed so that the number of digits in a number in a tile won't affect the board
    public String space(int i) {
        String num = Integer.toString(i);
        StringBuilder space = new StringBuilder();
        space.append(" ".repeat(Math.max(0, 5 - num.length())));
        return space.toString();
    }
    //parameter is a tile in the board, if the tile is empty (as in having '0'), the funtion will return " "
    //otherwise will return the same number
    public String value(int i){
        if(i == 0){
            return " ";
        }
        return Integer.toString(i);
    }
    //color go brrrrrrr, changes with each tile value
    public String color(int i){
        if(i == 2){
            return ANSI_CYAN;
        }else if(i == 4){
            return ANSI_PURPLE;
        }else if(i == 8){
            return ANSI_GREEN;
        }else if(i == 16){
            return ANSI_YELLOW;
        }else if(i == 32){
            return ANSI_RED;
        }else if(i == 64){
            return ANSI_BLUE;
        }else if(i == 128){
            return ANSI_PURPLE;
        }else if(i == 256){
            return ANSI_CYAN;
        }else if(i == 512){
            return ANSI_GREEN;
        }else if(i == 1024){
            return ANSI_YELLOW;
        }else if(i == 2048){
            return ANSI_BLACK;
        }
        return ANSI_RESET;
    }
    //sets the values in a second, non-visible-by-the-player board used to track if a tile has been combined back to false
    public void resetCombined(){
        for(int i = 0; i < combined.length; i++){
            Arrays.fill(combined[i], false);
        }
    }
    //check to see if the game is over
    public void checkPlaying(){
        checkWin();
        boolean stillHaveZero = false;
        boolean stillCanCombine = false;
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++) {
                try {
                    if (board[i][j] == board[i + 1][j]) {
                        stillCanCombine = true;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {}
                try {
                    if (board[i][j] == board[i - 1][j]) {
                        stillCanCombine = true;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {}
                try {
                    if (board[i][j] == board[i][j + 1]) {
                        stillCanCombine = true;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {}
                try {
                    if (board[i][j] == board[i][j - 1]) {
                        stillCanCombine = true;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {}
                if (board[i][j] == 0) {
                    stillHaveZero = true;
                }
            }
        }
        if(!stillCanCombine && !stillHaveZero){
            playing = false;
        }
    }
    //check to see if they player won
    public void checkWin(){
        for(int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if(board[i][j] == 2048){
                    win = true;
                }
            }
        }
    }
}
