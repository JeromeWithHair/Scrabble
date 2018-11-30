import java.util.ArrayList;

public class Tibbopotomus implements ScrabbleAI {

    private Board B = new Board();

    @Override
    public void setGateKeeper(GateKeeper gateKeeper) {

    }

    @Override
    public ScrabbleMove chooseMove() {

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                Location here = new Location(i, j);
                if (B.isOccupied(here)) {
                    for (Location moveDir : getEmptyDirection(here)) {
                       // findBestWord();
                    }
                }
            }
        }

        return null;
    }
    private boolean isLegalConfig(Location spot, String configuration) {

        return true;
    }

    /** Returns an array list of all of the directions that have four free spots and one free opposite
     * for a given spot on the board*/
    private ArrayList<Location> getEmptyDirection(Location spot) {
        ArrayList<Location> result = new ArrayList<>();
        ArrayList<Location> dirs = new ArrayList<>();
        dirs.add(new Location(1, 0));
        dirs.add(new Location(-1, 0));
        dirs.add(new Location(0, 1));
        dirs.add(new Location(0, -1));
        for (Location dir : dirs) {
            if (isEmptyDirection(spot, dir)) result.add(dir);
        }
        return result;
    }
    /**helper for getEmptyDirection */
    private boolean isEmptyDirection(Location spot, Location dir) {
        
        if (B.isOccupied(spot.antineighbor(dir))) return false;
        if (B.isOccupied(spot.neighbor(dir))) return false;
        if (B.isOccupied(spot.neighbor(dir).neighbor(dir))) return false;
        if (B.isOccupied(spot.neighbor(dir).neighbor(dir).neighbor(dir))) return false;
        if (B.isOccupied(spot.neighbor(dir).neighbor(dir).neighbor(dir).neighbor(dir))) return false;
        return true;
    }
}
