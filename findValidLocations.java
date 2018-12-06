//This method will go through the board to find good locations to place a word
// @return returns a 3D array where indicies 1&2 refer to location and 3 contains {maxHorizontal, minH, maxVertical, minV}
public int[][][] findValidLocations(Board b){
  int[][][] lengths = new int[15][15][4];
for(int i = 0; i<15; i++){
  for(int j = 0; j<15; j++){
    Location current = new Location(i,j);
  if(b.isOccupied(current)){
    for(int k = 0; k<4; k++){
      lengths[i][j][k] = 0;
    }
  }
   else{//check path 
      int[] check;
     check = findEmptyPath(b,current,HORIZONTAL);
      lengths[i][j][0] = check[0];
      lengths[i][j][1] = check[1];
      check = findEmptyPath(b,current,VERTICAL);
      lengths[i][j][2] = check[0];
      lengths[i][j][3] = check[1];
    }
  }
  }
  return lengths;
}
      public int[] findEmptyPath(board b, Location start, Location direction){
       int min = -1; // minimum length needed to play off
       int index = 0;
       Location current = start;
       while(index<9){
         if(!current.isOnBoard()){
                if(min == -1){
                    index = 9;
                    break;
                }
                else{
                    break;
                }
            }
            if(b.isOccupied(current)){ // found a tile
                break;
            }
            Location neighbor1 = current.neighbor(direction.opposite());
            Location antineighbor2 = current.antineighbor(direction.opposite());
            boolean check1 = neighbor1.isOnBoard() && b.isOccupied(neighbor1);
            boolean check2 = antineighbor2.isOnBoard() && b.isOccupied(antineighbor2);
          if(index == 0 && current.antineighbor(direction).isOnBoard() && b.isOccupied(current.antineighbor(direction))){
                min = 1;
            }
            if(min == -1 && (check1 || check2)){
                min = index+1;
            }
        index++;
        current = current.neighbor(direction);
       }
           int[] values = new int[2];
           if(min == -1 && index == 9){ //cannot play off
            values[0] = 0;
            values[1] = 0;
           }
           else if (min == -1 && index < 9){ // ends by ramming into a word, but nothing before then
             values[0] = index;
             values[1] = index;
           }
           else{
             values[0] = index;
             values[1] = min;
           }
           return values;
      }

