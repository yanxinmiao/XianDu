package cn.xiandu.app.bean;

/**
 * Created by dell on 2016/9/12.
 *
 * 定义事件
 *
 * 所有能被实例化为 Object 的实例都可以作为事件
 * eventbus 3中如果用到了索引加速，事件类的修饰符必须为 public
 */

public class ThemeMode {
    private int mode ;

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
