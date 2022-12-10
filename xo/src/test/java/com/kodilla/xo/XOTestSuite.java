package com.kodilla.xo;

import com.kodilla.xo.board.Board;
import com.kodilla.xo.board.PositionConverter;
import com.kodilla.xo.mechanics.GameMechanics;
import com.kodilla.xo.mechanics.PositionAlreadySetException;
import com.kodilla.xo.mechanics.SelectionOutOfScopeException;
import com.kodilla.xo.user.User;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class XOTestSuite {

    @Nested
    public class BoardTestSuite {

        @Test
        void testPositionToRow() {
            //Given

            //When
            int row3 = PositionConverter.positionToRow(3, 3);
            int row2 = PositionConverter.positionToRow(5, 3);
            int row1 = PositionConverter.positionToRow(7, 3);

            //Then
            assertAll(() -> assertEquals(2, row3),
                    () -> assertEquals(1, row2),
                    () -> assertEquals(0, row1));
        }

        @Test
        void testPositionToColum() {
            //Given

            //When
            int column3 = PositionConverter.positionToColumn(3, 3);
            int column2 = PositionConverter.positionToColumn(5, 3);
            int column1 = PositionConverter.positionToColumn(7, 3);

            //Then
            assertAll(() -> assertEquals(2, column3),
                    () -> assertEquals(1, column2),
                    () -> assertEquals(0, column1));
        }

        @Test
        void testAddToBoard() {
            //Given
            Board board = new Board();
            User testUser1 = new User(1);
            testUser1.setUserSelection(2);
            User testUser2 = new User(2);
            testUser2.setUserSelection(4);
            User testUser3 = new User(1);
            testUser3.setUserSelection(9);

            //When
            board.addToBoard(testUser1);
            board.addToBoard(testUser2);
            board.addToBoard(testUser3);

            //Then
            assertAll(() -> assertEquals(1, board.getBoard()[2][1]),
                    () -> assertEquals(2, board.getBoard()[1][0]),
                    () -> assertEquals(1, board.getBoard()[0][2]),
                    () -> assertEquals(0, board.getBoard()[0][0]));
        }

        @Test
        void testAt() {
            //Given
            Board board = new Board();
            User testUser1 = new User(1);
            testUser1.setUserSelection(2);
            User testUser2 = new User(2);
            testUser2.setUserSelection(4);
            User testUser3 = new User(1);
            testUser3.setUserSelection(9);

            board.addToBoard(testUser1);
            board.addToBoard(testUser2);
            board.addToBoard(testUser3);

            //When
            int result1 = board.at(2);
            int result2 = board.at(4);
            int result3 = board.at(9);
            int result4 = board.at(7);

            //Then
            assertAll(() -> assertEquals(1, result1),
                    () -> assertEquals(2, result2),
                    () -> assertEquals(1, result3),
                    () -> assertEquals(0, result4));
        }

    }

    @Nested
    public class MechanicsTestSuite {

        private final User userX = new User(1);
        private final User userO = new User(2);

        private Board initializeBoardWithX(int i, int j, int k){
            Board board = new Board();

            userX.setUserSelection(i);
            board.addToBoard(userX);

            userX.setUserSelection(j);
            board.addToBoard(userX);

            userX.setUserSelection(k);
            board.addToBoard(userX);

            return board;
        }

        private Board initializeBoardWithO(int i, int j, int k){
            Board board = new Board();

            userO.setUserSelection(i);
            board.addToBoard(userO);

            userO.setUserSelection(j);
            board.addToBoard(userO);

            userO.setUserSelection(k);
            board.addToBoard(userO);

            return board;
        }

        @Test
        void testSwitchActiveUser() {
            //Given
            GameMechanics gameMechanics = new GameMechanics();

            //When
            User initialUser = gameMechanics.getActiveUser();
            gameMechanics.switchActiveUser();
            User userAfterFirstSwitch = gameMechanics.getActiveUser();
            gameMechanics.switchActiveUser();
            User userAfterTwoSwitches = gameMechanics.getActiveUser();

            //Then
            assertAll(() -> assertNotEquals(initialUser, userAfterFirstSwitch),
                    () -> assertEquals(initialUser, userAfterTwoSwitches));
        }

        @Test
        void testValidateSelectionOutOfScope() {
            //Given
            GameMechanics gameMechanics = new GameMechanics();
            gameMechanics.getActiveUser().setUserSelection(10);

            //When+Then
            assertThrows(SelectionOutOfScopeException.class,
                    () -> gameMechanics.validateSelection(new Board()));
        }

        @Test
        void testValidateSelectionPositionAlreadyExist() {
            //Given
            GameMechanics gameMechanics = new GameMechanics();
            Board board = new Board();

            User user = new User(1);
            user.setUserSelection(9);

            board.addToBoard(user);

            //When
            gameMechanics.getActiveUser().setUserSelection(9);

            //Then
            assertThrows(PositionAlreadySetException.class,
                    () -> gameMechanics.validateSelection(board));
        }

        @Test
        void testValidateSelectionPosition1() {
            //Given
            GameMechanics gameMechanics = new GameMechanics();
            gameMechanics.getActiveUser().setUserSelection(1);

            //When + Then
            assertDoesNotThrow(() -> gameMechanics.validateSelection(new Board()));
        }

        @Test
        void testValidateSelectionPosition9() {
            //Given
            GameMechanics gameMechanics = new GameMechanics();
            gameMechanics.getActiveUser().setUserSelection(9);

            //When + Then
            assertDoesNotThrow(() -> gameMechanics.validateSelection(new Board()));
        }

        @Test
        void testWinByRowsForUserX(){
            //Given
            GameMechanics gameMechanics = new GameMechanics();
            Board board1 = initializeBoardWithX(7, 8, 9);
            Board board2 = initializeBoardWithX(4, 5, 6);
            Board board3 = initializeBoardWithX(1, 2, 3);
            Board board4 = initializeBoardWithX(7, 5, 9);

            //When
            boolean result1 = gameMechanics.winByRows(board1, userX);
            boolean result2 = gameMechanics.winByRows(board2, userX);
            boolean result3 = gameMechanics.winByRows(board3, userX);
            boolean result4 = gameMechanics.winByRows(board4, userX);

            //Then
            assertAll(() -> assertTrue(result1),
                    () -> assertTrue(result2),
                    () -> assertTrue(result3),
                    () -> assertFalse(result4));
        }

        @Test
        void testWinByRowsForUserO(){
            //Given
            GameMechanics gameMechanics = new GameMechanics();
            Board board1 = initializeBoardWithO(7, 8, 9);
            Board board2 = initializeBoardWithO(4, 5, 6);
            Board board3 = initializeBoardWithO(1, 2, 3);
            Board board4 = initializeBoardWithO(7, 5, 9);

            //When
            boolean result1 = gameMechanics.winByRows(board1, userO);
            boolean result2 = gameMechanics.winByRows(board2, userO);
            boolean result3 = gameMechanics.winByRows(board3, userO);
            boolean result4 = gameMechanics.winByRows(board4, userO);

            //Then
            assertAll(() -> assertTrue(result1),
                    () -> assertTrue(result2),
                    () -> assertTrue(result3),
                    () -> assertFalse(result4));
        }

        @Test
        void testWinByColumnsForUserX(){
            //Given
            GameMechanics gameMechanics = new GameMechanics();
            Board board1 = initializeBoardWithX(7, 4, 1);
            Board board2 = initializeBoardWithX(8, 5, 2);
            Board board3 = initializeBoardWithX(9, 6, 3);
            Board board4 = initializeBoardWithX(7, 5, 1);

            //When
            boolean result1 = gameMechanics.winByColumns(board1, userX);
            boolean result2 = gameMechanics.winByColumns(board2, userX);
            boolean result3 = gameMechanics.winByColumns(board3, userX);
            boolean result4 = gameMechanics.winByColumns(board4, userX);

            //Then
            assertAll(() -> assertTrue(result1),
                    () -> assertTrue(result2),
                    () -> assertTrue(result3),
                    () -> assertFalse(result4));

        }

        @Test
        void testWinByColumnsForUserO(){
            //Given
            GameMechanics gameMechanics = new GameMechanics();
            Board board1 = initializeBoardWithO(7, 4, 1);
            Board board2 = initializeBoardWithO(8, 5, 2);
            Board board3 = initializeBoardWithO(9, 6, 3);
            Board board4 = initializeBoardWithO(7, 5, 1);

            //When
            boolean result1 = gameMechanics.winByColumns(board1, userO);
            boolean result2 = gameMechanics.winByColumns(board2, userO);
            boolean result3 = gameMechanics.winByColumns(board3, userO);
            boolean result4 = gameMechanics.winByColumns(board4, userO);

            //Then
            assertAll(() -> assertTrue(result1),
                    () -> assertTrue(result2),
                    () -> assertTrue(result3),
                    () -> assertFalse(result4));

        }

        @Test
        void testWinByDiagonalForUserX(){
            //Given
            GameMechanics gameMechanics = new GameMechanics();
            Board board1 = initializeBoardWithX(7, 5, 3);
            Board board2 = initializeBoardWithX(7, 6, 3);

            //When
            boolean result1 = gameMechanics.winByDiagonal(board1, userX);
            boolean result2 = gameMechanics.winByDiagonal(board2, userX);

            //Then
            assertAll(() -> assertTrue(result1),
                    () -> assertFalse(result2));

        }

        @Test
        void testWinByDiagonalForUserO(){
            //Given
            GameMechanics gameMechanics = new GameMechanics();
            Board board1 = initializeBoardWithO(7, 5, 3);
            Board board2 = initializeBoardWithO(7, 6, 3);

            //When
            boolean result1 = gameMechanics.winByDiagonal(board1, userO);
            boolean result2 = gameMechanics.winByDiagonal(board2, userO);

            //Then
            assertAll(() -> assertTrue(result1),
                    () -> assertFalse(result2));

        }

        @Test
        void testWinByAntiDiagonalForUserX(){
            //Given
            GameMechanics gameMechanics = new GameMechanics();
            Board board1 = initializeBoardWithX(9, 5, 1);
            Board board2 = initializeBoardWithX(9, 6, 1);

            //When
            boolean result1 = gameMechanics.winByAntiDiagonal(board1, userX);
            boolean result2 = gameMechanics.winByAntiDiagonal(board2, userX);

            //Then
            assertAll(() -> assertTrue(result1),
                    () -> assertFalse(result2));

        }

        @Test
        void testWinByAntiDiagonalForUserO(){
            //Given
            GameMechanics gameMechanics = new GameMechanics();
            Board board1 = initializeBoardWithO(9, 5, 1);
            Board board2 = initializeBoardWithO(9, 6, 1);

            //When
            boolean result1 = gameMechanics.winByAntiDiagonal(board1, userO);
            boolean result2 = gameMechanics.winByAntiDiagonal(board2, userO);

            //Then
            assertAll(() -> assertTrue(result1),
                    () -> assertFalse(result2));

        }

        @Test
        void testWinForUserX(){
            //Given
            GameMechanics gameMechanics = new GameMechanics();
            Board boardRows = initializeBoardWithX(7, 8, 9);
            Board boardColumns = initializeBoardWithX(8, 5, 2);
            Board boardDiagonal = initializeBoardWithX(7, 5, 3);
            Board boardAntiDiagonal = initializeBoardWithX(9, 5, 1);

            //When
            boolean result1 = gameMechanics.win(boardRows, userX);
            boolean result2 = gameMechanics.win(boardColumns, userX);
            boolean result3 = gameMechanics.win(boardDiagonal, userX);
            boolean result4 = gameMechanics.win(boardAntiDiagonal, userX);

            //Then
            assertAll(() -> assertTrue(result1),
                    () -> assertTrue(result2),
                    () -> assertTrue(result3),
                    () -> assertTrue(result4));
        }

        @Test
        void testWinForUserO(){
            //Given
            GameMechanics gameMechanics = new GameMechanics();
            Board boardRows = initializeBoardWithO(7, 8, 9);
            Board boardColumns = initializeBoardWithO(8, 5, 2);
            Board boardDiagonal = initializeBoardWithO(7, 5, 3);
            Board boardAntiDiagonal = initializeBoardWithO(9, 5, 1);

            //When
            boolean result1 = gameMechanics.win(boardRows, userO);
            boolean result2 = gameMechanics.win(boardColumns, userO);
            boolean result3 = gameMechanics.win(boardDiagonal, userO);
            boolean result4 = gameMechanics.win(boardAntiDiagonal, userO);

            //Then
            assertAll(() -> assertTrue(result1),
                    () -> assertTrue(result2),
                    () -> assertTrue(result3),
                    () -> assertTrue(result4));
        }

        @Test
        void testDraw(){
            //Given
            GameMechanics gameMechanics = new GameMechanics();
            Board boardDraw = new Board();

            //board
            //XOX
            //XOX
            //OXO
            userX.setUserSelection(7);
            boardDraw.addToBoard(userX);
            userO.setUserSelection(8);
            boardDraw.addToBoard(userO);
            userX.setUserSelection(9);
            boardDraw.addToBoard(userX);
            userO.setUserSelection(5);
            boardDraw.addToBoard(userO);
            userX.setUserSelection(4);
            boardDraw.addToBoard(userX);
            userO.setUserSelection(1);
            boardDraw.addToBoard(userO);
            userX.setUserSelection(6);
            boardDraw.addToBoard(userX);
            userO.setUserSelection(3);
            boardDraw.addToBoard(userO);
            userX.setUserSelection(2);
            boardDraw.addToBoard(userX);

            //When
            boolean result = gameMechanics.draw(boardDraw);

            //Then
            assertTrue(result);
        }

        @Test
        void testNotDraw(){
            //Given
            GameMechanics gameMechanics = new GameMechanics();
            Board boardDraw = new Board();

            //board
            //XOX
            //XOX
            //XXO
            userX.setUserSelection(7);
            boardDraw.addToBoard(userX);
            userO.setUserSelection(8);
            boardDraw.addToBoard(userO);
            userX.setUserSelection(9);
            boardDraw.addToBoard(userX);
            userO.setUserSelection(5);
            boardDraw.addToBoard(userO);
            userX.setUserSelection(4);
            boardDraw.addToBoard(userX);
            userO.setUserSelection(1);
            boardDraw.addToBoard(userX);
            userX.setUserSelection(6);
            boardDraw.addToBoard(userX);
            userO.setUserSelection(3);
            boardDraw.addToBoard(userO);
            userX.setUserSelection(2);
            boardDraw.addToBoard(userX);

            //When
            boolean result = gameMechanics.draw(boardDraw);

            //Then
            assertFalse(result);
        }
    }
}