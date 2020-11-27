package ru.kpfu.itis.game;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Random;

public class GameWindow {

    private Random random = new Random();
    private char lastWinner = 'X';
    private Checker checker = new Checker();

    public void createWindow(Stage primaryStage) {
        VBox root = new VBox();
        root.setId("root");
        HBox top = new HBox();
        top.setId("top");
        Button newGame = new Button("Новая игра");
        newGame.setId("newGame");
        Button quit = new Button("Выход");
        quit.setId("quit");

        GridPane grid = new GridPane();
        grid.setId("grid");

        Button[][] buttons = new Button[3][3];
        Label winnerLabel = new Label();
        Label resultLabel = new Label();

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                int x = i, y = j;
                buttons[i][j] = new Button("");
                grid.add(buttons[i][j], j, i);
                buttons[i][j].setPrefWidth(70);
                buttons[i][j].setPrefHeight(70);

                buttons[i][j].setOnAction(e -> {
                    int xCount = 0, oCount = 0;
                    String winner = "";
                    if (resultLabel.getText().equals("")) {

                        if (lastWinner == 'X') {
                            for (int a = 0; a < 3; a++) {
                                for (int b = 0; b < 3; b++) {
                                    if (buttons[a][b].getText().equals("X"))
                                        xCount++;
                                }
                            }

                            if (buttons[x][y].getText().equals("")) {
                                buttons[x][y].setText("X");
                                buttons[x][y].setId("X");
                                xCount++;
                                winner = checker.check(buttons);
                                if (!winner.equals("")) {
                                    winnerLabel.setId("winner");
                                    winnerLabel.setText("ПОБЕДА");
                                    resultLabel.setId(winner);
                                    resultLabel.setText(winner);
                                    switch (winner) {
                                        case "X":
                                            lastWinner = 'X';
                                            break;
                                        case "O":
                                            lastWinner = 'O';
                                    }
                                } else if (xCount == 5) {
                                    winnerLabel.setId("winner");
                                    winnerLabel.setText("НИЧЬЯ");
                                }

                                if (winnerLabel.getText().equals(""))
                                    fillO(buttons);
                                winner = checker.check(buttons);

                                if (!winner.equals("")) {
                                    winnerLabel.setId("winner");
                                    winnerLabel.setText("ПОБЕДА");
                                    resultLabel.setId(winner);
                                    resultLabel.setText(winner);
                                    switch (winner) {
                                        case "X":
                                            lastWinner = 'X';
                                            break;
                                        case "O":
                                            lastWinner = 'O';
                                    }
                                }
                            }
                        } else {
                            for (int a = 0; a < 3; a++) {
                                for (int b = 0; b < 3; b++) {
                                    if (buttons[a][b].getText().equals("O"))
                                        oCount++;
                                }
                            }

                            if (buttons[x][y].getText().equals("")) {
                                buttons[x][y].setText("X");
                                buttons[x][y].setId("X");
                            }
                            winner = checker.check(buttons);

                            if (!winner.equals("")) {
                                winnerLabel.setId("winner");
                                winnerLabel.setText("ПОБЕДА");
                                resultLabel.setId(winner);
                                resultLabel.setText(winner);
                                switch (winner) {
                                    case "X":
                                        lastWinner = 'X';
                                        break;
                                    case "O":
                                        lastWinner = 'O';
                                }
                            }

                            if (winnerLabel.getText().equals("")) {
                                fillO(buttons);
                                oCount++;
                            }
                            winner = checker.check(buttons);

                            if (!winner.equals("")) {
                                winnerLabel.setId("winner");
                                winnerLabel.setText("ПОБЕДА");
                                resultLabel.setId(winner);
                                resultLabel.setText(winner);
                                switch (winner) {
                                    case "X":
                                        lastWinner = 'X';
                                        break;
                                    case "O":
                                        lastWinner = 'O';
                                }
                            } else if (oCount == 5) {
                                winnerLabel.setId("winner");
                                winnerLabel.setText("НИЧЬЯ");
                            }
                        }
                    }
                });

            }

