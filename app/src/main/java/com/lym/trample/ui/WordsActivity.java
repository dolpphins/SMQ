package com.lym.trample.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.lym.trample.R;
import com.lym.trample.base.BaseActivity;
import com.lym.trample.bean.Square;
import com.lym.trample.conf.WordsKeeper;
import com.lym.trample.utils.TextUtil;
import com.lym.trample.widget.DropSurfaceView;
import com.lym.trample.widget.DropViewConfiguration;
import com.lym.trample.word.generator.BaseWordGenerator;
import com.lym.trample.word.generator.IWordGenerator;
import com.lym.trample.word.generator.impl.RandomWordGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mao on 2015/11/5.
 */
public class WordsActivity extends BaseActivity implements DropSurfaceView.OnDrawSurfaceViewListener,
                                                    DropSurfaceView.OnSurfaceViewTouchListener, DropSurfaceView.OnGameOverListener{

    private final static String TAG = "WordsActivity";

    private DropSurfaceView words_drop_main_surfaceview;
    private DropViewConfiguration config;
    private Paint paint = new Paint();

    private BaseWordGenerator mWordsGenerator;

    private TextView words_drop_main_word;
    private TextView words_drop_main_scores;
    private int mScores;

    private List<String> mWordsList;
    private String currentWord;
    private List<String> mWordCharList;
    private int currentLetterIndex;//当前应该点击的字母索引
    private List<String> randomCharList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_words_main_activity);

        words_drop_main_surfaceview = (DropSurfaceView) findViewById(R.id.words_drop_main_surfaceview);
        words_drop_main_word = (TextView) findViewById(R.id.words_drop_main_word);
        words_drop_main_scores = (TextView) findViewById(R.id.words_drop_main_scores);

        words_drop_main_scores.setText(mScores + "");
        config = new DropViewConfiguration.Builder(this)
                .setPipeBorderColor(Color.parseColor("#0000ff"))
                .setPipeCount(4)
                .setCanvasColor(Color.WHITE)
                .build();
        words_drop_main_surfaceview.setConfiguration(config);
        words_drop_main_surfaceview.setOnDrawSurfaceViewListener(this);
        words_drop_main_surfaceview.setOnSurfaceViewTouchListener(this);
        words_drop_main_surfaceview.setOnGameOverListener(this);

        words_drop_main_surfaceview.setSpeed(18 * config.getRect().height() / 1920);

        //测试
        String name = "animal";
        mWordsList = WordsKeeper.getWordsList(name);
        mWordsGenerator = new RandomWordGenerator(mWordsList);
    }

    @Override
    public void onDrawSurfaceViewSquareItem(Canvas canvas, Square square, boolean started) {
        Log.i(TAG, "onDrawSurfaceViewSquareItem");
        if(mWordCharList != null) {
            Log.i(TAG, "current word:" + currentWord + ";current letter:" + mWordCharList.get(currentLetterIndex));
        }
        paint.reset();
        if(started) {
            paint.setColor(Color.BLACK);
            canvas.drawRect(square.toRect(), paint);

            Rect rect = TextUtil.getFillRectForText(square.toRect());
            paint.setColor(Color.WHITE);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(rect.width());
            Paint.FontMetrics metrics = paint.getFontMetrics();
            //注意metrics的所有值都是以基线(0)为参照
            float baseline = (rect.top + rect.bottom -  metrics.ascent) / 2;
            canvas.drawText("GO", (rect.left + rect.right) / 2, baseline, paint);
        } else {
            IWordGenerator.WordMapEntry entry = castToWordMapEntryFromObject(square.getBundle());
            if(entry == null) {
                entry = new IWordGenerator.WordMapEntry();
                entry.setLetter(mWordsGenerator.generate(randomCharList));
                square.setBundle(entry);
            } else if(entry.isPressed()) {//已经按过了设置为不可见
                return;
            }
            String letter = entry.getLetter();

            paint.setColor(Color.BLACK);
            canvas.drawRect(square.toRect(), paint);

            Rect rect = TextUtil.getFillRectForText(square.toRect());
            paint.setColor(Color.WHITE);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setTextSize(rect.width());
            Paint.FontMetrics metrics = paint.getFontMetrics();
            //注意metrics的所有值都是以基线(0)为参照
            float baseline = (rect.top + rect.bottom -  metrics.ascent) / 2;
            canvas.drawText(letter, (rect.left + rect.right) / 2, baseline, paint);
        }
    }

    @Override
    public boolean onSurfaceViewTouchOutsideDown(MotionEvent event, Square square) {

        IWordGenerator.WordMapEntry entry = new IWordGenerator.WordMapEntry();
        entry.setLetter("");
        square.setBundle(entry);
        words_drop_main_surfaceview.stop(square, DropSurfaceView.OnGameOverListener.GAME_OVER_OUT_SQUARE_TYPE);

        return true;
    }

    @Override
    public boolean onSurfaceViewTouchSquareDown(MotionEvent event, Square square) {
        IWordGenerator.WordMapEntry entry = castToWordMapEntryFromObject(square.getBundle());
        //开始游戏
        if(entry == null) {
            mScores = 0;
            words_drop_main_scores.setText(mScores + "");
            currentWord = mWordsGenerator.generate();
            words_drop_main_word.setText(currentWord);
            mWordCharList = string2CharList(currentWord);
            currentLetterIndex = 0;//设置当前应该点击的字母为单词的第一个字母
            randomCharList = generateRandomCharList(mWordCharList, currentLetterIndex);
            words_drop_main_surfaceview.start();
        } else {
            if(mWordCharList.get(currentLetterIndex).equals(entry.getLetter())) {
                entry.setIsPressed(true);//设置为已按状态
                //该单词已完成,更换为新的单词
                if(currentLetterIndex >= mWordCharList.size() - 1) {
                    currentWord = mWordsGenerator.generate();
                    words_drop_main_word.setText(currentWord);
                    mWordCharList = string2CharList(currentWord);
                    currentLetterIndex = 0;//设置当前应该点击的字母为单词的第一个字母
                    //计算分数
                    mScores++;
                    words_drop_main_scores.setText(mScores + "");
                } else {
                    currentLetterIndex++;
                }
                randomCharList = generateRandomCharList(mWordCharList, currentLetterIndex);
            } else {
                //失败
                words_drop_main_surfaceview.stop(square, DropSurfaceView.OnGameOverListener.GAME_OVER_SQUARE_ERROR_TYPE);
            }
        }
        return true;
    }

    @Override
    public boolean onIsGameOver(List<Square> squareList) {
        for(Square square : squareList) {
            if (square.getStartY() > config.getRect().bottom) {
                IWordGenerator.WordMapEntry entry = castToWordMapEntryFromObject(square.getBundle());
                if(mWordCharList.get(currentLetterIndex).equals(entry.getLetter())) {
                    words_drop_main_surfaceview.setGameOverRect(square);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onHandleGameOver(Square square, int type) {

    }

    private IWordGenerator.WordMapEntry castToWordMapEntryFromObject(Object obj) {
        if(obj == null) {
            return null;
        }
        try {
            return (IWordGenerator.WordMapEntry) obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<String> string2CharList(String s) {
        if(TextUtils.isEmpty(s)) {
            return null;
        }
        List<String> stringList = new ArrayList<String>();
        char[] arr = s.toCharArray();
        for(char c : arr) {
            stringList.add(c + "");
        }
        return stringList;
    }

    //获取随机字符集合
    private List<String> generateRandomCharList(List<String> charList, int currentIndex) {
        if(charList == null) {
            return null;
        }
        int size = charList.size();
        if(currentIndex < 0 || currentIndex >= size) {
            return null;
        }
        List<String> list = new ArrayList<String>();
        list.add(charList.get(currentIndex));
        list.add(charList.get(currentIndex));//增大当前字母概率
        if(currentIndex - 1 >= 0) {
            list.add(charList.get(currentIndex - 1));
        }
        if(currentIndex + 1 < charList.size()) {
            list.add(charList.get(currentIndex + 1));
        }
        return list;
    }
}
