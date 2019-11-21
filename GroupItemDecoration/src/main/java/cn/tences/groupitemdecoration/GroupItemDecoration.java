package cn.tences.groupitemdecoration;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

/**
 * date：2019/11/19 15:13
 * author：tences
 * email：m.tences@qq.com
 * description：
 */
public class GroupItemDecoration extends RecyclerView.ItemDecoration {

    private Context context;

    private Drawable dividerDrawble;
    private Drawable groupDivider;
    private NeedTitleCallback needTitleCallback;
    private Paint paintCeiling;
    private float groupDividerHeight = 25;
    private float textMarginLeft = 0;
    private float textMarginRight = 0;
    private int groupBgColor = 0xFF444444;
    private int mGravity = Gravity.START;
    private TextPaint textPaint;
    private int textColor = 0xFFFFFFFF;
    private float textSize = 30;
    private Paint.FontMetrics fontMetrics;
    private Rect text_rect = new Rect();

    private Paint paintNormalDivider;
    private int normalDividerColor = 0xFF444444;
    private float normalDividerHeight = 2;
    private float normalDividerMarginLeft = 0;
    private float normalDividerMarginRight = 0;

    private float iconHeight = -1;
    private float iconMarginLeft = 0;
    private float iconMarginRight = 0;

    //吸顶时，记录上次吸顶position
    private int oldChildPosition = -1;
    //默认不需要吸顶
    private boolean needCeiling = false;
    //默认不需要最后一条分割线
    private boolean needLastDivider = false;

    public void setNeedTitleCallback(NeedTitleCallback needTitleCallback) {
        this.needTitleCallback = needTitleCallback;
    }

    public GroupItemDecoration(Context context) {
        this.context = context;
    }

    //设置群组的divider
    public GroupItemDecoration setGroupDivider(@DrawableRes int drawableRes){
        this.groupDivider = context.getResources().getDrawable(drawableRes);
        return this;
    }
    //设置群组divider的背景颜色，与 设置 群组的divider 互斥，如果同时设置，则以设置的divider为准
    public GroupItemDecoration setGroupBackgroundColor(@ColorRes int colorRes){
        this.groupBgColor = context.getResources().getColor(colorRes);
        return this;
    }
    //设置群组divier的高度
    public GroupItemDecoration setGroupDividerHeight(float dimenRes){
        this.groupDividerHeight = dp2px(dimenRes);
        return this;
    }
    //设置群组字体颜色
    public GroupItemDecoration setGroupDividerTextColor(@ColorRes int colorRes){
        this.textColor = context.getResources().getColor(colorRes);
        return this;
    }
    //设置群组字体大小
    public GroupItemDecoration setGroupDividerTextSize(float dimenRes){
        this.textSize = dp2px(dimenRes);
        return this;
    }
    //设置群组文字gravity
    public GroupItemDecoration setGroupDividerTextGravity(int gravity){
        this.mGravity = gravity;
        return this;
    }
    //设置群组文字离边距
    public GroupItemDecoration setGroupTextMarginLeft(float margin){
        this.textMarginLeft = dp2px(margin);
        return this;
    }
    //设置群组文字离边距
    public GroupItemDecoration setGroupTextMarginRight(float margin){
        this.textMarginRight = dp2px(margin);
        return this;
    }
    //设置普通的divider
    public GroupItemDecoration setSubDivider(@DrawableRes int drawableRes){
        this.dividerDrawble = context.getResources().getDrawable(drawableRes);
        return this;
    }
    //设置普通的divider的颜色
    public GroupItemDecoration setSubDividerColor(@ColorRes int colorRes){
        this.normalDividerColor = context.getResources().getColor(colorRes);
        return this;
    }
    //设置普通divider的高度
    public GroupItemDecoration setSubDividerHeight(float dimenRes){
        this.normalDividerHeight = dp2px(dimenRes);
        return this;
    }
    //设置二级divider的margin
    public GroupItemDecoration setSubDividerMarginLeft(float margin){
        this.normalDividerMarginLeft = dp2px(margin);
        return this;
    }
    public GroupItemDecoration setSubDividerMarginRight(float margin){
        this.normalDividerMarginRight = dp2px(margin);
        return this;
    }
    //设置icon图标高度
    public GroupItemDecoration setIconHeight(float iconHeight) {
        this.iconHeight = dp2px(iconHeight);
        return this;
    }
    //设置icon图标左边边距
    public GroupItemDecoration setIconMarginLeft(float iconMarginLeft) {
        this.iconMarginLeft = dp2px(iconMarginLeft);
        return this;
    }
    //设置icon图标右边边距
    public GroupItemDecoration setIconMarginRight(float iconMarginRight) {
        this.iconMarginRight = dp2px(iconMarginRight);
        return this;
    }
    //设置是否需要吸顶
    public GroupItemDecoration setNeedCeiling(boolean needCeiling) {
        this.needCeiling = needCeiling;
        return this;
    }
    //设置是否显示最后一条divider
    public GroupItemDecoration setNeedLastDivider(boolean needLastDivider) {
        this.needLastDivider = needLastDivider;
        return this;
    }

