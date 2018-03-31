package demos.nurbs.surfaceapp;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.*;
import com.jogamp.opengl.util.glut.gl2.GLUTgl2;

/**
 * Listener reacting to events occuring on OpenGL canvas
 * Listener reagující na události na OpenGL plátně
 *
 * @author Tomáš Hráský
 */
public class GLListener implements GLEventListener {
    /**
     * Object realizing OpenGL functions
     * objekt realizující základní OpenGL funkce
     */
    private GL2 gl;

    /**
     * GLU object
     * Objekt realizující funkce nadstavbové knihovny GLU
     */
    private GLU glu;

    /**
     * NURBS object
     * OpenGL Objekt NURBS křivek a ploch
     */
    private GLUnurbs nurbs;

    /**
     * Parent window
     * Rodičovské okno
     */
    private SurfaceApp app;

    /**
     * Coords of canvas corners
     * Viewport (souřadnice rohů plátna)
     */
    private int[] viewport;

    /**
     * Modelview matrix
     * Matice modelview
     */
    private double[] mvmatrix;

    /**
     * Projection matrix
     * Projekční matice
     */
    private double[] projmatrix;


    /**
     * Light source position vector
     * Vektor zdroje světla
     */
    private float[] lightPosition = {0.0f, 1.0f, 0f, 1.0f};

    /**
     * Seconf light source position vector
     * Vektor druhého zdroje světla
     */
    private float[] lightPosition3 = {0.0f, 0.0f, 1.0f, 0.0f};

    /**
     * Ambient light vector
     * Ambientní složka světla
     */
    private float[] lightAmbient = {1f, 1f, 1f, 1f};

    /**
     * Difusion material vector
     * Difuzní složka barvy materiálu
     */
    private float[] materialDiffuse = {0.8f, 0.4f, 0.4f, 1.0f};

    /**
     * Difusion ligh vector
     * Difúzní složka světla
     */
    private float[] lightDiffuse = {1f, 1f, 1f, 1.0f};

    /**
     * GLUT object
     * Objekt pro podporu funkcionality GL utility toolkit
     */
    private GLUTgl2 glut;


    /**
     * Creates new GLListener with link to parent window
     * Vytvoří nový GLListener s odkazem na rodičovské okno
     *
     * @param app parent window
     */
    public GLListener(SurfaceApp app) {
        this.app = app;

        viewport = new int[4];
        mvmatrix = new double[16];
        projmatrix = new double[16];

    }

    /* (non-Javadoc)
     * @see com.jogamp.opengl.GLEventListener#init(com.jogamp.opengl.GLAutoDrawable)
     */
    public void init(GLAutoDrawable drawable) {
        this.gl = drawable.getGL().getGL2();
        this.glu = new GLU();
        this.glut = new GLUTgl2();


        this.nurbs = glu.gluNewNurbsRenderer();
        //		gl.glClearColor(0, 0, 0, 0);
        gl.glClearColor(1, 1, 1, 0);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthFunc(GL2.GL_LESS);
        gl.glClearDepth(1000.0f);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
    }

    public void dispose(GLAutoDrawable drawable) {
        this.gl = null;
        this.glu = null;
        this.glut = null;
        this.nurbs = null;
    }

