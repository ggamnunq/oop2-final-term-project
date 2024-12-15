package resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class TextSource {

    private File file = null;
    private FileReader reader = null;
    private Vector<String> strings = new Vector<>();

    public TextSource() {
        try {
            this.file = new File("src/resource/words.txt");
            this.reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            while ((line = br.readLine()) != null) {
                strings.add(line);
            }
            br.close();
        } catch (IOException e) {
            return;
        }
    }

    public String getRandomString() {
        int randomIdx = (int) (Math.random() * strings.size());
        return strings.get(randomIdx);
    }

    public void addWord(String word) {
        // 값이 없거나, 공백이거나, 이미 있는 값일 경우 값을 추가하지 않음
        if (word == null || word.trim().isEmpty() || strings.contains(word)) {
            return;
        }

        try {
            // 단어 입력할 때마다 FileWriter 객체 생성 -> 안그러면 두 번째부터는 기록이 안됌
            FileWriter writer = new FileWriter(file, true);
            // 파일에 추가
            writer.write(word + "\n");
            // 벡터에 추가히야 프로그램 작동 후에 추가된 단어들도 게임에서 적용할 수 있도록 함
            strings.add(word);
            writer.close();
        } catch (IOException e) {
            return;
        }
    }
}
