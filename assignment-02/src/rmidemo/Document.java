package rmidemo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Document implements Serializable {

    private List<String> strings = new ArrayList<>();

    public void addString(String newString) {
        strings.add(newString);
    }

    @Override
    public String toString() {
        return "Document:\n" + String.join("\n", strings);
    }
}
