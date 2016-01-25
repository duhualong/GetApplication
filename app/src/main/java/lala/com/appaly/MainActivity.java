package lala.com.appaly;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    private TextView customAppsBtn, systemAppsBtn;
    private ListView lv;
    private List<PackageInfo> customApps; // 普通应用程序
    private List<PackageInfo> systemApps; // 系统应用程序
    MyAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customAppsBtn = (TextView)findViewById(R.id.button01);
        systemAppsBtn = (TextView)findViewById(R.id.button02);
        lv = (ListView)findViewById(R.id.lv);
        //加载系统应用和普通应用程序
        loadApps();
        adapter = new MyAdapter(this, customApps);
        lv.setAdapter(adapter);

        customAppsBtn.setOnClickListener(new TextView.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "普通应用", Toast.LENGTH_SHORT).show();
                //切换到普通程序列表
                updateAppList(customApps);
            }

        });
        systemAppsBtn.setOnClickListener(new TextView.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "系统应用", Toast.LENGTH_SHORT).show();
                //切换到系统程序列表
                updateAppList(systemApps);
            }

        });
    }

    //列出普通应用程序
    private void loadApps()
    {

        customApps = new ArrayList<PackageInfo>();
        systemApps = new ArrayList<PackageInfo>();
        //得到PackageManager对象
        PackageManager pm = this.getPackageManager();
        //得到系统安装的所有程序包的PackageInfo对象
        List<PackageInfo> packages = pm.getInstalledPackages(0);
        for(PackageInfo pi:packages)
        {
            //列出普通应用
            if((pi.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)<=0)
            {
                customApps.add(pi);
            }
            //列出系统应用，总是感觉这里设计的有问题，希望高手指点
            if((pi.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)>0)
            {
                systemApps.add(pi);
            }
        }
    }

    private void updateAppList(List<PackageInfo> apps)
    {
        adapter.setData(apps);
        adapter.notifyDataSetChanged();
    }

    //ViewHolder静态类
    static class ViewHolder
    {
        public ImageView appicon;
        public TextView appName;
        public TextView packageName;
    }

    class MyAdapter extends BaseAdapter
    {
        private LayoutInflater mInflater = null;
        private List<PackageInfo> apps;
        private MyAdapter(Context context, List<PackageInfo> apps)
        {
            this.mInflater = LayoutInflater.from(context);
            this.apps = apps;
        }
        //setData()要在MyAdapter类里设置，用于设置数据源
        public void setData(List<PackageInfo> apps)
        {
            this.apps = apps;
        }
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return customApps.size();
        }
        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null)
            {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.listitem, null);
                holder.appicon = (ImageView)convertView.findViewById(R.id.appicon);
                holder.appName = (TextView)convertView.findViewById(R.id.appName);
                holder.packageName = (TextView)convertView.findViewById(R.id.packageName);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder)convertView.getTag();
            }
            PackageManager pm = getPackageManager(); // 得到pm对象
            PackageInfo info = apps.get(position);
            ApplicationInfo appInfo = info.applicationInfo;

            holder.appicon.setImageDrawable(pm.getApplicationIcon(appInfo));
            holder.appName.setText(pm.getApplicationLabel(appInfo));
            holder.packageName.setText(appInfo.packageName);
            return convertView;
        }
    }
}