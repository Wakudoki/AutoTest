package com.transcend.autotest.StoreJetCloud;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.support.test.uiautomator.UiCollection;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.util.Log;

import org.junit.Assert;
import org.junit.Before;

import java.io.IOException;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

public class StoreJetCloudAbstract {

    UiDevice mDevice;
    Instrumentation instrumentation;
    static int Phone = 0;
    static int Pad = 1;
    int Testing_device = Phone;
    static int maxSwipeCount = 5;  //找不到檔案時的滑動次數
    static int action_execute_times = 3; //動作執行次數，例如: 刪除

    boolean isAdmin = true;
    static String username = "mike";
    static String userpass = "1234";
    static String testFolderName = "mike_test"; //Public, userFolder, USB底下的測試資料夾名稱。請自行建立
    static String imageTestFolder = "ImageTest";
    static String musicTestFolder = "MusicTest";

    static int Type_List = 0;
    static int Type_Grid = 1;

    final static int Test_Total_Numbs = 3;
    final static int Test_Folder_Public = 0;
    final static int Test_Folder_User = 1;
    final static int Test_Folder_USB = 2;


    @Before
    public void setup() throws UiObjectNotFoundException, RemoteException {
        instrumentation = getInstrumentation();
        mDevice = UiDevice.getInstance(instrumentation);

        if (isAdmin) {
            username = "admin";
            userpass = "test";
        }
    }

    protected void license_process() throws UiObjectNotFoundException, RemoteException {
        //點擊同意
        UiObject agree = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/button_agree"));
        agree.clickAndWaitForNewWindow();

        UiObject next = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/introduce_started_button"));
        next.click();
        sleep(400);
        next.click();
        sleep(400);
        next.click();
        sleep(400);
        next.click();
        sleep(400);
        next.clickAndWaitForNewWindow();

        login_process();
    }

    protected void login_process() throws UiObjectNotFoundException, RemoteException {
        UiObject email = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/edit_text_email"));
        UiObject password = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/edit_text_password"));
        UiObject login = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/login_by_email"));

        email.setText("mike_chen@transcend-info.com");
        password.setText("123456");
        login.clickAndWaitForNewWindow();

        sleep(5000);

        UiCollection nasRecyclerView = new UiCollection(new UiSelector().resourceId("com.transcend.nas:id/nas_finder_recycler_view"));
        UiObject main = nasRecyclerView.getChildByInstance(new UiSelector().resourceId("com.transcend.nas:id/listitem_nas_finder_list_layout"), 0);
        main.clickAndWaitForNewWindow();

        UiObject admin_acc = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/dialog_login_account"));
        UiObject admin_pass = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/dialog_login_password"));
        UiObject confirm = new UiObject(new UiSelector().resourceId("android:id/button1"));

        admin_acc.setText(username);
        admin_pass.setText(userpass);
        confirm.clickAndWaitForNewWindow();
    }

    protected UiScrollable getRecentRecyclerView(){
        return new UiScrollable(new UiSelector().resourceId("com.transcend.nas:id/main_recycler_view"));
    }

    protected UiScrollable getMainRecyclerView(){
        return new UiScrollable(new UiSelector().resourceId("com.transcend.nas:id/recycler_view"));
    }

