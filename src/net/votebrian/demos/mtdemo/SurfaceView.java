package net.votebrian.demos.mtdemo;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class SurfaceView extends GLSurfaceView {

    private GLESRenderer renderer;

    public SurfaceView(Context context, AttributeSet atr) {
        super(context, atr);
        renderer = new GLESRenderer(context);

        setRenderer(renderer);
    }
}