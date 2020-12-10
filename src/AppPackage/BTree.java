package AppPackage;

import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author nixrajput
 */
public class BTree {

    Node parentNode;

    public BTree(Node parentNode) {
        this.parentNode = parentNode;
    }

    public void addKey(Key newKey) {

        if (this.parentNode.keys[0] == null) { //If the parent node is empty in the beginning.
            parentNode.keys[0] = newKey;
        } else if (this.parentNode.keys[0].leftNode == null) { //If the new key is to be placed in parent node.

            int insertionPlace = -1;
            Node node = this.parentNode;
            for (var i = 0; i < node.keys.length; i++) {

                if (node.keys[i] == null) { //If we find a place for it in the arrray.
                    node.keys[i] = newKey;
                    insertionPlace = i;
                }

                if (insertionPlace != -1) { //If the new key has been inserted.

                    if (insertionPlace == node.keys.length - 1) { //If it is placed on the very right(limit) of the keys array. Then it should be splitted.

                        int middleIndex = node.keys.length / 2;
                        Key middleKey = node.keys[middleIndex];

                        Node rightNode = new Node(node.max);
                        Node leftNode = new Node(node.max);

                        int leftIndex = 0, rightIndex = 0;

                        //Making left and right node for middle node.
                        for (var j = 0; j < node.keys.length; j++) {

                            if (node.keys[j].value < middleKey.value) {

                                leftNode.keys[leftIndex] = node.keys[j];
                                leftIndex++;
                                node.keys[j] = null;

                            } else if (node.keys[j].value > middleKey.value) {

                                rightNode.keys[rightIndex] = node.keys[j];
                                rightIndex++;
                                node.keys[j] = null;
                            }
                        }
                        node.keys[middleIndex] = null;

                        //After making left and right node set child and parent relationship.
                        this.parentNode = node;
                        this.parentNode.keys[0] = middleKey;

                        leftNode.parentNode = this.parentNode;
                        rightNode.parentNode = this.parentNode;

                        middleKey.leftNode = leftNode;
                        middleKey.rightNode = rightNode;

                        //break the loop.
                        break;

                    } else { //Else it is fine.
                        break;
                    }
                }
            }

            //If the new key is to be placed not in the parent node.
        } else if (this.parentNode.keys[0].leftNode != null) {

            Node node = this.parentNode;

            //While we reach to the leaf node.
            while (node.keys[0].leftNode != null) {

                int loop = 0;
                //If the value is new key value is greater than a node then send it to the right side else send it to the left side.
                for (var i = 0; i < node.keys.length; i++, loop++) {
                    //Not to check the value if keys contains null at that place to avoid null pointer exception. Else it means that we need to go to the right side.
                    if (node.keys[i] != null) {
                        if (node.keys[i].value > newKey.value) {//Moving toward left side.
                            node = node.keys[i].leftNode;
                            break;
                        }
                    } else {//Moving toward right side.
                        node = node.keys[i - 1].rightNode;
                        break;
                    }
                }
                //Checking if the new value is greater then all keys value in that node then it means that new key will definitely goes to right side.
                if (loop == node.keys.length) {
                    //Moving toward right side.
                    node = node.keys[loop - 1].rightNode;
                }

            }

            //Now we have found the node where we have to place the new key.
            //Add new key in to the node
            int indexPlaced = placeNode(node, newKey);

            //If the new key is placed on the very right side of the keys array then we have to split it again.
            if (indexPlaced == node.keys.length - 1) {

                //While we adjust all nodes till parent node.
                while (node.parentNode != null) {//Move till we get parent node.

                    int middleIndex = node.keys.length / 2;

                    //Find out the median key.
                    Key middleKey = node.keys[middleIndex];
                    //Make left node.
                    Node leftNode = new Node(node.max);
                    //Make right node.
                    Node rightNode = new Node(node.max);
                    int leftIndex = 0, rightIndex = 0;

                    //Placing keys in the left or right nodes of adjusted node.
                    for (var i = 0; i < node.keys.length; i++) {

                        if (node.keys[i].value < middleKey.value) {

                            leftNode.keys[leftIndex] = node.keys[i];
                            leftIndex++;
                            node.keys[i] = null;

                        } else if (node.keys[i].value > middleKey.value) {

                            rightNode.keys[rightIndex] = node.keys[i];
                            rightIndex++;
                            node.keys[i] = null;
                        }
                    }

                    node.keys[middleIndex] = null;
                    middleKey.leftNode = leftNode;
                    middleKey.rightNode = rightNode;

                    node = node.parentNode;

                    leftNode.parentNode = node;
                    rightNode.parentNode = node;

                    for (Key key : leftNode.keys) {
                        if (key != null) {
                            if (key.leftNode != null) {
                                key.leftNode.parentNode = leftNode;
                            }
                            if (key.rightNode != null) {
                                key.rightNode.parentNode = leftNode;
                            }
                        }
                    }

                    for (Key key : rightNode.keys) {
                        if (key != null) {
                            if (key.leftNode != null) {
                                key.leftNode.parentNode = rightNode;
                            }
                            if (key.rightNode != null) {
                                key.rightNode.parentNode = rightNode;
                            }
                        }
                    }

                    int placedIndex = placeNode(node, middleKey);
                    if (placedIndex == node.keys.length - 1) {

                        if (node.parentNode == null) {

                            int middleIndexRoot = node.keys.length / 2;
                            Key middleKeyRoot = node.keys[middleIndexRoot];
                            Node leftNodeRoot = new Node(node.max);
                            Node rightNodeRoot = new Node(node.max);
                            int leftIndexRoot = 0;
                            int rightIndexRoot = 0;

                            //Placing keys in the left or right nodes of adjusted node.
                            for (int i = 0; i < node.keys.length; i++) {

                                if (node.keys[i].value < middleKeyRoot.value) {

                                    leftNodeRoot.keys[leftIndexRoot] = node.keys[i];
                                    leftIndexRoot++;
                                    node.keys[i] = null;

                                } else if (node.keys[i].value > middleKeyRoot.value) {

                                    rightNodeRoot.keys[rightIndexRoot] = node.keys[i];
                                    rightIndexRoot++;
                                    node.keys[i] = null;
                                }
                            }
                            node.keys[middleIndexRoot] = null;
                            node.keys[0] = middleKeyRoot;

                            for (Key key : leftNodeRoot.keys) {
                                if (key != null) {
                                    key.leftNode.parentNode = leftNodeRoot;
                                    key.rightNode.parentNode = leftNodeRoot;
                                }
                            }

                            for (Key key : rightNodeRoot.keys) {
                                if (key != null) {
                                    key.leftNode.parentNode = rightNodeRoot;
                                    key.rightNode.parentNode = rightNodeRoot;
                                }
                            }
                            middleKeyRoot.leftNode = leftNodeRoot;
                            middleKeyRoot.rightNode = rightNodeRoot;

                            leftNodeRoot.parentNode = node;
                            rightNodeRoot.parentNode = node;

                            this.parentNode = node;

                        }
                    } else {
                        break;
                    }
                }
            }
        }
    }

