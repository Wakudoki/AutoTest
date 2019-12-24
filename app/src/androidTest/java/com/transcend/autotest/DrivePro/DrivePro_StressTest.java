package com.transcend.autotest.DrivePro;

import android.app.Instrumentation;
import android.os.RemoteException;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiCollection;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.UiWatcher;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DrivePro_StressTest extends DriveProAbstract{

    private static int Type_Video = 0;
    private static int Type_EmergencyVideo = 1;
    private static int Type_Image = 2;
    private static int Type_Music = 3;

    private static String process1_path = "/mnt/sdcard/DrivePro_BugList Stress test/1. Live View 多次切換/";
    private static String process2_path = "/mnt/sdcard/DrivePro_BugList Stress test/2. 多次下載 (影片)/";
    private static String process3_path = "/mnt/sdcard/DrivePro_BugList Stress test/3. 多次下載 (緊急影片)/";
    private static String process4_path = "/mnt/sdcard/DrivePro_BugList Stress test/4. 多次下載 (相片)/";
    private static String process5_path = "/mnt/sdcard/DrivePro_BugList Stress test/5. 多次刪除 (影片)/";
    private static String process6_path = "/mnt/sdcard/DrivePro_BugList Stress test/6. 多次刪除 (緊急影片)/";
    private static String process7_path = "/mnt/sdcard/DrivePro_BugList Stress test/7. 多次刪除 (相片)/";
    private static String process8_path = "/mnt/sdcard/DrivePro_BugList Stress test/8. 畫面翻轉/";

    private static int imageDownloadWaitingTime = 10000;    //圖片下載等待10秒
    private static int videoDownloadWaitingTime = 10000;   //影片下載等待2分鐘

    @Before
    public void setup() {
        super.setup();
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
            clickDrawerRemote();
            if (isFinal){
                sleep(1000);
                f = new File(process1_path + "6. Click Browser.png");
                mDevice.takeScreenshot(f, 1.0f, 50);
            }
        }
    }

    //TODO Process2
    @Test
    public void Process2_DownloadVideo() throws UiObjectNotFoundException {
        File f = new File(process2_path);
        if (!f.exists())
            f.mkdirs();

        //打開Drawer
        openDrawer();
        sleep(1000);
        f = new File(process2_path + "1. Open Drawer.png");
        mDevice.takeScreenshot(f, 1.0f, 50);


        //點擊瀏覽
        clickDrawerRemote();
        sleep(1000);
        f = new File(process2_path + "2. Click Browser.png");
        mDevice.takeScreenshot(f, 1.0f, 50);

        //點擊影片tab
        clickTabItem(0);
        sleep(1000);
        f = new File(process2_path + "3. Click Video Tab.png");
        mDevice.takeScreenshot(f, 1.0f, 50);

        UiScrollable recycler = getMainRecyclerView();
        UiObject item = getItem(recycler, 0);
        if (item.exists()){
            item.clickAndWaitForNewWindow();

            //點擊第一個影片
            sleep(1000);
            f = new File(process2_path + "4. Click Video Item.png");
            mDevice.takeScreenshot(f, 1.0f, 50);

            for (int i = 0; i < execute_times; i++) {
                boolean isFinal = (i == execute_times - 1);

                //點擊下載
                UiObject downloadObj = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/vlc_button_download"));
                downloadObj.clickAndWaitForNewWindow();
                sleep(1000);
                if (isFinal) {
                    f = new File(process2_path + "5. Click Download.png");
                    mDevice.takeScreenshot(f, 1.0f, 50);
                }

                //點擊確認
                UiObject confirm = new UiObject(new UiSelector().resourceId("android:id/button1"));
                if (confirm.exists()) {
                    confirm.click();
                    sleep(1000);
                    if (isFinal){
                        f = new File(process2_path + "6. Click Confirm.png");
                        mDevice.takeScreenshot(f, 1.0f, 50);
                    }
                }

                waitForDialog();

                if (isFinal){
                    f = new File(process2_path + "7. Download Finished.png");
                    mDevice.takeScreenshot(f, 1.0f, 50);
                }

                clickVideoNext();
            }
            mDevice.pressBack();
            waitForDialog();
        }
    }

    //TODO Process3
    @Test
    public void Process3_DownloadEmergencyVideo() throws UiObjectNotFoundException {
        File f = new File(process3_path);
        if (!f.exists())
            f.mkdirs();

        //打開Drawer
        openDrawer();
        sleep(1000);
        f = new File(process3_path + "1. Open Drawer.png");
        mDevice.takeScreenshot(f, 1.0f, 50);


        //點擊瀏覽
        clickDrawerRemote();
        sleep(1000);
        f = new File(process3_path + "2. Click Browser.png");
        mDevice.takeScreenshot(f, 1.0f, 50);

        //點擊緊急影片tab
        clickTabItem(1);
        sleep(1000);
        f = new File(process3_path + "3. Click Emergency Video Tab.png");
        mDevice.takeScreenshot(f, 1.0f, 50);

        UiScrollable recycler = getMainRecyclerView();
        UiObject item = getItem(recycler, 0);
        if (item.exists()){
            item.clickAndWaitForNewWindow();

            //點擊第一個影片
            sleep(1000);
            f = new File(process3_path + "4. Click Video Item.png");
            mDevice.takeScreenshot(f, 1.0f, 50);

            for (int i = 0; i < execute_times; i++) {
                boolean isFinal = (i == execute_times - 1);

                //點擊下載
                UiObject downloadObj = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/vlc_button_download"));
                downloadObj.clickAndWaitForNewWindow();
                sleep(1000);
                if (isFinal) {
                    f = new File(process3_path + "5. Click Download.png");
                    mDevice.takeScreenshot(f, 1.0f, 50);
                }

                //點擊確認
                UiObject confirm = new UiObject(new UiSelector().resourceId("android:id/button1"));
                if (confirm.exists()) {
                    confirm.click();
                    sleep(1000);
                    if (isFinal){
                        f = new File(process3_path + "6. Click Confirm.png");
                        mDevice.takeScreenshot(f, 1.0f, 50);
                    }
                }

                waitForDialog();

                if (isFinal){
                    f = new File(process3_path + "7. Download Finished.png");
                    mDevice.takeScreenshot(f, 1.0f, 50);
                }

                clickVideoNext();
            }
            mDevice.pressBack();
            waitForDialog();
        }
    }

    //TODO Process4
    @Test
    public void Process4_DownloadImage() throws UiObjectNotFoundException {
        File f = new File(process4_path);
        if (!f.exists())
            f.mkdirs();

        //打開Drawer
        openDrawer();
        sleep(1000);
        f = new File(process4_path + "1. Open Drawer.png");
        mDevice.takeScreenshot(f, 1.0f, 50);


        //點擊瀏覽
        clickDrawerRemote();
        sleep(1000);
        f = new File(process4_path + "2. Click Browser.png");
        mDevice.takeScreenshot(f, 1.0f, 50);

        //點擊圖片tab
        clickTabItem(2);
        sleep(1000);
        f = new File(process4_path + "3. Click Image Tab.png");
        mDevice.takeScreenshot(f, 1.0f, 50);

        UiScrollable recycler = getMainRecyclerView();
        UiObject item = getItem(recycler, 0);
        if (item.exists()){
            item.clickAndWaitForNewWindow();

            //點擊第一個圖片
            sleep(1000);
            f = new File(process4_path + "4. Click Image Item.png");
            mDevice.takeScreenshot(f, 1.0f, 50);

            for (int i = 0; i < execute_times; i++) {
                boolean isFinal = (i == execute_times - 1);

                //點擊下載
                UiObject downloadObj = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/photoviewer_download"));
                downloadObj.clickAndWaitForNewWindow();
                sleep(1000);
                if (isFinal) {
                    f = new File(process4_path + "5. Click Download.png");
                    mDevice.takeScreenshot(f, 1.0f, 50);
                }

                //點擊確認
                UiObject confirm = new UiObject(new UiSelector().resourceId("android:id/button1"));
                if (confirm.exists()) {
                    confirm.click();
                    sleep(1000);
                    if (isFinal){
                        f = new File(process4_path + "6. Click Confirm.png");
                        mDevice.takeScreenshot(f, 1.0f, 50);
                    }
                }

                waitForDialog();

                if (isFinal){
                    f = new File(process4_path + "7. Download Finished.png");
                    mDevice.takeScreenshot(f, 1.0f, 50);
                }

                swipeToNextViewPager();
            }
            mDevice.pressBack();
            waitForDialog();
        }
    }

    //TODO Process5
    @Test
    public void Process5_DeleteVideo() throws UiObjectNotFoundException {
        File f = new File(process5_path);
        if (!f.exists())
            f.mkdirs();

        //打開Drawer
        openDrawer();
        sleep(1000);
        f = new File(process5_path + "1. Open Drawer.png");
        mDevice.takeScreenshot(f, 1.0f, 50);


        //點擊瀏覽
        clickDrawerRemote();
        sleep(1000);
        f = new File(process5_path + "2. Click Browser.png");
        mDevice.takeScreenshot(f, 1.0f, 50);

        //點擊圖片tab
        clickTabItem(0);
        sleep(1000);
        f = new File(process5_path + "3. Click Video Tab.png");
        mDevice.takeScreenshot(f, 1.0f, 50);

        UiScrollable recycler = getMainRecyclerView();
        UiObject item = getItem(recycler, 0);
        if (item.exists()) {
            item.clickAndWaitForNewWindow();

            //點擊第一個影片
            sleep(1000);
            f = new File(process5_path + "4. Click Video Item.png");
            mDevice.takeScreenshot(f, 1.0f, 50);

            for (int i = 0; i < execute_times; i++) {
                boolean isFinal = (i == execute_times - 1);

                //點擊下載
                UiObject deleteObj = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/vlc_button_delete"));
                deleteObj.clickAndWaitForNewWindow();
                sleep(1000);
                if (isFinal) {
                    f = new File(process5_path + "5. Click Delete.png");
                    mDevice.takeScreenshot(f, 1.0f, 50);
                }

                //點擊確認
                UiObject confirm = new UiObject(new UiSelector().resourceId("android:id/button1"));
                if (confirm.exists()) {
                    confirm.click();
                    sleep(1000);
                    if (isFinal){
                        f = new File(process5_path + "6. Click Confirm.png");
                        mDevice.takeScreenshot(f, 1.0f, 50);
                    }
                }

                waitForDialog();

                if (isFinal){
                    f = new File(process5_path + "7. Delete Finished.png");
                    mDevice.takeScreenshot(f, 1.0f, 50);
                }

                deleteObj = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/vlc_button_delete"));
                if (!deleteObj.exists()) {
                    f = new File(process5_path + "Final. No file for deleting .png");
                    mDevice.takeScreenshot(f, 1.0f, 50);
                    return;
                }
            }
            mDevice.pressBack();
            waitForDialog();
        }
    }

    //TODO Process6
    @Test
    public void Process6_DeleteEmergencyVideo() throws UiObjectNotFoundException {
        File f = new File(process6_path);
        if (!f.exists())
            f.mkdirs();

        //打開Drawer
        openDrawer();
        sleep(1000);
        f = new File(process6_path + "1. Open Drawer.png");
        mDevice.takeScreenshot(f, 1.0f, 50);


        //點擊瀏覽
        clickDrawerRemote();
        sleep(1000);
        f = new File(process6_path + "2. Click Browser.png");
        mDevice.takeScreenshot(f, 1.0f, 50);

        //點擊圖片tab
        clickTabItem(1);
        sleep(1000);
        f = new File(process6_path + "3. Click Emergency Video Tab.png");
        mDevice.takeScreenshot(f, 1.0f, 50);

        UiScrollable recycler = getMainRecyclerView();
        UiObject item = getItem(recycler, 0);
        if (item.exists()) {
            item.clickAndWaitForNewWindow();

            //點擊第一個影片
            sleep(1000);
            f = new File(process6_path + "4. Click Video Item.png");
            mDevice.takeScreenshot(f, 1.0f, 50);

            for (int i = 0; i < execute_times; i++) {
                boolean isFinal = (i == execute_times - 1);

                //點擊下載
                UiObject deleteObj = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/vlc_button_delete"));
                deleteObj.clickAndWaitForNewWindow();
                sleep(1000);
                if (isFinal) {
                    f = new File(process6_path + "5. Click Delete.png");
                    mDevice.takeScreenshot(f, 1.0f, 50);
                }

                //點擊確認
                UiObject confirm = new UiObject(new UiSelector().resourceId("android:id/button1"));
                if (confirm.exists()) {
                    confirm.click();
                    sleep(1000);
                    if (isFinal){
                        f = new File(process6_path + "6. Click Confirm.png");
                        mDevice.takeScreenshot(f, 1.0f, 50);
                    }
                }

                waitForDialog();

                if (isFinal){
                    f = new File(process6_path + "7. Delete Finished.png");
                    mDevice.takeScreenshot(f, 1.0f, 50);
                }

                deleteObj = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/vlc_button_delete"));
                if (!deleteObj.exists()) {
                    f = new File(process6_path + "Final. No file for deleting .png");
                    mDevice.takeScreenshot(f, 1.0f, 50);
                    return;
                }
            }
            mDevice.pressBack();
            waitForDialog();
        }
    }

    //TODO Process7
    @Test
    public void Process7_DeleteImage() throws UiObjectNotFoundException {
        File f = new File(process7_path);
        if (!f.exists())
            f.mkdirs();

        //打開Drawer
        openDrawer();
        sleep(1000);
        f = new File(process7_path + "1. Open Drawer.png");
        mDevice.takeScreenshot(f, 1.0f, 50);


        //點擊瀏覽
        clickDrawerRemote();
        sleep(1000);
        f = new File(process7_path + "2. Click Browser.png");
        mDevice.takeScreenshot(f, 1.0f, 50);

        //點擊圖片tab
        clickTabItem(2);
        sleep(1000);
        f = new File(process7_path + "3. Click Image Tab.png");
        mDevice.takeScreenshot(f, 1.0f, 50);

        UiScrollable recycler = getMainRecyclerView();
        UiObject item = getItem(recycler, 0);
        if (item.exists()) {
            item.clickAndWaitForNewWindow();

            //點擊第一個影片
            sleep(1000);
            f = new File(process7_path + "4. Click Image Item.png");
            mDevice.takeScreenshot(f, 1.0f, 50);

            for (int i = 0; i < execute_times; i++) {
                boolean isFinal = (i == execute_times - 1);

                //點擊下載
                UiObject deleteObj = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/photoviewer_delete"));
                deleteObj.clickAndWaitForNewWindow();
                sleep(1000);
                if (isFinal) {
                    f = new File(process7_path + "5. Click Delete.png");
                    mDevice.takeScreenshot(f, 1.0f, 50);
                }

                //點擊確認
                UiObject confirm = new UiObject(new UiSelector().resourceId("android:id/button1"));
                if (confirm.exists()) {
                    confirm.click();
                    sleep(1000);
                    if (isFinal){
                        f = new File(process7_path + "6. Click Confirm.png");
                        mDevice.takeScreenshot(f, 1.0f, 50);
                    }
                }

                waitForDialog();

                if (isFinal){
                    f = new File(process7_path + "7. Delete Finished.png");
                    mDevice.takeScreenshot(f, 1.0f, 50);
                }

                deleteObj = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/photoviewer_delete"));
                if (!deleteObj.exists()) {
                    f = new File(process7_path + "Final. No file for deleting .png");
                    mDevice.takeScreenshot(f, 1.0f, 50);
                    return;
                }
            }
            mDevice.pressBack();
            waitForDialog();
        }
    }

    //TODO Process8
    @Test
    public void Process8_Rotate() throws UiObjectNotFoundException, RemoteException {
        File f = new File(process8_path);
        if (!f.exists())
            f.mkdirs();

        int browser_mode = 0;
        int pic_count = 0;
        while(browser_mode < Browser_Modes){
            //打開Drawer
            openDrawer();
            sleep(1000);
            f = new File(process8_path + (pic_count++) + ". Open Drawer.png");
            mDevice.takeScreenshot(f, 1.0f, 50);

            int browser_type = -1;
            if (browser_mode == 0) {
                //點擊瀏覽
                clickDrawerRemote();
                browser_type = Remote_Browser_Types;

                sleep(1000);
                f = new File(process8_path + (pic_count++) + ". Click Browser.png");
                mDevice.takeScreenshot(f, 1.0f, 50);
            }
            else{
                //點擊本機
                clickDrawerMyStorage();
                browser_type = Local_Browser_Types;

                sleep(1000);
                f = new File(process8_path + (pic_count++) + ". Click MyStorage.png");
                mDevice.takeScreenshot(f, 1.0f, 50);
            }

            int type = 0;
            String typeString = "";
            while(type < browser_type){
                switch(type){
                    case 0:
                        typeString = "Video";
                        break;
                    case 1:
                        if (browser_type == Local_Browser_Types)
                            typeString = "Image";
                        else
                            typeString = "Emergency Video";
                        break;
                    case 2:
                        typeString = "Image";
                        break;
                }

                //點擊tab
                clickTabItem(type);
                sleep(1000);
                f = new File(process8_path + (pic_count++) + ". Click "+typeString+" Tab.png");
                mDevice.takeScreenshot(f, 1.0f, 50);

                UiScrollable recycler = getMainRecyclerView();
                UiObject item = getItem(recycler, 0);
                if (item.exists()) {
                    item.clickAndWaitForNewWindow();

                    //點擊第一個影片
                    sleep(1000);
                    f = new File(process8_path + (pic_count++) + ". Click "+typeString+" Item.png");
                    mDevice.takeScreenshot(f, 1.0f, 50);

                    for (int i = 0; i < execute_times; i++) {
                        boolean isFinal = (i == execute_times - 1);

                        mDevice.setOrientationLeft();
                        sleep(1000);
                        if (isFinal){
                            f = new File(process8_path + (pic_count++) + ". "+typeString+" Landscape.png");
                            mDevice.takeScreenshot(f, 1.0f, 50);
                        }

                        mDevice.setOrientationNatural();
                        sleep(1000);
                        if (isFinal){
                            f = new File(process8_path + (pic_count++) + ". "+typeString+" Portrait.png");
                            mDevice.takeScreenshot(f, 1.0f, 50);
                        }
                    }
                    mDevice.pressBack();
                    waitForDialog();
                }
                type++;
            }
            browser_mode++;
        }
    }

    protected void clickVideoNext() throws UiObjectNotFoundException {
        UiObject nextObj = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/vlc_button_next"));
        nextObj.click();

        waitForDialog();
    }

    protected void clickVideoPrev() throws UiObjectNotFoundException {
        UiObject prevObj = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/vlc_button_previous"));
        prevObj.click();

        waitForDialog();
    }
}
