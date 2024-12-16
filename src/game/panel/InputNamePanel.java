package game.panel;

import game.enums.PanelType;
import game.GameFrame;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// 플레이어 이름 입력 위한 패널
public class InputNamePanel extends BasePanel {

    // 이미지 불러옴
    private ImageIcon backgroundIcon = new ImageIcon("src/images/forest.jpg");
    private ImageIcon gamestartButton = new ImageIcon("src/images/gamestartButton.png");
    private ImageIcon previousIcon = new ImageIcon("src/images/previousButton.png");

    // 배경화면 ImageIcon -> Image
    private Image backgroundImg = backgroundIcon.getImage();

    // 버튼들
    private JLabel gameStartButton = new JLabel(gamestartButton); // 게임시작 버튼
    private JLabel previousLabel = new JLabel(previousIcon); // 이전버튼

    // 플레이어 이름 입력하라는 안내문
    private JLabel instructionLabel = new JLabel("플레이어 이름을 입력하세요!", SwingConstants.CENTER); //
    // 이름 입력하는 JTextField
    private JTextField inputField = new JTextField();

    // 이름 String 초기화
    private String name = "";

    // 생성자
    public InputNamePanel(GameFrame gameFrame) {
        super(gameFrame); // BasePanel 생성자
        setLayout(null); // 버튼을 임의의 위치에 배치하기 위해 layout null로 설정

        // 이전 버튼. 난이도 설정으로 이동
        putChangePanelButton(this, previousLabel, PanelType.SELECT_DIFFICULTY, 100, 650);
        // 게임 시작 버튼. 게임 시작함
        putButton(this, gameStartButton, 900, 650);

        // 이름 입력 안내 JLabel 추가
        instructionLabel.setBounds(220, 330, 800, 50); // JTextField 위에 배치
        instructionLabel.setFont(new Font("Arial", Font.BOLD, 50)); // font 설정
        instructionLabel.setForeground(Color.WHITE); // 텍스트 색상 설정
        add(instructionLabel); // 라벨 추가

        // 이름 입력 JTextField 추가
        inputField.setBounds(450, 400, 300, 100); // x, y, width, height
        inputField.setBorder(new LineBorder(new Color(111, 106, 106, 149), 8)); // 테두리 설정
        inputField.setFont(new Font("Arial", Font.PLAIN, 40)); // 폰트 설정
        inputField.setHorizontalAlignment(JTextField.CENTER); // 텍스트 가운데 정렬
        add(inputField); // 라벨 추가

        // 게임시작 버튼 라벨에 마우스 리스너 추가
        gameStartButton.addMouseListener(new MouseAdapter() {

            @Override // 마우스 클릭시 작동
            public void mouseClicked(MouseEvent e) {

                // JTextField 값 . 좌우 공백 제거하여 가져옴
                name = inputField.getText().trim();

                // 가져온 값이 없으면 return
                if (name.length() == 0) {
                    return;
                }
                gameFrame.gameStart(); // 게임 시작
                inputField.setText(""); // JTextField 값 비움
            }

        });

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 배경 이미지 그리기
        g.drawImage(backgroundImg, 0, 0, this.getWidth(), this.getHeight(), 0, 0, backgroundIcon.getIconHeight(), backgroundIcon.getIconHeight() - 100, null);
    }

    // game over 후 점수 기록 할 때 사용
    public String getName(){
        return name; // 이름 return
    }

}
