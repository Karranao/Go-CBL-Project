import java.util.ArrayList;
import java.util.Comparator;


/** Contains methods used to count score at the end of a game of Go.
 * 
 */
public class CheckGo {
    private ArrayList<ArrayList<Integer>> locations;    //remaining locations to be checked
    private ArrayList<ArrayList<Integer>> locations2;   //copy of locations
    int[][] boardPositions; //positions of all tiles and who owns them on the board
    //holds points making up empty spaces enclosed by placed tiles
    protected ArrayList<ArrayList<String>> groups;   //all groups of enclosed tiles
    protected Boolean hasEye;   //track whether a group has an eye
    protected ArrayList<String> checked;    //track which tiles have been checked
    protected ArrayList<ArrayList<String>> whiteGroups; //all white tile groups formed
    protected ArrayList<ArrayList<String>> blackGroups; //all black tile groups formed
    //white tile groups that count to score
    protected ArrayList<ArrayList<String>> validWhiteGroups; 
    //black tile groups that count to score   
    protected ArrayList<ArrayList<String>> validBlackGroups;
    protected ArrayList<ArrayList<String>> eyes;    //coordinates of all eyes found
    //corresponding to each eye, all groups that contain it
    protected ArrayList<ArrayList<ArrayList<String>>> eyeContainedIn;

    /** Constructor.
     * 
     * @param boardPositions    holds positions and types of each tile on the board
     */
    public CheckGo(int[][] boardPositions) {
        this.locations = new ArrayList<ArrayList<Integer>>();
        this.locations2 = new ArrayList<ArrayList<Integer>>();
        this.boardPositions = boardPositions;
        this.groups = new ArrayList<ArrayList<String>>();
    }

