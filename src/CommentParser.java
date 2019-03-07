import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Parses a Java source code file and displays comments to the console.
 * JavaDoc comment parsing is in progress.
 * @author Shaun Sandstrom
 * @version 1.0
 */

public class CommentParser {
    private static int lineNum = 1;

    public static void main(String[] args) {
        File currFile;
        for(String filename : args) {
            currFile = new File(filename);
            try {
                parseFile(currFile);
            } catch(IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void parseFile(File file) throws IOException {
        InputStream inStream = new FileInputStream(file);
        Reader reader = new InputStreamReader(inStream, Charset.defaultCharset());
        Reader buffer = new BufferedReader(reader);

        int currCharInt;
        Map<Integer, String> comments = new TreeMap<>();
        while((currCharInt = buffer.read()) != -1) {
            char currChar = (char) currCharInt;
            if(currChar == '/') {
                currCharInt = buffer.read();
                currChar = (char) currCharInt;
                if(currChar == '/') {
                    StringBuilder strBuilder = parseSingleLineComment(buffer);
                    String currComment = strBuilder.toString();
                    // A new line character was reached.
                    comments.put(lineNum++, currComment);
                } else if(currChar == '*') {
                    int beginLineNum = lineNum;
                    StringBuilder strBuilder = new StringBuilder();
                    /*
                    if((currCharInt = buffer.read()) == '*') {
                        parseJavaDocComment(buffer, strBuilder);
                    }
                    */
                    parseMultiLineComment(buffer, strBuilder);
                    String currComment = strBuilder.toString();
                    comments.put(beginLineNum, currComment);
                }
            }
            if (currChar == '\n') {
                lineNum++;
            }
        }
        Set<Integer> keys = comments.keySet();
        for (Integer key : keys) {
            System.out.println("Line " + key + ": " + comments.get(key));
        }
    }

    public static StringBuilder parseSingleLineComment(Reader buffer)
            throws IOException {
        int currCharInt;
        char currChar;
        StringBuilder strBuilder = new StringBuilder();
        while((currCharInt = buffer.read()) != '\n') {
            currChar = (char) currCharInt;
            strBuilder.append(currChar);
        }
        return strBuilder;
    }

    public static void parseMultiLineComment(Reader buffer, StringBuilder strBuilder)
            throws IOException {
        int currCharInt;
        char currChar;
        while((currCharInt = buffer.read()) != '*') {
            if (currCharInt == '\n') {
                lineNum++;
            }
            currChar = (char) currCharInt;
            strBuilder.append(currChar);
        }
        currCharInt = buffer.read();
        if (currCharInt == '/') {
            return;
        } else if (currCharInt == '\n') {
            lineNum++;
        }
        currChar = (char) currCharInt;
        strBuilder.append(currChar);
        parseMultiLineComment(buffer, strBuilder);
    }

    /*
    public static void parseJavaDocComment(Reader buffer, StringBuilder strBuilder)
                                            throws IOException {
        int currCharInt;
        char currChar;
        while((currCharInt = buffer.read()) != '*') {
            if (currCharInt == '\n') {
                lineNum++;
            }
            currChar = (char) currCharInt;
            strBuilder.append(currChar);
        }
        currCharInt = buffer.read();
        if (currCharInt == '/') {
            return;
        } else if (currCharInt == '\n') {
            lineNum++;
        }
        currChar = (char) currCharInt;
        strBuilder.append(currChar);
        parseJavaDocComment(buffer, strBuilder);
    }
    */
}
