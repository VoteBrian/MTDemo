package net.votebrian.demos.mtdemo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import java.io.InputStream;
import java.io.IOException;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import android.util.Log;

public class Model {
    // -------
    // GLOBALS
    // -------
    private Context mCtx;

    private boolean mEnableModel = false;
    private boolean mEnableTexture = false;
    private boolean mEnableColor = false;
    private boolean mEnableOutline = false;
    private int mTexSet = 0;

    // Model
    private float[] mVertices = {
         0.0f,  0.5f, 0f,
        -0.5f, -0.5f, 0f,
         0.5f, -0.5f, 0f
    };
    private int mNumVertices = mVertices.length/3;
    private FloatBuffer mVertexBuffer;

    // Color
    private float[] mColor = {
        1.0f, 1.0f, 1.0f, 1.0f
    };
    private FloatBuffer mColorBuffer;

    // Textures
    private int[] mTexture = new int[2];

    private Bitmap mBitmap1;
    private Bitmap mBitmap2;

    private float[] mTexCoord1 = {
        0.5f, 0.0f,
        0.0f, 1.0f,
        1.0f, 1.0f
    };
    private FloatBuffer mTexCoordBuffer1;

    private float[] mTexCoord2 = {
        0.5f, 0.0f,
        0.0f, 1.0f,
        1.0f, 1.0f
    };
    private FloatBuffer mTexCoordBuffer2;

    // Outline
    private float[] mOutline = {
        0.0f, 0.0f, 0f,
        1.0f, 1.0f, 0f
    };
    private int mNumOutlineVertices = mOutline.length/3;
    private FloatBuffer mOutlineBuffer;

    private float[] mOutlineColor = {
        1.0f, 0.0f, 0.0f, 1.0f
    };
    private FloatBuffer mOutlineColorBuffer;

    // Translation
    private float mCentX = 0f;
    private float mCentY = 0f;
    private float mCentZ = 0f;



    // -----------
    // CONSTRUCTOR
    // -----------
    public Model(Context context, GL10 gl) {
        mCtx = context;

        enableModel();

        // default model
        initBuffers();

        // default textures
        initTextures(gl);
    }

    public void draw(GL10 gl) {
        gl.glPushMatrix();


        // Translations
        gl.glTranslatef(mCentX, mCentY, mCentZ);

        /* ------------
            DRAW MODEL
           ------------ */
        if(mEnableModel) {
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

            // Select between a solid color, one texture, or two textures
            switch(mTexSet) {
                case 0:
                    /* -----------------------------------
                        Draw model with solid color.
                        Disabled when setTexture() is called.
                        Run setModelColor( new float[]{r,g,b,a} ) to set color.
                       ----------------------------------- */

                    gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
                    gl.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);
                    break;
                case 1:
                    /* -----------------------------------
                        Draw model with a single texture
                        Enabled when calling setTexture( int mTexture )
                            First run:
                            gl.glGenTextures(1, mTexture, 0);
                            gl.glBindTexture(GL10.GL_TEXTURE_2D, mTexture);
                            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mTexBitmap, 0);
                        Run setTextureBuffer()
                       ----------------------------------- */

                    // Texture 1
                    gl.glClientActiveTexture(GL10.GL_TEXTURE0);
                    gl.glActiveTexture(GL10.GL_TEXTURE0);
                    gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
                    gl.glBindTexture(GL10.GL_TEXTURE_2D, mTexture[0]);
                    gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);
                    gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexCoordBuffer1);
                    gl.glEnable(GL10.GL_TEXTURE_2D);
                    break;
                case 2:
                    /* -----------------------------------
                        Draw model with a two textures
                        Enabled when calling setTexture( int, int )
                            First run:
                            gl.glGenTextures(1, mTexture, 0);
                            gl.glBindTexture(GL10.GL_TEXTURE_2D, mTexture);
                            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mTexBitmap, 0);
                        Run setTextureBuffer()
                       ----------------------------------- */

