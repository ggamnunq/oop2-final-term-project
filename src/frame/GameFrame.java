package frame;

import enums.Panels;
import panel.*;
import resource.TextSource;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    private HomePanel homePanel = new HomePanel(this);
    private HowToPlayPanel howToPlayPanel = new HowToPlayPanel(this);
    private InputNamePanel inputNamePanel = new InputNamePanel(this);
    private TextSource textSource = new TextSource();
    private ScorePanel scorePanel = new ScorePanel();
    private StatusPanel statusPanel = new StatusPanel();
    private GamePanel gamePanel = new GamePanel(textSource, scorePanel, statusPanel, inputNamePanel);
    private SelectDifficultyPanel selectDifficultyPanel = new SelectDifficultyPanel(this, gamePanel);
    private SelectCharacterPanel selectCharacterPanel = new SelectCharacterPanel(this, gamePanel);

    public GameFrame() {

        setTitle("게임");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setContentPane(homePanel);
        setVisible(true);
    }

    // panel을 교체함 ( enum으로 panel 종류를 구분 )
    public void changePanel(Panels panel){

        JPanel selectedPanel = null;
        switch (panel){
            case HOME: //
                setContentPane(homePanel);
                break;
            case SELECT_CHARACTER:
                setContentPane(selectCharacterPanel);
                break;
            case SELECT_DIFFICULTY:
                setContentPane(selectDifficultyPanel);
                break;
            case GAME:
                setContentPane(makeSplit());
                break;
            case HOW_TO_PLAY:
                setContentPane(howToPlayPanel);
                break;
            case INPUT_PLAYER_NAME:
                setContentPane(inputNamePanel);
                break;
            default:
                // 찾는 enum 값이 없을 경우 예외 발생
                throw new IllegalArgumentException();

        }
        showPanel();
    }

    public void gameStart(){
        changePanel(Panels.GAME);
        gamePanel.gameStart();
    }

    //새로 설정된 패널이 제대로 적용되도록 revalidate()와 repaint() 호출
    public void showPanel(){
        revalidate(); // 컨테이너에 부착된 컴포넌트의 재배치 지시
        repaint(); // 컨테이너 다시 그리기 지시
    }

    // 화면을 분할하고 SplitPane 객체(제일 큰거)를 반환
    public JSplitPane makeSplit() {

        JSplitPane hPane = new JSplitPane();
        hPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        hPane.setDividerLocation(800);
        hPane.setEnabled(false); //크기 조절 불가
        getContentPane().add(hPane, BorderLayout.CENTER);

        JSplitPane vPane = new JSplitPane();
        vPane.setEnabled(false);
        vPane.setOrientation(JSplitPane.VERTICAL_SPLIT);

        //게임 시작 버튼을 누르는 시기에 GamePanel 객체를 만들도록 함
        hPane.setLeftComponent(gamePanel);
        hPane.setRightComponent(vPane);
        vPane.setDividerLocation(400);
        vPane.setTopComponent(scorePanel);
        vPane.setBottomComponent(statusPanel);

        return hPane;
    }

    public static void main(String[] args) {
        new GameFrame();
    }

}
