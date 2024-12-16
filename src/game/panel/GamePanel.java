package game.panel;

import game.enums.Difficulty;
import game.enums.PanelType;
import game.GameFrame;
import game.enums.SoundEnum;
import game.resource.AudioPlayer;
import game.resource.ScoreRecord;
import game.resource.TextSource;
import game.character.Enemy;
import game.character.Character;

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
    private ImageIcon monster_weakIcon = new ImageIcon("src/images/monster_weak.png");
    private ImageIcon hostageIcon = new ImageIcon("src/images/hostage.jpg");
    private ImageIcon airplaneIcon = new ImageIcon("src/images/airplane.png");

    // Icon -> Image
    private Image backgroundImg = backgroundIcon.getImage();
    private Image walkingSoldierImg = walkingSoldierIcon.getImage();
    private Image monsterWeakImg = monster_weakIcon.getImage();
    private Image hostageImg = hostageIcon.getImage();
    private Image airplaneImg = airplaneIcon.getImage();

    // 기록 로딩
    private TextSource textSource = null;
    private ScoreRecord scoreRecord = null;

    // 게임 내 구성된 패널
    private ScorePanel scorePanel = null; // 점수 패널
    private StatusPanel statusPanel = null; // 상태 패널( 총알, 생명 )

    // 단어 입력 패널 생성
    private InputPanel inputPanel = new InputPanel();
    // 게임 시작 전 이름 입력하는 패널
    private InputNamePanel inputNamePanel = null;

    //캐릭터 객체 생성
    private Character character = null;
    // 인질은 위치로만 저장
    private Point hostage = null;

    //게임 동작을 위한 스레드들
    private MonsterMakingThread monsterMakingThread = null;
    private MonsterMovingThread monsterMovingThread = null;
    private EmergencyThread emergencyThread = null;

    //플레어어가 움직이는 스레드. 몬스터를 때릴 때마다 새로운 스레드 만듦
    private PlayerMovingThread playerMovingThread = null;

    //몬스터 저장 위한 Map key : 단어 , value : Enemy 객체
    private Map<String, Enemy> enemyMap = null;

    //난이도 객체 생성
    private Difficulty difficulty = null;

    //공습경보 상황인지 구분하기 위한 변수
    private boolean emergencyFlag = false;
    private AudioPlayer emergencySoundPlayer = null;

    private GameFrame gameFrame = null;

    // 생성자
    // 외부 패널들의 정보들을 가져오면서 생성
    public GamePanel(GameFrame gameFrame, TextSource textSource, ScorePanel scorePanel, StatusPanel statusPanel, InputNamePanel inputNamePanel, ScoreRecord scoreRecord) {
        this.gameFrame = gameFrame;
        // 생성자 호출 시 외부로부터 패널들을 주입받음
        this.inputNamePanel = inputNamePanel;
        this.textSource = textSource;
        this.scorePanel = scorePanel;
        this.statusPanel = statusPanel;
        this.scoreRecord = scoreRecord;

        setLayout(new BorderLayout());
        // 단어 입력 패널 배치
        add(inputPanel, BorderLayout.SOUTH);
        // 인질 생성
        makeHostage();
    }

    // 게임 시작 메서드 ( 사용자 이름 입력 패널에서 호출 )
    public void gameStart(){

        // 적들을 저장하기 위한
        // ConcurrentHashMap은 다중 스레드 환경에서 안전하게 사용할 수 있다. ( 해시 기반 )
        enemyMap = new ConcurrentHashMap<>();

        // 게임 동작에 필요한 스레드 객체 할당
        monsterMakingThread = new MonsterMakingThread(); // 몬스터 생성 스레드
        monsterMovingThread = new MonsterMovingThread(); // 몬스터 움직임 스레드
        emergencyThread = new EmergencyThread(); // 공습경보 실행 스레드

        //각 스레드 실행
        monsterMakingThread.start();
        monsterMovingThread.start();
        emergencyThread.start();
    }

    // 게임의 캐릭터 설정 ( 캐릭터 선택 패널에서 호출하여 여기서 할당 )
    public void setCharacter(Character character){
        this.character = character; // 캐릭터 선택 패널에서 선택된 character를 주입
        statusPanel.setCharacter(character); // 상태패널에 캐릭터의 정보(체력, 최대 총알 수, 리로딩 시간) 지정
        makePlayer(); // 캐릭터 생성 메서드
    }

    // 난이도 설정
    public void setDifficulty(Difficulty.DifficultyEnum difficulty){
        // 난이도 선택 패널에서 선택 난이도를 GamePane의 difficultiy에 적용
        this.difficulty = new Difficulty(difficulty);
    }

    // 게임 맵 내에 랜덤한 위치 구하는 메서드
    private int getRandomLocationInMap(){
        return (int) (Math.random() * 600) + 50; // 맵 크기 내에서 랜덤한 정수 반환
    }

    // 게임의 측면 중 랜덤한 위치 구하는 메서드
    private Point getRandomSideLocationInMap(){
        int edge = (int) (Math.random() * 4); // 왼쪽, 오른쪽, 위, 아래 중 하나 랜덤 선택
        int x, y; // 반환될 Point객체에 들어갈 x,y 좌표

        switch(edge) { // 랜덤 선택된 부분에 대한 switch문
            case 0: // 상단 측면
                x = getRandomLocationInMap(); // 랜덤 위치 반환
                y = 0;
                break;
            case 1: // 하단 측면
                x = getRandomLocationInMap();
                y = getHeight();
                break;
            case 2: // 좌측 측면
                x = 0;
                y = getRandomLocationInMap();
                break;
            case 3: // 우측 측면
                x = getWidth();
                y = getRandomLocationInMap();
                break;
            default: // 그 외에 값의 경우 (0,0)
                x = 0;
                y = 0;
        }

        //구한 좌표를 Point객체로 반환
        return new Point(x, y);
    }

    // 캐릭터 생성 메서드
    private void makePlayer() {
        // 캐릭터의 위치 지정
        // 캐릭터 클래스의 메서드 호출
        character.setPosition(new Point(getRandomLocationInMap(), getRandomLocationInMap()));
    }

    // 인질 생성
    private void makeHostage() {
        // 인질 위치를 랜덤으로 지정
        hostage = new Point( getRandomLocationInMap(), getRandomLocationInMap());
    }

    // 몬스터 생성
    private void makeMonster(){
        // 몬스터 위치 임의의 측면으로 지정
        Point randomLocation = getRandomSideLocationInMap();
        String randomString;

        do { // 일단 단어 랜덤 지정한 후에, 해당 단어가 할당된 몬스터 있는지 검사
            randomString = textSource.getRandomString(); // textSource로부터 words.txt에 저장된 랜덤 단어 가져옴
        }while(enemyMap.containsKey(randomString));
        // enemyMap에 key : 단어 , value : Enemy객체로 저장
        enemyMap.put(textSource.getRandomString(), new Enemy(difficulty.getMonsterLife(), randomLocation));
        repaint(); //그림 다시 그림
    }

    // 몬스터 이동
    private void moveMonsters(){

        // 게임 난이도에 따라 몬스터가 한 번 움직일 때의 거리 정의
        int moveDistance = difficulty.getMonsterMovingSpeed();
        // 몬스터가 플레이어에 부딪힐 때의 범위
        int crashXRange = 30;
        int crashYRange = 50;
        // player의 위치를 가져와 Point로 저장
        Point playerPos = character.getPosition();

        // 모든 몬스터에 대해 반복
        for (String word : enemyMap.keySet()) {

            // key(단어)를 통해 Enemy 객체 찾음
            Enemy enemy = enemyMap.get(word);
            Point enemyPosition = enemy.getPosition(); // Enemy객체를 통해 enemy의 위치 가져옴

            if(enemyPosition.x - playerPos.getX() < 0){ //플레이어보다 왼쪽에 있다면
                enemyPosition.x += moveDistance; // 오른쪽으로 이동
            }else { //플레이어보다 오른쪽에 있다면
                enemyPosition.x -= moveDistance; // 왼쪽으로 이동
            }

            if(enemyPosition.y - playerPos.getY() < 0){ //플레이어보다 위에 있다면
                enemyPosition.y += moveDistance; // 아래로 이동
            }else{ //플레이어보다 아래에 있다면
                enemyPosition.y -= moveDistance; // 위로 이동
            }

            // 플레이어와 닿을 떄
            // 몬스터가 한 번 움직일 때 x,y 여러 칸씩 움직이기 때문에 플레이어와 닿을 때를 구하기 위해서는
            // 몬스터가 ( 플레이어의 x,y 좌표 += crashRange ) 범위 안에 있어야 한다.
            if ((enemyPosition.x > playerPos.x - crashXRange && enemyPosition.x < playerPos.x + crashXRange)
                    && (enemyPosition.y > playerPos.y - crashYRange && enemyPosition.y < playerPos.y + crashYRange)) {

                // 몬스터가 플레이에 닿으면 해당 몬스터는 소멸
                enemyMap.remove(word); //map에서 제거
                statusPanel.playerDamaged(); //플레이어에게 데미지 줌 ( 생명 -1 ), statusPanel의 메서드 호출

                // 플레이어 사망
                if (statusPanel.getLife() <= 0) {
                    gameOver(scorePanel.getScore()); //게임오버 메서드 호출 ( 점수를 매개변수로 넣음 )
                }
            }
        }
        //화면 다시 그리기
        repaint();
    }

    // 플레이어 움직임
    private boolean movePlayer(Point destinationPos){

        // 플레이어의 위치 구함
        Point playerPos = character.getPosition();
        // 한 번 움직일 때의 거리
        int moveDistance = 1;

        // X 방향 이동
        if (playerPos.x < destinationPos.x) { // 목적지보다 왼쪽에 있을 때
            playerPos.x = Math.min(playerPos.x + moveDistance, destinationPos.x); // 오른쪽으로 이동
        } else if (playerPos.x > destinationPos.x) { // 목적지보다 오른쪽에 있을 떄
            playerPos.x = Math.max(playerPos.x - moveDistance, destinationPos.x); // 왼쪽으로 이동
        }

        // Y 방향 이동
        if (playerPos.y < destinationPos.y) { // 목적지보다 위에 있을 때
            playerPos.y = Math.min(playerPos.y + moveDistance, destinationPos.y); // 아래로 이동
        } else if (playerPos.y > destinationPos.y) { // 목적지보다 아래에 있을 때
            playerPos.y = Math.max(playerPos.y - moveDistance, destinationPos.y); // 위로 이동
        }

        // 플레이어가 목적지에 도달했는지 확인.
        boolean arrived = playerPos.equals(destinationPos);

        // 플레이어와 인질의 충돌 확인
        int collisionRange = 20; //충돌 범위
        if (Math.abs(hostage.x - playerPos.x) <= collisionRange // 플레이어와 인질 거리 차이의 절댓값으로 거리 구함
                && Math.abs(hostage.y - playerPos.y) <= collisionRange) {

            scorePanel.increaseScore(10); // 점수 증가
            makeHostage(); // 인질 위치 변경
            repaint(); // 그림 다시 그림
        }

        // 플레이어 움직이는 스레드 종료할 지 판단하기 위해 반환
        return arrived;
    }

    // 게임 오버 ( 점수를 매개변수로 받음 )
    private void gameOver(int score){

        /*종료할 때는 돌아가고있는 스레드들 종료하고, 점수를 기록*/

        // 몬스터 이동, 생성 스레드 종료
        monsterMakingThread.interrupt();
        monsterMovingThread.interrupt();
        // 공습경보 스레드 종료
        emergencyThread.interrupt();
        // 이름&점수 기록
        scoreRecord.recordScore(inputNamePanel.getName(), score, difficulty);
        // bgm 재생
        gameFrame.startBGM();
        // 랭킹 화면으로 이동
        gameFrame.changePanel(PanelType.RANKING);
    }

    // 몬스터 때리는 메서드
    private void hitEnemy(String word){

        AudioPlayer audioPlayer = new AudioPlayer(SoundEnum.GUN);
        audioPlayer.playSoundAsync();
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

    // 단어를 입력하기 위한 패널
    class InputPanel extends JPanel {

        //최대 단어 20인 JTextField 생성
        private JTextField tf = new JTextField(20);

        public InputPanel() {
            tf.setHorizontalAlignment(SwingConstants.CENTER); //textfield 내의 글씨를 가운데 정렬
            add(tf); // JTextField 배치

            // enter 쳤을 때 작동하도록 리스너 달아줌
            tf.addActionListener( new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {

                    //JTextField의 값 가져옴
                    JTextField t = (JTextField) e.getSource();
                    String text = t.getText();

                    //단어가 공백이면 작동 안 함
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

    //5초마다 몬스터 하나씩 증가하는 스레드
    class MonsterMakingThread extends Thread {

        @Override
        public void run() { // 스레드 실행 작동
            //5초마다 반복하며 몬스터 생성
            while (true) {
                makeMonster(); // 몬스터 생성 메서드 호출
                try{
                    sleep(5000); //5초 sleep
                }catch(InterruptedException e){
                    // 예외 발생 시 리턴
                    return;
                }
            }
        }
    }

    //모든 몬스터가 플레이어 방향으로 움직이는 스레드
    class MonsterMovingThread extends Thread {

        @Override
        public void run() {
            // 모든 몬스터가 100ms마다 플레이어 방향으로 이동함
            while (true) {
                moveMonsters();
                try{
                    sleep(100); // 0.1초 sleep
                }catch(InterruptedException e){
                    return; // 예외 발생 시 return
                }
            }
        }
    }

    // 공습경보 스레드
    class EmergencyThread extends Thread {

        // 비행기 정보
        private int x;
        private int y;
        // 랜덤한 타이밍을 저장하는 변수
        private int randTiming;

        private EmergencyThread() {
            x=0; y=1000; // 스레드 처음에 생성할 때는 보이지 않는 곳에 비행기 숨김
        }

        @Override // 스레드 실행 시 작동
        public void run() {

            while(true){

                try {
                    randTiming = (int) (Math.random() * 30000); // 0~30초 중 랜덤한 시기 정해서 변수에 저장
                    // 시작점
                    x = getRandomLocationInMap(); // x좌표는 랜덤
                    y = 800; // 시작점 y좌표

                    sleep(randTiming); //난수만큼 sleep. sleep 후에 공습경보 시작
                    emergencyFlag = false; //공습경보 발생시 특정 단어 입력했는지 검사하기 위한 flag false로 초기화

                    // 공습경보
                    // 오디오 플레이어 객체 생성, 비행기 사운드로 지정
                    emergencySoundPlayer = new AudioPlayer(SoundEnum.AIRPLANE);
                    emergencySoundPlayer.playSoundAsync(); //사운드 실행
                    // 맨 위로 올라갈 때까지 계속 반복
                    while (y > 0) {
                        sleep(7); // 7ms마다 반복
                        y -= 1; // 비행기 위치 위로 1픽셀
                        repaint(); // 비행기 위치를 갱신하기 위해 다시 그림
                    }
                    emergencySoundPlayer.stopSound();
                    // 공습경보 끝날 때까지 "공습경보" or "emergency" 입력하지 못했다면?
                    // true이면 특정 단어 입력 성공한거임
                    if(!emergencyFlag){
                        emergencySoundPlayer.stopSound(); //비행기 사운드 재생 멈춤
                        //게임 종료 및 점수 0점으로 마무리
                        gameOver(0);
                    }

                } catch (InterruptedException e) {
                    return; // 예외 발생 시 return
                }

            }

        }
    }

    // 플레이어가 한 번 움직일 떄 모습을 나타내기 위한 스레드
    class PlayerMovingThread extends Thread {

        @Override // 스레드 실행 시 작동
        public void run() {

            // 한 번 이동할 때 x,y 50픽셀씩 이동
            int distance = 50;
            Point playerPos = character.getPosition(); // 플레이어 위치 저장

            // movePlayer() 메서드에 매개변수로 넘기기 위한 목적지 Point 생성. 플레이어의 위치로 처음에 지정
            Point destinationPos = new Point(playerPos.x, playerPos.y);
            boolean arriveFlag = false; // 목적지에 도착했는지 체크하는 flag

            // 플레이어와 인질의 방향 관계에 따라 목적지 설정
            if (playerPos.x < hostage.x) { // 플레이어가 왼쪽에 있는 경우
                destinationPos.x += distance; // 목적지x += distance
            }else if(playerPos.x > hostage.x) { // 플레이어가 오른쪽에 있는 경우
                destinationPos.x -= distance; // 목적지x += distance
            }

            if (playerPos.y < hostage.y) { // 플레이어가 위에 있는 경우
                destinationPos.y += distance; // 목적지y += distance
            } else if (playerPos.y > hostage.y) { // 플레이어가 아래에 있는 경우
                destinationPos.y -= distance; // 목적지y -= distance
            }

            // 목적지에 도착하기까지 반복
            while (!arriveFlag) {
                try {
                    sleep(10); // 10ms마다 반복
                    // 플레이어 이동하고, 목적지에 도착했는지 flag에 저장
                    arriveFlag = movePlayer(destinationPos);
                    repaint(); // 플레이어 위치 갱신 위해 다시 그림
                } catch (InterruptedException e) {
                    return; // 예외 발생 시 return
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
        setFont(new Font("Arial", Font.BOLD, 20)); // font 설정
        // 모든 몬스터에 대해 반복
        for (String word : enemyMap.keySet()) {
            Enemy enemy = enemyMap.get(word); //몬스터 객체 가져옴
            g.drawImage(monsterWeakImg, enemy.getPosition().x, enemy.getPosition().y, 100, 100, null); // 몬스터 그림
            setForeground(Color.WHITE); // 몬스터 글씨 색 White
            g.drawString(word, enemy.getPosition().x+20, enemy.getPosition().y); // 몬스터 글씨 그림
        }

    }
}
