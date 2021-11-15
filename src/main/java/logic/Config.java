package logic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

// syntax analyzer
public class Config {

    private final HashMap<String, String> params = new HashMap<>();

    private static final String DELIMITER = "=";
    private final List<String> tokens;
    public Config(List<String> tokens){this.tokens=tokens;}

    public boolean isValidToken(String token){
        return tokens.contains(token);
    }
    public int getNumTokens() { return tokens.size();}

    public void ParseConfig(String path) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(path));
            String line;
            int numLines = 0;
            while (scanner.hasNext()){
                line = scanner.nextLine();
                numLines++;
                String[] tokens = Arrays.stream(line.split(DELIMITER))
                        .map(String::trim)
                        .toArray(String[]::new);
                if (!isValidToken(tokens[0])){
                    throw new IllegalArgumentException("Invalid config value at line " + numLines+ " :" + tokens[0]);
                }
                this.params.put(tokens[0],tokens[1]);
            }

            if (this.params.size() != getNumTokens()){
                throw new IllegalArgumentException("incomplete properties file - not enough parameters");
            }


    }



    public HashMap<String,String> getParams() { return this.params;}
}
