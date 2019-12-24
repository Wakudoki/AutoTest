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

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class StoreJetCloud_StressTest extends StoreJetCloudAbstract{

    private static int Type_Video = 0;
    private static int Type_Image = 1;
    private static int Type_Music = 2;

    private static String process1_path = "/mnt/sdcard/StoreJet Cloud/Stress test/1. Download Video/";
    private static String process2_path = "/mnt/sdcard/StoreJet Cloud/Stress test/2. Download Image/";
    private static String process3_path = "/mnt/sdcard/StoreJet Cloud/Stress test/3. Upload Video/";
    private static String process4_path = "/mnt/sdcard/StoreJet Cloud/Stress test/4. Upload Image/";
    private static String process5_path = "/mnt/sdcard/StoreJet Cloud/Stress test/5. Auto Backup Video/";
    private static String process6_path = "/mnt/sdcard/StoreJet Cloud/Stress test/6. Auto Backup Image/";

    //在admin裡自動備份的上傳資料夾名稱，自動備份的資料夾則必須改成"SJC下載資料夾" (/mnt/sdcard/StoreJet Cloud/Downloads)
    private static String auto_backup_upload_folder = "HTC_A9u";

    //備份年月份，要進到該資料夾查看結果
    private static String backup_month = "201901";

    private static String admin_download_folder = "AutoTest Download";
    private static String admin_upload_folder = "AutoTest Upload";

    //存放上傳用檔案的資料夾名稱，直接放在手機根目錄的話則直接留空白即可
    private static String upload_folder = "Develop";

    private static int imageDownloadWaitingTime = 10000;    //圖片下載等待10秒
    private static int videoDownloadWaitingTime = 10000;   //影片下載等待2分鐘
    private static int imageUploadWaitingTime = 10000;    //圖片下載等待30秒
    private static int videoUploadWaitingTime = 1000;   //影片下載等待4分鐘

    @Before
    public void setup() throws UiObjectNotFoundException, RemoteException {
        super.setup();

        getPermission(mDevice);

        //刷掉程式
        closeAPP(mDevice, "com.transcend.nas");

        sleep(1000);

        //開啟SJC
        startAPP("com.transcend.nas");

        //判斷是否進到主頁面
        UiScrollable mainRecycler = getMainRecyclerView();
        while (!mainRecycler.exists()) {
            sleep(1000);

            UiObject auto_backup = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/title_text"));
            if (auto_backup.exists()){
                mDevice.setOrientationLeft();
                //需恆為豎屏
                Assert.assertEquals(true, mDevice.getDisplayWidth() < mDevice.getDisplayHeight());
                mDevice.setOrientationNatural();
                mDevice.unfreezeRotation();

                UiObject not_now = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/cancel_text"));
                not_now.click();
            }

            mainRecycler = getMainRecyclerView();

//            Log.e("SJC 80", mainRecycler.exists()+"");
        }
    }

    //TODO Process1
    @Test
    public void Process1_Download_Video() throws UiObjectNotFoundException {
        //刪除下載資料夾裡所有物品，並回到browser頁面
        deleteDownloadItem();

        for (int times = 0; times < action_execute_times; times++){
            //進入admin資料夾
            enterUserFolder();

            //進入下載資料夾
            enterDownloadFolder();

            //判斷顯示模式，若為Grid則轉為List顯示
            if (isGridView())
                changeBrowserViewType();

            File f = new File(process1_path);
            if (!f.exists())
                f.mkdirs();

            boolean isFinal = (times == action_execute_times - 1);

            if (isFinal) {
                sleep(1000);
                f = new File(process1_path + "1. Enter admin.png");
                mDevice.takeScreenshot(f, 1.0f, 50);
            }

            //執行下載並記錄下載檔案的名稱
            String itemTitle = Download(Type_Video, isFinal, process1_path);
            sleep(videoDownloadWaitingTime);

            f = new File(process1_path + "Check Download Item/");
            if (!f.exists())
                f.mkdirs();
            checkDownloadItem(times, itemTitle, process1_path + "Check Download Item/" + (times+"") + ". " + itemTitle + ".png");
        }
    }

    //TODO Process2
    @Test
    public void Process2_Download_Image() throws UiObjectNotFoundException{
        //刪除下載資料夾裡所有物品，並回到browser頁面
        deleteDownloadItem();

        waitForProgress();

        for (int times = 0; times < action_execute_times; times++){
            //進入admin資料夾
            enterUserFolder();

            //進入下載資料夾
            enterDownloadFolder();

            //判斷顯示模式，若為Grid則轉為List顯示
            if (isGridView())
                changeBrowserViewType();

            File f = new File(process2_path);
            if (!f.exists())
                f.mkdirs();

            boolean isFinal = (times == action_execute_times - 1);

            if (isFinal) {
                sleep(1000);
                f = new File(process2_path + "1. Enter admin.png");
                mDevice.takeScreenshot(f, 1.0f, 50);
            }

            //執行下載並記錄下載檔案的名稱
            String itemTitle = Download(Type_Image, isFinal, process2_path);
            sleep(imageDownloadWaitingTime);

            f = new File(process2_path + "Check Download Item/");
            if (!f.exists())
                f.mkdirs();
            checkDownloadItem(times, itemTitle, process2_path + "Check Download Item/" + (times+"") + ". " + itemTitle + ".png");
        }
    }

    //TODO Process3
    @Test
    public void Process3_Upload_Video() throws UiObjectNotFoundException{
        for (int times = 0; times < action_execute_times; times++){
            //進入admin資料夾
            enterUserFolder();

            //進入上傳資料夾
            enterUploadFolder();

            //判斷顯示模式，若為Grid則轉為List顯示
            if (isGridView())
                changeBrowserViewType();

            File f = new File(process3_path);
            if (!f.exists())
                f.mkdirs();

            sleep(1000);
            f = new File(process3_path + "1. Enter admin.png") ;
            mDevice.takeScreenshot(f , 1.0f, 50);

            boolean isFinal = (times == action_execute_times - 1);
            String itemTitle = Upload(Type_Video, isFinal);
            sleep(videoUploadWaitingTime);

            f = new File(process3_path + "Check Upload Item/");
            if (!f.exists())
                f.mkdirs();

            checkUploadItem(times, itemTitle, process3_path + "Check Upload Item/" + (times+"") + ". " + itemTitle + ".png");
        }
    }

    //TODO Process4
    @Test
    public void Process4_Upload_Image() throws UiObjectNotFoundException {
        for (int times = 0; times < action_execute_times; times++){
            //進入admin資料夾
            enterUserFolder();

            //進入上傳資料夾
            enterUploadFolder();

            //判斷顯示模式，若為Grid則轉為List顯示
            if (isGridView())
                changeBrowserViewType();

            File f = new File(process4_path);
            if (!f.exists())
                f.mkdirs();

            sleep(1000);
            f = new File(process4_path + "1. Enter admin.png") ;
            mDevice.takeScreenshot(f , 1.0f, 50);

            boolean isFinal = (times == action_execute_times - 1);
            String itemTitle = Upload(Type_Image, isFinal);
            sleep(imageUploadWaitingTime);

            f = new File(process4_path + "Check Upload Item/");
            if (!f.exists())
                f.mkdirs();

            checkUploadItem(times, itemTitle, process4_path + "Check Upload Item/" + (times+"") + ". " + itemTitle + ".png");
        }
    }

    //TODO Process5
    @Test
    public void Process5_Auto_Backup_Video() throws UiObjectNotFoundException {
        for (int times = 0; times < action_execute_times; times++){
            //進入test資料夾
            enterTestFolder(Test_Folder_User);

            //進入下載資料夾
            enterDownloadFolder();

            //判斷顯示模式，若為Grid則轉為List顯示
            if (isGridView())
                changeBrowserViewType();

            String backupItemTitle = Download(Type_Video, false, process5_path);

            //等待下載
            sleep(videoDownloadWaitingTime);

            //至指定資料夾拍照做檢查
            AutoBackupCheck(Type_Video, times == action_execute_times - 1, times, backupItemTitle);

            //打開drawer
            openDrawer();

            //點擊browser
            clickDrawerBrowser();
        }
    }

    //TODO Process6
    @Test
    public void Process6_Auto_Backup_Image() throws UiObjectNotFoundException {
        for (int times = 0; times < action_execute_times; times++){
            //進入admin資料夾
            enterUserFolder();

            //進入下載資料夾
            enterDownloadFolder();

            //判斷顯示模式，若為Grid則轉為List顯示
            if (isGridView())
                changeBrowserViewType();

            String backupItemTitle = Download(Type_Image, false, process6_path);

            //等待下載
            sleep(imageDownloadWaitingTime);

            //至指定資料夾拍照做檢查
            AutoBackupCheck(Type_Image, times == action_execute_times - 1, times, backupItemTitle);

            //打開drawer
            openDrawer();

            //點擊browser
            clickDrawerBrowser();
        }
    }

    //TODO Download
    private String Download(int type, boolean isFinal, String path) throws UiObjectNotFoundException {

        //記錄下載的檔案標題
        String downloadFileTitle = null;

        //打開menu
        openMenu();

        if (isFinal){
            sleep(1000);
            File f = new File(path + "2. Open Menu.png") ;
            mDevice.takeScreenshot(f , 1.0f, 50);
        }

        //點擊"選擇"
        UiCollection menuList = new UiCollection(new UiSelector().className("android.widget.ListView"));
        UiObject selectObj = menuList.getChildByText(new UiSelector().resourceId("com.transcend.nas:id/title"), "選擇");
        selectObj.click();

        if (isFinal){
            sleep(1000) ;
            File f = new File(path + "3. Click Select Mode.png") ;
            mDevice.takeScreenshot(f , 1.0f, 50);
        }

        int current_swipe = 0;
        while(current_swipe < maxSwipeCount){
            UiScrollable recyclerView = getMainRecyclerView();
            for (int count = 0; count < getItemCount(recyclerView); count++){
                UiObject itemTitleObj = getListItem(recyclerView, count);
                String [] splitTitle = itemTitleObj.getText().split("\\.");
                String fileExtension = splitTitle[splitTitle.length-1]; //副檔名
                if (type == Type_Video && isVideo(fileExtension.toLowerCase())){
                    if (itemTitleObj.exists()){
                        downloadFileTitle = itemTitleObj.getText();
                        itemTitleObj.click();

                        if (isFinal){
                            sleep(1000) ;
                            File f = new File(path + "4. Select Item.png") ;
                            mDevice.takeScreenshot(f , 1.0f, 50);
                        }

                        //選取後開始下載
                        openMenu();
                        menuList = getMenuList();
                        UiObject downloadObj = getMenuItem(menuList, getMenuCount(menuList) - 1);
                        downloadObj.click();

                        if (isFinal){
                            sleep(1000) ;
                            File f = new File(path + "5. Click Download.png") ;
                            mDevice.takeScreenshot(f , 1.0f, 50);
                        }

                        UiObject confirm = new UiObject(new UiSelector().resourceId("android:id/button1"));
                        confirm.click();

                        if (isFinal){
                            sleep(1000) ;
                            File f = new File(path + "6. Click Confirm.png") ;
                            mDevice.takeScreenshot(f , 1.0f, 50);
                        }

                        return downloadFileTitle;
                    }
                }
                else if (type == Type_Image && isImage(fileExtension.toLowerCase())){
                    if (itemTitleObj.exists()){
                        downloadFileTitle = itemTitleObj.getText();
                        itemTitleObj.click();

                        if (isFinal){
                            sleep(1000) ;
                            File f = new File(path + "4. Select Item.png") ;
                            mDevice.takeScreenshot(f , 1.0f, 50);
                        }

                        //選取後開始下載
                        UiObject downloadObj = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/file_manage_editor_action_transmission"));
                        downloadObj.click();

                        if (isFinal){
                            sleep(1000) ;
                            File f = new File(path + "5. Click Download.png") ;
                            mDevice.takeScreenshot(f , 1.0f, 50);
                        }

                        UiObject confirm = new UiObject(new UiSelector().resourceId("android:id/button1"));
                        confirm.click();

                        if (isFinal){
                            sleep(1000) ;
                            File f = new File(path + "6. Click Confirm.png") ;
                            mDevice.takeScreenshot(f , 1.0f, 50);
                        }
                        return downloadFileTitle;
                    }
                }
            }
            recyclerView.scrollToEnd(1, 6);
            current_swipe++;
        }

        return null;
    }

    //TODO Upload
    private String Upload(int type, boolean isFinal) throws UiObjectNotFoundException {

        //記錄上傳的檔案標題
        String uploadFileTitle = null;

        String path;    //存放截圖的位置
        if (type == Type_Video)
            path = process3_path;
        else
            path = process4_path;

        //打開menu
        openMenu();

        if (isFinal){
            sleep(1000);
            File f = new File(path + "2. Open Menu.png") ;
            mDevice.takeScreenshot(f , 1.0f, 50);
        }

        //點擊"上傳"
        UiCollection menuList = new UiCollection(new UiSelector().className("android.widget.ListView"));
        UiObject uploadObj = menuList.getChildByText(new UiSelector().resourceId("com.transcend.nas:id/title"), "上傳");
        uploadObj.click();

        if (isFinal){
            sleep(1000) ;
            File f = new File(path + "3. Click Upload.png") ;
            mDevice.takeScreenshot(f , 1.0f, 50);
        }

        //判斷是否有成功進到上傳用的資料夾
        boolean isEnterFolder = enterFolder(upload_folder);
        if (isEnterFolder){
            //判斷顯示模式，若為Grid則轉為List顯示
            if (isGridView()){
                UiObject viewTypeObj = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/file_picker_viewer_action_view"));
                viewTypeObj.click();
            }

            int current_swipe = 0;
            while(current_swipe < maxSwipeCount){
                UiScrollable recyclerView = getMainRecyclerView();
                for (int count = 0; count < getItemCount(recyclerView) - 1; count++){
                    UiObject itemTitleObj = getListItem(recyclerView, count);
                    String [] splitTitle = itemTitleObj.getText().split("\\.");
                    String fileExtension = splitTitle[splitTitle.length-1]; //副檔名
                    if (type == Type_Video && isVideo(fileExtension.toLowerCase())){
                        if (itemTitleObj.exists()){
                            uploadFileTitle = itemTitleObj.getText().toString();
                            itemTitleObj.click();

                            if (isFinal){
                                sleep(1000) ;
                                File f = new File(path + "4. Select Item.png") ;
                                mDevice.takeScreenshot(f , 1.0f, 50);
                            }

                            UiObject actionObj = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/action_confirm"));
                            actionObj.click();

                            if (isFinal){
                                sleep(1000) ;
                                File f = new File(path + "5. Click Upload.png") ;
                                mDevice.takeScreenshot(f , 1.0f, 50);
                            }

                            UiObject confirm = new UiObject(new UiSelector().resourceId("android:id/button1"));
                            confirm.click();

                            if (isFinal){
                                sleep(1000) ;
                                File f = new File(path + "6. Click Confirm.png") ;
                                mDevice.takeScreenshot(f , 1.0f, 50);
                            }

                            return uploadFileTitle;
                        }
                    }
                    else if (type == Type_Image && isImage(fileExtension.toLowerCase())){
                        if (itemTitleObj.exists()){
                            uploadFileTitle = itemTitleObj.getText().toString();
                            itemTitleObj.click();

                            if (isFinal){
                                sleep(1000) ;
                                File f = new File(path + "4. Select Item.png") ;
                                mDevice.takeScreenshot(f , 1.0f, 50);
                            }

                            UiObject actionObj = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/action_confirm"));
                            actionObj.click();

                            if (isFinal){
                                sleep(1000) ;
                                File f = new File(path + "5. Click Upload.png") ;
                                mDevice.takeScreenshot(f , 1.0f, 50);
                            }

                            UiObject confirm = new UiObject(new UiSelector().resourceId("android:id/button1"));
                            confirm.click();

                            if (isFinal){
                                sleep(1000) ;
                                File f = new File(path + "6. Click Confirm.png") ;
                                mDevice.takeScreenshot(f , 1.0f, 50);
                            }

                            return uploadFileTitle;
                        }
                    }
                }
                recyclerView.scrollToEnd(1, 6);
                current_swipe++;
            }
        }

        return null;
    }

    //TODO backup check
    private void AutoBackupCheck(int type, boolean isFinal, int count, String auto_backup_item_title) throws UiObjectNotFoundException {
        String path;    //存放截圖的位置
        if (type == Type_Video)
            path = process5_path;
        else
            path = process6_path;

        openDrawer();

        clickDrawerBrowser();

        enterUserFolder();

        //建立儲存檢查備份結果的資料夾
        if (!isFinal)
            path = path + "Backup Result/";
        File f = new File(path) ;
        if (!f.exists())
            f.mkdirs();

        if (isFinal){
            sleep(1000) ;
            f = new File(path + "1. Enter admin.png") ;
            mDevice.takeScreenshot(f , 1.0f, 50);
        }

        //進到backup資料夾
        if (enterFolder(auto_backup_upload_folder)){
            if (isFinal){
                sleep(1000) ;
                f = new File(path + "2. Enter Backup Folder.png") ;
                mDevice.takeScreenshot(f , 1.0f, 50);
            }
            if (enterFolder(backup_month)){
                if (auto_backup_item_title != null && !auto_backup_item_title.equals("")){
                    if (getItemCount(getMainRecyclerView()) == 0){
                        //下拉更新
                        getMainRecyclerView().scrollToBeginning(1, 7);
                        waitForProgress();
                        sleep(1000);
                    }

                    //滑到上傳物件名稱的旁邊
                    UiObject back_up_item = getMainRecyclerView().getChildByText(new UiSelector().resourceId("com.transcend.nas:id/item_title"), auto_backup_item_title, true);

                    if (!isFinal){
                        sleep(1000) ;
                        f = new File(path + count +". Backup\""+auto_backup_item_title+"\".png") ;
                        mDevice.takeScreenshot(f , 1.0f, 50);
                    }
                    else{
                        sleep(1000) ;
                        f = new File(path + "3. Enter Backup Month Folder.png") ;
                        mDevice.takeScreenshot(f , 1.0f, 50);
                    }
                }
            }
        }
    }

    private boolean enterFolder(String folderName) throws UiObjectNotFoundException {
        //空字串或空白表示檔案在根目錄
        if (folderName == null || folderName.equals("")){
            return true;
        }

        UiScrollable recyclerView = new UiScrollable(new UiSelector().resourceId("com.transcend.nas:id/recycler_view"));
        UiObject titleObj = recyclerView.getChildByText(new UiSelector().resourceId("com.transcend.nas:id/item_title"), folderName, true);
        if (titleObj.exists()){
            titleObj.click();
            waitForProgress();
            return true;
        }

        return false;
    }

    private void checkDownloadItem(int times, String title, String shotPathAndName) throws UiObjectNotFoundException {
        if (title == null || title.length() == 0)
            return;

        //打開drawer
        openDrawer();

        //點擊下載
        clickDrawerDownload();

        String tmpTitle = title.substring(0, title.lastIndexOf('.'));

        int scroll_times = 0;
        while (true){
            UiScrollable recyclerView = new UiScrollable(new UiSelector().resourceId("com.transcend.nas:id/recycler_view"));
            for (int i = 0; i < getItemCount(recyclerView); i++){
                if (getListItem(recyclerView, i).getText().toString().contains(tmpTitle)){
                    if (times == 0){
                        sleep(1000) ;
                        File f = new File(shotPathAndName) ;
                        mDevice.takeScreenshot(f , 1.0f, 50);

                        //打開drawer
                        openDrawer();

                        //點擊瀏覽
                        clickDrawerBrowser();

                        return;
                    }
                    else if (times > 0 && getListItem(recyclerView, i).getText().toString().contains((times+1)+"")) {
                        sleep(1000) ;
                        File f = new File(shotPathAndName) ;
                        mDevice.takeScreenshot(f , 1.0f, 50);

                        //打開drawer
                        openDrawer();

                        //點擊瀏覽
                        clickDrawerBrowser();

                        return;
                    }
                }
            }
            recyclerView.scrollToEnd(1, 5);
            scroll_times++;
            if (scroll_times > maxSwipeCount){
                //打開drawer
                openDrawer();

                //點擊瀏覽
                clickDrawerBrowser();

                return;
            }
        }
    }

    private void checkUploadItem(int times, String title, String shotPathAndName) throws UiObjectNotFoundException {
        if (title == null || title.length() == 0)
            return;

        UiScrollable recyclerView = getMainRecyclerView();
        if (getItemCount(recyclerView) > 0) {
            //下拉更新
            recyclerView.scrollToBeginning(1, 7);
            waitForProgress();
            sleep(1000);
        }
        else{
            UiScrollable viewPager = new UiScrollable(new UiSelector().resourceId("com.transcend.nas:id/viewPager"));
            if (viewPager.exists()){
                viewPager.scrollToBeginning(1, 7);
                waitForProgress();
                sleep(1000);
            }
            else
                return;
        }

        String tmpTitle = title.substring(0, title.lastIndexOf('.'));

        int scroll_times = 0;
        while (true){
            recyclerView = new UiScrollable(new UiSelector().resourceId("com.transcend.nas:id/recycler_view"));
            for (int i = 0; i < getItemCount(recyclerView); i++){
                if (isGridView())
                    changeBrowserViewType();
                if (getListItem(recyclerView, i).getText().toString().contains(tmpTitle)){
                    if (times == 0){
                        sleep(1000) ;
                        File f = new File(shotPathAndName) ;
                        mDevice.takeScreenshot(f , 1.0f, 50);

                        //打開drawer
                        openDrawer();

                        //點擊瀏覽
                        clickDrawerBrowser();

                        return;
                    }
                    else if (times > 0 && getListItem(recyclerView, i).getText().toString().contains((times+1)+"")) {
                        sleep(1000) ;
                        File f = new File(shotPathAndName) ;
                        mDevice.takeScreenshot(f , 1.0f, 50);

                        //打開drawer
                        openDrawer();

                        //點擊瀏覽
                        clickDrawerBrowser();

                        return;
                    }
                }
            }
            recyclerView.scrollToEnd(1, 4);
            scroll_times++;
            if (scroll_times > maxSwipeCount){
                //打開drawer
                openDrawer();

                //點擊瀏覽
                clickDrawerBrowser();

                return;
            }
        }
    }

    public void deleteDownloadItem() throws UiObjectNotFoundException {
        openDrawer();

        clickDrawerDownload();

        if (!getMainRecyclerView().exists()) {
            openDrawer();
            clickDrawerBrowser();
            return;
        }

        openMenu();

        //點擊"選擇"
        UiCollection menuList = new UiCollection(new UiSelector().className("android.widget.ListView"));
        UiObject selectAllObj = menuList.getChildByText(new UiSelector().resourceId("com.transcend.nas:id/title"), "全選");
        selectAllObj.click();

        openMenu();

        menuList = new UiCollection(new UiSelector().className("android.widget.ListView"));
        UiObject deleteObj = menuList.getChildByText(new UiSelector().resourceId("com.transcend.nas:id/title"), "刪除");
        deleteObj.click();

        UiObject confirm = new UiObject(new UiSelector().resourceId("android:id/button1"));
        confirm.click();

        openDrawer();

        clickDrawerBrowser();
    }

    public void enterUserFolder() throws UiObjectNotFoundException {
        UiObject admineFolder = getItem(getMainRecyclerView(), username);
        admineFolder.click();
        waitForProgress();
    }

    public void enterDownloadFolder() throws UiObjectNotFoundException {
        UiObject downloadFolder = getItem(getMainRecyclerView(), admin_download_folder);
        downloadFolder.click();
        waitForProgress();
    }

    public void enterUploadFolder() throws UiObjectNotFoundException {
        UiObject uploadFolder = getItem(getMainRecyclerView(), admin_upload_folder);
        uploadFolder.click();
        waitForProgress();
    }



    public static boolean isAudio(String fileSubTitle){
        if (fileSubTitle == null)
            return false;
        String [] audio = {"3gpp", "amr", "snd", "mid", "midi", "kar", "xmf", "mxmf", "mpga", "mpega",
                "mp2", "mp3", "m4a", "m3u", "sid", "aif", "aiff", "aifc", "gsm", "m3u", "wma",
                "wax", "ra", "rm", "ram", "pls", "sd2", "wav", "mka", "aac"};
        for (int i = 0; i < audio.length; i++){
            if (fileSubTitle.equals(audio[i])){
                return true;
            }
        }
        return false;
    }

    public static boolean isVideo(String fileSubTitle){
        if (fileSubTitle == null)
            return false;
        String [] video = {"3gpp", "3gp", "3g2", "dl", "dif", "dv", "fli", "m4v", "mpeg", "mpg", "mpe", "mp4",
                "vob", "qt", "mov", "mxu", "lsf", "lsx", "mng", "asf", "asx", "wm", "wmv", "wmx",
                "wvx", "avi", "movie", "rmvb", "rm", "rv", "flv", "hlv", "mkv", "divx", "evo", "f4v",
                "mks", "ts", "mts", "m2p", "m2t", "m2ts", "mk3d", "ogm",
                "trp", "ogv", "webm"};
        for (int i = 0; i < video.length; i++){
            if (fileSubTitle.equals(video[i])){
                return true;
            }
        }
        return false;
    }

    public static boolean isImage(String fileSubTitle){
        if (fileSubTitle == null)
            return false;
        String [] image = {"odi", "dmg", "iso", "bmp", "gif", "cur", "ico", "ief", "jpeg", "jpg", "jpe", "pcx", "png",
                "svg", "svgz", "tiff", "tif", "djvu", "djv", "wbmp", "ras", "cdr", "pat", "cdt", "cpt", "ico",
                "art", "jng", "bmp", "psd", "pnm", "pbm", "pgm", "ppm", "rgb", "xbm", "xpm", "xwd"};
        for (int i = 0; i < image.length; i++){
            if (fileSubTitle.equals(image[i])){
                return true;
            }
        }
        return false;
    }
}
