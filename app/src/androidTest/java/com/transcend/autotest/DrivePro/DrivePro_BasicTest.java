package com.transcend.autotest.DrivePro;

import android.os.RemoteException;
import android.support.test.uiautomator.UiCollection;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.util.Log;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DrivePro_BasicTest extends DriveProAbstract {

    int waiting_time = 1000;

    @Before
    public void setup(){
        super.setup();
    }

    @Test
    public void Browser() throws UiObjectNotFoundException {
        openDrawer();
        clickDrawerRemote();

        int type = 0;
        while(type < Remote_Browser_Types){
            clickTabItem(type);

            UiScrollable recyclerView = getMainRecyclerView();
            if (getItemCount(recyclerView) > 0){
                for (int i = 0; i < getItemCount(recyclerView); i++){
                    UiObject item = getItem(recyclerView, i);
                    String title = getItemTitle(recyclerView, i).getText();
                    if (item.exists()){
                        item.click();

                        //檢查示圖標題與物件標題是否一致
                        String toolbarTitle = getSingleViewTitle();
                        String [] toolbarTitleSplit = toolbarTitle.split("\n");
                        Assert.assertEquals(true, toolbarTitleSplit[0].equals(title));

                        if (type == Remote_Browser_Types - 1)
                            Assert.assertEquals(true, isAtImageView());
                        else
                            Assert.assertEquals(true, isAtVideoView());

                        //測試返回鍵&左上角返回鍵
                        if (i % 2 == 0) {
                            UiObject toggleBack = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/toggle_btn"));
                            toggleBack.click();
                            sleep(waiting_time);
                        }
                        else {
                            mDevice.pressBack();
                            sleep(waiting_time);
                        }
                    }
                }
            }
            else{
                sleep(1000);
                //無檔案的圖示必須存在
                UiObject noItemViewObj = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/empty_view"));
                Assert.assertEquals(true, noItemViewObj.exists());
            }

            type++;
        }

        openDrawer();
        clickDrawerMyStorage();
        type = 0;
        while(type < Local_Browser_Types){
            clickTabItem(type);

            UiScrollable recyclerView = getMainRecyclerView();
            if (getItemCount(recyclerView) > 0){
                for (int i = 0; i < getItemCount(recyclerView); i++){
                    UiObject item = getItem(recyclerView, i);
                    if (item.exists()){
                        item.click();

                        if (type == Local_Browser_Types - 1)
                            Assert.assertEquals(true, isAtImageView());
                        else
                            Assert.assertEquals(true, isAtVideoView());

                        //測試返回&左上角返回鍵
                        if (i % 2 == 0) {
                            UiObject toggleBack = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/toggle_btn"));
                            toggleBack.click();
                            sleep(waiting_time);
                        }
                        else {
                            mDevice.pressBack();
                            sleep(waiting_time);
                        }
                    }
                }
            }
            else{
                //無檔案的圖示必須存在
                UiObject noItemViewObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/empty_view"));
                Assert.assertEquals(false, noItemViewObj.exists());
            }
            type++;
        }
    }

    @Test
    public void SingleView_Image() throws UiObjectNotFoundException, RemoteException {
        int type = 0;
        while(type < 2){
            openDrawer();
            if (type == 0) {    //遠端
                clickDrawerRemote();
                clickTabItem(Remote_Browser_Types - 1);
            }
            else{   //本機
                clickDrawerMyStorage();
                clickTabItem(Local_Browser_Types - 1);
            }

            //////////測試下載 & 旋轉//////////
            UiScrollable recyclerView = getMainRecyclerView();
            int testCount = getItemCount(recyclerView);
            if (testCount > 3)
                testCount = 3;
            for (int i = 0; i < testCount; i++){
                UiObject item = getItem(recyclerView, i);
                if (item.exists()) {
                    item.click();

                    if (type == 0) {
                        //下載後檢查圖片個數
                        downloadItemInImageView();
                        checkViewPagerNums();
                    }

                    //旋轉後檢查名稱、圖片個數
                    String title = getSingleViewTitle();
                    mDevice.setOrientationLeft();
                    sleep(500);
                    checkViewPagerNums();
                    Assert.assertEquals(title, getSingleViewTitle());
                    mDevice.setOrientationNatural();
                    mDevice.setOrientationRight();
                    sleep(500);
                    checkViewPagerNums();
                    Assert.assertEquals(title, getSingleViewTitle());
                    mDevice.setOrientationNatural();
                    mDevice.unfreezeRotation();
                    Assert.assertEquals(title, getSingleViewTitle());

                    //點擊返回鍵 or 左上角返回
                    if (i % 2 == 0){
                        UiObject toggleBack = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/toggle_btn"));
                        toggleBack.click();
                    }
                    else
                        mDevice.pressBack();

                    waitForDialog();
                }
            }
            //////////測試下載 & 旋轉 - 結束//////////
            //////////測試滑動 & 刪除//////////
            recyclerView = getMainRecyclerView();
            if (getItemCount(recyclerView) > 0){
                UiObject item = getItem(recyclerView, 0);
                if (item.exists()){
                    item.click();

                    //返回後再次進到View，執行滑動並檢查
                    mDevice.pressBack();
                    item = getItem(recyclerView, 0);
                    int count = getItemCount(recyclerView);
                    item.click();
                    for (int i = 0; i < count; i++){
                        if (i < (count/3)) {
                            //輕滑一下，檢查名稱
                            String title = getSingleViewTitle();
                            lightSwipeToNextViewPager();
                            Assert.assertEquals(title, getSingleViewTitle());
                            swipeToNextViewPager(); //往下滑一張圖片，名稱不能一致
                            Assert.assertEquals(false, title.equals(getSingleViewTitle()));
                        }
                        else{   //連續滑動
                            String title = getSingleViewTitle();
                            swipeToNextViewPager(); //往下滑一張圖片，名稱不能一致
                            Assert.assertEquals(false, title.equals(getSingleViewTitle()));
                        }
                    }

                    //返回後再次進到View，執行連續刪除並檢查
                    mDevice.pressBack();
                    recyclerView.swipeUp(swipe_times);  //盡量滑至底部
                    recyclerView = getMainRecyclerView();
                    item = getItem(recyclerView, 0);
                    count = getItemCount(recyclerView);
                    item.click();
                    for (int i = 0; i < count; i++){
                        //試著刪除檔案，若回傳false則表示刪到沒有檔案而回到瀏覽頁面；true表示刪除成功
                        boolean stillAtView = deleteItemInImageView();
                        if (!stillAtView)
                            return;
                    }
                }
            }
            else{
                sleep(1000);
                //有檔案存在時，無檔案的圖示不能存在，反之亦然
                UiObject noItemViewObj = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/empty_view"));
                Assert.assertEquals(true, noItemViewObj.exists());
            }
            //////////測試滑動 & 刪除 - 結束//////////

            if (isAtImageView())
                mDevice.pressBack();

            type ++;
        }
    }

    @Test
    public void SingleView_Video() throws UiObjectNotFoundException, RemoteException {
        openDrawer();
        clickDrawerRemote();

        //////////測試能否播放//////////
        UiScrollable recyclerView = getMainRecyclerView();
        int testCount = getItemCount(recyclerView);
        if (testCount > 3) testCount = 3;
        for (int i = 0; i < testCount; i++){
            UiObject item = getItem(recyclerView, i);
            if (item.exists()) {
                item.click();

                //檢查播放、暫停鍵
                UiObject play = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/vlc_button_play_pause"));
                String [] currentTimeSplit = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/video_current_time")).getText().split(":");
                String [] durationSplit = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/video_total_time")).getText().split(":");
                int currentTime = Integer.parseInt(currentTimeSplit[0])*60 + Integer.parseInt(currentTimeSplit[1]);
                int duration = Integer.parseInt(durationSplit[0])*60 + Integer.parseInt(durationSplit[1]);
                //一開始會自動撥放，比較前後時間
                sleep(2000);
                currentTimeSplit = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/video_current_time")).getText().split(":");
                int postCurTime = Integer.parseInt(currentTimeSplit[0])*60 + Integer.parseInt(currentTimeSplit[1]);
                if (postCurTime > 0)
                    Assert.assertEquals(true, postCurTime >= currentTime);

                //此處等待執行的時間太久(未知原因)，此處先不測試暫停
//                currentTimeSplit = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/video_current_time")).getText().split(":");
//                currentTime = Integer.parseInt(currentTimeSplit[0])*60 + Integer.parseInt(currentTimeSplit[1]);
//                //暫停，前後時間應該一致
//                play.click();
//                sleep(2000);
//                durationSplit = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/video_current_time")).getText().split(":");
//                postCurTime = Integer.parseInt(durationSplit[0])*60 + Integer.parseInt(durationSplit[1]);
//                if (postCurTime > 0)
//                    Assert.assertEquals(currentTime, postCurTime);

                //總時間恆大於等於現在時間
                if (duration != currentTime)
                    Assert.assertEquals(true, duration > currentTime);
                Log.e("DrivePro_BugList", "duration & current: " + duration + ", " + currentTime);

                mDevice.pressBack();
            }
        }
        //////////測試能否播放 - 結束//////////

        //////////測試上、下一首 & 旋轉//////////
        List<String> fileList = new ArrayList<>();
        recyclerView = getMainRecyclerView();
        int count = getItemCount(recyclerView);
        for (int i = 0; i < count; i++){
            fileList.add(getItemTitle(recyclerView, i).getText());
        }
        if (count > 0) {
            UiObject item = getItem(recyclerView, 0);
            item.click();

            int position = 0;
            for (int i = 0; i < execute_times; i++){
                mDevice.setOrientationNatural();
                checkViewPagerNums();
                mDevice.setOrientationNatural();
                checkViewPagerNums();
                mDevice.setOrientationLeft();
                checkViewPagerNums();
                mDevice.setOrientationNatural();
                mDevice.unfreezeRotation();

                UiObject prev = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/vlc_button_previous"));
                UiObject next = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/vlc_button_next"));
                int random = (int)(Math.random() * 2);
                if (position == 0)  //第一部影片無法再上一首
                    random = 1;
                else if (position == fileList.size())   //列表的最後一部不一定是整體的最後一部影片，但保險起見作防呆保護
                    random = 0;

                if (random == 0){
                    position--;
                    prev.click();
                    String [] title = getSingleViewTitle().split("\n");
                    Assert.assertEquals(fileList.get(position), title[0]);
                }
                else{
                    position++;
                    next.click();
                    String [] title = getSingleViewTitle().split("\n");
                    Assert.assertEquals(fileList.get(position), title[0]);
                }
            }
        }
        mDevice.pressBack();
        waitForDialog();
        //////////測試上、下一首 & 旋轉 - 結束//////////
        //////////測試下載&刪除//////////
        recyclerView = getMainRecyclerView();
        if (getItemCount(recyclerView) > 0){
            UiObject item = getItem(recyclerView, 0);
            item.click();
            sleep(500);

            for (int i = 0; i < execute_times; i++){
                downloadItemInVideoView();
                checkViewPagerNums();
                makeVideoControlVisible();
                UiObject next = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/vlc_button_next"));
                next.click();
            }
            sleep(500);
            mDevice.pressBack();
            waitForDialog();

            recyclerView.swipeDown(recyclerView.getMaxSearchSwipes());
            item = getItem(recyclerView, 0);
            item.click();
            sleep(500);
            for (int i = 0; i < execute_times; i++){
                boolean stillAtView =  deleteItemInVideoView(); //是否刪到沒東西而回到瀏覽頁面
                if (!stillAtView)
                    return;
                checkViewPagerNums();
            }
            mDevice.pressBack();
            waitForDialog();
        }
        //////////測試下載&刪除 - 結束//////////
    }

    @Test
    public void Other_Home() throws UiObjectNotFoundException {
        for (int i = 0; i < execute_times; i++) {
            //點擊首頁
            openDrawer();
            clickDrawerHome();

            //檢查排版
            UiObject liveView = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/liveViewPortal"));
            UiObject browser = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/browserPortal"));
            Assert.assertEquals(true, Math.abs((liveView.getBounds().bottom - liveView.getBounds().top) - (browser.getBounds().bottom - browser.getBounds().top)) < 50);

            //點擊即時影像
            liveView.clickAndWaitForNewWindow();
            UiCollection webViewLayout = new UiCollection(new UiSelector().className("android.webkit.WebView"));
            webViewLayout.waitForExists(10000);
            sleep(500);

            //回到首頁並點擊瀏覽
            openDrawer();
            clickDrawerHome();
            browser.clickAndWaitForNewWindow();
            waitForDialog();
            if (!getMainRecyclerView().exists())
                Assert.assertEquals(true, new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/empty_view")).exists());
        }
    }

    @Test
    public void Other_Setting() throws UiObjectNotFoundException {
        openDrawer();
        clickDrawerSetting();

        UiScrollable settingRecycler = new UiScrollable(new UiSelector().resourceId("com.transcend.cvr:id/setting_recycleview"));

        //先用此方法滑動到有該文字的地方
        UiObject wifiTitleObj = settingRecycler.getChildByText(new UiSelector().resourceId("com.transcend.cvr:id/settingsSubtitle"), "Wi-Fi", true);
        //再取得其parent
        UiObject wifiParentObj = getParentFromChild(
                                    settingRecycler,
                                    new UiSelector().className("android.widget.LinearLayout"), //尋找之父親的Selector
                                    new UiSelector().resourceId("com.transcend.cvr:id/settingsSubtitle"), //參照物的Selector
                        "Wi-Fi");   //參照物的值
        //最後取得所需物件
        String wifi = wifiParentObj.getChild(new UiSelector().resourceId("com.transcend.cvr:id/settingsStatus")).getText();

        if (!wifi.equals("SSID:")){    //只顯示SSID:表示設定失敗
            String autoTurnOffScreenTime = "";
            String pixelText = "";

            UiObject pixelTitleObj = settingRecycler.getChildByText(new UiSelector().resourceId("com.transcend.cvr:id/settingsSubtitle"), "解析度", true);
            UiObject pixelParentObj = getParentFromChild(
                    settingRecycler,
                    new UiSelector().className("android.widget.LinearLayout"), //尋找之父親的Selector
                    new UiSelector().resourceId("com.transcend.cvr:id/settingsSubtitle"), //參照物的Selector
                    "解析度");   //參照物的值
            UiObject pixel = pixelParentObj.getChild(new UiSelector().resourceId("com.transcend.cvr:id/settingsStatus"));
            pixel.click();
            waitForDialog();

            UiCollection frame = new UiCollection(new UiSelector().resourceId("com.transcend.cvr:id/custom"));
            int instance = 1;
            //隨機取得關閉螢幕時間的LinearLayout序號
            int random = (int) (Math.random() * 2);
            switch(random){
                case 0:
                    instance = 1;
                    pixelText = "1080P+720P";
                    break;
                case 1:
                    instance = 2;
                    pixelText = "720P+720P";
                    break;
            }
            UiObject clickTarget = frame.getChildByInstance(new UiSelector().className("android.widget.LinearLayout"), instance);
            clickTarget.longClick();
            sleep(500);
            UiObject ok = new UiObject(new UiSelector().resourceId("android:id/button1"));
            ok.click();
            sleep(500);
            waitForDialog();
            Assert.assertEquals(pixelText, pixel.getText());

            UiObject autoTurnOffScreenTitleObj = settingRecycler.getChildByText(new UiSelector().resourceId("com.transcend.cvr:id/settingsSubtitle"), "自動關閉螢幕", true);
            UiObject autoTurnOffScreenParentObj = getParentFromChild(
                    settingRecycler,
                    new UiSelector().className("android.widget.LinearLayout"), //尋找之父親的Selector
                    new UiSelector().resourceId("com.transcend.cvr:id/settingsSubtitle"), //參照物的Selector
                    "自動關閉螢幕");   //參照物的值
            UiObject autoTurnOffScreen = autoTurnOffScreenParentObj.getChild(new UiSelector().resourceId("com.transcend.cvr:id/settingsStatus"));
            autoTurnOffScreen.click();
            waitForDialog();

            frame = new UiCollection(new UiSelector().resourceId("com.transcend.cvr:id/custom"));
            instance = 1;
            //隨機取得關閉螢幕時間的LinearLayout序號
            random = (int) (Math.random() * 3);
            switch(random){
                case 0:
                    instance = 1;
                    autoTurnOffScreenTime = "從不";
                    break;
                case 1:
                    instance = 4;
                    autoTurnOffScreenTime = "1分鐘";
                    break;
                case 2:
                    instance = 5;
                    autoTurnOffScreenTime = "3分鐘";
                    break;
            }
            clickTarget = frame.getChildByInstance(new UiSelector().className("android.widget.LinearLayout"), instance);
            clickTarget.longClick();
            sleep(500);
            ok = new UiObject(new UiSelector().resourceId("android:id/button1"));
            ok.click();
            sleep(500);
            waitForDialog();
            Assert.assertEquals(autoTurnOffScreenTime, autoTurnOffScreen.getText());

            //刷掉程式
            closeAPP(mDevice, "com.transcend.cvr");
            sleep(5000);
            startAPP("com.transcend.cvr");
            sleep(5000);
            //等待進入首頁
            UiObject liveView = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/liveViewPortal"));
            liveView.waitForExists(10000);

            //進到設定
            openDrawer();
            clickDrawerSetting();
            settingRecycler = new UiScrollable(new UiSelector().resourceId("com.transcend.cvr:id/setting_recycleview"));

            pixelTitleObj = settingRecycler.getChildByText(new UiSelector().resourceId("com.transcend.cvr:id/settingsSubtitle"), "解析度", true);
            pixelParentObj = getParentFromChild(
                    settingRecycler,
                    new UiSelector().className("android.widget.LinearLayout"), //尋找之父親的Selector
                    new UiSelector().resourceId("com.transcend.cvr:id/settingsSubtitle"), //參照物的Selector
                    "解析度");   //參照物的值
            pixel = pixelParentObj.getChild(new UiSelector().resourceId("com.transcend.cvr:id/settingsStatus"));
            Assert.assertEquals(pixelText, pixel.getText());

            autoTurnOffScreenTitleObj = settingRecycler.getChildByText(new UiSelector().resourceId("com.transcend.cvr:id/settingsSubtitle"), "自動關閉螢幕", true);
            autoTurnOffScreenParentObj = getParentFromChild(
                    settingRecycler,
                    new UiSelector().className("android.widget.LinearLayout"), //尋找之父親的Selector
                    new UiSelector().resourceId("com.transcend.cvr:id/settingsSubtitle"), //參照物的Selector
                    "自動關閉螢幕");   //參照物的值
            autoTurnOffScreen = autoTurnOffScreenParentObj.getChild(new UiSelector().resourceId("com.transcend.cvr:id/settingsStatus"));
            Assert.assertEquals(autoTurnOffScreenTime, autoTurnOffScreen.getText());
        }

        //目前進到關於裡面會莫名其妙的斷線(設定失敗)，暫時先不進行
//        UiObject aboutTitleObj = settingRecycler.getChildByText(new UiSelector().resourceId("com.transcend.cvr:id/settingsSubtitle"), "關於", true);
//        UiObject aboutParentObj = getParentFromChild(
//                settingRecycler,
//                new UiSelector().className("android.widget.LinearLayout"), //尋找之父親的Selector
//                new UiSelector().resourceId("com.transcend.cvr:id/settingsSubtitle"), //參照物的Selector
//                "關於");   //參照物的值
//        UiObject about = aboutParentObj.getChild(new UiSelector().resourceId("com.transcend.cvr:id/settingsStatus"));
//        about.click();
//        sleep(500);
//
//        UiCollection aboutList = new UiCollection(new UiSelector().resourceId("android:id/list"));
//        UiObject openSource = aboutList.getChildByInstance(new UiSelector().className("android.widget.LinearLayout"), 1);
//        UiObject eula = aboutList.getChildByInstance(new UiSelector().className("android.widget.LinearLayout"), 2);
//        openSource.click();
//        mDevice.pressBack();
//        eula.click();
//        mDevice.pressBack();
//
//        mDevice.pressBack();
//        waitForDialog();
    }

    //目前進到問題回饋，連線必斷!
//    @Test
//    public void Other_Feedback() throws UiObjectNotFoundException {
//        openDrawer();
//        clickDrawerFeedback();
//
//        UiObject sn_info = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/sn_info"));
//        sn_info.click();
//
//        UiObject product_text = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/product_introduction"));
//        product_text.waitForExists(10000);
//
//        Assert.assertEquals("行車記錄器", product_text.getText());
//
//        UiObject done = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/button_confirm"));
//        done.click();
//        sleep(500);
//    }

    protected UiObject getParentFromChild(UiScrollable recyclerView, UiSelector parentSelector, UiSelector observeChildSelector, String searchText){
        int count = recyclerView.getChildCount(new UiSelector().className("android.widget.LinearLayout"));
        for (int i = 0; i < count; i++){
            try {
                UiObject parent = recyclerView.getChildByInstance(parentSelector, i);
                UiObject child = parent.getChild(observeChildSelector);
                if (child.exists() && searchText.equals(child.getText())) {
                    return parent;
                }
            } catch (UiObjectNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }

        return null;
    }
}
