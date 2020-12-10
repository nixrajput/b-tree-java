package AppPackage;

/**
 *
 * @author nixrajput
 */
public class Node {

    int nodeLevel;
    int max;
    Node parentNode;
    Key[] keys;
    Key parentKey;

    public Node(int max) {

        this.max = max;
        keys = new Key[max];
        parentNode = null;
    }
}