    public void Builder(){
        if (groupDivider == null){
            paintCeiling = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
            paintCeiling.setColor(groupBgColor);
        }else {
            groupDividerHeight = groupDivider.getIntrinsicHeight();
        }

        if (textSize > groupDividerHeight){
            throw new IllegalArgumentException("请检查bar中字体尺寸，bar中字体尺寸不能大于bar的高度！");
        }

        if (dividerDrawble == null){
            paintNormalDivider = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
            paintNormalDivider.setColor(normalDividerColor);
        }else {
            normalDividerHeight = dividerDrawble.getIntrinsicHeight();
        }

        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        textPaint.setColor(textColor);
        textPaint.setTextAlign(Paint.Align.LEFT);

        textPaint.setTextSize(textSize);
        fontMetrics = new Paint.FontMetrics();
        textPaint.getFontMetrics(fontMetrics);
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (needTitleCallback != null && needCeiling) {
            View child = parent.getChildAt(0);
            int position = parent.getChildAdapterPosition(child);
            //处理下拉状态group文字不对应的问题
            String content;
            Bitmap bitmap = null;
            if (needTitleCallback.titleMap()!=null&&needTitleCallback.titleMap().keySet().contains(position)) {
                content = needTitleCallback.titleMap().get(position);
                if (needTitleCallback.iconMap() != null && needTitleCallback.iconMap().keySet().contains(position)){
                    bitmap =scaleBitmap(BitmapFactory.decodeResource(context.getResources(), needTitleCallback.iconMap().get(position)));
                }
                oldChildPosition = position;
            } else {
                int preKey = oldChildPosition;
                if (oldChildPosition > position) {
                    for (int key : needTitleCallback.titleMap().keySet()) {
                        if (oldChildPosition > key) {
                            if (preKey == oldChildPosition){
                                preKey = key;
                            }else {
                                preKey = Math.max(key,preKey);
                            }
                        }
                    }
                }
                content = needTitleCallback.titleMap() != null ? needTitleCallback.titleMap().get(preKey):"";
                if (needTitleCallback.iconMap() != null && needTitleCallback.iconMap().keySet().contains(preKey)){
                    bitmap =scaleBitmap(BitmapFactory.decodeResource(context.getResources(), needTitleCallback.iconMap().get(preKey)));
                }
            }
            textPaint.getTextBounds(content, 0, content.length(), text_rect);

            //查找可见列表中有title的item
            int childCount = parent.getChildCount();
            View childTemp = null;
            temp:for (int i = 0; i < childCount; i++) {
                childTemp = parent.getChildAt(i);
                int childLayoutPosition = parent.getChildLayoutPosition(childTemp);
                for (int pos : needTitleCallback.titleMap().keySet()){
                    if (pos == childLayoutPosition)break temp;
                }
            }

            int bottom = child.getBottom();
            if (childTemp != null){
                bottom = childTemp.getTop() - Math.round(groupDividerHeight);
                //如果第一个item的Bottom<=分割线的高度
                if (bottom > 0 && bottom <= groupDividerHeight) {
                    //根据recyclerview滑动改变吸顶的状态
                    slidingCeiling(c, parent, bottom, content, bitmap);
                } else {
                    //固定不动
                    ceiling(c, parent, content, bitmap);
                }
            }else {
                //固定不动
                ceiling(c, parent, content, bitmap);
            }
        }
    }

