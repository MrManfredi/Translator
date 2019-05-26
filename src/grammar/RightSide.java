package grammar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RightSide rightSide = (RightSide) o;
        return Objects.equals(words, rightSide.words);
    }

    @Override
    public int hashCode() {
        return Objects.hash(words);
    }

    public List<String> getWords() {
        return words;
    }
}
