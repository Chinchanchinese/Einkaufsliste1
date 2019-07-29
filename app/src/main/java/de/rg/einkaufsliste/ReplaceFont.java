package de.rg.einkaufsliste;

import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;

public class ReplaceFont {

    public static void replaceDefaultFont(Context context, String nameofcurrentfont, String nameofnewfont){
        Typeface customfontTypeface = Typeface.createFromAsset(context.getAssets(),nameofnewfont);
        replacefont(nameofcurrentfont, customfontTypeface);
    }

    private static void replacefont(String nameofcurrentfont, Typeface customfontTypeface) {
        try {
            Field myField = Typeface.class.getDeclaredField(nameofcurrentfont);
            myField.setAccessible(true);
            myField.set(null, customfontTypeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
