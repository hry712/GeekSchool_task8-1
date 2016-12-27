package com.example.hery6.twentynums;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by hery6 on 2016/12/12/0012.
 */

public class MainActivity extends Activity {
    private TextView tv;                    // 用于生成每个填充到网格布局里的 TextView 控件
    private GridLayout RootLayout;          // 根布局
    private GridLayout.Spec rowSpec;        // 设置每个控件在网格布局中的行位置
    private GridLayout.Spec colSpec;        // 同上，设置列位置
    private int screen_width;               // 可用的屏幕宽度
    private int screen_height;              // 可用的屏幕高度
    private GridLayout.LayoutParams params; // 设置网格中每个控件的一些显示参数
    private final int TOP_LEFT = 0;
    private final int TOP_RIGHT = 1;
    private final int BOTTOM_LEFT = 2;
    private final int BOTTOM_RIGHT = 3;
    private final int TOP_SIDE = 4;
    private final int LEFT_SIDE = 5;
    private final int RIGHT_SIDE = 6;
    private final int BOTTOM_SIDE = 7;
    private int row_count;                  // 保存要分割的行数，默认设为 5行
    private int col_count;                  // 保存要分割的列数，默认设为 4行
    private int margin;                     // 调整各个控件间的外边距值
    private int view_width;                 // 各个 TextView 自身的宽度
    private int view_height;                // 各个 TextView 自身的高度

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化各种属性设置
        InitAttrs();

        RootLayout = new GridLayout(this);
        RootLayout.setColumnCount(col_count);
        RootLayout.setRowCount(row_count);
        setContentView(RootLayout);

