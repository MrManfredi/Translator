package lexer;

import lexer.values.NameableValue;

import java.util.ArrayList;

public class Table<T> extends ArrayList<T> {
    public T getItem(String name){
        for (T tmp : this) {
            if (((NameableValue) tmp).getName().equals(name)) {
                return tmp;
            }
        }
        return null;
    }

    public boolean containsItem(String name){
        return this.stream().anyMatch(item -> (((NameableValue) item).getName().equals(name)));
    }
}
