import java.util.Iterator;

public interface LogAPI {

    boolean isEmpty();
    boolean hasNext();
    String next();
    Iterator<String> iterator();

}
