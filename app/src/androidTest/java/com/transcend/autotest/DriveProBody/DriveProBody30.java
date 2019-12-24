package com.transcend.autotest.DriveProBody;

import android.os.RemoteException;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiCollection;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DriveProBody30 extends DriveProBodyAbstract{

    @Before
    public void setup() throws UiObjectNotFoundException {
        super.setup();

        //一律回到 Home才開始測試
        openDrawer();
        clickDrawerHome();
        waitForDialog();
    }

    //TODO issue30_01
    @Test
    public void issue30_01() throws UiObjectNotFoundException, RemoteException {
        //打開drawer
        openDrawer();

        //點擊設定
        clickDrawerSetting();

        //取得並點擊 "裝置"
        UiScrollable settingRecycler = new UiScrollable(new UiSelector().resourceId("com.transcend.bcr:id/settings_recycler_view"));
        settingRecycler.scrollToEnd(5, 10);
        UiObject deviceObj = settingRecycler.getChildByText(new UiSelector().resourceId("com.transcend.bcr:id/settingsSubtitle"), "裝置");
        deviceObj.clickAndWaitForNewWindow();

        sleep(1000);

        UiObject device1 = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/device_one"));
        UiObject device2 = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/device_two"));
        UiObject device3 = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/device_three"));
        UiObject device4 = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/device_four"));

        UiObject image1 = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/device_one_figure"));
        UiObject image2 = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/device_two_figure"));
        UiObject image3 = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/device_three_figure"));
        UiObject image4 = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/device_four_figure"));

        //圖片應該被包在layout裡
        Assert.assertEquals(true, ((device1.getBounds().top <= image1.getBounds().top) || (device1.getBounds().bottom >= image1.getBounds().bottom)));
        Assert.assertEquals(true, ((device2.getBounds().top <= image2.getBounds().top) || (device2.getBounds().bottom >= image2.getBounds().bottom)));
        Assert.assertEquals(true, ((device3.getBounds().top <= image3.getBounds().top) || (device3.getBounds().bottom >= image3.getBounds().bottom)));
        Assert.assertEquals(true, ((device4.getBounds().top <= image4.getBounds().top) || (device4.getBounds().bottom >= image4.getBounds().bottom)));

        //轉橫屏，再判斷一次
        mDevice.setOrientationLeft();

        device1 = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/device_one"));
        device2 = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/device_two"));
        device3 = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/device_three"));
        device4 = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/device_four"));

        image1 = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/device_one_figure"));
        image2 = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/device_two_figure"));
        image3 = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/device_three_figure"));
        image4 = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/device_four_figure"));

        Assert.assertEquals(true, ((device1.getBounds().top <= image1.getBounds().top) || (device1.getBounds().bottom >= image1.getBounds().bottom)));
        Assert.assertEquals(true, ((device2.getBounds().top <= image2.getBounds().top) || (device2.getBounds().bottom >= image2.getBounds().bottom)));
        Assert.assertEquals(true, ((device3.getBounds().top <= image3.getBounds().top) || (device3.getBounds().bottom >= image3.getBounds().bottom)));
        Assert.assertEquals(true, ((device4.getBounds().top <= image4.getBounds().top) || (device4.getBounds().bottom >= image4.getBounds().bottom)));
    }

    //TODO issue30_05
    @Test
    public void issue30_05() throws UiObjectNotFoundException {
        //打開drawer
        openDrawer();

        //點擊瀏覽
        clickDrawerBrowser();

        //點擊第一個檔案
        UiScrollable browserRecycler = getMainRecyclerView();

        //刪除次數，大於兩個的檔案則僅刪除兩個，否則全刪
        int times = 0;
        if (getItemCount(browserRecycler) >= 2)
            times = 2;
        else
            times = getItemCount(browserRecycler);
        for (int count = 0; count < times; count++) {
            UiObject item1 = getItem(browserRecycler, count);
            item1.clickAndWaitForNewWindow();

            //刪除檔案
            deleteItemInVideoView();

            browserRecycler = getMainRecyclerView();
            //不該回到瀏覽頁面
            Assert.assertEquals(false, browserRecycler.exists());

            mDevice.pressBack();
        }
    }

    //TODO issue30_06, 12
    @Test
    public void issue30_06() throws UiObjectNotFoundException, RemoteException {
        //打開drawer
        openDrawer();

        //點擊我的儲存
        clickDrawerMyStorage();

        int position = 0;
        while(position < Local_Browser_Types.values().length){
            clickTabItem(position);

            //取得檔案個數
            UiScrollable storageRecycler = getMainRecyclerView();
            int itemCount = getItemCount(storageRecycler);

            //試著取得沒有檔案時的圖示，以及有檔案時必須存在的more btn
            UiObject noDataImage = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/empty_view"));
            UiObject moreBtn = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/btn_local_media_select"));

            if (itemCount == 0){
                Assert.assertEquals(true, noDataImage.exists());
                Assert.assertEquals(false, moreBtn.exists());
            }
            else{
                Assert.assertEquals(false, noDataImage.exists());
                Assert.assertEquals(true, moreBtn.exists());

                //判斷旋轉後排版是否跑掉  issue60_12
                checkComposing(storageRecycler);
            }

            position++;
        }

        //打開drawer
        openDrawer();

        //點擊瀏覽
        clickDrawerBrowser();

        position = 0;
        while(position < Remote_Browser_Types.values().length){
            clickTabItem(position);

            //取得檔案個數
            UiScrollable browserRecycler = getMainRecyclerView();
            int itemCount = getItemCount(browserRecycler);

            //試著取得沒有檔案時的圖示
            UiObject noDataImage = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/empty_view"));
            if (itemCount == 0){
                Assert.assertEquals(true, noDataImage.exists());
            }
            else{
                Assert.assertEquals(false, noDataImage.exists());

                //判斷旋轉後排版是否跑掉  issue60_12
                checkComposing(browserRecycler);
            }
            position++;
        }
    }

    //TODO issue30_14
    @Test
    public void issue30_14() throws UiObjectNotFoundException, RemoteException {
        //打開drawer
        openDrawer();

        //點擊瀏覽
        clickDrawerBrowser();

        //點擊第一個檔案
        UiScrollable browserRecycler = getMainRecyclerView();
        for (int count = 0; count < getItemCount(browserRecycler); count++){
            UiObject item1 = getItem(browserRecycler, count);
            item1.clickAndWaitForNewWindow();

            for(int i = 0; i < 3; i++){
                mDevice.setOrientationLeft();
                sleep(400);
                UiCollection imageLayout = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/image_layout"));
                Assert.assertEquals(1, imageLayout.getChildCount(new UiSelector().resourceId("com.transcend.bcr:id/video_image")));

                mDevice.setOrientationNatural();
                sleep(400);
                imageLayout = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/image_layout"));
                Assert.assertEquals(1, imageLayout.getChildCount(new UiSelector().resourceId("com.transcend.bcr:id/video_image")));
            }
            mDevice.unfreezeRotation();
            mDevice.pressBack();
        }

        //點擊圖片tab
        clickTabItem(1);

        browserRecycler = getMainRecyclerView();
        for (int count = 0; count < getItemCount(browserRecycler); count++){
            UiObject item1 = getItem(browserRecycler, count);
            item1.clickAndWaitForNewWindow();

            for(int i = 0; i < 3; i++){
                mDevice.setOrientationLeft();
                sleep(400);
                UiCollection imageLayout = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/photo_view_pager"));
                Assert.assertEquals(1, imageLayout.getChildCount(new UiSelector().resourceId("com.transcend.bcr:id/imageView")));

                mDevice.setOrientationNatural();
                sleep(400);
                imageLayout = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/photo_view_pager"));
                Assert.assertEquals(1, imageLayout.getChildCount(new UiSelector().resourceId("com.transcend.bcr:id/imageView")));
            }
            mDevice.unfreezeRotation();
            mDevice.pressBack();
        }


        //打開drawer
        openDrawer();

        //點擊我的儲存
        clickDrawerMyStorage();

        //此處跑影片及緊急錄影
        int position = 0;
        while(position < 2) {
            clickTabItem(position);

            //點擊第一個檔案
            UiScrollable storageRecycler = getMainRecyclerView();
            for (int count = 0; count < getItemCount(storageRecycler); count++) {
                UiObject item1 = getItem(storageRecycler, count);
                item1.clickAndWaitForNewWindow();

                for (int i = 0; i < 3; i++) {
                    mDevice.setOrientationLeft();
                    sleep(400);
                    UiCollection imageLayout = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/image_layout"));
                    Assert.assertEquals(1, imageLayout.getChildCount(new UiSelector().resourceId("com.transcend.bcr:id/video_image")));

                    mDevice.setOrientationNatural();
                    sleep(400);
                    imageLayout = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/image_layout"));
                    Assert.assertEquals(1, imageLayout.getChildCount(new UiSelector().resourceId("com.transcend.bcr:id/video_image")));
                }
                mDevice.unfreezeRotation();
                mDevice.pressBack();
            }
            position++;
        }

        //點擊圖片tab
        clickTabItem(2);

        UiScrollable storageRecycler = getMainRecyclerView();
        for (int count = 0; count < getItemCount(storageRecycler); count++){
            UiObject item1 = getItem(storageRecycler, count);
            item1.clickAndWaitForNewWindow();

            for(int i = 0; i < 3; i++){
                mDevice.setOrientationLeft();
                sleep(400);
                UiCollection imageLayout = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/photo_view_pager"));
                Assert.assertEquals(1, imageLayout.getChildCount(new UiSelector().resourceId("com.transcend.bcr:id/imageView")));

                mDevice.setOrientationNatural();
                sleep(400);
                imageLayout = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/photo_view_pager"));
                Assert.assertEquals(1, imageLayout.getChildCount(new UiSelector().resourceId("com.transcend.bcr:id/imageView")));
            }
            mDevice.unfreezeRotation();
            mDevice.pressBack();
        }
    }

    //TODO issue30_15
    @Test
    public void issue30_15() throws UiObjectNotFoundException, RemoteException {
        //打開drawer
        openDrawer();

        //點擊我的儲存
        clickDrawerMyStorage();

        mDevice.setOrientationLeft();

        UiScrollable storageRecycler = getMainRecyclerView();
        int item_count = getItemCount(storageRecycler);
        if (item_count == 0)
            return;

        mDevice.setOrientationNatural();

        //打開drawer
        openDrawer();

        //點擊首頁
        clickDrawerHome();

        mDevice.setOrientationLeft();

        //打開drawer
        openDrawer();

        //點擊我的儲存
        clickDrawerMyStorage();

        //檢查我的儲存排版
        storageRecycler = getMainRecyclerView();
        checkComposing(storageRecycler);

        //-----------以下換Browser---------//

        mDevice.setOrientationNatural();

        //打開drawer
        openDrawer();

        //點擊首頁
        clickDrawerHome();

        mDevice.setOrientationLeft();

        //打開drawer
        openDrawer();

        //點擊瀏覽
        clickDrawerBrowser();

        UiScrollable browserRecycler = getMainRecyclerView();
        checkComposing(browserRecycler);
    }

    //TODO issue30_17
    @Test
    public void issue30_17() throws UiObjectNotFoundException, RemoteException {
        //打開drawer
        openDrawer();

        //點擊我的儲存
        clickDrawerMyStorage();
        //此處run過影片及緊急錄影
        int position = 0;
        while(position < 2) {
            clickTabItem(position);

            //點擊第一個物件
            UiScrollable storageRecycler = getMainRecyclerView();
            for (int count = 0; count < getItemCount(storageRecycler); count++) {
                UiObject gridItem1 = getItem(storageRecycler, count);
                gridItem1.clickAndWaitForNewWindow();

                //點擊返回
                UiCollection toolbar = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/video_header_bar"));
                UiObject backObj = toolbar.getChildByInstance(new UiSelector().className("android.widget.ImageButton"), 0);
                backObj.click();

                //點擊第一個物件，轉橫屏再返回
                gridItem1.clickAndWaitForNewWindow();
                mDevice.setOrientationLeft();
                mDevice.pressBack();    //landscape沒有返回鍵

                //轉豎屏
                mDevice.setOrientationNatural();

                //若有確實回到瀏覽畫面，那item1應該要存在
                storageRecycler = getMainRecyclerView();
                gridItem1 = getItem(storageRecycler, count);
                Assert.assertEquals(true, gridItem1.exists());
            }
            position++;
        }

        //打開drawer
        openDrawer();

        //點擊瀏覽
        clickDrawerBrowser();

        //點擊第一個檔案
        UiScrollable browserRecycler = getMainRecyclerView();
        for (int count = 0; count < getItemCount(browserRecycler); count++){
            UiObject item1 = getItem(browserRecycler, count);
            item1.clickAndWaitForNewWindow();

            //點擊返回
            UiCollection toolbar = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/video_header_bar"));
            UiObject backObj = toolbar.getChildByInstance(new UiSelector().className("android.widget.ImageButton"), 0);
            backObj.click();

            //點擊第一個物件，轉橫屏再返回
            item1.clickAndWaitForNewWindow();
            mDevice.setOrientationLeft();
            mDevice.pressBack();    //landscape沒有返回鍵

            //轉豎屏
            mDevice.setOrientationNatural();

            //若有確實回到瀏覽畫面，那item1應該要存在
            browserRecycler = getMainRecyclerView();
            item1 = getItem(browserRecycler, count);
            Assert.assertEquals(true, item1.exists());
        }

        mDevice.unfreezeRotation();
    }

    //TODO issue30_18
    @Test
    public void issue30_18() throws UiObjectNotFoundException {
        //打開drawer
        openDrawer();

        //點擊我的儲存
        clickDrawerMyStorage();

        //進入選取模式的按鈕
//        UiObject selectBtn = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/btn_media_select_img"));
//        selectBtn.click();

        UiObject moreBtnObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/btn_local_media_select"));
        moreBtnObj.click();

        UiCollection selectMenu = new UiCollection(new UiSelector().className("android.widget.ListView"));
        UiObject selectObj = selectMenu.getChildByText(new UiSelector().resourceId("com.transcend.bcr:id/title"), "選取");
        selectObj.click();

        //長按第一個物件
        UiScrollable storageRecycler = getMainRecyclerView();
        UiObject item = getItem(storageRecycler, 0);
        item.click();

        //點擊more btn
        moreBtnObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/btn_local_media_select"));
        moreBtnObj.click();

        UiCollection list = new UiCollection(new UiSelector().className("android.widget.ListView"));
        UiObject deleteBtn = list.getChildByInstance(new UiSelector().className("android.widget.LinearLayout"), 1);
        deleteBtn.click();
        waitForDialog();

        Assert.assertEquals(true, getMainRecyclerView().exists());
    }

    //TODO issue30_19
    @Test
    public void issue30_19() throws UiObjectNotFoundException {
        //打開drawer
        openDrawer();

        //點擊瀏覽
        clickDrawerBrowser();

        //點擊第一個檔案
        UiScrollable browserRecycler = getMainRecyclerView();
        UiObject item1 = getItem(browserRecycler, 0);
        if (!item1.exists())
            return;
        item1.clickAndWaitForNewWindow();

        UiObject playBtn = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_play"));
        playBtn.click();
        UiObject currentTimeObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_current_time"));
        String initTime = currentTimeObj.getText();

        //讓他播個五秒
        sleep(5000);

        makeVideoControlVisible();
        deleteItemInVideoView();

        currentTimeObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_current_time"));
        if (currentTimeObj.exists())
            Assert.assertEquals(initTime, currentTimeObj.getText());

        mDevice.pressBack();

        browserRecycler = getMainRecyclerView();
        item1 = getItem(browserRecycler, 0);
        item1.clickAndWaitForNewWindow();

        playBtn = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_play"));
        playBtn.click();
        currentTimeObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_current_time"));
        initTime = currentTimeObj.getText();

        sleep(5000);

        makeVideoControlVisible();
        deleteItemInVideoView();

        currentTimeObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_current_time"));
        if (currentTimeObj.exists())
            Assert.assertEquals(initTime, currentTimeObj.getText());

        if (!getMainRecyclerView().exists()){
            mDevice.pressBack();
        }
    }

    //TODO issue30_21
    @Test
    public void issue30_21() throws UiObjectNotFoundException, RemoteException {
        //打開drawer
        openDrawer();

        //點擊我的儲存
        clickDrawerMyStorage();

        //紀錄toolbar的位置
        UiObject toolbar = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/toolbar"));
        int centerX = toolbar.getBounds().centerX();
        int centerY = toolbar.getBounds().centerY();

        //點擊第一個物件
        UiScrollable storageRecycler = getMainRecyclerView();
        //Log.e("issue30_21", ""+storageRecycler.getChildCount(new UiSelector().className("android.widget.FrameLayout")));
        for (int count = 0; count < getItemCount(storageRecycler); count++){
            UiObject item = getItem(storageRecycler, count);
            item.clickAndWaitForNewWindow();

            //旋轉3次
            for(int i = 0; i < 3; i++){
                mDevice.setOrientationLeft();
                sleep(400);
                UiCollection imageLayout = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/image_layout"));
                Assert.assertEquals(1, imageLayout.getChildCount(new UiSelector().resourceId("com.transcend.bcr:id/video_image")));

                mDevice.setOrientationNatural();
                sleep(400);
                imageLayout = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/image_layout"));
                Assert.assertEquals(1, imageLayout.getChildCount(new UiSelector().resourceId("com.transcend.bcr:id/video_image")));
            }
            mDevice.unfreezeRotation();

            mDevice.pressBack();
            toolbar = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/toolbar"));

            Assert.assertEquals(centerX, toolbar.getBounds().centerX());
            Assert.assertEquals(centerY, toolbar.getBounds().centerY());
        }
    }

    //TODO issue30_23
    @Test
    public void issue30_23() throws UiObjectNotFoundException{
        //打開drawer
        openDrawer();

        //點擊我的儲存
        clickDrawerMyStorage();

        //點擊第一個物件
        UiScrollable storageRecycler = getMainRecyclerView();
        int itemCount = getItemCount(storageRecycler);
        for (int count = 0; count < itemCount; count++){
            //點擊物件
            UiObject item = getItem(storageRecycler, count);
            item.clickAndWaitForNewWindow();

            //向後滑動隨機次數
            int random = (int)(Math.random()*itemCount);
            boolean isPlay = false;
            for (int i = 0; i < random; i++){
                //影片點擊完播放等待一段時間才滑動
                UiObject playObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_play"));
                if (playObj.exists() && !isPlay){
                    sleep(1000);
                    playObj.click();
                    sleep(5000);
                    isPlay = true;
                }
                //點擊下一首
                //swipeToNextViewPager();
                if (isPlay) {

                }
                makeVideoControlVisible();
                UiObject nextObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_next"));
                nextObj.click();
                sleep(1000);
            }

            mDevice.pressBack();
        }
    }

    //TODO issue30_24
    @Test
    public void issue30_24() throws UiObjectNotFoundException, RemoteException {
        //點開drawer
        openDrawer();

        //點擊browser
        clickDrawerHome();

        //轉橫屏
        mDevice.setOrientationLeft();

        //pad應開放landscape、phone則否
        if (Testing_device == Test_Device_Type.Pad.ordinal())
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
//        mDevice.setOrientationLeft();
//
//        //pad應開放landscape、phone則否
//        if (Testing_device == Pad)
//            Assert.assertEquals(true, mDevice.getDisplayWidth() > mDevice.getDisplayHeight());
//        else
//            Assert.assertEquals(true, mDevice.getDisplayWidth() > mDevice.getDisplayHeight());
//
//        mDevice.setOrientationNatural();
//        mDevice.unfreezeRotation();

        //點開drawer
        openDrawer();

        //點擊browser
        clickDrawerBrowser();

        //轉橫屏
        mDevice.setOrientationLeft();

        //pad應開放landscape、phone則否
        if (Testing_device == Test_Device_Type.Pad.ordinal())
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
        if (Testing_device == Test_Device_Type.Pad.ordinal())
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
        if (Testing_device == Test_Device_Type.Pad.ordinal())
            Assert.assertEquals(true, mDevice.getDisplayWidth() > mDevice.getDisplayHeight());
        else
            Assert.assertEquals(false, mDevice.getDisplayWidth() > mDevice.getDisplayHeight());

        mDevice.setOrientationNatural();
        mDevice.unfreezeRotation();
    }

    //TODO issue60_31
    @Test
    public void issue60_31() throws UiObjectNotFoundException, RemoteException {
        //點開drawer
        openDrawer();

        //點擊live view
        clickDrawerLiveView();

        //旋轉5次
        for (int i = 0; i < 5; i++){
            mDevice.setOrientationLeft();
            sleep(400);
            mDevice.setOrientationNatural();
            sleep(400);
        }
        mDevice.unfreezeRotation();

        //點開drawer
        openDrawer();

        //點擊我的儲存
        clickDrawerMyStorage();

        UiScrollable storageRecycler = getMainRecyclerView();
        checkComposing(storageRecycler);
/*
        UiScrollable storageRecycler = getMainRecyclerView();
        UiObject gridItem1, gridItem2, gridItem3, gridItem4, gridItem5, gridItem6, gridItem7;
        //grid item裡還有一個FrameLayout，故要跳著索取
        gridItem1 = storageRecycler.getChildByInstance(new UiSelector().className("android.widget.FrameLayout"), 0);
        gridItem2 = storageRecycler.getChildByInstance(new UiSelector().className("android.widget.FrameLayout"), 2);
        gridItem3 = storageRecycler.getChildByInstance(new UiSelector().className("android.widget.FrameLayout"), 4);
        gridItem4 = storageRecycler.getChildByInstance(new UiSelector().className("android.widget.FrameLayout"), 6);
        gridItem5 = storageRecycler.getChildByInstance(new UiSelector().className("android.widget.FrameLayout"), 8);
        gridItem6 = storageRecycler.getChildByInstance(new UiSelector().className("android.widget.FrameLayout"), 10);
        gridItem7 = storageRecycler.getChildByInstance(new UiSelector().className("android.widget.FrameLayout"), 12);

        if (Testing_device == Phone){
            mDevice.setOrientationNatural();
            //判斷前三個是否在同一水平上;
            boolean isAtSameLine = (gridItem1.getBounds().centerY() == gridItem2.getBounds().centerY()) && (gridItem2.getBounds().centerY() == gridItem3.getBounds().centerY()) &&
                    (gridItem1.getBounds().centerY() != gridItem4.getBounds().centerY());

            Assert.assertEquals(true, isAtSameLine);
        }
        else{
            mDevice.setOrientationNatural();
            //判斷前六個是否在同一水平上;
            boolean isAtSameLine = (gridItem1.getBounds().centerY() == gridItem2.getBounds().centerY()) && (gridItem2.getBounds().centerY() == gridItem3.getBounds().centerY()) &&
                    (gridItem3.getBounds().centerY() == gridItem4.getBounds().centerY()) && (gridItem4.getBounds().centerY() == gridItem5.getBounds().centerY()) &&
                    (gridItem5.getBounds().centerY() == gridItem6.getBounds().centerY())  && (gridItem1.getBounds().centerY() != gridItem7.getBounds().centerY());

            Assert.assertEquals(true, isAtSameLine);
        }
        mDevice.unfreezeRotation();*/
    }

    //TODO issue30_32
    @Test
    public void issue30_32() throws UiObjectNotFoundException, RemoteException {
        //打開drawer
        openDrawer();

        //點擊瀏覽
        clickDrawerBrowser();

        //點擊第一個檔案
        UiScrollable browserRecycler = getMainRecyclerView();
        int total = getItemCount(browserRecycler);
        if (total > 4)  //全下載會等太久...
            total = 4;
        for (int count = 0; count < total; count++){
            UiObject item1 = getItem(browserRecycler, count);
            item1.clickAndWaitForNewWindow();

            //轉豎屏
            mDevice.setOrientationLeft();

            //點一下螢幕讓控制bar出現
//            UiObject surfaceView = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/surface_view"));
//            surfaceView.clickBottomRight();

            //下載
            downloadItemInVideoView();

            //等待下載完成
            UiObject titleObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_toolbar_title"));
            while(!titleObj.exists()){
                titleObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_toolbar_title"));
            }

            for(int i = 0; i < 3; i++){
                mDevice.setOrientationLeft();
                sleep(400);
                UiCollection imageLayout = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/image_layout"));
                Assert.assertEquals(1, imageLayout.getChildCount(new UiSelector().resourceId("com.transcend.bcr:id/video_image")));

                mDevice.setOrientationNatural();
                sleep(400);
                imageLayout = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/image_layout"));
                Assert.assertEquals(1, imageLayout.getChildCount(new UiSelector().resourceId("com.transcend.bcr:id/video_image")));
            }
            mDevice.unfreezeRotation();

            //返回至瀏覽頁面
            mDevice.pressBack();

            browserRecycler = getMainRecyclerView();
            checkComposing(browserRecycler);

            /*
            UiScrollable storageRecycler = new UiScrollable(new UiSelector().resourceId("com.transcend.bcr:id/browser_view_pager"));
            UiObject gridItem1, gridItem2, gridItem3, gridItem4, gridItem5, gridItem6, gridItem7;
            //grid item裡還有一個FrameLayout，故要跳著索取
            gridItem1 = storageRecycler.getChildByInstance(new UiSelector().className("android.widget.FrameLayout"), 0);
            gridItem2 = storageRecycler.getChildByInstance(new UiSelector().className("android.widget.FrameLayout"), 2);
            gridItem3 = storageRecycler.getChildByInstance(new UiSelector().className("android.widget.FrameLayout"), 4);
            gridItem4 = storageRecycler.getChildByInstance(new UiSelector().className("android.widget.FrameLayout"), 6);
            gridItem5 = storageRecycler.getChildByInstance(new UiSelector().className("android.widget.FrameLayout"), 8);
            gridItem6 = storageRecycler.getChildByInstance(new UiSelector().className("android.widget.FrameLayout"), 10);
            gridItem7 = storageRecycler.getChildByInstance(new UiSelector().className("android.widget.FrameLayout"), 12);

            if (Testing_device == Phone){
                mDevice.setOrientationNatural();
                //判斷前三個是否在同一水平上;
                boolean isAtSameLine = (gridItem1.getBounds().centerY() == gridItem2.getBounds().centerY()) && (gridItem2.getBounds().centerY() == gridItem3.getBounds().centerY()) &&
                        (gridItem1.getBounds().centerY() != gridItem4.getBounds().centerY());

                Assert.assertEquals(true, isAtSameLine);
            }
            else{
                mDevice.setOrientationNatural();
                //判斷前六個是否在同一水平上;
                boolean isAtSameLine = (gridItem1.getBounds().centerY() == gridItem2.getBounds().centerY()) && (gridItem2.getBounds().centerY() == gridItem3.getBounds().centerY()) &&
                        (gridItem3.getBounds().centerY() == gridItem4.getBounds().centerY()) && (gridItem4.getBounds().centerY() == gridItem5.getBounds().centerY()) &&
                        (gridItem5.getBounds().centerY() == gridItem6.getBounds().centerY())  && (gridItem1.getBounds().centerY() != gridItem7.getBounds().centerY());

                Assert.assertEquals(true, isAtSameLine);
            }
            mDevice.unfreezeRotation();*/
        }
    }

    //TODO issue30_35
    @Test
    public void issue30_35() throws UiObjectNotFoundException, RemoteException {
        //打開drawer
        openDrawer();

        //點擊live view
        clickDrawerLiveView();

        //打開drawer
        openDrawer();

        //轉橫屏
        mDevice.setOrientationLeft();

        //點擊首頁
        clickDrawerHome();

        UiObject liveViewObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/layout_live_streaming"));
        UiObject browserObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/layout_browser"));

        Assert.assertEquals(true, liveViewObj.getBounds().centerY() < browserObj.getBounds().centerY());
        mDevice.unfreezeRotation();
    }
}
