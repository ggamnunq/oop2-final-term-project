package resource;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class ScoreRecord {

    private FileReader reader = null;
    private FileWriter writer = null;
    private File file = null;
    private BufferedReader br = null;
    private BufferedWriter bw = null;
    private StringTokenizer st = null;

    public ScoreRecord()  {
        try {
            this.file = new File("src/resource/scores.txt");
            this.reader = new FileReader(file);
            this.writer = new FileWriter(file, true);
            this.br = new BufferedReader(reader);
            this.bw = new BufferedWriter(writer);
        } catch (IOException e) {
            return;
        }
    }

    public Map<String, Integer> getTop10()  {

        Map<String, Integer> top10 = new HashMap<>();

        try {
            for (int i = 0; i < 10; i++) {
                st = new StringTokenizer(br.readLine());
                String name = st.nextToken();
                int score = Integer.parseInt(st.nextToken());
                top10.put(name, score);
            }
            return top10;
        } catch (IOException e) {
            return null;
        }
    }

    public void recordScore(String name, int score) {
        try{

            if (!file.exists()) {
                file.createNewFile();
            }

            writer.write(name + " " + score + "\n");
            writer.close();

        }catch (IOException e) {
            return;
        }

    }



}
