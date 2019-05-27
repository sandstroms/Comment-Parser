import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Parses a Java source code file and displays comments to the console.
 *
 * @author Shaun Sandstrom
 * @version 2.0
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
        Map<Integer, JavadocComment> javadocComments = new TreeMap<>();
        while((currCharInt = buffer.read()) != -1) {
            char currChar = (char) currCharInt;
            if(currChar == '/') {
                currCharInt = buffer.read();
                currChar = (char) currCharInt;
                if(currChar == '/') {
                    StringBuilder strBuilder = parseSingleLineComment(buffer);
                    String currComment = strBuilder.toString();
                    /* A new line character was reached since the single line
                       comment was read, thus increment lineNum. */
                    comments.put(lineNum++, currComment);
                } else if(currChar == '*') {
                    int beginLineNum = lineNum;
                    StringBuilder strBuilder = new StringBuilder();
                    currCharInt = buffer.read();
                    // A JavaDoc comment.
                    if((currCharInt) == '*') {
                        JavadocComment comment = new JavadocComment();
                        parseJavaDocComment(buffer, strBuilder, comment);
                        String text = strBuilder.toString();
                        comment.setDescr(text);
                        javadocComments.put(beginLineNum, comment);
                    } else {
                        currChar = (char) currCharInt;
                        if(currChar == '\n') {
                            lineNum++;
                        } else {
                            strBuilder.append(currChar);
                        }
                        parseMultiLineComment(buffer, strBuilder);
                        String currComment = strBuilder.toString();
                        comments.put(beginLineNum, currComment);
                    }
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
        Set<Integer> javadocKeys = javadocComments.keySet();
        for (Integer javadocKey : javadocKeys) {
            JavadocComment javadocComment = javadocComments.get(javadocKey);
            System.out.print("Line " + javadocKey + ": " + javadocComment.getDescr());
            if (!javadocComment.tagsExist()) {
                System.out.println("Tags:");
                List<String> params = javadocComment.getParams();
                String ret = javadocComment.getRet();
                List<String> authors = javadocComment.getAuthors();
                String version = javadocComment.getVersion();
                List<String> exceptions = javadocComment.getExceptions();
                List<String> sees = javadocComment.getSees();

                for (String param : params) {
                    System.out.println("Param: " + param);
                }
                if(!ret.equals("")) {
                    System.out.println("Return: " + ret);
                }
                for (String author : authors) {
                    System.out.println("Author: " + author);
                }
                if (!version.equals("")) {
                    System.out.println("Version: " + version);
                }
                for (String exception : exceptions) {
                    System.out.println("Exception: " + exception);
                }
                for (String see : sees) {
                    System.out.println("See: " + see);
                }
            }
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

    public static void parseJavaDocComment(Reader buffer, StringBuilder strBuilder,
                                           JavadocComment comment)
                                            throws IOException {
        int currCharInt;
        char currChar;

        while ((currCharInt = buffer.read()) != '*') {
            if (currCharInt == '\n') {
                lineNum++;
            } else if(currCharInt == '@') {
                String descr = strBuilder.toString();
                comment.setDescr(descr);
                parseTags(buffer, comment);
                // Once we're done parsing the tags, we're done parsing the Javadoc.
                return;
            }
            currChar = (char) currCharInt;
            strBuilder.append(currChar);
        }
        currCharInt = buffer.read();
        if (currCharInt == '/') {
            if (comment.getDescr().equals("")) {
                String descr = strBuilder.toString();
                comment.setDescr(descr);
            }
            return;
        } else if (currCharInt == '\n') {
            lineNum++;
        }
        currChar = (char) currCharInt;
        strBuilder.append(currChar);
        parseJavaDocComment(buffer, strBuilder, comment);
    }

    public static void parseTags(Reader buffer, JavadocComment comment) throws IOException {
        int currCharInt;
        char currChar;
        StringBuilder tagStrBuilder = new StringBuilder();
        StringBuilder tagDescrStrBuilder = new StringBuilder();

        while ((currCharInt = buffer.read()) != ' ') {
            currChar = (char) currCharInt;
            tagStrBuilder.append(currChar);
        }
        String tag = tagStrBuilder.toString();

        while ((currCharInt = buffer.read()) != '@') {
            if (currCharInt != '*') {
                if (currCharInt == '\n') {
                    lineNum++;
                }
                currChar = (char) currCharInt;
                tagDescrStrBuilder.append(currChar);
            }
            currCharInt = buffer.read();
            if (currCharInt == '/') {
                return;
            } else if (currCharInt == '\n') {
                lineNum++;
            }
            currChar = (char) currCharInt;
            tagDescrStrBuilder.append(currChar);
        }
        String tagDescr = tagDescrStrBuilder.toString();

        if (tagIsValid(tag)) {
            if (tag.equals("param")) {
                comment.addParam(tagDescr);
            } else if (tag.equals("return")) {
                comment.setRet(tagDescr);
            } else if (tag.equals("author")) {
                comment.addAuthor(tagDescr);
            } else if (tag.equals("version")) {
                comment.setVersion(tagDescr);
            } else if (tag.equals("exception") || tag.equals("throws")) {
                comment.addException(tagDescr);
            } else if (tag.equals("sees")) {
                comment.addSees(tagDescr);
            }
        }
        parseTags(buffer, comment);
    }

    /**
     * Determines if a tag is valid. Note: not all tags are accounted for,
     * only tags that I have seen used before or that seem common.
     * @param tag the string of the tag
     * @return if the tag is valid or not
     */
    private static boolean tagIsValid(String tag) {
        return (tag.equals("param") || tag.equals("return") || tag.equals("author")
            || tag.equals("version") || tag.equals("exception") || tag.equals("throws")
            || tag.equals("see"));
    }
}
