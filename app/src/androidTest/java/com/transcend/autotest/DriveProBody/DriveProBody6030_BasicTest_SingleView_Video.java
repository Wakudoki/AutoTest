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
public class DriveProBody6030_BasicTest_SingleView_Video {

    private UiDevice mDevice;
    private Instrumentation instrumentation;

    static int Phone = 0;
    static int Pad = 1;
    int Testing_device = Phone;

    private static int max_execute_time = 4;

    @Before
    public void setup() {
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

        makeVideoControlVisible();  //確認控制bar存在
        UiObject durationTimeObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_duration"));
        Assert.assertEquals(false, durationTimeObj.getText().equals("00:00:00"));
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
        deleteItemInSingleView();   //刪除物件
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
        deleteItemInSingleView();   //刪除物件
        postTitle = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_toolbar_title")).getText();
        Assert.assertEquals(false, preTitle.equals(postTitle));

        checkPlayable();    //檢查是否可以播放(判斷播放時間前後差別)

        checkVideoView();   //檢查video view個數

        for (int i = 0; i < max_execute_time; i++){
            deleteItemInSingleView();
        }
    }

    //TODO 5. video play
    @Test
    public void Fifth_VideoPlay() throws UiObjectNotFoundException, RemoteException {
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

        makeVideoControlVisible();
        while(true){
            UiObject currentTime = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_current_time"));
            UiObject duration = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_duration"));
            if (currentTime.getText().equals(duration.getText()))
                break;
        }

        checkVideoView();
    }

    //TODO 6. video next
    @Test
    public void Sixth_VideoNext() throws UiObjectNotFoundException {

        for (int i = 0; i < max_execute_time; i++){
            makeVideoControlVisible();  //確定控件存在
            UiObject nextObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_next"));
            nextObj.click();
            sleep(500);
        }

        for (int i = 0; i < max_execute_time; i++){makeVideoControlVisible();  //確定控件存在
            UiObject playObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_play"));
            playObj.click();
            makeVideoControlVisible();
            UiObject nextObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_next"));
            nextObj.click();
            sleep(500);
        }
    }

    //TODO 7. download
    @Test
    public void Seventh_Download() throws UiObjectNotFoundException, RemoteException {
        downloadItemInSingleView();
        mDevice.setOrientationLeft();
        waitForDialog();
        downloadItemInSingleView();
        mDevice.setOrientationNatural();
        waitForDialog();
        mDevice.unfreezeRotation();
    }

    //TODO 9. close
    @Test
    public void ninth_Close() throws UiObjectNotFoundException, RemoteException {
        UiObject closeObj = new UiObject(new UiSelector().className("android.widget.ImageButton"));
        closeObj.click();
        waitForDialog();

        UiScrollable recyclerView = getMainRecyclerView();
        checkComposing(recyclerView, true);
        UiObject item = getItem(recyclerView, 0);
        item.click();
        waitForDialog();
    }

    //TODO 取得物件
    public UiScrollable getMainRecyclerView(){
        return new UiScrollable(new UiSelector().resourceId("com.transcend.bcr:id/recycler_view"));
    }

    public int getItemCount(UiScrollable recycler){
        return recycler.getChildCount(new UiSelector().resourceId("com.transcend.bcr:id/item_manage"));
    }

    public UiObject getItem(UiScrollable recycler, int instance) throws UiObjectNotFoundException {
        return recycler.getChildByInstance(new UiSelector().resourceId("com.transcend.bcr:id/item_manage"), instance);
    }

    public UiObject getItemTitle(UiScrollable recycler, int instance) throws UiObjectNotFoundException {
        return recycler.getChildByInstance(new UiSelector().resourceId("com.transcend.bcr:id/item_title"), instance);
    }

    //TODO action
    public void swipeToNextViewPager(){
        mDevice.swipe(mDevice.getDisplayWidth()*2/3, mDevice.getDisplayHeight() / 2,
                0, mDevice.getDisplayHeight() / 2, 10);
        sleep(500);
    }

    public void swipeToPrevViewPager(){
        mDevice.swipe(0, mDevice.getDisplayHeight() / 2,
                mDevice.getDisplayWidth()*2/3, mDevice.getDisplayHeight() / 2, 10);
        sleep(500);
    }

    public void sleep(int mills){
        try {
            Thread.sleep(mills);
        }catch(Exception e){

        }
    }

    public void waitForDialog(){
        UiObject progress = new UiObject(new UiSelector().resourceId("android:id/progress"));
        while(progress.exists()){
            progress = new UiObject(new UiSelector().resourceId("android:id/progress"));
        }
    }

    public void openDrawer() throws UiObjectNotFoundException {
        UiObject drawer = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/toggle_btn"));
        drawer.clickAndWaitForNewWindow();
    }

    public void swipeDrawerLandscape(){
        mDevice.swipe(0, mDevice.getDisplayHeight() / 2,
                mDevice.getDisplayWidth(), mDevice.getDisplayHeight() / 2, 10);
        sleep(1000);
    }

    public void clickDrawerHome() throws UiObjectNotFoundException {
        UiCollection drawer = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/design_navigation_view"));
        UiObject home = drawer.getChildByInstance(new UiSelector().className("android.support.v7.widget.LinearLayoutCompat"), 0);
        home.clickAndWaitForNewWindow();
        waitForDialog();
    }

    public void clickDrawerLiveView() throws UiObjectNotFoundException {
        UiCollection drawer = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/design_navigation_view"));
        UiObject liveView = drawer.getChildByInstance(new UiSelector().className("android.support.v7.widget.LinearLayoutCompat"), 1);
        liveView.clickAndWaitForNewWindow();
        waitForDialog();
    }

