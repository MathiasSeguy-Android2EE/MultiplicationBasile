package com.android2ee.basile.multiplication;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.android2ee.basile.multiplication.view.AssessmentActivity;
import com.android2ee.basile.multiplication.view.MainActivity;
import com.android2ee.basile.multiplication.view.records.RecordsActivity;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.android2ee.basile.multiplication.utils.InstrumentedTestUtils.isExpectedActivity;

/**
 * Created by Created by Mathias Seguy alias Android2ee on 19/11/2017.
 */

@RunWith(AndroidJUnit4.class)
public class MainActivityUITest {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<MainActivity>(MainActivity.class, true, true);

    @Test
    public void checkSuccess_LaunchAssesmentActivity() throws InterruptedException {
        //Select the table five
        onView(withId(R.id.btn5))
                .perform(click());
        //and press start
        onView(withId(R.id.btnStart))
                .perform(click());

        Assert.assertTrue(isExpectedActivity(AssessmentActivity.class));
        Assert.assertEquals(false,MyApplication.ins().isAssesOnlyThisMultiplicationTable());
    }

    @Test
    public void checkSuccess_LaunchAssesmentActivitySingle() throws InterruptedException {
        //Select the table five
        onView(withId(R.id.btn5))
                .perform(click());
        //and check the not only table
        onView(withId(R.id.swtThisTableOnly))
                .perform(click());
        //and press start
        onView(withId(R.id.btnStart))
                .perform(click());

        Assert.assertTrue(isExpectedActivity(AssessmentActivity.class));
        Assert.assertEquals(true,MyApplication.ins().isAssesOnlyThisMultiplicationTable());
    }

    @Test
    public void checkSuccess_LaunchRecordsActivity() throws InterruptedException {
        //Select the menu item that launch the records
        onView(withId(R.id.action_winner))
                .perform(click());

        Assert.assertTrue(isExpectedActivity(RecordsActivity.class));
    }


}
