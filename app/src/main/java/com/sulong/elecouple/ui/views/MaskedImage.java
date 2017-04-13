package com.sulong.elecouple.ui.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public abstract class MaskedImage extends ImageView {
    private static final Xfermode MASK_XFERMODE;

    static {
        PorterDuff.Mode localMode = PorterDuff.Mode.DST_IN;
        MASK_XFERMODE = new PorterDuffXfermode(localMode);
    }

    public boolean have_stroke = false;
    private Bitmap mask;
    private Paint paint;

    public MaskedImage(Context paramContext) {
        super(paramContext);
    }

    public MaskedImage(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public MaskedImage(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    public void setStroke(boolean stroke) {
        have_stroke = stroke;
        invalidate();
    }

    public abstract Bitmap createMask();

    protected void onDraw(Canvas paramCanvas) {
        Drawable localDrawable = getDrawable();
        if (localDrawable == null)
            return;
        try {
            if (this.paint == null) {
                this.paint = new Paint();
                this.paint.setFilterBitmap(false);
                paint.setXfermode(MASK_XFERMODE);
            }
            float f1 = getWidth();
            float f2 = getHeight();
            int i = paramCanvas.saveLayer(0.0F, 0.0F, f1, f2, null, 31);
            int j = getWidth();
            int k = getHeight();
            localDrawable.setBounds(0, 0, j, k);
            localDrawable.draw(paramCanvas);
            if ((this.mask == null) || (this.mask.isRecycled())) {
                this.mask = createMask();
            }
            paramCanvas.drawBitmap(this.mask, 0.0F, 0.0F, this.paint);
            paramCanvas.restoreToCount(i);
            if (have_stroke) {
                Paint paint = new Paint();
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(1.5f);
                paint.setColor(Color.WHITE);
                RectF localRectF = new RectF(0.0F + 1f, 0.0F + 1f, f1 - 1f, f2 - 1f);
                paint.setAntiAlias(true);
                paint.setAlpha(178);
                paramCanvas.drawOval(localRectF, paint);
            }

            return;
        } catch (Exception localException) {
            StringBuilder localStringBuilder = new StringBuilder()
                    .append("Attempting to draw with recycled bitmap. View ID = ");
            System.out.println("localStringBuilder==" + localStringBuilder);
        }
    }
}
