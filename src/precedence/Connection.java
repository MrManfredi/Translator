package precedence;

import java.util.HashMap;
import java.util.Map;

public class Connection {
    private Map<String, Relation> connection;

    public Connection() {
        connection = new HashMap<>();
    }

    Connection(String rightWord, RelationType relation) {
        connection = new HashMap<>();
        connection.put(rightWord, new Relation(relation));
    }

    void addConnection(String rightWord, RelationType relation) {
        if (connection.containsKey(rightWord)){
            connection.get(rightWord).setRelation(relation);
        }
        else {
            connection.put(rightWord, new Relation(relation));
        }
    }

}
