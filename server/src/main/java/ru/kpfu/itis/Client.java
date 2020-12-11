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

public class Client extends Application implements ConnectionListener {

    private Button[][] buttons;
    public Label winnerLabel;
    public Label resultLabel;
    private String[][] btns;
    private static char crossSymbol = 'X';
    private static char zeroSymbol = 'O';

    private boolean isPlayerX = true;
    private boolean isPlayerO = false;
    private boolean yourTurn = false;

    public int turnCount = 0;

    public int XTurnCount;
    public int OTurnCount;

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
                                turnCount++;
                                String winner = Checker.check(buttons);
                                if (!winner.equals("")) {
                                    winnerLabel.setId("winner");
                                    winnerLabel.setText("ПОБЕДА");
                                    resultLabel.setId(winner);
                                    resultLabel.setText(winner);
                                } else if (XTurnCount == 5 || OTurnCount == 5) {
                                    winnerLabel.setId("winner");
                                    winnerLabel.setText("НИЧЬЯ");
                                    resultLabel.setId(winner);
                                    resultLabel.setText(winner);
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


        quit.setOnAction(e -> System.exit(0));

        quit.setPrefWidth(100);
        HBox.setMargin(quit, new Insets(10, 0, 10, 70));
        VBox.setMargin(grid, new Insets(20, 20, 20, 20));
        top.getChildren().addAll(quit);
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
            XTurnCount++;

        } else {
            btns[i][j] = (String.valueOf(zeroSymbol));
            OTurnCount++;
        }
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {
        throw new UnsupportedOperationException();
    }
}