    public int placeNode(Node node, Key newKey) {

        int indexPlaced = -1;
        for (var i = 0; i < node.keys.length; i++) {

            //Finding the position for new key.
            if (node.keys[i] == null) {

                boolean placed = false;
                //Finding the right place to put the new key in to root node.
                for (var j = i - 1; j >= 0; j--) {

                    if (node.keys[j].value > newKey.value) {
                        node.keys[j + 1] = node.keys[j];
                    } else {
                        node.keys[j + 1] = newKey;
                        node.keys[j].rightNode = newKey.leftNode;

                        placed = true;
                        break;
                    }
                }
                //If we couldn't place the new key it means that it is to be placed on the fist place.
                if (placed == false) {
                    node.keys[0] = newKey;
                    node.keys[1].leftNode = newKey.rightNode;
                }
                //Note down the position of the newly placed node.
                indexPlaced = i;
                break;
            }
        }

        return indexPlaced;
    }

    ArrayList<Integer> xaxis = new ArrayList<>();

    //Printing the keys in each node. INORDER printing.
    public void printKeys(Key[] keys, int startX, int startY) {

        //Iterating through all keys of a node.
        for (Key key : keys) {
            //Firstly printing the left most keys of all nodes.
            if (key != null) {
                if (key.leftNode != null) {
                    //System.out.println("Left: X = " + startX +"    Y = " + startY);
                    printKeys(key.leftNode.keys, startX - 20, startY + 20);
                }
            }
            //Print the central value.
            if (key != null) {
                if (xaxis.contains(startX)) {
                    System.out.println("Parent: X = " + (startX + 20) + "    Y = " + startY);
                } else {
                    System.out.println("Parent: X = " + startX + "    Y = " + startY);
                }
                xaxis.add(startX);
                System.out.println(key.value);
            }
            //At last print the right keys of each node.
            if (key != null) {
                if (key.rightNode != null) {
                    //System.out.println("Right: X = " + startX +"    Y = " + startY);
                    if (xaxis.contains(startX)) {
                        printKeys(key.rightNode.keys, startX + 20, startY + 20);
                    } else {
                        printKeys(key.rightNode.keys, startX, startY + 20);
                    }
                }
            }
        }
        //Print end line after printing each keys of a node.
        System.out.println();
    }

