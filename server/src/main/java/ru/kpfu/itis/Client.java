package ru.kpfu.itis;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client extends Application {

    private static Socket socket;
    private static DataInputThread dataInputThread;
    private static ObjectOutputStream objectOutputStream;
    private Button[][] buttons;
    private boolean[][] btns;
    private int[][] btnWinPositions = {{0, 1, 2}, {0, 4, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {2, 4, 6}, {3, 4, 5}, {6, 7, 8}};
    private char mySymbol = 'X';
    private char enemySymbol = 'O';

    public static void main(String[] args) throws IOException {
        socket = new Socket(Protocol.IP, Protocol.PORT);
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        btns = new boolean[3][3];
        KeyFrame keyFrame = new KeyFrame(Duration.millis(500),
                event -> {
                    for (int i = 0; i < btns.length; i++) {
                        for (int j = 0; j < btns[i].length; j++) {
                            if (btns[i][j]) {
                                buttons[i][j].setText(String.valueOf(enemySymbol));
                                btns[i][j] = false;
                            }
                        }
                    }
                });
        Timeline timeline = new Timeline(keyFrame);
        timeline.setCycleCount(-1);
        timeline.play();
        dataInputThread = new DataInputThread(socket.getInputStream(), this);
        System.out.println("Client");
        createWindow(primaryStage);
    }

    //TODO get from server who is X and who is O
    // server flags players turn
    // server checks who is winner
    // can add timer for player turn (timer ends - enemy wins)


    public void setPosition(int readInt) {
        System.out.println("btn");
        btns[readInt / 3][readInt % 3] = true;
    }

    public void getPosition(int readInt) {
        try {
            objectOutputStream.writeObject(readInt + "");
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

        buttons = new Button[3][3];
        Label winnerLabel = new Label();
        Label resultLabel = new Label();

        for (int i = 0; i < 3; i++) {
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

                        if (buttons[x][y].getText().equals("")) {
                            buttons[x][y].setText(String.valueOf(mySymbol));
                            buttons[x][y].setId("X");
                            xCount++;
                            getPosition(x * 3 + y);
                            winner = check(buttons);
                        }


                    }
                });
            }
        }

        newGame.setOnAction(e -> {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    buttons[i][j].setText("");
                    buttons[i][j].setId("empty");
                }
            }
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

    public static String check(Button[][] buttons) {
        for (int i = 0; i < 3; i++) {
            if (buttons[i][0].getText().equals(buttons[i][1].getText())
                    && buttons[i][1].getText().equals(buttons[i][2].getText())
                    && !buttons[i][2].getText().equals(""))
                return (buttons[i][0].getText());
            else if (buttons[0][i].getText().equals(buttons[1][i].getText())
                    && buttons[1][i].getText().equals(buttons[2][i].getText()) &&
                    !buttons[2][i].getText().equals(""))
                return (buttons[0][i].getText());
        }
        if ((buttons[0][0].getText().equals(buttons[1][1].getText())
                && buttons[1][1].getText().equals(buttons[2][2].getText()))
                || (buttons[0][2].getText().equals(buttons[1][1].getText())
                && buttons[1][1].getText().equals(buttons[2][0].getText()))
                && !buttons[1][1].getText().equals(""))
            return (buttons[1][1].getText());
        return ("");
    }
}
