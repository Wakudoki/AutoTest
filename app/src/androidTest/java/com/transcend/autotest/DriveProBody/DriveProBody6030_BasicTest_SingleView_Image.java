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
public class DriveProBody6030_BasicTest_SingleView_Image {

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

        UiScrollable recyclerView = getMainRecyclerView();

        checkComposing(recyclerView, false);

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
        clickTabItem(1);

        //檢查排版
        checkComposing(getMainRecyclerView(), false);

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
        clickTabItem(1);

        //檢查排版
        checkComposing(getMainRecyclerView(), false);

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
        clickTabItem(1);

        checkComposing(getMainRecyclerView(), false);

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

        clickTabItem(1);

        UiScrollable recyclerView = getMainRecyclerView();
        //回到singleView
        if (getItemCount(recyclerView) > 0) {
            UiObject item = getItem(recyclerView, 0);
            if (item.exists()) {
                item.clickAndWaitForNewWindow();
            }
        }
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

    public int getImageInViewPagerCount(){
        UiCollection viewPager = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/photo_view_pager"));
        return viewPager.getChildCount(new UiSelector().className("android.widget.RelativeLayout"));
    }

    //TODO action
    public void swipeToNextViewPager(){
        mDevice.swipe(mDevice.getDisplayWidth()*2/3, mDevice.getDisplayHeight() / 2,
                0, mDevice.getDisplayHeight() / 2, 10);
        sleep(500);
    }

    public void lightSwipeToNextViewPager(){
        //輕輕左滑一下
        mDevice.swipe(mDevice.getDisplayWidth() / 2, mDevice.getDisplayHeight() / 2,
                (mDevice.getDisplayWidth() / 2) - 90, mDevice.getDisplayHeight() / 2, 10);
        sleep(1000);
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

    public void openMenu() throws UiObjectNotFoundException {
        UiObject more = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/file_manage_viewer_action_more"));
        more.click();
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

    public void deleteItemInVideoView() throws UiObjectNotFoundException {
        UiObject delete = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_delete"));
        delete.clickAndWaitForNewWindow();
        sleep(400);

        UiObject confirm = new UiObject(new UiSelector().resourceId("android:id/button1"));
        confirm.click();
        sleep(1000);
    }

    public void deleteItemInImageView() throws UiObjectNotFoundException {
        UiObject menu = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/btn_player_remote_media_select"));
        menu.click();

        UiCollection menuList = new UiCollection(new UiSelector().className("android.widget.ListView"));
        UiObject delete = menuList.getChildByText(new UiSelector().resourceId("com.transcend.bcr:id/title"), "刪除");
        delete.click();

        UiObject confirm = new UiObject(new UiSelector().resourceId("android:id/button1"));
        confirm.click();
        waitForDialog();
        sleep(1000);
    }

    public void downloadItemInVideoView() throws UiObjectNotFoundException {
        UiObject download = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_download"));
        download.clickAndWaitForNewWindow();
        sleep(400);

        UiObject confirm = new UiObject(new UiSelector().resourceId("android:id/button1"));
        if (confirm.exists()) {
            confirm.click();
            sleep(1000);
        }
    }

    public void downloadItemInImageView() throws UiObjectNotFoundException {
        UiObject menu = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/btn_player_remote_media_select"));
        menu.click();

        UiCollection menuList = new UiCollection(new UiSelector().className("android.widget.ListView"));
        UiObject delete = menuList.getChildByText(new UiSelector().resourceId("com.transcend.bcr:id/title"), "下載");
        delete.click();

        UiObject confirm = new UiObject(new UiSelector().resourceId("android:id/button1"));
        confirm.click();
        waitForDialog();
        sleep(1000);
    }

    public void clickTabItem(int position) throws UiObjectNotFoundException {
        UiCollection tabLayout = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/tabLayout"));
        UiObject tabItem = tabLayout.getChildByInstance(new UiSelector().className("android.support.v7.app.ActionBar$Tab"), position);
        tabItem.click();
        waitForDialog();
    }

    //TODO 檢查排版
    public void checkComposing(UiScrollable recycler, boolean portraitOnly) throws UiObjectNotFoundException, RemoteException {
        if (recycler.getChildCount(new UiSelector().resourceId("com.transcend.nas:id/item_manage")) == 0)
            return;

        UiObject gridItem;
        int column_count;

        if (Testing_device == Phone){
            mDevice.setOrientationNatural();
            sleep(1000);
            gridItem = recycler.getChildByInstance(new UiSelector().resourceId("com.transcend.nas:id/item_manage"), 0);
            column_count = (int) (mDevice.getDisplayWidth() / gridItem.getBounds().width());
            Assert.assertEquals(3, column_count);

            if (!portraitOnly){
                mDevice.setOrientationLeft();
                sleep(1000);
                gridItem = recycler.getChildByInstance(new UiSelector().resourceId("com.transcend.nas:id/item_manage"), 0);
                column_count = (int) (mDevice.getDisplayWidth() / gridItem.getBounds().width());
                Assert.assertEquals(6, column_count);
            }
        }
        else{
            mDevice.setOrientationNatural();
            sleep(1000);
            gridItem = recycler.getChildByInstance(new UiSelector().resourceId("com.transcend.nas:id/item_manage"), 0);
            column_count = (int) (mDevice.getDisplayWidth() / gridItem.getBounds().width());
            Assert.assertEquals(6, column_count);

            if (!portraitOnly) {
                mDevice.setOrientationLeft();
                sleep(1000);
                gridItem = recycler.getChildByInstance(new UiSelector().resourceId("com.transcend.nas:id/item_manage"), 0);
                column_count = (int) (mDevice.getDisplayWidth() / gridItem.getBounds().width());
                Assert.assertEquals(8, column_count);
            }
        }

        mDevice.setOrientationNatural();
        mDevice.unfreezeRotation();
    }
}
