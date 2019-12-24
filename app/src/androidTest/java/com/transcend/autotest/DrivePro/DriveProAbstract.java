package com.transcend.autotest.DrivePro;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.test.uiautomator.UiCollection;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;

import org.junit.Assert;
import org.junit.Before;

import java.io.IOException;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

public class DriveProAbstract {
    protected UiDevice mDevice;
    protected Instrumentation instrumentation;
    protected int execute_times = 2;
    protected int swipe_times = 5;

    static protected int Remote_Browser_Types = 3;  //瀏覽頁面裡的內容總數，即影片、緊急錄影、照片(不包括全部檔案)
    static protected int Local_Browser_Types = 2;

    static protected int Browser_Modes = 2;  //Remote or Local

    @Before
    public void setup() {
        instrumentation = getInstrumentation();
        mDevice = UiDevice.getInstance(instrumentation);
    }

    protected UiScrollable getMainRecyclerView(){
        return new UiScrollable(new UiSelector().resourceId("com.transcend.cvr:id/recycler_view"));
    }

    protected int getItemCount(UiScrollable recycler){
        return recycler.getChildCount(new UiSelector().resourceId("com.transcend.cvr:id/item_manage"));
    }

    protected UiObject getItem(UiScrollable recycler, int instance) throws UiObjectNotFoundException {
        return recycler.getChildByInstance(new UiSelector().resourceId("com.transcend.cvr:id/item_manage"), instance);
    }

    protected UiObject getItemTitle(UiScrollable recycler, int instance) throws UiObjectNotFoundException {
        return recycler.getChildByInstance(new UiSelector().resourceId("com.transcend.cvr:id/item_title"), instance);

    }

    protected void tryToOpenDrawerBySwipe(){
        //右滑，試著滑出drawer
        mDevice.swipe(0, mDevice.getDisplayHeight() / 2,
                mDevice.getDisplayWidth() / 2, mDevice.getDisplayHeight() / 2, 10);

        sleep(1000);
    }

    protected void waitForDialog(){
        UiObject alertObj = new UiObject(new UiSelector().resourceId("android:id/progress"));
        while(alertObj.exists()){
            //等待dialog消失
        }
    }

    protected void openDrawer() throws UiObjectNotFoundException {
        //打開Drawer
        UiObject toggle = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/toggle_btn"));
        if (!toggle.exists()) {
            mDevice.pressBack();
            toggle = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/toggle_btn"));
        }
        toggle.click();

        sleep(1000);
    }

    protected void clickDrawerHome() throws UiObjectNotFoundException {
        UiObject drawer = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/design_navigation_view"));
        if (!drawer.exists()){
            openDrawer();
        }
        UiCollection drawerRecycler = new UiCollection(new UiSelector().resourceId("com.transcend.cvr:id/design_navigation_view"));
        UiObject drawerBrowser = drawerRecycler.getChildByText(new UiSelector().resourceId("com.transcend.cvr:id/design_menu_item_text"), "首頁");
        drawerBrowser.clickAndWaitForNewWindow();

        waitForDialog();
    }

    protected void clickDrawerMyStorage() throws UiObjectNotFoundException {
        UiObject drawer = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/design_navigation_view"));
        if (!drawer.exists()){
            openDrawer();
        }
        UiCollection drawerRecycler = new UiCollection(new UiSelector().resourceId("com.transcend.cvr:id/design_navigation_view"));
        UiObject drawerBrowser = drawerRecycler.getChildByText(new UiSelector().resourceId("com.transcend.cvr:id/design_menu_item_text"), "我的儲存");
        drawerBrowser.clickAndWaitForNewWindow();

        waitForDialog();
    }

    protected void clickDrawerRemote() throws UiObjectNotFoundException {
        UiCollection drawerRecycler = new UiCollection(new UiSelector().resourceId("com.transcend.cvr:id/design_navigation_view"));
        UiObject drawerBrowser = drawerRecycler.getChildByText(new UiSelector().resourceId("com.transcend.cvr:id/design_menu_item_text"), "行車記錄器");
        drawerBrowser.clickAndWaitForNewWindow();

        waitForDialog();
    }

