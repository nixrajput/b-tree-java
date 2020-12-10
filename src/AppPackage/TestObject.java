package AppPackage;

import java.awt.Color;
import java.awt.Font;
import no.geosoft.cc.geometry.Geometry;
import no.geosoft.cc.graphics.GObject;
import no.geosoft.cc.graphics.GPosition;
import no.geosoft.cc.graphics.GSegment;
import no.geosoft.cc.graphics.GStyle;
import no.geosoft.cc.graphics.GText;

/**
 *
 * @author nixrajput
 */
public class TestObject extends GObject {

    private TestObject parent_;
    private double x_;
    private double y_;
    private final GSegment rectangle_;
    private final GSegment line_;
    GStyle style = new GStyle();
    private final B_Tree_GUI outer;

    TestObject(String name, final B_Tree_GUI outer) {
        this.outer = outer;
        style.setForegroundColor(new Color(55, 55, 55));
        style.setBackgroundColor(new Color(60, 185, 145));
        style.setFont(new Font("Tahoma", Font.BOLD, 14));
        outer.scene_.setStyle(style);
        line_ = new GSegment();
        addSegment(line_);
        rectangle_ = new GSegment();
        addSegment(rectangle_);
        rectangle_.setText(new GText(name, GPosition.MIDDLE));
        setStyle(style);
    }

    public void setParent(GObject parent) {
        parent_ = parent instanceof TestObject ? (TestObject) parent : null;
        if (parent_ != null) {
            parent_.add(this);
        }
    }

    double getX() {
        return x_;
    }

    double getY() {
        return y_;
    }

    public void setX_(double x) {
        this.x_ = x;
    }

    public void setY_(double y) {
        this.y_ = y;
    }

    @Override
    public void draw() {
        if (parent_ != null) {
            line_.setGeometry(parent_.getX(), parent_.getY(), x_, y_);
        }
        rectangle_.setGeometryXy(Geometry.createRectangle(x_, y_, 10.0, 10.0));
    }

}
