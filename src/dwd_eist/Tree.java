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
        // If the stationId is less than the current node's id, go to the left subtree.
        if (stationId < entry.getId()) {
            // If the left child doesn't exist, create it.
            if (leftChild == null) {
                leftChild = new Tree(stationId);
            }
            // Recursively call putTemperature on the left child.
            leftChild.putTemperature(stationId, temperature);
        // If the stationId is greater than the current node's id, go to the right subtree.
        } else if (stationId > entry.getId()) {
            // If the right child doesn't exist, create it.
            if (rightChild == null) {
                rightChild = new Tree(stationId);
            }
            // Recursively call putTemperature on the right child.
            rightChild.putTemperature(stationId, temperature);
        // If the stationId is equal to the current node's id, we've found the correct node.
        } else {
            // Add the new temperature to the entry of the current node.
            entry.addTemperature(temperature);
        }
    }

    public Tree getTreeNode(int stationId) {
        // TODO: part 2.2.
        // If the current node's id matches the stationId, we've found the node.
        if (entry.getId() == stationId) {
            return this;
        }
        // If the stationId is less than the current node's id, search in the left subtree.
        if (stationId < entry.getId()) {
            // If the left child exists, recursively search in it. Otherwise, the node is not in the tree.
            return leftChild != null ? leftChild.getTreeNode(stationId) : null;
        } else {
            // If the stationId is greater, search in the right subtree.
            // If the right child exists, recursively search in it. Otherwise, the node is not in the tree.
            return rightChild != null ? rightChild.getTreeNode(stationId) : null;
        }
    }

    public Tree getParentNode(int stationId) {
        // TODO: part 2.3.
        // Check if the left or right child's ID matches the stationId. If so, the current node is the parent.
        if (leftChild != null && leftChild.getId() == stationId) {
            return this;
        }
        if (rightChild != null && rightChild.getId() == stationId) {
            return this;
        }
        // If the stationId is less than the current node's id, the parent must be in the left subtree.
        if (stationId < entry.getId()) {
            // If the left child exists, recursively search for the parent in the left subtree.
            return leftChild != null ? leftChild.getParentNode(stationId) : null;
        } else {
            // Otherwise, the parent must be in the right subtree.
            // If the right child exists, recursively search for the parent in the right subtree.
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
