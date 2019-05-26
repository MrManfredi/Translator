package grammar;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class RightSide {
    private List<String> words;

    public RightSide() {
        words = new ArrayList<>();
    }

    public RightSide(Stack<String> words) {
        this.words = new ArrayList<>();
        while (!words.isEmpty()) {
            this.words.add(words.pop());    // reverse copy
        }
    }

    public RightSide(List<String> words) {
        this.words = words;
    }

    public void addWord(String word) {
        words.add(word);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < words.size(); i++) {
            builder.append(words.get(i));
            if (i + 1 < words.size()) {
                builder.append("  ,  ");
            }
        }
        return builder.toString();
    }

    public List<String> getWords() {
        return words;
    }
}
