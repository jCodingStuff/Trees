import java.util.ArrayList;
import java.util.List;

/**
 * @author Julian Marrades
 * @version 0.1, 29/03/2018
 *
 * X is positive to the right, and Y is positive to the top
 * The square or rectangle or whatever has:
 *  - x, y: center
 *  - w: distance from the center to a horizontal limit
 *  - h: distance from the center to a vertical limit
 *  - capacity: number of points that can be held
 */
public class QuadTreeNode {

    private final float x;
    private final float y;
    private final float w;
    private final float h;
    private final int capacity;
    private List<Point> points;
    private QuadTreeNode[] subNodes; // 0 -> top-right, 1 -> top-left, 2 -> bot-left, 3 -> bot-right

    public QuadTreeNode(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.capacity = 4;
        this.points = new ArrayList<Point>();
    }

    public QuadTreeNode(float x, float y, float w, float h, int capacity) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.capacity = capacity;
        this.points = new ArrayList<Point>();
    }

    public List<Point> query(QuadTreeNode range) {
        List<Point> result = new ArrayList<Point>();
        if (this.intersects(range)) {
            if (!this.isDivided()) { // There is no more subdivisions
                for (Point point : this.points) {
                    if (range.contains(point)) result.add(point);
                }
            }
            else { // There exist subNodes
                for (QuadTreeNode node : this.subNodes) { // Query the subNodes and concat to result
                    concat(result, node.query(range));
                }
            }
        }
        return result;
    }

    public void insert(Point point) {
        if (this.contains(point)) {
            if (this.isDivided()) { // If it is divided, recursively call insert in the subNodes
                for (QuadTreeNode node : this.subNodes) {
                    node.insert(point);
                }
            }
            else { // It is not divided
                if (this.points.size() < this.capacity) { // There is capacity for more points
                    this.points.add(point);
                }
                else { // Capacity is full
                    this.subdivide(); // Create the subNodes
                    this.points.add(point); // Add the point for now
                    // Remove all the points adding them to subNodes
                    for (int i = this.points.size() - 1; i >= 0; i--) {
                        for (QuadTreeNode node : this.subNodes) {
                            node.insert(this.points.get(i));
                        }
                        this.points.remove(i);
                    }
                }
            }
        }
    }

    private boolean intersects(QuadTreeNode node) {
        return !(this.x + 2 * this.w < node.x ||
                 node.x + 2 * node.w < this.x ||
                 this.y + 2 * this.h < node.y ||
                 node.y + 2 * node.h < this.y);
    }

    private boolean contains(Point point) {
        return point.getX() >= this.x - this.w && point.getX() < this.x + this.w
                && point.getY() >= this.y - this.h && point.getY() < this.y + this.h;
    }

    private void subdivide() {
        this.subNodes = new QuadTreeNode[4];
        // TOP-RIGHT
        this.subNodes[0] = new QuadTreeNode(this.x + this.w / 2, this.y + this.h / 2, this.w / 2, this.h / 2,
                this.capacity);
        // TOP-LEFT
        this.subNodes[1] = new QuadTreeNode(this.x - this.w / 2, this.y + this.h / 2, this.w / 2, this.h / 2,
                this.capacity);
        // BOT-LEFT
        this.subNodes[2] = new QuadTreeNode(this.x - this.w / 2, this.y - this.h / 2, this.w / 2, this.h / 2,
                this.capacity);
        // BOT-RIGHT
        this.subNodes[3] = new QuadTreeNode(this.x + this.w / 2, this.y - this.h / 2, this.w / 2, this.h / 2,
                this.capacity);
    }



    public boolean isDivided() {
        return this.subNodes != null;
    }

    // GETTER AND SETTER AREA
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


    // STATIC HELPER AREA
    private static void concat(List<Point> list1, List<Point> list2) {
        for (Point point : list2) list1.add(point);
    }
}
