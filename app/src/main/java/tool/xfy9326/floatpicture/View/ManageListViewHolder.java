package tool.xfy9326.floatpicture.View;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import tool.xfy9326.floatpicture.R;

public class ManageListViewHolder extends AdvancedRecyclerView.ViewHolder {
    final ImageView imageView_Picture_Preview;
    final SwitchCompat switch_Picture_Show;
    final TextView textView_Picture_Name;
    final TextView textView_Picture_Id;
    final TextView textView_Picture_Error;
    final Button button_Picture_Edit;
    final Button button_Picture_Delete;

    ManageListViewHolder(View mView) {
        super(mView);
        imageView_Picture_Preview = mView.findViewById(R.id.adapter_picture_preview);
        switch_Picture_Show = mView.findViewById(R.id.adapter_picture_show);
        textView_Picture_Name = mView.findViewById(R.id.adapter_picture_name);
        textView_Picture_Id = mView.findViewById(R.id.adapter_picture_id);
        textView_Picture_Error = mView.findViewById(R.id.adapter_picture_error);
        button_Picture_Edit = mView.findViewById(R.id.adapter_picture_edit);
        button_Picture_Delete = mView.findViewById(R.id.adapter_picture_delete);
    }
}
