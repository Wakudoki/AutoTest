package com.transcend.autotest.StoreJetCloud;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiCollection;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.test.suitebuilder.annotation.LargeTest;
import android.util.Log;
import android.widget.Toast;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static android.support.test.InstrumentationRegistry.getContext;
import static android.support.test.InstrumentationRegistry.getInstrumentation;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class StoreJetCloud_BugList extends StoreJetCloudAbstract{

    @Before
    public void setup() throws UiObjectNotFoundException, RemoteException {
        super.setup();

        //刷掉程式
        closeAPP(mDevice, "com.transcend.nas");

        sleep(1000);

        //開啟SJC
        startAPP("com.transcend.nas");

        UiObject view_license = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/btn_view_license_agreement"));
        if (view_license.exists()){ //存在，開始登入
            view_license.clickAndWaitForNewWindow();
            license_process();
        }

        UiObject login = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/login_by_email"));
        if (login.exists()){
            login_process();
        }
        waitForProgress();

        //判斷是否進到主頁面
        UiScrollable mainRecycler = getMainRecyclerView();
        while (!mainRecycler.exists()) {
            sleep(1000);

            UiObject auto_backup = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/title_text"));
            if (auto_backup.exists()){
                UiObject not_now = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/cancel_text"));
                not_now.click();
            }

            mainRecycler = getMainRecyclerView();
        }
    }

    //TODO issue22  登入流程測試，須將程式的資料清除後操作
    @Test
    public void issue22(){

        sleep(1000);
    }

    //TODO issue1, 6
    @Test
    public void issue1() throws UiObjectNotFoundException {
        //打開drawer
        openDrawer();

        //點擊最近
        clickDrawerRecent();

        int swipeCount = 0, maxSwipeCount = 5;  //找不到檔案時的滑動次數
        String deleteItemTitle = null;  //紀錄刪除的檔名

        while(deleteItemTitle == null){
            //大於預設滑動次數則強制離開
            if (swipeCount >= maxSwipeCount)
                break;

            UiScrollable recyclerView = getRecentRecyclerView();

            //搜尋目前畫面呈現的所有檔案
            int currentListCount = getItemCount(recyclerView);
            for(int i = 0; i < currentListCount; i++){
                UiObject item = getListItemTitle(recyclerView, i);
                String title = item.getText();
                String [] titleSplit = title.split("\\.");
                String type = titleSplit[titleSplit.length-1].toLowerCase();

                if (type.toLowerCase().equals("jpg") || type.toLowerCase().equals("png")){
                    item.clickAndWaitForNewWindow();

                    //判斷是否有正確的進到single view
                    UiObject image = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/viewer_image"));
                    if (image.exists()) {
                        //刪除物件
                        DeleteItemInImageView();

                        deleteItemTitle = item.getText();

                        image = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/viewer_image"));
                        //判斷是否還停留在image視窗
                        Assert.assertEquals(false, image.exists());

                        break;
                    }
                    else{   //點到刪除後的檔案，toast出找不到檔案後會自動清掉該檔案，故要把 i 減回來
                        i--;
                    }
                }
            }

            if (deleteItemTitle != null) {
                scrollToEndOrTop(recyclerView, false);
                sleep(3000);
                //判斷刪除後的物件會出現在列表首頁，判斷是否還存在
                UiObject item = null;
                try{
                    item = recyclerView.getChildByText(new UiSelector().resourceId("com.transcend.nas:id/listitem_file_manage_title"), deleteItemTitle);
                } catch (UiObjectNotFoundException e){
                    Assert.assertEquals(null, item);
                }
            }
            else{   //找不到圖片檔，往下滑一階
                recyclerView.scrollToEnd(1);
                swipeCount++;
            }
        }
    }

    //TODO issue2, 38
    @Test
    public void issue2() throws UiObjectNotFoundException, RemoteException {
        //進到Public資料夾
        goToPublicFolder();

        //進到Mike資料夾
        goToMikeFolder();

        UiScrollable recyclerView = getMainRecyclerView();
        UiObject item = null;

        if (isGridView())
            changeBrowserViewType();

        //搜尋目前畫面呈現的所有檔案
        int currentListCount = getItemCount(recyclerView);
        for(int i = 0; i < currentListCount; i++){
            item = getItem(recyclerView, i);
            String title = item.getText();
            String [] titleSplit = title.split("\\.");
            String type = titleSplit[titleSplit.length-1].toLowerCase();

            if (type.equals("jpg") || type.equals("png")){
                UiObject itemIcon = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/item_icon"));
                int center_x = itemIcon.getBounds().centerX();
                int center_y = itemIcon.getBounds().centerY();
                item.clickAndWaitForNewWindow();

                //判斷是否有正確的進到single view
                UiObject image = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/viewer_image"));
                if (image.exists()) {
                    //旋轉
                    mDevice.setOrientationLeft();
                    sleep(400);
                    mDevice.setOrientationNatural();
                    sleep(400);

                    //返回
                    mDevice.pressBack();
                    mDevice.unfreezeRotation();

                    //確認圖標有無跑掉
                    itemIcon = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/item_icon"));
                    Assert.assertEquals(true, itemIcon.exists());
                    Assert.assertEquals(true, ((center_x == itemIcon.getBounds().centerX()) && (center_y == itemIcon.getBounds().centerY())));
                }
            }
        }
    }

    //TODO issue8
    @Test
    public void issue8() throws UiObjectNotFoundException, RemoteException {
        //進到Public資料夾
        goToPublicFolder();

        //進到Mike資料夾
        goToMikeFolder();

        //打開menu
        openMenu();

        //點擊上傳
        clickMenuUpload();

        UiScrollable recyclerView = getMainRecyclerView();

        if (!isGridView()) {
            UiObject changeViewType = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/file_picker_viewer_action_view"));
            changeViewType.click();
        }

        //依螢幕長寬判斷圖檔個數
        checkComposing(recyclerView, false);
    }

    //TODO issue12
    @Test
    public void issue12() throws UiObjectNotFoundException {
        //進到public資料夾
        goToPublicFolder();

        //點擊image tab
        clickTabImage();

        if (!isBrowserTitleEquals("依資料夾顯示")){
            //打開menu
            openMenu();

            UiCollection list = new UiCollection(new UiSelector().className("android.widget.ListView"));
            UiObject showAsFolder = list.getChildByText(new UiSelector().resourceId("com.transcend.nas:id/title"), "依資料夾顯示");
            showAsFolder.clickAndWaitForNewWindow();

            UiCollection toolbar = new UiCollection(new UiSelector().resourceId("com.transcend.nas:id/toolbar"));
            int textViewCount = toolbar.getChildCount(new UiSelector().className("android.widget.TextView"));
            //toolbar上面的textview應該只能有下拉式選單呈現的那的一個 (不知為何...more btn 為TextView)
            Assert.assertEquals(2, textViewCount);
        }


        //等待progress消失
        waitForProgress();

        //點擊image tab
        clickTabMusic();

        if (!isBrowserTitleEquals("依專輯顯示")) {
            //打開menu
            openMenu();

            UiCollection list = new UiCollection(new UiSelector().className("android.widget.ListView"));
            UiObject showAsFolder = list.getChildByText(new UiSelector().resourceId("com.transcend.nas:id/title"), "依專輯顯示");
            showAsFolder.clickAndWaitForNewWindow();

            UiCollection toolbar = new UiCollection(new UiSelector().resourceId("com.transcend.nas:id/toolbar"));
            int textViewCount = toolbar.getChildCount(new UiSelector().className("android.widget.TextView"));
            //toolbar上面的textview應該只能有下拉式選單呈現的那的一個 (不知為何...more btn 為TextView)
            Assert.assertEquals(2,  textViewCount);
        }

        //等待progress消失
        waitForProgress();

        //點擊image tab
        clickTabVideo();

        if (!isBrowserTitleEquals("依資料夾顯示")) {
            //打開menu
            openMenu();

            UiCollection list = new UiCollection(new UiSelector().className("android.widget.ListView"));
            UiObject showAsFolder = list.getChildByText(new UiSelector().resourceId("com.transcend.nas:id/title"), "依資料夾顯示");
            showAsFolder.clickAndWaitForNewWindow();

            UiCollection toolbar = new UiCollection(new UiSelector().resourceId("com.transcend.nas:id/toolbar"));
            int textViewCount = toolbar.getChildCount(new UiSelector().className("android.widget.TextView"));
            //toolbar上面的textview應該只能有下拉式選單呈現的那的一個 (不知為何...more btn 為TextView)
            Assert.assertEquals(2,  textViewCount);
        }
    }

    //TODO issue14
    @Test
    public void issue14() throws UiObjectNotFoundException {
        //進到public資料夾
        goToPublicFolder();

        //點擊image tab
        clickTabImage();

        UiObject dropdown = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/dropdown_text"));
        String dropdownText = dropdown.getText().trim();

        //打開menu
        openMenu();

        UiCollection menuList = new UiCollection(new UiSelector().className("android.widget.ListView"));
        for (int i = 0; i < menuList.getChildCount(new UiSelector().resourceId("com.transcend.nas:id/title")); i++){
            UiObject text = menuList.getChildByInstance(new UiSelector().resourceId("com.transcend.nas:id/title"), i);
            Assert.assertEquals(false, dropdownText.equals(text.getText()));
        }

        mDevice.pressBack();

        //點擊music tab
        clickTabMusic();

        dropdown = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/dropdown_text"));
        dropdownText = dropdown.getText().trim();

        //打開menu
        openMenu();

        menuList = new UiCollection(new UiSelector().className("android.widget.ListView"));
        for (int i = 0; i < menuList.getChildCount(new UiSelector().resourceId("com.transcend.nas:id/title")); i++){
            UiObject text = menuList.getChildByInstance(new UiSelector().resourceId("com.transcend.nas:id/title"), i);
            Assert.assertEquals(false, dropdownText.equals(text.getText()));
        }

        mDevice.pressBack();

        //點擊video tab
        clickTabVideo();

        dropdown = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/dropdown_text"));
        dropdownText = dropdown.getText().trim();

        //打開menu
        openMenu();

        menuList = new UiCollection(new UiSelector().className("android.widget.ListView"));
        for (int i = 0; i < menuList.getChildCount(new UiSelector().resourceId("com.transcend.nas:id/title")); i++){
            UiObject text = menuList.getChildByInstance(new UiSelector().resourceId("com.transcend.nas:id/title"), i);
            Assert.assertEquals(false, dropdownText.equals(text.getText()));
        }
    }

    //TODO issue19
    @Test
    public void issue19() throws UiObjectNotFoundException {
        //打開drawer
        openDrawer();

        //點擊最近
        clickDrawerRecent();

        UiScrollable recyclerView = getRecentRecyclerView();
        UiObject item = null;
        int swipeCount = 0;  //找不到檔案時的滑動次數
        boolean findItem = false;

        while(!findItem){
            //大於預設滑動次數則強制離開
            if (swipeCount >= maxSwipeCount)
                break;

            //搜尋目前畫面呈現的所有檔案
            int currentListCount = getItemCount(recyclerView);
            for(int i = 0; i < currentListCount; i++){
                item = getListItemTitle(recyclerView, i);
                String title = item.getText();
                String [] titleSplit = title.split("\\.");
                String type = titleSplit[titleSplit.length-1].toLowerCase();

                if (type.toLowerCase().equals("mp3") || type.toLowerCase().equals("aac") || type.toLowerCase().equals("m4a")){
                    item.clickAndWaitForNewWindow();

                    //判斷是否有正確的進到single view
                    UiObject image = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/music_image"));
                    if (image.exists()) {
                        UiObject prev = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/music_previous"));
                        UiObject next = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/music_next"));

                        Assert.assertEquals(false, prev.exists());
                        Assert.assertEquals(false, next.exists());

                        mDevice.pressBack();
                    }
                }
            }

            if (!findItem) {   //找不到音樂檔
                recyclerView.scrollToEnd(1);
                swipeCount++;
            }
        }
    }

    //TODO issue23
    @Test
    public void issue23() throws UiObjectNotFoundException, RemoteException {
        //進到public資料夾
        goToPublicFolder();

//        if (Testing_device == Pad) {
            mDevice.setOrientationLeft();
            Assert.assertEquals(true, mDevice.getDisplayWidth() > mDevice.getDisplayHeight());
//        }
//        else{
//            mDevice.setOrientationLeft();
//            Assert.assertEquals(true, mDevice.getDisplayWidth() < mDevice.getDisplayHeight());
//        }

        sleep(1000);
        mDevice.unfreezeRotation();
    }

    //TODO issue24
    @Test
    public void issue24() throws UiObjectNotFoundException, RemoteException {
        //進到public資料夾
//        goToPublicFolder();

        //點擊image tab
        clickTabImage();

        if (!isBrowserTitleEquals("查看全部照片")) {
            //打開menu
            openMenu();

            UiCollection list = new UiCollection(new UiSelector().className("android.widget.ListView"));
            UiObject showAsFolder = list.getChildByText(new UiSelector().resourceId("com.transcend.nas:id/title"), "查看全部照片");
            showAsFolder.clickAndWaitForNewWindow();
            sleep(3000);
        }

        UiScrollable recyclerView = getMainRecyclerView();
        for (int i = 0; i < getItemCount(recyclerView); i++){
            UiObject item = getItem(recyclerView, i);
            if (item.exists()){
                item.clickAndWaitForNewWindow();
                sleep(1000);

                //轉橫屏後返回
                mDevice.setOrientationLeft();
                sleep(400);
                mDevice.pressBack();

                //依照螢幕大小判斷排版
                checkComposing(recyclerView, true);

                mDevice.unfreezeRotation();
            }
        }
    }

    //TODO issue24+
    @Test
    public void issue24plus() throws UiObjectNotFoundException, RemoteException {
        //打開drawer
        openDrawer();

        //點擊最近
        clickDrawerRecent();

        UiScrollable recyclerView = getRecentRecyclerView();
        if (getItemCount(recyclerView) == 0)
            return;

        UiObject itemImage = getItem(recyclerView, 0);
        int preCenterX = itemImage.getBounds().centerX();
        int preCenterY = itemImage.getBounds().centerY();

        for (int i = 0; i < 10; i++){
            mDevice.setOrientationLeft();
            sleep(400);
            mDevice.setOrientationNatural();
            sleep(400);
        }
        mDevice.setOrientationNatural();
        mDevice.unfreezeRotation();

        recyclerView = getRecentRecyclerView();
        itemImage = getItem(recyclerView, 0);
        Assert.assertEquals(preCenterX, itemImage.getBounds().centerX());
        Assert.assertEquals(preCenterY, itemImage.getBounds().centerY());
    }

    //TODO issue25
    @Test
    public void issue25() throws UiObjectNotFoundException{
        //進到public資料夾
        goToPublicFolder();

        //進到Mike資料夾
        goToMikeFolder();

        //確認tab是否存在
        UiCollection tabLayout = getTabView();
        Assert.assertEquals(true, tabLayout.exists());

        if (isGridView()){
            changeBrowserViewType();
        }

        UiScrollable recycler = getMainRecyclerView();
        for (int i = 0; i < getItemCount(recycler); i++){
            UiObject titleObj = getListItemTitle(recycler, i);
            String title = titleObj.getText();
            String [] titleSplit = title.split("\\.");

            if (titleSplit.length == 1){
                titleObj.clickAndWaitForNewWindow();

                tabLayout = getTabView();
                Assert.assertEquals(true, tabLayout.exists());

                mDevice.pressBack();
            }
        }
    }

    //TODO issue26
    @Test
    public void issue26() throws UiObjectNotFoundException{
        //進到public資料夾
        goToPublicFolder();

        UiObject dropdown_text = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/dropdown_text"));
        Assert.assertEquals(true, dropdown_text.exists());

        if (isGridView())
            changeBrowserViewType();

        UiScrollable recycler = getMainRecyclerView();
        for (int i = 0; i < getItemCount(recycler); i++){
            UiObject titleObj = getListItemTitle(recycler, i);
            String title = titleObj.getText();
            String [] titleSplit = title.split("\\.");

            if (titleSplit.length == 1){
                titleObj.clickAndWaitForNewWindow();

                dropdown_text = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/dropdown_text"));
                Assert.assertEquals(true, dropdown_text.exists());

                mDevice.pressBack();
            }
        }
    }

    //TODO issue35
    @Test
    public void issue35() throws UiObjectNotFoundException{
        //進到public資料夾
        goToPublicFolder();

        UiObject cast = new UiObject(new UiSelector().description("投放按鈕；已中斷連線"));
        boolean exist = cast.exists();

        //點擊image tab
        clickTabImage();
        cast = new UiObject(new UiSelector().description("投放按鈕；已中斷連線"));
        Assert.assertEquals(exist, cast.exists());

        UiScrollable recycler = getMainRecyclerView();
        for (int i = 0; i < getItemCount(recycler); i++){
            UiObject item = getItem(recycler, i);
            item.clickAndWaitForNewWindow();

            cast = new UiObject(new UiSelector().description("投放按鈕；已中斷連線"));
            Assert.assertEquals(exist, cast.exists());

            mDevice.pressBack();
        }

        //點擊music tab
        clickTabMusic();
        cast = new UiObject(new UiSelector().description("投放按鈕；已中斷連線"));
        Assert.assertEquals(exist, cast.exists());

        //點擊video tab
        clickTabVideo();
        cast = new UiObject(new UiSelector().description("投放按鈕；已中斷連線"));
        Assert.assertEquals(exist, cast.exists());

        //打開menu
        openDrawer();

        //點擊最近
        clickDrawerRecent();
        cast = new UiObject(new UiSelector().description("投放按鈕；已中斷連線"));
        Assert.assertEquals(exist, cast.exists());

        //打開menu
        openDrawer();

        //點擊設備
        clickDrawerDevice();
        cast = new UiObject(new UiSelector().description("投放按鈕；已中斷連線"));
        Assert.assertEquals(exist, cast.exists());

        //打開menu
        openDrawer();

        //點擊下載
        clickDrawerDownload();
        cast = new UiObject(new UiSelector().description("投放按鈕；已中斷連線"));
        Assert.assertEquals(exist, cast.exists());
    }

    //TODO issue37
    @Test
    public void issue37() throws RemoteException {
        //點擊image tab
        clickTabImage();

        UiScrollable recyclerView = getMainRecyclerView();

        //判斷是否非檢視所有圖片，改回檢視所有圖片
        if (!isBrowserTitleEquals("查看全部照片")){
            openMenu();
            UiCollection list = new UiCollection(new UiSelector().className("android.widget.ListView"));
            UiObject showAsFolder = null;
            try {
                showAsFolder = list.getChildByText(new UiSelector().resourceId("com.transcend.nas:id/title"), "查看全部照片");
                showAsFolder.click();
                waitForProgress();
            } catch (UiObjectNotFoundException e) {
                e.printStackTrace();
                return;
            }
        }

        for (int i = 0; i < getItemCount(recyclerView); i++){
            UiObject item = null;
            try {
                item = getItem(recyclerView, i);
                if (item.exists()){
                    item.clickAndWaitForNewWindow();

                    sleep(400);

                    UiObject titleObj = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/toolbar_title"));
                    String titleText = titleObj.getText();

                    for (int j = 0; j < 2; j++){
                        //轉橫屏
                        mDevice.setOrientationRight();
                        sleep(400);
                        UiObject info = null;
                        info = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/media_info"));
                        info.clickAndWaitForNewWindow();
                        sleep(400);
                        UiObject backIcon = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/toggle_btn"));
                        if (!backIcon.exists())
                            return;
                        backIcon.clickAndWaitForNewWindow();
                        sleep(400);
                        //檢查viewPager個數
                        UiCollection viewpager = new UiCollection(new UiSelector().resourceId("com.transcend.nas:id/photo_view_pager"));
                        Assert.assertEquals(1, viewpager.getChildCount(new UiSelector().className("android.widget.RelativeLayout")));
                        //判斷檔名有沒有跑掉(有無變成另一張圖)
                        titleObj = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/toolbar_title"));
                        Assert.assertEquals(titleText, titleObj.getText());
                        sleep(400);

                        mDevice.setOrientationNatural();
                        //檢查viewPager個數
                        viewpager = new UiCollection(new UiSelector().resourceId("com.transcend.nas:id/photo_view_pager"));
                        Assert.assertEquals(1, viewpager.getChildCount(new UiSelector().className("android.widget.RelativeLayout")));
                        //判斷檔名有沒有跑掉(有無變成另一張圖)
                        titleObj = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/toolbar_title"));
                        Assert.assertEquals(titleText, titleObj.getText());
                        sleep(400);
                    }
                    mDevice.unfreezeRotation();
                    mDevice.pressBack();
                }
            } catch (UiObjectNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    //TODO issue40
    @Test
    public void issue40() throws UiObjectNotFoundException {
        //進到public資料夾
        goToPublicFolder();

        //進到Mike資料夾
        goToMikeFolder();

        if (isGridView()){
            changeBrowserViewType();
        }

        UiScrollable recyclerView = getMainRecyclerView();
        UiObject item = null;
        int swipeCount = 0;  //找不到檔案時的滑動次數
        String deleteItemTitle = null;  //紀錄刪除的檔名

        while(deleteItemTitle == null){
            //大於預設滑動次數則強制離開
            if (swipeCount >= maxSwipeCount)
                break;

            //搜尋目前畫面呈現的所有檔案
            int currentListCount = getItemCount(recyclerView);
            for(int i = 0; i < currentListCount; i++){
                item = getListItemTitle(recyclerView, i);
                String title = item.getText();
                if (title == null)  continue;
                String [] titleSplit = title.split("\\.");
                String type = titleSplit[titleSplit.length-1].toLowerCase();

                if (type.equals("jpg") || type.equals("png")){
                    item.clickAndWaitForNewWindow();

                    //判斷是否有正確的進到single view
                    UiObject image = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/viewer_image"));
                    if (image.exists()) {
                        //紀錄標題
                        UiObject titleObj = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/toolbar_title"));
                        deleteItemTitle = titleObj.getText();

                        //刪除物件
                        DeleteItemInImageView();

                        titleObj = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/toolbar_title"));
                        Assert.assertEquals(false, deleteItemTitle.equals(titleObj.getText()));
                        break;
                    }
                    else{   //點到刪除後的檔案，toast出找不到檔案後會自動清掉該檔案，故要把 i 減回來
                        i--;
                    }
                }
            }

            if (deleteItemTitle == null) {   //找不到圖片檔，往下滑一階
                recyclerView.scrollToEnd(1);
                swipeCount++;
            }
        }
    }

    //TODO issue41
    @Test
    public void issue41() throws UiObjectNotFoundException, RemoteException {
        //點擊image tab
        clickTabImage();

        UiScrollable recyclerView = getMainRecyclerView();

        //grid沒有東西則判斷為非檢視所有圖片，改回檢視所有圖片
        if (!isBrowserTitleEquals("查看全部照片")){
            openMenu();
            UiCollection list = new UiCollection(new UiSelector().className("android.widget.ListView"));
            UiObject showAsFolder = list.getChildByText(new UiSelector().resourceId("com.transcend.nas:id/title"), "查看全部照片");
            showAsFolder.click();
            waitForProgress();
        }

        if (!isGridView()){
            changeBrowserViewType();
        }

        for (int i=0; i < getItemCount(recyclerView); i++) {
            UiObject item = getItem(recyclerView, i);
            if (item.exists()) {
                item.clickAndWaitForNewWindow();

                UiObject titleObj = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/toolbar_title"));
                String preTitle = titleObj.getText();

                for (int j = 0; j < 3; j++){
                    mDevice.setOrientationLeft();
                    sleep(400);
                    mDevice.setOrientationNatural();
                    sleep(400);
                }
                mDevice.unfreezeRotation();

                titleObj = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/toolbar_title"));
                Assert.assertEquals(preTitle, titleObj.getText());

                mDevice.pressBack();
            }
        }
    }

    //TODO issue42
    @Test
    public void issue42() throws UiObjectNotFoundException, RemoteException {
        //進到public資料夾
//        goToPublicFolder();

        //點擊image tab
        clickTabImage();

        UiScrollable recyclerView = getMainRecyclerView();

        //grid沒有東西則判斷為非檢視所有圖片，改回檢視所有圖片
        if (!isBrowserTitleEquals("查看全部照片")){
            openMenu();
            UiCollection list = new UiCollection(new UiSelector().className("android.widget.ListView"));
            UiObject showAsFolder = list.getChildByText(new UiSelector().resourceId("com.transcend.nas:id/title"), "查看全部照片");
            showAsFolder.click();
            waitForProgress();
        }

        if (!isGridView())
            changeBrowserViewType();

        for(int i = 0; i < getItemCount(recyclerView); i++) {
            UiObject item = getItem(recyclerView, i);
            item.clickAndWaitForNewWindow();

            UiObject titleObj = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/toolbar_title"));
            String preTitle = titleObj.getText();

            //輕輕左滑一下
            mDevice.swipe(mDevice.getDisplayWidth() / 2, mDevice.getDisplayHeight() / 2,
                    (mDevice.getDisplayWidth() / 2) - 90, mDevice.getDisplayHeight() / 2, 10);
            sleep(1000);

            titleObj = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/toolbar_title"));
            Assert.assertEquals(preTitle, titleObj.getText());

            mDevice.pressBack();
        }
    }

    //TODO issue43
    @Test
    public void issue43() throws UiObjectNotFoundException, RemoteException {
        //進到public資料夾
//        goToPublicFolder();

        //進到image tab
        clickTabImage();

        UiScrollable recyclerView = getMainRecyclerView();

        //grid沒有東西則判斷為非檢視所有圖片，改回檢視所有圖片
        if (!isBrowserTitleEquals("查看全部照片")){
            openMenu();
            UiCollection list = new UiCollection(new UiSelector().className("android.widget.ListView"));
            UiObject showAsFolder = list.getChildByText(new UiSelector().resourceId("com.transcend.nas:id/title"), "查看全部照片");
            showAsFolder.click();
            waitForProgress();
        }

        if (!isGridView())
            changeBrowserViewType();

        UiObject item = getItem(recyclerView, 0);
        if (item.exists()){
            item.clickAndWaitForNewWindow();

            //轉橫屏
            mDevice.setOrientationLeft();

            //刪除檔案
            DeleteItemInImageView();
        }

        mDevice.pressBack();
        mDevice.unfreezeRotation();

        recyclerView = getMainRecyclerView();

        checkComposing(recyclerView, false);
    }

    //TODO issue45
    @Test
    public void issue45() throws UiObjectNotFoundException {
        //有檔案存在時，無檔案的圖示不能存在
        UiObject noItemViewObj = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/empty_view"));
        Assert.assertEquals(false, noItemViewObj.exists());

        clickTabImage();
        noItemViewObj = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/empty_view"));
        Assert.assertEquals(false, noItemViewObj.exists());

        clickTabMusic();
        noItemViewObj = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/empty_view"));
        Assert.assertEquals(false, noItemViewObj.exists());

        clickTabVideo();
        noItemViewObj = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/empty_view"));
        Assert.assertEquals(false, noItemViewObj.exists());
    }

    @Test
    public void issue55() throws UiObjectNotFoundException {
        //進入Public資料夾
        boolean isEnterTestFolder = enterTestFolder(Test_Folder_Public);
        if (isEnterTestFolder){
            //轉成List
            if(isGridView())
                changeBrowserViewType();

            //再進入到image test folder
            UiScrollable recycler = getMainRecyclerView();
            UiObject imageTestFolderObj = getItem(recycler, imageTestFolder);
            imageTestFolderObj.click();
            waitForProgress();

            recycler = getMainRecyclerView();
            if (getItemCount(recycler) > 0) {
                UiObject lastItem = getItem(recycler, 0);
                String copyTitle = getListItemTitle(recycler, 0).getText();  //試著找圖片來作複製
                longClick(lastItem);


                //打開菜單
                openMenu();
                UiCollection menuList = getMenuList();
                sleep(1000);
                UiObject copyObj = getMenuItem(menuList, 3);  //複製
                copyObj.click();
                sleep(500);
                waitForProgress();

                //回到根目錄
                UiObject dropDownTextObj = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/dropdown_text"));
                dropDownTextObj.click();
                sleep(500);
                UiCollection dropDownList = new UiCollection(new UiSelector().className("android.widget.ListView"));
                UiObject rootObj = dropDownList.getChildByInstance(new UiSelector().className("android.widget.RelativeLayout"),
                        dropDownList.getChildCount(new UiSelector().className("android.widget.RelativeLayout")) - 1);   //取得最後一個(根目錄)
                rootObj.click();
                waitForProgress();

                //進入使用者資料夾
                recycler = getMainRecyclerView();
                UiObject userFolderObj = getItem(recycler, username);
                userFolderObj.click();
                waitForProgress();

                //進入測試資料夾
                recycler = getMainRecyclerView();
                UiObject testFolderObj = getItem(recycler, testFolderName);
                testFolderObj.click();
                waitForProgress();

                //貼上
                UiObject pasteObj = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/action_confirm"));
                pasteObj.click();
                UiObject confirmObj = new UiObject(new UiSelector().resourceId("android:id/button1"));
                confirmObj.click();
                sleep(3000);

                //回到根目錄
                openDrawer();
                clickDrawerBrowser();

                //進入使用者資料夾
                recycler = getMainRecyclerView();
                userFolderObj = getItem(recycler, username);
                userFolderObj.click();
                waitForProgress();

                //進入測試資料夾
                recycler = getMainRecyclerView();
                testFolderObj = getItem(recycler, testFolderName);
                testFolderObj.click();
                waitForProgress();

                recycler = getMainRecyclerView();
                UiObject copyItem = getItem(recycler, copyTitle);
                Assert.assertEquals(true, copyItem.exists());
            }
        }
    }

    @Test
    public void issue55_plus() throws UiObjectNotFoundException {
        //進入使用者資料夾
        boolean isEnterTestFolder = enterTestFolder(Test_Folder_User);
        if (isEnterTestFolder){
            //轉成List
            if(isGridView())
                changeBrowserViewType();

            //再進入到image test folder
            UiScrollable recycler = getMainRecyclerView();
            UiObject imageTestFolderObj = getItem(recycler, imageTestFolder);
            imageTestFolderObj.click();
            waitForProgress();

            recycler = getMainRecyclerView();
            if (getItemCount(recycler) > 0){
                UiObject lastItem = getItem(recycler, 0);
                String copyTitle = getListItemTitle(recycler, 0).getText();  //試著找圖片來作複製
                longClick(lastItem);

                //打開菜單
                openMenu();
                UiCollection menuList = getMenuList();
                sleep(1000);
                UiObject copyObj = getMenuItem(menuList, 4);  //移動
                copyObj.click();
                sleep(500);
                waitForProgress();

                //回到根目錄
                UiObject dropDownTextObj = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/dropdown_text"));
                dropDownTextObj.click();
                sleep(500);
                UiCollection dropDownList = new UiCollection(new UiSelector().className("android.widget.ListView"));
                UiObject rootObj = dropDownList.getChildByInstance(new UiSelector().className("android.widget.RelativeLayout"),
                        dropDownList.getChildCount(new UiSelector().className("android.widget.RelativeLayout")) - 1);   //取得最後一個(根目錄)
                rootObj.click();
                waitForProgress();

                //此處不能出現貼上的按鈕
                UiObject actionBtn = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/action_confirm"));
                Assert.assertEquals(false, actionBtn.exists());

                //進入USB資料夾(如果存在)
                recycler = getMainRecyclerView();
                UiObject USBObj = getItem(recycler, "USB");
                if (USBObj != null) {
                    USBObj.click();
                    waitForProgress();

                    actionBtn = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/action_confirm"));
                    Assert.assertEquals(false, actionBtn.exists());

                    mDevice.pressBack();
                    waitForProgress();
                }

                //進入Public資料夾
                recycler = getMainRecyclerView();
                UiObject userFolderObj = getItem(recycler, "Public");
                userFolderObj.click();
                waitForProgress();

                //進入測試資料夾
                recycler = getMainRecyclerView();
                UiObject testFolderObj = getItem(recycler, testFolderName);
                testFolderObj.click();
                waitForProgress();

                //貼上
                UiObject pasteObj = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/action_confirm"));
                pasteObj.click();
                UiObject confirmObj = new UiObject(new UiSelector().resourceId("android:id/button1"));
                confirmObj.click();
                sleep(3000);

                //回到根目錄
                openDrawer();
                clickDrawerBrowser();

                //進入Public資料夾
                recycler = getMainRecyclerView();
                userFolderObj = getItem(recycler, "Public");
                userFolderObj.click();
                waitForProgress();

                //進入測試資料夾
                recycler = getMainRecyclerView();
                testFolderObj = getItem(recycler, testFolderName);
                testFolderObj.click();
                waitForProgress();

                recycler = getMainRecyclerView();
                UiObject copyItem = getItem(recycler, copyTitle);
                Assert.assertEquals(true, copyItem.exists());
            }
        }
    }
}
