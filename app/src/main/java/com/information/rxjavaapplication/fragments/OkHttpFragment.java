package com.information.rxjavaapplication.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.information.rxjavaapplication.R;
import com.information.rxjavaapplication.logs.LogAdapter;
import com.information.rxjavaapplication.square.Contributor;
import com.information.rxjavaapplication.square.GitHubServiceApi;
import com.information.rxjavaapplication.square.RestfulAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OkHttpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OkHttpFragment extends Fragment {
    private static final String sName = "jungjoonpark-pandora";
    private static final String sRepo = "rxAndroid";

    private Unbinder mUnbinder;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    // Log
    private LogAdapter mLogAdapter;
    private List<String> mLogs;

    @BindView(R.id.ohf_lv_log)
    ListView mLogView;

    public static OkHttpFragment newInstance() {
        OkHttpFragment fragment = new OkHttpFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupLogger();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_okhttp, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick(R.id.ohf_btn_retrofit)
    void getRetrofit() {
        startRetrofit();
    }

    /**
     * Retrofit + okHttp(Retrofit의 내부)
     */
    private void startRetrofit() {
        GitHubServiceApi service = RestfulAdapter.getInstance().getSimpleApi();
        Call<List<Contributor>> call = service.getCallContributors(sName, sRepo);
        // enqueue에 콜백을 등록하면 JSON에서 디코딩한 결과를 화면에 업데이트 가능
        call.enqueue(new Callback<List<Contributor>>() {
            @Override
            public void onResponse(Call<List<Contributor>> call, Response<List<Contributor>> response) {
                if(response.isSuccessful()) {
                    List<Contributor> contributors = response.body();
                    for (Contributor c:contributors) {
                        log(c.toString());
                    }
                } else {
                    log("not successful");
                }
            }

            @Override
            public void onFailure(Call<List<Contributor>> call, Throwable t) {
                log(t.getMessage());
            }
        });
    }

    @OnClick(R.id.ohf_btn_get_retrofit_okhttp)
    void getOkhttp() {
        startOkHttp();
    }

    /**
     * retrofit + okHttp
     */
    private void startOkHttp() {
        GitHubServiceApi service = RestfulAdapter.getInstance().getServiceApi();
        Call<List<Contributor>> call = service.getCallContributors(sName, sRepo);
        // enqueue: sendRequest
        call.enqueue(new Callback<List<Contributor>>() {
            @Override
            public void onResponse(Call<List<Contributor>> call, Response<List<Contributor>> response) {
                if (response.isSuccessful()) {
                    List<Contributor> contributors = response.body();
                    for (Contributor c : contributors) {
                        log(c.toString());
                    }
                } else {
                    log("not successful");
                }
            }

            @Override
            public void onFailure(Call<List<Contributor>> call, Throwable t) {
                log(t.getMessage());
            }
        });
    }

    @OnClick(R.id.ohf_btn_get_retrofit_okhttp_rx)
    void getRx() {
        startRx();
    }

    /**
     * Rtrofit + OkHttp + RxJava
     */
    private void startRx() {
        // getServiceApi()메서드 안의 retrofit 변수를 이용해 생성된 API프락시를 가져옴
        GitHubServiceApi service = RestfulAdapter.getInstance().getServiceApi();
        // owner와 repo의 값을 전달하면 observable변수에 저장된 Observable을 리턴
        Observable<List<Contributor>> observable = service.getObContributors(sName, sRepo);

        mCompositeDisposable.add(
                // 생성된 Observable에 구독자를 설정하면 getServiceApi()메서드를 호출하여 github.com에서 정보를 얻어옴
                // 결과는 구독자가 수신하게 되고 GSON에서 Contributor클래스의 구조에 맞게 디코딩
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeWith(new DisposableObserver<List<Contributor>>() {
                            @Override
                            public void onNext(List<Contributor> contributors) {
                                for(Contributor c:contributors) {
                                    log(c.toString());
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                log(e.getMessage());
                            }

                            @Override
                            public void onComplete() {
                                log("complete");
                            }
                        }));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mUnbinder!=null) mUnbinder.unbind();
        if(mCompositeDisposable!=null) mCompositeDisposable.clear();
    }

    private void log(String log) {
        mLogs.add(log);
        mLogAdapter.clear();
        mLogAdapter.addAll(mLogs);
    }

    private void setupLogger() {
        mLogs = new ArrayList<>();
        mLogAdapter = new LogAdapter(getActivity(), new ArrayList<>());
        mLogView.setAdapter(mLogAdapter);
    }
}