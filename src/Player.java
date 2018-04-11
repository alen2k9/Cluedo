public class Player implements PlayerAPI {

    private final String name;
    private final Token token;
    private Cards cards = new Cards();
    private final Cards viewedCards = new Cards();
    private boolean eliminated = false;
    private Bot bot;

    Player(String name, Token token) {
        this.name = name;
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public boolean hasName(String name) {
        return this.name.toLowerCase().trim().equals(name.toLowerCase().trim());
    }

    public Token getToken() {
        return token;
    }

    public void addCards(Cards cards) {
        this.cards = cards;
    }

    public Cards getCards() {
        return cards;
    }

    public void addViewedCard(Card card) {
        viewedCards.add(card);
    }

    public boolean hasSeen(String card) {
        for (Card viewedCard : viewedCards) {
            if (viewedCard.hasName(card)) {
                return true;
            }
        }
        return false;
    }

    public void eliminate() {
        eliminated = true;
    }

    public boolean isEliminated() {
        return eliminated;
    }

    public boolean hasCard(String name) {
        return cards.contains (name);
    }

    public void addBot (Bot bot) {
        this.bot = bot;
    }

    public Bot getBot () {
        return bot;
    }

    @Override
    public String toString() {
        return name + " (" + token.getName() + ")";
    }
}
