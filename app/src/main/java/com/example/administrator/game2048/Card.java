package com.example.administrator.game2048;

/**
 * Created by Administrator on 2017/04/27.
 *
 */

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Card extends FrameLayout {

    private int num = 0;

    private TextView textView = new TextView(getContext());

    public Card(Context context) {
        super(context);

        //设置文本居中显式
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(32);
        //表示布满整个父级容器
        LayoutParams lp = new LayoutParams(-1, -1);
        //设置每个card的margin
        lp.setMargins(10, 10, 0, 0);
        //利用LayoutParams 构造好方式之后，利用addView添加到我们的GridView中
        addView(textView, lp);
        setNum(0);
    }

    private void setBgColor() {
        switch (Integer.parseInt(textView.getText().toString().trim())){
            case 2:
                textView.setBackgroundColor(0xffeee4da);
                break;
            case 4:
                textView.setBackgroundColor(0xffede0c8);
                break;
            case 8:
                textView.setBackgroundColor(0xfff2b179);
                break;
            case 16:
                textView.setBackgroundColor(0xfff59563);
                break;
            case 32:
                textView.setBackgroundColor(0xfff67c5f);
                break;
            case 64:
                textView.setBackgroundColor(0xfff65e3b);
                break;
            case 128:
                textView.setBackgroundColor(0xffedcf72);
                break;
            case 256:
                textView.setBackgroundColor(0xffedcc61);
                break;
            case 512:
                textView.setBackgroundColor(0xffedc850);
                break;
            case 1024:
                textView.setBackgroundColor(0xffedc53f);
                break;
            case 2048:
                textView.setBackgroundColor(0xffedc22e);
                break;
            default:
                textView.setBackgroundColor(0xffeee4da);
                break;
        }
    }

    //判断这两张卡片绑定的数字是否相同
    public boolean equal(Card o) {
        return getNum() == o.getNum();
    }

    public int getNum() {
        return num;
    }

    //设置我们要显示的文字
    public void setNum(int num) {
        this.num = num;

        if (num <= 0) {
            textView.setText("");
            textView.setBackgroundColor(0xffeee4da);
        } else {
            //setText接收的int值为android的res文件中的id
            //String num_text = String.valueOf(num);
            textView.setText(num + "");
            setBgColor();
        }

    }
}
