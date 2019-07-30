package com.frankhon.jgithubbrowsersample.ui.common;

import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.frankhon.jgithubbrowsersample.R;
import com.frankhon.jgithubbrowsersample.vo.Resource;

import butterknife.BindView;

import static com.frankhon.jgithubbrowsersample.util.RequestStatus.ERROR;
import static com.frankhon.jgithubbrowsersample.util.RequestStatus.LOADING;

/**
 * Created by Frank_Hon on 7/30/2019.
 * E-mail: v-shhong@microsoft.com
 */
public class LoadingFragment extends Fragment {
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.retry)
    Button retry;
    @BindView(R.id.error_msg)
    TextView errorMag;

    protected void processLoadingState(Resource resource){
        progressBar.setVisibility(resource.getStatus() == LOADING ? View.VISIBLE : View.GONE);
        retry.setVisibility(resource.getStatus() == ERROR ? View.VISIBLE : View.GONE);
        errorMag.setVisibility(resource.getStatus() == ERROR ? View.VISIBLE : View.GONE);
    }

    protected void setOnRetryClickListener(View.OnClickListener onRetryClickListener){
        retry.setOnClickListener(onRetryClickListener);
    }
}
