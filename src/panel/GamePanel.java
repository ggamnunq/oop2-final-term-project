package panel;

import resource.TextSource;
import character.Enemy;
import character.John;
import character.Player;

import javax.swing.*;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

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

    private TextSource textSource = null;
    private ScorePanel scorePanel = null;
    private InputPanel inputPanel = new InputPanel();

    private Player player = null;
    private Point hostage = null;

    private MonsterMakingThread monsterMakingThread = new MonsterMakingThread();
    private MonsterMovingThread monsterMovingThread = new MonsterMovingThread();

    private Map<String, Enemy> enemyMap = new HashMap<>();

    public GamePanel(TextSource textSource, ScorePanel scorePanel) {

        this.textSource = textSource;
        this.scorePanel = scorePanel;

        setLayout(new BorderLayout());
        inputPanel.setLocation(200, 200);
        add(inputPanel, BorderLayout.SOUTH);

        makePlayer();
        makeHostage();

        monsterMakingThread.start();
        monsterMovingThread.start();
    }

    private int randomLocationInMap(){
        return (int) (Math.random() * 600) + 50;
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
        String randomString = textSource.getRandomString();
        while (enemyMap.containsKey(randomString)) {
            randomString = textSource.getRandomString();
        }
        enemyMap.put(textSource.getRandomString(), new Enemy(2,x,y));
    }

    private void moveMonsters(){

        int playerX = player.getX();
        int playerY = player.getY();

        for (String word : enemyMap.keySet()) {

            Enemy enemy = enemyMap.get(word);

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

    // 몬스터 때리는 메서드
    private boolean hitEnemy(String word){

        Enemy enemy = enemyMap.get(word);
        enemy.damage(1);
        repaint();

        enemyMap.remove(word);
        if (enemy.getLife() > 0) { //때렸는데도 몬스터 살이있는 경우 몬스터에 할당된 단어 변경
            enemyMap.put(textSource.getRandomString(), enemy);
        }else{ //때려서 몬스터가 죽은 경우, 점수 증가
            scorePanel.increaseScore(1);
        }

        return true;
    }


    class InputPanel extends JPanel {

        private JTextField tf = new JTextField(20);

        public InputPanel() {
            //textfield 내의 글씨를 가운데 정렬
            tf.setHorizontalAlignment(SwingConstants.CENTER);
            add(tf);
            tf.addActionListener( new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    JTextField t = (JTextField) e.getSource();
                    String text = t.getText();
                    if (text.length() == 0) {
                        return;
                    }

                    // 단어에 해당하는 몬스터 존재할 경우, 몬스터 때림
                    if(enemyMap.containsKey(text)){
                        hitEnemy(text);
                    }
                    t.setText("");
                }

            });
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
                    sleep(200);
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
        for (String word : enemyMap.keySet()) {
            Enemy enemy = enemyMap.get(word);
            g.drawImage(monsterWeakImg, enemy.getX(), enemy.getY(), 100, 100, null);
            g.drawString(word, enemy.getX()+25, enemy.getY());
        }

    }

}
