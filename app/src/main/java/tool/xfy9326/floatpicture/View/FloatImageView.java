package tool.xfy9326.floatpicture.View;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.MotionEvent;
import android.view.WindowManager;

import tool.xfy9326.floatpicture.Methods.WindowsMethods;
import tool.xfy9326.floatpicture.Utils.Config;

public class FloatImageView extends AppCompatImageView {
    private String PictureId = "";
    private WindowManager windowManager;
    private boolean moveable = false;

    private float mTouchStartX = 0;
    private float mTouchStartY = 0;
    private float x = 0;
    private float y = 0;
    private float mNowPositionX = Config.DATA_DEFAULT_PICTURE_POSITION_X;
    private float mNowPositionY = Config.DATA_DEFAULT_PICTURE_POSITION_Y;

    public FloatImageView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }

    @SuppressWarnings("unused")
    public String getPictureId() {
        return PictureId;
    }

    public void setPictureId(String id) {
        PictureId = id;
    }

    public void setMoveable(boolean moveable) {
        this.moveable = moveable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (moveable) {
            x = event.getRawX();
            y = event.getRawY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mTouchStartX = event.getX();
                    mTouchStartY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    getNowPosition();
                    updatePosition();
                    break;
                case MotionEvent.ACTION_UP:
                    getNowPosition();
                    updatePosition();
                    mTouchStartX = mTouchStartY = 0;
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    public float getMovedPositionX() {
        return mNowPositionX;
    }

    public float getMovedPositionY() {
        return mNowPositionY;
    }

    private void getNowPosition() {
        mNowPositionX = x - mTouchStartX;
        mNowPositionY = y - mTouchStartY;
    }

    private void updatePosition() {
        windowManager.updateViewLayout(this, WindowsMethods.getDefaultLayout((int) mNowPositionX, (int) mNowPositionY, moveable));
    }

}
