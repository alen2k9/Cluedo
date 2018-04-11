import java.util.ArrayList;
import java.util.Iterator;

public class Log implements Iterable<String>, Iterator<String>, LogAPI {

    private final ArrayList<String> messages = new ArrayList<>();
    private Iterator<String> iterator;

    public void addExchange(Player currentPlayer, Player queriedPlayer, Query query, boolean cardFound) {
        messages.add(currentPlayer + " questioned " + queriedPlayer + " about " + query.getSuspect() + " with the "
                + query.getWeapon() + " in the " + query.getRoom() + ".");
        if (cardFound) {
            messages.add(queriedPlayer + " showed one card.");
        } else {
            messages.add(queriedPlayer + " did not show any cards.");
        }
    }

    public void addExchange(Player currentPlayer, Player queriedPlayer, Query query, Card card) {
        messages.add(currentPlayer + " questioned " + queriedPlayer + " about " + query.getSuspect() + " with the "
                + query.getWeapon() + " in the " + query.getRoom() + ".");
        messages.add(queriedPlayer + " showed one card: " + card + ".");
    }

    public boolean isEmpty() {
        return messages.size()==0;
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    public String next() {
        return iterator.next();
    }

    public Iterator<String> iterator() {
        iterator = messages.iterator();
        return iterator;
    }
}
