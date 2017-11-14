package com.bwie.fragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.bwie.fragment.adapter.MyXlvAdapter;
import com.bwie.fragment.bean.Data;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import me.maxwin.view.XListView;

public class MainActivity extends AppCompatActivity implements XListView.IXListViewListener {

    private XListView xlv;
    private int startNum = 0;
    private List<Data.DataBean> list = new ArrayList<>();
    private MyXlvAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        x.view().inject(this);

        xlv = (XListView) findViewById(R.id.xlv);

        initView();
        loadData();

    }


    private void loadData() {

        RequestParams params = new RequestParams("http://www.93.gov.cn/93app/data.do");
        params.addQueryStringParameter("channelId", "1");
        params.addQueryStringParameter("startNum", "" + startNum);
        x.http().get(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Data data = gson.fromJson(result, Data.class);
                list.addAll(data.getData());
                adapter.notifyDataSetChanged();

                stopXlv();

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(MainActivity.this, "加载失败", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public boolean onCache(String result) {
                return false;
            }
        });
    }


    private void initView() {
        //加载更多内容
        xlv.setPullLoadEnable(true);
        //xlistview监听
        xlv.setXListViewListener(this);

        //适配器
        adapter = new MyXlvAdapter(this, list);
        xlv.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        startNum = 0;
        list.clear();
        loadData();

    }

    @Override
    public void onLoadMore() {
        startNum = list.size();
        loadData();

    }

    public void stopXlv() {
        xlv.stopRefresh();
        xlv.stopLoadMore();
        xlv.setRefreshTime("刚刚");
    }
}
