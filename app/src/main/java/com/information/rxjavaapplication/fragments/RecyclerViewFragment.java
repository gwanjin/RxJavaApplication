package com.information.rxjavaapplication.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.information.rxjavaapplication.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RecyclerViewFragment extends Fragment {
    public static final String TAG = RecyclerViewFragment.class.getSimpleName();

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    // RecyclerView는 단지 RecyclerView 항목들을 재활용하고 보여주는 역할만
    // Adapter는 필요한 ViewHolder 객체를 생성하고 데이터를 ViewHolder 객체와 결합하는 역할
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        mUnbinder = ButterKnife.bind(this, layout);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // RecyclerView는 직접 화면에 리스트 항목들을 위치시키진 않고, LayoutManager에게 해당 작업을 위임
        // LayoutManager는 리스트 항복 View들을 화면에 위치시키고 스크롤 동작을 처리
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerViewAdapter = new RecyclerViewAdapter();
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        // PublishSubject는 클릭이벤트를 구독중 -> Click 이벤트가 RecyclerItem 발행
        // publishSubject는 RecyclerItem를 발행
        // PublishSubject<RecyclerItem> -> RecyclerItem객체리턴
        mRecyclerViewAdapter.getItemPublishSubject().subscribe(s -> toast(s.getTitle()));
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mRecyclerViewAdapter == null) {
            return;
        }

        // 패키지 목록 표시
        getItemObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(item -> {
                    mRecyclerViewAdapter.updateItems(item);
                    // 리스트 항목 뷰에 들어간 데이터가 바꼈을 때 호출
                    // DataSet이 변경된 경우 그 내용을 등록된 Observer에 전달
                    // LayoutManager는 모든 자룔르 다시 바인딩하고 모든 View를 다시 레이아웃하게됨
                    mRecyclerViewAdapter.notifyDataSetChanged();
                });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        mUnbinder = null;
    }


    private Observable<RecyclerItem> getItemObservable() {

        final PackageManager pm = getActivity().getPackageManager();
        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        // 패키지 목록들을 RecyclerItem으로 순서대로 발행
        return Observable.fromIterable(pm.queryIntentActivities(i, 0))
                .sorted(new ResolveInfo.DisplayNameComparator(pm))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(item -> {
                    Drawable image = item.activityInfo.loadIcon(pm);
                    String title = item.activityInfo.loadLabel(pm).toString();
                    return new RecyclerItem(image, title);
                });
    }


    private void toast(String title) {
        Toast.makeText(getActivity().getApplicationContext(), title, Toast.LENGTH_SHORT).show();
    }
}