    protected void clickDrawerLiveView() throws UiObjectNotFoundException {
        UiObject drawer = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/design_navigation_view"));
        if (!drawer.exists()){
            openDrawer();
        }
        UiCollection drawerRecycler = new UiCollection(new UiSelector().resourceId("com.transcend.cvr:id/design_navigation_view"));
        UiObject drawerLiveView = drawerRecycler.getChildByText(new UiSelector().resourceId("com.transcend.cvr:id/design_menu_item_text"), "即時影像");
        drawerLiveView.clickAndWaitForNewWindow();

        waitForDialog();
    }

    protected void clickDrawerSetting() throws UiObjectNotFoundException {
        UiObject drawer = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/design_navigation_view"));
        if (!drawer.exists()){
            openDrawer();
        }
        UiCollection drawerRecycler = new UiCollection(new UiSelector().resourceId("com.transcend.cvr:id/design_navigation_view"));
        UiObject setting = drawerRecycler.getChildByText(new UiSelector().resourceId("com.transcend.cvr:id/design_menu_item_text"), "設定");
        setting.clickAndWaitForNewWindow();

        waitForDialog();
    }

    protected void clickDrawerFeedback() throws UiObjectNotFoundException {
        UiObject drawer = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/design_navigation_view"));
        if (!drawer.exists()){
            openDrawer();
        }
        UiCollection drawerRecycler = new UiCollection(new UiSelector().resourceId("com.transcend.cvr:id/design_navigation_view"));
        UiObject feedback = drawerRecycler.getChildByText(new UiSelector().resourceId("com.transcend.cvr:id/design_menu_item_text"), "意見回饋");
        feedback.clickAndWaitForNewWindow();

        waitForDialog();
    }

    protected String getSingleViewTitle() throws UiObjectNotFoundException {
        UiObject titleObject = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/toolbar_title"));
        while(!titleObject.exists()){
            UiObject view = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/pager_photoviewer"));
            UiObject video = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/video_surface"));
            if (view.exists()) {
                view.click();
                titleObject = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/toolbar_title"));
            }
            else if (video.exists()){
                video.click();
                titleObject = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/toolbar_title"));
            }
            sleep(500);
        }
        return titleObject.getText();
    }

    public void clickTabItem(int position) throws UiObjectNotFoundException {
        UiCollection tabLayout = new UiCollection(new UiSelector().resourceId("com.transcend.cvr:id/tabLayout"));
        tabLayout.waitForExists(10000);
        UiObject item = tabLayout.getChildByInstance(new UiSelector().className("android.support.v7.app.ActionBar$Tab"), position);
        item.waitForExists(10000);
        item.clickAndWaitForNewWindow();

        waitForDialog();
    }

    public void swipeToNextViewPager(){
        mDevice.swipe(mDevice.getDisplayWidth()*2/3, mDevice.getDisplayHeight() / 2,
                0, mDevice.getDisplayHeight() / 2, 10);
        sleep(1000);
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
        sleep(1000);
    }

    public void sleep(int mills){
        try {
            Thread.sleep(mills);
        }catch(Exception e){

        }
    }

