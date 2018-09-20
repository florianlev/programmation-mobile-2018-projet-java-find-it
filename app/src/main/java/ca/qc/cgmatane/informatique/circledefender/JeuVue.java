package ca.qc.cgmatane.informatique.circledefender;

import android.content.Context;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

public class JeuVue extends SurfaceView implements SurfaceHolder.Callback {
    public JeuVue(Context context) {
        super(context);

        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
