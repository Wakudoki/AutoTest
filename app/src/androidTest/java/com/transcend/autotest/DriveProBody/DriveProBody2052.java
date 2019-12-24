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
import android.support.test.uiautomator.UiWatcher;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DriveProBody2052 extends DriveProBodyAbstract{

    static private int browser_types = 3;  //瀏覽頁面裡的內容總數，即影片、緊急錄影、照片(不包括全部檔案)
    static private int browser_tabs = 2;   //瀏覽頁面裡的tab總數，即本地、遠端
    static int Phone = 0;
    static int Pad = 1;
    int Testing_device = Phone;

    @Before
    public void setup() throws UiObjectNotFoundException {
        super.setup();
    }

    //TODO issue2052_01
    @Test
    public void issue2052_01() throws UiObjectNotFoundException, RemoteException {
        //打開menu
        openDrawer();

        //點擊live view
        clickDrawerLiveView();

        UiObject liveViewObj = new UiObject(new UiSelector().className("android.webkit.WebView"));

        Assert.assertEquals(true, liveViewObj.getBounds().width() <= mDevice.getDisplayWidth());
        Assert.assertEquals(true, liveViewObj.getBounds().height() <= mDevice.getDisplayHeight());

        //轉橫屏
        mDevice.setOrientationLeft();

        liveViewObj = new UiObject(new UiSelector().className("android.webkit.WebView"));

        Assert.assertEquals(true, liveViewObj.getBounds().width() <= mDevice.getDisplayWidth());
        Assert.assertEquals(true, liveViewObj.getBounds().height() <= mDevice.getDisplayHeight());

        sleep(1000);
        mDevice.setOrientationNatural();
        mDevice.unfreezeRotation();
    }

    //TODO issue2052_02
    @Test
    public void issue2052_02() throws UiObjectNotFoundException {
        //打開menu
        openDrawer();

        //點擊home
        clickDrawerHome();

        UiObject liveViewObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/live_streaming"));
        liveViewObj.clickAndWaitForNewWindow();

        sleep(3000);

        UiObject alertObj = new UiObject(new UiSelector().resourceId("android:id/progress"));
        while(alertObj.exists()){
            //等待dialog消失
        }

        UiObject webViewObj = new UiObject(new UiSelector().className("android.webkit.WebView"));
        Assert.assertEquals(true, webViewObj.exists());

        //打開menu
        openDrawer();

        //點擊home
        clickDrawerHome();

        UiObject browserObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/browser"));
        browserObj.clickAndWaitForNewWindow();
        waitForDialog();

        UiScrollable browserRecycler = getMainRecyclerView();
        Assert.assertEquals(true, browserRecycler.exists());
    }

    //TODO issue2052_04
    @Test
    public void issue2052_04() throws UiObjectNotFoundException {
        //打開menu
        openDrawer();

        //點擊瀏覽
        clickDrawerBrowser();

        //紀錄畫面上所有影片
        UiScrollable recycler = getMainRecyclerView();
        int videoCount = getItemCount(recycler);
        sleep(200);

        //紀錄畫面上所有照片
        clickTabItem(2);
        recycler = getMainRecyclerView();
        int photoCount = getItemCount(recycler);
        sleep(200);

        //紀錄畫面上所有音檔
        clickTabItem(3);
        recycler = getMainRecyclerView();
        int soundCount = getItemCount(recycler);
        sleep(200);

        //比對先前的檔案
        clickTabItem(0);
        recycler = getMainRecyclerView();
        Assert.assertEquals(videoCount, getItemCount(recycler));
        sleep(200);

        //比對先前的檔案
        clickTabItem(3);
        recycler = getMainRecyclerView();
        Assert.assertEquals(soundCount, getItemCount(recycler));
        sleep(200);

        //比對先前的檔案
        clickTabItem(2);
        recycler = getMainRecyclerView();
        Assert.assertEquals(photoCount, getItemCount(recycler));
    }

    //TODO issue2052_05
    @Test
    public void issue2052_05() throws UiObjectNotFoundException {

        for (int i = 0; i < 3; i++) {
            //打開menu
            openDrawer();

            //點擊瀏覽
            clickDrawerBrowser();

            UiObject alertObj = new UiObject(new UiSelector().resourceId("android:id/progress"));
            while(alertObj.exists()){
                //等待dialog消失
            }

            //打開menu
            openDrawer();

            //點擊首頁
            clickDrawerHome();
        }

        UiObject liveViewObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/live_streaming"));
        liveViewObj.clickAndWaitForNewWindow();

        UiObject alertObj = new UiObject(new UiSelector().resourceId("android:id/progress"));
        while(alertObj.exists()){
            //等待dialog消失
        }

        UiObject webViewObj = new UiObject(new UiSelector().className("android.webkit.WebView"));
        Assert.assertEquals(true, webViewObj.exists());
    }

    //TODO issue2052_08
    @Test
    public void issue2052_08() throws UiObjectNotFoundException {
        //打開menu
        openDrawer();

        //點擊瀏覽
        clickDrawerBrowser();

        //圖片tab
        clickTabItem(2);

        //進到第一個物件
        UiScrollable recycler = getMainRecyclerView();
        UiObject item = getItem(recycler, 0);
        item.clickAndWaitForNewWindow();

        //試滑後滑往下一部影片
        for (int i = 0; i < 10; i++){
            UiObject titleObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/toolbar_title"));
            String pre_title = titleObj.getText();

            //輕輕左滑一下
            mDevice.swipe(mDevice.getDisplayWidth() / 2, mDevice.getDisplayHeight() / 2,
                    (mDevice.getDisplayWidth() / 2) - 90, mDevice.getDisplayHeight() / 2, 10);

            //判斷名稱有無改變
            titleObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/toolbar_title"));
            Assert.assertEquals(pre_title, titleObj.getText());

            swipeToNextViewPager();
        }

        mDevice.pressBack();
        waitForDialog();

        //打開menu
        openDrawer();

        //點擊我的儲存
        clickDrawerMyStorage();

        //圖片tab
        clickTabItem(2);

        //進到第一個物件
        UiScrollable storageRecycler =getMainRecyclerView();
        item = getItem(storageRecycler, 0);
        item.clickAndWaitForNewWindow();

        //試滑後滑往下一部影片
        for (int i = 0; i < 10; i++){
            UiObject titleObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/toolbar_title"));
            String pre_title = titleObj.getText();

            //輕輕左滑一下
            mDevice.swipe(mDevice.getDisplayWidth() / 2, mDevice.getDisplayHeight() / 2,
                    (mDevice.getDisplayWidth() / 2) - 90, mDevice.getDisplayHeight() / 2, 10);

            //判斷名稱有無改變
            titleObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/toolbar_title"));
            Assert.assertEquals(pre_title, titleObj.getText());

            swipeToNextViewPager();
        }

        mDevice.pressBack();
        waitForDialog();
    }

    //TODO issue2052_10
    @Test
    public void issue2052_10() throws UiObjectNotFoundException {
        //打開menu
        openDrawer();

        //點擊我的儲存
        clickDrawerMyStorage();

        //圖片tab
        clickTabItem(2);

        //進到第一個物件
        UiScrollable storageRecycler = getMainRecyclerView();
        UiObject item = getItem(storageRecycler, 0);
        item.clickAndWaitForNewWindow();

        for (int i = 0; i < 10; i++){
            int random = (int) (Math.random()*2);

            //隨機滑動
            switch(random) {
                case 0:
                    swipeToNextViewPager();
                    break;
                case 1:
                    swipeToPrevViewPager();
                    break;
            }

            sleep(1500);

            //圖片有存在，否則錯誤
            UiObject image = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/imageView"));
            Assert.assertEquals(true, image.exists());
        }

        mDevice.pressBack();
        waitForDialog();

        //打開menu
        openDrawer();

        //點擊瀏覽
        clickDrawerBrowser();

        //圖片tab
        clickTabItem(2);

        //進到第一個物件
        UiScrollable browserRecycler = getMainRecyclerView();
        item = getItem(browserRecycler, 0);
        item.clickAndWaitForNewWindow();

        for (int i = 0; i < 10; i++){
            int random = (int) (Math.random()*2);

            //隨機滑動
            switch(random) {
                case 0:
                    swipeToNextViewPager();
                    break;
                case 1:
                    swipeToPrevViewPager();
                    break;
            }

            sleep(1500);

            //圖片會有存在，否則錯誤
            UiObject image = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/imageView"));
            Assert.assertEquals(true, image.exists());
        }

        mDevice.pressBack();
        waitForDialog();
    }

    //TODO issue2052_13
    @Test
    public void issue2052_13() throws UiObjectNotFoundException, RemoteException {
        //打開drawer
        openDrawer();

        //點擊home
        clickDrawerHome();

        //轉橫屏
        mDevice.setOrientationLeft();

        //紀錄兩者的位置
        UiObject liveViewObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/live_streaming"));
        UiObject browserObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/browser"));
        int [] liveViewCenter = {liveViewObj.getBounds().centerX(), liveViewObj.getBounds().centerY()};
        int [] browserCenter = {browserObj.getBounds().centerX(), browserObj.getBounds().centerY()};
        liveViewObj.clickAndWaitForNewWindow();

        sleep(3000);

        //拉出drawer
        mDevice.swipe(0, mDevice.getDisplayHeight() / 2,
                        mDevice.getDisplayWidth() / 2, mDevice.getDisplayHeight() / 2, 10);
        //點擊首頁
        clickDrawerHome();

        //判斷操作前後的位置
        liveViewObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/live_streaming"));
        browserObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/browser"));
        Assert.assertEquals(true, (liveViewObj.getBounds().centerX() == liveViewCenter[0]) && (liveViewObj.getBounds().centerY() == liveViewCenter[1]));
        Assert.assertEquals(true, (browserObj.getBounds().centerX() == browserCenter[0]) && (browserObj.getBounds().centerY() == browserCenter[1]));

        mDevice.setOrientationNatural();
        mDevice.unfreezeRotation();
    }

    //TODO issue2052_14
    @Test
    public void issue2052_14() throws UiObjectNotFoundException {
        //打開drawer
        openDrawer();

        //點擊home
        clickDrawerHome();

        UiObject liveViewObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/live_streaming"));
        UiObject browserObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/browser"));

        //反覆從首頁進到live view or browser，並判斷是否正確進到該頁面
        for (int i = 0; i < 5; i++){
            //進到live view
            liveViewObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/live_streaming"));
            liveViewObj.clickAndWaitForNewWindow();
            UiObject alertObj = new UiObject(new UiSelector().resourceId("android:id/progress"));
            while(alertObj.exists()){
                //等待dialog消失
            }
            UiObject webView = new UiObject(new UiSelector().className("android.widget.Image"));
            Assert.assertEquals(true, webView.exists());

            sleep(3000);

            //回首頁
            openDrawer();
            clickDrawerHome();

            //進到browser
            browserObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/browser"));
            browserObj.clickAndWaitForNewWindow();
            alertObj = new UiObject(new UiSelector().resourceId("android:id/progress"));
            while(alertObj.exists()){
                //等待dialog消失
            }
            UiScrollable browserRecycler = getMainRecyclerView();
            Assert.assertEquals(true, browserRecycler.exists());

            //回首頁
            openDrawer();
            clickDrawerHome();

            sleep(3000);    //過於頻繁進出會導致檔案無法讀取
        }
    }

    //TODO issue2052_15
    @Test
    public void issue2052_15() throws UiObjectNotFoundException {
        //打開drawer
        openDrawer();

        //點擊瀏覽
        clickDrawerBrowser();

        //打開drawer
        openDrawer();

        //點擊home
        clickDrawerHome();

        //進到live view
        UiObject liveViewObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/live_streaming"));
        liveViewObj.clickAndWaitForNewWindow();

        UiObject alertObj = new UiObject(new UiSelector().resourceId("android:id/progress"));
        while(alertObj.exists()){
            //等待dialog消失
        }

        UiObject webView = new UiObject(new UiSelector().className("android.widget.Image"));
        Assert.assertEquals(true, webView.exists());
    }

    //TODO issue2052_17
    @Test
    public void issue2052_17() throws UiObjectNotFoundException {
        //打開drawer
        openDrawer();

        //點擊設定
        clickDrawerSetting();

        UiObject alertObj = new UiObject(new UiSelector().resourceId("android:id/progress"));
        while(alertObj.exists()){
            //等待dialog消失
        }

        //滑至底部
        UiScrollable recycler = new UiScrollable(new UiSelector().resourceId("com.transcend.bcr:id/settings_recycler_view"));
        recycler.scrollToEnd(3, 10);

        //點擊裝置
        recycler = new UiScrollable(new UiSelector().resourceId("com.transcend.bcr:id/settings_recycler_view"));
        UiObject device = recycler.getChildByInstance(new UiSelector().resourceId("com.transcend.bcr:id/settingsStatus"), 4);
        String deviceText = device.getText();
        device.clickAndWaitForNewWindow();

        //判斷現在裝置，必須重複點擊該裝置
        UiObject dpb20 = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/device_two"));
        UiObject dpb52 = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/device_one"));
        UiObject dpb20se = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/device_three"));
        if (deviceText.equals("DPB20"))
            dpb20.clickAndWaitForNewWindow();
        else if (deviceText.equals("DPB52"))
            dpb52.clickAndWaitForNewWindow();
        else
            dpb20se.clickAndWaitForNewWindow();

        alertObj = new UiObject(new UiSelector().resourceId("android:id/progress"));
        while(alertObj.exists()){
            //等待dialog消失
        }

        //判斷是否正確會到首頁
        UiObject liveViewObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/live_streaming"));
        UiObject browserObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/browser"));
        Assert.assertEquals(true, liveViewObj.exists());
        Assert.assertEquals(true, browserObj.exists());
    }

    //TODO issue2052_22
    @Test
    public void issue2052_22() throws UiObjectNotFoundException {
        //打開drawer
        openDrawer();

        //點擊設定
        clickDrawerSetting();

        //紀錄label字串
        UiScrollable recycler = new UiScrollable(new UiSelector().resourceId("com.transcend.bcr:id/settings_recycler_view"));
        recycler.scrollToBeginning(3, 10);
        UiObject labelObj = recycler.getChildByInstance(new UiSelector().resourceId("com.transcend.bcr:id/settingsStatus"), 2);
        String label = labelObj.getText();
        labelObj.clickAndWaitForNewWindow();

        //驗證字串
        UiObject editTextObj = new UiObject(new UiSelector().className("android.widget.EditText"));
        Assert.assertEquals(label, editTextObj.getText());

        UiObject cancel = new UiObject(new UiSelector().resourceId("android:id/button2"));
        cancel.click();

        //下滑至底
        recycler.scrollToEnd(3, 10);

        recycler = new UiScrollable(new UiSelector().resourceId("com.transcend.bcr:id/settings_recycler_view"));
        UiObject wifiObj = recycler.getChildByInstance(new UiSelector().resourceId("com.transcend.bcr:id/settingsStatus"), 2);
        String [] wifiText = wifiObj.getText().split(":");
        String ssid = wifiText[1];
        wifiObj.clickAndWaitForNewWindow();

        //驗證字串
        editTextObj = new UiObject(new UiSelector().className("android.widget.EditText"));
        Assert.assertEquals(ssid, editTextObj.getText());

        mDevice.pressBack();
    }

    //TODO issue2052_32
    @Test
    public void issue2052_32() throws UiObjectNotFoundException, RemoteException {
        //打開drawer
        openDrawer();

        //點擊home
        clickDrawerHome();

        UiObject liveViewObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/live_streaming"));
        liveViewObj.clickAndWaitForNewWindow();

        //轉橫屏，紀錄toolbar存在與否
        mDevice.setOrientationLeft();
        UiObject toolbarObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/main_toolbar"));
        boolean isExist = toolbarObj.exists();

        //滑出drawer
        mDevice.swipe(0, mDevice.getDisplayHeight() / 2,
                            mDevice.getDisplayWidth() / 2, mDevice.getDisplayHeight() / 2, 10);

        //點擊home
        clickDrawerHome();

        //先轉豎屏，再轉橫屏
        mDevice.setOrientationNatural();
        mDevice.setOrientationLeft();

        //進到liveView
        liveViewObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/live_streaming"));
        liveViewObj.clickAndWaitForNewWindow();

        //判斷toolbar存在與否
        toolbarObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/main_toolbar"));
        Assert.assertEquals(isExist, toolbarObj.exists());

        mDevice.setOrientationNatural();
        mDevice.unfreezeRotation();
    }
}
