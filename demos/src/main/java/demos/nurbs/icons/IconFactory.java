package demos.nurbs.icons;

import com.sun.opengl.util.StreamUtil;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;

public class IconFactory {
    private IconFactory() {
    }

    public static ImageIcon getIcon(String resourceName) {
        try {
            InputStream input = IconFactory.class.getClassLoader().getResourceAsStream(resourceName);
            byte[] data = StreamUtil.readAll2Array(input);
            return new ImageIcon(data, resourceName);
        } catch (IOException e) {
            return new ImageIcon();
        }
    }
}
