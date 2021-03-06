package com.github.keyboard3.developerinterview.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.keyboard3.developerinterview.Config;
import com.github.keyboard3.developerinterview.R;
import com.github.keyboard3.developerinterview.adapter.BaseAdapter;
import com.github.keyboard3.developerinterview.adapter.ProductAdapter;
import com.github.keyboard3.developerinterview.entity.PluginInfo;
import com.qihoo360.replugin.RePlugin;

import java.util.ArrayList;
import java.util.List;

public class ProductListFragment extends BaseFragment {

    private RecyclerView recyclerView;
    List<PluginInfo> list = new ArrayList<>();
    private ProductAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        recyclerView = getView().findViewById(R.id.rl_content);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ProductAdapter(list, getActivity());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                PluginInfo entity = list.get(position);
                com.qihoo360.replugin.model.PluginInfo pi = RePlugin.install(Config.StorageDirectory + "/" + entity.name + ".apk");
                if (pi != null) {
                    RePlugin.preload(pi);
                }
                showDialog();
                RePlugin.startActivity(getActivity(), RePlugin.createIntent(entity.packageName,
                        entity.mainClass));
            }
        });
        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder
                    viewHolder) {
                int flag = ItemTouchHelper.LEFT;
                return makeMovementFlags(0, flag);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder
                    viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                final PluginInfo entity = list.get(viewHolder.getAdapterPosition());
                new AlertDialog.Builder(getActivity()).setTitle("提示")
                        .setMessage("是否确定卸载该插件？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (RePlugin.uninstall(entity.packageName)) {
                                    Toast.makeText(getActivity(), "卸载成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), "卸载失败", Toast.LENGTH_SHORT).show();
                                }
                                dialogInterface.dismiss();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        adapter.notifyDataSetChanged();
                    }
                }).create().show();
            }
        };
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onPause() {
        super.onPause();
        hideDialog();
    }

    private void initData() {
        list.add(new PluginInfo("selfView",
                "com.github.keyboard3.selfview",
                "com.github.keyboard3.selfview.MainActivity",
                "自定义view集合"));

    }
}