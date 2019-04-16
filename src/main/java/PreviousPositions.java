import java.util.ArrayList;

class PreviousPositions {
    ArrayList<Integer> previousX;
    ArrayList<Integer> previousY;
    ArrayList<Integer> previousWidth;
    ArrayList<Integer> previousHeight;

    PreviousPositions() {
        previousX = new ArrayList<Integer>();
        previousY = new ArrayList<Integer>();
        previousWidth = new ArrayList<Integer>();
        previousHeight = new ArrayList<Integer>();
    }
    Integer getAverageX(){
        Integer sum = 0;
        for (Integer x:previousX) {
            sum+= x;

        }
        return sum/previousX.size();
    }
    Integer getAverageY(){
        Integer sum = 0;
        for (Integer x:previousY) {
            sum+= x;

        }
        return sum/previousX.size();
    }
    Integer getAverageWidth(){
        Integer sum = 0;
        for (Integer x:previousWidth) {
            sum+= x;

        }
        return sum/previousX.size();
    }
    Integer getAverageHeight(){
        Integer sum = 0;
        for (Integer x:previousHeight) {
            sum+= x;

        }
        return sum/previousX.size();
    }

}
