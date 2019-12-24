package com.transcend.autotest.DriveProBody;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.support.test.uiautomator.UiCollection;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.UiWatcher;
import android.util.Log;

import org.junit.Assert;
import org.junit.Before;

import java.io.IOException;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

public abstract class DriveProBodyAbstract {

    protected UiDevice mDevice;
    protected Instrumentation instrumentation;

    protected int Testing_device = Test_Device_Type.Phone.ordinal();
    protected int Testing_dashcam = Test_Dashcam_Type.Device30_60.ordinal();

    protected enum Test_Dashcam_Type{
        Device30_60, Device20_52
    }

    protected enum Remote_Browser_Types{
        Video, Image, Audio
    }

    protected enum Local_Browser_Types{
        Video, Emergency_Video, Image, Audio
    }

    protected enum Test_Device_Type{
        Phone, Pad
    }

    static int execute_times = 3;

    static int waiting_time = 1000;

    @Before
    public void setup() throws UiObjectNotFoundException {
        instrumentation = getInstrumentation();
        mDevice = UiDevice.getInstance(instrumentation);
    }

    protected void openDrawer() throws UiObjectNotFoundException {
        ensureHasToggle();

        UiObject drawer = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/toggle_btn"));
        drawer.clickAndWaitForNewWindow();
    }

    protected void ensureHasToggle(){
        UiObject surfaceObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/surface_view"));
        UiObject imageObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/image_layout"));
        UiObject videoObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_image"));

        if (surfaceObj.exists()) {
            mDevice.pressBack();
            waitForDialog();
        } else if (imageObj.exists()) {
            mDevice.pressBack();
            waitForDialog();
        } else if (videoObj.exists()) {
            mDevice.pressBack();
            waitForDialog();
        }
    }

    protected void swipeDrawerLandscape(){
        mDevice.swipe(0, mDevice.getDisplayHeight() / 2,
                mDevice.getDisplayWidth(), mDevice.getDisplayHeight() / 2, 10);
        sleep(1000);
    }

    protected void clickDrawerHome() throws UiObjectNotFoundException {
        UiCollection drawer = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/design_navigation_view"));
        if (!drawer.exists()) {
            openDrawer();
            drawer = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/design_navigation_view"));
        }
        UiObject home = drawer.getChildByInstance(new UiSelector().className("android.support.v7.widget.LinearLayoutCompat"), 0);
        home.clickAndWaitForNewWindow();
        waitForDialog();
    }

    protected void clickDrawerLiveView() throws UiObjectNotFoundException {
        UiCollection drawer = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/design_navigation_view"));
        if (!drawer.exists()) {
            openDrawer();
            drawer = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/design_navigation_view"));
        }
        UiObject liveView = drawer.getChildByInstance(new UiSelector().className("android.support.v7.widget.LinearLayoutCompat"), 1);
        liveView.clickAndWaitForNewWindow();
        waitForDialog();

        sleep(3000);

        UiObject wifiList = new UiObject(new UiSelector().resourceId("com.android.settings:id/list"));
        if (wifiList.exists()){
            mDevice.pressBack();
            sleep(1000);

            //點開drawer
            openDrawer();

            //點擊瀏覽
            clickDrawerLiveView();
        }
    }

    protected void clickDrawerBrowser() throws UiObjectNotFoundException {
        UiCollection drawer = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/design_navigation_view"));
        if (!drawer.exists()) {
            openDrawer();
            drawer = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/design_navigation_view"));
        }
        UiObject browser = drawer.getChildByInstance(new UiSelector().className("android.support.v7.widget.LinearLayoutCompat"), 2);
        browser.clickAndWaitForNewWindow();
        waitForDialog();

        sleep(3000);

        UiObject wifiList = new UiObject(new UiSelector().resourceId("com.android.settings:id/list"));
        if (wifiList.exists()){
            mDevice.pressBack();
            sleep(1000);

            //點開drawer
            openDrawer();

            //點擊瀏覽
            clickDrawerBrowser();
        }
    }

    protected void clickDrawerMyStorage() throws UiObjectNotFoundException {
        UiCollection drawer = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/design_navigation_view"));
        if (!drawer.exists()) {
            openDrawer();
            drawer = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/design_navigation_view"));
        }
        UiObject myStorage = drawer.getChildByInstance(new UiSelector().className("android.support.v7.widget.LinearLayoutCompat"), 3);
        myStorage.clickAndWaitForNewWindow();
        waitForDialog();
    }

    protected void clickDrawerSetting() throws UiObjectNotFoundException {
        UiCollection drawer = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/design_navigation_view"));
        if (!drawer.exists()) {
            openDrawer();
            drawer = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/design_navigation_view"));
        }
        UiObject setting = drawer.getChildByInstance(new UiSelector().className("android.support.v7.widget.LinearLayoutCompat"), 4);
        setting.clickAndWaitForNewWindow();
        waitForDialog();
    }