    /**
     * 吸顶固定状态
     * @param c
     * @param parent
     * @param content
     * @param bitmap
     */
    private void ceiling(@NonNull Canvas c, @NonNull RecyclerView parent, String content, Bitmap bitmap) {
        //画group
        if (groupDivider != null){//如果用户没有传入 drawableRes 则安卓用户传入的groupDividerHeight设置  默认groupDividerHeight 25
            groupDivider.setBounds(0,0,parent.getWidth() - parent.getPaddingRight(), Math.round(groupDividerHeight));
            groupDivider.draw(c);
        }else {
            c.drawRect(0, 0, parent.getWidth() - parent.getPaddingRight(), groupDividerHeight, paintCeiling);
        }
        //计算图标位置
        int iconHeight = 0;
        int iconWidth = 0;
        float iconLeft = 0;
        if (bitmap != null){
            iconHeight = bitmap.getHeight();
            iconWidth = bitmap.getWidth();
        }
        if (mGravity == Gravity.CENTER){
            iconLeft = (parent.getWidth() - iconWidth - text_rect.width() - textMarginLeft - iconMarginRight)/2.0f;
        }else if (mGravity == Gravity.END||mGravity==Gravity.RIGHT){
            iconLeft = parent.getWidth() - textMarginRight - textMarginLeft - text_rect.width() - iconWidth - iconMarginRight;
        }else {
            iconLeft = iconMarginLeft;
        }
        //画图标
        if (bitmap != null){
            c.drawBitmap(bitmap,iconLeft , (groupDividerHeight - iconHeight)/2.0f,textPaint);
        }
        //画文字
        float baseLineY = groupDividerHeight - (groupDividerHeight - (fontMetrics.descent - fontMetrics.ascent))/2.0f - fontMetrics.descent;
        if (bitmap != null){
            c.drawText(content, iconLeft + textMarginLeft + iconWidth + iconMarginRight, baseLineY,textPaint);
        }else {//如果没有图标则只使用设置的图标左边距作为文字的起始位置
            c.drawText(content, iconLeft, baseLineY,textPaint);
        }
    }

