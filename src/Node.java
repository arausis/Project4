public class Node extends Point{
    public int gscore = 0;
    public int hscore;
    public Node previous;
    public Node(Point p) {
        super(p.x, p.y);
    }

}
