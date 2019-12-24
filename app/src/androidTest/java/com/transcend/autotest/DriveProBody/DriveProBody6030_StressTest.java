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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DriveProBody6030_StressTest {
    private UiDevice mDevice;
    private Instrumentation instrumentation;

    private int execute_times = 3;

    private static int Type_Video = 0;
    private static int Type_EmergencyVideo = 1;
    private static int Type_Image = 2;
    private static int Type_Music = 3;

    private static String process1_path = "/mnt/sdcard/DrivePro_BugList Body Stress test/60_30/1. Live View 多次切換/";
    private static String process2_path = "/mnt/sdcard/DrivePro_BugList Body Stress test/60_30/2. 多次下載 (影片)/";
    private static String process3_path = "/mnt/sdcard/DrivePro_BugList Body Stress test/60_30/3. 多次下載 (相片)/";
    private static String process4_path = "/mnt/sdcard/DrivePro_BugList Body Stress test/60_30/4. 畫面翻轉 (影片)/";
    private static String process5_path = "/mnt/sdcard/DrivePro_BugList Body Stress test/60_30/5. 畫面翻轉 (相片)/";

    private static int imageDownloadWaitingTime = 10000;    //圖片下載等待10秒
    private static int videoDownloadWaitingTime = 10000;   //影片下載等待2分鐘

    @Before
    public void setup() throws UiObjectNotFoundException, RemoteException {
        instrumentation = getInstrumentation();
        mDevice = UiDevice.getInstance(instrumentation);

        getPermission(mDevice);
    }

    //TODO Process1
    @Test
    public void Process1_RepeatOperation() throws UiObjectNotFoundException {
        File f = new File(process1_path);
        if (!f.exists())
            f.mkdirs();

        for (int i = 0; i < execute_times; i++){
            boolean isFinal = (i == execute_times - 1);

            //打開Drawer
            openDrawer();
            if (isFinal){
                sleep(1000);
                f = new File(process1_path + "1. Open Drawer.png");
                mDevice.takeScreenshot(f, 1.0f, 50);
            }

            //點擊首頁
            clickDrawerHome();
            if (isFinal){
                sleep(1000);
                f = new File(process1_path + "2. Click Home.png");
                mDevice.takeScreenshot(f, 1.0f, 50);
            }

            //打開Drawer
            openDrawer();
            if (isFinal){
                sleep(1000);
                f = new File(process1_path + "3. Open Drawer.png");
                mDevice.takeScreenshot(f, 1.0f, 50);
            }

            //點擊即時影像
            clickDrawerLiveView();
            if (isFinal){
                sleep(1000);
                f = new File(process1_path + "4. Click LiveView.png");
                mDevice.takeScreenshot(f, 1.0f, 50);
            }

            //打開Drawer
            openDrawer();
            if (isFinal){
                sleep(1000);
                f = new File(process1_path + "5. Open Drawer.png");
                mDevice.takeScreenshot(f, 1.0f, 50);
            }

            //點擊瀏覽
            clickDrawerBrowser();
            if (isFinal){
                sleep(1000);
                f = new File(process1_path + "6. Click Browser.png");
                mDevice.takeScreenshot(f, 1.0f, 50);
            }
        }
    }

    //TODO Process2
    @Test
    public void Process2_Download_Video() throws UiObjectNotFoundException{
        deleteMyStorage(Type_Video);

        File f = new File(process2_path);
        if (!f.exists())
            f.mkdirs();

        //打開Drawer
        openDrawer();
        sleep(1000);
        f = new File(process2_path + "1. Open Drawer.png");
        mDevice.takeScreenshot(f, 1.0f, 50);

        //點擊瀏覽
        clickDrawerBrowser();
        sleep(1000);
        f = new File(process2_path + "2. Click Browser.png");
        mDevice.takeScreenshot(f, 1.0f, 50);

        //點擊影片分頁
        clickTabItem(0);
        sleep(1000);
        f = new File(process2_path + "3. Click Tab.png");
        mDevice.takeScreenshot(f, 1.0f, 50);

        downloadItemAtBrowser(Type_Video);

        mDevice.pressBack();
    }

    //TODO Process3
    @Test
    public void Process3_Download_Image() throws UiObjectNotFoundException{
        deleteMyStorage(Type_Image);

        File f = new File(process3_path);
        if (!f.exists())
            f.mkdirs();

        //打開Drawer
        openDrawer();
        sleep(1000);
        f = new File(process3_path + "1. Open Drawer.png");
        mDevice.takeScreenshot(f, 1.0f, 50);

        //點擊瀏覽
        clickDrawerBrowser();
        sleep(1000);
        f = new File(process3_path + "2. Click Browser.png");
        mDevice.takeScreenshot(f, 1.0f, 50);

        //點擊照片分頁
        clickTabItem(1);
        sleep(1000);
        f = new File(process3_path + "3. Click Tab.png");
        mDevice.takeScreenshot(f, 1.0f, 50);

        downloadItemAtBrowser(Type_Image);

        mDevice.pressBack();
    }

    //TODO Process4
    @Test
    public void Process4_Rotate_Video() throws UiObjectNotFoundException, RemoteException {
        File f = new File(process4_path);
        if (!f.exists())
            f.mkdirs();

        //打開Drawer
        openDrawer();
        sleep(1000);
        f = new File(process4_path + "1. Open Drawer.png");
        mDevice.takeScreenshot(f, 1.0f, 50);

        //點擊瀏覽
        clickDrawerBrowser();
        sleep(1000);
        f = new File(process4_path + "2. Click Browser.png");
        mDevice.takeScreenshot(f, 1.0f, 50);

        //點擊影片分頁
        clickTabItem(0);
        sleep(1000);
        f = new File(process4_path + "3. Click Tab.png");
        mDevice.takeScreenshot(f, 1.0f, 50);

        UiScrollable recyclerView = getMainRecyclerView();
        if (getItemCount(recyclerView) != 0){
            UiObject item = getItem(recyclerView, 0);
            item.click();

            sleep(1000);
            f = new File(process4_path + "4. Click Cell.png");
            mDevice.takeScreenshot(f, 1.0f, 50);

            for (int i = 0; i < execute_times; i++) {
                makeVideoControlVisible();
                UiObject playObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_play"));
                playObj.click();
                if (i == 0){
                    sleep(1000);
                    f = new File(process4_path + "5. Click Play.png");
                    mDevice.takeScreenshot(f, 1.0f, 50);
                }

                for (int j = 0; j < execute_times; j++){
                    mDevice.setOrientationLeft();
                    sleep(400);
                    if (j == 0){
                        sleep(400);
                        f = new File(process4_path + "6. Landscape.png");
                        mDevice.takeScreenshot(f, 1.0f, 50);
                    }
                    mDevice.setOrientationNatural();
                    sleep(400);
                    if (j == 0){
                        sleep(400);
                        f = new File(process4_path + "7. Portrait.png");
                        mDevice.takeScreenshot(f, 1.0f, 50);
                    }
                }
                mDevice.unfreezeRotation();

                makeVideoControlVisible();
                UiObject nextObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_next"));
                nextObj.click();
                sleep(400);
                waitForDialog();
            }
            mDevice.pressBack();
        }
    }

    //TODO Process5
    @Test
    public void Process5_Rotate_Image() throws UiObjectNotFoundException, RemoteException {
        File f = new File(process5_path);
        if (!f.exists())
            f.mkdirs();

        //打開Drawer
        openDrawer();
        sleep(1000);
        f = new File(process5_path + "1. Open Drawer.png");
        mDevice.takeScreenshot(f, 1.0f, 50);

        //點擊瀏覽
        clickDrawerBrowser();
        sleep(1000);
        f = new File(process5_path + "2. Click Browser.png");
        mDevice.takeScreenshot(f, 1.0f, 50);

        //點擊照片分頁
        clickTabItem(1);
        sleep(1000);
        f = new File(process5_path + "3. Click Tab.png");
        mDevice.takeScreenshot(f, 1.0f, 50);

        UiScrollable recyclerView = getMainRecyclerView();
        if (getItemCount(recyclerView) != 0){
            UiObject item = getItem(recyclerView, 0);
            item.click();

            sleep(1000);
            f = new File(process5_path + "4. Click Cell.png");
            mDevice.takeScreenshot(f, 1.0f, 50);

            for (int i = 0; i < execute_times; i++) {
                for (int j = 0; j < execute_times; j++){
                    mDevice.setOrientationLeft();
                    sleep(400);
                    if (j == 0){
                        sleep(400);
                        f = new File(process5_path + "5. Landscape.png");
                        mDevice.takeScreenshot(f, 1.0f, 50);
                    }
                    mDevice.setOrientationNatural();
                    sleep(400);
                    if (j == 0){
                        sleep(400);
                        f = new File(process5_path + "6. Portrait.png");
                        mDevice.takeScreenshot(f, 1.0f, 50);
                    }
                }
                mDevice.unfreezeRotation();

                swipeToNextViewPager();
                sleep(400);
            }
            mDevice.pressBack();
        }
    }

    private void downloadItemAtBrowser(int type) throws UiObjectNotFoundException {
        UiScrollable recyclerView = getMainRecyclerView();
        if (getItemCount(recyclerView) > 0){
            UiObject item = getItem(recyclerView, 0);
            item.click();
            waitForDialog();

            String path;
            if (type == Type_Video)
                path = process2_path + "4. Click Cell.png";
            else
                path = process3_path + "4. Click Cell.png";
            sleep(1000);
            File f = new File(path);
            mDevice.takeScreenshot(f, 1.0f, 50);

            for (int i = 0; i < execute_times; i++) {
                if (type == Type_Video) {
                    downloadItemInVideoView(i == 0);
                    UiObject nextBtn = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_next"));
                    nextBtn.click();
                } else {
                    downloadItemInImageView(i == 0);
                    swipeToNextViewPager();
                    sleep(800);
                }
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

    public UiObject getItemTitle(UiScrollable recycler, int instance) throws UiObjectNotFoundException {
        return recycler.getChildByInstance(new UiSelector().resourceId("com.transcend.bcr:id/item_title"), instance);
    }

    public int getImageInViewPagerCount(){
        UiCollection viewPager = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/photo_view_pager"));
        return viewPager.getChildCount(new UiSelector().className("com.transcend.bcr:id/imageView"));
    }

    //TODO action
    public void openDrawer() throws UiObjectNotFoundException {
        UiObject drawer = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/toggle_btn"));
        drawer.clickAndWaitForNewWindow();
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

    public void clickTabItem(int position) throws UiObjectNotFoundException {
        UiCollection tabLayout = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/tabLayout"));
        UiObject tabItem = tabLayout.getChildByInstance(new UiSelector().className("android.support.v7.app.ActionBar$Tab"), position);
        tabItem.click();
        waitForDialog();
    }

    public void deleteMyStorage(int type) throws UiObjectNotFoundException {
        openDrawer();
        clickDrawerMyStorage();

        if (type == Type_Video)
            clickTabItem(0);
        else
            clickTabItem(2);

        if (getItemCount(getMainRecyclerView()) == 0)
            return;

        UiObject menu = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/btn_local_media_select"));
        menu.click();
        sleep(500);
        UiCollection menuList = new UiCollection(new UiSelector().className("android.widget.ListView"));
        UiObject selectAll = menuList.getChildByText(new UiSelector().resourceId("com.transcend.bcr:id/title"), "選取全部");
        selectAll.click();

        menu = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/btn_local_media_select"));
        menu.click();
        sleep(500);
        menuList = new UiCollection(new UiSelector().className("android.widget.ListView"));
        UiObject delete = menuList.getChildByText(new UiSelector().resourceId("com.transcend.bcr:id/title"), "刪除");
        delete.click();

        waitForDialog();

        UiObject leaveActionMode = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/toggle_btn"));
        leaveActionMode.click();
    }

    public void downloadItemInVideoView(boolean isFirst) throws UiObjectNotFoundException {
        UiObject download = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_download"));
        download.clickAndWaitForNewWindow();
        sleep(400);
        if(isFirst) {
            sleep(400);
            File f = new File(process2_path + "5. Click Download.png");
            mDevice.takeScreenshot(f, 1.0f, 50);
        }

        UiObject confirm = new UiObject(new UiSelector().resourceId("android:id/button1"));
        if (confirm.exists()) {
            confirm.click();
            sleep(500);
            waitForDialog();

            if(isFirst) {
                sleep(1000);
                File f = new File(process2_path + "6. Click Confirm.png");
                mDevice.takeScreenshot(f, 1.0f, 50);
            }
        }
    }

    public void downloadItemInImageView(boolean isFirst) throws UiObjectNotFoundException {
        UiObject menu = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/btn_player_remote_media_select"));
        menu.click();
        sleep(400);
        if(isFirst) {
            sleep(400);
            File f = new File(process3_path + "5. Click Menu.png");
            mDevice.takeScreenshot(f, 1.0f, 50);
        }

        UiCollection menuList = new UiCollection(new UiSelector().className("android.widget.ListView"));
        UiObject download = menuList.getChildByText(new UiSelector().resourceId("com.transcend.bcr:id/title"), "下載");
        download.click();
        sleep(400);
        if(isFirst) {
            sleep(400);
            File f = new File(process3_path + "6. Click Download.png");
            mDevice.takeScreenshot(f, 1.0f, 50);
        }

        UiObject confirm = new UiObject(new UiSelector().resourceId("android:id/button1"));
        if (confirm.exists()) {
            confirm.click();
            sleep(500);
            waitForDialog();

            if(isFirst) {
                sleep(400);
                File f = new File(process3_path + "5. Click Confirm.png");
                mDevice.takeScreenshot(f, 1.0f, 50);
            }
        }
    }

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

    private void getPermission(UiDevice uiDevice){
        try {
            uiDevice.executeShellCommand("pm grant com.transcend.autotest android.permission.READ_EXTERNAL_STORAGE");
            uiDevice.executeShellCommand("pm grant com.transcend.autotest android.permission.WRITE_EXTERNAL_STORAGE");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
