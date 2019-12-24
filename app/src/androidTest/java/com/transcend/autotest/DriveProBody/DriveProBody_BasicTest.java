package com.transcend.autotest.DriveProBody;

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

public class DriveProBody_BasicTest extends DriveProBodyAbstract{

    @Before
    public void setup() throws UiObjectNotFoundException {
        super.setup();
    }

    @Test
    public void Browser() throws UiObjectNotFoundException {
        openDrawer();
        clickDrawerBrowser();

        int type = 0;
        while(type < Remote_Browser_Types.values().length){
            clickTabItem(type);

            UiScrollable recyclerView = getMainRecyclerView();
            if (getItemCount(recyclerView) > 0){
                for (int i = 0; i < getItemCount(recyclerView); i++){
                    UiObject item = getItem(recyclerView, i);
                    UiObject titleObj = getItemTitle(recyclerView, i);
                    String title = null;
                    if (titleObj != null)
                        title = titleObj.getText();
                    if (item.exists()){
                        item.click();

                        if (title != null) {
                            //檢查示圖標題與物件標題是否一致
                            String toolbarTitle = getSingleViewTitle();
                            String[] toolbarTitleSplit = toolbarTitle.split("\n");
                            Assert.assertEquals(true, toolbarTitleSplit[0].equals(title));
                        }

                        if (type == Remote_Browser_Types.Video.ordinal())
                            Assert.assertEquals(true, isAtVideoView());
                        else if (type == Remote_Browser_Types.Image.ordinal())
                            Assert.assertEquals(true, isAtImageView());
                        else if (type == Remote_Browser_Types.Audio.ordinal())
                            Assert.assertEquals(true, isAtMusicView());

                        //測試返回鍵&左上角返回鍵
                        if (i % 2 == 0) {
                            UiObject toggleBack = new UiObject(new UiSelector().className("android.widget.ImageButton"));
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
                UiObject noItemViewObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/empty_view"));
                Assert.assertEquals(true, noItemViewObj.exists());
            }

            type++;
        }

        openDrawer();
        clickDrawerMyStorage();

        type = 0;
        while(type < Local_Browser_Types.values().length){
            clickTabItem(type);

            UiScrollable recyclerView = getMainRecyclerView();
            if (getItemCount(recyclerView) > 0){
                for (int i = 0; i < getItemCount(recyclerView); i++){
                    UiObject item = getItem(recyclerView, i);
                    if (item.exists()){
                        item.click();

                        if (type == Local_Browser_Types.Video.ordinal() || type == Local_Browser_Types.Emergency_Video.ordinal())
                            Assert.assertEquals(true, isAtVideoView());
                        else if (type == Local_Browser_Types.Image.ordinal())
                            Assert.assertEquals(true, isAtImageView());
                        else if (type == Local_Browser_Types.Audio.ordinal())
                            Assert.assertEquals(true, isAtMusicView());

                        //測試返回&左上角返回鍵
                        if (i % 2 == 0) {
                            UiObject toggleBack = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/toggle_btn"));
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
    public void SingleView_Video() throws UiObjectNotFoundException, RemoteException {
        openDrawer();
        clickDrawerBrowser();

        //////////測試能否播放//////////
        UiScrollable recyclerView = getMainRecyclerView();
        for (int i = 0; i < getItemCount(recyclerView); i++){
            UiObject item = getItem(recyclerView, i);
            if (item.exists()) {
                item.click();

                //檢查播放、暫停鍵
                UiObject play = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_play"));
                play.click();
                sleep(3500);

                makeVideoControlVisible();
                UiObject currentObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_current_time"));
                UiObject durationObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_duration"));
                durationObj.waitForExists(10000);

                String [] currentTimeSplit = currentObj.getText().split(":");
                String [] durationSplit = durationObj.getText().split(":");
                int currentTime = Integer.parseInt(currentTimeSplit[0])*60*60 + Integer.parseInt(currentTimeSplit[1])*60 + Integer.parseInt(currentTimeSplit[2]);
                int duration = Integer.parseInt(durationSplit[0])*60*60 + Integer.parseInt(durationSplit[1])*60 + Integer.parseInt(durationSplit[2]);

                int count = 0;
                while(duration == 0 && count < 100){   //讀取影片中
                    makeVideoControlVisible();
                    durationObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_duration"));
                    durationObj.waitForExists(10000);
                    durationSplit = durationObj.getText().split(":");
                    duration = Integer.parseInt(durationSplit[0])*60*60 + Integer.parseInt(durationSplit[1])*60 + Integer.parseInt(durationSplit[1]);
                    count++;
                }

                makeVideoControlVisible();
                currentObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_current_time"));
                currentObj.waitForExists(10000);
                currentTimeSplit = currentObj.getText().split(":");
                int postCurTime = Integer.parseInt(currentTimeSplit[0])*60 + Integer.parseInt(currentTimeSplit[1]);
                if (postCurTime > 0)
                    Assert.assertEquals(true, postCurTime >= currentTime);

                //總時間恆大於等於現在時間
                if (duration != currentTime && count != 100)
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
        int noTitleItemCount = 0;
        for (int i = 0; i < count; i++){
            UiObject title = getItemTitle(recyclerView, i);
            if (title != null)
                fileList.add(title.getText());
            else
                noTitleItemCount++; //扣掉沒標題的檔案(因為grid可能會遮到標題)
        }
        count -= noTitleItemCount;
        if (count > 0) {
            UiObject item = getItem(recyclerView, 0);
            item.click();

            int position = 0;
            for (int i = 0; i < execute_times; i++){
                mDevice.setOrientationNatural();
                checkVideoView();
                mDevice.setOrientationNatural();
                checkVideoView();
                mDevice.setOrientationLeft();
                checkVideoView();
                mDevice.setOrientationNatural();
                mDevice.unfreezeRotation();
                sleep(500);

                UiObject prev = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_previous"));
                UiObject next = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_next"));
                int random = (int)(Math.random() * 2);
                if (position == 0)  //第一部影片無法再上一首
                    random = 1;
                else if (position == count)   //列表的最後一部不一定是整體的最後一部影片，但保險起見作防呆保護
                    random = 0;

                if (random == 0){
                    position--;
                    prev.click();
                    sleep(500);
                    String [] title = getSingleViewTitle().split("\n");
                    Assert.assertEquals(fileList.get(position), title[0]);
                }
                else{
                    position++;
                    next.click();
                    sleep(500);
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
                checkVideoView();
                makeVideoControlVisible();
                UiObject next = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_next"));
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
                checkVideoView();
            }
            mDevice.pressBack();
            waitForDialog();
        }
        //////////測試下載&刪除 - 結束//////////

        if (isAtVideoView())
            mDevice.pressBack();
    }

    @Test
    public void SingleView_Image() throws UiObjectNotFoundException, RemoteException {
        int type = 0;
        while(type < 2){
            openDrawer();
            if (type == 0) {    //遠端
                clickDrawerBrowser();
                clickTabItem(Remote_Browser_Types.Image.ordinal());
            }
            else{   //本機
                clickDrawerMyStorage();
                clickTabItem(Local_Browser_Types.Image.ordinal());
            }

            //////////測試下載 & 旋轉//////////
            UiScrollable recyclerView = getMainRecyclerView();
            int testCount = getItemCount(recyclerView);
            for (int i = 0; i < testCount; i++){
                UiObject item = getItem(recyclerView, i);
                if (item.exists()) {
                    item.clickAndWaitForNewWindow();

                    if (type == 0) {
                        //下載後檢查圖片個數
                        downloadItemInImageView();
                        checkImageView();
                    }

                    //旋轉後檢查名稱、圖片個數
                    String title = getSingleViewTitle();
                    mDevice.setOrientationLeft();
                    sleep(500);
                    checkImageView();
                    Assert.assertEquals(title, getSingleViewTitle());
                    mDevice.setOrientationNatural();
                    mDevice.setOrientationRight();
                    sleep(500);
                    checkImageView();
                    Assert.assertEquals(title, getSingleViewTitle());
                    mDevice.setOrientationNatural();
                    mDevice.unfreezeRotation();
                    Assert.assertEquals(title, getSingleViewTitle());

                    //點擊返回鍵 or 左上角返回
                    if (i % 2 == 0){
                        UiObject toggleBack = new UiObject(new UiSelector().className("android.widget.ImageButton"));
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

                    if (type == 0) {    //目前本地的singleView沒有刪除功能
                        //返回後再次進到View，執行連續刪除並檢查
                        mDevice.pressBack();
                        scrollToEndOrTop(recyclerView, true);  //滑至底部
                        recyclerView = getMainRecyclerView();
                        item = getItem(recyclerView, 0);
                        count = getItemCount(recyclerView);
                        item.click();
                        for (int i = 0; i < count; i++) {
                            //試著刪除檔案，若回傳false則表示刪到沒有檔案而回到瀏覽頁面；true表示刪除成功
                            boolean stillAtView = deleteItemInImageView();
                            if (!stillAtView)
                                return;
                        }
                    }
                }
            }
            else{
                sleep(1000);
                //有檔案存在時，無檔案的圖示不能存在，反之亦然
                UiObject noItemViewObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/empty_view"));
                Assert.assertEquals(true, noItemViewObj.exists());
            }
            //////////測試滑動 & 刪除 - 結束//////////

            if (isAtImageView())
                mDevice.pressBack();

            type ++;
        }
    }

    @Test
    public void SingleView_Audio() throws UiObjectNotFoundException {
        openDrawer();
        clickDrawerBrowser();
        clickTabItem(Remote_Browser_Types.Audio.ordinal());

        //////////測試能否播放//////////
        UiScrollable recyclerView = getMainRecyclerView();
        for (int i = 0; i < getItemCount(recyclerView); i++){
            UiObject item = getItem(recyclerView, i);
            if (item.exists()) {
                item.click();

                //檢查播放、暫停鍵
                UiObject play = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/music_play"));
                play.click();
                sleep(3500);

                UiObject currentObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/music_current_time"));
                UiObject durationObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/music_duration"));
                durationObj.waitForExists(10000);

                String [] currentTimeSplit = currentObj.getText().split(":");
                String [] durationSplit = durationObj.getText().split(":");
                int currentTime = Integer.parseInt(currentTimeSplit[0])*60*60 + Integer.parseInt(currentTimeSplit[1])*60 + Integer.parseInt(currentTimeSplit[2]);
                int duration = Integer.parseInt(durationSplit[0])*60*60 + Integer.parseInt(durationSplit[1])*60 + Integer.parseInt(durationSplit[2]);

                int count = 0;
                while(duration == 0 && count < 100){   //讀取影片中
                    durationObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/music_duration"));
                    durationObj.waitForExists(10000);
                    durationSplit = durationObj.getText().split(":");
                    duration = Integer.parseInt(durationSplit[0])*60*60 + Integer.parseInt(durationSplit[1])*60 + Integer.parseInt(durationSplit[1]);
                    count++;
                    Log.e("DPB", count + "");
                }

                currentObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/music_current_time"));
                currentObj.waitForExists(10000);
                currentTimeSplit = currentObj.getText().split(":");
                int postCurTime = Integer.parseInt(currentTimeSplit[0])*60 + Integer.parseInt(currentTimeSplit[1]);
                if (postCurTime > 0)
                    Assert.assertEquals(true, postCurTime >= currentTime);

                //總時間恆大於等於現在時間
                if (duration != currentTime && count != 100)
                    Assert.assertEquals(true, duration > currentTime);

                mDevice.pressBack();
            }
        }
        //////////測試能否播放 - 結束//////////

        //////////測試上、下一首//////////
        List<String> fileList = new ArrayList<>();
        recyclerView = getMainRecyclerView();
        int count = getItemCount(recyclerView);
        int noTitleItemCount = 0;
        for (int i = 0; i < count; i++){
            UiObject title = getItemTitle(recyclerView, i);
            if (title != null)
                fileList.add(title.getText());
            else
                noTitleItemCount++; //扣掉沒標題的檔案(因為grid可能會遮到標題)
        }
        count -= noTitleItemCount;
        if (count > 0) {
            UiObject item = getItem(recyclerView, 0);
            item.click();

            int position = 0;
            for (int i = 0; i < execute_times; i++){
                UiObject prev = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/music_previous"));
                UiObject next = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/music_next"));
                int random = (int)(Math.random() * 2);
                if (position == 0)  //第一部影片無法再上一首
                    random = 1;
                else if (position == count)   //列表的最後一部不一定是整體的最後一部影片，但保險起見作防呆保護
                    random = 0;

                if (random == 0){
                    position--;
                    prev.click();
                    sleep(500);
                    String [] title = getSingleViewTitle().split("\n");
                    Assert.assertEquals(fileList.get(position), title[0]);
                }
                else{
                    position++;
                    next.click();
                    sleep(500);
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
                downloadItemInMusicView();
                UiObject next = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/music_next"));
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
                boolean stillAtView =  deleteItemInMusicView(); //是否刪到沒東西而回到瀏覽頁面
                if (!stillAtView)
                    return;
            }
            mDevice.pressBack();
            waitForDialog();
        }
        //////////測試下載&刪除 - 結束//////////

        if (isAtMusicView())
            mDevice.pressBack();
    }

    @Test
    public void Local_Device() throws UiObjectNotFoundException {
        openDrawer();
        clickDrawerMyStorage();

        UiScrollable recyclerView = getMainRecyclerView();
        //////////檢查長按//////////
        for (int i = 0; i < getItemCount(recyclerView); i++){
            UiObject selectItem = getItem(recyclerView, i);
            //長按的另一種解法，用以控制秒數。longclick有時會無法正確觸發
            mDevice.swipe(selectItem.getBounds().centerX(), selectItem.getBounds().centerY(),
                    selectItem.getBounds().centerX(), selectItem.getBounds().centerY(), 100);

            sleep(500);

            //確定有進到選擇模式
            boolean isSelectMode = isSelectMode();
            if (isSelectMode) {
                selectItem = getItem(recyclerView, i);
                Assert.assertEquals(true, selectItem.isSelected());
            }
            else  //若未進入到選取模式，則點擊返回鍵回到瀏覽頁面
                mDevice.pressBack();

            if (isSelectMode)
                mDevice.pressBack();

            sleep(500);
        }
        //////////檢查長按 - 結束//////////

        //////////檢查選取模式//////////

        //打開菜單
        openMenu();
        UiCollection menuList = getMenuList();
        //點擊全選
        UiObject selectMode = getMenuItem(menuList, 0);
        selectMode.click();
        sleep(500);

        //有進到selectMode才繼續
        if (isSelectMode()) {

            //確認標題是否正確
            int selectCount = 0;
            recyclerView = getMainRecyclerView();
            for (int i= 0; i < getItemCount(recyclerView); i++) {
                UiObject item = getItem(recyclerView, i);
                item.click();
                selectCount++;
                sleep(500);
                UiObject actionModeTitle = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/action_mode_custom_title"));
                Assert.assertEquals(true, actionModeTitle.getText().equals(selectCount + " 個選取項目"));
            }

            //打開菜單
            openMenu();
            menuList = getMenuList();
            //點擊全選
            UiObject selectAllObj = getMenuItem(menuList, 2);
            selectAllObj.click();
            sleep(500);

            mDevice.pressBack();
        }

        //檢查全選以及選取模式的返回鍵
        openMenu();
        menuList = getMenuList();
        //進入選取模式
        UiObject selectAllObj = getMenuItem(menuList, 1);
        selectAllObj.click();
        sleep(500);
        if (isSelectMode()) {
            UiObject toggleBack = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/toggle_btn"));
            toggleBack.click();
            sleep(500);
            Assert.assertEquals(false, isSelectMode());
        }
        //////////檢查選取模式 - 結束//////////
    }

    @Test
    public void Others() throws UiObjectNotFoundException, RemoteException {
        //////////檢查首頁//////////
        if (Testing_dashcam == Test_Dashcam_Type.Device30_60.ordinal()) {
            //點擊首頁
            openDrawer();
            clickDrawerHome();

            //檢查排版，誤差不能超過50
            UiObject liveView = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/layout_live_streaming"));
            UiObject browser = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/layout_browser"));
            Assert.assertEquals(true, Math.abs((liveView.getBounds().bottom - liveView.getBounds().top) - (browser.getBounds().bottom - browser.getBounds().top)) < 50);

            //測試所有按鈕
            UiObject snapshot = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/btn_snapshot"));
            UiObject recording = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/btn_recording"));
            UiObject location = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/btn_gps"));
            UiObject stealth = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/btn_stealth_mode"));
            UiObject microphone = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/btn_microphone"));

            int sleepTime = 1500;

            snapshot.click();
            sleep(sleepTime);
            snapshot.click();
            sleep(sleepTime);

            recording.click();
            sleep(sleepTime);
            recording.click();
            sleep(sleepTime);

            location.click();
            sleep(sleepTime);
            location.click();
            sleep(sleepTime);

            stealth.click();
            sleep(sleepTime);
            stealth.click();
            sleep(sleepTime);

            microphone.click();
            sleep(sleepTime);
            microphone.click();
            sleep(sleepTime);
        }

        UiObject liveView = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/layout_live_streaming"));
        UiObject browser = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/layout_browser"));
        for (int i = 0; i < execute_times; i++) {
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
            UiObject wifiList = new UiObject(new UiSelector().resourceId("com.android.settings:id/list"));
            if (wifiList.exists()){
                mDevice.pressBack();
                sleep(2000);

                browser.clickAndWaitForNewWindow();
                waitForDialog();
            }
            if (!getMainRecyclerView().exists())
                Assert.assertEquals(true, new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/empty_view")).exists());

            //點擊首頁
            openDrawer();
            clickDrawerHome();
        }
        //////////檢查首頁 - 結束//////////

        //////////檢查即時影像//////////
        //點擊即時影像
        liveView = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/layout_live_streaming"));
        liveView.clickAndWaitForNewWindow();
        UiCollection webViewLayout = new UiCollection(new UiSelector().className("android.webkit.WebView"));
        webViewLayout.waitForExists(10000);
        sleep(500);

        UiObject refresh = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/btn_reload_web_view"));
        refresh.click();
        sleep(500);

        mDevice.setOrientationLeft();
        sleep(500);
        UiObject webView = new UiObject(new UiSelector().className("android.webkit.WebView"));
        Assert.assertEquals(true, webView.getBounds().right == mDevice.getDisplayWidth());
        Assert.assertEquals(true, webView.getBounds().bottom == mDevice.getDisplayHeight());
        mDevice.setOrientationNatural();
        sleep(500);
        mDevice.unfreezeRotation();
        //////////檢查即時影像 - 結束//////////

        //////////檢查設定//////////
        openDrawer();
        clickDrawerSetting();

        UiScrollable settingRecycler = new UiScrollable(new UiSelector().resourceId("com.transcend.bcr:id/settings_recycler_view"));

        String recordTime = "";
        String pixelText = "";

        UiObject pixelTitleObj = settingRecycler.getChildByText(new UiSelector().resourceId("com.transcend.bcr:id/settingsSubtitle"), "解析度", true);
        UiObject pixelParentObj = getParentFromChild(
                settingRecycler,
                new UiSelector().className("android.widget.LinearLayout"), //尋找之父親的Selector
                new UiSelector().resourceId("com.transcend.bcr:id/settingsSubtitle"), //參照物的Selector
                "解析度");   //參照物的值
        UiObject pixel = pixelParentObj.getChild(new UiSelector().resourceId("com.transcend.bcr:id/settingsStatus"));
        pixel.click();
        waitForDialog();

        UiCollection frame = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/custom"));
        int instance = 1;
        //隨機取得解析度的LinearLayout序號
        int random = (int) (Math.random() * 3);
        switch(random){
            case 0:
                instance = 1;
                pixelText = "1080P";
                break;
            case 1:
                instance = 2;
                pixelText = "720P";
                break;
            case 2:
                instance = 3;
                pixelText = "480P";
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

        UiObject recordTitleObj = settingRecycler.getChildByText(new UiSelector().resourceId("com.transcend.bcr:id/settingsSubtitle"), "緩錄影片長度", true);
        UiObject recordParentObj = getParentFromChild(
                settingRecycler,
                new UiSelector().className("android.widget.LinearLayout"), //尋找之父親的Selector
                new UiSelector().resourceId("com.transcend.bcr:id/settingsSubtitle"), //參照物的Selector
                "緩錄影片長度");   //參照物的值
        UiObject record = recordParentObj.getChild(new UiSelector().resourceId("com.transcend.bcr:id/settingsStatus"));
        record.click();
        waitForDialog();

        frame = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/custom"));
        instance = 1;
        //隨機取得解析度的LinearLayout序號
        random = (int) (Math.random() * 4);
        switch(random){
            case 0:
                instance = 1;
                recordTime = "30s";
                break;
            case 1:
                instance = 2;
                recordTime = "60s";
                break;
            case 2:
                instance = 3;
                recordTime = "90s";
                break;
            case 3:
                instance = 4;
                recordTime = "120s";
                break;
        }
        clickTarget = frame.getChildByInstance(new UiSelector().className("android.widget.LinearLayout"), instance);
        clickTarget.longClick();
        sleep(500);
        ok = new UiObject(new UiSelector().resourceId("android:id/button1"));
        ok.click();
        sleep(500);
        waitForDialog();
        Assert.assertEquals(recordTime, record.getText());

        //刷掉程式
        closeAPP(mDevice, "com.transcend.bcr");
        sleep(5000);
        startAPP("com.transcend.bcr");
        sleep(5000);
        //等待進入首頁
        liveView = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/layout_live_streaming"));
        liveView.waitForExists(10000);

        //進到設定
        openDrawer();
        clickDrawerSetting();
        settingRecycler = new UiScrollable(new UiSelector().resourceId("com.transcend.bcr:id/settings_recycler_view"));

        pixelTitleObj = settingRecycler.getChildByText(new UiSelector().resourceId("com.transcend.bcr:id/settingsSubtitle"), "解析度", true);
        pixelParentObj = getParentFromChild(
                settingRecycler,
                new UiSelector().className("android.widget.LinearLayout"), //尋找之父親的Selector
                new UiSelector().resourceId("com.transcend.bcr:id/settingsSubtitle"), //參照物的Selector
                "解析度");   //參照物的值
        pixel = pixelParentObj.getChild(new UiSelector().resourceId("com.transcend.bcr:id/settingsStatus"));
        Assert.assertEquals(pixelText, pixel.getText());

        recordTitleObj = settingRecycler.getChildByText(new UiSelector().resourceId("com.transcend.bcr:id/settingsSubtitle"), "緩錄影片長度", true);
        recordParentObj = getParentFromChild(
                settingRecycler,
                new UiSelector().className("android.widget.LinearLayout"), //尋找之父親的Selector
                new UiSelector().resourceId("com.transcend.bcr:id/settingsSubtitle"), //參照物的Selector
                "緩錄影片長度");   //參照物的值
        record = recordParentObj.getChild(new UiSelector().resourceId("com.transcend.bcr:id/settingsStatus"));
        Assert.assertEquals(recordTime, record.getText());
        //////////檢查設定 - 結束//////////

        //////////檢查關於//////////
        settingRecycler = new UiScrollable(new UiSelector().resourceId("com.transcend.bcr:id/settings_recycler_view"));
        UiObject aboutTitleObj = settingRecycler.getChildByText(new UiSelector().resourceId("com.transcend.bcr:id/settingsSubtitle"), "關於", true);
        UiObject aboutParentObj = getParentFromChild(
                settingRecycler,
                new UiSelector().className("android.widget.LinearLayout"), //尋找之父親的Selector
                new UiSelector().resourceId("com.transcend.bcr:id/settingsSubtitle"), //參照物的Selector
                "關於");   //參照物的值
        UiObject about = aboutParentObj.getChild(new UiSelector().resourceId("com.transcend.bcr:id/settingsStatus"));
        about.click();

        UiCollection aboutRecycler = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/about_recycler_view"));
        UiObject eula = aboutRecycler.getChildByInstance(new UiSelector().resourceId("com.transcend.bcr:id/about_entry_layout"), 1);
        UiObject openSource = aboutRecycler.getChildByInstance(new UiSelector().resourceId("com.transcend.bcr:id/about_entry_layout"), 2);

        openSource.clickAndWaitForNewWindow();
        sleep(500);
        mDevice.pressBack();
        openSource.clickAndWaitForNewWindow();
        sleep(500);
        UiObject toggleBack = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/toggle_btn"));
        toggleBack.click();
        sleep(1000);

        eula.clickAndWaitForNewWindow();
        sleep(500);
        mDevice.pressBack();
        eula.clickAndWaitForNewWindow();
        sleep(500);
        toggleBack = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/toggle_btn"));
        toggleBack.click();

        toggleBack.click();
        //////////檢查關於 - 結束//////////
    }

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
