import java.awt.*;

public class Logic {
    public static final Position INITIAL_POSITION_PLAYER1 = new Position(0, 0);
    public static final Position INITIAL_POSITION_PLAYER2 = new Position(10, 10);


    private Position positionVariation[] = {new Position(0, 0), new Position(0, 0)};

    private int[] lifes = {10, 10};

    public void resetPosition(){
        positionVariation[0].x = 0;
        positionVariation[0].y = 0;
        positionVariation[1].x = 0;
        positionVariation[1].y = 0;
    }

    public Position getPosition(int player) {
        return positionVariation[player];
    }

    public void movement(int player, String direction) {
        System.out.println(player + " " + direction);
        int dx = 0;
        int dy = 0;

        if(direction.equals("right")){
            dx = 10;
        }

        if(direction.equals("left")){
            dx = -10;
        }

        if(direction.equals("up")){
            dy = -10;
        }

        if(direction.equals("down")){
            dy = 10;
        }

        positionVariation[player].x = dx;
        positionVariation[player].y = dy;
    }



}
