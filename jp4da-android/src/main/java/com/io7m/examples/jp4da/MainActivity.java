package com.io7m.examples.jp4da;

import android.os.Bundle;
import com.jogamp.newt.ScreenMode;
import com.jogamp.newt.event.MouseAdapter;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.newt.event.ScreenModeListener;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.Animator;
import jogamp.newt.driver.android.NewtBaseActivity;

public class MainActivity extends NewtBaseActivity {
    @Override
    public void onCreate(
            final Bundle state) {
        super.onCreate(state);

        final GLCapabilities caps =
                new GLCapabilities(GLProfile.get(GLProfile.GLES2));
        final GLWindow gl_window = GLWindow.create(caps);
        gl_window.setFullscreen(true);

        this.setContentView(this.getWindow(), gl_window);

        gl_window.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(
                    final MouseEvent e) {
                if (e.getPressure(true) > 2f) { // show Keyboard
                    ((com.jogamp.newt.Window) e.getSource()).setKeyboardVisible(true);
                }
            }
        });

        final Example example = new Example();

        // demo.enableAndroidTrace(true);
        gl_window.addGLEventListener(example);


        final Animator animator = new Animator(gl_window);
        this.setAnimator(animator);

        gl_window.setVisible(true);
        animator.setUpdateFPSFrames(60, System.err);
        animator.resetFPSCounter();
        gl_window.resetFPSCounter();
    }
}
