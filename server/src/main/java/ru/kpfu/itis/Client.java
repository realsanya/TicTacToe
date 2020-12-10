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
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class Client extends Application implements ConnectionListener {

    private int[][] wins = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};

    private Button[][] buttons;
    public Label winnerLabel;
    public Label resultLabel;
    private String[][] btns;
    private static char crossSymbol = 'X';
    private static char zeroSymbol = 'O';

    private boolean isPlayerX = true;
    private boolean isPlayerO = false;

    HashMap<Integer, Character> map = new HashMap<>();

    public int turnCount = 0;

    public boolean XTurn = true;
    public boolean YTurn = false;

    private static char lastSymbol;
    private static ArrayList<Character> turns = new ArrayList<>();
    private TCPConnection connection;

    public static void main(String[] args) throws IOException {
        turns.add(crossSymbol);
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Socket socket = new Socket(Protocol.IP, Protocol.PORT);
        connection = new TCPConnection(Client.this, socket);

        resultLabel = new Label();
        winnerLabel = new Label();
        btns = new String[3][3];
        KeyFrame keyFrame = new KeyFrame(Duration.millis(500),
                event -> {
                    for (int i = 0; i < btns.length; i++) {
                        for (int j = 0; j < btns[i].length; j++) {
                            if (btns[i][j] != null) {
                                buttons[i][j].setText(btns[i][j]);

                                String winner = Checker.check(buttons);
                                if (!winner.equals("")) {
                                    winnerLabel.setId("winner");
                                    winnerLabel.setText("ПОБЕДА");
                                    resultLabel.setId(winner);
                                    resultLabel.setText(winner);
                                } else {
                                    //TODO если ничья
                                }
                            }
                        }
                    }
                });

        Timeline timeline = new Timeline(keyFrame);
        timeline.setCycleCount(-1);
        timeline.play();

        createWindow(primaryStage);
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

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int x = i, y = j;
                buttons[i][j] = new Button("");
                grid.add(buttons[i][j], j, i);
                buttons[i][j].setPrefWidth(70);
                buttons[i][j].setPrefHeight(70);

                buttons[i][j].setOnAction(e -> {
                    if (resultLabel.getText().equals("")) {
                        if (resultLabel.getText().equals("")) {
                            if (buttons[x][y].getText().equals("")) {
                                connection.sendObject(x * 3 + y);
                            }
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

    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {

    }

    @Override
    public void onReceiveObject(TCPConnection tcpConnection, Object object) {
        Integer a = (Integer) object;
        int readInt = a % 10;
        int i = readInt / 3;
        int j = readInt % 3;

        if (a / 10 == 1) {
            btns[i][j] = (String.valueOf(crossSymbol));
        } else {
            btns[i][j] = (String.valueOf(zeroSymbol));
        }

    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {

    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {

    }
}

