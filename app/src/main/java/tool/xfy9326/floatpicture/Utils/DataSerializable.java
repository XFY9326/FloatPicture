package tool.xfy9326.floatpicture.Utils;

import android.widget.ImageView;

import java.io.Serializable;

public class DataSerializable implements Serializable {
    private ImageView imageView;
    private String PictureId;
    private String PictureName;
    private int Position_X;
    private int Position_Y;
    private float zoom;

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public String getPictureId() {
        return PictureId;
    }

    public void setPictureId(String pictureId) {
        PictureId = pictureId;
    }

    public String getPictureName() {
        return PictureName;
    }

    public void setPictureName(String pictureName) {
        PictureName = pictureName;
    }

    public int getPosition_X() {
        return Position_X;
    }

    public void setPosition_X(int position_X) {
        Position_X = position_X;
    }

    public int getPosition_Y() {
        return Position_Y;
    }

    public void setPosition_Y(int position_Y) {
        Position_Y = position_Y;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }
}
