package cn.xiandu.app;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * Created by dell on 2016/12/27.
 */

public class HuiTinkerApplication extends TinkerApplication {

    public HuiTinkerApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "cn.xiandu.app.ApplicationDelegate",
                "com.tencent.tinker.loader.TinkerLoader", false);
        MyApplication myApplication = new MyApplication();
    }
}
