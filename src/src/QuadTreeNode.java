public class QuadTreeNode {

    private final float x;
    private final float y;
    private final float w;
    private final float h;
    private final int capacity;
    private QuadTreeNode[] subNodes; // 0 -> top-right, 1 -> top-left, 2 -> bot-left, 3 -> bot-right

    public QuadTreeNode(float x, float y, float w, float h, int capacity) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.capacity = capacity;
    }

    private void subdivide() {
        this.subNodes = new QuadTreeNode[4];
    }



    public boolean isDivided() {
        return this.subNodes == null;
    }

    // GETTER AND SETTER AREA!!!
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getW() {
        return w;
    }

    public float getH() {
        return h;
    }

    public int getCapacity() {
        return capacity;
    }
}
