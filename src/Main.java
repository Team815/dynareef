import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    final int hSpacing = 88;
    final int vSpacing = 46;
    final Route route = new Route();
    final BranchButton[] buttons;
    final JButton undoButton;

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        var reef = route.getReef();
        var branches = reef.getAllBranches();
        var frame = new JFrame("Dynareef");
        frame.setSize(1200, 700);
        final int x = 500;
        final int y = 400;

        buttons = Stream.of(
                createPipeButtons(Arrays.copyOfRange(branches, 0, 4), x, y, 0, 1),
                createPipeButtons(Arrays.copyOfRange(branches, 4, 8), x + hSpacing, y, 0, 1),
                createPipeButtons(Arrays.copyOfRange(branches, 8, 12), x + 2 * hSpacing, y - vSpacing, 1, 1),
                createPipeButtons(Arrays.copyOfRange(branches, 12, 16), x + 3 * hSpacing, y - 2 * vSpacing, 1, 1),
                createPipeButtons(Arrays.copyOfRange(branches, 16, 20), x + 3 * hSpacing, y - 3 * vSpacing, 1, -1),
                createPipeButtons(Arrays.copyOfRange(branches, 20, 24), x + 2 * hSpacing, y - 4 * vSpacing, 1, -1),
                createPipeButtons(Arrays.copyOfRange(branches, 24, 28), x + hSpacing, y - 5 * vSpacing, 0, -1),
                createPipeButtons(Arrays.copyOfRange(branches, 28, 32), x, y - 5 * vSpacing, 0, -1),
                createPipeButtons(Arrays.copyOfRange(branches, 32, 36), x - hSpacing, y - 4 * vSpacing, -1, -1),
                createPipeButtons(Arrays.copyOfRange(branches, 36, 40), x - 2 * hSpacing, y - 3 * vSpacing, -1, -1),
                createPipeButtons(Arrays.copyOfRange(branches, 40, 44), x - 2 * hSpacing, y - 2 * vSpacing, -1, 1),
                createPipeButtons(Arrays.copyOfRange(branches, 44, 48), x - hSpacing, y - vSpacing, -1, 1)
        ).flatMap(Stream::of).toArray(BranchButton[]::new);

        for (var button : buttons) {
            frame.add(button);
        }

        undoButton = new JButton("Undo");
        undoButton.setBounds(4, 4, 64, 24);
        undoButton.addActionListener(e -> {
            var branch = route.removeBranch();
            var branchButton = Arrays
                    .stream(buttons)
                    .filter(button -> button.getBranch().equals(branch))
                    .findAny()
                    .orElseThrow();
            branchButton.setText(branchButton.getText().substring(0, 2));
            branchButton.setBackground(null);
            updateButtons();
        });
        frame.add(undoButton);

        updateButtons();

        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private BranchButton[] createPipeButtons(Branch[] branches, int x, int y, int hDirection, int vDirection) {
        return IntStream
                .range(0, branches.length)
                .mapToObj(i -> createButton(
                        branches[i],
                        "L" + i,
                        x + hSpacing * hDirection * i,
                        y + vSpacing * vDirection * i))
                .toArray(BranchButton[]::new);
    }

    private BranchButton createButton(Branch branch, String label, int x, int y) {
        final int width = 84;
        final int height = 42;
        var button = new BranchButton(branch, label);
        button.setBounds(x, y, width, height);
        button.setMargin(new Insets(0, 0, 0, 0));
        var defaultFont = button.getFont();
        button.setFont(new Font(defaultFont.getFontName(), defaultFont.getStyle(), 24));
        button.addActionListener(e -> {
            route.addBranch(button.getBranch());
            button.setBackground(new Color(255, 200, 200));
            button.setText(button.getText() + " (" + route.getBranchCount() + ")");
            updateButtons();
        });
        return button;
    }

    private void updateButtons() {
        var validBranches = route.nextValidBranches();
        for (var button : buttons) {
            button.setEnabled(Arrays.asList(validBranches).contains(button.getBranch()));
        }
        undoButton.setEnabled(route.getBranchCount() != 0);
    }
}