    protected void clickBackInSingleView() throws UiObjectNotFoundException {
        //UiCollection toolbar = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/media_player_toolbar"));
        //UiObject back = toolbar.getChildByInstance(new UiSelector().className("android.widget.ImageButton"), 0);
        UiObject back = new UiObject(new UiSelector().className("android.widget.ImageButton"));
        back.clickAndWaitForNewWindow();
    }

    protected void clickTabItem(int position) throws UiObjectNotFoundException {
        UiCollection tabLayout = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/tabLayout"));
        UiObject tabItem = tabLayout.getChildByInstance(new UiSelector().className("android.support.v7.app.ActionBar$Tab"), position);
        tabItem.click();
        waitForDialog();
    }

    protected void openMenu() throws UiObjectNotFoundException {
        UiObject menu = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/btn_local_media_select"));
        menu.click();
    }

    protected UiCollection getMenuList(){
        return new UiCollection(new UiSelector().className("android.widget.ListView"));
    }

    protected UiObject getMenuItem(UiCollection menuList, int instance) throws UiObjectNotFoundException {
        return menuList.getChildByInstance(new UiSelector().className("android.widget.LinearLayout"), instance);
    }

    //TODO 取得物件
    protected UiScrollable getMainRecyclerView(){
        return new UiScrollable(new UiSelector().resourceId("com.transcend.bcr:id/recycler_view"));
    }

    protected int getItemCount(UiScrollable recycler){
        return recycler.getChildCount(new UiSelector().resourceId("com.transcend.bcr:id/item_manage"));
    }

    protected UiObject getItem(UiScrollable recycler, int instance) throws UiObjectNotFoundException {
        return recycler.getChildByInstance(new UiSelector().resourceId("com.transcend.bcr:id/item_manage"), instance);
    }

    protected void swipeToNextViewPager(){
        mDevice.swipe(mDevice.getDisplayWidth()*2/3, mDevice.getDisplayHeight() / 2,
                0, mDevice.getDisplayHeight() / 2, 10);
        sleep(500);
    }

    protected void lightSwipeToNextViewPager(){
        //輕輕左滑一下
        mDevice.swipe(mDevice.getDisplayWidth() / 2, mDevice.getDisplayHeight() / 2,
                (mDevice.getDisplayWidth() / 2) - 90, mDevice.getDisplayHeight() / 2, 10);
        sleep(1000);
    }

    protected void swipeToPrevViewPager(){
        mDevice.swipe(0, mDevice.getDisplayHeight() / 2,
                mDevice.getDisplayWidth()*2/3, mDevice.getDisplayHeight() / 2, 10);
        sleep(500);
    }

    protected void sleep(int mills){
        try {
            Thread.sleep(mills);
        }catch(Exception e){

        }
    }

    protected void waitForDialog(){
        UiObject progress = new UiObject(new UiSelector().resourceId("android:id/progress"));
        while(progress.exists()){
            progress = new UiObject(new UiSelector().resourceId("android:id/progress"));
        }
    }

    protected void makeVideoControlVisible() throws UiObjectNotFoundException {
        UiObject toolbarTitle = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_toolbar_title"));
        UiObject videoImageObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/surface_view"));
        UiObject imageObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/image_layout"));
        while(!toolbarTitle.exists()){
            if (videoImageObj.exists())
                videoImageObj.click();
            else if (imageObj.exists())
                imageObj.click();
            else
                return;
            sleep(100);
            toolbarTitle = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_toolbar_title"));
        }
    }

    protected int getImageInViewPagerCount(){
        UiCollection viewPager = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/photo_view_pager"));
        return viewPager.getChildCount(new UiSelector().className("android.widget.RelativeLayout"));
    }

