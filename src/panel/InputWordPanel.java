package panel;

import enums.Panels;
import frame.GameFrame;
import resource.TextSource;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InputWordPanel extends BasePanel {

    private ImageIcon backgroundIcon = new ImageIcon("src/images/forest.jpg");
    private ImageIcon previousIcon = new ImageIcon("src/images/previousButton.png");
    private Image backgroundImg = backgroundIcon.getImage();

    private TextSource textSource = null;
    private JTextField inputField = new JTextField();

    private JLabel previousLabel = new JLabel(previousIcon);

    public InputWordPanel(GameFrame gameFrame, TextSource textSource) {
        super(gameFrame);
        this.textSource = textSource;

        setLayout(null);

        // 이전 버튼. 난이도 설정으로 이동
        putChangePanelButton(this, previousLabel, Panels.HOME, 100, 650);

        // 단어 입력 JTextField 추가
        inputField.setBounds(450, 400, 300, 100); // x, y, width, height
        inputField.setBorder(new LineBorder(new Color(111, 106, 106, 149), 8));
        inputField.setFont(new Font("Arial", Font.PLAIN, 40)); // 폰트 설정
        inputField.setHorizontalAlignment(JTextField.CENTER); // 텍스트 가운데 정렬
        add(inputField);

        // inputField ㅇㅔ ㄷㅏㄴㅇㅓ
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField t = (JTextField) e.getSource();
                String text = t.getText();
                if (text.length() == 0) {
                    return;
                }
                textSource.addWord(text);
                t.setText("");
            }
        });

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImg, 0,0, this.getWidth(), this.getHeight(),0,0, backgroundIcon.getIconWidth(), backgroundIcon.getIconHeight()-100, null);

    }

}
