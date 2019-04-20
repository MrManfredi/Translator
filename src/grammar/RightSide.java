package grammar;

import java.util.ArrayList;
import java.util.List;

public class RightSide {
    private List<String> words;

    public RightSide() {
        words = new ArrayList<>();
    }

    public RightSide(List<String> words) {
        this.words = words;
    }

    public void addWord(String word) {
        words.add(word);
    }

    public void show() {
        System.out.print(words);
    }

    public List<String> getWords() {
        return words;
    }
}
