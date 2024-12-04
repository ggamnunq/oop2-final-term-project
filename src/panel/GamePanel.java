package panel;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    //이미지 파일 로딩
    private ImageIcon icon = new ImageIcon("src/images/forest.jpg");
    private Image backgroundImg = icon.getImage();
    private InputPanel inputPanel = new InputPanel();

    public GamePanel() {
        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.SOUTH);
    }
    
    class InputPanel extends JPanel {

        private JTextField tf = new JTextField(20);

        public InputPanel() {
            //textfield 내의 글씨를 가운데 정렬
            tf.setHorizontalAlignment(SwingConstants.CENTER);
            add(tf);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImg, 0,0, this.getWidth(), this.getHeight(), null);
    }

}
