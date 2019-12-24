package com.transcend.autotest.DrivePro;

import android.os.RemoteException;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiCollection;
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

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DrivePro_BugList extends DriveProAbstract{

    @Before
    public void setup() {
        super.setup();
    }

    //TODO issue 1
    @Test
    public void issue1() throws UiObjectNotFoundException {
        //打開Drawer
        openDrawer();

        //點擊本機
        clickDrawerMyStorage();

        //找到界面上的元素。
        UiScrollable recyclerView = getMainRecyclerView();
        UiObject item = getItem(recyclerView, 0);
        if (item.exists()) {
            item.clickAndWaitForNewWindow();

            sleep(1000);

            //右滑，試著滑出drawer
            tryToOpenDrawerBySwipe();

            //回到瀏覽畫面
            mDevice.pressBack();

            sleep(1000);

            //右滑，試著滑出drawer
            tryToOpenDrawerBySwipe();

            //issue1，判斷drawer是否存在
            UiCollection drawer = new UiCollection(new UiSelector().resourceId("com.transcend.cvr:id/drawer_layout"));
            Assert.assertEquals(true, drawer.exists());

            clickDrawerMyStorage();
        }
    }

    //TODO issue 2
    @Test
    public void issue2() throws UiObjectNotFoundException, RemoteException {
        //打開Drawer
        openDrawer();

        //點擊本機
        clickDrawerMyStorage();

        //找到界面上的元素。
        UiScrollable recyclerView = getMainRecyclerView();
        UiObject item = getItem(recyclerView, 0);
        if (item.exists()) {
            item.clickAndWaitForNewWindow();

            //轉橫屏後返回
            mDevice.setOrientationLeft();

            mDevice.pressBack();

            mDevice.unfreezeRotation();


            //issue2
            Assert.assertEquals(true, mDevice.getDisplayWidth() < mDevice.getDisplayHeight());
        }
    }

    //TODO issue 3
    @Test
    public void issue3() throws UiObjectNotFoundException, RemoteException {
        //打開Drawer
        openDrawer();

        //點擊本機
        clickDrawerMyStorage();

        //找到界面上的元素。
        UiScrollable recyclerView = getMainRecyclerView();

        //紀錄目前顯示的檔案數量
        int pre_count = recyclerView.getChildCount();

        //點擊影片
        UiObject item = getItem(recyclerView, 0);
        if (item.exists())
            item.clickAndWaitForNewWindow();
        else
            return;

        //轉橫屏
        mDevice.setOrientationLeft();

        //返回
        mDevice.pressBack();

        waitForDialog();

        //打開drawer
        openDrawer();

        //點擊"即時影像"
        clickDrawerLiveView();

        //打開drawer
        tryToOpenDrawerBySwipe();

        //轉橫屏
        mDevice.setOrientationNatural();

        //點擊本機
        clickDrawerMyStorage();

        //issue3，檢查顯示在列表上檔案的個數
        int post_count = recyclerView.getChildCount();
        Assert.assertEquals(pre_count, post_count);

        mDevice.unfreezeRotation();
    }

    //TODO issue 4
    @Test
    public void issue4() throws UiObjectNotFoundException, RemoteException {
        //打開Drawer
        openDrawer();

        //點擊本機
        clickDrawerMyStorage();

        int type = 0;
        while(type < Local_Browser_Types){
            clickTabItem(type);

            //找到界面上的元素。
            UiScrollable recyclerView = getMainRecyclerView();
            int total = getItemCount(recyclerView);
            if (total > 3)
                total = 3;
            for(int count = 0; count < total; count++){
                UiObject item = getItem(recyclerView, count);
                if (item.exists()) {
                    item.clickAndWaitForNewWindow();

                    for (int i = 0; i < 1; i++) {
                        if (type < Local_Browser_Types - 1) {
                            mDevice.setOrientationLeft();
                            UiObject playerView = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/video_surface"));
                            if (!playerView.exists()){
                                UiObject imgPlay = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/play_img"));
                                org.junit.Assert.assertEquals(true, imgPlay.exists());
                            }

                            mDevice.setOrientationNatural();
                            playerView = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/video_surface"));
                            if (!playerView.exists()){
                                UiObject imgPlay = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/play_img"));
                                org.junit.Assert.assertEquals(true, imgPlay.exists());
                            }
                        }
                        else{
                            mDevice.setOrientationLeft();
                            UiCollection viewpager = new UiCollection(new UiSelector().resourceId("com.transcend.cvr:id/pager_photoviewer"));
                            Assert.assertEquals(1, viewpager.getChildCount(new UiSelector().resourceId("com.transcend.cvr:id/photo_view")));

                            mDevice.setOrientationNatural();
                            viewpager = new UiCollection(new UiSelector().resourceId("com.transcend.cvr:id/pager_photoviewer"));
                            Assert.assertEquals(1, viewpager.getChildCount(new UiSelector().resourceId("com.transcend.cvr:id/photo_view")));
                        }
                    }

                    mDevice.pressBack();
                }
            }

            type++;
        }

        //打開Drawer
        openDrawer();

        //點擊遠端
        clickDrawerRemote();

        type = 0;
        while(type < Remote_Browser_Types){
            clickTabItem(type);

            //找到界面上的元素。
            UiScrollable recyclerView = getMainRecyclerView();
            int total = getItemCount(recyclerView);
            if (total > 3)
                total = 3;
            for(int count = 0; count < total; count++){
                UiObject item = getItem(recyclerView, count);
                if (item.exists()) {
                    item.clickAndWaitForNewWindow();

                    for (int i = 0; i < 1; i++) {
                        if (type < 2) {
                            mDevice.setOrientationLeft();
                            UiObject playerView = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/video_surface"));
                            if (!playerView.exists()){
                                UiObject imgPlay = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/play_img"));
                                org.junit.Assert.assertEquals(true, imgPlay.exists());
                            }

                            mDevice.setOrientationNatural();
                            playerView = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/video_surface"));
                            if (!playerView.exists()){
                                UiObject imgPlay = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/play_img"));
                                org.junit.Assert.assertEquals(true, imgPlay.exists());
                            }
                        }
                        else{
                            mDevice.setOrientationLeft();
                            UiCollection viewpager = new UiCollection(new UiSelector().resourceId("com.transcend.cvr:id/pager_photoviewer"));
                            Assert.assertEquals(1, viewpager.getChildCount(new UiSelector().resourceId("com.transcend.cvr:id/photo_view")));

                            mDevice.setOrientationNatural();
                            viewpager = new UiCollection(new UiSelector().resourceId("com.transcend.cvr:id/pager_photoviewer"));
                            Assert.assertEquals(1, viewpager.getChildCount(new UiSelector().resourceId("com.transcend.cvr:id/photo_view")));
                        }
                    }

                    mDevice.pressBack();
                }
            }

            type++;
        }

        mDevice.unfreezeRotation();
    }

    //TODO issue 5
    @Test
    public void issue5() throws UiObjectNotFoundException, RemoteException {
        //打開Drawer
        openDrawer();

        //點擊遠端
        clickDrawerRemote();

        int type = 0;
        while(type < Remote_Browser_Types) {
            clickTabItem(type);

            //找到界面上的元素。
            UiScrollable recyclerView = getMainRecyclerView();
            int total = getItemCount(recyclerView);
            if (total > 3)
                total = 3;
            for(int count = 0; count < total; count++){
                UiObject item = getItem(recyclerView, count);
                if (item.exists()) {
                    item.clickAndWaitForNewWindow();

                    //取得標題
                    String itemTitle = getSingleViewTitle();
                    for (int i = 0; i < 1; i++) {
                        //轉為橫屏
                        mDevice.setOrientationLeft();
                        sleep(400);

                        Assert.assertEquals(itemTitle, getSingleViewTitle());

                        mDevice.setOrientationNatural();
                        sleep(400);

                        Assert.assertEquals(itemTitle, getSingleViewTitle());
                    }

                    mDevice.pressBack();
                }
            }
            type++;
        }

        waitForDialog();

        //打開Drawer
        openDrawer();

        //點擊本機
        clickDrawerMyStorage();

        type = 0;
        while(type < Local_Browser_Types) {
            clickTabItem(type);

            //找到界面上的元素。
            UiScrollable recyclerView = getMainRecyclerView();
            int total = getItemCount(recyclerView);
            if (total > 3)
                total = 3;
            for(int count = 0; count < total; count++){
                UiObject item = getItem(recyclerView, count);
                if (item.exists()) {
                    item.clickAndWaitForNewWindow();

                    //取得標題
                    String itemTitle = getSingleViewTitle();
                    for (int i = 0; i < 1; i++) {
                        //轉為橫屏
                        mDevice.setOrientationLeft();
                        sleep(400);

                        Assert.assertEquals(itemTitle, getSingleViewTitle());

                        mDevice.setOrientationNatural();
                        sleep(400);

                        Assert.assertEquals(itemTitle, getSingleViewTitle());
                    }

                    mDevice.pressBack();
                }
            }
            type++;
        }

        //重新啟動螢幕自動旋轉
        mDevice.unfreezeRotation();
    }

    //TODO issue 6
    @Test
    public void issue6() throws UiObjectNotFoundException {
        //打開Drawer
        openDrawer();

        //點擊遠端
        clickDrawerRemote();

        //各類型皆跑一遍
        int type = 0;
        while (type < Remote_Browser_Types){
            clickTabItem(type);

            //找到界面上的元素，點擊影片內容
            UiScrollable recyclerView = getMainRecyclerView();
            UiObject item = getItem(recyclerView, 0);
            if (item.exists()){
                item.clickAndWaitForNewWindow();

                if (type != 2) {
                    UiObject next = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/vlc_button_next"));
                    UiObject prev = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/vlc_button_previous"));

                    if (!next.exists() || !prev.exists())
                        break;

                    //隨機滑動，查看是否會閃退
                    for (int i = 0; i < 3; i++) {
                        int random = (int) (Math.random() * 2);
                        if (random == 0) {
                            next.click();
                            sleep(400);
                        }
                        else {
                            prev.click();
                            sleep(400);
                        }
                    }
                }
                else {      //照片才能滑動
                    swipeToNextViewPager();
                    swipeToNextViewPager();

                    //隨機滑動，查看是否會閃退
                    for (int i = 0; i < 3; i++) {
                        int random = (int) (Math.random() * 2);
                        if (random == 0)
                            swipeToNextViewPager();
                        else
                            swipeToPrevViewPager();
                    }
                }
                mDevice.pressBack();
            }
            type++;
        }

        //打開Drawer
        openDrawer();

        //點擊本機
        clickDrawerMyStorage();

        //各類型皆跑一遍
        type = 0;
        while (type < Local_Browser_Types){
            clickTabItem(type);

            //找到界面上的元素，點擊影片內容
            UiCollection recyclerView = new UiCollection(new UiSelector().className("android.support.v7.widget.RecyclerView"));
            UiObject item = recyclerView.getChildByInstance(new UiSelector().resourceId("com.transcend.cvr:id/listitem_file_manage"), 0);
            if (item.exists()){
                item.clickAndWaitForNewWindow();

                if (type != Local_Browser_Types - 1) {
                    UiObject next = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/vlc_button_next"));
                    UiObject prev = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/vlc_button_previous"));

                    //隨機滑動，查看是否會閃退
                    for (int i = 0; i < 3; i++) {
                        int random = (int) (Math.random() * 2);
                        if (random == 0) {
                            next.click();
                            sleep(400);
                        }
                        else {
                            prev.click();
                            sleep(400);
                        }
                    }
                }
                else {
                    swipeToNextViewPager();
                    swipeToNextViewPager();

                    //隨機滑動，查看是否會閃退
                    for (int i = 0; i < 3; i++) {
                        int random = (int) (Math.random() * 2);
                        if (random == 0)
                            swipeToNextViewPager();
                        else
                            swipeToPrevViewPager();
                    }
                }
                mDevice.pressBack();
            }
            type++;
        }
    }

    //TODO issue 7
    @Test
    public void issue7() throws UiObjectNotFoundException {
        waitForDialog();

        //打開Drawer
        openDrawer();

        //點擊遠端
        clickDrawerRemote();

        //各類型皆跑一遍
        int type = 0;
        while (type < Remote_Browser_Types){
            clickTabItem(type);

            //找到界面上的元素，點擊影片內容
            UiScrollable recyclerView = getMainRecyclerView();
            UiObject item = getItem(recyclerView, 0);
            if (item.exists()){
                item.clickAndWaitForNewWindow();

                if (type != 2) {
                    UiObject next = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/vlc_button_next"));
                    UiObject prev = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/vlc_button_previous"));

                    //隨機滑動，查看是否會閃退
                    for (int i = 0; i < 3; i++) {
                        int random = (int) (Math.random() * 2);
                        if (random == 0) {
                            next.click();
                            sleep(400);
                        }
                        else {
                            prev.click();
                            sleep(400);
                        }
                    }
                }
                else {
                    swipeToNextViewPager();
                    swipeToNextViewPager();

                    //隨機滑動，查看是否會閃退
                    for (int i = 0; i < 5; i++) {
                        int random = (int) (Math.random() * 2);
                        if (random == 0)
                            swipeToNextViewPager();
                        else
                            swipeToPrevViewPager();
                    }
                }
                mDevice.pressBack();
            }
            type++;
        }

        //打開Drawer
        openDrawer();

        //點擊本機
        clickDrawerMyStorage();

        //各類型皆跑一遍
        type = 0;
        while (type < Local_Browser_Types){
            clickTabItem(type);

            //找到界面上的元素，點擊影片內容
            UiScrollable recyclerView = getMainRecyclerView();
            UiObject item = getItem(recyclerView, 0);
            if (item.exists()){
                item.clickAndWaitForNewWindow();

                if (type != Local_Browser_Types - 1) {
                    UiObject next = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/vlc_button_next"));
                    UiObject prev = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/vlc_button_previous"));

                    //隨機滑動，查看是否會閃退
                    for (int i = 0; i < 3; i++) {
                        int random = (int) (Math.random() * 2);
                        if (random == 0) {
                            next.click();
                            sleep(400);
                        }
                        else {
                            prev.click();
                            sleep(400);
                        }
                    }
                }
                else {
                    swipeToNextViewPager();
                    swipeToNextViewPager();

                    //隨機滑動，查看是否會閃退
                    for (int i = 0; i < 5; i++) {
                        int random = (int) (Math.random() * 2);
                        if (random == 0)
                            swipeToNextViewPager();
                        else
                            swipeToPrevViewPager();
                    }
                }
                mDevice.pressBack();
            }
            type++;
        }
    }

    //TODO issue 12, 16
    @Test
    public void issue12() throws UiObjectNotFoundException {
        //打開Drawer
        openDrawer();

        //點擊遠端
        clickDrawerRemote();

        //全部檔案，判斷為空時，空檔案的圖片是否顯示；有檔案時，空檔案的圖片是否隱藏
        UiScrollable recyclerView = getMainRecyclerView();
        UiObject nodata = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/empty_view"));
        if(getItemCount(recyclerView) == 0)
            Assert.assertEquals(true, nodata.exists());
        else
            Assert.assertEquals(false, nodata.exists());


        //點擊緊急錄影，判斷為空時，空檔案的圖片是否顯示；有檔案時，空檔案的圖片是否隱藏
        clickTabItem(1);
        recyclerView = getMainRecyclerView();
        nodata = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/empty_view"));
        if(getItemCount(recyclerView) == 0)
            Assert.assertEquals(true, nodata.exists());
        else
            Assert.assertEquals(false, nodata.exists());


        //點擊照片，判斷為空時，空檔案的圖片是否顯示；有檔案時，空檔案的圖片是否隱藏
        clickTabItem(2);
        recyclerView = getMainRecyclerView();
        nodata = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/empty_view"));
        if(getItemCount(recyclerView) == 0)
            Assert.assertEquals(true, nodata.exists());
        else
            Assert.assertEquals(false, nodata.exists());

        //打開Drawer
        openDrawer();

        //點擊本機
        clickDrawerMyStorage();

        //全部檔案，判斷為空時，空檔案的圖片是否顯示；有檔案時，空檔案的圖片是否隱藏
        recyclerView = getMainRecyclerView();
        nodata = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/empty_view"));
        if(getItemCount(recyclerView) == 0)
            Assert.assertEquals(true, nodata.exists());
        else
            Assert.assertEquals(false, nodata.exists());


        //點擊照片，判斷為空時，空檔案的圖片是否顯示；有檔案時，空檔案的圖片是否隱藏
        clickTabItem(1);
        recyclerView = getMainRecyclerView();
        nodata = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/empty_view"));
        if(getItemCount(recyclerView) == 0)
            Assert.assertEquals(true, nodata.exists());
        else
            Assert.assertEquals(false, nodata.exists());
    }

    //TODO issue 14
    @Test
    public void issue14() throws UiObjectNotFoundException {
        openDrawer();

        clickDrawerHome();

        UiCollection frame = new UiCollection(new UiSelector().className("android.widget.LinearLayout"));
        UiObject topImage = frame.getChildByInstance(new UiSelector().className("android.widget.RelativeLayout"), 0);
        UiObject downImage = frame.getChildByInstance(new UiSelector().className("android.widget.RelativeLayout"), 1);

        int topWidth = topImage.getBounds().right - topImage.getBounds().left;
        int topHeight = topImage.getBounds().bottom - topImage.getBounds().top;
        int downWidth = downImage.getBounds().right - downImage.getBounds().left;
        int downHeight = downImage.getBounds().bottom - downImage.getBounds().top;

        //Log.e("issue14", "top: " + topWidth + ", " + topHeight + "; down: " + downWidth + ", " + downHeight);
        Assert.assertEquals(topWidth, downWidth);
        Assert.assertEquals(topHeight, downHeight);
    }

    //TODO issue 8, 15, 18
    @Test
    public void issue15() throws UiObjectNotFoundException {
        //打開Drawer
        openDrawer();

        //點擊瀏覽
        clickDrawerRemote();

        int type = 0;
        while(type < Remote_Browser_Types){
            clickTabItem(type);

            //找到界面上的元素，點擊影片內容
            UiScrollable recyclerView = getMainRecyclerView();
            UiObject item = getItem(recyclerView, 0);
            if (item.exists()) {
                item.clickAndWaitForNewWindow();

                //紀錄刪除前的標題
                String preTitle = getSingleViewTitle();

                //點擊刪除
                UiObject deleteBtn;
                if (type !=2)
                    deleteBtn = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/vlc_button_delete"));
                else
                    deleteBtn = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/photoviewer_delete"));
                deleteBtn.clickAndWaitForNewWindow();
                sleep(1000);
                //獲取刪除資訊
                UiObject deleteMessage = new UiObject(new UiSelector().resourceId("android:id/message"));
                String [] deleteText = deleteMessage.getText().split("\\.");

                //取得刪除前標題裡所含的檔案名稱
                String [] textSplit = preTitle.split("\\s");

                //判斷刪除的檔案與標題是否一致
                Assert.assertEquals(true, deleteText[0].equals(textSplit[0]));

                //點擊確定以刪除
                UiObject alertDeleteBtn = new UiObject(new UiSelector().resourceId("android:id/button1"));
                alertDeleteBtn.clickAndWaitForNewWindow();

                //紀錄刪除後的標題
                String postTitle = getSingleViewTitle();

                //判斷刪除前後的標題否一致以確認是否刪除成功
                Assert.assertEquals(false, preTitle.equals(postTitle));

                recyclerView = getMainRecyclerView();
                if (!recyclerView.exists()) {
                    mDevice.pressBack();
                    waitForDialog();
                }
            }
            type++;
        }

        //打開Drawer
        openDrawer();

        //點擊本機
        clickDrawerMyStorage();

        type = 0;
        while(type < Local_Browser_Types){
            clickTabItem(type);

            //找到界面上的元素，點擊影片內容
            UiScrollable recyclerView = getMainRecyclerView();
            UiObject item = getItem(recyclerView, 0);
            if (item.exists()) {
                item.clickAndWaitForNewWindow();

                //紀錄刪除前的標題
                String preTitle = getSingleViewTitle();

                //點擊刪除
                UiObject deleteBtn;
                if (type != Local_Browser_Types - 1)
                    deleteBtn = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/vlc_button_delete"));
                else
                    deleteBtn = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/photoviewer_delete"));
                deleteBtn.clickAndWaitForNewWindow();
                sleep(1000);
                //獲取刪除資訊
                UiObject deleteMessage = new UiObject(new UiSelector().resourceId("android:id/message"));
                String [] deleteText = deleteMessage.getText().split("\\.");

                //取得刪除前標題裡所含的檔案名稱
                String [] textSplit = preTitle.split("\\s");

                //判斷刪除的檔案與標題是否一致
                Assert.assertEquals(true, deleteText[0].equals(textSplit[0]));

                //點擊確定以刪除
                UiObject alertDeleteBtn = new UiObject(new UiSelector().resourceId("android:id/button1"));
                alertDeleteBtn.clickAndWaitForNewWindow();

                //紀錄刪除後的標題
                String postTitle = getSingleViewTitle();

                //判斷刪除前後的標題否一致以確認是否刪除成功
                Assert.assertEquals(false, preTitle.equals(postTitle));

                recyclerView = getMainRecyclerView();
                if (!recyclerView.exists()) {
                    mDevice.pressBack();
                    waitForDialog();
                }
            }
            type++;
        }
    }

    //TODO issue 17
    @Test
    public void issue17() throws UiObjectNotFoundException {
        //打開Drawer
        openDrawer();

        //點擊遠端
        clickDrawerRemote();

        int type = 0;
        while (type < 2) {
            clickTabItem(type);

            //找到界面上的元素，點擊影片
            UiScrollable recyclerView = getMainRecyclerView();
            int total = getItemCount(recyclerView);
            if (total > 3)
                total = 3;
            for(int count = 0; count < total; count++){
                UiObject item = getItem(recyclerView, count);
                if (item.exists())
                    item.clickAndWaitForNewWindow();
                else
                    break;

                //取得播放按鈕
//                UiObject playBtn = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/play_img"));
//                playBtn.clickAndWaitForNewWindow();

                UiObject totalTimeObj = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/video_total_time"));
                if (totalTimeObj.exists()){
                    String [] totalTimeSplit = totalTimeObj.getText().split(":");
                    int totalH = Integer.parseInt(totalTimeSplit[0]);
                    int totalM = Integer.parseInt(totalTimeSplit[1]);
                    int totalTime = totalH * 60 + totalM;

                    //等待總時間+1秒
                    sleep((total+1)*1000);
                }

                UiObject next = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/vlc_button_next"));
                next.click();
                sleep(1000);

                totalTimeObj = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/video_total_time"));
                if (totalTimeObj.exists()){
                    String [] totalTimeSplit = totalTimeObj.getText().split(":");
                    int totalH = Integer.parseInt(totalTimeSplit[0]);
                    int totalM = Integer.parseInt(totalTimeSplit[1]);
                    int totalTime = totalH * 60 + totalM;

                    //等待總時間+1秒
                    sleep((total+1)*1000);
                }
                sleep(1500);
                mDevice.pressBack();
            }
            type++;
        }


        //打開Drawer
        openDrawer();

        //點擊本機
        clickDrawerMyStorage();

        //找到界面上的元素，點擊影片
        UiScrollable recyclerView = getMainRecyclerView();
        int total = getItemCount(recyclerView);
        if (total > 3)
            total = 3;
        for(int count = 0; count < total; count++){
            UiObject item = getItem(recyclerView, count);
            if (item.exists())
                item.clickAndWaitForNewWindow();
            else
                break;
            //取得播放按鈕
//                UiObject playBtn = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/play_img"));
//                playBtn.clickAndWaitForNewWindow();

            UiObject totalTimeObj = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/video_total_time"));
            if (totalTimeObj.exists()){
                String [] totalTimeSplit = totalTimeObj.getText().split(":");
                int totalH = Integer.parseInt(totalTimeSplit[0]);
                int totalM = Integer.parseInt(totalTimeSplit[1]);
                int totalTime = totalH * 60 + totalM;

                //等待總時間+1秒
                sleep((total+1)*1000);
            }

            UiObject next = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/vlc_button_next"));
            next.click();
            sleep(3000);

            totalTimeObj = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/video_total_time"));
            if (totalTimeObj.exists()){
                String [] totalTimeSplit = totalTimeObj.getText().split(":");
                int totalH = Integer.parseInt(totalTimeSplit[0]);
                int totalM = Integer.parseInt(totalTimeSplit[1]);
                int totalTime = totalH * 60 + totalM;

                //等待總時間+1秒
                sleep((total+1)*1000);
            }
            sleep(1500);
            mDevice.pressBack();
        }
    }

    //TODO issue 20
    @Test
    public void issue20() throws UiObjectNotFoundException {
        //打開Drawer
        openDrawer();

        //點擊本機
        clickDrawerMyStorage();

        int type = 0;
        while (type < Local_Browser_Types) {
            clickTabItem(type);

            //找到界面上的元素，點擊影片
            UiScrollable recyclerView = getMainRecyclerView();
            for(int count = 0; count < getItemCount(recyclerView); count++){
                UiObject item = recyclerView.getChildByInstance(new UiSelector().resourceId("com.transcend.cvr:id/listitem_file_manage"), count);
                UiObject list_title = recyclerView.getChildByInstance(new UiSelector().resourceId("com.transcend.cvr:id/itemName"), count);
                String [] pre_title_split = null;
                if (item.exists()) {
                    pre_title_split = list_title.getText().split("\\_");
                    item.clickAndWaitForNewWindow();
                }
                else
                    break;

                UiObject single_title = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/toolbar_title"));
                String [] post_title_split = single_title.getText().split("\\s");

                Assert.assertEquals(pre_title_split[1] + "_" + pre_title_split[0], post_title_split[0] + "_" + post_title_split[1] + " " + post_title_split[2]);
                mDevice.pressBack();
            }
            type++;
        }

        //打開Drawer
        openDrawer();

        //點擊行車記錄器
        clickDrawerRemote();

        type = 0;
        while (type < Remote_Browser_Types) {
            clickTabItem(type);

            //找到界面上的元素，點擊影片
            UiScrollable recyclerView = getMainRecyclerView();
            int total = getItemCount(recyclerView);
            if (total > 5)
                total = 5;
            for(int count = 0; count < total; count++){
                UiObject item = getItem(recyclerView, count);
                UiObject list_title = getItemTitle(recyclerView, count);
                String pre_title = null;
                if (item.exists()) {
                    pre_title = list_title.getText();
                    item.clickAndWaitForNewWindow();
                }
                else
                    break;

                UiObject single_title = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/toolbar_title"));
                String [] post_title_split = single_title.getText().split("\\s");

                Assert.assertEquals(pre_title, post_title_split[0]);
                mDevice.pressBack();
            }
            type++;
        }
    }

    //TODO issue 21
    @Test
    public void issue21() throws UiObjectNotFoundException, IOException {
        //打開Drawer
        openDrawer();

        //點擊設定
        clickDrawerSetting();

        sleep(3000);

        String autoTurnOffScreenTime = null;

        UiScrollable recyclerView = new UiScrollable(new UiSelector().resourceId("com.transcend.cvr:id/setting_recycleview"));
        UiObject autoTurnOffScreen = recyclerView.getChildByInstance(new UiSelector().resourceId("com.transcend.cvr:id/settingsStatus"), 6);
        autoTurnOffScreen.clickAndWaitForNewWindow();
        sleep(1000);

        UiCollection frame = new UiCollection(new UiSelector().resourceId("com.transcend.cvr:id/custom"));

        int instance = 1;
        //隨機取得關閉螢幕時間的LinearLayout序號
        int random = (int) (Math.random() * 3);
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

        UiObject clickTarget = frame.getChildByInstance(new UiSelector().className("android.widget.LinearLayout"), instance);
        clickTarget.click();
        clickTarget.click();
        sleep(1000);

        UiObject ok = new UiObject(new UiSelector().resourceId("android:id/button1"));
        ok.click();
        sleep(1000);
        autoTurnOffScreen = recyclerView.getChildByInstance(new UiSelector().resourceId("com.transcend.cvr:id/settingsStatus"), 6);
        Assert.assertEquals(autoTurnOffScreenTime, autoTurnOffScreen.getText());


        String pixelText = null;
        UiObject pixel = recyclerView.getChildByInstance(new UiSelector().resourceId("com.transcend.cvr:id/settingsStatus"), 0);
        pixel.clickAndWaitForNewWindow();
        sleep(1000);

        frame = new UiCollection(new UiSelector().resourceId("com.transcend.cvr:id/custom"));

        instance = 1;
        //隨機取得關閉螢幕時間的LinearLayout序號
        random = (int) (Math.random() * 2);
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

        clickTarget = frame.getChildByInstance(new UiSelector().className("android.widget.LinearLayout"), instance);
        clickTarget.click();
        clickTarget.click();
        sleep(1000);

        ok = new UiObject(new UiSelector().resourceId("android:id/button1"));
        ok.clickAndWaitForNewWindow();
        sleep(1000);
        pixel = recyclerView.getChildByInstance(new UiSelector().resourceId("com.transcend.cvr:id/settingsStatus"), 0);
        Assert.assertEquals(pixelText, pixel.getText());

        sleep(3000);

        //刷掉程式
        closeAPP(mDevice, "com.transcend.cvr");
        sleep(1000);
        startAPP("com.transcend.cvr");
        sleep(5000);

        //點開drawer
        openDrawer();

        //點擊設定
        clickDrawerSetting();

        sleep(1000);

        autoTurnOffScreen = recyclerView.getChildByInstance(new UiSelector().resourceId("com.transcend.cvr:id/settingsStatus"), 6);
        pixel = recyclerView.getChildByInstance(new UiSelector().resourceId("com.transcend.cvr:id/settingsStatus"), 0);

        Assert.assertEquals(autoTurnOffScreenTime, autoTurnOffScreen.getText());
        Assert.assertEquals(pixelText, pixel.getText());
    }

    //TODO issue  19, 22
    @Test
    public void issue22() throws UiObjectNotFoundException {
        //打開Drawer
        openDrawer();

        //點擊遠端
        clickDrawerRemote();

        int type = 0;
        while(type < 2){
            clickTabItem(type);

            //點擊影片
            //找到界面上的元素。
            UiScrollable recyclerView = getMainRecyclerView();
            int total = getItemCount(recyclerView);
            if (total > 1)
                total = 1;
            for(int count = 0; count < total; count++){
                UiObject item = getItem(recyclerView, 0);
                if (item.exists())
                    item.clickAndWaitForNewWindow();
                else {
                    break;
                }

                //取得現在時間與總時間
                UiObject totalTimeObj = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/video_total_time"));
                UiObject currentTime = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/video_current_time"));
                int totalSleepTime = 0;
                int preTime = 0;
                if (totalTimeObj.exists() && currentTime.exists()){
                    String [] totalTimeSplit = totalTimeObj.getText().split(":");
                    int totalH = Integer.parseInt(totalTimeSplit[0]);
                    int totalM = Integer.parseInt(totalTimeSplit[1]);
                    int totalTime = totalH * 60 + totalM;

                    String [] currentTimeSplit = currentTime.getText().split(":");
                    int currentH = Integer.parseInt(currentTimeSplit[0]);
                    int currentM = Integer.parseInt(currentTimeSplit[1]);
                    int current = currentH * 60 + currentM;

                    //總時間須比現在時間長
                    Assert.assertEquals(true, totalTime >= current);

                    sleep(1000);
                    totalSleepTime++;

                    while(totalSleepTime < totalTime){
                        currentTimeSplit = currentTime.getText().split(":");
                        currentH = Integer.parseInt(currentTimeSplit[0]);
                        currentM = Integer.parseInt(currentTimeSplit[1]);
                        current = currentH * 60 + currentM;

                        Assert.assertEquals(true, totalTime >= current);

                        if (current == 0)
                            preTime = -1;
                        Assert.assertEquals(true, preTime <= current);
                        preTime = current;

                        sleep(1000);
                        totalSleepTime++;
                    }
                }

                UiObject next = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/vlc_button_next"));
                next.click();
                sleep(3000);

                //時間歸零
                totalSleepTime = 0;

                totalTimeObj = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/video_total_time"));
                preTime = 0;
                if (totalTimeObj.exists() && currentTime.exists()){
                    String [] totalTimeSplit = totalTimeObj.getText().split(":");
                    int totalH = Integer.parseInt(totalTimeSplit[0]);
                    int totalM = Integer.parseInt(totalTimeSplit[1]);
                    int totalTime = totalH * 60 + totalM;

                    String [] currentTimeSplit = currentTime.getText().split(":");
                    int currentH = Integer.parseInt(currentTimeSplit[0]);
                    int currentM = Integer.parseInt(currentTimeSplit[1]);
                    int current = currentH * 60 + currentM;

                    //總時間須比現在時間長
                    Assert.assertEquals(true, totalTime >= current);

                    sleep(1000);
                    totalSleepTime++;

                    while(totalSleepTime < totalTime){
                        currentTimeSplit = currentTime.getText().split(":");
                        currentH = Integer.parseInt(currentTimeSplit[0]);
                        currentM = Integer.parseInt(currentTimeSplit[1]);
                        current = currentH * 60 + currentM;

                        Assert.assertEquals(true, totalTime >= current);
                        if (current == 0)
                            preTime = -1;
                        Assert.assertEquals(true, preTime <= current);
                        preTime = current;

                        sleep(1000);
                        totalSleepTime++;
                    }
                }
                mDevice.pressBack();
            }
            type++;
        }
    }

    //刪除後檢查檔案列表
    @Test
    public void issue_25() throws UiObjectNotFoundException {
        int type = 0;
        while(type < 2){

            switch (type){
                case 0:
                    //打開Drawer
                    openDrawer();
                    //點擊遠端
                    clickDrawerRemote();
                    break;
                default:
                    //打開Drawer
                    openDrawer();
                    //點擊本地
                    clickDrawerMyStorage();
                    break;
            }

            UiScrollable recycler = getMainRecyclerView();
            if (getItemCount(recycler) > 0) {
                UiObject item = getItem(recycler, 0);
                String itemTitle = getItemTitle(recycler, 0).getText();
                item.click();
                sleep(1000);

                deleteItemInVideoView();
                sleep(1000);

                mDevice.pressBack();
                waitForDialog();

                //檢查檔案清單是否有更新(此處檢查第一個檔案名是否有改變)
                recycler = getMainRecyclerView();
                Assert.assertEquals(false, getItemTitle(recycler, 0).getText().equals(itemTitle));

                item = getItem(recycler, 0);
                itemTitle = getItemTitle(recycler, 0).getText();
                item.click();
                sleep(1000);

                deleteItemInVideoView();
                sleep(1000);

                //左上角返回鍵
                UiObject toggleBack = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/toggle_btn"));
                toggleBack.click();
                waitForDialog();

                //檢查檔案清單是否有更新(此處檢查第一個檔案名是否有改變)
                recycler = getMainRecyclerView();
                Assert.assertEquals(false, getItemTitle(recycler, 0).getText().equals(itemTitle));
            }
            type++;
        }
    }
}