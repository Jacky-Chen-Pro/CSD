package com.android.incongress.cd.conference.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;

import com.mobile.incongress.cd.conference.basic.csccm.R;

public class EmotionsUtils {

    private String[] mEmotions = new String[] { "[cool]", "[dx]", "[gz]", "[hx]", "[jy]", "[k]", "[lh]", "[ll]",
            "[love]", "[no]", "[ok]", "[sq]", "[tp]", "[wx]", "[xh]", "[y]", "[z]","[nnnn]","[nnnn]","[nnnn]","[nnnn]" };
    public static int[] emojo = new int[] { R.drawable.cool, R.drawable.dx, R.drawable.gz, R.drawable.hx,
            R.drawable.jy, R.drawable.k, R.drawable.lh, R.drawable.ll, R.drawable.love, R.drawable.no, R.drawable.ok,
            R.drawable.sq, R.drawable.tp, R.drawable.wx, R.drawable.xh, R.drawable.y, R.drawable.z,android.R.color.transparent,android.R.color.transparent,android.R.color.transparent,R.drawable.btn_delected };
    private final Pattern mPattern;
    private final HashMap<String, Integer> mEmoticonsMap;

    private static Context mContext;
    private static EmotionsUtils mInstance;
    private static Resources mResources;

    public EmotionsUtils() {
        mPattern = buildPattern();
        mEmoticonsMap = buildSmileyToRes();
    }

    public static void init(Context context) {
        mInstance = new EmotionsUtils();
        mContext = context;
        mResources = mContext.getResources();
    }

    public static EmotionsUtils getInstance() {
        return mInstance;
    }

    private HashMap<String, Integer> buildSmileyToRes() {
        HashMap<String, Integer> smileyToRes = new HashMap<String, Integer>();
        for (int i = 0; i < emojo.length; i++) {
            smileyToRes.put(mEmotions[i], emojo[i]);

        }
        return smileyToRes;
    }

    private Pattern buildPattern() {
        StringBuilder patternString = new StringBuilder();
        patternString.append('(');
        ArrayList<String> smileyTexts = new ArrayList<String>();
        for (String s : mEmotions) {
            smileyTexts.add(s);
        }
        Collections.sort(smileyTexts, new Comparator() {

            @Override
            public int compare(Object lhs, Object rhs) {
                return rhs.toString().length() - lhs.toString().length();
            }
        });
        for (String s : smileyTexts) {
            patternString.append(Pattern.quote(s));
            patternString.append('|');
        }
        patternString.replace(patternString.length() - 1, patternString.length(), ")");
        return Pattern.compile(patternString.toString());
    }

    public String getEmotionCode(int position) {
        String emotionText = null;
        if (position >= 0 && position < mEmotions.length) {
            emotionText = mEmotions[position];
        }
        return emotionText;
    }

    private CharSequence addSmileySpans(CharSequence text) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        Matcher matcher = mPattern.matcher(text);
        while (matcher.find()) {
            int resId = mEmoticonsMap.get(matcher.group());
            Drawable drawable = mResources.getDrawable(resId);
            int bound = mContext.getResources().getDimensionPixelOffset(R.dimen.emoticon_bound_size);
            drawable.setBounds(0, 0, bound, bound);
            builder.setSpan(new ImageSpan(drawable), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }
    public int matchSmileyLength(CharSequence text) {
        Matcher matcher = mPattern.matcher(text);
        int count=0;
        int length=0;
        while (matcher.find()) {
        	int size=matcher.group().length();
        	length=length+size;
        	count++;
        }
        return length;
    }
    
    public CharSequence formatMessage(String text) {
        SpannableStringBuilder buf = new SpannableStringBuilder();
        if (!TextUtils.isEmpty(text)) {
            buf.append(addSmileySpans(text));
        }
        return buf;
    }
    
    public int[] getLastEmoution(String text){
        int[] pos = new int[2];
        addSmileySpans(text);
        Matcher matcher = mPattern.matcher(text);
        boolean matchs = false;
        while (matcher.find()) {
            pos[0] = matcher.start();
            pos[1] = matcher.end();
            matchs = true;
        }
        if (matchs) {
            return pos;
        }
        return null;
        
        
    }

}
