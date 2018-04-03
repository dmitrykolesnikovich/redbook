package demos.applets;

import com.jogamp.openal.util.ALut;
import com.jogamp.opengl.GLAnimatorControl;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import demos.gears.Gears;
import demos.joal.SingleStaticSource;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;

/**
 * Shows how to deploy an applet using both JOGL and JOAL. This demo
 * must be referenced from a web page via an &lt;applet&gt; tag.
 */

public class GearsJOALApplet extends Applet {
    private GLAnimatorControl animator;

    public void init() {
        setLayout(new GridLayout(1, 2));
        GLCanvas canvas = new GLCanvas();
        canvas.addGLEventListener(new Gears());
        canvas.setSize(getSize());
        add(canvas);
        JPanel joalDemoParent = new JPanel();
        SingleStaticSource joalDemo = new SingleStaticSource(true, joalDemoParent, false);
        add(joalDemoParent);
        animator = new FPSAnimator(canvas, 60);
    }

    public void start() {
        animator.start();
    }

    public void stop() {
        // FIXME: do I need to do anything else here?
        animator.stop();
        // Note that the SingleStaticSource demo does an alutInit()
        // internally (on the Event Dispatch Thread), so we should do an
        // alutExit() ourselves
        try {
            EventQueue.invokeAndWait(new Runnable() {
                public void run() {
                    ALut.alutExit();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
