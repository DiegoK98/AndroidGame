package dadm.scaffold.engine;

public class Vector {

    public double x;
    public double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double magnitude() {
        double squares = Math.pow(x, 2) + Math.pow(y, 2);
        return Math.sqrt(squares);
    }

    public double angle() {
        return Math.atan2(y,x) - Math.atan2(0,1);
    }

    public void clamp(double limit) {
        if(this.magnitude() > limit) {
            x = x * (limit / magnitude()) * Math.abs(Math.cos(angle()));
            y = y * (limit / magnitude()) * Math.abs(Math.sin(angle()));
        }
    }
}