    protected UiObject getItem(UiScrollable recycler, String text) {
        try {
            return recycler.getChildByText(new UiSelector().resourceId("com.transcend.nas:id/item_title"), text, true);
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected UiObject getListItemTitle(UiScrollable recycler, int instance) throws UiObjectNotFoundException {
        return recycler.getChildByInstance(new UiSelector().resourceId("com.transcend.nas:id/item_title"), instance);
    }

    protected UiObject getListItemSubTitle(UiScrollable recycler, int instance) throws UiObjectNotFoundException {
        return recycler.getChildByInstance(new UiSelector().resourceId("com.transcend.nas:id/item_subtitle"), instance);
    }

    protected UiObject getItem(UiScrollable recycler, int instance) throws UiObjectNotFoundException {
        return recycler.getChildByInstance(new UiSelector().resourceId("com.transcend.nas:id/item_manage"), instance);
    }

    protected int getItemCount(UiScrollable recycler){
        return recycler.getChildCount(new UiSelector().resourceId("com.transcend.nas:id/item_manage"));
    }

    protected UiCollection getDrawerView(){
        return new UiCollection(new UiSelector().resourceId("com.transcend.nas:id/design_navigation_view"));
    }

    protected UiObject getDrawerItem(UiCollection drawer, String text) throws UiObjectNotFoundException {
        return drawer.getChildByText(new UiSelector().resourceId("com.transcend.nas:id/design_menu_item_text"), text);
    }

    protected UiObject getDrawerItem(UiCollection drawer, int instance) throws UiObjectNotFoundException {
        return drawer.getChildByInstance(new UiSelector().resourceId("com.transcend.nas:id/design_menu_item_text"), instance);
    }

    protected UiObject getImageTitle(){
        return new UiObject(new UiSelector().resourceId("com.transcend.nas:id/toolbar_title"));
    }

    protected UiCollection getTabView(){
        return new UiCollection(new UiSelector().resourceId("com.transcend.nas:id/tabLayout"));
    }

    protected UiObject getTabItem(UiCollection tabView, int instance) throws UiObjectNotFoundException {
        return tabView.getChildByInstance(new UiSelector().className("android.support.v7.app.ActionBar$Tab"), instance);
    }

    protected void goToPublicFolder() throws UiObjectNotFoundException {
        UiObject publicFolder = getItem(getMainRecyclerView(), "Public");
        publicFolder.click();
    }

    protected void goToMikeFolder() throws UiObjectNotFoundException {
        UiObject mike = getItem(getMainRecyclerView(), "mike_test");
        mike.click();
    }

    protected boolean openDrawer() {
        UiObject drawerIcon = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/toggle_btn"));
        try {
            drawerIcon.click();
            return true;
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected UiObject getListItem(UiScrollable recycler, int instance) throws UiObjectNotFoundException {
        return recycler.getChildByInstance(new UiSelector().resourceId("com.transcend.nas:id/item_title"), instance);
    }

    //取得選取模式的選取符號，可選取資料夾
    protected UiObject getSelectModeMark(UiScrollable recycler, int instance) throws UiObjectNotFoundException {
        return recycler.getChildByInstance(new UiSelector().resourceId("com.transcend.nas:id/item_mark"), instance);
    }

    protected boolean clickDrawerBrowser() {
        UiCollection drawer = new UiCollection(new UiSelector().resourceId("com.transcend.nas:id/design_navigation_view"));
        UiObject browser = null;
        try {
            browser = drawer.getChildByInstance(new UiSelector().className("android.support.v7.widget.LinearLayoutCompat"), 0);
            browser.click();
            waitForProgress();
            return true;
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected void clickDrawerRecent() throws UiObjectNotFoundException {
        UiObject recent = getDrawerItem(getDrawerView(), "最近");
        recent.clickAndWaitForNewWindow();
    }

    protected void clickDrawerDownload() throws UiObjectNotFoundException {
        UiObject download = getDrawerItem(getDrawerView(), "下載");
        download.clickAndWaitForNewWindow();
    }

    protected void clickDrawerSystem() throws UiObjectNotFoundException {
        UiObject system = getDrawerItem(getDrawerView(), "系統資訊");
        system.clickAndWaitForNewWindow();
    }

    protected void clickDrawerSetting() throws UiObjectNotFoundException {
        UiObject system = getDrawerItem(getDrawerView(), "設定");
        system.clickAndWaitForNewWindow();
    }

    protected void clickDrawerDevice() throws UiObjectNotFoundException {
        UiObject device = getDrawerItem(getDrawerView(), 3);
        device.clickAndWaitForNewWindow();
    }

    protected boolean clickTabImage() {
        UiObject image = null;
        try {
            image = getTabItem(getTabView(), 1);
            image.click();
            waitForProgress();
            return true;
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected boolean clickTabMusic() {
        UiObject music = null;
        try {
            music = getTabItem(getTabView(), 2);
            music.click();
            waitForProgress();
            return true;
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected boolean clickTabVideo() {
        UiObject video = null;
        try {
            video = getTabItem(getTabView(), 3);
            video.click();
            waitForProgress();
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected String getBrowseTitle() throws UiObjectNotFoundException {
        return new UiObject(new UiSelector().resourceId("com.transcend.nas:id/dropdown_text")).getText();
    }

    protected void changeMediaBrowserMode(String text) throws UiObjectNotFoundException {
        openMenu();
        UiCollection menuList = getMenuList();
        UiObject item = menuList.getChildByText(new UiSelector().resourceId("com.transcend.nas:id/title"), text);
        if (item.exists())
            item.click();
    }

    protected boolean isSelectMode(){
        UiObject selectAll = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/file_manage_editor_action_select_all"));
        return selectAll.exists();
    }

    protected void DeleteItemInImageView() throws UiObjectNotFoundException {
        UiCollection single_view_footer = new UiCollection(new UiSelector().resourceId("com.transcend.nas:id/viewer_footer_bar"));
        UiObject delete = single_view_footer.getChildByInstance(new UiSelector().className("android.widget.ImageView"), 3);
        delete.clickAndWaitForNewWindow();
        sleep(800);
        UiObject confirm = new UiObject(new UiSelector().resourceId("android:id/button1"));
        confirm.clickAndWaitForNewWindow();
        sleep(1500);
    }

    protected void DownloadItemInImageView() throws UiObjectNotFoundException {
        UiCollection single_view_footer = new UiCollection(new UiSelector().resourceId("com.transcend.nas:id/viewer_footer_bar"));
        UiObject download = single_view_footer.getChildByInstance(new UiSelector().className("android.widget.ImageView"), 1);
        download.clickAndWaitForNewWindow();
        sleep(800);
        UiObject confirm = new UiObject(new UiSelector().resourceId("android:id/button1"));
        confirm.clickAndWaitForNewWindow();
        sleep(1500);
    }

    protected boolean openMenu() {
        UiObject more = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/file_manage_viewer_action_more"));
        try {
            more.click();
            sleep(500);
            return true;
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected UiCollection getMenuList(){
        return new UiCollection(new UiSelector().className("android.widget.ListView"));
    }

    protected int getMenuCount(UiCollection menuList){
        return menuList.getChildCount(new UiSelector().className("android.widget.LinearLayout"));
    }

    protected UiObject getMenuItem(UiCollection menuList, int instance) throws UiObjectNotFoundException {
        return menuList.getChildByInstance(new UiSelector().className("android.widget.LinearLayout"), instance);
    }

    protected void clickMenuUpload() throws UiObjectNotFoundException {
        UiCollection list = new UiCollection(new UiSelector().className("android.widget.ListView"));
        UiObject upload = list.getChildByInstance(new UiSelector().className("android.widget.LinearLayout"), 3);
        upload.clickAndWaitForNewWindow();
    }

    protected void changePickerViewType() throws UiObjectNotFoundException {
        UiObject typeBtn = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/file_picker_viewer_action_view"));
        typeBtn.click();
    }

    protected void sleep(int mills){
        try {
            Thread.sleep(mills);
        }catch(Exception e){

        }
    }

    protected void swipeToNextViewPager(){
        mDevice.swipe(mDevice.getDisplayWidth()*2/3, mDevice.getDisplayHeight() / 2,
                0, mDevice.getDisplayHeight() / 2, 10);
        sleep(500);
    }

    protected void lightSwipeToNextViewPager(){
        //輕輕左滑一下
        mDevice.swipe(mDevice.getDisplayWidth() / 2, mDevice.getDisplayHeight() / 2,
                (mDevice.getDisplayWidth() / 2) - 90, mDevice.getDisplayHeight() / 2, 10);
        sleep(1000);
    }

    protected void closeAPP(UiDevice uiDevice, String sPackageName){
//        Log.i(TAG, "closeAPP: ");
        try {
            uiDevice.executeShellCommand("am force-stop "+sPackageName);//通過命令行關閉app
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void startAPP(UiDevice uiDevice,String sPackageName, String sLaunchActivity){
        try {
            uiDevice.executeShellCommand("am start -n "+sPackageName+"/"+sLaunchActivity);//通過命令行啟動app
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void startAPP(String sPackageName){
        Context mContext = instrumentation.getContext();

        Intent myIntent = mContext.getPackageManager().getLaunchIntentForPackage(sPackageName);  //通过Intent启动app
        mContext.startActivity(myIntent);
    }

    protected void waitForProgress(){
        UiObject progress = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/main_progress_bar"));
        while(progress.exists()){
            progress = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/main_progress_bar"));
        }
    }

    protected void waitForMusicProgress(){
        UiObject progress = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/music_progress_view"));
        while(progress.exists()){
            progress = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/music_progress_view"));
        }
    }

    protected void changeBrowserViewType() throws UiObjectNotFoundException {
        UiObject menu = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/file_manage_viewer_action_more"));
        menu.click();

        UiCollection menuList = new UiCollection(new UiSelector().className("android.widget.ListView"));
        UiObject changeViewType = menuList.getChildByInstance(new UiSelector().className("android.widget.LinearLayout"),
                menuList.getChildCount(new UiSelector().className("android.widget.LinearLayout")) - 1);
        changeViewType.clickAndWaitForNewWindow();
    }

    protected boolean isGridView() throws UiObjectNotFoundException {
        UiObject title = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/item_title"));
        UiObject icon = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/item_icon"));
        if (!title.exists())
            return true;

        if (icon.getBounds().centerY() < title.getBounds().centerY())
            return true;

        return false;
    }

    protected boolean isBrowserTitleEquals(String text) {
        UiObject titleObj;
        try {
            titleObj = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/dropdown_text"));
            return titleObj.getText().equals(text);
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    //TODO 檢查排版
    protected void checkComposing(UiScrollable recycler, boolean portraitOnly) throws UiObjectNotFoundException, RemoteException {
        if (recycler.getChildCount(new UiSelector().resourceId("com.transcend.nas:id/item_manage")) == 0)
            return;

        UiObject gridItem;
        int column_count;

        if (Testing_device == Phone){
            mDevice.setOrientationNatural();
            sleep(1000);
            gridItem = recycler.getChildByInstance(new UiSelector().resourceId("com.transcend.nas:id/item_manage"), 0);
            column_count = (int) (mDevice.getDisplayWidth() / gridItem.getBounds().width());
            Assert.assertEquals(3, column_count);

            if (!portraitOnly){
                mDevice.setOrientationLeft();
                sleep(1000);
                gridItem = recycler.getChildByInstance(new UiSelector().resourceId("com.transcend.nas:id/item_manage"), 0);
                column_count = (int) (mDevice.getDisplayWidth() / gridItem.getBounds().width());
                Assert.assertEquals(6, column_count);
            }
        }
        else{
            mDevice.setOrientationNatural();
            sleep(1000);
            gridItem = recycler.getChildByInstance(new UiSelector().resourceId("com.transcend.nas:id/item_manage"), 0);
            column_count = (int) (mDevice.getDisplayWidth() / gridItem.getBounds().width());
            Assert.assertEquals(6, column_count);

            if (!portraitOnly) {
                mDevice.setOrientationLeft();
                sleep(1000);
                gridItem = recycler.getChildByInstance(new UiSelector().resourceId("com.transcend.nas:id/item_manage"), 0);
                column_count = (int) (mDevice.getDisplayWidth() / gridItem.getBounds().width());
                Assert.assertEquals(8, column_count);
            }
        }

        mDevice.setOrientationNatural();
        mDevice.unfreezeRotation();
    }

    //檢查圖片數，應該只能有一張圖
    protected void checkImageNumber(){
        UiCollection viewpager = new UiCollection(new UiSelector().resourceId("com.transcend.nas:id/photo_view_pager"));
        if (viewpager.exists()){
            Assert.assertEquals(1, viewpager.getChildCount(new UiSelector().resourceId("com.transcend.nas:id/viewer_image")));
        }
    }

    protected static boolean isAudio(String fileSubTitle){
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

    protected static boolean isVideo(String fileSubTitle){
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

    protected static boolean isImage(String fileSubTitle){
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

    protected void getPermission(UiDevice uiDevice){
        try {
            uiDevice.executeShellCommand("pm grant com.transcend.autotest android.permission.READ_EXTERNAL_STORAGE");
            uiDevice.executeShellCommand("pm grant com.transcend.autotest android.permission.WRITE_EXTERNAL_STORAGE");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void scrollToEndOrTop(UiScrollable recyclerView, boolean toEnd) throws UiObjectNotFoundException {
        int x = 0, y = 0;
        int sameValueCount = 0; //若滑動N次位置恆不變，則判斷為已滑至底部
        while(sameValueCount < maxSwipeCount){
            if (getItemCount(recyclerView) > 0){
                UiObject item = getItem(recyclerView, 0);
                int tmpX = item.getBounds().centerX();
                int tmpY = item.getBounds().centerY();
                if (tmpX == x && tmpY == y)
                    sameValueCount++;
                else{
                    x = tmpX;
                    y = tmpY;
                }

                if (toEnd)
                    recyclerView.swipeUp(recyclerView.getMaxSearchSwipes());
                else
                    recyclerView.swipeDown(recyclerView.getMaxSearchSwipes());
            }
        }
    }

    protected boolean isAtBrowserView(){
        return getMainRecyclerView().exists();
    }

    protected boolean isAtImageView(){
        return new UiObject(new UiSelector().resourceId("com.transcend.nas:id/photo_view_pager")).exists();
    }

    protected boolean isAtMusicView(){
        return new UiObject(new UiSelector().resourceId("com.transcend.nas:id/music_image")).exists();
    }

    //進到測試資料夾，目前預設是/Public/mike_test/
    protected boolean enterTestFolder(int test_folder_num) throws UiObjectNotFoundException {
        //回到瀏覽首頁
        openDrawer();
        clickDrawerBrowser();

        UiScrollable recyclerView = getMainRecyclerView();

        String testFolderText = null;
        switch (test_folder_num){
            case Test_Folder_User:
                testFolderText = username;
                break;
            case Test_Folder_USB:
                testFolderText = "USB";
                break;
            default:
                testFolderText = "Public";
                break;
        }

        //進入test資料夾
        UiObject publicFolder = getItem(recyclerView, testFolderText);
        if (publicFolder.exists()) {
            publicFolder.click();
            waitForProgress();

            recyclerView = getMainRecyclerView();
            //進入mike_test資料夾
            UiObject mikeFolder = getItem(recyclerView, testFolderName);
            if (mikeFolder.exists()) {
                mikeFolder.click();
                waitForProgress();
                return true;
            }
        }
        return false;
    }

    protected void longClick(UiObject item) throws UiObjectNotFoundException {
        //長按的另一種解法，用以控制秒數。longclick有時會無法正確觸發
        mDevice.swipe(item.getBounds().centerX(), item.getBounds().centerY(),
                item.getBounds().centerX(), item.getBounds().centerY(), 100);
        sleep(500);
    }
}
