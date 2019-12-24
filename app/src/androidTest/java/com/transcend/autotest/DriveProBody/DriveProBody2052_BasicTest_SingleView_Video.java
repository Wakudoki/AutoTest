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
import android.util.Log;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DriveProBody2052_BasicTest_SingleView_Video extends DriveProBodyAbstract{

    static int Phone = 0;
    static int Pad = 1;
    int Testing_device = Phone;

    private static int max_execute_time = 2;

    @Before
    public void setup() throws UiObjectNotFoundException {
        super.setup();
        instrumentation = getInstrumentation();
        mDevice = UiDevice.getInstance(instrumentation);
    }

    //TODO 1. none
    @Test
    public void First_None() throws UiObjectNotFoundException {
        //返回browser，記錄item名稱後點擊進入，再確認名稱是否與點擊前一致
        mDevice.pressBack();
        waitForDialog();
        UiScrollable recyclerView = getMainRecyclerView();
        UiObject item = getItem(recyclerView, 0);
        String itemTitle = getItemTitle(recyclerView, 0).getText();
        item.clickAndWaitForNewWindow();
        waitForDialog();
        UiObject singleViewTitleObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_toolbar_title"));
        String [] singleTitleSplit = singleViewTitleObj.getText().split("\n");
        Assert.assertEquals(true, singleTitleSplit[0].equals(itemTitle));

        checkVideoView();   //檢查video view的個數

        checkPlayable();    //檢查是否可以播放(判斷播放時間前後差別)

        checkVideoView();   //檢查video view的個數
    }

    //TODO 2. rotate
    @Test
    public void Second_Rotate() throws UiObjectNotFoundException, RemoteException {
        makeVideoControlVisible();
        UiObject portraitTitleObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_toolbar_title"));
        String portrait_title = portraitTitleObj.getText();

        mDevice.setOrientationLeft();

        checkVideoView();   //檢查video view的個數

        //翻轉後檢查名稱是否相同
        makeVideoControlVisible();
        UiObject landscapeTitleObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_toolbar_title"));
        Assert.assertEquals(true, portrait_title.equals(landscapeTitleObj.getText()));

        mDevice.pressBack();
        checkComposing(getMainRecyclerView(), true);    //檢查排版

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

    //TODO 3. swipe
    @Test
    public void Third_Swipe() {

    }

    //TODO 4. delete
    @Test
    public void Third_Delete() throws UiObjectNotFoundException, RemoteException {
        makeVideoControlVisible();  //確認控制bar存在
        String preTitle = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_toolbar_title")).getText();
        deleteItemInVideoView();   //刪除物件
        String postTitle = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_toolbar_title")).getText();
        Assert.assertEquals(false, preTitle.equals(postTitle));

        checkPlayable();    //檢查是否可以播放(判斷播放時間前後差別)

        //返回browser，滑至底部，從最後一個檔案開始選取
        mDevice.pressBack();
        waitForDialog();
        checkComposing(getMainRecyclerView(), true);   //檢查排版
        UiScrollable recyclerView = getMainRecyclerView();
        recyclerView.scrollToEnd(5);
        UiObject lastItem = getItem(recyclerView, getItemCount(recyclerView) - 1);
        lastItem.click();
        waitForDialog();
        makeVideoControlVisible();  //確認控制bar存在
        preTitle = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_toolbar_title")).getText();
        deleteItemInVideoView();   //刪除物件
        postTitle = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_toolbar_title")).getText();
        Assert.assertEquals(false, preTitle.equals(postTitle));

        checkPlayable();    //檢查是否可以播放(判斷播放時間前後差別)

        checkVideoView();   //檢查video view個數

        for (int i = 0; i < max_execute_time; i++){
            deleteItemInVideoView();
        }
    }

    //TODO 5. video play
    @Test
    public void Fifth_VideoPlay() throws UiObjectNotFoundException, RemoteException {
        makeVideoControlVisible();
        UiObject playObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_play"));
        playObj.click();

        for (int i = 0; i < max_execute_time; i++){
            mDevice.setOrientationLeft();
            sleep(500);
            checkVideoView();
            mDevice.setOrientationNatural();
            sleep(500);
            checkVideoView();
        }
        mDevice.unfreezeRotation();

        while(true){
            makeVideoControlVisible();
            UiObject currentTime = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_current_time"));
            UiObject duration = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_duration"));
            if (currentTime.getText().equals(duration.getText()))
                break;
        }

        checkVideoView();
    }

    //TODO 6. video play
    @Test
    public void Sixth_VideoNext() throws UiObjectNotFoundException {

        for (int i = 0; i < max_execute_time; i++){
            makeVideoControlVisible();  //確定控件存在
            UiObject nextObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_next"));
            nextObj.click();
            sleep(500);
        }

        for (int i = 0; i < max_execute_time; i++){
            makeVideoControlVisible();  //確定控件存在
            UiObject playObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_play"));
            playObj.click();
            sleep(1000);
            makeVideoControlVisible();
            UiObject nextObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_next"));
            nextObj.click();
            sleep(500);
        }
    }

    //TODO 7. download
    @Test
    public void Seventh_Download() throws UiObjectNotFoundException, RemoteException {
        downloadItemInVideoView();
        mDevice.setOrientationLeft();
        waitForDialog();
        downloadItemInVideoView();
        mDevice.setOrientationNatural();
        waitForDialog();
        mDevice.unfreezeRotation();
    }

    //TODO 9. close
    @Test
    public void ninth_Close() throws UiObjectNotFoundException, RemoteException {
        makeVideoControlVisible();
        UiObject closeObj = new UiObject(new UiSelector().className("android.widget.ImageButton"));
        closeObj.click();
        waitForDialog();

        UiScrollable recyclerView = getMainRecyclerView();
        checkComposing(recyclerView, true);
        UiObject item = getItem(recyclerView, 0);
        item.click();
        waitForDialog();
    }
}
