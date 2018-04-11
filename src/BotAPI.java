public interface BotAPI {

    String getName ();
    String getCommand ();
    String getMove();
    String getSuspect();
    String getWeapon();
    String getRoom();
    String getDoor();
    String getCard(Cards matchingCards);

}