    /**
     * 滑动切换吸顶条目
     * @param c
     * @param parent
     * @param childBottom
     * @param content
     * @param bitmap
     */
    private void slidingCeiling(@NonNull Canvas c, @NonNull RecyclerView parent, int childBottom, String content, Bitmap bitmap) {
        //画吸顶栏，随着RecyclerView滑动 分割线的top=固定为0不动,bottom则赋值为child的bottom值.
        if (groupDivider != null){//如果用户没有传入 drawableRes 则安卓用户传入的groupDividerHeight设置  默认groupDividerHeight 25
            groupDivider.setBounds(0,0,parent.getWidth() - parent.getPaddingRight(), childBottom);
            groupDivider.draw(c);
        }else {
            c.drawRect(0, 0, parent.getWidth() - parent.getPaddingRight(), childBottom, paintCeiling);
        }

        //计算图标位置
        int iconHeight = 0;
        int iconWidth = 0;
        float iconLeft = 0;
        if (bitmap != null){
            iconHeight = bitmap.getHeight();
            iconWidth = bitmap.getWidth();
        }
        //根据用户设置的gravity设置group显示位置
        if (mGravity == Gravity.CENTER){
            iconLeft = (parent.getWidth() - iconWidth - text_rect.width() - textMarginLeft - iconMarginRight)/2.0f;
        }else if (mGravity == Gravity.END||mGravity==Gravity.RIGHT){
            iconLeft = parent.getWidth() - textMarginRight - textMarginLeft - text_rect.width() - iconWidth - iconMarginRight;
        }else {
            iconLeft = iconMarginLeft;
        }

        //画图标
        if (bitmap != null){
            c.drawBitmap(bitmap,iconLeft ,childBottom - (groupDividerHeight - iconHeight)/2.0f - iconHeight,textPaint);
        }
        //根据icon的位置开始画文字
        float baseLineY = childBottom- (groupDividerHeight - (fontMetrics.descent - fontMetrics.ascent))/2.0f - fontMetrics.descent;
        if (bitmap != null){
            c.drawText(content, iconLeft + iconWidth + iconMarginRight + textMarginLeft, baseLineY,textPaint);
        }else {
            c.drawText(content, iconLeft, baseLineY,textPaint);
        }

    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

        int gLeft = parent.getPaddingLeft();
        int gRight = parent.getWidth() - parent.getPaddingRight();
        int left = parent.getPaddingLeft() + Math.round(normalDividerMarginLeft);
        int right = parent.getWidth() - parent.getPaddingRight() - Math.round(normalDividerMarginRight);
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int childLayoutPosition = parent.getChildLayoutPosition(child);

            //画GroupBar
            drawGroupView(c, parent, gLeft, gRight, child, params, childLayoutPosition);
            //如果回调为空，直接执行即可，并且默认列表最后一条没有分割线
            if (emptyCallback(c, parent, left, right, child, params, childLayoutPosition)) continue;
            //画二级分割线
            childItemDivider(c, parent, left, right, child, params, childLayoutPosition);
        }
    }

    /**
     * 画二级分割线
     * @param c
     * @param parent
     * @param left
     * @param right
     * @param child
     * @param params
     * @param childLayoutPosition
     */
    private void childItemDivider(@NonNull Canvas c, @NonNull RecyclerView parent, int left, int right, View child, RecyclerView.LayoutParams params, int childLayoutPosition) {
        if (needTitleCallback.titleMap()!= null && !needTitleCallback.titleMap().keySet().contains(childLayoutPosition + 1)) {//如果下一个不需要title则开始准备画此条目的divider
            if (!needLastDivider) {//判断此列表最底部是否需要分割线
                RecyclerView.Adapter adapter = parent.getAdapter();
                if (adapter != null) {
                    if (childLayoutPosition + 1 != adapter.getItemCount()) {
                        int top = child.getBottom() + params.bottomMargin + Math.round(child.getTranslationY());
                        int bottom = top + Math.round(normalDividerHeight);
                        if (dividerDrawble != null){
                            dividerDrawble.setBounds(left, top, right, bottom);
                            dividerDrawble.draw(c);
                        }else {
                            c.drawRect(left,top,right,bottom,paintNormalDivider);
                        }
                    }
                } else {
                    int top = child.getBottom() + params.bottomMargin + Math.round(child.getTranslationY());
                    int bottom = top + Math.round(normalDividerHeight);
                    if (dividerDrawble != null){
                        dividerDrawble.setBounds(left, top, right, bottom);
                        dividerDrawble.draw(c);
                    }else {
                        c.drawRect(left,top,right,bottom,paintNormalDivider);
                    }
                }
            } else {//需要分割线
                int top = child.getBottom() + params.bottomMargin + Math.round(child.getTranslationY());
                int bottom = top + Math.round(normalDividerHeight);
                if (dividerDrawble != null){
                    dividerDrawble.setBounds(left, top, right, bottom);
                    dividerDrawble.draw(c);
                }else {
                    c.drawRect(left,top,right,bottom,paintNormalDivider);
                }
            }
        }
    }

    /**
     * 如果回调不为空，则画groupBar 内容
     * @param c
     * @param parent
     * @param gLeft
     * @param gRight
     * @param child
     * @param params
     * @param childLayoutPosition
     */
    private void drawGroupView(@NonNull Canvas c, @NonNull RecyclerView parent, int gLeft, int gRight, View child, RecyclerView.LayoutParams params, int childLayoutPosition) {
        if (needTitleCallback != null) {
            if (needTitleCallback.titleMap() != null && needTitleCallback.titleMap().keySet().contains(childLayoutPosition)) {
                int bottom = child.getTop() - params.topMargin - Math.round(child.getTranslationY());
                int top = bottom - Math.round(groupDividerHeight);
                int childPosition = parent.getChildAdapterPosition(child);
                //首先处理文字，以便画icon时 测量文字宽高
                String content = "";
                if (needTitleCallback.titleMap().keySet().contains(childPosition)) {
                    content = needTitleCallback.titleMap().get(childPosition);
                }
                textPaint.getTextBounds(content, 0, content.length(), text_rect);

                //画分组条
                if (groupDivider != null){//如果用户没有设置drawableRes，则用默认宽高
                    groupDivider.setBounds(gLeft,top,gRight,bottom);
                    groupDivider.draw(c);
                }else {
                    c.drawRect(gLeft, top, gRight, bottom, paintCeiling);
                }

                //计算图标位置
                int iconHeight = 0;
                int iconWidth = 0;
                float iconLeft = 0;
                Bitmap bitmap = null;
                if (needTitleCallback.iconMap() != null && needTitleCallback.iconMap().keySet().contains(childLayoutPosition)){
                    bitmap = scaleBitmap(BitmapFactory.decodeResource(context.getResources(), needTitleCallback.iconMap().get(childLayoutPosition)));
                    iconHeight = bitmap.getHeight();
                    iconWidth = bitmap.getWidth();
                }
                //根据用户传入的gravity 计算画icon的位置
                if (mGravity == Gravity.CENTER){
                    iconLeft = (parent.getWidth() - iconWidth - text_rect.width() - textMarginLeft - iconMarginRight)/2.0f;
                }else if (mGravity == Gravity.END||mGravity==Gravity.RIGHT){
                    iconLeft = parent.getWidth() - textMarginRight - textMarginLeft - text_rect.width() - iconWidth - iconMarginRight;
                }else {
                    iconLeft = iconMarginLeft;
                }
                //画图标
                if (bitmap != null){
                    c.drawBitmap(bitmap,iconLeft ,top + (groupDividerHeight - iconHeight)/2.0f,textPaint);
                }
                //画文字
                float bottomY = bottom - (groupDividerHeight - fontMetrics.descent + fontMetrics.ascent)/2 - fontMetrics.descent ;
                if (bitmap != null){
                    c.drawText(content, iconLeft + textMarginLeft + iconWidth + iconMarginRight, bottomY, textPaint);
                }else {
                    c.drawText(content, iconLeft, bottomY, textPaint);
                }
            }
        }
    }

    /**
     * 如果用户没有设置回调则 默认设置
     * @param c
     * @param parent
     * @param left
     * @param right
     * @param child
     * @param params
     * @param childLayoutPosition
     * @return
     */
    private boolean emptyCallback(@NonNull Canvas c, @NonNull RecyclerView parent, int left, int right, View child, RecyclerView.LayoutParams params, int childLayoutPosition) {
        if (needTitleCallback == null) {
            RecyclerView.Adapter adapter = parent.getAdapter();
            if (adapter != null) {
                if (childLayoutPosition + 1 != adapter.getItemCount()) {
                    int top = child.getBottom() + params.bottomMargin + Math.round(child.getTranslationY());
                    int bottom = top + Math.round(normalDividerHeight);
                    if (dividerDrawble != null){
                        dividerDrawble.setBounds(left, top, right, bottom);
                        dividerDrawble.draw(c);
                    }else {
                        c.drawRect(left,top,right,bottom,paintNormalDivider);
                    }
                }
            } else {
                int top = child.getBottom() + params.bottomMargin + Math.round(child.getTranslationY());
                int bottom = top + Math.round(normalDividerHeight);
                if (dividerDrawble != null){
                    dividerDrawble.setBounds(left, top, right, bottom);
                    dividerDrawble.draw(c);
                }else {
                    c.drawRect(left,top,right,bottom,paintNormalDivider);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,@NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int pos = parent.getChildLayoutPosition(view);
        RecyclerView.Adapter adapter = parent.getAdapter();
        if (needTitleCallback != null && needTitleCallback.titleMap()!= null && needTitleCallback.titleMap().keySet().contains(pos)) {
            outRect.top = Math.round(groupDividerHeight);
        }
        //如果回调为空，直接执行即可，并且默认列表最后一条没有分割线
        if (needTitleCallback == null) {
            if (adapter != null) {
                if (pos + 1 != adapter.getItemCount()) {//最后一条不要分割线
                    outRect.bottom = Math.round(normalDividerHeight);
                }
            } else {
                outRect.bottom = Math.round(normalDividerHeight);
            }
            return;
        }

        if (needTitleCallback.titleMap()!= null && !needTitleCallback.titleMap().keySet().contains(pos + 1)) {//判断下一个条目是否需要title
            // 如果不需要则需要在此条目下画分割线
            if (!needLastDivider) {//判断此列表最后一个条目是否需要展示分割线，如果不需要则不预留画分割线的空间
                if (adapter != null) {
                    if (pos + 1 != adapter.getItemCount()) {
                        outRect.bottom = Math.round(normalDividerHeight);
                    }
                } else {
                    outRect.bottom = Math.round(normalDividerHeight);
                }
            } else {
                outRect.bottom = Math.round(normalDividerHeight);
            }
        }
    }

    /**
     * 按比例缩放icon
     * @param bitmap
     * @return
     */
    private Bitmap scaleBitmap(Bitmap bitmap) {
        Matrix matrix=new Matrix();
        float scale;
        if (iconHeight > 0 && iconHeight < groupDividerHeight){
            scale = iconHeight/ (float) bitmap.getHeight();
        }else {
            scale = groupDividerHeight/ (float) bitmap.getHeight();
        }
        matrix.postScale(scale,scale);
        return Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,false);
    }

    /**
     * dp转px
     * @param dp
     * @return
     */
    private float dp2px(float dp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    /**
     * 回调 如果实现此回调，必须传titleMap ，不实现回调，就是默认的垂直分割线
     */
    public interface NeedTitleCallback {
        //分类名称
        HashMap<Integer, String> titleMap();
        //icon
        HashMap<Integer,Integer> iconMap();
    }
}