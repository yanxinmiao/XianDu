package cn.xiandu.app.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.xiandu.app.utils.PathUtils;
import cn.xiandu.app.utils.ToastUtils;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PicViewActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.image)
    ImageView image;
    private String url, id;
    PhotoViewAttacher mAttacher;
    private final int SAVEFINISH_CODE = 10002;
    private Bitmap bm;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SAVEFINISH_CODE:
                    ToastUtils.showMyToast("已保存到相册");
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);
        ButterKnife.bind(this);

        toolbar.setTitle("查看大图");
        toolbar.setContentInsetStartWithNavigation(0);
        toolbar.setTitleTextAppearance(this, R.style.Theme_ToolBar_Base_Title);//修改主标题的外观，包括文字颜色，文字大小等
        toolbar.inflateMenu(R.menu.pic_toolbar_meun);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_download:
                        if (bm == null) {
                            return true;
                        }
                        save();
                        break;
                }
                return false;
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        url = getIntent().getStringExtra("url");
        id = getIntent().getStringExtra("id");
        Logger.d("url:" + url);
        Logger.d("id:" + id);
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Glide
                .with(this) // could be an issue!
                .load(url)
                .asBitmap()
                .placeholder(R.drawable.default_news_cat_pic)
                .error(R.drawable.default_news_cat_pic)
                .into(target);
    }

    private SimpleTarget target = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
            bm = bitmap;
            image.setImageBitmap(bitmap);
            mAttacher = new PhotoViewAttacher(image);
        }
    };

    private void save() {
        String savePath = PathUtils.getPubImageCachePath() + id + ".jpg";
        File f = new File(savePath);
        if (f.exists()) {
            ToastUtils.showMyToast("该图片已保存");
            return;
        }
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                saveBitmap(bm);
            }
        });
    }

    /**
     * 把裁剪后的图片保存到本地方法
     */
    public void saveBitmap(Bitmap bm) {
        String savePath = PathUtils.getPubImageCachePath() + id + ".jpg";
        File f = new File(savePath);
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            Message msg = Message.obtain();
            msg.what = SAVEFINISH_CODE;
            handler.sendMessage(msg);
            //发广播，更新图库
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(f);
            intent.setData(uri);
            sendBroadcast(intent);
        } catch (Exception e) {

        }
    }

}
