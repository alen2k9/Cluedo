public interface PlayerAPI {

    String getName();
    boolean hasName(String name);
    Token getToken();
    Cards getCards();
    boolean hasSeen(String card);
    boolean hasCard(String name);
    String toString();

}
