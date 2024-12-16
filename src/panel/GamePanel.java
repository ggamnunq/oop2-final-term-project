package panel;

import enums.Difficulty;
import enums.Panels;
import frame.GameFrame;
import resource.ScoreRecord;
import resource.TextSource;
import character.Enemy;
import character.Character;

import javax.swing.*;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GamePanel extends BasePanel {

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
    private ScoreRecord scoreRecord = null;

    // 패널들 로딩
    // 게임 내 구성된 패널
    private ScorePanel scorePanel = null;
    private StatusPanel statusPanel = null;
    private InputPanel inputPanel = new InputPanel();
    // 게임 시작 전 이름 입력하는 패널
    private InputNamePanel inputNamePanel = null;

    //캐릭터와 인질
    private Character character = null;
    private Point hostage = null;

    //게임 동작을 위한 스레드들
    private MonsterMakingThread monsterMakingThread = null;
    private MonsterMovingThread monsterMovingThread = null;
    private EmergencyThread emergencyThread = null;

    //플레어어가 움직이는 스레드. 몬스터를 때릴 때마다 새로운 스레드 만듦
    private PlayerMovingThread playerMovingThread = null;

    //몬스터 저장 위한 Map
    private Map<String, Enemy> enemyMap = null;

    private Difficulty difficulty = null;

    //공습경보 상황인지 구분하기 위한 변수
    private boolean emergencyFlag = false;

    public GamePanel(GameFrame gameFrame, TextSource textSource, ScorePanel scorePanel, StatusPanel statusPanel, InputNamePanel inputNamePanel, ScoreRecord scoreRecord) {
        super(gameFrame);
        this.inputNamePanel = inputNamePanel;
        this.textSource = textSource;
        this.scorePanel = scorePanel;
        this.statusPanel = statusPanel;
        this.scoreRecord = scoreRecord;

        setLayout(null);
        add(inputPanel, BorderLayout.SOUTH);
        makeHostage();
    }

    public void gameStart(){

        enemyMap = new ConcurrentHashMap<>();

        monsterMovingThread = new MonsterMovingThread();
        monsterMakingThread = new MonsterMakingThread();
        emergencyThread = new EmergencyThread();

        monsterMakingThread.start();
        monsterMovingThread.start();
        emergencyThread.start();
    }

    // 게임의 캐릭터 설정
    public void setCharacter(Character character){
        this.character = character;
        statusPanel.setCharacter(character);
        makePlayer();
    }

    // 난이도 설정
    public void setDifficulty(Difficulty.DifficultyEnum difficulty){
        // 게임 내부에
        this.difficulty = new Difficulty(difficulty);
    }

    private int getRandomLocationInMap(){
        return (int) (Math.random() * 600) + 50;
    }

    private Point getRandomSideLocationInMap(){
        int edge = (int) (Math.random() * 4);
        int x, y;

        switch(edge) {
            case 0:
                x = getRandomLocationInMap();
                y = 0;
                break;
            case 1:
                x = getRandomLocationInMap();
                y = getHeight();
                break;
            case 2:
                x = 0;
                y = getRandomLocationInMap();
                break;
            case 3:
                x = getWidth();
                y = getRandomLocationInMap();
                break;
            default:
                x = 0;
                y = 0;
        }

        return new Point(x, y);
    }

    private void makePlayer() {
        character.setPosition(new Point(getRandomLocationInMap(), getRandomLocationInMap()));
    }

    private void makeHostage() {
        hostage = new Point( getRandomLocationInMap(), getRandomLocationInMap());
    }

    private void makeMonster(){
        Point randomLocation = getRandomSideLocationInMap();
        String randomString = textSource.getRandomString();
        while (enemyMap.containsKey(randomString)) {
            randomString = textSource.getRandomString();
        }
        enemyMap.put(textSource.getRandomString(), new Enemy(difficulty.getMonsterLife(), randomLocation));
    }

    private void moveMonsters(){

        int moveDistance = difficulty.getMonsterMovingSpeed();
        int crashXRange = 30;
        int crashYRange = 50;
        Point playerPos = character.getPosition();

        for (String word : enemyMap.keySet()) {

            Enemy enemy = enemyMap.get(word);
            Point enemyPosition = enemy.getPosition();

            if(enemyPosition.x - playerPos.getX() < 0){ //플레이어보다 왼쪽에 있다면
                enemyPosition.x += moveDistance;
            }else { //플레이어보다 오른쪽에 있다면
                enemyPosition.x -= moveDistance;
            }

            if(enemyPosition.y - playerPos.getY() < 0){ //플레이어보다 위에 있다면
                enemyPosition.y += moveDistance;
            }else{ //플레이어보다 아래에 있다면
                enemyPosition.y -= moveDistance;
            }

            // 플레이어와 닿을 떄
            // 몬스터가 한 번 움직일 때 x,y 3칸씩 움직이기 때문에 플레이어와 닿을 때를 구하기 위해서는
            // 몬스터가 ( 플레이어의 x,y 좌표 += crashRange ) 범위 안에 있어야 한다.
            if ((enemyPosition.x > playerPos.x - crashXRange && enemyPosition.x < playerPos.x + crashXRange)
                    && (enemyPosition.y > playerPos.y - crashYRange && enemyPosition.y < playerPos.y + crashYRange)) {
                enemyMap.remove(word);
                statusPanel.playerDamaged();

                // 플레이어 사망
                if (statusPanel.getLife() <= 0) {
                    gameOver(scorePanel.getScore());
                }
            }
        }
        repaint();
    }

    // 플레이어 움직임
    private boolean movePlayer(Point destinationPos){

        Point playerPos = character.getPosition();
        int moveDistance = 1;

        if (playerPos.x < destinationPos.x) {
            playerPos.x += moveDistance;
        } else {
            playerPos.x -= moveDistance;
        }

        if (playerPos.y < destinationPos.y) {
            playerPos.y += moveDistance;
        }else{
            playerPos.y -= moveDistance;
        }

        int collisionRange = 20;
        if(playerPos.x == destinationPos.x && playerPos.y == destinationPos.y){
            if (Math.abs(hostage.x - playerPos.x) <= collisionRange
                    && Math.abs(hostage.y - playerPos.y) <= collisionRange) {
                scorePanel.increaseScore(10);
                hostage.x = getRandomLocationInMap();
                hostage.y = getRandomLocationInMap();
                repaint();
            }
            repaint();
            return true;
        }

        return false;
    }

    // 게임 오버
    private void gameOver(int score){

        // 몬스터 이동, 생성 스레드 종료
        monsterMakingThread.interrupt();
        monsterMovingThread.interrupt();
        // 공습경보 스레드 종료
        emergencyThread.interrupt();
        // 이름&점수 기록
        scoreRecord.recordScore(inputNamePanel.getName(), score, difficulty);
        // 랭킹 화면으로 이동
        changePanel(Panels.RANKING);
    }

    // 몬스터 때리는 메서드
    private void hitEnemy(String word){

        Enemy enemy = enemyMap.get(word);
        enemy.damaged(1); // 몬스터에 데미지 입힘
        repaint();

        enemyMap.remove(word); // 때리면 word를 가진 몬스터가 사라짐

        if (enemy.getLife() > 0) { //때렸는데도 몬스터 살이있는 경우
            // 몬스터에 할당된 단어 변경 ( map의 key만 다른 단어로 하고, Enemy 객체는 동일한 거를 넣음 -> 좌표 유지 )
            enemyMap.put(textSource.getRandomString(), enemy);
        }else{ //때려서 몬스터가 죽은 경우
            //점수 증가
            scorePanel.increaseScore(1);
            //플레이어가 인질 방향으로 움직임
            //playerThread가 null아 아닌지, run()메서드가 실행중이라면 interrupt하고 새로운 스레드를 생성하여 다시 움직인다.
            if (playerMovingThread != null && playerMovingThread.isAlive()) {
                playerMovingThread.interrupt();
            }
            playerMovingThread = new PlayerMovingThread();
            playerMovingThread.start();
        }

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
                        if (text.equals("공습경보") || text.equals("emergency")) {
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
                    randTiming = (int) (Math.random() * 30000);
                    x = getRandomLocationInMap();
                    y = 800;
                    //난수만큼 sleep. sleep 후에 공습경보 시작
                    sleep(randTiming);
                    //공습경보 발생시 특정 단어 입력했는지 검사하기 위한 flag false로 초기화
                    emergencyFlag = false;
                    // 공습경보
                    while (y > 0) {
                        sleep(7);
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

    class PlayerMovingThread extends Thread {

        @Override
        public void run() {

            int destination = 50;
            Point destinationPos = new Point();
            boolean arriveFlag = false;
            Point playerPos = character.getPosition();

            destinationPos.x = playerPos.x;
            destinationPos.y = playerPos.y;

            if (playerPos.x < hostage.x) {
                destinationPos.x += destination;
            }else if(playerPos.x > hostage.x) {
                destinationPos.x -= destination;
            }

            if (playerPos.y < hostage.y) {
                destinationPos.y += destination;
            } else if (playerPos.y > hostage.y) {
                destinationPos.y -= destination;
            }

            while (!arriveFlag) {
                try {
                    sleep(10);
                    arriveFlag = movePlayer(destinationPos);
                    repaint();
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
        g.drawImage(walkingSoldierImg, character.getPosition().x, character.getPosition().y, 150,150,null);
        //인질 캐릭터 그리기
        g.drawImage(hostageImg, hostage.x, hostage.y, 100,100,null);
        //비행기 그리기
        g.drawImage(airplaneImg, emergencyThread.x, emergencyThread.y, 100,100,null);

        //몬스터 그리기
        setFont(new Font("Arial", Font.BOLD, 20));
        for (String word : enemyMap.keySet()) {
            Enemy enemy = enemyMap.get(word);
            g.drawImage(monsterWeakImg, enemy.getPosition().x, enemy.getPosition().y, 100, 100, null);
            setForeground(Color.WHITE);
            g.drawString(word, enemy.getPosition().x+20, enemy.getPosition().y);
        }

    }
}
