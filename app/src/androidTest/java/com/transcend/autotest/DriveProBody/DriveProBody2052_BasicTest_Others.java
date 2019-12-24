package com.transcend.autotest.DriveProBody;

import android.app.Instrumentation;
import android.os.RemoteException;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiCollection;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DriveProBody2052_BasicTest_Others extends DriveProBodyAbstract{

    static int Phone = 0;
    static int Pad = 1;
    int Testing_device = Phone;

    private static int max_execute_time = 4;

    @Before
    public void setup() throws UiObjectNotFoundException {
        super.setup();
        instrumentation = getInstrumentation();
        mDevice = UiDevice.getInstance(instrumentation);
    }

    //TODO 1. home click
    @Test
    public void First_HomeClick() throws UiObjectNotFoundException {
        //進到home
        openDrawer();
        clickDrawerHome();

        UiCollection frame = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/main_frame_layout"));
        UiObject liveView = frame.getChildByText(new UiSelector().className("android.widget.TextView"), "即時影像");
        liveView.clickAndWaitForNewWindow();
        UiCollection webViewLayout = new UiCollection(new UiSelector().className("android.webkit.WebView"));
        Assert.assertEquals(1, webViewLayout.getChildCount(new UiSelector().className("android.widget.Image")));
        sleep(500);

        //進到home
        openDrawer();
        clickDrawerHome();
        frame = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/main_frame_layout"));
        UiObject browser = frame.getChildByText(new UiSelector().className("android.widget.TextView"), "DrivePro_BugList Body");
        browser.clickAndWaitForNewWindow();
        waitForDialog();
        Assert.assertEquals(true, getMainRecyclerView().exists());
    }

    //TODO 3. repeat switch
    @Test
    public void Third_RepeatSwitch() throws UiObjectNotFoundException {
        //進到home
        openDrawer();
        clickDrawerHome();

        for (int i = 0; i < max_execute_time; i++){
            First_HomeClick();
        }
    }
}
