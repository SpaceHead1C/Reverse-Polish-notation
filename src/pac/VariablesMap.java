package pac;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

public class VariablesMap {
    private Map<String, Double> map;

    public VariablesMap() {
        this.map = new HashMap<>();
    }

    public void load(String filename) throws IOException, IllegalArgumentException {
        try (BufferedReader f = new BufferedReader(new FileReader(filename))) {
            map.clear();
            String strLine = "";
            while ((strLine = f.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(strLine, " ", false);
                String token = "";
                String key;
                Double value;

                if (tokenizer.hasMoreTokens()) {
                    token = tokenizer.nextToken();
                    int codeFirstChar = token.charAt(0);
                    if (codeFirstChar > 64 && codeFirstChar < 91 || codeFirstChar > 96 && codeFirstChar < 123) {
                        key = token;
                    } else {
                        throw new IllegalArgumentException("Can not resolve variable name: " + token);
                    }
                } else {
                    throw new IllegalArgumentException("Except variable's name in line: " + strLine);
                }
                if (tokenizer.hasMoreTokens()) {
                    token = tokenizer.nextToken();
                    try {
                        value = Double.valueOf(token);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException(e.getMessage());
                    }
                } else {
                    throw new IllegalArgumentException("Expect variable's value in line: " + strLine);
                }

                map.put(key, value);
            }
        } catch (IOException e) {
            throw e;
        }
    }

    public HashMap<String, Double> getMap() {
        HashMap<String, Double> map = new HashMap<>();

        Iterator<Map.Entry<String, Double>> entries = this.map.entrySet().iterator();
        Map.Entry<String, Double> entry;
        while (entries.hasNext()) {
            entry = entries.next();
            map.put(entry.getKey(), entry.getValue());
        }

        return map;
    }
}
