package ppg.experiment.wesnoth.chat.wml;

public class WMLNode {
    private String node;
    private WMLNode parent;

    public WMLNode(String node, WMLNode parent) {
        this.node = node;
        this.parent = parent;
    }

    public String getNode() {
        return node;
    }

    public WMLNode getParent() {
        return parent;
    }

    @Override
    public String toString() {
        return "WMLNode [node=" + node + "]";
    }

}
