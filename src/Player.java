import java.util.ArrayList;
import java.util.List;

public class Player {
    // pieces
    ArrayList<String> pieces = new ArrayList<>(List.of("f1", "f2", "f3", "f4", "f5", "f6", "f7", "f8", "b1", "b2", "b3", "b4", "b5", "b6", "b7", "b8"));

    // side and piece type
    private String side;
    private String pieceType;

    // Constructor
    public Player() {
    }

    // getter method for side
    public String getSide() {
        return side;
    }

    // setter method for side
    public void setSide(String side) {
        this.side = side;
    }

    // getter method for pieceType
    public String getPieceType() {
        return pieceType;
    }

    // setter method for pieceType
    public void setPieceType(String pieceType) {
        this.pieceType = pieceType;
    }
}
