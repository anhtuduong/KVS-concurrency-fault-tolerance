package dwd_eist;


import java.util.List;

public class Tree {
    // Data stored in the node:
    private Entry entry;

    private Tree leftChild, rightChild;


    public Tree(int stationId) {
        this.entry = new Entry(stationId);
    }

    public Tree(Entry entry) {
        this.entry = entry;
    }

    public String toString() {
        return "(" + this.getEntry().getId() + "," + this.getEntry().getValues() + ")" + " " + (this.getLeftChild() != null ? this.getLeftChild().toString() : "") + " " + (this.getLeftChild() != null ? this.getRightChild().toString() : "");
    }

    public void putTemperature(int stationId, double temperature) {
        // TODO: part 2.1.
        if (stationId < entry.getId()) {
            if (leftChild == null) {
                leftChild = new Tree(stationId);
            }
            leftChild.putTemperature(stationId, temperature);
        } else if (stationId > entry.getId()) {
            if (rightChild == null) {
                rightChild = new Tree(stationId);
            }
            rightChild.putTemperature(stationId, temperature);
        } else {
            entry.addTemperature(temperature);
        }
    }

    public Tree getTreeNode(int stationId) {
        // TODO: part 2.2.
        if (entry.getId() == stationId) {
            return this;
        }
        if (stationId < entry.getId()) {
            return leftChild != null ? leftChild.getTreeNode(stationId) : null;
        } else {
            return rightChild != null ? rightChild.getTreeNode(stationId) : null;
        }
    }

    public Tree getParentNode(int stationId) {
        // TODO: part 2.3.
        if (leftChild != null && leftChild.getId() == stationId) {
            return this;
        }
        if (rightChild != null && rightChild.getId() == stationId) {
            return this;
        }
        if (stationId < entry.getId()) {
            return leftChild != null ? leftChild.getParentNode(stationId) : null;
        } else {
            return rightChild != null ? rightChild.getParentNode(stationId) : null;
        }
    }

    /**
     * Important methods for testing. Do NOT remove them, otherwise you WILL lose significant number of points.
     */
    public Entry getEntry() {
        return entry;
    }

    public Tree getLeftChild() {
        return leftChild;
    }

    public Tree getRightChild() {
        return rightChild;
    }

    public void setLeftChild(Tree leftChild) {
        this.leftChild = leftChild;
    }

    public void setRightChild(Tree rightChild) {
        this.rightChild = rightChild;
    }

    public int getId() {
        return entry.getId();
    }
}
