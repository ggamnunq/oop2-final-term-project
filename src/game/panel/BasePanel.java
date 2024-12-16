package game.panel;

import game.enums.PanelType;
import game.GameFrame;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// 모든 패널의 공통된 기능들 담은 패널 클래스
public class BasePanel extends JPanel {

    // JFrame 객체 담음
    // 모든 패널들이 JFrame의 메서드를 쉽게 사용할 수 있도록 함
    private GameFrame gameFrame = null;

    //생성자
    public BasePanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
    }

    // 버튼 배치 메서드
    public void putButton(JPanel jpanel, JLabel jLabel, int xPos, int yPos) {
        jLabel.setSize(200,110);
        jLabel.setLocation(xPos, yPos);
        jpanel.add(jLabel);
    }

    // 패널 변경( 화면 전환 )
    public void changePanel(PanelType toPanel) {
        // GameFrame의 메서드 호출
        // 패널 변경 메서드
        gameFrame.changePanel(toPanel);
    }

    // 라벨을 클릭하면 패널 변경( 화면 전환 )
    public void addChangeFunctionListener(JLabel jLabel, PanelType toPanel){
        // 매개변수로 넘어온 label에 마우스 리스너 추가
        jLabel.addMouseListener(new MouseAdapter() {

            @Override // 마우스 클릭 시 작동
            public void mouseClicked(MouseEvent e) {
                // 패널 변경 메서드
                gameFrame.changePanel(toPanel);
            }

        });
    }

    // Label 추가&패널 변경 기능을 같이 실행하는 메서드
    public void putChangePanelButton(JPanel jpanel, JLabel jLabel, PanelType toPanel, int xPos, int yPos) {

        putButton(jpanel, jLabel, xPos, yPos); // Label 배치
        addChangeFunctionListener(jLabel, toPanel); // 매개변수로 넘어온 label에 패널 변경리스너 등록
    }

}
