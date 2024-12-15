package panel;

import enums.Difficulty;
import enums.Panels;
import frame.GameFrame;
import resource.ScoreRecord;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class RankingPanel extends BasePanel{

    private ImageIcon backgroundIcon = new ImageIcon("src/images/rankingBackground.png");
    private ImageIcon previousIcon = new ImageIcon("src/images/previousButton.png");

    private Image backgroundImg = backgroundIcon.getImage();
    private Difficulty difficulty = new Difficulty(Difficulty.DifficultyEnum.EASY);

    private SelectDifficultyPanel selectDifficultyPanel = new SelectDifficultyPanel();
    private ScoreRecord scoreRecord = null;

    private JLabel previousLabel = new JLabel(previousIcon);

    public RankingPanel(GameFrame gameFrame, ScoreRecord scoreRecord) {

        super(gameFrame);
        setLayout(null);
        this.scoreRecord = scoreRecord;

        // 이전 버튼. 난이도 설정으로 이동
        putChangePanelButton(this, previousLabel, Panels.HOME, 100, 650);

        // 난이도 선택 패널 버튼
        selectDifficultyPanel.setLocation(0,0); // 패널 위치 설정
        selectDifficultyPanel.setSize(1200, 35); // 패널 크기 설정
        add(selectDifficultyPanel); // 패널 배치
    }

    class SelectDifficultyPanel extends JPanel{

        JButton easyButton = new JButton("Easy");
        JButton normalButton = new JButton("Normal");
        JButton hardButton = new JButton("Hard");

        public SelectDifficultyPanel() {
            setOpaque(false); // 배경 투명하게 변경
            // 난이도 선택 버튼 배치
            add (easyButton);
            add (normalButton);
            add (hardButton);
            // 버튼별로 난이도 설정 ( 마우스 이벤트 리스너 )
            addDifficultySelectListener(easyButton, Difficulty.DifficultyEnum.EASY);
            addDifficultySelectListener(normalButton, Difficulty.DifficultyEnum.NORMAL);
            addDifficultySelectListener(hardButton, Difficulty.DifficultyEnum.HARD);
        }
    }

    private void addDifficultySelectListener(JButton jButton, Difficulty.DifficultyEnum settingDifficulty) {
        jButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                difficulty.setDifficultyEnum(settingDifficulty);
                repaint();
            }
        });
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImg, 0,0, this.getWidth(), this.getHeight(),0,0, backgroundIcon.getIconWidth(), backgroundIcon.getIconHeight()-100, null);
        List<String[]> top5 = scoreRecord.getTop5(difficulty);
        // 랭킹 출력
        for (int i = 0; i < top5.size(); i++) {
            String[] strings = top5.get(i);
            String name = strings[0];
            String score = strings[1];
            setFont(new Font("Arial", Font.BOLD, 50));
            setForeground(new Color(237, 236, 236, 194));
            g.drawString(name, 550, 380+((i)*85));
            g.drawString(score+"점", 750, 380+((i)*85));
        }
    }
}
