package resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class TextSource {

    private FileReader reader = null;
    private File file = null;
    private Vector<String> strings = new Vector<>();

    public TextSource()  {
        try {
            this.file = new File("src/resource/words.txt");
            this.reader = new FileReader(file);
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            while((line = br.readLine()) != null) {
                strings.add(line);
            }
        } catch (IOException e) {
            return;
        }
    }

    public String getRandomString() {
        int randomIdx = (int)(Math.random()*strings.size());
        return strings.get(randomIdx);
    }

}