    /** Checks the number of points each player has in completed Go game. Calls functions required
     * to complete this task in order, using structures like loops to do so.
     */
    public void checkPoints(Board board) {
        int groupNum = 0; 
        int row;
        int col;
        //holds original format of boardPositions to reset it later.
        int[][] tempBoardPositions = new int[9][9]; 
        for (int i = 0; i < 9; i++) {
            for (int x = 0; x < 9; x++) {
                //copying boardPositions into tempBoardPositions
                tempBoardPositions[i][x] = boardPositions[i][x];
            }
        }


        //adding coordinates of all board locations to locations list
        int count = 0;
        for (row = 0; row < 9; row++) {
            for (col = 0; col < 9; col++) {
                locations.add(new ArrayList<Integer>());
                locations.get(count).add(row);
                locations.get(count).add(col);
                //creating second identical locations list to reset locations to original values
                locations2.add(new ArrayList<Integer>());
                locations2.get(count).add(row);
                locations2.get(count).add(col);
                count++;
            }
        }

        //Iterating through every space on the board that's yet to be checked by floodfill
        while (!locations.isEmpty()) {

            row = locations.get(0).get(0);  //initial row of tile to be checked
            col = locations.get(0).get(1);  //initial column of tile to be checked

            //initiating floodfill if position checked is empty
            if (boardPositions[row][col] == 0) {   
                //new group to store locations found by floodfill 
                groups.add(new ArrayList<String>());   
                floodFill(row, col, groupNum, 1);   //finding group surrounded by black tiles
                groupNum++; //iterating to next group
            } else {
                removeLocation(row, col);   //removes row and col location from locations arraylist
            }

        }
        locations = locations2; //resetting locations to original values
        int whiteStart = groupNum;  //saving index of start of white groups in group arraylist

        //resetting boardPositions
        for (int i = 0; i < 9; i++) {
            for (int x = 0; x < 9; x++) {
                boardPositions[i][x] = tempBoardPositions[i][x];
            }
        }

        //Iterating through every space on the board that's yet to be checked by floodfill
        while (!locations.isEmpty()) {
            row = locations.get(0).get(0);  //initial row of tile to be checked
            col = locations.get(0).get(1);  //initial column of tile to be checked

            //initiating floodfill if position checked is empty
            if (boardPositions[row][col] == 0) {    
                //new group to store locations found by floodfill 
                groups.add(new ArrayList<String>());
                floodFill(row, col, groupNum, 2);   //finding group surrounded by white tiles
                groupNum++; //iterating to next group
            } else {
                removeLocation(row, col);   //removes row and col location from locations arraylist
            }

        }

        whiteGroups = new ArrayList<ArrayList<String>>();   //to store groups surrounded by white
        blackGroups = new ArrayList<ArrayList<String>>();   //to store groups surrounded by black
        whiteGroups.addAll(groups);
        blackGroups.addAll(groups);

        //removing black tile group elements from white group
        for (int i = 0; i < whiteStart; i++) {
            whiteGroups.removeFirst();
        }
        //removing white tile group elements from black group
        for (int i = whiteStart; i < groups.size(); i++) {
            blackGroups.removeLast();
        }

        //sorting groups by size in ascending order
        whiteGroups.sort(Comparator.comparingInt(ArrayList::size));
        blackGroups.sort(Comparator.comparingInt(ArrayList::size));

        //removing oversized groups
        for (int i = 0; i < whiteGroups.size(); i++) {
            if (whiteGroups.get(i).size() > 40) {
                whiteGroups.remove(i);
            }
        }
        //removing oversized groups
        for (int i = 0; i < blackGroups.size(); i++) {
            if (blackGroups.get(i).size() > 35) {
                blackGroups.remove(i);
            }
        }

        //groups to hold groups deemed valid based on rules of Go
        validWhiteGroups = new ArrayList<ArrayList<String>>();
        validBlackGroups = new ArrayList<ArrayList<String>>();
        Boolean isValid;    //holds bool determining whether a group is valid or not

        //resetting boardPositions for use in subsequent algorithm
        for (int i = 0; i < 9; i++) {
            for (int x = 0; x < 9; x++) {
                boardPositions[i][x] = tempBoardPositions[i][x];
            }
        }

        
        eyes = new ArrayList<ArrayList<String>>();  //holds locations determined to be "eyes" in Go
        //holds corresponding groups that contain the eyes
        eyeContainedIn = new ArrayList<ArrayList<ArrayList<String>>>(); 

        //number of times algorithm must be run to check every group
        int iterations = whiteGroups.size() + blackGroups.size();
        //iterating through each remaining group checking if they are a valid area or not.
        for (int i = 0; i < iterations; i++) {
            isValid = false;
            if (!whiteGroups.isEmpty() && !blackGroups.isEmpty()) { //if neither group is empty
                //if white contains smallest group
                if (whiteGroups.getFirst().size() < blackGroups.getFirst().size()) {
                    //checks whether first white group is valid
                    isValid = investigateArea(whiteGroups.getFirst(), blackGroups, 2);
                    if (isValid) {
                        validWhiteGroups.add(whiteGroups.getFirst());
                    }
                    whiteGroups.removeFirst();  //removes checked group
                } else {    //if black contains smallest group
                    //checks whether first black group is valid
                    isValid = investigateArea(blackGroups.getFirst(), whiteGroups, 1);
                    if (isValid) {
                        validBlackGroups.add(blackGroups.getFirst());
                    }
                    blackGroups.removeFirst();  //removes checked group
                }
            } else {    //if one of the groups is empty
                if (whiteGroups.isEmpty()) {    //investigating remaining black group
                    isValid = investigateArea(blackGroups.getFirst(), whiteGroups, 1);
                    if (isValid) {
                        validBlackGroups.add(blackGroups.getFirst());
                    }
                    blackGroups.removeFirst();
                } else {    //investigating remaining white group
                    isValid = investigateArea(whiteGroups.getFirst(), blackGroups, 2);
                    if (isValid) {
                        validWhiteGroups.add(whiteGroups.getFirst());
                    }
                    whiteGroups.removeFirst();
                }
            }
        }

        int whiteTotal = 0; //total points for white
        int blackTotal = 0; //total points for black
        for (int i = 0; i < validWhiteGroups.size(); i++) {
            whiteTotal += validWhiteGroups.get(i).size();
        }

        for (int i = 0; i < validBlackGroups.size(); i++) {
            blackTotal += validBlackGroups.get(i).size();
        }
        
        String winner;
        //determining who won the game
        if (blackTotal > whiteTotal) {
            winner = "Black";
        } else if (blackTotal < whiteTotal) {
            winner = "White";
        } else {
            winner = "tie";
        }
        board.endgameData(blackTotal, whiteTotal, winner);  //stores totals and the winner in board
    }

