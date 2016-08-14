package views;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.guaimao.guaimaoapp.R;
import com.tendcloud.tenddata.TCAgent;

import butterknife.ButterKnife;
import butterknife.OnClick;
import listener.ReturnListener;

/**
 * Created by eleven on 16/8/11.
 */
public class GeneralDialogFragment extends DialogFragment {

    ReturnListener listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.activity_dialog, container);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick({R.id.ll_dialog_refresh, R.id.ll_dialog_quit_game, R.id.ic_dialog_clear_cache, R.id.ic_dialog_return_home})
    public void onClick(View view) {
        dismiss();
        if (null != listener) {
            switch (view.getId()) {
                case R.id.ll_dialog_refresh:
                    TCAgent.onEvent(getActivity(), "ClickRefreshButton");
                    listener.clickRefresh();
//                    Toast.makeText(getActivity(), "点击了刷新按钮", Toast.LENGTH_SHORT);
                    break;
                case R.id.ll_dialog_quit_game:
//                    Toast.makeText(getActivity(), "点击了退出游戏按钮", Toast.LENGTH_SHORT);
                    TCAgent.onEvent(getActivity(), "ClickQuitGameButton");
                    listener.clickQuitGame();
                    break;
                case R.id.ic_dialog_clear_cache:
                    TCAgent.onEvent(getActivity(), "ClickClearCacheButton");
                    listener.clickClearCache();
//                    Toast.makeText(getActivity(), "点击了清楚缓存按钮", Toast.LENGTH_SHORT);
                    break;
                case R.id.ic_dialog_return_home:
                    TCAgent.onEvent(getActivity(), "ClickRetrunHomeButton");
                    listener.clickGoHome();
//                    Toast.makeText(getActivity(), "点击了返回首页按钮", Toast.LENGTH_SHORT);
                    break;
            }
        }
    }


    public void setListener(ReturnListener listener) {
        this.listener = listener;
    }

}
