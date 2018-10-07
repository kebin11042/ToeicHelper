package com.example.bang.toeichelper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;

/**
 * Created by BANG on 2015-02-08.
 */
public class IMAGE_PROCESSING {

    private Context context;

    public IMAGE_PROCESSING(Context context) {
        this.context = context;
    }

    public Drawable getDrawableFromBitmap(Bitmap bitmap){
        Drawable drawable = new BitmapDrawable(bitmap);
        return drawable;
    }

    public Bitmap getBitmapFromDrawable(BitmapDrawable drawable){
        return drawable.getBitmap();
    }

    public Bitmap getBitmapFromResource(int resource){
        return BitmapFactory.decodeResource(context.getResources(), resource);
    }

    public Bitmap takeScreenShot(Activity activity)
    {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();

        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height  - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }

    //블러 효과 내는 메소드
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public Bitmap blur(Bitmap sentBitmap, float radius) {
        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        final RenderScript rs = RenderScript.create(this.context);
        final Allocation input =
                Allocation.createFromBitmap(rs,
                        sentBitmap,
                        Allocation.MipmapControl.MIPMAP_NONE,
                        Allocation.USAGE_SCRIPT);
        final Allocation output = Allocation.createTyped(rs, input.getType());
        final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        script.setRadius(radius);   //0.0 ~ 25.0
        script.setInput(input);
        script.forEach(output);
        output.copyTo(bitmap);

        return bitmap;
    }
}
