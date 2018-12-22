package com.messiaen.cryptotoolbox.feature.utils;

import android.annotation.TargetApi;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.UpdateAppearance;
import android.widget.TextView;

import com.messiaen.cryptotoolbox.feature.CryptoToolsApplication;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Stream;

import androidx.annotation.NonNull;

public class Highlight {

    public static void underlineString(@NonNull final TextView textView, @NonNull final String text,
                                       final String... strings) {
        if (strings == null || strings.length == 0) {
            textView.setText(text);
            return;
        }

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N)
            performUnderlineString(textView, text,
                    Arrays.stream(strings).filter(s -> !s.isEmpty()).map(String::toUpperCase));
        else
            performUnderlineString(textView, text, strings);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static void performUnderlineString(@NonNull final TextView textView, @NonNull final String text,
                                               @NonNull final Stream<String> stringStream) {
        final SpannableString spannableString = new SpannableString(text);

        Arrays.stream(spannableString
                .getSpans(0, spannableString.length(), ColoredUnderlineSpan.class))
                .forEach(coloredUnderlineSpan -> spannableString.removeSpan(coloredUnderlineSpan));

        final String upperText = text.toUpperCase();
        stringStream.forEach(s -> {
            int indexOfKeyword = upperText.indexOf(s);
            while (indexOfKeyword != -1) {
                spannableString.setSpan(
                        new ColoredUnderlineSpan(CryptoToolsApplication.COLOR_ACCENT_LIGHT),
                        indexOfKeyword, indexOfKeyword + s.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                indexOfKeyword = upperText.indexOf(s, indexOfKeyword + s.length());
            }
        });

        textView.setText(spannableString);
    }

    private static void performUnderlineString(@NonNull final TextView textView,
                                               @NonNull final String text,
                                               @NonNull final String... strings) {
        final SpannableString spannableString = new SpannableString(text);

        final ColoredUnderlineSpan[] underlineSpans =
                spannableString.getSpans(0, spannableString.length(), ColoredUnderlineSpan.class);

        for (ColoredUnderlineSpan underlineSpan : underlineSpans)
            spannableString.removeSpan(underlineSpan);

        for (String string : strings) {
            if (string != null && !string.isEmpty()) {
                final String lowerText = spannableString.toString().toLowerCase();
                final String lowerKeyword = string.toLowerCase();
                int indexOfKeyword = lowerText.indexOf(lowerKeyword);

                while (indexOfKeyword != -1) {
                    spannableString.setSpan(
                            new ColoredUnderlineSpan(CryptoToolsApplication.COLOR_ACCENT_LIGHT),
                            indexOfKeyword, indexOfKeyword + lowerKeyword.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    indexOfKeyword = lowerText.indexOf(lowerKeyword,
                            indexOfKeyword + lowerKeyword.length());
                }
            }
        }

        textView.setText(spannableString);
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