    /** floodFill algorithm used to replace all empty tiles with 3's and save their coordinates by
     * groups (each group represents one space enclosed by tiles of the border color or the board
     * borders).
     * 
     * @param row   row in boardPositions corresponding to a tile
     * @param col   column in boardPositions corresponding to a tile
     * @param groupNum  indicates which index of groups tiles correspond to
     * @param border    border color to not be checked
     */
    public void floodFill(int row, int col, int groupNum, int border) {
        //checking current tile
        if (boardPositions[row][col] != border && boardPositions[row][col] != 3) {
            boardPositions[row][col] = 3;   //marking tile as checked
            removeLocation(row, col);   //removing tile from pool of locations to be checked
            //saving coordinates of tile as a string for its respective group in groups arraylist
            groups.get(groupNum).add(row + "" + col);

        }
        //checking tile above
        if (row + 1 <= 8 && boardPositions[row + 1][col] != border 
                && boardPositions[row + 1][col] != 3) {

            boardPositions[row + 1][col] = 3;   //marking tile as checked
            removeLocation(row, col);   //removing tile from pool of locations to be checked
            floodFill(row + 1, col, groupNum, border);
            //saving coordinates of tile as a string for its respective group in groups arraylist
            groups.get(groupNum).add((row + 1) + "" + col);

        }
        //checking tile below
        if (row - 1 >= 0 && boardPositions[row - 1][col] != border 
                && boardPositions[row - 1][col] != 3) {

            boardPositions[row - 1][col] = 3;   //marking tile as checked
            removeLocation(row, col);   //removing tile from pool of locations to be checked
            floodFill(row - 1, col, groupNum, border);
            //saving coordinates of tile as a string for its respective group in groups arraylist
            groups.get(groupNum).add((row - 1) + "" + col);

        }
        //checking tile to the left
        if (col + 1 <= 8 && boardPositions[row][col + 1] != border 
                && boardPositions[row][col + 1] != 3) {

            boardPositions[row][col + 1] = 3;   //marking tile as checked
            removeLocation(row, col);   //removing tile from pool of locations to be checked
            floodFill(row, col + 1, groupNum, border);
            //saving coordinates of tile as a string for its respective group in groups arraylist
            groups.get(groupNum).add(row + "" + (col + 1));
        }
        //checking tile to the right
        if (col - 1 >= 0 && boardPositions[row][col - 1] != border 
                && boardPositions[row][col - 1] != 3) {

            boardPositions[row][col - 1] = 3;   //marking tile as checked
            removeLocation(row, col);   //removing tile from pool of locations to be checked
            floodFill(row, col - 1, groupNum, border);
            //saving coordinates of tile as a string for its respective group in groups arraylist
            groups.get(groupNum).add(row + "" + (col - 1));

        }
        return;
    }


    /** Removes the location defined by row and col from ArrayList containing
     * remaining points to be checked.
     * 
     * @param row   row of point being removed
     * @param col   column of point being removed
     */
    public void removeLocation(int row, int col) {
        //iterating through all locations
        for (int i = 0; i < locations.size(); i++) {
            //checking if index of location contains the coordinates of the point to be removed
            if (locations.get(i).get(0) == row && locations.get(i).get(1) == col) {
                locations.remove(i);
            }
        }
    }


    /** Checks whether all locations in group are held within any member of otherGroups, returning
     * any groups that do hold the group.
     * 
     * @param group group being investigated
     * @param otherGroups   //all groups of the other color tiles
     * @return  An arraylist containing arraylists of coordinates of
     *               each group held in otherGroups that contains group
     */
    public ArrayList<ArrayList<String>> contains(ArrayList<String> group,
                ArrayList<ArrayList<String>> otherGroups) {
        //holds groups that may contain group
        ArrayList<ArrayList<String>> containedIn = new ArrayList<ArrayList<String>>();  
        for (int i = 0; i < otherGroups.size(); i++) {
            if (otherGroups.get(i).containsAll(group)) {
                containedIn.add(otherGroups.get(i));    //storing groups that contain target group
            }
        }
        return containedIn;
    }

    /** Checks whether an area is a valid area to count towards the points of a player. Checks
     * whether the area is within an area of the other player, in which case that area can only
     * be valid if it has two or more "eyes", calls methods required to determine this.
     * @param group //group being checked
     * @param otherGroups   //all potentially valid groups belonging to other player
     * @param type  //type of group being checked
     * @return  true if group is valid, otherwise false
    */
    public Boolean investigateArea(ArrayList<String> group,
            ArrayList<ArrayList<String>> otherGroups, int type) {

        //holds indices of groups that group being investigated in is contained in
        ArrayList<ArrayList<String>> containedIn = contains(group, otherGroups);

        if (containedIn.isEmpty()) {    //group is automatically valid if not surrounded by another
            return true;
        } else {
            checkForEyes(group, type);  //checks whether group has eyes
            if (hasEye) {
                eyes.add(group);    //storing eye
                eyeContainedIn.add(containedIn);    //storing groups surrounding the eye
                return true;    //group deemed valid if it has eyes
            } else {
                return false;
            }
        }
    }

