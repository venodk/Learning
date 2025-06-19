public class TicTakToe {

    /*
    * return
    * 0 - draw
    * 1 - player 1 win (X)
    * 2 - player 2 win (O)
    * */
    public static int getWinner(Character[][] board) {
        int[] rowCount = new int[board.length];
        int[] colCount = new int[board.length];
        int diagonal = 0;
        int revDiagonal = 0;

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (null == board[row][col]) continue;

                if (board[row][col] == 'X') {
                    rowCount[row]++;
                    colCount[col]++;
                    if (rowCount[row] == board.length || colCount[col] == board.length) return 1;
                } else if (board[row][col] == 'O') {
                    rowCount[row]--;
                    colCount[col]--;
                    if (rowCount[row] == -board.length || colCount[col] == -board.length) return 2;
                }
            }
        }
        return 0;
    }

    public static void main(String[] args) {
        Character[][] board1 = new Character[][] {{'O', null, 'O'}, {'X', null, 'X'}, {'X', 'O', 'X'}};
        int winner = TicTakToe.getWinner(board1);
        System.out.println(winner);
    }
}

