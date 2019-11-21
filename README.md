# GroupItemDecoration
## 通过RecyclerView的ItemDecoration自定义分组列表分割线
### 实现效果图如下：

<img src="https://raw.githubusercontent.com/MTences/GroupItemDecoration/master/imgs/pic.gif" width=320/>
<img src="https://raw.githubusercontent.com/MTences/GroupItemDecoration/master/imgs/screen_pic.png" width=320/>
### 实现原理：
- 继承RecyclerView.ItemDecoration；
- 重新onDrawOver（）方法，即吸顶实现；
- 重写getItemOffsets（）方法，即计算分组bar以及普通divider的宽高；
- 重写onDraw（）方法，即画出 bar 和 divider 以及bar 上的文字和图标等信息。
#### 其次开放了部分公用变量的设置方法，可供使用者调用使用，以及数据设置的回调方法，以便于使用者更好控制数据信息。
##### 如下使用方式：
```java
HashMap<Integer,String> map = new HashMap<>();
map.put(0,"长相思");
map.put(10,"白毛女");
map.put(15,"大雪纷飞");
map.put(20,"班门弄斧");
map.put(33,"农用机器人老吴");
map.put(25,"北京一夜冷了脚");

HashMap<Integer,String> iconMap = new HashMap<>();
iconMap.put(0,R.mipmap.ic_launcher);
iconMap.put(10,R.mipmap.ic_launcher);
iconMap.put(15,R.mipmap.ic_launcher);
iconMap.put(20,R.mipmap.ic_launcher);
iconMap.put(33,R.mipmap.ic_launcher);
iconMap.put(25,R.mipmap.ic_launcher);
GroupItemDecoration groupItemDecoration = new GroupItemDecoration(this);
        groupItemDecoration
                .setGroupBackgroundColor(R.color.colorPrimaryDark)
                .setGroupDividerHeight(80)
                .setGroupDividerTextColor(R.color.colorFFFFFF)
                .setGroupDividerTextSize(15)
                .setGroupTextMarginLeft(15)
                .setGroupTextMarginRight(15)
                .setIconHeight(50)
                .setIconMarginLeft(15)
                .setSubDividerColor(R.color.colorPrimary)
                .setSubDividerHeight(1)
                .setSubDividerMarginLeft(15)
                .setSubDividerMarginRight(15)
                .setNeedCeiling(true)
                .setNeedLastDivider(true)
                .Builder();
        groupItemDecoration.setNeedTitleCallback(new GroupItemDecoration.NeedTitleCallback() {
            @Override
            public HashMap<Integer, String> titleMap() {
                return map;
            }

            @Override
            public HashMap<Integer, Integer> iconMap() {
                return iconMap;
            }
        });
        recyclerView.addItemDecoration(groupItemDecoration);
```
#### 罗列出以下公用方法

```java
    //设置群组的divider
    setGroupDivider(@DrawableRes int drawableRes)

    //设置群组divider的背景颜色，与设置群组的divider互斥，如果同时设置，则以设置的divider为准
    setGroupBackgroundColor(@ColorRes int colorRes)

    //设置群组divier的高度
    setGroupDividerHeight(float dimenRes)

    //设置群组字体颜色
    setGroupDividerTextColor(@ColorRes int colorRes)

    //设置群组字体大小
    setGroupDividerTextSize(float dimenRes)

    //设置群组文字gravity
    setGroupDividerTextGravity(int gravity)

    //设置群组文字离边距
    setGroupTextMarginLeft(float margin)

    //设置群组文字离边距
    setGroupTextMarginRight(float margin)

    //设置普通的divider
    setSubDivider(@DrawableRes int drawableRes)

    //设置普通的divider的颜色
    setSubDividerColor(@ColorRes int colorRes)

    //设置普通divider的高度
    setSubDividerHeight(float dimenRes)

    //设置二级divider的margin
    setSubDividerMarginLeft(float margin)
    setSubDividerMarginRight(float margin)

    //设置icon图标高度
    setIconHeight(float iconHeight)

    //设置icon图标左边边距
    setIconMarginLeft(float iconMarginLeft)

    //设置icon图标右边边距
    setIconMarginRight(float iconMarginRight)

    //设置是否需要吸顶
    setNeedCeiling(boolean needCeiling)

    //设置是否显示最后一条divider
    setNeedLastDivider(boolean needLastDivider)

```
#### 设置完自己需要的属性后需要Builder一次








