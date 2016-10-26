package ppg.experiment.wesnoth.chat.wml;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class WMLNode {
    private String node;
    private WMLNode parent;
    private Map<String, String> attributes;
    private List<WMLNode> children;

    public WMLNode(String node, WMLNode parent) {
        this.node = node;
        this.parent = parent;
        attributes = new HashMap<>();
        children = new LinkedList<>();
    }

    public String getNode() {
        return node;
    }

    public WMLNode getParent() {
        return parent;
    }

    @Override
    public String toString() {
        return "WMLNode [node=" + node + ", attributes=" + attributes
                + ", children=" + children + "]";
    }

    public void addAttribute(String currentAttribute, String sequence) {
        attributes.put(currentAttribute, sequence);
    }

    public String getAttribute(String string) {
        return this.attributes.get(string);
    }

    public void addChild(WMLNode node) {
        children.add(node);
    }

    public List<WMLNode> getChildren(String node) {
        List<WMLNode> result = new LinkedList<>();
        for (WMLNode child : children) {
            if (node.equals(child.getNode())) {
                result.add(child);
            }
        }
        return result;
    }

}
