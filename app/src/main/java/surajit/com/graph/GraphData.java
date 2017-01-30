package surajit.com.graph;

/**
 * Created by Surajit Sarkar on 30/1/17.
 * Company : Bitcanny Technologies Pvt. Ltd.
 * Email   : surajit@bitcanny.com
 */

public class GraphData {

    enum Type{
        LINE,BAR
    }

    private int minX, maxX, y;
    private Type type;

    /**
     *
     * @param x minutes
     * @param y temp
     */
    public GraphData(int x, int y) {
        super();
        this.minX = x;
        this.y = y;
        type = Type.LINE;
    }

    /**
     *
     * @param minX start minute
     * @param maxX stop minute
     * @param y temp
     */
    public GraphData(int minX, int maxX, int y) {
        super();
        this.minX = minX;
        this.y = y;
        this.maxX = maxX;
        type = Type.BAR;
    }

    public int getX() {
        return minX;
    }

    public int getMinX(){
        return getX();
    }

    public int getMaxX() {
        return maxX;
    }

    public int getY() {
        return this.y;
    }

    public Type getType(){
        return type;
    }

}
