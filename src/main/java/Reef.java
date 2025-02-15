import java.util.Arrays;
import java.util.stream.IntStream;

public class Reef {
    private final Pipe[] pipes = IntStream.rangeClosed(0, 11).mapToObj(Pipe::new).toArray(Pipe[]::new);
    private final Pipe[] topPipes = new Pipe[]{pipes[8], pipes[9], pipes[10], pipes[11]};
    private final Pipe[] bottomPipes = new Pipe[]{pipes[2], pipes[3], pipes[4], pipes[5]};
    private final Pipe[] middlePipes = new Pipe[]{pipes[0], pipes[1], pipes[6], pipes[7]};
    private Pipe[] startingPipes = new Pipe[]{pipes[4], pipes[5], pipes[6], pipes[7], pipes[8], pipes[9]};

    public enum PipePosition {
        TOP,
        BOTTOM,
        MIDDLE
    }

    public Branch[] getAllBranches() {
        return getBranches(pipes);
    }

    public Branch[] getStartingBranches() {
        return getBranches(startingPipes);
    }

    public Branch[] getBranchesAtPosition(PipePosition[] positions) {
        return Arrays
                .stream(positions)
                .flatMap(position -> switch (position) {
                    case TOP -> Arrays.stream(getBranches(topPipes));
                    case MIDDLE -> Arrays.stream(getBranches(middlePipes));
                    case BOTTOM -> Arrays.stream(getBranches(bottomPipes));
                })
                .toArray(Branch[]::new);
    }

    public PipePosition getPipePosition(Pipe pipe) {
        return Arrays.asList(topPipes).contains(pipe) ? PipePosition.TOP
                : Arrays.asList(bottomPipes).contains(pipe) ? PipePosition.BOTTOM
                : PipePosition.MIDDLE;
    }

    private static Branch[] getBranches(Pipe[] pipes) {
        return Arrays.stream(pipes).flatMap(pipe -> Arrays.stream(pipe.getBranches())).toArray(Branch[]::new);
    }
}
