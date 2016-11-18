package graphmodel;

import java.awt.*;

/**
 * Created by jules on 16/11/2016.
 */
public class ColoredVertex {
    public String name;
    public Color c = Color.black;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ColoredVertex that = (ColoredVertex) o;

        return name.equals(that.name);

    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public ColoredVertex(String name, Color c) {
        this.name = name;
        this.c = c;
    }

    @Override
    public String toString() {
        return "ColoredVertex{" +
                "name='" + name + '\'' +
                '}';
    }

    public ColoredVertex(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getC() {
        return c;
    }

    public void setC(Color c) {
        this.c = c;
    }
}
