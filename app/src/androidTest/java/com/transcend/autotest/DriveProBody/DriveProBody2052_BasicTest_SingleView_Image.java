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
public class DriveProBody2052_BasicTest_SingleView_Image extends DriveProBodyAbstract{

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

    //TODO 1. none
    @Test
    public void First_None() {
        //判斷畫面上的圖個數
        Assert.assertEquals(1, getImageInViewPagerCount());
    }

    //TODO 2. rotate
    @Test
    public void Second_Rotate() throws UiObjectNotFoundException, RemoteException {
        UiObject portraitTitleObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/toolbar_title"));
        String portrait_title = portraitTitleObj.getText();

        //檢查viewPager個數
        UiCollection viewpager = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/photo_view_pager"));
        Assert.assertEquals(1, viewpager.getChildCount(new UiSelector().className("android.widget.RelativeLayout")));

        mDevice.setOrientationLeft();

        UiObject landscapeTitleObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/toolbar_title"));
        String landscape_title = landscapeTitleObj.getText();
        //翻轉後檢查名稱是否相同
        Assert.assertEquals(portrait_title, landscape_title);

        //檢查viewPager個數
        viewpager = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/photo_view_pager"));
        Assert.assertEquals(1, viewpager.getChildCount(new UiSelector().className("android.widget.RelativeLayout")));

        mDevice.pressBack();
        waitForDialog();

        UiScrollable recyclerView = getMainRecyclerView();

        checkComposing(recyclerView, true);

        if (getItemCount(recyclerView) > 0) {
            UiObject item = getItem(recyclerView, 0);
            if (item.exists()) {
                item.click();
                waitForDialog();
            }
        }

        mDevice.setOrientationNatural();
        mDevice.unfreezeRotation();
    }

    //TODO 3. swipe
    @Test
    public void Third_Swipe() throws UiObjectNotFoundException {
        //滑動後判斷名稱
        UiObject portraitTitleObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/toolbar_title"));
        String portrait_title = portraitTitleObj.getText();
        swipeToNextViewPager();
        portraitTitleObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/toolbar_title"));
        Assert.assertEquals(false, portrait_title.equals(portraitTitleObj.getText()));

        //輕滑判斷名稱有無跑掉
        portrait_title = portraitTitleObj.getText();
        lightSwipeToNextViewPager();
        portraitTitleObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/toolbar_title"));
        Assert.assertEquals(true, portrait_title.equals(portraitTitleObj.getText()));

        //輕滑判斷名稱有無跑掉
        portrait_title = portraitTitleObj.getText();
        lightSwipeToNextViewPager();
        portraitTitleObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/toolbar_title"));
        Assert.assertEquals(true, portrait_title.equals(portraitTitleObj.getText()));

        //連續滑動
        for (int i = 0; i < max_execute_time; i++){
            swipeToNextViewPager();
        }

        UiScrollable recyclerView = getMainRecyclerView();
        //回到singleView
        if (getItemCount(recyclerView) > 0) {
            UiObject item = getItem(recyclerView, 0);
            if (item.exists()) {
                item.click();
                waitForDialog();
            }
        }
    }

    //TODO 4. delete
    @Test
    public void Third_Delete() throws UiObjectNotFoundException, RemoteException {
        //記錄名稱後刪除物件
        UiObject titleObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/toolbar_title"));
        while (!titleObj.exists()){
            UiObject imageObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/viewer_image"));
            imageObj.click();
            titleObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/toolbar_title"));
        }
        String deleteItemTitle = titleObj.getText();
        deleteItemInImageView();

        //檢查名稱，已刪除的物件不可能有一樣的名稱
        titleObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/toolbar_title"));
        Assert.assertEquals(false, deleteItemTitle.equals(titleObj.getText()));

        //檢查圖片是否存在
        UiObject imageObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/imageView"));
        Assert.assertEquals(true, imageObj.exists());

        mDevice.pressBack();
        waitForDialog();
        clickTabItem(2);

        //檢查排版
        checkComposing(getMainRecyclerView(), true);

        //滑至底部並點擊最後一個物件
        UiScrollable recyclerView = getMainRecyclerView();
        recyclerView.scrollToEnd(5, 10);
        UiObject lastItem = getItem(recyclerView, getItemCount(recyclerView) - 1);
        lastItem.click();
        waitForDialog();

        //記錄名稱後刪除物件
        titleObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/toolbar_title"));
        while (!titleObj.exists()){
            imageObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/imageView"));
            imageObj.click();
            titleObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/toolbar_title"));
        }
        deleteItemTitle = titleObj.getText();
        deleteItemInImageView();

        //檢查名稱，已刪除的物件不可能有一樣的名稱
        titleObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/toolbar_title"));
        Assert.assertEquals(false, deleteItemTitle.equals(titleObj.getText()));

        //檢查圖片是否存在
        imageObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/imageView"));
        Assert.assertEquals(true, imageObj.exists());

        mDevice.pressBack();
        waitForDialog();
        clickTabItem(2);

        //檢查排版
        checkComposing(getMainRecyclerView(), true);

        //回到singleView
        if (getItemCount(recyclerView) > 0) {
            UiObject item = getItem(recyclerView, 0);
            if (item.exists()) {
                item.click();
                waitForDialog();
            }
        }

        //連續刪除
        for (int i = 0; i < max_execute_time; i++){
            deleteItemInImageView();
        }
    }

    //TODO 7. download
    @Test
    public void Seventh_Download() throws UiObjectNotFoundException, RemoteException {
        //下載後檢查viewPager個數
        downloadItemInImageView();
        Assert.assertEquals(1, getImageInViewPagerCount());

        //翻轉後檢察viewPager個數
        mDevice.setOrientationLeft();
        sleep(500);
        Assert.assertEquals(1, getImageInViewPagerCount());

        //返回後點擊影片tab
        mDevice.pressBack();
        waitForDialog();
        clickTabItem(2);

        checkComposing(getMainRecyclerView(), true);

        if (getItemCount(getMainRecyclerView()) > 0) {
            UiObject item = getItem(getMainRecyclerView(), 0);
            item.clickAndWaitForNewWindow();
            sleep(500);
        }
    }

    //TODO 9. close
    @Test
    public void Ninth_Close() throws UiObjectNotFoundException {
        UiObject backObj = new UiObject(new UiSelector().className("android.widget.ImageButton"));
        backObj.click();
        Assert.assertEquals(true, getMainRecyclerView().exists());

        clickTabItem(2);

        UiScrollable recyclerView = getMainRecyclerView();
        //回到singleView
        if (getItemCount(recyclerView) > 0) {
            UiObject item = getItem(recyclerView, 0);
            if (item.exists()) {
                item.clickAndWaitForNewWindow();
            }
        }
    }
}