    /** Saves the coordinates passed as parameters as "checked" so that
     * they aren't checked again.
     * 
     * @param row   row of element that has been checked
     * @param col   col of element that has been checked
     */
    public void addChecked(int row, int col) {
        String pair = (row + "" + col); //string value of pair of coordinates
        checked.add(pair);
    }

    /** Checks if a pair of coordinates has been checked before.
     * 
     * @param row   row of element
     * @param col   column of element
     * @return  whether or not element has been checked
     */
    public Boolean isChecked(int row, int col) {
        String pair = (row + "" + col); //converting into string value of coordinates for comparison
        return checked.contains(pair);
    }

    /** Checks whether group passed as parameter has any eyes.
     * 
     * @param group group being checked
     * @param type  player group belongs to
     */
    public void checkForEyes(ArrayList<String> group, int type) {
        checked = new ArrayList<String>();  //elements that have been checked
        hasEye = false;

        //adding all coordinates in group to checked so that they don't get checked again
        for (int i = 0; i < group.size(); i++) {
            checked.add(group.get(i));
        }
        //running checkEye for each member of the group
        for (int i = 0; i < group.size(); i++) {
            int row = Integer.parseInt(group.get(i).substring(0,1));
            int col = Integer.parseInt(group.get(i).substring(1));
            checkEye(row, col, type);
        }
        
    }

    /** Algorithm checks whether surroundings have been checked, and if they are the same color
     * as the group being checked or if they are empty.
     * If an empty tile is found next to group's borders, calls isEye method to see if it is an eye.
     * Calls function again if tile found is of the same color as the group passed
     * 
     * @param row   coordinate of tile
     * @param col   coordinate of tile
     * @param border    color of player who owns the group
     */
    public void checkEye(int row, int col, int border) {
        //checking tile above
        if (row + 1 <= 8 && !isChecked(row + 1, col) 
                && (boardPositions[row + 1][col] == border || boardPositions[row + 1][col] == 0)) {

            addChecked(row + 1, col);   //saves location as checked
            if (boardPositions[row + 1][col] == border) {
                //running checkEye again if tile is border color
                checkEye(row + 1, col, border);
            } else {
                //checking if empty space is an eye
                isEye(row + 1, col, border);
            }
        }
        //checking tile below
        if (row - 1 >= 0 && !isChecked(row - 1, col)
                && (boardPositions[row - 1][col] == border || boardPositions[row - 1][col] == 0)) {

            addChecked(row - 1, col);   //saves location as checked
            if (boardPositions[row - 1][col] == border) {
                //running checkEye again if tile is border color
                checkEye(row - 1, col, border);
            } else {
                //checking if empty space is an eye
                isEye(row - 1, col, border);
            }
        }
        //checking tile to the right
        if (col +  1 <= 8 && !isChecked(row, col + 1)
                && (boardPositions[row][col + 1] == border || boardPositions[row][col + 1] == 0)) {

            addChecked(row, col + 1);   //saves location as checked
            if (boardPositions[row][col + 1] == border) {
                //running checkEye again if tile is border color
                checkEye(row, col + 1, border);
            } else {
                //checking if empty space is an eye
                isEye(row, col + 1, border);
            }
        }
        //checking tile to the right
        if (col -  1 >= 0 && !isChecked(row, col - 1)
                && (boardPositions[row][col - 1] == border || boardPositions[row][col - 1] == 0)) {

            addChecked(row, col - 1);   //saves location as checked
            if (boardPositions[row][col - 1] == border) {
                //running checkEye again if tile is border color
                checkEye(row, col - 1, border);
            } else {
                //checking if empty space is an eye
                isEye(row, col - 1, border);
            }
        }
        return;        
    }

    /** Checks spaces surrounding a possible eye to see if the player owns all of them.
     * 
     * @param row   row of tile being checked
     * @param col   col of tile being checked
     * @param border    color of player/border
     */
    public void isEye(int row, int col, int border) {
        Boolean eye = true;
        //setting eye to false if  any of the surrounding tiles do not belong to the player.
        if (col - 1 >= 0 && boardPositions[row][col - 1] != border) {
            eye = false;
        }
        if (col + 1 <= 8 && boardPositions[row][col + 1] != border) {
            eye = false;
        }
        if (row - 1 >= 0 && boardPositions[row - 1][col] != border) {
            eye = false;
        }
        if (row + 1 <= 8 && boardPositions[row + 1][col] != border) {
            eye = false;
        }
        if (eye.equals(true)) {
            hasEye = true;
        }
    }
}