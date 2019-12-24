package com.transcend.autotest.DrivePro;

import android.os.RemoteException;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DrivePro_BasicTest_SingleView_Video extends DriveProAbstract {

    @Before
    public void setup(){
        super.setup();
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
        UiObject singleViewTitleObj = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/toolbar_title"));
        String [] singleTitleSplit = singleViewTitleObj.getText().split("\n");
        Assert.assertEquals(true, singleTitleSplit[0].equals(itemTitle));

        UiObject playerView = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/video_surface"));
        if (!playerView.exists()){
            UiObject imgPlay = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/play_img"));
            Assert.assertEquals(true, imgPlay.exists());
        }

        makeVideoControlVisible();  //確認控制bar存在
        UiObject durationTimeObj = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/video_total_time"));
        Assert.assertEquals(false, durationTimeObj.getText().equals("00:00:00"));
    }

    //TODO 2. rotate
    @Test
    public void Second_Rotate() throws UiObjectNotFoundException, RemoteException {
        makeVideoControlVisible();
        UiObject portraitTitleObj = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/toolbar_title"));
        String portrait_title = portraitTitleObj.getText();

        mDevice.setOrientationLeft();

        UiObject playerView = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/video_surface"));
        if (!playerView.exists()){
            UiObject imgPlay = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/play_img"));
            Assert.assertEquals(true, imgPlay.exists());
        }

        //翻轉後檢查名稱是否相同
        makeVideoControlVisible();
        UiObject landscapeTitleObj = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/toolbar_title"));
        Assert.assertEquals(true, portrait_title.equals(landscapeTitleObj.getText()));

        mDevice.setOrientationNatural();
        mDevice.unfreezeRotation();
    }

    //TODO 4. delete
    @Test
    public void Third_Delete() throws UiObjectNotFoundException, RemoteException {
        makeVideoControlVisible();  //確認控制bar存在
        String preTitle = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/toolbar_title")).getText();
        boolean stillAtView = deleteItemInImageView();  //試著刪除檔案，若回傳false則表示刪到沒有檔案而回到瀏覽頁面；true表示刪除成功
        if (!stillAtView)
            return;
        String postTitle = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/toolbar_title")).getText();
        Assert.assertEquals(false, preTitle.equals(postTitle));

        //返回browser，滑至底部，從最後一個檔案開始選取
        mDevice.pressBack();
        waitForDialog();

        UiScrollable recyclerView = getMainRecyclerView();
        recyclerView.scrollToEnd(5);
        UiObject lastItem = getItem(recyclerView, getItemCount(recyclerView) - 1);
        lastItem.click();
        waitForDialog();
        makeVideoControlVisible();  //確認控制bar存在
        preTitle = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/toolbar_title")).getText();
        stillAtView = deleteItemInImageView();  //試著刪除檔案，若回傳false則表示刪到沒有檔案而回到瀏覽頁面；true表示刪除成功
        if (!stillAtView)
            return;
        postTitle = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/toolbar_title")).getText();
        Assert.assertEquals(false, preTitle.equals(postTitle));

        for (int i = 0; i < execute_times; i++){
            stillAtView = deleteItemInImageView();  //試著刪除檔案，若回傳false則表示刪到沒有檔案而回到瀏覽頁面；true表示刪除成功
            if (!stillAtView)
                return;
        }
    }

    //TODO 5. video play
    @Test
    public void Fifth_VideoPlay() throws UiObjectNotFoundException, RemoteException {

        for (int i = 0; i < execute_times; i++){
            mDevice.setOrientationLeft();
            sleep(500);
            mDevice.setOrientationNatural();
            sleep(500);
        }
        mDevice.unfreezeRotation();

        makeVideoControlVisible();
        while(true){
            UiObject currentTime = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/video_current_time"));
            UiObject duration = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/video_total_time"));
            if (currentTime.getText().equals(duration.getText()))
                break;
        }
    }

    //TODO 6. video next
    @Test
    public void Sixth_VideoNext() throws UiObjectNotFoundException {

        for (int i = 0; i < execute_times; i++){
            makeVideoControlVisible();  //確定控件存在
            UiObject nextObj = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/vlc_button_next"));
            nextObj.click();
            sleep(500);
        }

        for (int i = 0; i < execute_times; i++){makeVideoControlVisible();  //確定控件存在
            UiObject playObj = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/vlc_button_play_pause"));
            playObj.click();
            makeVideoControlVisible();
            UiObject nextObj = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/vlc_button_next"));
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
    public void ninth_Close() throws UiObjectNotFoundException {
        UiObject closeObj = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/toggle_btn"));
        closeObj.click();
        waitForDialog();

        UiScrollable recyclerView = getMainRecyclerView();
        UiObject item = getItem(recyclerView, 0);
        item.click();
        waitForDialog();
    }
}
