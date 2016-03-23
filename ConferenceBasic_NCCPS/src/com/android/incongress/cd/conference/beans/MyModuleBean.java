package com.android.incongress.cd.conference.beans;

/**
 * Created by Jakcy on 2015/3/29.
 *
 * 支持的模块 包括模块名称，模块的Icon资源id，模块的开启与否
 */
public class MyModuleBean {
    private int name;
    private int iconId;
    private boolean isInUse;

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public boolean isInUse() {
        return isInUse;
    }

    public void setInUse(boolean isInUse) {
        this.isInUse = isInUse;
    }

    @Override
    public String toString() {
        return "MyModuleBean{" +
                "name='" + name + '\'' +
                ", iconId=" + iconId +
                ", isInUse=" + isInUse +
                '}';
    }

    public MyModuleBean(int name, int iconId, boolean isInUse) {
        this.name = name;
        this.iconId = iconId;
        this.isInUse = isInUse;
    }
}
