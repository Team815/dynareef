import javax.swing.*;

public class BranchButton extends JButton {
    private final Branch branch;

    public BranchButton(Branch branch, String label) {
        super(label);
        this.branch = branch;
    }

    public Branch getBranch() {
        return branch;
    }
}