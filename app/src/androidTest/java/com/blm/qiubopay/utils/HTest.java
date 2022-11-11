package com.blm.qiubopay.utils;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.platform.app.InstrumentationRegistry;

import com.blm.qiubopay.R;
import com.blm.qiubopay.helpers.AppPreferences;
import com.blm.qiubopay.n3.DATA;
import com.reginald.editspinner.EditSpinner;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import mx.devapps.utils.components.HSpinner;
import mx.devapps.utils.components.otp.PinView;
import pl.droidsonroids.gif.GifImageView;

public final class HTest {

    private static Integer COUNT = 0;

    private static String TAG = "";

    //PUBLICO

    public static void start(Class<?> context) {
        timer(DATA.TIEMPO_INICIO);
        COUNT = 0;
        TAG = context.getSimpleName();
        logger( null, new HItem("START", 0, "VIEW"));
    }

    public static void finish() {
        HItem item = new HItem("FINISH", 0, "VIEW");
        logger( null, item);
        timer(DATA.TIEMPO_FIN);
    }

    public static void logout() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        AppPreferences.Logout(context);
        timer(DATA.TIEMPO_ACCION);
    }

    public static void setValueInput(HItem item) {
        timer(DATA.TIEMPO_ACCION);
        setValue(item);
    }

    public static void setValueInputDate(HItem item) {
        timer(DATA.TIEMPO_ACCION);
        setValueDate(item);
    }

    public static void setValuePin(HItem item) {
        timer(DATA.TIEMPO_ACCION);
        viewPin(item);
        if(!item.getCheck()) { error(item); }
    }

    public static void setValueSpinner(HItem item) {
        setValueInput(new HItem(item.getName(), "", item.getIndex()));
        timer(DATA.TIEMPO_ACCION);
        viewSpinner(item);
        if(!item.getCheck()) { error(item); }
        timer(DATA.TIEMPO_ACCION);
        onData(allOf(is(instanceOf(String.class)), is(item.getValue())))
                .perform(ViewActions.click());
    }

    public static void setValueESpinner(HItem item) {
        timer(DATA.TIEMPO_ACCION);
        viewEditSpinner(item);
        if(!item.getCheck()) { error(item); }
        timer(DATA.TIEMPO_ACCION);
        //onData(allOf(is(instanceOf(String.class)), is(item.getValue())))
        //        .perform(ViewActions.click());
    }



    public static void clickButton(HItem item) {
        timer(DATA.TIEMPO_ACCION);
        viewButton(item);
        if(!item.getCheck()) { error(item); }
    }

    public static void clickRadio(HItem item) {
        timer(DATA.TIEMPO_ACCION);
        viewRadio(item);
        if(!item.getCheck()) { error(item); }
    }

    public static void clickImage(HItem item) {
        timer(DATA.TIEMPO_ACCION);
        viewImage(item);
        if(!item.getCheck()) { error(item); }
    }

    public static void clickText(HItem item) {
        timer(DATA.TIEMPO_ACCION);
        viewText(item);
        if(!item.getCheck()) { error(item); }
    }

    public static void checkText(HItem item) {
        timer(DATA.TIEMPO_ACCION);
        checkTextView(item);
        if(!item.getCheck()) { error(item); }
    }

    public static void clickLinearLayout(HItem item) {
        timer(DATA.TIEMPO_ACCION);
        viewLinearLayout(item);
        if(!item.getCheck()) { error(item); }
    }

    public static void scrollRecycler(HItem item) {
        timer(DATA.TIEMPO_ACCION);
        scrollRecyclerView(item.getPosition());
    }

    public static void clickTextElement(HItem item) {
        timer(DATA.TIEMPO_ACCION);
        clickElement(item);
        if(!item.getCheck()) { error(item); }
    }

    public static void checkLoading() {
        try {
            timer(DATA.TIEMPO_ACCION);
            HItem item = new HItem("LOADING", 0, "GifImageView");
            item.setCheck(true);
            do {
                item.setCheck(false);
                viewImageGif(item);
            } while(item.getCheck());

        } catch (Exception ex) { }
    }

    public static void pressBack() {
        Espresso.pressBack();
    }

    //PRIVADO

    private static void setValue(HItem item) {
        viewEdit(item, false);
        if(!item.getCheck()) { error(item); }
    }

    private static void setValueDate(HItem item) {
        viewEdit(item, true);
        if(!item.getCheck()) { error(item); }
    }

    private static void viewEdit(HItem item, boolean date) {

        item.setTag("EditText");

        onView(withIndex(
                withClassName(containsString(EditText.class.getSimpleName())), item)
        ).check((view, noViewFoundException) -> {
            if ((view instanceof EditText)) {
                EditText edit = (EditText) view;
                String hint = edit.getHint().toString().toUpperCase();

                Log.d("withIndex", hint);
                Log.d("withIndex", item.getName().toUpperCase());
                Log.d("withIndex", hint.equals(item.getName().toUpperCase()) + "");

                if(hint.equals(item.getName().toUpperCase())) {
                    edit.getParent().requestChildFocus(edit,edit);
                    edit.requestFocus();
                    item.setCheck(true);
                    if(date) edit.setText(item.getValue());
                    logger( edit, item);
                }
            }
        });

        try {
            if(item.getCheck() && !date) {
                onView(withIndex(
                        withClassName(containsString(EditText.class.getSimpleName())), item)
                ).perform(typeText(item.getValue()));
                Espresso.closeSoftKeyboard();
            }
        } catch (Exception ex) { }

    }

    private static void viewSpinner(HItem item) {

        item.setTag("Spinner");

        onView(withIndex(
                withClassName(containsString(HSpinner.class.getSimpleName())), item)
        ).check((view, noViewFoundException) -> {
            if ((view instanceof HSpinner)) {
                HSpinner spinner = (HSpinner) view;
                spinner.getParent().requestChildFocus(spinner,spinner);
                spinner.requestFocus();
                item.setCheck(true);
                spinner.performClick();
                logger( spinner, item);
            }
        });
    }

    private static void viewEditSpinner(HItem item) {

        item.setTag("EditSpinner");

        onView(withIndex(
                withClassName(containsString(EditSpinner.class.getSimpleName())), item)
        ).check((view, noViewFoundException) -> {
            if ((view instanceof EditSpinner)) {
                EditSpinner spinner = (EditSpinner) view;
                spinner.getParent().requestChildFocus(spinner,spinner);
                spinner.requestFocus();
                item.setCheck(true);

                spinner.performClick();
                timer(1);
                spinner.selectItem(0);

                logger( spinner, item);
            }
        });
    }

    private static void viewPin(HItem item) {

        item.setTag("PinView");

        onView(withIndex(
                withClassName(containsString(PinView.class.getSimpleName())), item)
        ).check((view, noViewFoundException) -> {
            if ((view instanceof PinView)) {
                PinView pin = (PinView) view;
                pin.getParent().requestChildFocus(pin,pin);
                pin.requestFocus();
                item.setCheck(true);
                //pin.setText(item.getValue());
                logger( pin, item);
            }
        });

        try {
            if(item.getCheck()) {
                onView(withIndex(
                        withClassName(containsString(PinView.class.getSimpleName())), item)
                ).perform(typeText(item.getValue()));
                Espresso.closeSoftKeyboard();
            }
        } catch (Exception ex) { }

    }

    private static void viewButton(HItem item) {

        item.setTag("Button");

        onView(withIndex(
                withClassName(containsString(Button.class.getSimpleName())), item)
        ).check((view, noViewFoundException) -> {
            if ((view instanceof Button)) {
                Button button = (Button) view;
                String text = button.getText().toString().toUpperCase();
                if(text.equals(item.getName().toUpperCase())) {
                    button.getParent().requestChildFocus(button,button);
                    button.requestFocus();
                    item.setCheck(true);
                    button.performClick();
                    timer(2);
                    logger( button, item);
                }
            }
        });

    }

    private static void viewRadio(HItem item) {

        item.setTag("RadioButton");

        onView(withIndex(
                withClassName(containsString(RadioButton.class.getSimpleName())), item)
        ).check((view, noViewFoundException) -> {
            if ((view instanceof RadioButton)) {
                RadioButton radio = (RadioButton) view;
                String id = radio.getId() + "";
                if(id.equals(item.getName().toUpperCase())) {
                    radio.getParent().requestChildFocus(radio,radio);
                    radio.requestFocus();
                    item.setCheck(true);
                    radio.performClick();
                    logger( radio, item);
                }
            }
        });

    }

    private static void viewImage(HItem item) {

        item.setTag("ImageView");

        onView(withIndex(
                withClassName(containsString(ImageView.class.getSimpleName())), item)
        ).check((view, noViewFoundException) -> {
            if ((view instanceof ImageView)) {
                ImageView image = (ImageView) view;
                String id = image.getId() + "";
                if(id.equals(item.getName().toUpperCase())) {
                    image.getParent().requestChildFocus(image,image);
                    image.requestFocus();
                    item.setCheck(true);
                    image.performClick();
                    image.callOnClick();
                    logger( image, item);
                }
            }
        });

    }

    private static void viewText(HItem item) {

        item.setTag("TextView");

        onView(withIndex(
                withClassName(containsString(TextView.class.getSimpleName())), item)
        ).check((view, noViewFoundException) -> {
            if ((view instanceof TextView)) {
                TextView label = (TextView) view;
                String text = label.getText().toString().toUpperCase();
                if(text.equals(item.getName().toUpperCase())) {
                    label.getParent().requestChildFocus(label,label);
                    label.requestFocus();
                    item.setCheck(true);
                    label.performClick();
                    logger( label, item);
                }
            }
        });

    }

    private static void viewImageGif(HItem item) {

        item.setTag("GifImageView");

        onView(withIndex(
                withClassName(containsString(GifImageView.class.getSimpleName())), item)
        ).check((view, noViewFoundException) -> {
            if ((view instanceof GifImageView)) {
                GifImageView gif = (GifImageView) view;
                String id = gif.getId() + "";
                gif.getParent().requestChildFocus(gif,gif);
                gif.requestFocus();
                item.setCheck(true);
                gif.performClick();
                logger( gif, item);
            }
        });

    }

    private static void viewLinearLayout(HItem item) {

        item.setTag("LinearLayout");

        onView(withIndex(
                withClassName(containsString(LinearLayout.class.getSimpleName())), item)
        ).check((view, noViewFoundException) -> {
            if ((view instanceof LinearLayout)) {
                LinearLayout layout = (LinearLayout) view;
                String id = layout.getId() + "";
                if(id.equals(item.getName().toUpperCase())) {
                    layout.getParent().requestChildFocus(layout,layout);
                    layout.requestFocus();
                    item.setCheck(true);
                    layout.performClick();
                    logger( layout, item);
                }
            }
        });

    }

    private static void viewTextElement(HItem item) {

        item.setTag("TextView");

        onView(
                allOf(withClassName(containsString(View.class.getSimpleName())), withText(item.getName()))
        ).check((view, noViewFoundException) -> {

            if ((view instanceof TextView)) {
                TextView label = (TextView) view;
                String text = label.getText().toString().toUpperCase();

                Log.d("viewTextElement",  " inicio " + text);
                Log.d("viewTextElement",  item.getName().toUpperCase());
                Log.d("viewTextElement",  text.equals(item.getName().toUpperCase()) + "");

                if(text.equals(item.getName().toUpperCase())) {
                    label.getParent().requestChildFocus(label,label);
                    label.requestFocus();
                    item.setCheck(true);
                    label.performClick();
                    View cont = label;

                    for(int i=0; i<item.getIndex(); i++) {
                        cont = (View) cont.getParent();
                        cont.performClick();
                        Log.d("viewTextElement", "" + cont.getClass().getSimpleName());
                    }

                    if(item.getOption()) {
                        for(View v: getAllChildrenBFS(cont)) {
                            v.performClick();
                        }
                    }

                    logger( label, item);
                }
            }
        });

    }

    private static List<View> getAllChildrenBFS(View v) {
        List<View> visited = new ArrayList<View>();
        List<View> unvisited = new ArrayList<View>();
        unvisited.add(v);

        while (!unvisited.isEmpty()) {
            View child = unvisited.remove(0);
            visited.add(child);
            if (!(child instanceof ViewGroup)) continue;
            ViewGroup group = (ViewGroup) child;
            final int childCount = group.getChildCount();
            for (int i=0; i<childCount; i++) unvisited.add(group.getChildAt(i));
        }

        return visited;
    }

    private static Matcher<View> withIndex(final Matcher<View> matcher, final HItem item) {
        return new TypeSafeMatcher<View>() {
            int currentIndex = 0;

            @Override
            public void describeTo(Description description) {
                description.appendText("with index: ");
                description.appendValue(item.getIndex());
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                //Log.d("matchesSafely", view.getClass().getSimpleName() + " => " + currentIndex + " : " + view.getId());
                switch (item.getTag()) {

                    case "AlertDialog":
                        if ( view.getClass().getSimpleName().equals(item.getTag())) {
                            Log.d("withIndex", "AlertDialog => " + currentIndex + " : " + view.getId());
                        }
                        break;
                    case "TextView":
                        if ((view instanceof TextView)) {
                            TextView element = (TextView) view;
                            Log.d("withIndex", "TextView => " + currentIndex + " : " + element.getText());
                        }
                        break;
                    case "LinearLayout":
                        if ((view instanceof LinearLayout)) {
                            LinearLayout element = (LinearLayout) view;
                            Log.d("withIndex", "LinearLayout => " + currentIndex + " : " + element.getId());
                        }
                        break;
                    case "PinView":
                    case "EditText":
                        if ((view instanceof EditText)) {
                            EditText element = (EditText) view;
                            Log.d("withIndex", "EditText => " + currentIndex + " : " + element.getHint());
                        }
                        break;
                    case "Button":
                        if ((view instanceof Button)) {
                            Button element = (Button) view;
                            Log.d("withIndex", "Button => " + currentIndex + " : " + element.getText());
                        }
                        break;
                    case "ImageView":
                        if ((view instanceof ImageView)) {
                            ImageView element = (ImageView) view;
                            Log.d("withIndex", "ImageView => " + currentIndex + " : " +element.getId() + " : " + (element.getVisibility() == View.VISIBLE));
                        }
                        break;
                    case "RadioButton":
                        if ((view instanceof RadioButton)) {
                            RadioButton element = (RadioButton) view;
                            Log.d("withIndex", "RadioButton => " + currentIndex + " : " + element.getId());
                        }
                    case "CardView":
                        if ((view instanceof CardView)) {
                            CardView element = (CardView) view;
                            Log.d("withIndex", "CardView => " + currentIndex + " : " + element.getId());
                        }
                        break;
                }

                return matcher.matches(view) && currentIndex++ == item.getIndex();
            }
        };
    }

    public static void timer(int value) {
        try {
            TimeUnit.SECONDS.sleep(value);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void error(HItem item) {

        HItem error = new HItem("AlertDialogLayout");

        checkAlertDialog(error);

        if(error.getCheck()) {
            item.setText("\n- " + item.getTag() + " element not found - " + item.getName() + "\n - " +
                    error.getTag() + " - " + error.getText());
            item.setTag(error.getTag());
        } else {
            item.setText("\n- " + item.getTag() + " element not found - " + item.getName());
        }

        String[] tags = TAG.split("_");
        String message = (++COUNT + "_" + tags[2] + "_ERROR_"  + item.getTag() + "_" + item.getName()).toUpperCase().replaceAll(" ", "_");

        Log.d("HMatcher", TAG + " " + message);

        takeScreen(tags[1], message);

        fail(item.getText());

    }

    public static void launch(Activity context) {
        try {
            Intent intent = new Intent(context, context.getClass());
            context.startActivity(intent);
            context.finish();
        } catch (Exception ex) { }
    }

    private static void logger(View view, HItem item) {

        timer(DATA.TIEMPO_ACCION);

        String[] tags = TAG.split("_");

        String message = (++COUNT + "_" + tags[2] + "_" + item.getTag() + "_" + item.getName()).toUpperCase().replaceAll(" ", "_");

        Log.d("HMatcher", TAG + " " + message);

        takeScreen(tags[1], message);

    }

    private static void takeScreen(String folder, String file) {

        try {
            HScreenshot.capture(folder, TAG, file);
        }catch (Exception ex) { }

    }

    private static void clickElement(HItem item) {

        item.setTag("View");

        List<View> list = new ArrayList<>();

        onView((elements(
                withClassName(equalTo(View.class.getSimpleName())), list)))
                .check((view, noViewFoundException) -> { });

        for(View view : list) {

            Integer id = 0;
            String text = "";
            String type = view.getClass().getSimpleName().replace("AppCompat", "");

            switch (type) {
                case "TextView" :
                    TextView textView = (TextView) view;
                    text = textView.getText().toString();
                    break;
                default:
                    break;
            }

            Log.d("elements", type + " : " + id + " : " + text);

            if(item.getName().equalsIgnoreCase(text)) {

                id = view.getId();

                if(item.getOption()) {
                    viewTextElement(item);
                } else {
                    onView(allOf(withId(id), withText(item.getName())))
                            .check(matches(isDisplayed()))
                        .perform(click());

                    item.setCheck(true);
                    logger( view, item);
                }


                break;
            }

        }

    }

    private static void checkTextView(HItem item) {

        item.setTag("TextView");

        List<View> list = new ArrayList<>();

        onView((elements(
                withClassName(equalTo(TextView.class.getSimpleName())), list)))
                .check((view, noViewFoundException) -> { });

        for(View view : list) {

            String text = "";
            String type = view.getClass().getSimpleName().replace("AppCompat", "");

            switch (type) {
                case "TextView":
                    TextView textView = (TextView) view;
                    text = textView.getText().toString();
                    break;
            }

            if (item.getName().equalsIgnoreCase(text)) {
                item.setCheck(true);
                break;
            }
        }
    }

    private static void checkAlertDialog(HItem item) {

        item.setTag("AlertDialog");

        List<View> list = new ArrayList<>();

        onView((elements(
                withClassName(equalTo(AlertDialog.class.getSimpleName())), list)))
                .check((view, noViewFoundException) -> { });

        for(View view : list) {
            String type = view.getClass().getSimpleName().replace("AppCompat", "");
            if (item.getName().equalsIgnoreCase(type)) {
                TextView text = view.findViewById(R.id.text_message);
                item.setText(text.getText().toString());
                item.setCheck(true);
                break;
            }
        }
    }

    private static void scrollRecyclerView(Integer position) {

        List<View> list = new ArrayList<>();

        onView((elements(
                withClassName(equalTo(RecyclerView.class.getSimpleName())), list)))
                .check((view, noViewFoundException) -> { });

        for(View view : list) {

            String type = view.getClass().getSimpleName().replace("AppCompat", "");

            switch (type) {
                case "RecyclerView":
                    RecyclerView recycler = (RecyclerView) view;
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if(position > 0) {
                                recycler.scrollToPosition(Math.max((position - 1), 0));
                            } else {
                                recycler.scrollToPosition( (recycler.getAdapter().getItemCount() -1) );
                            }
                        }
                    });
                    break;
            }

        }
    }

    private static Matcher<View> elements(final Matcher<View> matcher, final List<View> list) {
        return new TypeSafeMatcher<View>() {
            int index = 0;

            @Override
            public void describeTo(Description description) {
                description.appendText("index");
                description.appendValue(index++);
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                view.setContentDescription("" + index);
                list.add(view);
                return false;
            }
        };
    }

}