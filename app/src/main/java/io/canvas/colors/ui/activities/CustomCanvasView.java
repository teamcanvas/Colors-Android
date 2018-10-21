package io.canvas.colors.ui.activities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import io.canvas.colors.R;

public class CustomCanvasView extends View {
    Paint paint;
    Path path;

    public CustomCanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setShader(new LinearGradient(0, 0, 800, getHeight(), getResources().getColor(R.color.startColor),
                getResources().getColor(R.color.endColor), Shader.TileMode.CLAMP));
        paint.setStrokeWidth(10);
        paint.setStyle(Paint.Style.FILL);

        path = new Path();
        path.moveTo(0, 1000);
        path.lineTo(0, 0);
        path.lineTo(1700, 800);
        path.lineTo(1700, 1000);

        path.close();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath(path, paint);
    }
}
