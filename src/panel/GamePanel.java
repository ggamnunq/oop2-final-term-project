package panel;

import resource.ScoreRecord;
import resource.TextSource;
import character.Enemy;
import character.John;
import character.Player;

import javax.swing.*;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GamePanel extends JPanel {

    //이미지 파일 로딩
    private ImageIcon backgroundIcon = new ImageIcon("src/images/forest.jpg");
    private ImageIcon walkingSoldierIcon = new ImageIcon("src/images/walking_soldier.png");
    private ImageIcon shootingSoldierIcon = new ImageIcon("src/images/shooting_soldier.png");
    private ImageIcon monster_weakIcon = new ImageIcon("src/images/monster_weak.png");
    private ImageIcon hostageIcon = new ImageIcon("src/images/hostage.jpg");
    private ImageIcon airplaneIcon = new ImageIcon("src/images/airplane.png");
    private Image backgroundImg = backgroundIcon.getImage();
    private Image walkingSoldierImg = walkingSoldierIcon.getImage();
    private Image monsterWeakImg = monster_weakIcon.getImage();
    private Image hostageImg = hostageIcon.getImage();
    private Image airplaneImg = airplaneIcon.getImage();

    // 기록 로딩
    private TextSource textSource = null;
    private ScoreRecord scoreRecord = new ScoreRecord();

    // 패널들 로딩
    private ScorePanel scorePanel = null;
    private InputNamePanel inputNamePanel = null;
    private StatusPanel statusPanel = new StatusPanel();
    private InputPanel inputPanel = new InputPanel();

    private Player player = null;
    private Point hostage = null;

    private MonsterMakingThread monsterMakingThread = new MonsterMakingThread();
    private MonsterMovingThread monsterMovingThread = new MonsterMovingThread();
    private EmergencyThread emergencyThread = new EmergencyThread();

    private Map<String, Enemy> enemyMap = new ConcurrentHashMap<>();

    private boolean emergencyFlag = false;

    public GamePanel(TextSource textSource, ScorePanel scorePanel, StatusPanel statusPanel, InputNamePanel inputNamePanel) {

        this.scorePanel = scorePanel;
        this.statusPanel = statusPanel;
        this.inputNamePanel = inputNamePanel;

        this.textSource = textSource;

        setLayout(new BorderLayout());
        inputPanel.setLocation(200, 200);
        add(inputPanel, BorderLayout.SOUTH);

        makePlayer();
        makeHostage();

        monsterMakingThread.start();
        monsterMovingThread.start();
        emergencyThread.start();
    }

    private int getRandomLocationInMap(){
        return (int) (Math.random() * 600) + 50;
    }

    private void makePlayer() {

        player = new John();
        player.setX(getRandomLocationInMap());
        player.setY(getRandomLocationInMap());
    }

    private void makeHostage() {
        hostage = new Point( getRandomLocationInMap(), getRandomLocationInMap());
    }

    private void makeMonster(){
        int x = getRandomLocationInMap();
        int y = getRandomLocationInMap();
        String randomString = textSource.getRandomString();
        while (enemyMap.containsKey(randomString)) {
            randomString = textSource.getRandomString();
        }
        enemyMap.put(textSource.getRandomString(), new Enemy(2,x,y));
    }

    private void moveMonsters(){

        int moveDistance = 1;
        int crashXRange = 30;
        int crashYRange = 50;
        int playerX = player.getX();
        int playerY = player.getY();

        for (String word : enemyMap.keySet()) {

            Enemy enemy = enemyMap.get(word);

            int enemyX = enemy.getX();
            int enemyY = enemy.getY();

            if(enemyX - playerX < 0){ //플레이어보다 왼쪽에 있다면
                enemyX += moveDistance;
            }else { //플레이어보다 오른쪽에 있다면
                enemyX -= moveDistance;
            }

            if(enemyY - playerY < 0){ //플레이어보다 위에 있다면
                enemyY += moveDistance;
            }else{ //플레이어보다 아래에 있다면
                enemyY -= moveDistance;
            }

            // 플레이어와 닿을 떄
            // 몬스터가 한 번 움직일 때 x,y 3칸씩 움직이기 때문에 플레이어와 닿을 때를 구하기 위해서는
            // 몬스터가 ( 플레이어의 x,y 좌표 += crashRange ) 범위 안에 있어야 한다.
            if ((enemyX > playerX - crashXRange && enemyX < playerX + crashXRange)
                    && (enemyY > playerY - crashYRange && enemyY < playerY + crashYRange)) {
                enemyMap.remove(word);
                statusPanel.playerDamaged();

                // 플레이어 사망
                if (statusPanel.getLife() <= 0) {
                    gameOver(scorePanel.getScore());
                }

            }else{ //플레이어와 닿지 않을 때
                enemy.setLocation(enemyX, enemyY);
            }
        }
    }

    // 게임 오버
    private void gameOver(int score){

        // 몬스터 이동, 생성 스레드 종료
        monsterMakingThread.interrupt();
        monsterMovingThread.interrupt();
        // 공습경보 스레드 종료
        emergencyThread.interrupt();

        // 이름&점수 기록
        scoreRecord.recordScore(inputNamePanel.getName(), score);
    }

    // 몬스터 때리는 메서드
    private boolean hitEnemy(String word){

        Enemy enemy = enemyMap.get(word);
        enemy.damaged(1); // 몬스터에 데미지 입힘
        repaint();

        enemyMap.remove(word); // 때리면 word를 가진 몬스터가 사라짐

        if (enemy.getLife() > 0) { //때렸는데도 몬스터 살이있는 경우
            // 몬스터에 할당된 단어 변경 ( map의 key만 다른 단어로 하고, Enemy 객체는 동일한 거를 넣음 -> 좌표 유지 )
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

                    // 총알이 있으면 일단 발사 -> 총알 감소 -> 몬스터 때림 -> 재장전
                    if (statusPanel.getCurrentBulletAmount() > 0) {

                        statusPanel.decreaseBullet(); //총알 감소(발사함)

                        // 공습경보 해제 위한 단어 입력 성공 시, emergencyFlag 로 바꿈 (공습경보 대처완료)
                        if (text.equals("공습경보") || text.equals("emgergency")) {
                            emergencyFlag = true;
                        }

                        // 단어에 해당하는 몬스터 존재하면 몬스터 때림
                        if(enemyMap.containsKey(text) ){
                            hitEnemy(text); //몬스터 때림
                        }
                        // 총알 모두 소모 시 재장전
                        if (statusPanel.getCurrentBulletAmount() <= 0) {
                            statusPanel.reload();
                        }
                    }
                    // 입력 시 TextField 글씨 비움 ( 조건 상관 x )
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
                    sleep(5000);
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

    class EmergencyThread extends Thread {

        private int x;
        private int y;
        private int randTiming;

        private EmergencyThread() {
            x=0; y=1000; // 스레드 처음에 생성할 때는 비행기 숨김
        }

        @Override
        public void run() {

            while(true){

                try {
                    randTiming = (int) (Math.random() * 3000);
                    x = getRandomLocationInMap();
                    y = 800;
                    //난수만큼 sleep. sleep 후에 공습경보 시작
                    sleep(randTiming);
                    //공습경보 발생시 특정 단어 입력했는지 검사하기 위한 flag false로 초기화
                    emergencyFlag = false;
                    // 공습경보
                    while (y > 0) {
                        sleep(4);
                        y -= 1;
                        repaint();
                    }
                    // 공습경보 끝날 때까지 "공습경보" or "emergency" 입력하지 못했다면?
                    // true이면 특정 단어 입력 성공한거임
                    if(!emergencyFlag){
                        //게임 종료 및 점수 0점으로 마무리
                        gameOver(0);
                    }

                } catch (InterruptedException e) {
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
        //비행기 그리기
        g.drawImage(airplaneImg, emergencyThread.x, emergencyThread.y, 100,100,null);
        for (String word : enemyMap.keySet()) {
            Enemy enemy = enemyMap.get(word);
            g.drawImage(monsterWeakImg, enemy.getX(), enemy.getY(), 100, 100, null);
            g.drawString(word, enemy.getX()+25, enemy.getY());
        }
    }
}
