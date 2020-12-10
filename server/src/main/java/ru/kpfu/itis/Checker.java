package ru.kpfu.itis;

import javafx.scene.control.Button;

public class Checker {
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
