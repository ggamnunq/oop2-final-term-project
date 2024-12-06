import resource.TextSource;
import panel.GamePanel;
import panel.ScorePanel;
import panel.StatusPanel;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    private TextSource textSource = new TextSource();
    private ScorePanel scorePanel = new ScorePanel();
    private StatusPanel statusPanel = new StatusPanel();
    private GamePanel gamePanel = new GamePanel(textSource, scorePanel);

    public GameFrame() {

        setTitle("게임");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        makeSplit();
        setVisible(true);
    }

    private void makeSplit() {
        JSplitPane hPane = new JSplitPane();
        hPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        hPane.setDividerLocation(800);
        hPane.setEnabled(false);
        getContentPane().add(hPane, BorderLayout.CENTER);

        JSplitPane vPane = new JSplitPane();
        vPane.setEnabled(false);
        vPane.setOrientation(JSplitPane.VERTICAL_SPLIT);

        hPane.setLeftComponent(gamePanel);
        hPane.setRightComponent(vPane);
        vPane.setDividerLocation(400);
        vPane.setTopComponent(scorePanel);
        vPane.setBottomComponent(statusPanel);

    }

    public static void main(String[] args) {
        new GameFrame();
    }

}
