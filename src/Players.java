import java.util.ArrayList;
import java.util.Iterator;

public class Players implements Iterable<Player>, Iterator<Player> {

    private final ArrayList<Player> players = new ArrayList<>();
    private int currentPlayerIndex;
    private Iterator<Player> iterator;

    Players() {
    }

    Players (Players players) {
        for (Player player : players) {
            this.players.add (player);
        }
    }

    public void clear() {
        players.clear();
    }

    public void add(Player player) {
        players.add(player);
    }

    public boolean contains(String name) {
        for (Player player : players) {
            if (player.hasName(name)) {
                return true;
            }
        }
        return false;
    }

    public Player get(int index) {
        return players.get(index);
    }

    public int count() {
        return players.size();
    }

    public void setCurrentPlayer(String name) {
        currentPlayerIndex = 0;
        while (!players.get(currentPlayerIndex).hasName(name)) {
            currentPlayerIndex++;
        }
    }

    public Player getCurrentPlayer () {
        return players.get(currentPlayerIndex);
    }

    public void turnOver() {
        do {
            if (currentPlayerIndex < players.size() - 1) {
                currentPlayerIndex++;
            } else {
                currentPlayerIndex = 0;
            }
        } while (players.get(currentPlayerIndex).isEliminated());
    }

    public Player getPlayerOnTheLeft(Player player) {
        int index = players.indexOf(player);
        if (index < players.size()-1) {
            index++;
        } else {
            index = 0;
        }
        return players.get(index);
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    public Player next() {
        return iterator.next();
    }

    public Iterator<Player> iterator() {
        iterator = players.iterator();
        return iterator;
    }

}