    protected void closeAPP(UiDevice uiDevice,String sPackageName){
        try {
            uiDevice.executeShellCommand("am force-stop "+sPackageName);//通過命令行關閉app
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void startAPP(UiDevice uiDevice,String sPackageName, String sLaunchActivity){
        try {
            uiDevice.executeShellCommand("am start -n "+sPackageName+"/"+sLaunchActivity);//通過命令行啟動app
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void startAPP(String sPackageName){
        Context mContext = instrumentation.getContext();

        Intent myIntent = mContext.getPackageManager().getLaunchIntentForPackage(sPackageName);  //通过Intent启动app
        mContext.startActivity(myIntent);
    }

    protected void downloadItemInImageView() throws UiObjectNotFoundException {
        makeImageControlBarVisible();

        //取得圖片標題
        String toolbarTitle = getSingleViewTitle();
        String [] toolbarTitleSplit = toolbarTitle.split("\n");

        UiObject download = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/photoviewer_download"));
        download.click();
        sleep(1000);

        //判斷下載物件名稱與圖片名稱
        UiObject message = new UiObject(new UiSelector().resourceId("android:id/message"));
        Assert.assertEquals(true, toolbarTitleSplit[0].equals(message.getText()));

        UiObject confirm = new UiObject(new UiSelector().resourceId("android:id/button1"));
        confirm.click();
        waitForDialog();
    }

    protected boolean deleteItemInImageView() throws UiObjectNotFoundException {
        makeImageControlBarVisible();

        //取得圖片標題
        String toolbarTitle = getSingleViewTitle();
        String [] toolbarTitleSplit = toolbarTitle.split("\n");

        UiObject delete = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/photoviewer_delete"));
        if (!delete.exists())
            makeImageControlBarVisible();
        delete.waitForExists(10000);
        delete.click();
        sleep(1000);

        //判斷刪除物件名稱與圖片名稱
        UiObject message = new UiObject(new UiSelector().resourceId("android:id/message"));
        Assert.assertEquals(true, toolbarTitleSplit[0].equals(message.getText()));

        UiObject confirm = new UiObject(new UiSelector().resourceId("android:id/button1"));
        confirm.click();
        waitForDialog();

        //若刪到沒檔案，會跳回瀏覽頁面
        if (!getMainRecyclerView().exists()) {
            //刪除後比較前後標題，不能一致
            Assert.assertEquals(false, toolbarTitle.equals(getSingleViewTitle()));
            return true;
        }
        return false;
    }

    protected void makeImageControlBarVisible() throws UiObjectNotFoundException {
        UiObject toolbar = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/toolbar_title"));
        while(!toolbar.exists()){
            UiObject image = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/photo_view"));
            if (image.exists())
                image.click();
            toolbar = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/toolbar_title"));
        }
        return;
    }

    protected boolean deleteItemInVideoView() throws UiObjectNotFoundException {
        makeVideoControlVisible();

        //取得圖片標題
        String toolbarTitle = getSingleViewTitle();
        String [] toolbarTitleSplit = toolbarTitle.split("\n");

        UiObject delete = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/vlc_button_delete"));
        delete.clickAndWaitForNewWindow();
        sleep(400);

        //判斷刪除物件名稱與資訊名稱
        UiObject message = new UiObject(new UiSelector().resourceId("android:id/message"));
        Assert.assertEquals(true, toolbarTitleSplit[0].equals(message.getText()));

        UiObject confirm = new UiObject(new UiSelector().resourceId("android:id/button1"));
        confirm.click();
        sleep(1000);

        //若刪到沒檔案，會跳回瀏覽頁面
        if (!getMainRecyclerView().exists()) {
            //刪除後比較前後標題，不能一致
             Assert.assertEquals(false, toolbarTitle.equals(getSingleViewTitle()));
            return true;
        }
        return false;
    }

    public void downloadItemInVideoView() throws UiObjectNotFoundException {
        makeVideoControlVisible();

        //取得圖片標題
        String toolbarTitle = getSingleViewTitle();
        String [] toolbarTitleSplit = toolbarTitle.split("\n");

        UiObject download = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/vlc_button_download"));
        download.clickAndWaitForNewWindow();
        sleep(400);

        //判斷下載物件名稱與圖片名稱
        UiObject message = new UiObject(new UiSelector().resourceId("android:id/message"));
        Assert.assertEquals(true, toolbarTitleSplit[0].equals(message.getText()));

        UiObject confirm = new UiObject(new UiSelector().resourceId("android:id/button1"));
        if (confirm.exists()) {
            confirm.click();
            waitForDialog();
            sleep(1000);
        }
    }

    protected void makeVideoControlVisible() throws UiObjectNotFoundException {
        UiObject toolbar = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/toolbar_title"));
        while (!toolbar.exists()){
            UiObject surfaceView = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/video_surface"));
            if (surfaceView.exists())
                surfaceView.click();
            toolbar = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/toolbar_title"));
        }
        return;
    }

    protected void checkViewPagerNums(){
        if (!isAtImageView())
            return;

        UiCollection viewPager = new UiCollection(new UiSelector().resourceId("com.transcend.cvr:id/pager_photoviewer"));
        Assert.assertEquals(1, viewPager.getChildCount(new UiSelector().resourceId("com.transcend.cvr:id/photo_view")));
    }

    protected boolean isAtVideoView(){
        UiObject currentTime = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/video_current_time"));
        return currentTime.exists();
    }

    protected boolean isAtImageView(){
        UiObject viewPager = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/pager_photoviewer"));
        return viewPager.exists();
    }
}
