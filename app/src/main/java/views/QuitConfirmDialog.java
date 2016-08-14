package views;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.guaimao.guaimaoapp.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import listener.ReturnListener;

/**
 * Created by eleven on 16/8/12.
 */
public class QuitConfirmDialog extends DialogFragment {

    ReturnListener listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_quit_comfirm, container);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.tv_quit_dialog_confirm, R.id.tv_quit_dialog_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_quit_dialog_confirm:
                if (listener != null)
                    listener.clickConfirm();
                break;
            case R.id.tv_quit_dialog_cancel:
                if (listener != null)
                    listener.clickCancel();
                break;
        }
    }

    public void setListener(ReturnListener listener) {
        this.listener = listener;
    }
}