        // 绘制四个拐角的控件
        DrawCorners(RootLayout, margin);
        // 绘制四条边的控件
        DrawSides(RootLayout, margin);
        // 绘制内部的所有控件
        DrawInner(RootLayout, margin);
    }

    private void InitAttrs() {
        InitScreenSizeValue();
        InitRowsCols(5, 4);
        InitWidgetMargin(10);
        InitViewSizeValue();
    }

    private void InitScreenSizeValue() {
        // 获取顶部通知栏的高度，用于计算当 Activity 没有铺满整个屏幕时的可用屏幕宽高值
        int notification_bar_height = Resources.getSystem().getDimensionPixelSize(
                Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android"));
        WindowManager wm = (WindowManager) this.getSystemService(WINDOW_SERVICE);
        screen_width = wm.getDefaultDisplay().getWidth();
        // 将整个屏幕的高度值减去顶部通知栏所占用的高度，得到最大可用屏幕高度
        screen_height = wm.getDefaultDisplay().getHeight() - notification_bar_height;
    }

    // 初始化控件的行列数
    private void InitRowsCols(int Rows, int Cols) {
        row_count = Rows;
        col_count = Cols;
    }

    // 初始化控件之间的外边距值
    private void InitWidgetMargin(int Margin) {
        margin = Margin;
    }

    // 初始化控件自身的宽高值
    private void InitViewSizeValue() {
        view_width = (screen_width - (col_count + 1)*margin) / col_count;
        view_height = (screen_height - (row_count + 1)*margin) /  row_count;
    }

    // 绘制四个拐角处的控件，为了保证这四个拐角处与其他控件和屏幕边缘的距离一致，
    // 需单独调整每个拐角控件两个方向上的外边距值
    private void DrawCorners(GridLayout RootLayout, int Margin) {
        TextView corner_view;

        // Draw the top-left corner
        corner_view = GenTextView("1");
        params = GenParams(TOP_LEFT, Margin);
        RootLayout.addView(corner_view, params);

        // Draw the top-right corner
        corner_view = GenTextView(col_count + "");
        params = GenParams(TOP_RIGHT, Margin);
        RootLayout.addView(corner_view, params);

        // Draw the bottom-left corner
        corner_view = GenTextView((row_count - 1)*col_count + 1 +"");
        params = GenParams(BOTTOM_LEFT, Margin);
        RootLayout.addView(corner_view, params);

        // Draw the bottom-right corner
        corner_view = GenTextView(row_count*col_count + "");
        params = GenParams(BOTTOM_RIGHT, Margin);
        RootLayout.addView(corner_view, params);
    }

    // 绘制边缘处的控件，这部分控件需要调整好挨在屏幕一边的外边距值
    private void DrawSides(GridLayout RootLayout, int Margin) {
        DrawTopSide(RootLayout, Margin);
        DrawRightSide(RootLayout, Margin);
        DrawBottomSide(RootLayout,  Margin);
        DrawLeftSide(RootLayout, Margin);
    }

    // 绘制内部的控件，这部分控件各个方向上的外边距都是一样的，不需要特别调整
    private void DrawInner(GridLayout RootLayout, int Margin) {
        TextView inner_view;

        for (int i = 1; i < row_count - 1; i++) {
            for (int j = 1; j < col_count -1; j++) {
                inner_view = GenTextView(i*4+j+1+"");
                rowSpec = GridLayout.spec(i);
                colSpec = GridLayout.spec(j);
                params = new GridLayout.LayoutParams(rowSpec, colSpec);
                params.setMargins(Margin/2, Margin/2, Margin/2, Margin/2);
                params.width = view_width;
                params.height = view_height;
                RootLayout.addView(inner_view, params);
            }
        }
    }

    // 绘制顶部去掉左右拐角的一行控件
    private void DrawTopSide(GridLayout RootLayout, int Margin) {
        TextView top_side_view;
        for (int i = 1; i < col_count - 1; i++) {
            top_side_view = GenTextView(i + 1 + "");
            params = GenParams(TOP_SIDE, Margin, i);
            RootLayout.addView(top_side_view, params);
        }
    }

    // 同上，绘制右边一列
    private void DrawRightSide(GridLayout RootLayout, int Margin) {
        TextView right_side_view;

        for (int i = 1; i < row_count - 1; i++) {
            right_side_view = GenTextView(4*(i+1) + "");
            params = GenParams(RIGHT_SIDE, Margin, i);
            RootLayout.addView(right_side_view, params);
        }
    }

    // 同上，绘制底部一行
    private void DrawBottomSide(GridLayout RootLayout, int Margin) {
        TextView bottom_side_view;
        int init_num = (row_count - 1) * col_count + 1;

        for (int i = 1; i < col_count - 1; i++) {
            bottom_side_view = GenTextView(init_num + i + "");
            params = GenParams(BOTTOM_SIDE, Margin, i);
            RootLayout.addView(bottom_side_view, params);
        }
    }

    // 同上，绘制左边一列
    private void DrawLeftSide(GridLayout RootLayout, int Margin) {
        TextView left_side_view;
        for (int i = 1; i < row_count -1 ; i++) {
            left_side_view = GenTextView(col_count * i + 1 +"");
            params = GenParams(LEFT_SIDE, Margin, i);
            RootLayout.addView(left_side_view, params);
        }
    }

    // 用于生成一个 TextView 控件，将一些基本属性的设置都放在这个方法中
    private TextView GenTextView(String Text) {
        tv = new TextView(this);
        tv.setText(Text);
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(Color.WHITE);
        tv.setBackgroundColor(Color.RED);
        return tv;
    }

    // 这是针对各个拐角处的控件调整其控件参数的方法
    private GridLayout.LayoutParams GenParams(int Direction,  int Margin) {
        switch (Direction) {
            // 左上角
            case TOP_LEFT:
                rowSpec = GridLayout.spec(0);
                colSpec = GridLayout.spec(0);
                params = new GridLayout.LayoutParams(rowSpec, colSpec);
                params.setMargins(Margin, Margin, Margin/2, Margin/2);
                params.width = view_width;
                params.height = view_height;
                break;
            // 右上角
            case TOP_RIGHT:
                rowSpec = GridLayout.spec(0);
                colSpec = GridLayout.spec(col_count - 1);
                params = new GridLayout.LayoutParams(rowSpec, colSpec);
                params.setMargins(Margin/2, Margin, Margin, Margin/2);
                params.width = view_width;
                params.height = view_height;
                break;
            // 左下角
            case BOTTOM_LEFT:
                rowSpec = GridLayout.spec(row_count - 1);
                colSpec = GridLayout.spec(0);
                params = new GridLayout.LayoutParams(rowSpec, colSpec);
                params.setMargins(Margin, Margin/2, Margin/2, Margin);
                params.width = view_width;
                params.height = view_height;
                break;
            // 右下角
            case BOTTOM_RIGHT:
                rowSpec = GridLayout.spec(row_count - 1);
                colSpec = GridLayout.spec(col_count - 1);
                params = new GridLayout.LayoutParams(rowSpec, colSpec);
                params.setMargins(Margin/2, Margin/2, Margin, Margin);
                params.width = view_width;
                params.height = view_height;
                break;
        }
        return params;
    }

    // 这是针对各个边缘处的控件调整其控件参数的方法
    private GridLayout.LayoutParams GenParams(int Direction, int Margin, int Order) {
        switch (Direction) {
            case TOP_SIDE:
                rowSpec = GridLayout.spec(0);
                colSpec = GridLayout.spec(Order);
                params = new GridLayout.LayoutParams(rowSpec, colSpec);
                params.setMargins(Margin/2, Margin, Margin/2, Margin/2);
                params.width = view_width;
                params.height = view_height;
                break;
            case RIGHT_SIDE:
                rowSpec = GridLayout.spec(Order);
                colSpec = GridLayout.spec(col_count - 1);
                params = new GridLayout.LayoutParams(rowSpec, colSpec);
                params.setMargins(Margin/2, Margin/2, Margin, Margin/2);
                params.width = view_width;
                params.height = view_height;
                break;
            case BOTTOM_SIDE:
                rowSpec = GridLayout.spec(row_count - 1);
                colSpec = GridLayout.spec(Order);
                params = new GridLayout.LayoutParams(rowSpec, colSpec);
                params.setMargins(Margin/2, Margin/2, Margin/2, Margin);
                params.width = view_width;
                params.height = view_height;
                break;
            case LEFT_SIDE:
                rowSpec = GridLayout.spec(Order);
                colSpec = GridLayout.spec(0);
                params = new GridLayout.LayoutParams(rowSpec, colSpec);
                params.setMargins(Margin, Margin/2, Margin/2, Margin/2);
                params.width = view_width;
                params.height = view_height;
                break;
        }
        return params;
    }
}
