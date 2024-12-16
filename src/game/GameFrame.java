package game;

import game.enums.PanelType;
import game.enums.SoundEnum;
import game.panel.*;
import game.resource.AudioPlayer;
import game.resource.ScoreRecord;
import game.resource.TextSource;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    // 기록 다루는 클래스들
    private ScoreRecord scoreRecord = new ScoreRecord();
    private TextSource textSource = new TextSource();
    // 패널 생성
    private HomePanel homePanel = new HomePanel(this); // 홈화면 패널
    private HowToPlayPanel howToPlayPanel = new HowToPlayPanel(this); // 게임방법 패널
    private InputNamePanel inputNamePanel = new InputNamePanel(this); // 이름 입력 패널
    private ScorePanel scorePanel = new ScorePanel(); // 게임 내 점수 패널
    private StatusPanel statusPanel = new StatusPanel(); // 게임 내 상태 패널
    private GamePanel gamePanel = new GamePanel(this, textSource, scorePanel, statusPanel, inputNamePanel, scoreRecord); // 게임패널
    private SelectDifficultyPanel selectDifficultyPanel = new SelectDifficultyPanel(this, gamePanel); // 난이도 선택 패널
    private SelectCharacterPanel selectCharacterPanel = new SelectCharacterPanel(this, gamePanel); // 캐릭터 선택 패널
    private RankingPanel rankingPanel = new RankingPanel(this, scoreRecord); // 랭킹 패널
    private InputWordPanel inputWordPanel = new InputWordPanel(this, textSource); // 새로운 단어 추가 패널

    // 오디오 다루는 클래스
    private AudioPlayer bgmPlayer = null;

    // 생성자
    public GameFrame() {

        setTitle("라이언 일병 구하기"); // 프레임 제목
        setSize(1200, 800); // 프레이 사이즈
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 프레임 닫으면 프로그램 종료
        setResizable(false); // 사이즈 재설정 no
        // 화면의 가운데로 JFrame을 배치
        setLocationRelativeTo(null); // 프로그램 실행 시 화면의 가운데에 띄워지도록 설정
        setContentPane(homePanel); // 컨탠트팬 homePanel로 설정

        // 음악 실행
        bgmPlayer = new AudioPlayer(SoundEnum.BGM); // BGM 음악 객체 생성
        bgmPlayer.playSoundLoop(); // 음악 무한으로 재생

        setVisible(true); // 화면이 보이도록 함
    }

    // panel을 교체하는 메서드 ( enum으로 panel 종류를 구분 )
    public void changePanel(PanelType panel){

        // 매개변수로 넘어온 타입에 해당하는 패널로 컨탠트팬 설정
        switch (panel){
            case HOME: // 홈화면
                setContentPane(homePanel);
                break;
            case SELECT_CHARACTER: // 캐릭터 선택 화면
                setContentPane(selectCharacterPanel);
                break;
            case SELECT_DIFFICULTY: // 난이도 선택 화면
                setContentPane(selectDifficultyPanel);
                break;
            case GAME: //게임 화면
                setContentPane(makeSplit());
                break;
            case HOW_TO_PLAY: // 게임방법 화면
                setContentPane(howToPlayPanel);
                break;
            case INPUT_PLAYER_NAME: // 플레이어 이름 입력 화면
                setContentPane(inputNamePanel);
                break;
            case RANKING: // 랭킹 화면
                setContentPane(rankingPanel);
                break;
            case ADD_WORDS: // 새로운 단어 추가 화면
                setContentPane(inputWordPanel);
                break;
            default:
                // 찾는 enum 값이 없을 경우 예외 발생
                throw new IllegalArgumentException();

        }
        // 설정된 컨탠트팬이 보이도록 하는 메서드 호출
        showPanel();
    }

    // 게임을 시작하는 메서드
    public void gameStart(){
        changePanel(PanelType.GAME); // 화면을 게임화면으로 전환
        scorePanel.reset();
        bgmPlayer.stopSound();
        gamePanel.gameStart(); // gamePanel 내의 게임 시작 메서드 호출
    }

    //새로 설정된 패널이 제대로 적용되도록 revalidate()와 repaint() 호출
    public void showPanel(){
        revalidate(); // 컨테이너에 부착된 컴포넌트의 재배치 지시
        repaint(); // 컨테이너 다시 그리기 지시
    }

    // 화면을 분할하고 SplitPane 객체(제일 큰거)를 반환
    public JSplitPane makeSplit() {

        // 좌우로 분할하는 JSplitPane 생성
        JSplitPane hPane = new JSplitPane();
        hPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT); // 수평 분할 설정
        hPane.setDividerLocation(800); // 분할 위치를 800 픽셀로 설정
        hPane.setEnabled(false); // 크기 조절 불가능하도록 설정
        getContentPane().add(hPane, BorderLayout.CENTER); // JSplitPane을 메인 컨테이너에 추가

        // 위아래로 분할하는 JSplitPane 생성
        JSplitPane vPane = new JSplitPane();
        vPane.setEnabled(false); // 크기 조절 불가능하도록 설정
        vPane.setOrientation(JSplitPane.VERTICAL_SPLIT); // 수직 분할 설정

        hPane.setLeftComponent(gamePanel); // 왼쪽 영역에 gamePanel 추가
        hPane.setRightComponent(vPane); // 오른쪽 영역에 vPane 추가

        vPane.setDividerLocation(400); // 수직 분할 위치를 400 픽셀로 설정
        vPane.setTopComponent(scorePanel); // 우측 상단 영역에 scorePanel 추가
        vPane.setBottomComponent(statusPanel); // 우측 하단 영역에 statusPanel 추가

        return hPane; // 생성된 JSplitPane 반환
    }

    public void startBGM(){
        bgmPlayer.playSoundAsync();
    }

    public static void main(String[] args) {
        new GameFrame();
    }

}
