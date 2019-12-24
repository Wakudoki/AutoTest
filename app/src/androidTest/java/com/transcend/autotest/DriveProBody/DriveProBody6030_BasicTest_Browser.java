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
public class DriveProBody6030_BasicTest_Browser {

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
    public void First_None() throws RemoteException, UiObjectNotFoundException {
        UiScrollable recyclerView = getMainRecyclerView();

        //檢查排版
        checkComposing(recyclerView, true);

        if (getItemCount(recyclerView) > 0){
            //有檔案存在時，無檔案的圖示不能存在
            UiObject noItemViewObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/empty_view"));
            Assert.assertEquals(false, noItemViewObj.exists());

            recyclerView.scrollToBeginning(5);

            //檢查長寬，理論上長寬差不能超過一定量
            UiObject item = getItem(recyclerView, 0);
            Assert.assertEquals(true, Math.abs(item.getBounds().width() - item.getBounds().height()) < 30);

            //檢查相對位置
            UiObject item1 = getItem(recyclerView, 1);
            Assert.assertEquals(item.getBounds().centerY(), item1.getBounds().centerY());
        }
        else{
            //無檔案存在時，無檔案的圖示必須存在
            UiObject noItemViewObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/empty_view"));
            Assert.assertEquals(true, noItemViewObj.exists());
        }

        UiCollection tabLayout = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/tabLayout"));
        Assert.assertEquals(true, tabLayout.exists());
    }

    //TODO 2. switch&back
    @Test
    public void Second_SwitchAndBack() throws UiObjectNotFoundException, RemoteException {
        //進入首頁後回到browser，並檢查排版
        openDrawer();
        clickDrawerHome();
        openDrawer();
        clickDrawerBrowser();
        checkComposing(getMainRecyclerView(), true);

        //進入即時影像後回到browser，並檢查排版
        openDrawer();
        clickDrawerLiveView();
        openDrawer();
        clickDrawerBrowser();
        checkComposing(getMainRecyclerView(), true);

        //進入我的儲存後回到browser，並檢查排版
        openDrawer();
        clickDrawerMyStorage();
        checkComposing(getMainRecyclerView(), true);
        openDrawer();
        clickDrawerBrowser();
        checkComposing(getMainRecyclerView(), true);
    }

    //TODO 3. rotate
    @Test
    public void Third_Rotate() throws UiObjectNotFoundException, RemoteException {
        for (int i = 0; i < max_execute_time; i++){
            checkComposing(getMainRecyclerView(), true);
            sleep(1000);
        }
    }

    //TODO 4. tab
    @Test
    public void Fourth_TabCell() throws UiObjectNotFoundException {
        UiScrollable recyclerView;
        int swipeCount = 0;
        while(swipeCount < max_execute_time) {
            recyclerView = getMainRecyclerView();
            for (int i = 0; i < getItemCount(recyclerView) - 3; i++) {
                UiObject item = getItem(recyclerView, i);
                String cellTitle = getItemTitle(recyclerView, i).getText();
                item.click();
                waitForDialog();
                sleep(500);

                String singleViewTitle = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_toolbar_title")).getText();
                String [] titleSplit = singleViewTitle.split("\n");

                Assert.assertEquals(cellTitle, titleSplit[0]);

                mDevice.pressBack();
                waitForDialog();
            }

            recyclerView.scrollToEnd(1);
            swipeCount ++;
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

    public UiObject getItemTitle(UiScrollable recycler, int instance) throws UiObjectNotFoundException {
        return recycler.getChildByInstance(new UiSelector().resourceId("com.transcend.bcr:id/item_title"), instance);
    }

    public int getImageInViewPagerCount(){
        UiCollection viewPager = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/photo_view_pager"));
        return viewPager.getChildCount(new UiSelector().className("com.transcend.bcr:id/imageView"));
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
