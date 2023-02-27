import java.util.Arrays;
import java.util.Scanner;

public class ConnectFour {
    private static String[][] gameBoard = new String[7][7];
    private static int[][] filled_gameBoard = new int[7][7];
    private static int column, player_number;
    private static boolean turn = true;
    private static boolean playerWon = false;   
    private static String disk_color; 
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args){
        for(int i=0;i<gameBoard.length;i++){
            //What Arrays.fill() does is change all the elements in the row, which was null previously, to "| "
            Arrays.fill(gameBoard[i], "| ");
        }
        Arrays.fill(gameBoard[6], "--");
        for(int i=6;i>=0;i--){
            gameBoard[i][6] = "| |";
        }
        play();
    }

    public static void play(){
        while(!playerWon){
            if(turn){
                System.out.print("Player 1, drop your red disk at column (0-6): ");
                disk_color = "R";
                player_number = 1;
            } else {
                System.out.print("Player 2, drop your yellow disk at column (0-6): ");
                disk_color = "Y";
                player_number = 2;
            }
            //.next() finds the next token in a the string, basically ignores spaces.
            //.charAt() gets the character at index 0
            char temp = sc.next().charAt(0);
            //This is why it's necessary to use .next().charAt(0), in order to convert from char to int
            //What temp - '0' does is since characters can be represented by ascii values, where 0 is 48, what 
            //temp - '0' does is let's say temp is 1, the ascii value for that is 49, since 0 is 48, what we are doing is
            //49-48, which the result us 1, so temp-'0' allows is to convert from char to int
            column = temp - '0';
            if(!validMove(column)){
                continue;
            }
            for(int row=5;row>=0;row--){
                int col = column;
                String tile = gameBoard[row][col];
                if(tile == "| "){
                    gameBoard[row][col] = "|" + disk_color;
                    filled_gameBoard[row][col] = player_number;
                    break;
                }
            }

            printGameBoard();

            if(checkWinner(filled_gameBoard, disk_color, player_number)){
                if(turn){
                    System.out.println("Player 1 won");
                } else {
                    System.out.println("Player 2 won");
                }
                playerWon = true;
            }
            turn = !turn;
        }
    }

    public static void printGameBoard(){
        for(String[] row:gameBoard){
            for(String col:row){
                System.out.print(col);
            }
            System.out.println();
            //what a try statement does is if the code inside the try statement has an error, it executes the code in the catch statement
            //Thread.sleep() can sometimes throw a InterruptedException, which means something wants to interrupt the current thread. What a thread  
            //is on a CPU core can have, there are multiple threads, so the CPU can do multiple things at the same time. What  
            //Thread.currentThread().interrupt() does is clear the interrupted thread, so we don't get any issues. 
            try{
                Thread.sleep(100);
            } catch(InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }  
    }

    public static boolean checkWinner(int[][] filled_gameBoard, String disk_color, int player_number){
        //check for horizontal win 
        if(horizontal_win(filled_gameBoard, disk_color, player_number)){
            return true;
        }
        
        int[][] copy_of_filled_gameBoard = new int[7][7];
        //This code transposes a matrix, which means that for example its turns this matrix of
        // 1,2,3     1,4,7
        // 4,5,6 --> 2,5,8
        // 7,8,9     3,6,9  
        //This turns the columns into rows, which means that we can reuse the horizontal_win code to check for vertical wins
        for (int i = 0; i < filled_gameBoard.length; i++) {
            for (int j = 0; j < filled_gameBoard[i].length; j++) {
                copy_of_filled_gameBoard[j][i] = filled_gameBoard[i][j];
            }
        }
        //check for vertical win 
        if(horizontal_win(copy_of_filled_gameBoard, disk_color, player_number)){
            return true;
        } 
        //check for major diagonal win
        if(major_diagonal_win(filled_gameBoard, disk_color, player_number)){
            return true;
        }
        //creates a new array but the elements of each of gameBoard arrays are reversed. The reason for reversing the arrays in the gameBoard
        //matrix is to check for minor diagonal wins. By reversing the array, we can reuse the major diagonal code
        int[][] reversed_filled_game_board = new int[7][7];
        for(int i=0;i<gameBoard.length;i++){
            int index = 0;  
            for(int j=gameBoard[i].length-1;j>=0;j--){
                reversed_filled_game_board[i][index] = filled_gameBoard[i][j];
                index++;
            }
        }
        //check for minor diagonal win 
        if(major_diagonal_win(reversed_filled_game_board, disk_color, player_number)){
            return true;
        } 
        return false;
    }
    public static boolean horizontal_win(int[][] filled_gameBoard, String disk_color, int player_number){
        int count = 0;
        for(int row=0;row<gameBoard.length-1;row++){
            for(int col=0;col<gameBoard[row].length;col++){
                if(filled_gameBoard[row][col] == player_number){
                    count++;
                } 
                if(count > 0 && filled_gameBoard[row][col] != player_number){
                    count = 0;
                } else if(count == 4){
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean major_diagonal_win(int[][] filled_gameBoard, String disk_color, int player_number){
        //This traverses a matrix diagonally 
        int temp = filled_gameBoard.length+filled_gameBoard[0].length-2;
        for(int k=0;k<temp;k++) {
            int count = 0;
            for(int j=0;j<=k;j++) {
                int i = k-j;
                if(i < filled_gameBoard.length && j < filled_gameBoard[0].length) {
                    if(filled_gameBoard[i][j] == player_number){ 
                        count++;
                    } else if(count > 0 && filled_gameBoard[i][j] != player_number){
                        count = 0;
                    }
                }
                if(count == 4){
                    return true;
                }
            }
        }
        return false;
    }
    public static boolean validMove(int column){
        if(column >= gameBoard.length || column < 0){
            return false;
        }
        return true;
    }
}
