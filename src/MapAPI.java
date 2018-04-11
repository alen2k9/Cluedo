
public interface MapAPI {

    boolean isCorridor(Coordinates position);
    boolean isDoor(Coordinates currentPosition, Coordinates nextPosition);
    Room getRoom(Coordinates position);
    Room getRoom(String name);
    boolean isValidMove(Coordinates startingPosition, String key);
    Coordinates getNewPosition(Coordinates startingPosition, String key);

}