    /* (non-Javadoc)
     * @see com.jogamp.opengl.GLEventListener#display(com.jogamp.opengl.GLAutoDrawable)
     */
    public void display(GLAutoDrawable drawable) {

        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();

        glu.gluLookAt(0, 0, 400, 0, 0, 0, 0, 1, 0);


        //		gl.glPushMatrix();
        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glPolygonMode(GL2.GL_FRONT, GL2.GL_FILL);
        gl.glPolygonMode(GL2.GL_BACK, GL2.GL_FILL);
        gl.glDisable(GL2.GL_CULL_FACE);

        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, materialDiffuse, 0);
        gl.glMaterialfv(GL2.GL_BACK, GL2.GL_DIFFUSE, materialDiffuse, 0);

        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPosition, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, materialDiffuse, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, lightAmbient, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, lightAmbient, 0);

        gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_POSITION, lightPosition3, 0);
        gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_DIFFUSE, lightDiffuse, 0);

        if (app.isLightingEnabled()) gl.glEnable(GL2.GL_LIGHTING);
        else gl.glDisable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GL2.GL_LIGHT2);
        //		gl.glPopMatrix();


        gl.glTranslatef(-200, -200, -100);

        gl.glRotatef(app.getXrotation(), 1, 0, 0);
        gl.glRotatef(app.getYrotation(), 0, 1, 0);
        gl.glRotatef(app.getZrotation(), 0, 0, 1);


        //		glut.glutSolidTeapot(20);

        float[] knotsU = Surface.getInstance().getKnotsU();
        float[] knotsV = Surface.getInstance().getKnotsV();
        float[] ctrlpoints = Surface.getInstance().getCtrlPoints();


        gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0);
        gl.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, mvmatrix, 0);
        gl.glGetDoublev(GL2.GL_PROJECTION_MATRIX, projmatrix, 0);

        gl.glEnable(GL2.GL_LINE_SMOOTH);
        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        gl.glHint(GL2.GL_LINE_SMOOTH_HINT, GL2.GL_DONT_CARE);

        gl.glLineWidth(3);

        //TODO getpointsinV or inU
        if (Surface.getInstance().isSurfaceFinished()) {
            glu.gluBeginSurface(nurbs);
            glu.gluNurbsSurface(nurbs,
                    knotsU.length,
                    knotsU,
                    knotsV.length,
                    knotsV,
                    Surface.getInstance().getPointsInV() * 4,
                    4,
                    ctrlpoints,
                    Surface.getInstance().getOrderU(),
                    Surface.getInstance().getOrderV(),
                    GL2.GL_MAP2_VERTEX_4);
            glu.gluEndSurface(nurbs);
        }

        gl.glDisable(GL2.GL_LIGHTING);

        //		gl.glColor3f(1,1,1);
        gl.glColor3f(0, 0, 0);
        gl.glPointSize(5);
        gl.glBegin(GL2.GL_POINTS);
        for (int i = 0; i < ctrlpoints.length / 4; i++) {
            gl.glVertex3d(ctrlpoints[i * 4] / ctrlpoints[i * 4 + 3], ctrlpoints[i * 4 + 1] / ctrlpoints[i * 4 + 3],
                    ctrlpoints[i * 4 + 2] / ctrlpoints[i * 4 + 3]);
        }
        gl.glEnd();

        //		double[] coords = new double[3];
        for (int i = 0; i < ctrlpoints.length / 4; i++) {
            gl.glRasterPos3d(ctrlpoints[i * 4] / ctrlpoints[i * 4 + 3], ctrlpoints[i * 4 + 1] / ctrlpoints[i * 4 + 3],
                    ctrlpoints[i * 4 + 2] / ctrlpoints[i * 4 + 3]);
            //gl.glRasterPos2f((int)coords[0], (int)(viewport[3]-coords[1]-1-5));
            //			gl.glRasterPos2d(20,20);
            glut.glutBitmapString(GLUTgl2.BITMAP_HELVETICA_18, String.valueOf(i + 1));

        }


        //TODO zobrazovat síť - musí to být pomocí dvou vnořených forcyklů
        //TODO draw mesh - it needs two nested for statements
        gl.glLineWidth(1);
        //		gl.glBegin(GL2.GL_LINE_STRIP);
        int baseIndex = 0;


        //"příčná žebra"
        //"cross ribs"
        for (int i = 0; i < Surface.getInstance().getPointsInU(); i++) {
            gl.glBegin(GL2.GL_LINE_STRIP);
            for (int j = 0; j < Surface.getInstance().getPointsInV(); j++) {
                baseIndex = i * Surface.getInstance().getPointsInV() * 4 + j * 4;
                gl.glVertex3f(ctrlpoints[baseIndex + 0] / ctrlpoints[baseIndex + 3],
                        ctrlpoints[baseIndex + 1] / ctrlpoints[baseIndex + 3],
                        ctrlpoints[baseIndex + 2] / ctrlpoints[baseIndex + 3]);
            }
            gl.glEnd();
        }
        //"podélná žebra"
        //"alongway ribs"
        for (int j = 0; j < Surface.getInstance().getPointsInV(); j++) {
            gl.glBegin(GL2.GL_LINE_STRIP);
            for (int i = 0; i < Surface.getInstance().getPointsInU(); i++) {
                baseIndex = i * Surface.getInstance().getPointsInV() * 4 + j * 4;
                gl.glVertex3f(ctrlpoints[baseIndex + 0] / ctrlpoints[baseIndex + 3],
                        ctrlpoints[baseIndex + 1] / ctrlpoints[baseIndex + 3],
                        ctrlpoints[baseIndex + 2] / ctrlpoints[baseIndex + 3]);
            }
            gl.glEnd();
        }

        gl.glColor3f(0, 0, 1);
        if (Surface.getInstance().getBodIndex() >= 0) {
            gl.glPointSize(8);
            gl.glBegin(GL2.GL_POINTS);
            int i = Surface.getInstance().getBodIndex();
            gl.glVertex3d(ctrlpoints[i * 4] / ctrlpoints[i * 4 + 3], ctrlpoints[i * 4 + 1] / ctrlpoints[i * 4 + 3],
                    ctrlpoints[i * 4 + 2] / ctrlpoints[i * 4 + 3]);
            gl.glEnd();
        }


    }

    /* (non-Javadoc)
     * @see com.jogamp.opengl.GLEventListener#reshape(com.jogamp.opengl.GLAutoDrawable, int, int, int, int)
     */
    public void reshape(GLAutoDrawable drawable, int x, int y, int width,
                        int height) {
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(65.0, (double) width / height, 0.1, 1000.0);
        //		gl.glScalef(1, -1, 1);
        //		gl.glTranslatef(0, -drawable.getHeight(), 0);

    }

    /* (non-Javadoc)
     * @see com.jogamp.opengl.GLEventListener#displayChanged(com.jogamp.opengl.GLAutoDrawable, boolean, boolean)
     */
    public void displayChanged(GLAutoDrawable arg0, boolean arg1, boolean arg2) {
    }

    /**
     * Returns GLU object in use
     * Vrací používáný GLU objekt
     *
     * @return GLU objekt
     */
    public GLU getGlu() {
        return glu;
    }

    /**
     * Returns viewpord corners coords
     * Vrací souřadnice rohů viewportu
     *
     * @return array with viewport corner's coords
     */
    public int[] getViewport() {
        return viewport;
    }

    /**
     * Returns model view matrix
     * Vrací modelview matici
     *
     * @return modelview matrix
     */
    public double[] getMvmatrix() {
        return mvmatrix;
    }

    /**
     * Returns projection matrix
     * Vrací projekční matici
     *
     * @return projection matrix
     */
    public double[] getProjmatrix() {
        return projmatrix;
    }
}
