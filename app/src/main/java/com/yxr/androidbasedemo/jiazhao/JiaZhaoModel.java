package com.yxr.androidbasedemo.jiazhao;

import androidx.lifecycle.Lifecycle;
import android.os.Handler;
import androidx.annotation.NonNull;
import android.util.Log;

import com.yxr.base.http.HttpSimpleListener;
import com.yxr.base.http.BaseSubscriber;
import com.yxr.base.http.HttpManager;
import com.yxr.base.model.BaseModel;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * @author ciba
 * @description 描述
 * @date 2020/9/17
 */
public class JiaZhaoModel extends BaseModel<JiaZhaoPresenter> {
    private Handler handler = new Handler();

    public JiaZhaoModel(@NonNull Lifecycle lifecycle, JiaZhaoPresenter presenter) {
        super(lifecycle, presenter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    public void getJiaZhaoResponse(@NonNull final Subject subject, @NonNull final Model model, @NonNull final TestType testType, final HttpSimpleListener<JiaZhaoResponse> httpListener) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("TTTTTTAG", "getJiaZhaoResponse ::::::: ");
                Map<String, String> queryMap = new HashMap<>(4);
                queryMap.put("key", "f329bc1003c9def3f0d11c97f78ab55f");
                queryMap.put("subject", subject.getSubject());
                queryMap.put("model", model.getModel());
                queryMap.put("testType", testType.getTestType());
                HttpManager.getInstance().request(getService(JiaZhaoService.class).getJiaZhaoResponse(queryMap), new BaseSubscriber<JiaZhaoResponse>(getLifecycle(), httpListener));
            }
        }, 2000);
    }

    public enum Subject {
        SUBJECT1("1"), SUBJECT4("4");

        private final String subject;

        Subject(String subject) {
            this.subject = subject;
        }

        public String getSubject() {
            return subject;
        }
    }

    public enum TestType {
        RAND("rand"), ORDER("order");

        private final String testType;

        TestType(String testType) {
            this.testType = testType;
        }

        public String getTestType() {
            return testType;
        }
    }

    public enum Model {
        A1("a1"), A2("a2"), B1("b1"), B2("b2"), C1("c1"), C2("c2");

        private final String model;

        Model(String model) {
            this.model = model;
        }

        public String getModel() {
            return model;
        }
    }

    public interface JiaZhaoService {
        @GET("jztk/query")
        Observable<Result<JiaZhaoResponse>> getJiaZhaoResponse(@QueryMap Map<String, String> queryMap);
    }
}
