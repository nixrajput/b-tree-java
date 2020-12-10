package AppPackage;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.*;
import no.geosoft.cc.graphics.*;

public final class B_Tree_GUI extends JFrame implements GInteraction {

    private static final long serialVersionUID = 1L;

    GScene scene_;
    GWindow window = new GWindow();
    private BTree bTree = new BTree(new Node(3));
    private final JButton button = new JButton("Add Node");
    private final JButton delButton = new JButton("Delete");
    private JTextField textField = new JTextField("", 20);
    private final JLabel label = new JLabel("Enter New Node");
    private ArrayList<Double> xaxis = new ArrayList<>();

    public B_Tree_GUI() {
        super("B-Tree Algorithm");

        button.addActionListener((ActionEvent e) -> {
            if (textField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Enter Some Data...");
            } else {
                String text = textField.getText();
                bTree.addKey(new Key(Integer.valueOf(text)));

                scene_.removeAll();
                xaxis.clear();

                double startX = 400;
                double startY = 100;

                Key[] keys = bTree.getParentKey();
                bTree.printKeys(keys, (int) startX, (int) startY);

                printKeys(keys, startX, startY, null);
                window.redraw();
                scene_.refresh();
                textField.setText(null);

            }
        });

        delButton.addActionListener((ActionEvent e) -> {
            if (textField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Enter Some Data...");
            } else {
                String text = textField.getText();
                bTree.deleteKey(Integer.valueOf(text));

                scene_.removeAll();
                xaxis.clear();

                double startX = 400;
                double startY = 100;

                Key[] keys = bTree.getParentKey();
                bTree.printKeys(keys, (int) startX, (int) startY);

                printKeys(keys, startX, startY, null);
                window.redraw();
                scene_.refresh();
                textField.setText(null);

            }
        });

        show_Graph();
    }

    JPanel topLevel = new JPanel();

    public void show_Graph() {
        setLocationByPlatform(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the GUI
        topLevel.setLayout(new BorderLayout());
        getContentPane().add(topLevel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(label);
        textField.setSize(50, 20);
        buttonPanel.add(textField);
        buttonPanel.add(button);
        buttonPanel.add(delButton);
        topLevel.add(buttonPanel, BorderLayout.NORTH);

        // Create the graphic canvas
        topLevel.add(window.getCanvas(), BorderLayout.CENTER);

        // Create scene with default viewport and world extent settings
        scene_ = new GScene(window, "Scene");

        double w0[] = {0.0, 1280.0, 0.0};
        double w1[] = {1280.0, 1280.0, 0.0};
        double w2[] = {0.0, 0.0, 0.0};
        scene_.setWorldExtent(w0, w1, w2);

        GStyle style = new GStyle();
        style.setForegroundColor(new Color(0, 0, 0));
        style.setBackgroundColor(new Color(255, 255, 255));
        style.setFont(new Font("Tahoma", Font.BOLD, 14));
        scene_.setStyle(style);

        double startX = 400;
        double startY = 100;

        Key[] keys = bTree.getParentKey();

        printKeys(keys, startX, startY, null);

        pack();
        setSize(new Dimension(1280, 800));
        window.startInteraction(this);

    }

    public void printKeys(Key[] keys, double startX, double startY, GObject gObject) {

        //Iterating through all keys of a node.
        for (int i = 0; i < keys.length; i++) {

            //Firstly printing the left most keys of all nodes.
            TestObject object = null;
            if (keys[i] != null) {

                object = new TestObject(String.valueOf(keys[i].value), this);
                if (keys[i].leftNode != null) {
                    printKeys(keys[i].leftNode.keys, startX - 30, startY + 100, object);
                }
            }

            //Print the central value.
            if (keys[i] != null) {

                if (xaxis.contains(startX)) {

                    if (i == 0) {
                        startX += 30;
                        while (xaxis.contains(startX)) {
                            startX += 30;
                        }
//                        GObject object11 = new TestObject(String.valueOf(keys[i].value), scene_, startX, startY);
                        object.setX_(startX);
                        object.setY_(startY);
                        if (gObject == null || i > 0) {

                            object.setParent(scene_);
                        } else {
                            object.setParent(gObject);
                        }

                        scene_.add(object);

                    } else {

                        if (keys[i].value < 10) {
//                            GObject object11 = new TestObject(String.valueOf(keys[i].value), scene_, (startX + 15), startY);
                            object.setX_(startX + 20);
                            object.setY_(startY);
                            if (gObject == null || i > 0) {
                                object.setParent(scene_);
                            } else {
                                object.setParent(gObject);
                            }
                            scene_.add(object);
                        } else if (keys[i].value >= 10) {
//                            GObject object = new TestObject(String.valueOf(keys[i].value), scene_, (startX + 25), startY);
                            object.setX_(startX + 20);
                            object.setY_(startY);
                            if (gObject == null || i > 0) {
                                object.setParent(scene_);
                            } else {
                                object.setParent(gObject);
                            }
                            scene_.add(object);
                        }
                    }
                } else {
//                    GObject object22 = new TestObject (String.valueOf(keys[i].value), scene_, startX, startY);
                    object.setX_(startX);
                    object.setY_(startY);
                    if (gObject == null || i > 0) {
                        object.setParent(scene_);
                    } else {
                        object.setParent(gObject);
                    }
                    scene_.add(object);
                }

                xaxis.add(startX);
            }

            //At last print the right keys of each node.
            if (keys[i] != null) {

                if (keys[i].rightNode != null) {
                    if (keys[i + 1] == null) {
                        printKeys(keys[i].rightNode.keys, startX + 30, startY + 100, object);
                    }
                }
            }

        }
        //Print end line after printing each keys of a node.
    }

    @Override
    public void event(GScene scene, int event, int x, int y) {
        if (event == GWindow.BUTTON1_UP
                || event == GWindow.BUTTON2_UP) {
            boolean isSelected = event == GWindow.BUTTON1_UP;

            GSegment selectedSegment = scene_.findSegment(x, y);
            if (selectedSegment == null) {
                return;
            }

            GStyle style = selectedSegment.getOwner().getStyle();
            if (style == null) {
                return;
            }

            if (isSelected) {
                style.setBackgroundColor(new Color((float) Math.random(),
                        (float) Math.random(),
                        (float) Math.random()));
            } else {
                style.unsetBackgroundColor();
            }

            scene_.refresh();
        }
    }

    public static void main(String[] args) {
        B_Tree_GUI b_Tree_GUI = new B_Tree_GUI();
        b_Tree_GUI.setVisible(true);
    }
}
