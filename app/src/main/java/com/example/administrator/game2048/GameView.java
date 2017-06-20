package com.example.administrator.game2048;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/04/26.
 */

public class GameView extends GridLayout {

    private static int FLAG = 0;
    private Card[][] cardsMap = new Card[4][4];
    private List<Point> emptyPoint = new ArrayList<>();

    public GameView(Context context) {
        super(context);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initGameView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GameView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

//    private void getBestData() {
//        TextView textView = (TextView) findViewById(R.id.game_bestscore);
//        String bestScore = null;
//        bestScore = DataSaver.getDataSaver().readFile();
//        if (bestScore.equals("")) {
//            textView.append("0");
//        } else {
//            textView.append(bestScore);
//        }
//    }

    private void checkComplete() {
        boolean complete = true;
        ALL:
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (cardsMap[x][y].getNum() == 0 ||
                        (x > 0 && cardsMap[x][y].equal(cardsMap[x - 1][y])) ||
                        (x < 3 && cardsMap[x][y].equal(cardsMap[x + 1][y])) ||
                        (y > 0 && cardsMap[x][y].equal(cardsMap[x][y - 1])) ||
                        (y < 3 && cardsMap[x][y].equal(cardsMap[x][y + 1]))) {
                    complete = false;
                    break ALL;
                }
            }
        }
        if (complete) {
            new AlertDialog.Builder(getContext()).setTitle("Hello").setMessage("Game Over").setNegativeButton("restart", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startGame();
                }
                //必须调用.show（）方法，否则你的AlertDialog不会显示
            }).show();
        }
    }

    private void initGameView() {
        //指明GridLayout为4列,也可以在xml中指定
        setColumnCount(4);

        //0x表明该背景不透明 ff 应该表示十六进制
        setBackgroundColor(0xfff9f6f2);

        setOnTouchListener(new OnTouchListener() {

            private float startX, startY, offsetX, offsetY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    //按下屏幕
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    //离开屏幕
                    case MotionEvent.ACTION_UP:
                        offsetX = event.getX() - startX;
                        offsetY = event.getY() - startY;
                        //不能光靠判断左右偏移，当用户斜着滑动屏幕时，往哪个方向偏移的多
                        //就确定滑动方向为偏移量大的方向
                        //这里给定一个偏移量，当偏移超过5的时候，才有效
                        if (Math.abs(offsetX) > Math.abs(offsetY)) {
                            if (offsetX < -5) {
                                swipeLeft();
                            } else if (offsetX > 5) {
                                swipeRight();
                            }
                        } else {
                            if (offsetY < -5) {
                                swipeUp();
                            } else if (offsetY > 5) {
                                swipeDown();
                            }
                        }
                        break;

                }
                //如果返回false 只能监听到touchdown事件
                return true;
            }
        });
    }

    //当布局改变时，我们要去动态设置屏幕大小 我们在Manifest文件中指明了我们
    //这个Activity的方向是竖直的，这就使得屏幕不能横向转动，所以onSizeChanged
    //只会执行一次，就是在第一次被创建的时候
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //对宽高取最小值,是
        int cardWidth = (Math.min(w, h) - 10) / 4;
        addCards(cardWidth, cardWidth);
        //getBestData();
        startGame();
    }

    //开启游戏
    private void startGame() {
        //点击开始游戏时，需要先清空一下屏幕
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                cardsMap[x][y].setNum(0);
            }
        }
        //将得分设置为0 初始化得分
        MainActivity.getMainActivity().clearScore();
        //清空完屏幕之后，需要添加两个随机数
        addRandonNum();
        addRandonNum();
        //获取最高分数据
    }

    //添加一个随机数
    private void addRandonNum() {
        emptyPoint.clear();
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (cardsMap[x][y].getNum() <= 0) {
                    emptyPoint.add(new Point(x, y));
                }
            }
        }
        //Point p 是确定随机去移除一个空的点
        //然后为这个点添加一个随机数 2或者4 其出现比例为1:9
        Point p = emptyPoint.remove((int) (Math.random() * emptyPoint.size()));
        cardsMap[p.x][p.y].setNum(Math.random() > 0.1 ? 2 : 4);
    }

    private void addCards(int cardWidth, int cardHeight) {
        Card card;
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                card = new Card(getContext());
                card.setNum(0);
                addView(card, cardWidth, cardHeight);
                //将我们所添加的Card放到二位数组中
                cardsMap[x][y] = card;
            }
        }
    }

    private void swipeDown() {
        FLAG = 0;
        //Toast.makeText(getContext(), "down", Toast.LENGTH_SHORT).show();
        for (int x = 0; x < 4; x++) {
            for (int y = 3; y >= 0; y--) {
                //从左到右遍历
                for (int y1 = y - 1; y1 >= 0; y1--) {
                    if (cardsMap[x][y1].getNum() > 0) {
                        if (cardsMap[x][y].getNum() <= 0) {
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);
                            //如果不做x-- 会导致如果中间间隔一个 比如 2 0 2会变成2 2 不会合并
                            y++;

                        } else if (cardsMap[x][y].equal(cardsMap[x][y1])) {
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x][y1].setNum(0);
                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                        }
                        FLAG = 1;
                        break;
                    }
                }
            }
        }
        if (FLAG == 1) {
            addRandonNum();
            checkComplete();
        }
    }

    private void swipeUp() {
        FLAG = 0;
        // Toast.makeText(getContext(), "up", Toast.LENGTH_SHORT).show();
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                //从左到右遍历
                for (int y1 = y + 1; y1 < 4; y1++) {
                    if (cardsMap[x][y1].getNum() > 0) {
                        if (cardsMap[x][y].getNum() <= 0) {
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);
                            //如果不做x-- 会导致如果中间间隔一个 比如 2 0 2会变成2 2 不会合并
                            y--;

                        } else if (cardsMap[x][y].equal(cardsMap[x][y1])) {
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x][y1].setNum(0);
                            //只要有合并 合并完成后 数字是几 就加几分
                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                        }
                        FLAG = 1;
                        break;
                    }
                }
            }
        }
        if (FLAG == 1) {
            addRandonNum();
            checkComplete();
        }
    }

    private void swipeRight() {
        FLAG = 0;
        // Toast.makeText(getContext(), "right", Toast.LENGTH_SHORT).show();
        for (int y = 0; y < 4; y++) {
            for (int x = 3; x >= 0; x--) {
                //从左到右遍历
                for (int x1 = x - 1; x1 >= 0; x1--) {
                    if (cardsMap[x1][y].getNum() > 0) {
                        if (cardsMap[x][y].getNum() <= 0) {
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);
                            //如果不做x-- 会导致如果中间间隔一个 比如 2 0 2会变成2 2 不会合并
                            x++;
                        } else if (cardsMap[x][y].equal(cardsMap[x1][y])) {
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x1][y].setNum(0);
                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                        }
                        FLAG = 1;
                        break;
                    }
                }
            }
        }
        if (FLAG == 1) {
            addRandonNum();
            checkComplete();
        }
    }

    private void swipeLeft() {
        FLAG = 0;
        // Toast.makeText(getContext(), "left", Toast.LENGTH_SHORT).show();
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                //从左到右遍历
                for (int x1 = x + 1; x1 < 4; x1++) {
                    if (cardsMap[x1][y].getNum() > 0) {

                        if (cardsMap[x][y].getNum() <= 0) {
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);
                            //如果不做x-- 会导致如果中间间隔一个 比如 2 0 2会变成2 2 不会合并
                            x--;

                        } else if (cardsMap[x][y].equal(cardsMap[x1][y])) {
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x1][y].setNum(0);
                            MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            FLAG = 1;
                            break;
                        }
                    }
                }
            }
            if (FLAG == 1) {
                addRandonNum();
                checkComplete();
            }
        }
    }

}