package main;

public enum Piececolor {
	white(0), 
	black(1),
	none(-1);
	
	private int idnum;

    Piececolor(int idnum) {
        this.idnum = idnum;
    }
	
    public int num() {
        return idnum;
    }
    
    public Piececolor opposite() {
    	if (this == Piececolor.white) {
    		return Piececolor.black;
    	} else {
    		return Piececolor.white;
    	}
    }
}
