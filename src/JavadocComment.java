import java.util.ArrayList;
import java.util.List;

public class JavadocComment {
    private String descr;
    private List<String> params;
    private String ret;
    private List<String> authors;
    private String version;
    private List<String> exceptions;
    private List<String> sees;

    public JavadocComment() {
        descr = "";
        params = new ArrayList<>();
        ret = "";
        authors = new ArrayList<>();
        version = "";
        exceptions = new ArrayList<>();
        sees = new ArrayList<>();
    }

    public JavadocComment(String descr) {
        this.descr = descr;

        params = new ArrayList<>();
        ret = "";
        authors = new ArrayList<>();
        version = "";
        exceptions = new ArrayList<>();
        sees = new ArrayList<>();
    }

    public boolean tagsExist() {
        return params.isEmpty() && !ret.equals("") && authors.isEmpty()
                && !version.equals("") && exceptions.isEmpty() && sees.isEmpty();
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public void addParam(String param) {
        params.add(param);
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public void addAuthor(String author) {
        authors.add(author);
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void addException(String exception) {
        exceptions.add(exception);
    }

    public void addSees(String see) {
        sees.add(see);
    }

    public String getDescr() {
        return descr;
    }

    public List<String> getParams() {
        return params;
    }

    public String getRet() {
        return ret;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public String getVersion() {
        return version;
    }

    public List<String> getExceptions() {
        return exceptions;
    }

    public List<String> getSees() {
        return sees;
    }
}
