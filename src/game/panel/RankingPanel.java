package game.panel;

import game.enums.Difficulty;
import game.enums.PanelType;
import game.GameFrame;
import game.resource.ScoreRecord;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

// 난이도별 랭킹 보는 패널
public class RankingPanel extends BasePanel{

    // 이미지 불러옴
    private ImageIcon backgroundIcon = new ImageIcon("src/images/rankingBackground.png");
    private ImageIcon homeIcon = new ImageIcon("src/images/homeButton.png");

    // 배경화면 ImageIcon -> Image
    private Image backgroundImg = backgroundIcon.getImage();

    // Difficulty 객체 생성. 초기 난이도는 EASY
    private Difficulty difficulty = new Difficulty(Difficulty.DifficultyEnum.EASY);

    // 화면 상단에서 난이도 선택할 수 있도록 하는 패널
    private SelectDifficultyPanel selectDifficultyPanel = new SelectDifficultyPanel();
    // 점수 기록 관리 클래스
    private ScoreRecord scoreRecord = null;
    // 홈으로 넘어가는 버튼 라벨 생성
    private JLabel homeLabel = new JLabel(homeIcon);

    // 생성자
    public RankingPanel(GameFrame gameFrame, ScoreRecord scoreRecord) {

        super(gameFrame); //BasePanel 생성자
        setLayout(null); // 라벨 위치를 임의로 정하기 위해 layout null
        this.scoreRecord = scoreRecord; // 매개변수로 넘어온 ScoreRecord 객체 주입

        // 이전 버튼. 난이도 설정으로 이동
        putChangePanelButton(this, homeLabel, PanelType.HOME, 100, 650);

        // 난이도 선택 패널 설정
        selectDifficultyPanel.setLocation(0,0); // 패널 위치 설정
        selectDifficultyPanel.setSize(1200, 35); // 패널 크기 설정
        add(selectDifficultyPanel); // 패널 배치
    }

    // 난이도 선택 버튼이 있는 패널
    class SelectDifficultyPanel extends JPanel{

        // 난이도별 버튼 생성
        private JButton easyButton = new JButton("Easy");
        private JButton normalButton = new JButton("Normal");
        private JButton hardButton = new JButton("Hard");

        // 생성자
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

    // 라벨에 리스너 등록하는 메서드. 난이도 선택 패널에서 호출
    private void addDifficultySelectListener(JButton jButton, Difficulty.DifficultyEnum settingDifficulty) {
        // 리스너 등록
        jButton.addMouseListener(new MouseAdapter() {
            @Override // 마우스 클릭 시 작동
            public void mouseClicked(MouseEvent e) {
                // 난이도 설정
                difficulty.setDifficultyEnum(settingDifficulty); // 난이도
                repaint(); // 다시 그림
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 배경화면 그리기
        g.drawImage(backgroundImg, 0,0, this.getWidth(), this.getHeight(),0,0, backgroundIcon.getIconWidth(), backgroundIcon.getIconHeight()-100, null);
        // ScoreRecord로부터 난이도에 해당하는 기록들 중 상위 5개 불러와 List에 저장
        List<String[]> top5 = scoreRecord.getTop5(difficulty);
        // 랭킹 출력
        for (int i = 0; i < top5.size(); i++) {

            String[] strings = top5.get(i); // 문자열 배열 저장

            // 배열에서 이름과 점수 불러옴
            String name = strings[0];
            String score = strings[1];

            setFont(new Font("Arial", Font.BOLD, 50)); // font 설정
            setForeground(new Color(237, 236, 236, 194)); // font 색상 설정
            g.drawString(name, 550, 380+((i)*85)); // 이름 그리기
            g.drawString(score+"점", 750, 380+((i)*85)); // 점수 그리기
        }
    }
}
