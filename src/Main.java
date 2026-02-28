import javax.swing.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Raycast 01");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GameWindow game = new GameWindow();
        frame.add(game);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        game.requestFocusInWindow();
    }
}