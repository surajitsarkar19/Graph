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

    private float minX, maxX, y;
    private Type type;

    public GraphData(float x, float y) {
        super();
        this.minX = x;
        this.y = y;
        type = Type.LINE;
    }

    public GraphData(float minX, float maxX, float y) {
        super();
        this.minX = minX;
        this.y = y;
        this.maxX = maxX;
        type = Type.BAR;
    }

    public float getX() {
        float val = (float) minX/60;
        return val;
    }

    public float getMinX(){
        return getX();
    }

    public float getMaxX() {
        float val = (float) maxX/60;
        return val;
    }

    public float getY() {
        return this.y;
    }

    public Type getType(){
        return type;
    }

}