        newGame.setOnAction(e -> {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    buttons[i][j].setText("");
                    buttons[i][j].setId("empty");
                }
            }
            if (lastWinner == 'O')
                fillO(buttons);
            winnerLabel.setId("");
            winnerLabel.setText("");
            resultLabel.setId("");
            resultLabel.setText("");
        });

        quit.setOnAction(e -> System.exit(0));

        newGame.setPrefWidth(100);
        HBox.setMargin(newGame, new Insets(10, 0, 10, 15));
        quit.setPrefWidth(100);
        HBox.setMargin(quit, new Insets(10, 0, 10, 20));
        VBox.setMargin(grid, new Insets(20, 20, 20, 20));
        top.getChildren().addAll(newGame, quit);
        winnerLabel.setAlignment(Pos.CENTER);
        winnerLabel.setPrefWidth(70);
        VBox.setMargin(winnerLabel, new Insets(0, 0, 0, 90));
        resultLabel.setPrefWidth(70);
        resultLabel.setPrefHeight(70);
        resultLabel.setAlignment(Pos.CENTER);
        VBox.setMargin(resultLabel, new Insets(0, 0, 0, 90));
        root.getChildren().addAll(top, grid, winnerLabel, resultLabel);
        primaryStage.setTitle("Крестики-нолики");
        Scene scene = new Scene(root, 250, 400);
        primaryStage.setScene(scene);
        primaryStage.show();

    }


    private void fillO(Button[][] buttons) {
        int i, j = 0;
        int xCount = 0, oCount = 0, count = 0;

        for (i = 0; i < 3; i++) {
            for (j = 0; j < 3; j++) {
                if (buttons[i][j].getText().equals("O"))
                    count++;
            }
        }

        if ((count < 4 && lastWinner == 'X') || (count < 5 && lastWinner == 'O')) {

            for (i = 0; i < 3; i++) {
                xCount = oCount = 0;
                for (j = 0; j < 3; j++)
                    if (buttons[i][j].getText().equals("O"))
                        oCount++;
                    else if (buttons[i][j].getText().equals("X"))
                        xCount++;
                if (oCount == 2)
                    break;
            }
            if (oCount == 2 && xCount == 0) {
                for (j = 0; j < 3; j++)
                    if (buttons[i][j].getText().equals("")) {
                        buttons[i][j].setText("O");
                        buttons[i][j].setId("O");
                    }
            } else {

                for (i = 0; i < 3; i++) {
                    xCount = oCount = 0;
                    for (j = 0; j < 3; j++)
                        if (buttons[j][i].getText().equals("O"))
                            oCount++;
                        else if (buttons[j][i].getText().equals("X"))
                            xCount++;
                    if (oCount == 2)
                        break;
                }

                if (oCount == 2 && xCount == 0) {
                    for (j = 0; j < 3; j++)
                        if (buttons[j][i].getText().equals("")) {
                            buttons[j][i].setText("O");
                            buttons[j][i].setId("O");
                        }
                } else {
                    xCount = oCount = 0;
                    for (i = 0; i < 3; i++)
                        if (buttons[i][i].getText().equals("O"))
                            oCount++;
                        else if (buttons[i][i].getText().equals("X"))
                            xCount++;
                    if (oCount == 2 && xCount == 0) {
                        for (i = 0; i < 3; i++)
                            if (buttons[i][i].getText().equals("")) {
                                buttons[i][i].setText("O");
                                buttons[i][i].setId("O");
                            }
                    } else {
                        xCount = oCount = 0;
                        for (i = 0; i < 3; i++)
                            if (buttons[i][2 - i].getText().equals("O"))
                                oCount++;
                            else if (buttons[i][2 - i].getText().equals("X"))
                                xCount++;
                        if (oCount == 2 && xCount == 0) {
                            for (i = 0; i < 3; i++)
                                if (buttons[i][2 - i].getText().equals("")) {
                                    buttons[i][2 - i].setText("O");
                                    buttons[i][2 - i].setId("O");
                                }
                        } else {
                            for (i = 0; i < 3; i++) {
                                xCount = oCount = 0;
                                for (j = 0; j < 3; j++)
                                    if (buttons[i][j].getText().equals("O"))
                                        oCount++;
                                    else if (buttons[i][j].getText().equals("X"))
                                        xCount++;
                                if (xCount == 2)
                                    break;
                            }
                            if (xCount == 2 && oCount == 0) {
                                for (j = 0; j < 3; j++)
                                    if (buttons[i][j].getText().equals("")) {
                                        buttons[i][j].setText("O");
                                        buttons[i][j].setId("O");
                                    }
                            } else {
                                for (i = 0; i < 3; i++) {
                                    xCount = oCount = 0;
                                    for (j = 0; j < 3; j++)
                                        if (buttons[j][i].getText().equals("O"))
                                            oCount++;
                                        else if (buttons[j][i].getText().equals("X"))
                                            xCount++;
                                    if (xCount == 2)
                                        break;
                                }
                                if (xCount == 2 && oCount == 0) {
                                    for (j = 0; j < 3; j++)
                                        if (buttons[j][i].getText().equals("")) {
                                            buttons[j][i].setText("O");
                                            buttons[j][i].setId("O");
                                        }
                                } else {
                                    xCount = oCount = 0;
                                    for (i = 0; i < 3; i++)
                                        if (buttons[i][i].getText().equals("O"))
                                            oCount++;
                                        else if (buttons[i][i].getText().equals("X"))
                                            xCount++;
                                    if (xCount == 2 && oCount == 0) {
                                        for (i = 0; i < 3; i++)
                                            if (buttons[i][i].getText().equals("")) {
                                                buttons[i][i].setText("O");
                                                buttons[i][i].setId("O");
                                            }
                                    } else {
                                        xCount = oCount = 0;
                                        for (i = 0; i < 3; i++)
                                            if (buttons[i][2 - i].getText().equals("O"))
                                                oCount++;
                                            else if (buttons[i][2 - i].getText().equals("X"))
                                                xCount++;
                                        if (xCount == 2 && oCount == 0) {
                                            for (i = 0; i < 3; i++)
                                                if (buttons[i][2 - i].getText().equals("")) {
                                                    buttons[i][2 - i].setText("O");
                                                    buttons[i][2 - i].setId("O");
                                                }
                                        } else {
                                            do {
                                                i = random.nextInt(3);
                                                j = random.nextInt(3);
                                            } while (buttons[i][j].getText().equals("X") || buttons[i][j].getText().equals("O"));
                                            buttons[i][j].setText("O");
                                            buttons[i][j].setId("O");
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
