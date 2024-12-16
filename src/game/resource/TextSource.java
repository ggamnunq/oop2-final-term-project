package game.resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

// 텍스트 리소스를 관리하는 클래스
public class TextSource {

    private File file = null; // 단어를 저장할 파일
    private FileReader reader = null; // 파일 읽기 스트림
    private Vector<String> strings = new Vector<>(); // 파일에서 읽은 단어들을 저장할 벡터 생성

    // 생성자: 파일에서 단어를 읽어 벡터에 저장
    public TextSource() {
        try {
            this.file = new File("src/game/resource/words.txt"); // 단어 파일 경로 설정
            this.reader = new FileReader(file); // 파일 읽기 스트림 초기화
            BufferedReader br = new BufferedReader(reader); // 버퍼를 이용한 파일 읽기
            String line = "";

            // 파일에서 한 줄씩 읽어서 벡터에 추가
            while ((line = br.readLine()) != null) {
                strings.add(line);
            }
            br.close(); // 스트림 닫기
        } catch (IOException e) {
            return; // 예외 발생 시 아무 작업도 하지 않음
        }
    }

    // 벡터에서 랜덤한 문자열 반환
    public String getRandomString() {
        int randomIdx = (int) (Math.random() * strings.size()); // 랜덤 인덱스 생성
        return strings.get(randomIdx); // 해당 인덱스의 단어 반환
    }

    // 새로운 단어를 파일과 벡터에 추가
    public void addWord(String word) {
        // 단어가 null이거나 공백이거나 이미 벡터에 존재하면 추가하지 않음
        if (word == null || word.trim().isEmpty() || strings.contains(word)) {
            return;
        }

        try {
            // 단어를 파일에 추가하기 위해 FileWriter 객체 생성 (append 모드)
            FileWriter writer = new FileWriter(file, true);

            // 파일에 단어 기록
            writer.write(word + "\n");

            // 벡터에 단어 추가 (추가된 단어가 게임에 바로 반영되도록)
            strings.add(word);

            writer.close(); // 파일 스트림 닫기
        } catch (IOException e) {
            return; // 예외 발생 시 아무 작업도 하지 않음
        }
    }
}
