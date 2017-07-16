package tool.xfy9326.floatpicture.View;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

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
        for (Object o : pictureInfo.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
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
        holder.imageView_Picture_Preview.setImageBitmap(ImageMethods.getPreviewBitmap(mPictureId));
        PictureData pictureData = new PictureData();
        pictureData.setDataControl(mPictureId);

        Switch switch_Picture_Show = holder.switch_Picture_Show;
        switch_Picture_Show.setChecked(pictureData.getBoolean(Config.DATA_PICTURE_SHOW_ENABLED, Config.DATA_DEFAULT_PICTURE_SHOW_ENABLED));
        switch_Picture_Show.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    ManageMethods.showWindowById(mActivity, mPictureId);
                } else {
                    ManageMethods.hideWindowById(mActivity, mPictureId);
                }
                PictureData pictureData = new PictureData();
                pictureData.setDataControl(mPictureId);
                pictureData.put(Config.DATA_PICTURE_SHOW_ENABLED, b);
                pictureData.commit(null);
            }
        });

        holder.button_Picture_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PictureData pictureData = new PictureData();
                pictureData.setDataControl(mPictureId);
                if (pictureData.getBoolean(Config.DATA_PICTURE_SHOW_ENABLED, Config.DATA_DEFAULT_PICTURE_SHOW_ENABLED)) {
                    Intent intent = new Intent(mActivity, PictureSettingsActivity.class);
                    intent.putExtra(Config.INTENT_PICTURE_EDIT_MODE, true);
                    intent.putExtra(Config.INTENT_PICTURE_EDIT_ID, mPictureId);
                    intent.putExtra(Config.INTENT_PICTURE_EDIT_POSITION, holder.getAdapterPosition());
                    mActivity.startActivityForResult(intent, Config.REQUEST_CODE_ACTIVITY_PICTURE_SETTINGS_CHANGE);
                } else {
                    MainActivity.SnackShow(mActivity, R.string.action_warn_edit_hided_window);
                }
            }
        });

        holder.button_Picture_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ManageMethods.DeleteWin(mActivity, mPictureId);
                updateData();
                holder.switch_Picture_Show.setOnCheckedChangeListener(null);
                holder.button_Picture_Edit.setOnClickListener(null);
                holder.button_Picture_Delete.setOnClickListener(null);
                int position = holder.getAdapterPosition();
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount() - position);
                MainActivity.SnackShow(mActivity, R.string.action_delete_window);
            }
        });
    }

    @Override
    public ManageListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        View mView = inflater.inflate(R.layout.adapter_manage_list, parent, false);
        return new ManageListViewHolder(mView);
    }
}
