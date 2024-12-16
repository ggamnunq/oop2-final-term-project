package game.resource;
import game.enums.SoundEnum;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioPlayer {

    // 경로 설정: 각 사운드 파일의 위치
    private String bgmPath = "src/game/resource/bgm.wav";
    private String gunPath = "src/game/resource/gunShotSound.wav";
    private String airplanePath = "src/game/resource/airplane.wav";

    // Clip 객체는 사운드의 재생을 관리
    private Clip clip;
    private File soundFile = null;

    // 사운드 종류를 구분하는 열거형
    private SoundEnum soundEnum = null;

    // 생성자: 사운드 종류에 따라 파일을 설정
    public AudioPlayer(SoundEnum soundEnum) {
        this.soundEnum = soundEnum;
        switch (soundEnum) {
            case BGM:
                this.soundFile =  new File(bgmPath);  // 배경 음악 파일 경로 설정
                break;
            case GUN:
                this.soundFile =  new File(gunPath);  // 총격 소리 파일 경로 설정
                break;
            case AIRPLANE:
                this.soundFile =  new File(airplanePath);  // 비행기 소리 파일 경로 설정
                break;
        }
    }

    // 비동기적으로 사운드를 재생하는 메서드
    public void playSoundAsync() {
        // 별도의 스레드에서 사운드 재생을 처리
        new Thread(this::playSound).start();
    }

    // 사운드를 재생하는 메서드
    public void playSound() {
        try {
            // AudioInputStream을 사용하여 사운드 파일을 읽어들임
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();  // Clip 객체를 생성하여 사운드를 처리
            clip.open(audioStream);  // 오디오 스트림을 clip에 연결

            // 볼륨 조절
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float volume = -20.0f;  // 볼륨 레벨 설정 (음악의 소리 크기를 조절)
            volumeControl.setValue(volume);  // 설정한 볼륨으로 조정

            clip.start();  // 사운드 시작
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            return; // 예외 발생 시 return
        }
    }


    // 사운드를 무한 반복 재생하는 메서드
    public void playSoundLoop() {
        try {
            // AudioInputStream을 사용하여 사운드 파일을 읽어들임
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();  // Clip 객체를 생성하여 사운드를 처리
            clip.open(audioStream);  // 오디오 스트림을 clip에 연결

            // 볼륨 조절
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float volume = -20.0f;  // 볼륨 레벨 설정 (음악의 소리 크기를 조절)
            volumeControl.setValue(volume);  // 설정한 볼륨으로 조정

            clip.loop(Clip.LOOP_CONTINUOUSLY);  // 사운드 무한 반복
            clip.start();  // 사운드 시작
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            return; // 예외 발생 시 return
        }
    }

    // 사운드를 중지하는 메서드
    public void stopSound() {
        // clip이 실행 중이면 중지
        if (clip != null && clip.isRunning()) {
            clip.stop();  // 사운드 재생 중지
        }
    }
}
