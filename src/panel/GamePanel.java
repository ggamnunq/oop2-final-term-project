package panel;

import character.Enemy;
import character.John;
import character.Player;

import javax.swing.*;
import java.awt.*;

import java.util.Vector;

public class GamePanel extends JPanel {

    //이미지 파일 로딩
    private ImageIcon backgroundIcon = new ImageIcon("src/images/forest.jpg");
    private ImageIcon walkingSoldierIcon = new ImageIcon("src/images/walking_soldier.png");
    private ImageIcon shootingSoldierIcon = new ImageIcon("src/images/shooting_soldier.png");
    private ImageIcon monster_weakIcon = new ImageIcon("src/images/monster_weak.png");
    private ImageIcon hostageIcon = new ImageIcon("src/images/hostage.jpg");
    private Image backgroundImg = backgroundIcon.getImage();
    private Image walkingSoldierImg = walkingSoldierIcon.getImage();
    private Image monsterWeakImg = monster_weakIcon.getImage();
    private Image hostageImg = hostageIcon.getImage();

    private InputPanel inputPanel = new InputPanel();

    private Player player = null;
    private Point hostage = null;

    private MonsterMakingThread monsterMakingThread = new MonsterMakingThread();
    private MonsterMovingThread monsterMovingThread = new MonsterMovingThread();


    Vector<Enemy> enemies = new Vector<>();

    public GamePanel() {
        setLayout(new BorderLayout());
        inputPanel.setLocation(200, 200);
        add(inputPanel, BorderLayout.SOUTH);

        makePlayer();
        makeHostage();
        monsterMakingThread.start();
        monsterMovingThread.start();
    }

    public int randomLocationInMap(){
        return (int) (Math.random() * 600) + 100;
    }

    private void makePlayer() {

        player = new John();
        player.setX(randomLocationInMap());
        player.setY(randomLocationInMap());

    }

    private void makeHostage() {
        hostage = new Point( randomLocationInMap(), randomLocationInMap());
    }

    private void makeMonster(){
        int x = randomLocationInMap();
        int y = randomLocationInMap();
        enemies.add(new Enemy(1, x, y, "temp"));
    }

    private void moveMonsters(){

        int playerX = player.getX();
        int playerY = player.getY();

        for (Enemy enemy : enemies) {
            int enemyX = enemy.getX();
            int enemyY = enemy.getY();
            if(enemyX - playerX < 0){ //플레이어보다 왼쪽에 있다면
                enemyX += 3;
            }else { //플레이어보다 오른쪽에 있다면
                enemyX -= 3;
            }

            if(enemyY - playerY < 0){ //플레이어보다 위에 있다면
                enemyY += 3;
            }else{ //플레이어보다 아래에 있다면
                enemyY -= 3;
            }
            enemy.setLocation(enemyX, enemyY);
        }

    }

    class InputPanel extends JPanel {

        private JTextField tf = new JTextField(20);

        public InputPanel() {
            //textfield 내의 글씨를 가운데 정렬
            tf.setHorizontalAlignment(SwingConstants.CENTER);
            add(tf);
        }
    }



    //3초마다 몬스터 하나씩 증가하는 스레드
    class MonsterMakingThread extends Thread {

        private int monsterCount = 0;

        @Override
        public void run() {
            while (true) {
                makeMonster();
                repaint();
                try{
                    sleep(3000);
                }catch(InterruptedException e){
                    return;
                }
            }
        }

    }

    //모든 몬스터가 플레이어 방향으로 움직이는 스레드
    class MonsterMovingThread extends Thread {

        @Override
        public void run() {
            while (true) {
                moveMonsters();
                repaint();
                try{
                    sleep(100);
                }catch(InterruptedException e){
                    return;
                }
            }
        }

    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //배경이미지 그리기
        g.drawImage(backgroundImg, 0,0, this.getWidth(), this.getHeight(), null);
        //플레이어 그리기
        g.drawImage(walkingSoldierImg, player.getX(), player.getY(), 150,150,null);
        //인질 캐릭터 그리기
        g.drawImage(hostageImg, hostage.x, hostage.y, 100,100,null);
        for (Enemy enemy : enemies) {
            g.drawImage(monsterWeakImg, enemy.getX(), enemy.getY(), 100, 100, null);
            g.drawString(enemy.getWord(), enemy.getX()+50, enemy.getY()+50);
        }

    }

}
