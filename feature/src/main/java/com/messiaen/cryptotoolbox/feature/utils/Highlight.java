package com.messiaen.cryptotoolbox.feature.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.UnderlineSpan;
import android.text.style.UpdateAppearance;
import android.widget.TextView;

import com.messiaen.cryptotoolbox.feature.CryptoToolsApplication;

import java.lang.reflect.Method;

public class Highlight {

    public static void underlineString(TextView textView, String text, String string) {
        SpannableString spannableString = new SpannableString(text);

        ColoredUnderlineSpan[] underlineSpans =
                spannableString.getSpans(0, spannableString.length(), ColoredUnderlineSpan.class);

        for (ColoredUnderlineSpan underlineSpan : underlineSpans)
            spannableString.removeSpan(underlineSpan);

        if (string != null && !string.isEmpty()) {
            String lowerText = spannableString.toString().toLowerCase();
            String lowerKeyword = string.toLowerCase();
            int indexOfKeyword = lowerText.indexOf(lowerKeyword);

            while (indexOfKeyword != -1) {
                spannableString.setSpan(new ColoredUnderlineSpan(CryptoToolsApplication.COLOR_ACCENT_LIGHT),
                        indexOfKeyword, indexOfKeyword + lowerKeyword.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                indexOfKeyword = lowerText.indexOf(lowerKeyword,
                        indexOfKeyword + lowerKeyword.length());
            }

            textView.setText(spannableString);
        } else {
            textView.setText(text);
        }
    }

    final static class ColoredUnderlineSpan extends CharacterStyle implements UpdateAppearance {
        private final int color;

        public ColoredUnderlineSpan(int color) {
            this.color = color;
        }

        @Override
        public void updateDrawState(TextPaint tp) {
            try {
                final Method method = TextPaint.class.getMethod("setUnderlineText",
                        Integer.TYPE,
                        Float.TYPE);
                method.invoke(tp, color, 8.0f);
            } catch (final Exception e) {
                tp.setUnderlineText(true);
            }
        }
    }
}
