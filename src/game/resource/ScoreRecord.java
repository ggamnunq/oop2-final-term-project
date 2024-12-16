package game.resource;

import game.enums.Difficulty;

import java.io.*;
import java.util.*;

// 점수 관리 클래스
public class ScoreRecord {

    // 파일 입출력 관련 필드
    private FileReader reader = null; // 파일 읽기 위한 FileReader
    private FileWriter writer = null; // 파일 쓰기 위한 FileWriter
    private File file = null; // 현재 사용하는 파일
    private BufferedReader br = null; // 파일 읽기 위한 BufferedReader
    private BufferedWriter bw = null; // 파일 쓰기 위한 BufferedWriter

    // 각 난이도의 점수 파일 경로
    private String easyScoresPath =  "src/game/resource/easyScores.txt";
    private String normalScoresPath =  "src/game/resource/normalScores.txt";
    private String hardScoresPath =  "src/game/resource/hardScores.txt";

    // 특정 난이도에서 상위 5개 점수 가져오기
    public List<String[]> getTop5(Difficulty difficulty) {

        List<String[]> all = new LinkedList<>(); // 모든 점수를 저장할 리스트

        try {
            // 선택된 난이도의 파일 설정
            setFileSetting(difficulty);

            String line;
            // 파일에서 한 줄씩 읽어서 리스트에 추가
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" "); // 이름과 점수를 구분
                String name = parts[0]; // 이름
                String score = parts[1]; // 점수
                all.add(new String[]{name, score}); // 이름과 점수를 배열로 저장
            }
        } catch (Exception e) {
        }

        // 점수를 내림차순으로 정렬
        all.sort(new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                int score1 = Integer.parseInt(o1[1]); // o1의 점수
                int score2 = Integer.parseInt(o2[1]); // o2의 점수

                return score2 - score1; // 내림차순 정렬
            }
        });

        // 상위 5개의 점수만 가져옴
        List<String[]> top5 = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            if (all.size() >= i + 1) {
                top5.add(all.get(i)); // 리스트에 추가
            }
        }

        return top5; // 상위 5개 점수 정보 담긴 리스트 반환
    }

    // 점수 기록
    public void recordScore(String name, int score, Difficulty difficulty) {
        try {
            // 선택된 난이도의 파일 설정
            setFileSetting(difficulty);

            // 파일이 존재하지 않으면 생성
            if (!file.exists()) {
                file.createNewFile();
            }

            // 파일에 이름과 점수 기록
            writer.write(name + " " + score + "\n");
            writer.close(); // 파일 닫기
        } catch (IOException e) {
            return; // 예외 발생 시 아무 작업도 하지 않음
        }
    }

    // 난이도에 따라 파일 설정
    private void setFileSetting(Difficulty difficulty) {
        try {
            // 난이도에 따라 파일 설정
            switch (difficulty.getDifficultyEnum()) {
                case EASY:
                    this.file = new File(easyScoresPath);
                    break;
                case NORMAL:
                    this.file = new File(normalScoresPath);
                    break;
                case HARD:
                    this.file = new File(hardScoresPath);
                    break;

            }

            // 파일 읽기/쓰기 스트림 초기화
            this.reader = new FileReader(file);
            this.writer = new FileWriter(file, true); // append 모드
            this.bw = new BufferedWriter(writer);
            this.br = new BufferedReader(reader);
        } catch (IOException e) {
            return; // 예외 발생 시 아무 작업도 하지 않음
        }
    }
}
