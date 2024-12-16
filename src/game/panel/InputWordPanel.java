package game.panel;

import game.enums.PanelType;
import game.GameFrame;
import game.resource.TextSource;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// 추가할 단어 입력하는 패널
public class InputWordPanel extends BasePanel {

    // 이미지 불러옴
    private ImageIcon backgroundIcon = new ImageIcon("src/images/forest.jpg");
    private ImageIcon previousIcon = new ImageIcon("src/images/previousButton.png");

    // 배경화면 ImageIcon -> Image
    private Image backgroundImg = backgroundIcon.getImage();

    // 단어 기록 관리하는 클래스
    private TextSource textSource = null;
    // 단어 입력하는 JTextField
    private JTextField inputField = new JTextField();

    // 이전으로 가는 Label
    private JLabel previousLabel = new JLabel(previousIcon);
    // 새로운 단어 입력하라는 안내문
    private JLabel instructionLabel = new JLabel("새로운 단어를 입력하세요!", SwingConstants.CENTER);

    //생성자
    public InputWordPanel(GameFrame gameFrame, TextSource textSource) {
        super(gameFrame); // BasePanel 생성자
        this.textSource = textSource; // 매개변수로 넘어온 Textsource 객체 주입

        // 버튼 임의 위치에 배치하기 위해 layout null 설정
        setLayout(null);

        // 이전 버튼. 난이도 설정으로 이동
        putChangePanelButton(this, previousLabel, PanelType.HOME, 100, 650);

        // 단어 입력 안내 JLabel 추가
        instructionLabel.setBounds(220, 330, 800, 50); // JTextField 위에 배치
        instructionLabel.setFont(new Font("Arial", Font.BOLD, 50)); // fond 설정
        instructionLabel.setForeground(Color.WHITE); // 텍스트 색상 설정
        add(instructionLabel); // 라벨에 추가

        // 단어 입력 JTextField 추가
        inputField.setBounds(450, 400, 300, 100); // x, y, width, height
        inputField.setBorder(new LineBorder(new Color(111, 106, 106, 149), 8)); // 테두리 설정
        inputField.setFont(new Font("Arial", Font.PLAIN, 40)); // 폰트 설정
        inputField.setHorizontalAlignment(JTextField.CENTER); // 텍스트 가운데 정렬
        add(inputField); // 라벨에 추가

        // inputField에 리스너 등록
        inputField.addActionListener(new ActionListener() {

            @Override // enter 치면 작동하
            public void actionPerformed(ActionEvent e) {
                // 입력된 문자열 값 불러옴
                JTextField t = (JTextField) e.getSource();
                String text = t.getText();

                // 문자열이 없다면 리턴
                if (text.length() == 0) {
                    return;
                }
                // TextSource 클래스의 단어 추가하는 메서드 호출
                textSource.addWord(text);
                //단어 입력 후에는 JTextField 비움
                t.setText("");
            }
        });

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //배경화면 그리기
        g.drawImage(backgroundImg, 0,0, this.getWidth(), this.getHeight(),0,0, backgroundIcon.getIconWidth(), backgroundIcon.getIconHeight()-100, null);

    }

}
