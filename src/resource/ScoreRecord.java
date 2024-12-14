package resource;

import enums.Difficulty;

import java.io.*;
import java.util.*;

public class ScoreRecord {

    private FileReader reader = null;
    private FileWriter writer = null;
    private File easySores = null;
    private BufferedReader br = null;
    private BufferedWriter bw = null;

    private String easyScoresPath =  "src/resource/easyScores.txt";
    private String normalScoresPath =  "src/resource/normalScores.txt";
    private String hardScoresPath =  "src/resource/hardScores.txt";

    public List<String[]> getTop5(Difficulty difficulty)  {

        List<String[]> all = new LinkedList<>();

        try {

            setFileSetting(difficulty);

            String line;
            while((line = br.readLine()) != null){
                String[] parts = line.split(" ");
                String name = parts[0];
                String score = parts[1];
                all.add(new String[]{name, score});
            }
        } catch (Exception e) {
            return null;
        }

        // all을 내림차순으로 정렬 ( Comparator 활용 )
        all.sort(new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                // o1과 o2의 점수를 비교 (문자열을 정수로 변환하여 비교)
                int score1 = Integer.parseInt(o1[1]);
                int score2 = Integer.parseInt(o2[1]);

                // 내림차순: score2 - score1
                return score2 - score1;
            }
        });

        List<String[]> top5 = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            if (all.size() >= i+1) {
                top5.add(all.get(i));
            }
        }

        return top5;
    }

    public void recordScore(String name, int score, Difficulty difficulty) {
        try{

            setFileSetting(difficulty);
            if (!easySores.exists()) {
                easySores.createNewFile();
            }

            writer.write(name + " " + score + "\n");
            writer.close();

        }catch (IOException e) {
            return;
        }

    }

    private void setFileSetting(Difficulty difficulty){

        try {

            switch (difficulty.getDifficultyEnum()) {
                case EASY:
                    this.easySores = new File(easyScoresPath);
                    break;
                case NORMAL:
                    this.easySores = new File(normalScoresPath);
                    break;
                case HARD:
                    this.easySores = new File(hardScoresPath);
                    break;
            }

            this.reader = new FileReader(easySores);
            this.writer = new FileWriter(easySores, true);
            this.bw = new BufferedWriter(writer);
            this.br = new BufferedReader(reader);
        } catch (IOException e) {
            return;
        }
    }



}