    public void clickDrawerBrowser() throws UiObjectNotFoundException {
        UiCollection drawer = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/design_navigation_view"));
        UiObject browser = drawer.getChildByInstance(new UiSelector().className("android.support.v7.widget.LinearLayoutCompat"), 2);
        browser.clickAndWaitForNewWindow();
        waitForDialog();
    }

    public void clickDrawerMyStorage() throws UiObjectNotFoundException {
        UiCollection drawer = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/design_navigation_view"));
        UiObject myStorage = drawer.getChildByInstance(new UiSelector().className("android.support.v7.widget.LinearLayoutCompat"), 3);
        myStorage.clickAndWaitForNewWindow();
        waitForDialog();
    }

    public void clickDrawerSetting() throws UiObjectNotFoundException {
        UiCollection drawer = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/design_navigation_view"));
        UiObject setting = drawer.getChildByInstance(new UiSelector().className("android.support.v7.widget.LinearLayoutCompat"), 4);
        setting.clickAndWaitForNewWindow();
        waitForDialog();
    }

    public void clickBackInSingleView() throws UiObjectNotFoundException {
        //UiCollection toolbar = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/media_player_toolbar"));
        //UiObject back = toolbar.getChildByInstance(new UiSelector().className("android.widget.ImageButton"), 0);
        UiObject back = new UiObject(new UiSelector().className("android.widget.ImageButton"));
        back.clickAndWaitForNewWindow();
    }

    public void deleteItemInSingleView() throws UiObjectNotFoundException {
        UiObject delete = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_delete"));
        delete.clickAndWaitForNewWindow();
        sleep(400);

        UiObject confirm = new UiObject(new UiSelector().resourceId("android:id/button1"));
        confirm.click();
        sleep(1000);
    }

    public void downloadItemInSingleView() throws UiObjectNotFoundException {
        UiObject download = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_download"));
        download.clickAndWaitForNewWindow();
        sleep(400);

        UiObject confirm = new UiObject(new UiSelector().resourceId("android:id/button1"));
        if (confirm.exists()) {
            confirm.click();
            sleep(1000);
        }
    }

    public void clickTabItem(int position) throws UiObjectNotFoundException {
        UiCollection tabLayout = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/tabLayout"));
        UiObject tabItem = tabLayout.getChildByInstance(new UiSelector().className("android.support.v7.app.ActionBar$Tab"), position);
        tabItem.click();
        waitForDialog();
    }

    public void makeVideoControlVisible() throws UiObjectNotFoundException {
        UiObject toolbarTitle = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_toolbar_title"));
        UiObject videoImageObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/surface_view"));
        while(!toolbarTitle.exists()){
            if (!videoImageObj.exists())
                return;
            videoImageObj.click();
            toolbarTitle = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_current_time"));
        }
    }

    public void checkPlayable() throws UiObjectNotFoundException {
        makeVideoControlVisible();  //確認控制bar存在
        UiObject currentTimeObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_current_time"));
        String preCurrentTime = currentTimeObj.getText();
        UiObject playObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_play"));
        playObj.click();
        sleep(3000);
        makeVideoControlVisible();  //確認控制bar存在
        currentTimeObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_current_time"));
        Assert.assertEquals(false, preCurrentTime.equals(currentTimeObj.getText()));
        playObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_play"));
        playObj.click();
    }

    public void checkVideoView(){
        UiObject imageObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_image"));
        if (imageObj.exists()){
            //檢查viewPager個數
            UiCollection layout = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/image_layout"));
            Assert.assertEquals(1, layout.getChildCount(new UiSelector().resourceId("com.transcend.bcr:id/video_image")));
        }
        else{
            //檢查viewPager個數
            UiCollection layout = new UiCollection(new UiSelector().className("android.widget.RelativeLayout"));
            Assert.assertEquals(1, layout.getChildCount(new UiSelector().resourceId("com.transcend.bcr:id/surface_view")));
        }
    }

    //TODO 檢查排版
    public void checkComposing(UiScrollable recycler, boolean portraitOnly) throws UiObjectNotFoundException, RemoteException {
        if (recycler.getChildCount(new UiSelector().resourceId("com.transcend.bcr:id/item_manage")) == 0)
            return;

        UiObject gridItem;
        int column_count;

        if (Testing_device == Phone){
            mDevice.setOrientationNatural();
            sleep(1000);
            gridItem = recycler.getChildByInstance(new UiSelector().resourceId("com.transcend.bcr:id/item_manage"), 0);
            column_count = (int) (mDevice.getDisplayWidth() / gridItem.getBounds().width());
            Assert.assertEquals(3, column_count);

            if (!portraitOnly){
                mDevice.setOrientationLeft();
                sleep(1000);
                gridItem = recycler.getChildByInstance(new UiSelector().resourceId("com.transcend.bcr:id/item_manage"), 0);
                column_count = (int) (mDevice.getDisplayWidth() / gridItem.getBounds().width());
                Assert.assertEquals(6, column_count);
            }
        }
        else{
            mDevice.setOrientationNatural();
            sleep(1000);
            gridItem = recycler.getChildByInstance(new UiSelector().resourceId("com.transcend.bcr:id/item_manage"), 0);
            column_count = (int) (mDevice.getDisplayWidth() / gridItem.getBounds().width());
            Assert.assertEquals(6, column_count);

            if (!portraitOnly) {
                mDevice.setOrientationLeft();
                sleep(1000);
                gridItem = recycler.getChildByInstance(new UiSelector().resourceId("com.transcend.bcr:id/item_manage"), 0);
                column_count = (int) (mDevice.getDisplayWidth() / gridItem.getBounds().width());
                Assert.assertEquals(8, column_count);
            }
        }

        mDevice.setOrientationNatural();
        mDevice.unfreezeRotation();
    }
}
