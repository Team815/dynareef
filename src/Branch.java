public class Branch extends Waypoint {
    private Pipe pipe;

    public Branch(int level, Pipe pipe) {
        super(level);
        this.pipe = pipe;
    }

    @Override
    public int getId() {
        return pipe.getId() * 10 + id;
    }

    public Pipe getPipe() {
        return pipe;
    }
}