    protected boolean deleteItemInMusicView() throws UiObjectNotFoundException {
        //取得圖片標題
        String toolbarTitle = getSingleViewTitle();
        String [] toolbarTitleSplit = toolbarTitle.split("\n");

        UiObject delete = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/music_shuffle"));
        delete.clickAndWaitForNewWindow();
        sleep(400);

        //判斷刪除物件名稱與資訊名稱
        UiObject message = new UiObject(new UiSelector().resourceId("android:id/message"));
        message.waitForExists(10000);
        String [] messageSplit = message.getText().split("\\.");
        Assert.assertEquals(true, toolbarTitleSplit[0].equals(messageSplit[0]));

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

    protected boolean deleteItemInVideoView() throws UiObjectNotFoundException {
        //取得圖片標題
        String toolbarTitle = getSingleViewTitle();
        String [] toolbarTitleSplit = toolbarTitle.split("\n");

        UiObject delete = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_delete"));
        delete.clickAndWaitForNewWindow();
        sleep(400);

        //判斷刪除物件名稱與資訊名稱
        UiObject message = new UiObject(new UiSelector().resourceId("android:id/message"));
        message.waitForExists(10000);
        String [] messageSplit = message.getText().split("\\.");
        Assert.assertEquals(true, toolbarTitleSplit[0].equals(messageSplit[0]));

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

    protected boolean deleteItemInImageView() throws UiObjectNotFoundException {
        //取得圖片標題
        String toolbarTitle = getSingleViewTitle();
        String [] toolbarTitleSplit = toolbarTitle.split("\n");

        UiObject menu = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/btn_player_remote_media_select"));
        menu.click();

        UiCollection menuList = new UiCollection(new UiSelector().className("android.widget.ListView"));
        UiObject delete = menuList.getChildByText(new UiSelector().resourceId("com.transcend.bcr:id/title"), "刪除");
        delete.click();

        //判斷刪除物件名稱與資訊名稱
        UiObject message = new UiObject(new UiSelector().resourceId("android:id/message"));
        message.waitForExists(10000);
        String [] messageSplit = message.getText().split("\\.");
        Assert.assertEquals(true, toolbarTitleSplit[0].equals(messageSplit[0]));

        UiObject confirm = new UiObject(new UiSelector().resourceId("android:id/button1"));
        confirm.click();
        waitForDialog();
        sleep(1000);

        //若刪到沒檔案，會跳回瀏覽頁面
        if (!getMainRecyclerView().exists()) {
            //刪除後比較前後標題，不能一致
            Assert.assertEquals(false, toolbarTitle.equals(getSingleViewTitle()));
            return true;
        }
        return false;
    }

    protected void downloadItemInMusicView() throws UiObjectNotFoundException {
        //取得圖片標題
        String toolbarTitle = getSingleViewTitle();
        String [] toolbarTitleSplit = toolbarTitle.split("\n");

        UiObject download = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/music_repeat"));
        download.clickAndWaitForNewWindow();
        sleep(400);

        //判斷下載物件名稱與圖片名稱
        UiObject message = new UiObject(new UiSelector().resourceId("android:id/message"));
        String[] messageSplit = message.getText().split("\\.");
        Assert.assertEquals(true, toolbarTitleSplit[0].equals(messageSplit[0]));

        UiObject confirm = new UiObject(new UiSelector().resourceId("android:id/button1"));
        if (confirm.exists()) {
            confirm.click();
            waitForDialog();
        }
    }

    protected void downloadItemInVideoView() throws UiObjectNotFoundException {
        //取得圖片標題
        String toolbarTitle = getSingleViewTitle();
        String [] toolbarTitleSplit = toolbarTitle.split("\n");

        UiObject download = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_download"));
        download.clickAndWaitForNewWindow();
        sleep(400);

        //判斷下載物件名稱與圖片名稱
        UiObject message = new UiObject(new UiSelector().resourceId("android:id/message"));
        String[] messageSplit = message.getText().split("\\.");
        Assert.assertEquals(true, toolbarTitleSplit[0].equals(messageSplit[0]));

        UiObject confirm = new UiObject(new UiSelector().resourceId("android:id/button1"));
        if (confirm.exists()) {
            confirm.click();
            waitForDialog();
        }
    }

    protected void downloadItemInImageView() throws UiObjectNotFoundException {
        //取得圖片標題
        String toolbarTitle = getSingleViewTitle();
        String [] toolbarTitleSplit = toolbarTitle.split("\n");

        UiObject menu = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/btn_player_remote_media_select"));
        menu.click();

        UiCollection menuList = new UiCollection(new UiSelector().className("android.widget.ListView"));
        UiObject download = menuList.getChildByText(new UiSelector().resourceId("com.transcend.bcr:id/title"), "下載");
        download.click();

        //判斷下載物件名稱與圖片名稱
        UiObject message = new UiObject(new UiSelector().resourceId("android:id/message"));
        String[] messageSplit = message.getText().split("\\.");
        Assert.assertEquals(true, toolbarTitleSplit[0].equals(messageSplit[0]));

        UiObject confirm = new UiObject(new UiSelector().resourceId("android:id/button1"));
        confirm.click();
        waitForDialog();
        sleep(1000);
    }

    protected UiObject getItemTitle(UiScrollable recycler, int instance) throws UiObjectNotFoundException {
        UiObject title = recycler.getChildByInstance(new UiSelector().resourceId("com.transcend.bcr:id/item_title"), instance);
        if (title.exists())
            return title;
        else
            return null;
    }

    protected void checkPlayable() throws UiObjectNotFoundException {
        UiObject playObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_play"));
        playObj.click();
        sleep(3000);
        makeVideoControlVisible();  //確認控制bar存在
        UiObject currentTimeObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_current_time"));
        String preCurrentTime = currentTimeObj.getText();
        sleep(3000);
        makeVideoControlVisible();  //確認控制bar存在
        currentTimeObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_current_time"));
        if (currentTimeObj.getText().equals("00:00:00")){
            playObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_play"));
            playObj.click();
            return;
        }
        Assert.assertEquals(false, preCurrentTime.equals(currentTimeObj.getText()));
        playObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_play"));
        playObj.click();
    }

    protected void checkVideoView(){
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
        sleep(500);
    }

    protected void checkImageView(){
        UiCollection viewPager = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/photo_view_pager"));
        Assert.assertEquals(1, viewPager.getChildCount(new UiSelector().className("android.widget.RelativeLayout")));
        sleep(500);
    }

    //TODO 檢查排版
    protected void checkComposing(UiScrollable recycler) throws UiObjectNotFoundException, RemoteException {

        if (recycler.getChildCount(new UiSelector().className("android.widget.FrameLayout")) == 0)
            return;

        UiObject gridItem1;
        //grid item裡還有一個FrameLayout，故要跳著索取
        gridItem1 = recycler.getChildByInstance(new UiSelector().className("android.widget.FrameLayout"), 0);

        int column_count = (int) (mDevice.getDisplayWidth() / gridItem1.getBounds().width());
        //Log.e("checkIssue60_12",""+(mDevice.getDisplayWidth() / gridItem1.getBounds().width()));

        if (Testing_device == Test_Device_Type.Phone.ordinal()){
            mDevice.setOrientationNatural();

            Assert.assertEquals(3, column_count);
        }
        else{
            mDevice.setOrientationNatural();
            Assert.assertEquals(6, column_count);

            mDevice.setOrientationLeft();
            column_count = (int) (mDevice.getDisplayWidth() / gridItem1.getBounds().width());
            Assert.assertEquals(8, column_count);
        }

        mDevice.unfreezeRotation();
    }

    //TODO 檢查排版
    protected void checkComposing(UiScrollable recycler, boolean portraitOnly) throws UiObjectNotFoundException, RemoteException {
        if (recycler.getChildCount(new UiSelector().resourceId("com.transcend.bcr:id/item_manage")) == 0)
            return;

        UiObject gridItem;
        int column_count;

        if (Testing_device == Test_Device_Type.Phone.ordinal()){
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

    protected String getSingleViewTitle() throws UiObjectNotFoundException {
        UiObject imageViewObs = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/imageView"));
        UiObject musicViewObs = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/music_image"));
        UiObject videoViewObs1 = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_image"));
        UiObject videoViewObs2 = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/surface_view"));

        if (imageViewObs.exists()){
            UiObject toolbarTitle = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/toolbar_title"));
            while(!toolbarTitle.exists()){
                imageViewObs.click();
            }
            return toolbarTitle.getText();
        }
        else if (musicViewObs.exists()){
            UiObject toolbarTitle = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/music_toolbar_title"));
            return toolbarTitle.getText();
        }
        else if (videoViewObs1.exists() || videoViewObs2.exists()){
            UiObject toolbarTitle = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_toolbar_title"));
            while(!toolbarTitle.exists()){
                if (videoViewObs1.exists())
                    videoViewObs1.click();
                else if (videoViewObs2.exists())
                    videoViewObs2.click();
            }
            return toolbarTitle.getText();
        }
        return "";
    }

    protected boolean isAtImageView(){
        return new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/imageView")).exists();
    }

    protected boolean isAtMusicView(){
        return new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/music_image")).exists();
    }

    protected boolean isAtVideoView(){
        return  new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_image")).exists() || new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/surface_view")).exists();
    }

    protected boolean isSelectMode(){
        UiObject actionModeTitle = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/action_mode_custom_title"));
        try {
            return actionModeTitle.getText().contains("選取項目");
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected void scrollToEndOrTop(UiScrollable recyclerView, boolean toEnd) throws UiObjectNotFoundException {
        int x = 0, y = 0;
        int sameValueCount = 0; //若滑動N次位置恆不變，則判斷為已滑至底部
        while(sameValueCount < 5){
            if (getItemCount(recyclerView) > 0){
                UiObject item = getItem(recyclerView, 0);
                int tmpX = item.getBounds().centerX();
                int tmpY = item.getBounds().centerY();
                if (tmpX == x && tmpY == y)
                    sameValueCount++;
                else{
                    x = tmpX;
                    y = tmpY;
                }

                if (toEnd)
                    recyclerView.swipeUp(recyclerView.getMaxSearchSwipes());
                else
                    recyclerView.swipeDown(recyclerView.getMaxSearchSwipes());
            }
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
}
