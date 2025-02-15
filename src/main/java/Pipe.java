import java.util.stream.IntStream;

public class Pipe {
    private final int id;
    private final Branch[] branches = IntStream
            .rangeClosed(0, 3)
            .mapToObj(i -> new Branch(i, this))
            .toArray(Branch[]::new);

    public Pipe(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Branch[] getBranches() {
        return branches;
    }
}