                    // Texture 1
                    gl.glClientActiveTexture(GL10.GL_TEXTURE0);
                    gl.glActiveTexture(GL10.GL_TEXTURE0);
                    gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
                    gl.glBindTexture(GL10.GL_TEXTURE_2D, mTexture[0]);
                    gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_MODULATE);
                    gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexCoordBuffer1);
                    gl.glEnable(GL10.GL_TEXTURE_2D);

                    // Texture 2
                    gl.glClientActiveTexture(GL10.GL_TEXTURE1);
                    gl.glActiveTexture(GL10.GL_TEXTURE1);
                    gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
                    gl.glBindTexture(GL10.GL_TEXTURE_2D, mTexture[1]);
                    gl.glTexEnvf(GL10.GL_TEXTURE_ENV, GL10.GL_TEXTURE_ENV_MODE, GL10.GL_DECAL);
                    gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexCoordBuffer2);
                    gl.glEnable(GL10.GL_TEXTURE_2D);

                    // Revert to avoid conflicts with other draws
                    gl.glActiveTexture(GL10.GL_TEXTURE0);
                    break;
            }

            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
            gl.glDrawArrays(GL10.GL_TRIANGLES, 0, mNumVertices);


            gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
            gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            gl.glDisable(GL10.GL_TEXTURE_2D);
        }


        /* ---------------
            DRAW OUTLINES

            Requires the following subroutines be called:
                enableOutlines()
                setOutlineIndicies()
           --------------- */
        if(mEnableOutline) {
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

            gl.glColorPointer(4, GL10.GL_FLOAT, 0, mOutlineColorBuffer);
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mOutlineBuffer);
            gl.glDrawArrays(GL10.GL_LINES, 0, mNumOutlineVertices);

            gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        }

        gl.glPopMatrix();
    }

    /* -------
        MODEL
       ------- */
    public void disableModel() {
        mEnableModel = false;
    }

    public void enableModel() {
        mEnableModel = true;
    }

    public void setModelColor(float[] array) {
        mColor = new float[array.length];
        mColor = array;

        mColorBuffer = buildColorBuffer(mColor, mNumVertices);
    }

    public void setPosition(float x, float y, float z) {
        mCentX = x;
        mCentY = y;
        mCentZ = z;
    }

    public void setVertices(float[] vertices) {
        mVertices = new float[vertices.length];
        mVertices = vertices;
        mNumVertices = mVertices.length/3;

        mVertexBuffer = makeFloatBuffer(mVertices);
    }

    /* ----------
        TEXTURES
       ---------- */
    public void setTexture(boolean tex1, boolean tex2) {
        if(tex1 && tex2) {
            mTexSet = 2;
        } else if (tex1) {
            mTexSet = 1;
        }
    }

    public void setTexture(int texture) {
        mTexture[0] = texture;

        mTexSet = 1;
    }

    public void setTexture(int texture1, int texture2) {
        mTexture[0] = texture1;
        mTexture[1] = texture2;

        mTexSet = 2;
    }

    public void initTextures(GL10 gl) {
        // Generate textures
        gl.glGenTextures(2, mTexture, 0);

        // Texture 1
        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTexture[0]);
        mBitmap1 = loadBitmap(R.drawable.default1);
        if(mBitmap1 != null) {
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mBitmap1, 0);
        }

        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);


        // Texture 2
        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTexture[1]);
        mBitmap2 = loadBitmap(R.drawable.default2);
        if(mBitmap1 != null) {
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, mBitmap2, 0);
        }

        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);


        // Buffers
        mTexCoordBuffer1 = makeFloatBuffer(mTexCoord1);
        mTexCoordBuffer2 = makeFloatBuffer(mTexCoord2);
    }

    public Bitmap loadBitmap(int id) {
        InputStream is = mCtx.getResources().openRawResource(id);

        try {
            return BitmapFactory.decodeStream(is);
        } finally {
            try {
                is.close();
                is = null;
            } catch (IOException e) {
                is = null;
            }
        }
    }

    /* ----------
        OUTLINES
       ---------- */
    public void disableOutline() {
        mEnableOutline = false;
    }

    public void enableOutline() {
        mEnableOutline = true;
    }

    public void setOutlineIndices(int[] array) {
        int numIndices = array.length;

        mOutline = new float[numIndices * 3];

        for(int c = 0; c < numIndices; c++) {
            mOutline[c*3] = mVertices[array[c]*3];
            mOutline[c*3 + 1] = mVertices[array[c]*3 + 1];
            mOutline[c*3 + 2] = mVertices[array[c]*3 + 2];
        }

        mNumOutlineVertices = mOutline.length/3;

        mOutlineBuffer = makeFloatBuffer(mOutline);
        mOutlineColorBuffer = buildColorBuffer(mOutlineColor, mNumOutlineVertices);
    }

    public void setOutlineColor(float[] array) {
        mOutlineColor = new float[array.length];
        mOutlineColor = array;

        mOutlineColorBuffer = buildColorBuffer(mOutlineColor, mNumOutlineVertices);
    }


    /* ---------
        GENERAL
       --------- */
    private FloatBuffer makeFloatBuffer(float[] array) {
        ByteBuffer bb = ByteBuffer.allocateDirect(array.length * 4);
        bb.order(ByteOrder.nativeOrder());

        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(array);
        fb.position(0);
        return fb;
    }

    public FloatBuffer buildColorBuffer(float[] array, int numVertices) {
        float[] tempArray = new float[numVertices*4];

        for(int c = 0; c < numVertices; c++) {
            tempArray[c*4 + 0] = array[0];
            tempArray[c*4 + 1] = array[1];
            tempArray[c*4 + 2] = array[2];
            tempArray[c*4 + 3] = array[3];
        }

        return makeFloatBuffer(tempArray);
    }

    private void initBuffers() {
        mVertexBuffer = makeFloatBuffer(mVertices);
        mColorBuffer = makeFloatBuffer(mColor);
        mOutlineColorBuffer = makeFloatBuffer(mOutlineColor);
    }
}