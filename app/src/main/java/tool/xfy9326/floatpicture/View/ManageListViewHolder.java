package tool.xfy9326.floatpicture.View;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import tool.xfy9326.floatpicture.R;

class ManageListViewHolder extends AdvancedRecyclerView.ViewHolder {
    final ImageView imageView_Picture_Preview;
    final Switch switch_Picture_Show;
    final TextView textView_Picture_Name;
    final TextView textView_Picture_Id;
    final Button button_Picture_Edit;
    final Button button_Picture_Delete;

    ManageListViewHolder(View mView) {
        super(mView);
        imageView_Picture_Preview = (ImageView) mView.findViewById(R.id.adapter_picture_preview);
        switch_Picture_Show = (Switch) mView.findViewById(R.id.adapter_picture_show);
        textView_Picture_Name = (TextView) mView.findViewById(R.id.adapter_picture_name);
        textView_Picture_Id = (TextView) mView.findViewById(R.id.adapter_picture_id);
        button_Picture_Edit = (Button) mView.findViewById(R.id.adapter_picture_edit);
        button_Picture_Delete = (Button) mView.findViewById(R.id.adapter_picture_delete);
    }
}
