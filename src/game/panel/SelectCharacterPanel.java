package game.panel;

import game.character.John;
import game.character.Michael;
import game.character.Milner;
import game.character.Character;
import game.enums.PanelType;
import game.GameFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SelectCharacterPanel extends BasePanel{

    // 이미지 로딩
    private ImageIcon backgroundIcon = new ImageIcon("src/images/characterSelectBackground.png");
    private ImageIcon selectButton = new ImageIcon("src/images/selectButton.png");
    private ImageIcon previousIcon = new ImageIcon("src/images/previousButton.png");
    private Image backgroundImg = backgroundIcon.getImage();

    // 이전 화면 버튼 라벨
    private JLabel previousLabel = new JLabel(previousIcon);
    // 캐릭터 선택 버튼 라벨
    private JLabel selectJohnButtonLabel = new JLabel(selectButton);
    private JLabel selectMichaelButtonLabel = new JLabel(selectButton);
    private JLabel selectMilnerButtonLabel = new JLabel(selectButton);

    //GamePanel. 처음엔 null
    private GamePanel gamePanel = null;

    // 생성자
    public SelectCharacterPanel(GameFrame gameFrame, GamePanel gamePanel) {

        super(gameFrame); // BasePanel 생성자
        this.gamePanel = gamePanel; // 매개변수로 넘어온 gamePanel을 주입

        setLayout(null); // 라벨을 임의의 위치에 배치하기 위해 layout null로 설정
        // 이전 버튼
        putChangePanelButton(this, previousLabel, PanelType.HOME, 100,650);
        // 캐릭터 선택 버튼 배치
        putButton(this, selectJohnButtonLabel, 150, 500);
        putButton(this, selectMichaelButtonLabel, 480, 500);
        putButton(this, selectMilnerButtonLabel, 820, 500);
        // 각 버튼에 캐릭터 선택하는 리스너 생성
        addCharacterSelectListener(selectJohnButtonLabel, new John());
        addCharacterSelectListener(selectMichaelButtonLabel, new Michael());
        addCharacterSelectListener(selectMilnerButtonLabel, new Milner());
    }

    // 캐릭터 선택하는 리스너 등록하는 메서드
    private void addCharacterSelectListener(JLabel jLabel, Character character){
        // 라벨에 마우스 리스너 등록
        jLabel.addMouseListener(new MouseAdapter() {

            @Override // 마우스 클릭 시 작동
            public void mouseClicked(MouseEvent e) {
                gamePanel.setCharacter(character); // 캐릭터 선택하는 메서드. StatusPanel에 캐릭터 정보 등록
                changePanel(PanelType.SELECT_DIFFICULTY); // 난이도 선택 패널로 전환
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //배경이미지 그리기
        g.drawImage(backgroundImg, 0,0, this.getWidth(), this.getHeight(),0,0, backgroundIcon.getIconWidth(), backgroundIcon.getIconHeight()-100, null);
    }
}
