package main;

public enum PieceID {
	pawn(0,100),
	rook(1,500),
	knight(2,320),
	bishop(3,330),
	queen(4,900),
	king(5,400);
	
	private int value;
	private int num;

    PieceID(int num, int value) {
        this.num = num;
        this.value = value;
    }

    public int num() {
        return num;
    }
    
    public int value() {
        return value;
    }
}
