package net.votebrian.demos.mtdemo;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.egl.EGLConfig;

import android.content.Context;

import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;

import android.util.Log;

public class GLESRenderer implements GLSurfaceView.Renderer {
    private Context mCtx;

    private int mWidth;
    private int mHeight;

    private Model model1;
    private Model model2;
    private Model model3;
    private Model model4;

    private float mNearH = 0f;
    private float mNearW = 0f;
    private float mNearZ = 5f;
    private float mFarZ = 15f;

    private int mViewW = 0;
    private int mViewH = 0;
    private float mViewAngle = 10f;;

    public GLESRenderer(Context context) {
        Log.v("RENDERER", "Constructor");
        mCtx = context;
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        setDisplayProperties(gl);


        /* ---------------------------------------
            Triangle with solid color and outline
           --------------------------------------- */
        model1 = new Model(mCtx, gl);

        model1.setModelColor(new float[] {1.0f, 1.0f, 1.0f, 0.3f});
        model1.setPosition(-0.5f, 1.0f, -10f);

        model1.setOutlineIndices(new int[]{0,1,1,2,2,0});
        model1.enableOutline();


        /* ---------------------------------------
            Triangle with one texture
           --------------------------------------- */
        model2 = new Model(mCtx, gl);

        model2.setPosition(0.5f, 1.0f, -10f);
        model2.setTexture(true, false);


        /* ---------------------------------------
            Triangle with two textures
           --------------------------------------- */
        model3 = new Model(mCtx, gl);

        model3.setPosition(-0.5f, -1.0f, -10f);
        model3.setTexture(true, true);


        /* ---------------------------------------
            Triangle with outlines only
           --------------------------------------- */
        model4 = new Model(mCtx, gl);

        model4.disableModel();
        model4.setPosition(0.5f, -1.0f, -10f);

        model4.setOutlineIndices(new int[]{0,1,1,2,2,0});
        model4.enableOutline();
    }

    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();

        // draw cards
        model1.draw(gl);
        model2.draw(gl);
        model3.draw(gl);
        model4.draw(gl);
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mViewW = width;
        mViewH = height;

        setDisplayProperties(gl);
        setProjection(gl);
    }

    private void setDisplayProperties(GL10 gl) {
        // Set clear color
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        // Set to remove CW triangles
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glFrontFace(GL10.GL_CCW);
        gl.glCullFace(GL10.GL_BACK);

        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);

        // set blend parameter
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
    }

    private void setProjection(GL10 gl) {
        float ratio = (float) mViewW / (float) mViewH;

        // determine the "half-width" and "half-height" of our view at the near cutoff Z value stuff
        // stuff stuff
        mNearH = (float) (mNearZ * (Math.tan(Math.toRadians(mViewAngle))));
        mNearW = mNearH * ratio;

        // Define orthographic projection
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glFrustumf(-mNearW, mNearW, -mNearH, mNearH, mNearZ, mFarZ);
        gl.glViewport(0, 0, mViewW, mViewH);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
    }
}