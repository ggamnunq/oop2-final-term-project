package panel;

import enums.Panels;
import frame.GameFrame;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class InputNamePanel extends BasePanel {

    private ImageIcon backgroundIcon = new ImageIcon("src/images/forest.jpg");
    private ImageIcon gamestartButton = new ImageIcon("src/images/gamestartButton.png");
    private ImageIcon previousIcon = new ImageIcon("src/images/previousButton.png");

    private Image backgroundImg = backgroundIcon.getImage();

    private JLabel gameStartButton = new JLabel(gamestartButton);
    private JLabel previousLabel = new JLabel(previousIcon);
    private JTextField inputField = new JTextField();

    private String name = "";

    public InputNamePanel(GameFrame gameFrame) {
        super(gameFrame);
        setLayout(null);

        // 이전 버튼. 난이도 설정으로 이동
        putChangePanelButton(this, previousLabel, Panels.SELECT_DIFFICULTY, 100, 650);
        // 게임 시작 버튼. 게임 시작함
        putButton(this, gameStartButton, 900, 650);

        // 이름 입력 JTextField 추가
        inputField.setBounds(450, 400, 300, 100); // x, y, width, height
        inputField.setBorder(new LineBorder(new Color(111, 106, 106, 149), 8));
        inputField.setFont(new Font("Arial", Font.PLAIN, 40)); // 폰트 설정
        inputField.setHorizontalAlignment(JTextField.CENTER); // 텍스트 가운데 정렬
        add(inputField);

        gameStartButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

            name = inputField.getText().trim();
            if (name.length() == 0) {
                return;
            }
            gameFrame.gameStart();
            }
        });

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 배경 이미지 그리기
        g.drawImage(backgroundImg, 0, 0, this.getWidth(), this.getHeight(), 0, 0, backgroundIcon.getIconHeight(), backgroundIcon.getIconHeight() - 100, null);
    }

    public String getName(){
        return name;
    }

}
