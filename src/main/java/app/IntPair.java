package app;

public class IntPair implements Comparable<IntPair> {
    private Integer x,y;

    public IntPair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }


    @Override
    public int compareTo(IntPair instance) {
        if (this.getX() == instance.getX() && this.getY() == instance.getY()) {
            return 0;
        } else if (this.getX() > instance.getX() || this.getY() > instance.getY()) {
            return 1;
        }
        return -1;
    }
}