    public void deleteKey(int key) {

        Node node = parentNode;
        int index = -1;

        while (node != null) {

            boolean found = false;
            for (int i = 0; i < node.keys.length; i++) {

                if (node.keys[i] == null) {

                    node = node.keys[i - 1].rightNode;
                    break;
                } else if (node.keys[i].value > key) {
                    node = node.keys[i].leftNode;
                    break;
                } else if (node.keys[i].value == key) {
                    index = i;
                    found = true;
                    break;
                }
            }

            if (found == true) {
                break;
            }
        }

        //If we haven't find any key to delete.
        if (index == -1) {
            System.out.println("");
            JOptionPane.showMessageDialog(null, "Node Not Found!!!");
        } //If we just have to delete the key in the node and to need do nothing with rest of the tree.
        else if (node.keys[index].leftNode == null && node.keys[1] != null) {
            node.keys[index] = null;

            for (var i = index; i < node.keys.length - 1; i++) {

                node.keys[i] = node.keys[i + 1];
            }
            node.keys[node.keys.length - 1] = null;

            //When deleting a leaf node key we have to to remove the whole node.
        } else if (node.keys[index].leftNode == null && node.keys[1] == null) {

            Key rightKey = null, leftKey = null;
            Node nodeParent = node.parentNode;

            for (Key key1 : nodeParent.keys) {
                if (key1 != null) {
                    if (key1.rightNode == node) {
                        rightKey = key1;
                    } else if (key1.leftNode == node) {
                        leftKey = key1;
                    }
                }
            }

            if (leftKey != null && leftKey.rightNode.keys[1] != null) { //If there are more then one element in right side then get borrow from right side.

                node.keys[0].value = leftKey.value;
                leftKey.value = leftKey.rightNode.keys[0].value;

                for (int i = 0; i < leftKey.rightNode.keys.length - 1; i++) {
                    leftKey.rightNode.keys[i] = leftKey.rightNode.keys[i + 1];
                }
                leftKey.rightNode.keys[leftKey.rightNode.keys.length - 1] = null;

            } else if (rightKey != null && rightKey.leftNode.keys[1] != null) {//If there are more then one element in left side then get borrow from left side.

                node.keys[0].value = rightKey.value;

                for (var i = 0; i < rightKey.leftNode.keys.length; i++) {

                    if (rightKey.leftNode.keys[i] == null) {
                        rightKey.value = rightKey.leftNode.keys[i - 1].value;
                        rightKey.leftNode.keys[i - 1] = null;
                        break;
                    }
                }

            } else if (leftKey != null && rightKey == null && nodeParent.keys[1] != null) {

                Node rightNode = leftKey.rightNode;
                leftKey.rightNode = null;
                leftKey.leftNode = null;

                for (var i = 0; i < rightNode.keys.length; i++) {

                    if (rightNode.keys[i] == null) {

                        for (int j = i; j > 0; j--) {
                            rightNode.keys[j] = rightNode.keys[j - 1];
                        }
                        break;
                    }
                }

                rightNode.keys[0] = leftKey;
                for (var i = 0; i < nodeParent.keys.length - 1; i++) {

                    nodeParent.keys[i] = nodeParent.keys[i + 1];
                }
                nodeParent.keys[nodeParent.keys.length - 1] = null;

            } else if (leftKey != null && rightKey != null) {

                Node leftNode = rightKey.leftNode;

                rightKey.leftNode = null;
                rightKey.rightNode = null;

                for (int i = 0; i < leftNode.keys.length; i++) {
                    if (leftNode.keys[i] == null) {
                        leftNode.keys[i] = rightKey;
                        break;
                    }
                }

                leftKey.leftNode = leftNode;

                for (var i = 0; i < nodeParent.keys.length; i++) {

                    if (nodeParent.keys[i] == rightKey) {

                        for (int j = i; j < nodeParent.keys.length - 1; j++) {

                            nodeParent.keys[j] = nodeParent.keys[j + 1];
                        }
                        nodeParent.keys[nodeParent.keys.length - 1] = null;
                        break;
                    }
                }

            } else if (rightKey != null && leftKey == null && nodeParent.keys[1] != null) {

                Node leftNode = rightKey.leftNode;
                rightKey.leftNode = null;
                rightKey.rightNode = null;

                for (int i = 0; i < nodeParent.keys.length; i++) {

                    if (nodeParent.keys[i].value == rightKey.value) {
                        nodeParent.keys[i] = null;
                        break;
                    }
                }

                for (int i = 0; i < leftNode.keys.length; i++) {

                    if (leftNode.keys[i] == null) {
                        leftNode.keys[i] = rightKey;
                        break;
                    }
                }

            } else { //Delete node and merge the above nodes till parent node.
                System.out.println("Delete node and merge the above nodes till parent node");
                Node newNode = null;
                while (nodeParent != null) {
                    Key keyToMerge = null;
                    int loopBreak = 2;
                    for (int i = 0; i < nodeParent.keys.length; i++) {
                        if (nodeParent.keys[i].rightNode == node) {

                            if (key == 12) {
                            }
                            if (i > 0) {
                                loopBreak = 1;
                            } else {
                                loopBreak = 0;
                            }
                            keyToMerge = nodeParent.keys[i];
                            if (keyToMerge.leftNode.keys[1] == null) {

                                for (int y = i; y < nodeParent.keys.length - 1; y++) {
                                    nodeParent.keys[y] = nodeParent.keys[y + 1];
                                }
                                nodeParent.keys[nodeParent.keys.length - 1] = null;
                                for (int j = 0; j < keyToMerge.leftNode.keys.length; j++) {

                                    if (keyToMerge.leftNode.keys[j] == null) {
                                        Node leftNode = keyToMerge.leftNode;
                                        keyToMerge.leftNode.keys[j] = keyToMerge;
                                        keyToMerge.leftNode = keyToMerge.leftNode.keys[j - 1].rightNode;
                                        keyToMerge.rightNode = newNode;

                                        if (newNode != null) {
                                            newNode.parentNode = leftNode;
                                        }

                                        newNode = leftNode;
                                        if (nodeParent.parentNode == null && nodeParent.keys[0] == null) {
                                            this.parentNode = newNode;
                                        }
                                        break;
                                    }
                                }

                                if (nodeParent.keys[i] != null) {

                                    nodeParent.keys[i].leftNode = newNode;
                                    if (i - 1 > 0) {

                                        nodeParent.keys[i - 1].rightNode = newNode;
                                    }
                                    loopBreak = 1;
                                }

                            } else if (keyToMerge.leftNode.keys[1] != null) {

                                Key rightMostKey = null;
                                for (int m = 0; m < keyToMerge.leftNode.keys.length; m++) {

                                    if (keyToMerge.leftNode.keys[m] == null) {
                                        rightMostKey = keyToMerge.leftNode.keys[m - 1];
                                        keyToMerge.leftNode.keys[m - 1] = null;
                                        break;
                                    }
                                }
                                rightMostKey.leftNode = keyToMerge.leftNode;
                                keyToMerge.leftNode = rightMostKey.rightNode;
                                keyToMerge.leftNode.parentNode = node;
                                rightMostKey.rightNode = node;
                                node.keys[0] = keyToMerge;
                                keyToMerge.rightNode = newNode;
                                newNode.parentNode = node;
                                nodeParent.keys[i] = rightMostKey;
                                loopBreak = 1;

                            }
                        } else if (nodeParent.keys[i].leftNode == node) {

                            if (i > 0) {
                                loopBreak = 1;
                            } else {
                                loopBreak = 0;
                            }
                            keyToMerge = nodeParent.keys[i];

                            if (keyToMerge.rightNode.keys[1] == null) {
                                for (int y = i; y < nodeParent.keys.length - 1; y++) {
                                    nodeParent.keys[y] = nodeParent.keys[y + 1];
                                }
                                nodeParent.keys[nodeParent.keys.length - 1] = null;
                                for (int j = 0; j < keyToMerge.rightNode.keys.length; j++) {

                                    if (keyToMerge.rightNode.keys[j] == null) {

                                        Node rightNode = keyToMerge.rightNode;
                                        for (int k = j; k > 0; k--) {
                                            rightNode.keys[k] = rightNode.keys[k - 1];
                                        }
                                        rightNode.keys[0] = keyToMerge;

                                        keyToMerge.rightNode = keyToMerge.rightNode.keys[1].leftNode;
                                        keyToMerge.leftNode = newNode;

                                        if (newNode != null) {

                                            newNode.parentNode = rightNode;
                                        }

                                        newNode = rightNode;
                                        if (nodeParent.parentNode == null && nodeParent.keys[0] == null) {
                                            this.parentNode = newNode;
                                        }
                                        break;
                                    }
                                }

                                if (nodeParent.keys[i] != null) {

                                    nodeParent.keys[i].leftNode = newNode;
                                    if (i - 1 > 0) {

                                        nodeParent.keys[i - 1].rightNode = newNode;
                                    }
                                    loopBreak = 1;
                                }

                            } else if (keyToMerge.rightNode.keys[1] != null) {

                                System.out.println("Attention here....");
                            }

                        }

                        if (loopBreak == 0) {
                            node = nodeParent;
                            nodeParent = nodeParent.parentNode;
                            newNode.parentNode = nodeParent;
                            break;
                        } else if (loopBreak == 1) {
                            nodeParent = null;
                            break;
                        }
                    }
                }
            }

        } else if (node.keys[index].leftNode != null && node.keys[index].rightNode != null) {
            System.out.println("Not a leaf node........");
            JOptionPane.showMessageDialog(null, "Not a Leaf Node!!!");
            Node nodeParent = node.parentNode;

            if (node.keys[index].leftNode.keys[1] != null) {
                System.out.println("Left side contains more than one node.");
                JOptionPane.showMessageDialog(null, "Left side contains more than one Node!!!");

            } else if (node.keys[index].rightNode.keys[1] != null) {
                System.out.println("Right side contains more than one node.");
                JOptionPane.showMessageDialog(null, "Right side contains more than one Node!!!");
            }
        }
    }

    public Key[] getParentKey() {
        return parentNode.keys;
    }
}
