package com.information.rxjavaapplication.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.information.rxjavaapplication.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private List<RecyclerItem> mItems = new ArrayList<>();
    private PublishSubject<RecyclerItem> mPublishSubject;

    RecyclerViewAdapter() {
        this.mPublishSubject = PublishSubject.create();
    }


    /**
     * onCreateViewHolder에서 ViewHolder 객체를 생성
     * getItemCount()호출 후, Adapter의 onCreateViewHolder(ViewGroup, int) 메서드를 호출한다.
     * onCreateviewHolder가 실행되면 RecyclerView는 Adapter에게 ViewHolder 객체를 받음.
     * @param parent
     * @param viewType
     * @return ViewHolder 리턴
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 직접 정의한 ViewHolder을 리턴
        // 경우에 따라 한번만 호출
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        return new MyViewHolder(view);
    }

    /**
     *
     * onBindViewholder(ViewHolder, int)를 호출하면서 전에 Adapter에게 받았던 ViewHolder 객체와 리스트에서 해당 ViewHolder의 위치를 인자로 전달한다
     * Adapter는 인자로 받은 위치에 맞는 데이터를 찾은 후 ViewHolder의 View에 결합
     * @param holder ：Adapter에게 받았던 ViewHolder 객체
     * @param position ：리스트에서 해당 ViewHolder의 위치
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // RecyclerView에서 받은 위치 값을 토대로 알맞은 데이터를 찾은 후, holder인자의 뷰 아이템에 값을 넣어줌
        final RecyclerItem item = mItems.get(position);
        holder.mImageView.setImageDrawable(item.getImage());
        holder.mTitleView.setText(item.getTitle());
        // 실시간으로 처리되어야 하기때문에 PublishSubject로 구현
        holder.getClickObserver(item).subscribe(mPublishSubject);
    }

    /**
     *
     * 먼저 RecyclerView는 우리가 구현할 Adapter의 getItemCount() 메서드를 호출해서 RecyclerView에서 보여줘야하는 리스트 항목 총 갯수를 요청
     *
    * */
    @Override
    public int getItemCount() {
        // DataSet을 직접 구현
        // 저장된 배열 요소 개수 리턴
        return mItems.size();
    }

    public void updateItems(List<RecyclerItem> items) {
        mItems.addAll(items);
    }

    public void updateItems(RecyclerItem item) {
        mItems.add(item);
    }

    public PublishSubject<RecyclerItem> getItemPublishSubject() {
        return mPublishSubject;
    }

    // ViewHolder는 리스트 항목 하나의 View를 만들고 보존
    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_image)
        ImageView mImageView;
        @BindView(R.id.item_title)
        TextView mTitleView;

        /**
         *
         * @param itemView ：리스트 항목에 대한 레이아웃
         */
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        Observable<RecyclerItem> getClickObserver(RecyclerItem item) {
            // Click시 RecyclerItem(클릭한 아이템)을 발행
            return Observable.create(e -> itemView.setOnClickListener(view -> e.onNext(item)));
//            return Observable.create(emitter -> itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    emitter.onNext(item);
//                }
//            }));
        }
    }
}
