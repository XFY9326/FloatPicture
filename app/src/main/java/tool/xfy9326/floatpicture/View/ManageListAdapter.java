package tool.xfy9326.floatpicture.View;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import tool.xfy9326.floatpicture.Activities.MainActivity;
import tool.xfy9326.floatpicture.Activities.PictureSettingsActivity;
import tool.xfy9326.floatpicture.Methods.ImageMethods;
import tool.xfy9326.floatpicture.Methods.ManageMethods;
import tool.xfy9326.floatpicture.R;
import tool.xfy9326.floatpicture.Utils.Config;
import tool.xfy9326.floatpicture.Utils.PictureData;

public class ManageListAdapter extends AdvancedRecyclerView.Adapter<ManageListViewHolder> {
    private final Activity mActivity;
    private final PictureData pictureData;
    private LinkedHashMap<String, String> pictureInfo;
    private ArrayList<String> PictureId_Array;
    private ArrayList<String> PictureName_Array;

    public ManageListAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        pictureData = new PictureData();
        updateData();
    }

    public void updateData() {
        pictureInfo = pictureData.getListArray();
        PictureId_Array = new ArrayList<>();
        PictureName_Array = new ArrayList<>();
        for (Map.Entry<?, ?> entry : pictureInfo.entrySet()) {
            PictureId_Array.add(entry.getKey().toString());
            PictureName_Array.add(entry.getValue().toString());
        }
    }

    @Override
    public int getItemCount() {
        return pictureInfo.size();
    }

    @Override
    public void onBindViewHolder(final ManageListViewHolder holder, int position) {
        final String mPictureId = PictureId_Array.get(holder.getAdapterPosition());
        final String mPictureName = PictureName_Array.get(holder.getAdapterPosition());
        holder.textView_Picture_Name.setText(mPictureName);
        holder.textView_Picture_Id.setText(mPictureId);
        holder.imageView_Picture_Preview.setImageBitmap(ImageMethods.getPreviewBitmap(mActivity, mPictureId));
        final PictureData pictureData = new PictureData();
        pictureData.setDataControl(mPictureId);

        if (!ImageMethods.isPictureFileExist(mPictureId)) {
            holder.textView_Picture_Error.setVisibility(View.VISIBLE);
        } else {
            holder.textView_Picture_Error.setVisibility(View.INVISIBLE);
        }

        SwitchCompat switch_Picture_Show = holder.switch_Picture_Show;
        switch_Picture_Show.setChecked(pictureData.getBoolean(Config.DATA_PICTURE_SHOW_ENABLED, Config.DATA_DEFAULT_PICTURE_SHOW_ENABLED));
        switch_Picture_Show.setOnCheckedChangeListener((compoundButton, b) -> ManageMethods.setWindowVisible(mActivity, pictureData, mPictureId, b));

        holder.button_Picture_Edit.setOnClickListener(view -> {
            PictureData pictureData1 = new PictureData();
            pictureData1.setDataControl(mPictureId);
            if (pictureData1.getBoolean(Config.DATA_PICTURE_SHOW_ENABLED, Config.DATA_DEFAULT_PICTURE_SHOW_ENABLED)) {
                Intent intent = new Intent(mActivity, PictureSettingsActivity.class);
                intent.putExtra(Config.INTENT_PICTURE_EDIT_MODE, true);
                intent.putExtra(Config.INTENT_PICTURE_EDIT_ID, mPictureId);
                intent.putExtra(Config.INTENT_PICTURE_EDIT_POSITION, holder.getAdapterPosition());
                mActivity.startActivityForResult(intent, Config.REQUEST_CODE_ACTIVITY_PICTURE_SETTINGS_CHANGE);
            } else {
                MainActivity.SnackShow(mActivity, R.string.action_warn_edit_hided_window);
            }
        });

        holder.button_Picture_Delete.setOnClickListener(v -> {
            ManageMethods.DeleteWin(mActivity, mPictureId);
            updateData();
            holder.switch_Picture_Show.setOnCheckedChangeListener(null);
            holder.button_Picture_Edit.setOnClickListener(null);
            holder.button_Picture_Delete.setOnClickListener(null);
            int position1 = holder.getAdapterPosition();
            notifyItemRemoved(position1);
            notifyItemRangeChanged(position1, getItemCount() - position1);
            MainActivity.SnackShow(mActivity, R.string.action_delete_window);
            ManageMethods.updateNotificationCount(mActivity);
        });
    }

    @Override
    @NonNull
    public ManageListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View mView = inflater.inflate(R.layout.adapter_manage_list, parent, false);
        return new ManageListViewHolder(mView);
    }
}
