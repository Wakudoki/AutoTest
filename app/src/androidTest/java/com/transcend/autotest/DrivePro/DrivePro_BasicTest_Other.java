package com.transcend.autotest.DrivePro;

import android.support.test.uiautomator.UiCollection;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DrivePro_BasicTest_Other extends DriveProAbstract {

    @Before
    public void setup(){
        super.setup();
    }

    //TODO 1. home click
    @Test
    public void First_HomeClick() throws UiObjectNotFoundException {
        //進到home
        openDrawer();
        clickDrawerHome();

        UiObject liveView = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/liveViewPortal"));
        liveView.clickAndWaitForNewWindow();
        sleep(1000);
        UiCollection webViewLayout = new UiCollection(new UiSelector().className("android.webkit.WebView"));
        Assert.assertEquals(1, webViewLayout.getChildCount(new UiSelector().className("android.widget.Image")));
        sleep(500);

        //進到home
        openDrawer();
        clickDrawerHome();
        UiObject browser = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/browserPortal"));
        browser.clickAndWaitForNewWindow();
        waitForDialog();
        if (!getMainRecyclerView().exists())
            Assert.assertEquals(true, new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/empty_view")).exists());
    }

    //TODO 3. repeat switch
    @Test
    public void Third_RepeatSwitch() throws UiObjectNotFoundException {
        for (int i = 0; i < execute_times; i++){
            First_HomeClick();
        }
    }
}
