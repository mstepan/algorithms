package com.max.algs.backtracking;

import java.util.Arrays;
import java.util.BitSet;

import org.apache.log4j.Logger;

/**
 * 
 * Example of usage:
 * 
 *	int[][]board = Sudoku.createBoard();
 *	LOG.info( "Before: " );
 *	Sudoku.printBoard( board );
 *	Sudoku.solve(board);
 *  LOG.info( "\nAfter: " );
 *  Sudoku.printBoard( board );
 *
 */
public final class Sudoku {

	private static final Logger LOG = Logger.getLogger(Sudoku.class);
	
	private static final int BOARD_ROW_SIZE = 9; 
	
	private static final int BOARD_FULL_SIZE = BOARD_ROW_SIZE * BOARD_ROW_SIZE; 
	
	private static boolean finished = false;
	
	private Sudoku(){
		super();
	}
	
	
	public static void solve( int[][] board ){		
		checkBoard( board );
		
		BitSet markedCells = new BitSet(BOARD_FULL_SIZE);
		
		for( int row = 0; row < board.length; row++ ){		
			for( int col = 0; col < board[row].length; col++ ){
				if( board[row][col] > 0 ){
					markedCells.set( row*BOARD_ROW_SIZE + col);
				}
			}
		}
		
		fillBoardRec( board, markedCells );		
	}

	
	private static void fillBoardRec( int[][] board, BitSet markedCells ){
		
		if( markedCells.cardinality() == BOARD_FULL_SIZE ){			
			finished = true;			
			LOG.info( "\nSudoku solved" );
			return;
		}
		
		int freeCell = markedCells.nextClearBit(0);
	
		int freeRow = freeCell / BOARD_ROW_SIZE;
		int freeCol = freeCell % BOARD_ROW_SIZE;		
		
		markedCells.set(freeCell);
		
		for( int num = 1; num < 10 && !finished; num++ ){			
			if( canSet(num, board, freeRow, freeCol) ){				
				board[freeRow][freeCol] = num;				
				fillBoardRec( board, markedCells);				
			}
		}		
		
		if( !finished ){
			board[freeRow][freeCol] = 0;
			markedCells.clear(freeCell);
		}
		
	}
	
	
	
	private static boolean canSet(int num, int[][] board, int rowToSet,	int colToSet) {
		
		// check row
		for( int col = 0; col < BOARD_ROW_SIZE; col++ ){
			if( board[rowToSet][col] == num ){
				return false;
			}
		}
		
		// check column
		for( int row = 0; row < BOARD_ROW_SIZE; row++ ){
			if( board[row][colToSet] == num ){
				return false;
			}
		}
		
		// check 3x3		
		int baseRow = rowToSet/3;
		int baseCol = colToSet/3;
		
		for( int row = baseRow*3; row < 3*(baseRow+1); row++ ){
			for( int col = baseCol*3; col < 3*(baseCol+1); col++ ){
				if( board[row][col] == num ){
					return false;
				}
			}
		}

		return true;
	}


	private static void checkBoard(  int[][] board ){
		if( board == null ){
			throw new IllegalArgumentException("Board is NULL");
		}
		
		if( board.length != BOARD_ROW_SIZE ){
			throw new IllegalArgumentException("Board size is incorrect");
		}
		
		for( int row = 0; row < board.length; row++ ){
			if( board[row].length != BOARD_ROW_SIZE ){
				throw new IllegalArgumentException("Board size is incorrect");
			}
		}
	}
	
	public static int[][] createEmptyBoard(){
			
		int[][]board = new int[9][9];
			
		for( int row = 0; row < board.length; row++ ){
			board[row] = new int[]{ 0,0,0,0,0,0,0,0,0 };
		}
		
		return board;
	}
	
	public static int[][] createBoard(){
		
		int[][]board = createEmptyBoard();
		
		board[0][7] = 1;
		board[0][8] = 2;
		
		board[1][4] = 3;
		board[1][5] = 5;
		
		board[2][3] = 6;
		board[2][7] = 7;
		
		board[3][0] = 7;
		board[3][6] = 3;
		
		board[4][3] = 4;
		board[4][6] = 8;
		
		board[5][0] = 1;

		board[6][3] = 1;
		board[6][4] = 2;
		
		board[7][1] = 8;
		board[7][7] = 4;
		
		board[8][1] = 5;
		board[8][6] = 6;
		
		return board;
	}
		
	public static void printBoard(int[][] board){		
		for( int row = 0; row < board.length; row++ ){
			LOG.info( Arrays.toString(board[row]) );
		}		
	}

}
