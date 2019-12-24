package com.transcend.autotest.DriveProBody;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.support.test.espresso.ViewInteraction;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiCollection;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.UiWatcher;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isRoot;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DriveProBody60 extends DriveProBodyAbstract{

    static private int browser_types = 3;  //瀏覽頁面裡的內容總數，即影片、緊急錄影、照片(不包括全部檔案)
    static private int browser_tabs = 2;   //瀏覽頁面裡的tab總數，即本地、遠端
    static int Phone = 0;
    static int Pad = 1;
    int Testing_device = Phone;

    @Before
    public void setup() throws UiObjectNotFoundException {
        super.setup();
    }

    //TODO issue60_02
    @Test
    public void issue60_02() throws UiObjectNotFoundException, IOException {
        //107/10/17 無法自動連線，先不進行此issue
//
//        sleep(6000);
//
//        //打開drawer
//        openDrawer();
//
//        //點擊設定
//        clickDrawerSetting();
//
//        UiScrollable recyclerView = new UiScrollable(new UiSelector().resourceId("com.transcend.bcr:id/settings_recycler_view"));
//        UiObject pixelObj = recyclerView.getChildByInstance(new UiSelector().resourceId("com.transcend.bcr:id/settingsStatus"), 0);
//        pixelObj.clickAndWaitForNewWindow();
//        sleep(1000);
//
//        UiCollection frame = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/custom"));
//
//        String pixelText = null;
//        int instance = 1;
//        //隨機取得關閉螢幕時間的LinearLayout序號
//        int random = (int) (Math.random() * 3);
//        switch(random){
//            case 0:
//                instance = 1;
//                pixelText = "1080P";
//                break;
//            case 1:
//                instance = 2;
//                pixelText = "720P";
//                break;
//            case 2:
//                instance = 3;
//                pixelText = "480P";
//                break;
//        }
//
//        UiObject clickTarget = frame.getChildByInstance(new UiSelector().className("android.widget.LinearLayout"), instance);
//        clickTarget.click();
//        clickTarget.click();
//        sleep(1000);
//
//        UiObject ok = new UiObject(new UiSelector().resourceId("android:id/button1"));
//        ok.click();
//        sleep(1000);
//        pixelObj = recyclerView.getChildByInstance(new UiSelector().resourceId("com.transcend.bcr:id/settingsStatus"), 0);
//        Assert.assertEquals(pixelText, pixelObj.getText());
//
//        //刷掉程式
//        closeAPP(mDevice, "com.transcend.bcr");
//
//        sleep(3000);
//
//        startAPP("com.transcend.bcr");
//
//        //監聽dialog
//        alertWatcher();
//
//        openDrawer();
//        clickDrawerLiveView();
//        sleep(6000);
//        mDevice.pressBack();
//
//        //點開drawer
//        openDrawer();
//
//        //點擊設定
//        clickDrawerSetting();
//
//        sleep(1000);
//
//        recyclerView = new UiScrollable(new UiSelector().resourceId("com.transcend.bcr:id/settings_recycler_view"));
//        pixelObj = recyclerView.getChildByInstance(new UiSelector().resourceId("com.transcend.bcr:id/settingsStatus"), 0);
//        Assert.assertEquals(pixelText, pixelObj.getText());
    }

    //TODO issue60_07
    @Test
    public void issue60_07() throws UiObjectNotFoundException, RemoteException {
        //點開drawer
        openDrawer();

        //點擊browser
        clickDrawerBrowser();

        UiScrollable browserRecycler = getMainRecyclerView();



//        UiObject item1 = getItem(browserRecycler, 0);
//        UiObject item2, item3, item4;
//        //Log.e("60 issue 07", ""+browserRecycler.getChildCount(new UiSelector().className("android.widget.FrameLayout")));
////        if (browserRecycler.getChildCount(new UiSelector().className("android.widget.FrameLayout")) < 4){
////            return;
////        }
//        item2 = browserRecycler.getChildByInstance(new UiSelector().className("android.widget.FrameLayout"), 1);
//        item3 = browserRecycler.getChildByInstance(new UiSelector().className("android.widget.FrameLayout"), 2);
//        item4 = browserRecycler.getChildByInstance(new UiSelector().className("android.widget.FrameLayout"), 3);
//
//        //紀錄操作前的X, Y座標
//        int centerX1, centerY1, centerX2, centerY2, centerX3, centerY3, centerX4, centerY4;
//        centerX1 = item1.getBounds().centerX();
//        centerY1 = item1.getBounds().centerY();
//        centerX2 = item2.getBounds().centerX();
//        centerY2 = item2.getBounds().centerY();
//        centerX3 = item3.getBounds().centerX();
//        centerY3 = item3.getBounds().centerY();
//        centerX4 = item4.getBounds().centerX();
//        centerY4 = item4.getBounds().centerY();

        //點開drawer
        openDrawer();

        //點擊live view
        clickDrawerLiveView();

        //轉橫屏
        mDevice.setOrientationLeft();

        sleep(1000);

        //滑開drawer
        swipeDrawerLandscape();

        //點擊browser
        clickDrawerBrowser();

        //點開drawer
        openDrawer();

        //點擊live view
        clickDrawerLiveView();

        //轉豎屏
        mDevice.setOrientationNatural();;

        //點開drawer
        openDrawer();

        //點擊browser
        clickDrawerBrowser();

        //檢查排版
        browserRecycler = getMainRecyclerView();
        checkComposing(browserRecycler);

//        item1 = browserRecycler.getChildByInstance(new UiSelector().className("android.widget.FrameLayout"), 0);
//        item2 = browserRecycler.getChildByInstance(new UiSelector().className("android.widget.FrameLayout"), 1);
//        item3 = browserRecycler.getChildByInstance(new UiSelector().className("android.widget.FrameLayout"), 2);
//        item4 = browserRecycler.getChildByInstance(new UiSelector().className("android.widget.FrameLayout"), 3);
//
//        Assert.assertEquals(true, ((centerX1==item1.getBounds().centerX()) && (centerY1==item1.getBounds().centerY())));
//        Assert.assertEquals(true, ((centerX2==item2.getBounds().centerX()) && (centerY2==item2.getBounds().centerY())));
//        Assert.assertEquals(true, ((centerX3==item3.getBounds().centerX()) && (centerY3==item3.getBounds().centerY())));
//        Assert.assertEquals(true, ((centerX4==item4.getBounds().centerX()) && (centerY4==item4.getBounds().centerY())));
//
//        mDevice.unfreezeRotation();
    }

    //TODO issue60_08, 09
    @Test
    public void issue60_08() throws UiObjectNotFoundException, RemoteException {
        //點開drawer
        openDrawer();

        //點擊browser
        clickDrawerBrowser();

        UiScrollable browserRecycler = getMainRecyclerView();
        UiObject item1 = getItem(browserRecycler, 0);
        if (item1.exists()) {
            item1.clickAndWaitForNewWindow();

            //刪除
            deleteItemInVideoView();

            //返回
            clickBackInSingleView();
        }

        //檢查排版
        browserRecycler = getMainRecyclerView();
        checkComposing(browserRecycler);
    }

    //TODO issue60_10
    @Test
    public void issue60_10() throws UiObjectNotFoundException, RemoteException {
        //點開drawer
        openDrawer();

        //點擊browser
        clickDrawerBrowser();

        UiScrollable browserRecycler = getMainRecyclerView();
//        Log.e("issue60_10", ""+browserRecycler.getChildCount(new UiSelector().className("android.widget.FrameLayout")));
        for (int count = 0; count < getItemCount(browserRecycler); count++){
            UiObject item1 = getItem(browserRecycler, count);
            if (item1.exists()){
                item1.clickAndWaitForNewWindow();

                //紀錄標題
                UiObject titleObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_toolbar_title"));
                String title = titleObj.getText();

                //旋轉
                for (int i = 0; i < 3; i++){
                    mDevice.setOrientationLeft();
                    sleep(400);
                    mDevice.setOrientationNatural();
                    sleep(400);
                }
                mDevice.unfreezeRotation();

                //確認標題有無跑掉
                titleObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_toolbar_title"));
                Assert.assertEquals(title, titleObj.getText());

                mDevice.pressBack();
            }
        }

        //點開drawer
        openDrawer();

        //點擊我的儲存
        clickDrawerMyStorage();

        UiScrollable storageRecycler = getMainRecyclerView();
//        Log.e("issue60_10", ""+storageRecycler.getChildCount(new UiSelector().className("android.widget.FrameLayout")));
        for (int count = 0; count < getItemCount(storageRecycler); count++){
            UiObject item1 = getItem(storageRecycler, count);
            if (item1.exists()){
                item1.clickAndWaitForNewWindow();

                //紀錄標題
                UiObject titleObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_toolbar_title"));
                String title = titleObj.getText();

                //旋轉
                for (int i = 0; i < 3; i++){
                    mDevice.setOrientationLeft();
                    sleep(400);
                    mDevice.setOrientationNatural();
                    sleep(400);
                }
                mDevice.unfreezeRotation();

                //確認標題有無跑掉
                titleObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_toolbar_title"));
                Assert.assertEquals(title, titleObj.getText());

                mDevice.pressBack();
            }
        }
    }

    //TODO issue60_11, 12
    @Test
    public void issue60_11() throws UiObjectNotFoundException {
        //點開drawer
        openDrawer();

        //點擊browser
        clickDrawerBrowser();

        UiScrollable browserRecycler = getMainRecyclerView();
        UiObject item1 = getItem(browserRecycler, 0);
        if (item1.exists()){
            item1.clickAndWaitForNewWindow();

            UiObject titleObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_toolbar_title"));
            String [] titleSplit = titleObj.getText().split("\\n");
            String [] dateTimeSplit = titleSplit[1].split("\\s");
            String today_date = dateTimeSplit[0];
            String date = today_date;

            //持續刪除，直到換天
            while(today_date.equals(date)) {
                deleteItemInVideoView();

                sleep(3000);

                titleObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_toolbar_title"));
                if (!titleObj.exists())
                    break;
                titleSplit = titleObj.getText().split("\\n");
                dateTimeSplit = titleSplit[1].split("\\s");
                date = dateTimeSplit[0];
            }
            //能完整結束表示沒問題了(issue11)

            UiObject empty = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/empty_view"));
            if(empty.exists()){ //空檔案的圖示出現，表示刪到沒東西了
                return;
            }

            //判斷畫面是否有跑掉(issue12)
            UiObject view = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_image"));
            if (!view.exists()){    //不存在表示不是影片，試著擷取照片
                view = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/imageView"));
                if (!view.exists()){    //不存在表示不是照片，試著擷取音檔圖片
                    view = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/music_image"));
                }
                else {
                    mDevice.pressBack();
                    return;
                }
            }
            Assert.assertEquals(view.getBounds().left, 0);
            Assert.assertEquals(view.getBounds().right, mDevice.getDisplayWidth());
        }

        mDevice.pressBack();
    }

    //TODO issue60_13
    @Test
    public void issue60_13() throws UiObjectNotFoundException {
        //點開drawer
        openDrawer();

        //點擊browser
        clickDrawerBrowser();

        UiObject empty = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/empty_view"));
        if(empty.exists()){ //空檔案的圖示出現，表示沒東西了
            return;
        }

        //盡量滑到底部，選取最後一個檔案
        UiScrollable recycler = getMainRecyclerView();
        recycler.scrollToEnd(10, 10);
        UiObject lastItem = getItem(recycler, getItemCount(recycler) - 1);
        if (lastItem.exists()){
            lastItem.clickAndWaitForNewWindow();

            //紀錄標題
            UiObject titleObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_toolbar_title"));
            String preTitle = titleObj.getText();

            //刪除檔案
            deleteItemInVideoView();

            //判斷刪除後的標題是否一致
            titleObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_toolbar_title"));
            Assert.assertEquals(false, preTitle.equals(titleObj.getText()));
        }
        mDevice.pressBack();
    }

    //TODO issue15, 16
    @Test
    public void issue60_15() throws UiObjectNotFoundException {
        //點開drawer
        openDrawer();

        //點擊browser
        clickDrawerBrowser();

        UiScrollable browserRecycler = getMainRecyclerView();
        UiObject item1 = getItem(browserRecycler, 0);
        if (item1.exists()){
            item1.clickAndWaitForNewWindow();

            for (int i = 0; i < 20; i++) {
                int random = (int) (Math.random()*2);
                switch (random){
                    case 0:
//                        swipeToPrevViewPager();
                        UiObject prev = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_previous"));
                        prev.click();
                        sleep(1000);
                        break;
                    case 1:
//                        swipeToNextViewPager();
                        UiObject next = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_next"));
                        next.click();
                        sleep(1000);
                        break;
                }
            }

            UiObject playBtn = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_play"));
            playBtn.click();

            sleep(3000);

            UiObject image = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/surface_view"));
            image.click();
            image.click();

            deleteItemInVideoView();
            sleep(1000);
            deleteItemInVideoView();
            sleep(1000);
            deleteItemInVideoView();
            sleep(1000);

            playBtn = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_play"));
            playBtn.click();

            mDevice.pressBack();
        }
    }

    //TODO issue60_18
//    @Test
//    public void issue_60_18() throws UiObjectNotFoundException {
//        //點開drawer
//        openDrawer();
//
//        //點擊我的儲存
//        clickDrawerMyStorage();

        //開始選取模式
//        UiObject select_mode_btn = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/btn_media_select_img"));
//        select_mode_btn.click();
//
//        //點選物品
//        UiCollection storage_recycler = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/storage_recycler_view"));
//        UiObject item = storage_recycler.getChildByInstance(new UiSelector().className("android.widget.FrameLayout"), 0);
//        item.click();
//
//        UiObject delete_btn = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/delete_btn"));
//        delete_btn.click();
//
//        //點擊more btn
//        UiObject moreBtnObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/btn_local_media_select"));
//        moreBtnObj.click();
//
//        UiCollection list = new UiCollection(new UiSelector().className("android.widget.ListView"));
//        UiObject deleteBtn = list.getChildByInstance(new UiSelector().className("android.widget.LinearLayout"), 0);
//        deleteBtn.click();
//        waitForDialog();
//    }

    //TODO issue60_20
    @Test
    public void issue60_20() throws UiObjectNotFoundException, RemoteException {
        //點開drawer
        openDrawer();

        //點擊browser
        clickDrawerHome();

        //轉橫屏
        mDevice.setOrientationLeft();

        //pad應開放landscape、phone則否
        if (Testing_device == Pad)
            Assert.assertEquals(true, mDevice.getDisplayWidth() > mDevice.getDisplayHeight());
        else
            Assert.assertEquals(false, mDevice.getDisplayWidth() > mDevice.getDisplayHeight());

        mDevice.setOrientationNatural();
        mDevice.unfreezeRotation();

        //點開drawer
        openDrawer();

        //點擊browser
        clickDrawerLiveView();

        //轉橫屏
        mDevice.setOrientationLeft();

        sleep(1000);

//        Log.e("504", mDevice.getDisplayWidth()+", "+ mDevice.getDisplayHeight());

        //pad應開放landscape、phone則否
        if (Testing_device == Pad)
            Assert.assertEquals(true, mDevice.getDisplayWidth() > mDevice.getDisplayHeight());
        else
            Assert.assertEquals(true, mDevice.getDisplayWidth() > mDevice.getDisplayHeight());

        mDevice.setOrientationNatural();
        mDevice.unfreezeRotation();

        //點開drawer
        openDrawer();

        //點擊browser
        clickDrawerBrowser();

        //轉橫屏
        mDevice.setOrientationLeft();

        //pad應開放landscape、phone則否
        if (Testing_device == Pad)
            Assert.assertEquals(true, mDevice.getDisplayWidth() > mDevice.getDisplayHeight());
        else
            Assert.assertEquals(false, mDevice.getDisplayWidth() > mDevice.getDisplayHeight());

        mDevice.setOrientationNatural();
        mDevice.unfreezeRotation();

        //點開drawer
        openDrawer();

        //點擊browser
        clickDrawerMyStorage();

        //轉橫屏
        mDevice.setOrientationLeft();

        //pad應開放landscape、phone則否
        if (Testing_device == Pad)
            Assert.assertEquals(true, mDevice.getDisplayWidth() > mDevice.getDisplayHeight());
        else
            Assert.assertEquals(false, mDevice.getDisplayWidth() > mDevice.getDisplayHeight());

        mDevice.setOrientationNatural();
        mDevice.unfreezeRotation();

        //點開drawer
        openDrawer();

        //點擊browser
        clickDrawerSetting();

        //轉橫屏
        mDevice.setOrientationLeft();

        //pad應開放landscape、phone則否
        if (Testing_device == Pad)
            Assert.assertEquals(true, mDevice.getDisplayWidth() > mDevice.getDisplayHeight());
        else
            Assert.assertEquals(false, mDevice.getDisplayWidth() > mDevice.getDisplayHeight());

        mDevice.setOrientationNatural();
        mDevice.unfreezeRotation();
    }

    //TODO issue60_21
    @Test
    public void issue60_21() throws UiObjectNotFoundException, RemoteException, IOException {
        //點開drawer
        openDrawer();

        //點擊首頁
        clickDrawerHome();

        UiCollection fragment = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/main_frame_layout"));
        UiObject pre_top = fragment.getChildByInstance(new UiSelector().className("android.widget.LinearLayout"), 1);
        UiObject pre_middle = fragment.getChildByInstance(new UiSelector().className("android.widget.LinearLayout"), 2);
        UiObject pre_bottom = fragment.getChildByInstance(new UiSelector().className("android.widget.LinearLayout"), 3);

        //點開drawer
        openDrawer();

        //點擊live view
        clickDrawerLiveView();

        //轉橫屏
        mDevice.setOrientationLeft();

        // Launch Settings
        mDevice.executeShellCommand("am start -n com.android.settings/.Settings");

        // Scroll to WLAN and click
        UiScrollable uiScrollable = new UiScrollable(new UiSelector().scrollable(true));
        uiScrollable.scrollTextIntoView("Wi‑Fi");
        mDevice.findObject(new UiSelector().text("Wi‑Fi")).clickAndWaitForNewWindow();

        // 判斷開關 如果與想要的狀態不同則點擊
        UiObject uiObject = mDevice.findObject(new UiSelector().className("android.widget.Switch"));
        uiObject.click();

        mDevice.pressBack();
        sleep(400);
        mDevice.pressBack();

        mDevice.setOrientationNatural();
        mDevice.unfreezeRotation();

        //點開drawer
        openDrawer();

        //點擊首頁
        clickDrawerHome();

        fragment = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/main_frame_layout"));
        UiObject top = fragment.getChildByInstance(new UiSelector().className("android.widget.LinearLayout"), 1);
        UiObject middle = fragment.getChildByInstance(new UiSelector().className("android.widget.LinearLayout"), 2);
        UiObject bottom = fragment.getChildByInstance(new UiSelector().className("android.widget.LinearLayout"), 3);

        Assert.assertEquals(true, ((pre_top.getBounds().top == top.getBounds().top) || (pre_top.getBounds().bottom == top.getBounds().bottom)));
        Assert.assertEquals(true, ((pre_middle.getBounds().top == middle.getBounds().top) || (pre_middle.getBounds().bottom == middle.getBounds().bottom)));
        Assert.assertEquals(true, ((pre_bottom.getBounds().top == bottom.getBounds().top) || (pre_bottom.getBounds().bottom == bottom.getBounds().bottom)));
    }

    //TODO issue60_22
    @Test
    public void issue60_22() throws UiObjectNotFoundException, RemoteException, IOException {
        //點開drawer
        openDrawer();

        //點擊瀏覽
        clickDrawerBrowser();

        UiScrollable browserRecycler = getMainRecyclerView();
        UiObject item1 = getItem(browserRecycler, 0);
        UiObject item2, item3, item4;
        //Log.e("60 issue 07", ""+browserRecycler.getChildCount(new UiSelector().className("android.widget.FrameLayout")));
//        if (browserRecycler.getChildCount(new UiSelector().className("android.widget.FrameLayout")) < 4){
//            return;
//        }
//        item2 = browserRecycler.getChildByInstance(new UiSelector().className("android.widget.FrameLayout"), 1);
//        item3 = browserRecycler.getChildByInstance(new UiSelector().className("android.widget.FrameLayout"), 2);
//        item4 = browserRecycler.getChildByInstance(new UiSelector().className("android.widget.FrameLayout"), 3);
//
//        //紀錄操作前的X, Y座標
//        int centerX1, centerY1, centerX2, centerY2, centerX3, centerY3, centerX4, centerY4;
//        centerX1 = item1.getBounds().centerX();
//        centerY1 = item1.getBounds().centerY();
//        centerX2 = item2.getBounds().centerX();
//        centerY2 = item2.getBounds().centerY();
//        centerX3 = item3.getBounds().centerX();
//        centerY3 = item3.getBounds().centerY();
//        centerX4 = item4.getBounds().centerX();
//        centerY4 = item4.getBounds().centerY();

        item1.clickAndWaitForNewWindow();

        //轉橫屏
        mDevice.setOrientationLeft();

        // Launch Settings
        mDevice.executeShellCommand("am start -n com.android.settings/.Settings");

        // Scroll to WLAN and click
        UiScrollable uiScrollable = new UiScrollable(new UiSelector().scrollable(true));
        uiScrollable.scrollTextIntoView("Wi‑Fi");
        mDevice.findObject(new UiSelector().text("Wi‑Fi")).clickAndWaitForNewWindow();

        // 判斷開關 如果與想要的狀態不同則點擊
        UiObject uiObject = mDevice.findObject(new UiSelector().className("android.widget.Switch"));
        uiObject.click();

        mDevice.pressBack();
        sleep(400);
        mDevice.pressBack();

        mDevice.setOrientationNatural();
        mDevice.unfreezeRotation();

        //點開drawer
        openDrawer();

        //點擊瀏覽
        clickDrawerBrowser();

        //點開drawer
        openDrawer();

        //點擊瀏覽
        clickDrawerBrowser();
        browserRecycler = getMainRecyclerView();
        checkComposing(browserRecycler);
//        item1 = browserRecycler.getChildByInstance(new UiSelector().className("android.widget.FrameLayout"), 0);
//        item2 = browserRecycler.getChildByInstance(new UiSelector().className("android.widget.FrameLayout"), 1);
//        item3 = browserRecycler.getChildByInstance(new UiSelector().className("android.widget.FrameLayout"), 2);
//        item4 = browserRecycler.getChildByInstance(new UiSelector().className("android.widget.FrameLayout"), 3);
//
//        Assert.assertEquals(true, ((centerX1==item1.getBounds().centerX()) && (centerY1==item1.getBounds().centerY())));
//        Assert.assertEquals(true, ((centerX2==item2.getBounds().centerX()) && (centerY2==item2.getBounds().centerY())));
//        Assert.assertEquals(true, ((centerX3==item3.getBounds().centerX()) && (centerY3==item3.getBounds().centerY())));
//        Assert.assertEquals(true, ((centerX4==item4.getBounds().centerX()) && (centerY4==item4.getBounds().centerY())));

        mDevice.unfreezeRotation();
    }
}