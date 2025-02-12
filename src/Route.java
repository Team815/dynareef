import java.util.*;

public class Route {
    private final Reef reef = new Reef();
    private final Waypoint topCoralStation = new Waypoint(200);
    private final Waypoint bottomCoralStation = new Waypoint(300);
    private final Waypoint startTop = new Waypoint(400);
    private final Waypoint startMiddle = new Waypoint(500);
    private final Waypoint startBottom = new Waypoint(600);
    private final Stack<Waypoint> waypoints = new Stack<>();
    private Branch lastBranch;

    public Branch[] nextValidBranches() {
        if (waypoints.isEmpty()) {
            return reef.getStartingBranches();
        }
        var lastPosition = reef.getPipePosition(lastBranch.getPipe());
        var validBranches = switch (lastPosition) {
            case MIDDLE -> reef.getAllBranches();
            case TOP, BOTTOM ->
                    reef.getBranchesAtPosition(new Reef.PipePosition[]{lastPosition, Reef.PipePosition.MIDDLE});
        };
        return Arrays.stream(validBranches).filter(branch -> !waypoints.contains(branch)).toArray(Branch[]::new);
    }

    public void addBranch(Branch branch) {
        if (waypoints.isEmpty()) {
            var start = switch (reef.getPipePosition(branch.getPipe())) {
                case TOP -> startTop;
                case MIDDLE -> startMiddle;
                case BOTTOM -> startBottom;
            };
            waypoints.add(start);
        } else {
            var lastBranchPosition = reef.getPipePosition(lastBranch.getPipe());
            var coralStation = switch (lastBranchPosition) {
                case TOP, BOTTOM -> getClosestCoralStationTo(lastBranchPosition);
                case MIDDLE -> {
                    var nextBranchPosition = reef.getPipePosition(branch.getPipe());
                    yield getClosestCoralStationTo(nextBranchPosition);
                }
            };
            waypoints.add(coralStation);
        }
        waypoints.add(branch);
        lastBranch = branch;
        publishPath();
    }

    public Branch removeBranch() {
        var removedBranch = waypoints.pop();
        waypoints.pop(); // Remove the corresponding coral station waypoint or start waypoint
        publishPath();
        return (Branch) removedBranch;
    }

    public Waypoint getClosestCoralStationTo(Reef.PipePosition position) {
        return switch (position) {
            case TOP -> topCoralStation;
            case BOTTOM, MIDDLE -> bottomCoralStation;
        };
    }

    public Reef getReef() {
        return reef;
    }

    public int getBranchCount() {
        return waypoints.size() / 2;
    }

    private void publishPath() {
        var pathIds = getPathIds();
        for (var id : pathIds) {
            System.out.print(id + " ");
        }
        System.out.println();
    }

    private long[] getPathIds() {
        if (waypoints.isEmpty()) {
            return new long[0];
        }
        var pathIds = new long[waypoints.size() - 1];
        for (var i = 0; i < pathIds.length; i++) {
            var from = waypoints.get(i);
            var to = waypoints.get(i + 1);
            var pathId = from.getId() * 1000 + to.getId();
            pathIds[i] = pathId;
        }
        return pathIds;
    }
}
