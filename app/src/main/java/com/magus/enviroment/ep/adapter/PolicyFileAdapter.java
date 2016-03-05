package com.magus.enviroment.ep.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.magus.enviroment.R;
import com.magus.enviroment.ep.bean.PolicyFile;
import com.magus.enviroment.ep.util.MyDownload;
import com.magus.enviroment.ep.util.MyDownloadHandler;
import com.magus.enviroment.ui.UIUtil;

import java.io.File;
import java.util.List;

/**
 * Created by pau on 15/7/17.
 */
public class PolicyFileAdapter extends BaseAdapter {
    private static final String TAG = "AttentionDetailAdapter";

    private List<PolicyFile> mList;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private ProgressDialog mProgressDialog;

    public PolicyFileAdapter(Context context, List<PolicyFile> list) {
        this.mList = list;
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);

    }

    public void setList(List<PolicyFile> list) {
        if (list != null) {
            this.mList = list;
            notifyDataSetChanged();
        } else {
            return;
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();//初始化控件
            convertView = mLayoutInflater.inflate(R.layout.item_policy_file, null);
            holder.pdfName = (TextView) convertView.findViewById(R.id.pdf_name);
            holder.pdfDownload = (TextView) convertView.findViewById(R.id.pdf_download);
            holder.pdfRead = (TextView) convertView.findViewById(R.id.pdf_read);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final PolicyFile policyFile = mList.get(position);
        String[] names = policyFile.getFileUrl().split("/");
        final String path = Environment.getExternalStorageDirectory()
                + "/Environment/download/" + names[names.length - 1];

        final File file = new File(path);
        final boolean isFileExists = file.exists();
        if (isFileExists) {
            holder.pdfDownload.setText("已下载");
        }
        holder.pdfName.setText(policyFile.getFileName());

        System.out.println("文件地址" + path);
        holder.pdfRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final File fi = new File(path);
                boolean isDow=fi.exists();
                if (isDow) {
                    openFile(file);
                } else {
                    UIUtil.toast(mContext, "文件不存在，请先下载");
                }
            }
        });
        holder.pdfDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFileExists) {
                    openFile(file);
                } else {
                    mProgressDialog = UIUtil.initDialog(mContext, "正在下载请稍后...");
                    mProgressDialog.show();
                    MyDownload myDownload = new MyDownload(policyFile.getFileUrl(), new MyDownloadHandler() {
                        @Override
                        public void onSuccess(File file) {
                            openFile(file);
                            new Handler(mContext.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    holder.pdfDownload.setText("已下载");
                                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                        mProgressDialog.dismiss();
                                        mProgressDialog = null;
                                    }
                                }
                            });
                        }

                        @Override
                        public void onFailed() {
                            ((Activity) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                        mProgressDialog.dismiss();
                                        mProgressDialog = null;
                                    }
                                    UIUtil.toast(mContext, "下载失败");
                                }
                            });
                        }
                    });
                    new Thread(myDownload) {
                    }.start();
                }
            }
        });
        return convertView;
    }

    /**
     * 打开pdf
     *
     * @param file
     */
    private void openFile(File file) {
        System.out.println(file);
        if (file.exists()) {
            android.util.Log.e(TAG, "打开");
            Uri path = Uri.fromFile(file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(path, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mContext.startActivity(intent);
        } else {
            UIUtil.toast(mContext, "文件不存在，请先下载");
        }
    }

    class ViewHolder {
        TextView pdfName;
        TextView pdfDownload;
        TextView pdfRead;

    }
}
