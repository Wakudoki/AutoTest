package com.transcend.autotest.StoreJetCloud;

import android.os.RemoteException;
import android.support.test.uiautomator.UiCollection;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class StoreJetCloud_BasicTest extends StoreJetCloudAbstract {

    static String [] search_text_list = {"fo", "jpg", "png", "mkv", "mp3"};

    @Before
    public void setup() throws RemoteException, UiObjectNotFoundException {
        super.setup();
        sleep(3000);
    }

    @Test
    public void Browser_RootFolder() throws UiObjectNotFoundException {
        //回到瀏覽首頁
        openDrawer();
        clickDrawerBrowser();

        UiScrollable recyclerView = getMainRecyclerView();

        //至少要有Public & user folder
        Assert.assertEquals(true, getItemCount(recyclerView) >= 2);

        Assert.assertEquals(true, getItem(recyclerView, "Public").exists());
        Assert.assertEquals(true, getItem(recyclerView, username).exists());

        //試著切換瀏覽型態
        if (isGridView())
            changeBrowserViewType();
        else{
            changeBrowserViewType();
            sleep(1000);
            changeBrowserViewType();
        }

        //首頁不能有副標題
        for (int i = 0; i < getItemCount(recyclerView); i++) {
            Assert.assertEquals(false, getListItemSubTitle(recyclerView, i).exists());
        }

        //嘗試進入選擇模式，首頁不能進入選擇模式
        UiObject item = getItem(recyclerView, 0);
        item.longClick();
        boolean isSelectMode = isSelectMode();
        Assert.assertEquals(false, isSelectMode);
        if (isSelectMode)
            mDevice.pressBack();

        //首頁除了最後一項(瀏覽型態切換)外，皆不能點選
        openMenu();
        UiCollection menuList = getMenuList();
        for (int i = 0; i < getMenuCount(menuList) - 1; i++){
            UiObject menuItem = getMenuItem(menuList, i);
            Assert.assertEquals(false, menuItem.isEnabled());
        }
        mDevice.pressBack();
        waitForProgress();
    }

    @Test
    public void Browser_EnterFolder() throws UiObjectNotFoundException, RemoteException {
        //回到瀏覽首頁
        openDrawer();
        clickDrawerBrowser();

        String targetFolder;

        int target = Test_Folder_Public;
        while(target < Test_Total_Numbs){
            switch(target){
                case Test_Folder_User:
                    targetFolder = username;
                    break;
                case Test_Folder_USB:
                    targetFolder = "USB";
                    break;
                default:
                    targetFolder = "Public";
                    break;
            }
            UiScrollable recyclerView = getMainRecyclerView();

            //進入測試資料夾
            UiObject testFolder = getItem(recyclerView, targetFolder);
            if (testFolder != null && testFolder.exists()) {
                testFolder.click();
                waitForProgress();

                if (isGridView())
                    changeBrowserViewType();
                recyclerView = getMainRecyclerView();

                //點擊各資料夾
                int folderCount = recyclerView.getChildCount(new UiSelector().resourceId("com.transcend.nas:id/item_info"));
                for (int i = 0; i < folderCount; i++){
                    UiObject folder = recyclerView.getChildByInstance(new UiSelector().resourceId("com.transcend.nas:id/item_info"), i);
                    String folder_name = getListItemTitle(recyclerView, i).getText();
                    folder.click();
                    waitForProgress();

                    //確定進到該資料夾
                    if (folder_name.equals(getBrowseTitle())) {
                        //切換瀏覽型態，檢查Grid排版
                        if (!isGridView())
                            changeBrowserViewType();
                        //檢查排版
                        checkComposing(recyclerView, false);
                        sleep(500);

                        changeBrowserViewType();    //切回List

                        //以返回鍵回到上一層
                        mDevice.pressBack();
                        waitForProgress();

                        if (folder.exists()) {
                            folder.click();

                            //進行history page點擊
                            UiObject historyArrow = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/dropdown_arrow"));
                            if (historyArrow.exists()) {
                                historyArrow.clickAndWaitForNewWindow();

                                UiCollection historyList = new UiCollection(new UiSelector().className("android.widget.ListView"));
                                UiObject parent = historyList.getChildByInstance(new UiSelector().className("android.widget.RelativeLayout"), 1);
                                parent.click();
                            } else
                                mDevice.pressBack();
                            waitForProgress();
                        }
                    }
                }

                mDevice.pressBack();
                waitForProgress();
            }   //End of test folder

            target++;
        }
    }

    @Test
    public void Browser_Click() throws UiObjectNotFoundException {
        //回到瀏覽首頁
        openDrawer();
        clickDrawerBrowser();

        String targetFolder;

        int target = Test_Folder_Public;
        while(target < Test_Total_Numbs) {
            switch (target) {
                case Test_Folder_User:
                    targetFolder = username;
                    break;
                case Test_Folder_USB:
                    targetFolder = "USB";
                    break;
                default:
                    targetFolder = "Public";
                    break;
            }

            UiScrollable recyclerView = getMainRecyclerView();
            //進入測試資料夾
            UiObject testFolder = getItem(recyclerView, targetFolder);
            if (testFolder != null && testFolder.exists()) {
                testFolder.click();
                waitForProgress();

                recyclerView = getMainRecyclerView();
                //檢查點擊功能
                for (int i = 0; i < getItemCount(recyclerView); i++){
                    UiObject item = getItem(recyclerView, i);
                    String itemTitle = "";
                    if (item.exists()){
                        itemTitle = getListItemTitle(recyclerView, i).getText();
                        item.clickAndWaitForNewWindow();
                        waitForProgress();

                        if (isAtBrowserView()){ //如果是資料夾，檢查無檔案圖示的顯示
                            //判斷點擊的物件名稱與進入的資料夾名稱是否一致
                            Assert.assertEquals(true, itemTitle.equals(new UiObject(new UiSelector().resourceId("com.transcend.nas:id/dropdown_text")).getText()));

                            UiScrollable subRecycle = getMainRecyclerView();
                            //有檔案存在時，無檔案的圖示不能存在，反之亦然
                            UiObject noItemViewObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/empty_view"));
                            if (getItemCount(subRecycle) == 0)
                                Assert.assertEquals(true, noItemViewObj.exists());
                            else
                                Assert.assertEquals(false, noItemViewObj.exists());
                        }
                        else if (isAtImageView()){
                            //判斷點擊的物件名稱與進入的圖片名稱是否一致
                            Assert.assertEquals(true, itemTitle.equals(getImageTitle().getText()));
                        }
                        else if (isAtMusicView()){
                            //判斷點擊的物件名稱與進入的音樂名稱是否一致
                            Assert.assertEquals(true, itemTitle.equals(getImageTitle().getText()));
                        }

                        mDevice.pressBack();
                        waitForProgress();
                    }
                }

                //檢查長按功能
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
                    else{   //因為長按並非每次都有作用，若未盡到選取模式，則點擊返回鍵回到瀏覽頁面
                        mDevice.pressBack();
                        waitForProgress();
                    }

                    if (isSelectMode) {
                        mDevice.pressBack();
                        waitForProgress();
                    }

                    sleep(500);
                }

                mDevice.pressBack();
                waitForProgress();
            }
            target++;
        }
    }

    @Test
    public void Option() throws UiObjectNotFoundException {
        boolean isEnterTestFolder = enterTestFolder(Test_Folder_Public);
        if (isEnterTestFolder){
            //打開菜單
            openMenu();
            UiCollection menuList = getMenuList();
            //進入選取模式
            UiObject selectModeObj = getMenuItem(menuList, 1);
            selectModeObj.click();

            UiScrollable recycler = getMainRecyclerView();
            int selectCount = 0;
            for (int i = 0; i < getItemCount(recycler); i++){
                if (selectCount == 0){  //0選取不能出現選單
                    openMenu();
                    menuList = getMenuList();
                    Assert.assertEquals(false, menuList.exists());
                }

                UiObject selectModeTitleObj = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/action_mode_custom_title"));
                if (selectCount > 0)
                    Assert.assertEquals(true, selectModeTitleObj.getText().equals(selectCount + "個選取項目"));
                else
                    Assert.assertEquals(true, selectModeTitleObj.getText().equals("沒有選取項目"));

                UiObject selectMarkObj = getSelectModeMark(recycler, i);
                if (selectMarkObj.exists()){
                    selectMarkObj.click();
                    sleep(500);
                    Assert.assertEquals(true, selectMarkObj.isSelected());
                    selectCount ++;
                }
            }

            UiObject selectAllObj = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/file_manage_editor_action_select_all"));
            selectAllObj.click();
            sleep(500);
            selectAllObj.click();
            sleep(500);

            //離開選取模式
            if (isSelectMode())
                mDevice.pressBack();
            waitForProgress();

            sleep(1000);

            //////////測試新增資料夾//////////
            //打開菜單
            openMenu();
            menuList = getMenuList();
            UiObject newFolder = getMenuItem(menuList, 4);
            newFolder.clickAndWaitForNewWindow();

            UiObject nameObj = new UiObject(new UiSelector().className("android.widget.EditText"));
            nameObj.setText("新增資料夾測試");
            String newFolderName = nameObj.getText();
            UiObject repeatName = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/textinput_error"));
            int count = 0;
            while(repeatName.exists()){
                nameObj.setText(newFolderName + " (" + count++ + ")");
                repeatName = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/textinput_error"));
                sleep(800);
            }
            newFolderName = nameObj.getText();

            UiObject confirmBtnObj = new UiObject(new UiSelector().resourceId("android:id/button1"));
            confirmBtnObj.click();
            sleep(500);
            waitForProgress();

            recycler = getMainRecyclerView();
            UiObject newItem = getItem(recycler, newFolderName);
            Assert.assertEquals(true, newItem.exists());
            //////////測試新增資料夾 - 結束//////////

            //////////測試搜尋//////////        文字填完後無法按enter以開始搜尋
//            UiObject searchObj = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/search_btn"));
//            if (searchObj.exists()){
//                searchObj.click();
//                sleep(500);
//
//                UiObject searchEditView = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/search_editText"));
//                if (searchEditView.exists()) {  //正確進入選取模式
//                    for (String searchText : search_text_list) {
//                        searchEditView.setText(searchText);
//                        searchEditView.clickBottomRight();
//
//                        UiScrollable searchRecycler = getMainRecyclerView();
//                        for (int i = 0; i < getItemCount(searchRecycler); i++) {
//                            String title = getListItemTitle(searchRecycler, i).getText();
//                            Assert.assertEquals(true, title.contains(searchText));
//                        }
//                    }
//                    mDevice.pressBack();    //點返回鍵後應該要離開選取模式
//                    Assert.assertEquals(false, searchEditView.exists());
//                }
//
//                searchObj.click();
//                sleep(500);
//                searchEditView = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/search_editText"));
//                UiObject toggleObj = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/toggle_btn"));
//                if (searchEditView.exists()){
//                    toggleObj.click();  //點左上角的返回鍵後應該要離開選取模式
//                    Assert.assertEquals(false, searchEditView.exists());
//                }
//            }
            //////////測試搜尋 - 結束//////////
        }
    }

    @Test
    public void Browser_Tabs() throws UiObjectNotFoundException, RemoteException {
        openDrawer();
        clickDrawerBrowser();
        clickTabImage();
        if (!getBrowseTitle().equals("查看全部照片"))
            changeMediaBrowserMode("查看全部照片");
        UiScrollable mediaRecycler = getMainRecyclerView();
        for (int i = 0; i < getItemCount(mediaRecycler); i++){
            UiObject item = getItem(mediaRecycler, i);
            if (isGridView()){
                UiObject itemTitle = item.getChild(new UiSelector().resourceId("com.transcend.nas:id/item_title"));
                if (itemTitle.exists()) //圖示顯示時，此處有標題表示該圖暫時不支援
                    continue;
            }
            if (item.exists()){
                item.clickAndWaitForNewWindow();
                sleep(500);
                Assert.assertEquals(true, isAtImageView());

                if (i % 2 == 0) //測試系統返回鍵 & 左上返回鍵
                    mDevice.pressBack();
                else {
                    UiObject backObj = new UiObject(new UiSelector().className("android.widget.ImageButton"));
                    backObj.click();
                }
                waitForProgress();

                if (!isGridView())
                    changeBrowserViewType();

                checkComposing(mediaRecycler, true);
            }
        }

        clickTabMusic();
        if (!getBrowseTitle().equals("查看全部歌曲"))
            changeMediaBrowserMode("查看全部歌曲");
        mediaRecycler = getMainRecyclerView();
        int count = getItemCount(mediaRecycler);
        for (int i = 0; i < getItemCount(mediaRecycler); i++){
            UiObject item = getItem(mediaRecycler, i);
            if (item.exists()){
                item.clickAndWaitForNewWindow();
                waitForMusicProgress();
                sleep(500);
                Assert.assertEquals(true, isAtMusicView());

                if (i % 2 == 0) //測試系統返回鍵 & 左上返回鍵
                    mDevice.pressBack();
                else {
                    UiObject backObj = new UiObject(new UiSelector().className("android.widget.ImageButton"));
                    backObj.click();
                }
                waitForProgress();
            }

            if (!isGridView())
                changeBrowserViewType();

            checkComposing(mediaRecycler, true);
        }
    }

    @Test
    public void SingleView_Image() throws UiObjectNotFoundException, RemoteException {
        boolean isEnterTestFolder = enterTestFolder(Test_Folder_Public);
        if (isEnterTestFolder){
            UiScrollable recycler = getMainRecyclerView();
            UiObject imageTestFolderObj = getItem(recycler, imageTestFolder);
            if (imageTestFolderObj.exists()) {
                imageTestFolderObj.clickAndWaitForNewWindow();
                sleep(500);

                recycler = getMainRecyclerView();
                if (getItemCount(recycler) > 0){
                    UiObject item = getItem(recycler, 0);
                    item.clickAndWaitForNewWindow();

                    if (!isAtImageView())
                        return;

                    //////////檢查資訊頁面//////////
                    UiObject infoObj = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/media_info"));
                    infoObj.clickAndWaitForNewWindow();
                    sleep(1000);

                    UiCollection infoRecycler = new UiCollection(new UiSelector().resourceId("com.transcend.nas:id/infomation_recycler_view"));
                    UiObject infoItem = infoRecycler.getChildByInstance(new UiSelector().resourceId("com.transcend.nas:id/item_manage"), 0);
                    infoItem.waitForExists(10000);
                    int infoItemCount = infoRecycler.getChildCount(new UiSelector().resourceId("com.transcend.nas:id/item_manage"));
                    Assert.assertEquals(true,  infoItemCount >= 2);  //至少兩項資訊

                    //測試系統返回鍵 & 左上返回鍵
                    UiObject backObj = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/toggle_btn"));
                    backObj.click();
                    Assert.assertEquals(true, isAtImageView());

                    infoObj.clickAndWaitForNewWindow();
                    sleep(1000);

                    mDevice.pressBack();
                    Assert.assertEquals(true, isAtImageView());
                    //////////檢查資訊頁面 - 結束//////////

                    //////////檢查Footer功能//////////
                    UiCollection footer = new UiCollection(new UiSelector().resourceId("com.transcend.nas:id/viewer_footer_bar"));
                    UiObject shareObj = footer.getChildByInstance(new UiSelector().className("android.widget.ImageView"), 0);
                    UiObject downloadObj = footer.getChildByInstance(new UiSelector().className("android.widget.ImageView"), 1);
//                    UiObject sharelinkObj = footer.getChildByInstance(new UiSelector().className("android.widget.ImageView"), 2);
                    UiObject deleteObj = footer.getChildByInstance(new UiSelector().className("android.widget.ImageView"), 3);

                    shareObj.clickAndWaitForNewWindow(10000);
                    UiObject shareList = new UiObject(new UiSelector().resourceId("android:id/resolver_list"));
                    Assert.assertEquals(true, shareList.exists());  //理當存在，不存在也無執行下去的必要
                    mDevice.pressBack();

//                    sharelinkObj.clickAndWaitForNewWindow(10000);
//                    UiObject always = new UiObject(new UiSelector().resourceId("android:id/button_always"));
//                    if (!always.exists())
//                        return;
//                    Assert.assertEquals(true, always.exists());
//                    mDevice.pressBack();

                    DownloadItemInImageView();

                    //刪除後檢查名稱是否不一致
                    String title = getImageTitle().getText();
                    DeleteItemInImageView();
                    if (isAtImageView())    //若無檔案會跳回Browser頁面
                        Assert.assertEquals(false, title.equals(getImageTitle().getText()));
                    else {
                        Assert.assertEquals(true, new UiObject(new UiSelector().resourceId("com.transcend.nas:id/empty_view")).exists());
                        return; //回瀏覽頁面無法繼續以下檢查
                    }

                    //左上角返回
                    UiObject imgBackObj = new UiObject(new UiSelector().className("android.widget.ImageButton"));
                    imgBackObj.clickAndWaitForNewWindow();

                    //切換瀏覽模式
                    if (!isGridView())
                        changeBrowserViewType();
                    //檢查排版
                    recycler = getMainRecyclerView();
                    checkComposing(recycler, true);

                    //進到ImageView
                    item = getItem(recycler, 0);
                    item.clickAndWaitForNewWindow();

                    //系統返回鍵
                    mDevice.pressBack();
                    waitForProgress();

                    //切換瀏覽模式
                    if (!isGridView())
                        changeBrowserViewType();
                    //檢查排版
                    recycler = getMainRecyclerView();
                    checkComposing(recycler, true);

                    //進到ImageView
                    item = getItem(recycler, 0);
                    item.clickAndWaitForNewWindow();

                    //連續刪除
                    for (int i = 0; i < action_execute_times; i++){
                        //刪除後檢查名稱是否不一致
                        title = getImageTitle().getText();
                        DeleteItemInImageView();
                        if (isAtImageView())    //若無檔案會跳回Browser頁面
                            Assert.assertEquals(false, title.equals(getImageTitle().getText()));
                        else {
                            Assert.assertEquals(true, new UiObject(new UiSelector().resourceId("com.transcend.nas:id/empty_view")).exists());
                            return;  //回瀏覽頁面無法繼續以下檢查
                        }
                    }
                    //////////檢查Footer功能 - 結束//////////

                    //////////檢查Swipe功能//////////
                    for (int i = 0; i < action_execute_times; i++){
                        //旋轉，檢查圖片數目 & 名稱
                        title = getImageTitle().getText();
                        mDevice.setOrientationLeft();
                        checkImageNumber();
                        Assert.assertEquals(title, getImageTitle().getText());
                        mDevice.setOrientationNatural();
                        mDevice.unfreezeRotation();

                        //滑動檢查圖片數目
                        swipeToNextViewPager();
                        checkImageNumber();

                        //輕輕滑動檢查名稱
                        title = getImageTitle().getText();
                        lightSwipeToNextViewPager();
                        Assert.assertEquals(title, getImageTitle().getText());
                    }
                    //////////檢查Swipe功能 - 結束//////////


                }
            }

            if (isAtImageView())    //回到瀏覽頁面
                mDevice.pressBack();
            waitForProgress();
        }
    }

    @Test
    public void SingleView_Music() throws UiObjectNotFoundException {
        boolean isEnterTestFolder = enterTestFolder(Test_Folder_Public);
        if (isEnterTestFolder) {
            UiScrollable recycler = getMainRecyclerView();
            UiObject musicTestFolder = getItem(recycler, "MusicTest");
            if (musicTestFolder.exists()) {
                musicTestFolder.clickAndWaitForNewWindow();
                sleep(500);

                recycler = getMainRecyclerView();
                if (getItemCount(recycler) > 0) {
                    UiObject item = getItem(recycler, 0);
                    item.clickAndWaitForNewWindow();
                    waitForMusicProgress();

                    if (!isAtMusicView())
                        return;

                    //////////檢查資訊頁面//////////
                    UiObject infoObj = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/media_info"));
                    infoObj.waitForExists(10000);
                    infoObj.clickAndWaitForNewWindow();
                    sleep(1000);

                    UiCollection infoRecycler = new UiCollection(new UiSelector().resourceId("com.transcend.nas:id/infomation_recycler_view"));
                    UiObject infoItem = infoRecycler.getChildByInstance(new UiSelector().resourceId("com.transcend.nas:id/item_manage"), 0);
                    infoItem.waitForExists(10000);
                    int infoItemCount = infoRecycler.getChildCount(new UiSelector().resourceId("com.transcend.nas:id/item_manage"));
                    Assert.assertEquals(true, infoItemCount >= 2);  //至少兩項資訊

                    //測試系統返回鍵 & 左上返回鍵
                    UiObject backObj = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/toggle_btn"));
                    backObj.click();
                    Assert.assertEquals(true, isAtMusicView());

                    infoObj.clickAndWaitForNewWindow();
                    sleep(1000);

                    mDevice.pressBack();
                    Assert.assertEquals(true, isAtMusicView());
                    //////////檢查資訊頁面 - 結束//////////

                    //////////檢查按鈕//////////
                    UiObject play = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/music_play"));
                    UiObject prev = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/music_previous"));
                    UiObject next = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/music_next"));

                    String title = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/music_toolbar_title")).getText();

                    //檢查下一首
                    next.click();
                    waitForMusicProgress();
                    Assert.assertEquals(false, title.equals(new UiObject(new UiSelector().resourceId("com.transcend.nas:id/music_toolbar_title")).getText()));
                    title = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/music_toolbar_title")).getText();

                    //檢查上一首
                    prev.click();
                    waitForMusicProgress();
                    Assert.assertEquals(false, title.equals(new UiObject(new UiSelector().resourceId("com.transcend.nas:id/music_toolbar_title")).getText()));

                    String preTime = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/music_current_time")).getText();
                    sleep(3000);
                    String postTime = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/music_current_time")).getText();
                    play.click();
                    if (preTime.equals(postTime)){  ////表示從暫停→播放
                        preTime = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/music_current_time")).getText();
                        sleep(3000);
                        postTime = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/music_current_time")).getText();
                        Assert.assertEquals(false, preTime.equals(postTime));
                        play.click();
                    }
                    else{   //表示從播放→暫停
                        preTime = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/music_current_time")).getText();
                        sleep(3000);
                        postTime = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/music_current_time")).getText();
                        Assert.assertEquals(true, preTime.equals(postTime));
                    }
                    //////////檢查按鈕 - 結束//////////

                    mDevice.pressBack();
                    waitForProgress();
                }

                //////////檢查返回鍵//////////
                if(isGridView())
                    changeBrowserViewType();

                recycler =getMainRecyclerView();
                for (int i = 0; i < getItemCount(recycler); i++) {
                    UiObject item = getItem(recycler, i);
                    String itemTitle = getListItemTitle(recycler, i).getText();
                    if (item.exists()) {
                        item.click();
                        waitForMusicProgress();

                        if (!isAtMusicView())
                            return;

                        //檢查進入single view的名稱
                        String singleViewTitle = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/music_toolbar_title")).getText();
                        Assert.assertEquals(itemTitle, singleViewTitle);

                        if (i % 2 == 0) {
                            UiObject toggleBack = new UiObject(new UiSelector().className("android.widget.ImageButton"));
                            if (toggleBack.exists()) {
                                toggleBack.click();
                                waitForProgress();
                            }
                        }
                        else{
                            mDevice.pressBack();
                            waitForProgress();
                        }
                    }
                }
                //////////檢查返回鍵 - 結束//////////
            }
        }
    }

    @Test
    public void Local_Device() throws UiObjectNotFoundException, RemoteException {
        //進到本機
        openDrawer();
        clickDrawerDevice();

        UiScrollable recyclerView = getMainRecyclerView();

        //////////檢查排版//////////
        //更改瀏覽型態並檢查排版
        for (int i = 0; i < action_execute_times; i++){
            changeBrowserViewType();
            if (isGridView())
                checkComposing(recyclerView, true);
        }

        if (isGridView())
            changeBrowserViewType();
        //////////檢查排版 - 結束//////////

        //////////檢查點擊資料夾//////////
        //點擊各資料夾
        int folderCount = getItemCount(recyclerView);
        for (int i = 0; i < folderCount; i++){
            UiObject folderArrow = recyclerView.getChildByInstance(new UiSelector().resourceId("com.transcend.nas:id/item_info"), i);
            UiObject item = getItem(recyclerView, i);
            String folder_name = getListItemTitle(recyclerView, i).getText();
            if(folderArrow.exists()) {
                item.click();
                waitForProgress();

                //確定進到該資料夾
                if (folder_name.equals(getBrowseTitle())) {
                    //切換瀏覽型態，檢查Grid排版
                    if (!isGridView())
                        changeBrowserViewType();
                    //檢查排版
                    checkComposing(recyclerView, false);
                    sleep(500);

                    changeBrowserViewType();    //切回List

                    //以返回鍵回到上一層
                    mDevice.pressBack();
                    waitForProgress();

                    item.click();

                    //進行history page點擊
                    UiObject historyArrow = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/dropdown_arrow"));
                    if (historyArrow.exists()) {
                        historyArrow.clickAndWaitForNewWindow();

                        UiCollection historyList = new UiCollection(new UiSelector().className("android.widget.ListView"));
                        UiObject parent = historyList.getChildByInstance(new UiSelector().className("android.widget.RelativeLayout"), 1);
                        parent.click();
                    } else
                        mDevice.pressBack();
                    waitForProgress();
                }
            }
        }
        //////////檢查點擊資料夾 - 結束//////////

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
            else{   //若未進入到選取模式，則點擊返回鍵回到瀏覽頁面
                mDevice.pressBack();
                waitForProgress();
            }


            if (isSelectMode) {
                mDevice.pressBack();
                waitForProgress();
            }

            sleep(500);
        }
        //////////檢查長按 - 結束//////////

        //////////檢查選擇模式//////////
        //打開菜單
        openMenu();
        UiCollection menuList = getMenuList();
        //進入選取模式
        UiObject selectModeObj = getMenuItem(menuList, 1);
        selectModeObj.click();

        int selectCount = 0;
        for (int i = 0; i < getItemCount(recyclerView); i++){
            UiObject selectModeTitleObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/action_mode_custom_title"));
            if (selectCount > 0)
                Assert.assertEquals(true, selectModeTitleObj.getText().equals(selectCount + "個選取項目"));
            else
                Assert.assertEquals(true, selectModeTitleObj.getText().equals("沒有選取項目"));

            UiObject selectMarkObj = getSelectModeMark(recyclerView, i);
            if (selectMarkObj.exists()){
                selectMarkObj.click();
                sleep(500);
                Assert.assertEquals(true, selectMarkObj.isSelected());
                selectCount ++;
            }
        }

        //離開選取模式
        if (isSelectMode())
            mDevice.pressBack();


        sleep(1000);
        //////////檢查選擇模式 - 結束//////////

        //////////測試新增資料夾//////////
        //打開菜單
        openMenu();
        menuList = getMenuList();
        UiObject newFolder = getMenuItem(menuList, 3);
        newFolder.clickAndWaitForNewWindow();

        UiObject nameObj = new UiObject(new UiSelector().className("android.widget.EditText"));
        nameObj.setText("新增資料夾測試");
        String newFolderName = nameObj.getText();
        UiObject repeatName = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/textinput_error"));
        int count = 0;
        while(repeatName.exists()){
            nameObj.setText(newFolderName + " (" + count++ + ")");
            repeatName = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/textinput_error"));
        }
        newFolderName = nameObj.getText();

        UiObject confirmBtnObj = new UiObject(new UiSelector().resourceId("android:id/button1"));
        confirmBtnObj.click();
        sleep(500);

        recyclerView = getMainRecyclerView();
        UiObject newItem = getItem(recyclerView, newFolderName);
        Assert.assertEquals(true, newItem.exists());
        //////////測試新增資料夾 - 結束//////////
    }

    @Test
    public void System() throws UiObjectNotFoundException {
        openDrawer();
        clickDrawerSystem();

        UiObject power = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/system_reboot"));
        if (isAdmin)
            Assert.assertEquals(true, power.exists());
        else
            Assert.assertEquals(false, power.exists());

        UiObject settingProgress = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/settings_progress_view"));
        settingProgress.waitUntilGone(10000);

        UiScrollable systemRecycler = new UiScrollable(new UiSelector().resourceId("com.transcend.nas:id/system_recycler_view"));
        UiObject nasName = systemRecycler.getChildByInstance(new UiSelector().resourceId("com.transcend.nas:id/content_layout"), 1);
        UiObject username = nasName.getFromParent(new UiSelector().resourceId("android:id/summary"));
        if (isAdmin) {
            Assert.assertEquals(true, nasName.isClickable());
            Assert.assertEquals(true, username.getText().equals("admin"));
        }
        else {
            Assert.assertEquals(false, nasName.isClickable());
            Assert.assertEquals(true, username.getText().equals(this.username));
        }

        UiObject progress = systemRecycler.getChild(new UiSelector().resourceId("com.transcend.nas:id/preference_progress"));
        if (isAdmin)
            Assert.assertEquals(true, progress.exists());
        else
            Assert.assertEquals(false, progress.exists());

        if (isAdmin){
            UiObject storage = systemRecycler.getChildByInstance(new UiSelector().resourceId("com.transcend.nas:id/content_layout"), 3);
            if (storage.isClickable()){
                storage.clickAndWaitForNewWindow();
                UiObject diskProgress = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/disk_info_progress_view"));
                diskProgress.waitUntilGone(10000);

                UiObject toggle = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/toggle_btn"));
                toggle.click();

                settingProgress = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/settings_progress_view"));
                settingProgress.waitUntilGone(10000);

                storage.clickAndWaitForNewWindow();
                diskProgress = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/disk_info_progress_view"));
                diskProgress.waitUntilGone(10000);

                mDevice.pressBack();
                settingProgress = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/settings_progress_view"));
                settingProgress.waitUntilGone(10000);
            }

            UiObject firmware = systemRecycler.getChildByText(new UiSelector().resourceId("android:id/title"), "韌體", true);
            firmware.clickAndWaitForNewWindow();
            sleep(3000);
            UiObject toggle = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/toggle_btn"));
            toggle.click();
            settingProgress = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/settings_progress_view"));
            settingProgress.waitUntilGone(10000);
            firmware.clickAndWaitForNewWindow();
            sleep(3000);
            mDevice.pressBack();
            settingProgress = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/settings_progress_view"));
            settingProgress.waitUntilGone(10000);


            UiObject sleep = systemRecycler.getChildByText(new UiSelector().resourceId("android:id/title"), "休眠排程", true);
            firmware.clickAndWaitForNewWindow();
            sleep(3000);
            toggle = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/toggle_btn"));
            toggle.click();
            settingProgress = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/settings_progress_view"));
            settingProgress.waitUntilGone(10000);
            firmware.clickAndWaitForNewWindow();
            sleep(3000);
            mDevice.pressBack();
            settingProgress = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/settings_progress_view"));
            settingProgress.waitUntilGone(10000);
        }
    }

    @Test
    public void Setting() throws UiObjectNotFoundException {
        openDrawer();
        clickDrawerSetting();

        UiCollection settingFramee = new UiCollection(new UiSelector().resourceId("com.transcend.nas:id/settings_frame"));

        UiObject downloadDest = settingFramee.getChildByText(new UiSelector().resourceId("android:id/title"), "下載位置");
        UiObject downloadPath = downloadDest.getFromParent(new UiSelector().resourceId("android:id/summary"));
        Assert.assertEquals(true, downloadPath.getText().contains("StoreJet Cloud/Downloads"));
        downloadDest.clickAndWaitForNewWindow();

        String title = getBrowseTitle();
        if (title != null){
            if (title.equals("Storage")) {
                //檢查是否有進到下載位置的選擇頁面，有SD卡的裝置不適用
                UiObject actionBtn = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/action_confirm"));
                actionBtn.waitForExists(3000);
            }
        }
        //離開下載路徑選擇
        UiObject toggleBtn = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/toggle_btn"));
        toggleBtn.click();

        //檢查清除Cache結果
        UiObject cache = settingFramee.getChildByText(new UiSelector().resourceId("android:id/title"), "快取");
        cache.clickAndWaitForNewWindow();
        UiObject alertTitle = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/alertTitle"));
        alertTitle.waitForExists(3000);
        if (alertTitle.exists()){
            UiObject confirm = new UiObject(new UiSelector().resourceId("android:id/button1"));
            confirm.click();
            sleep(500);
            UiObject used = cache.getFromParent(new UiSelector().resourceId("android:id/summary"));
            Assert.assertEquals(true, used.getText().equals("已使用: 0 KB"));
        }

        //檢查清除Cache結果
        UiObject about = settingFramee.getChildByText(new UiSelector().resourceId("android:id/title"), "關於");
        about.clickAndWaitForNewWindow();
        toggleBtn = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/toggle_btn"));

        UiCollection list = new UiCollection(new UiSelector().resourceId("android:id/list"));
        list.waitForExists(3000);
        if (list.exists()){
            UiObject openSource = list.getChildByInstance(new UiSelector().className("android.widget.LinearLayout"), 1);
            UiObject eula = list.getChildByInstance(new UiSelector().className("android.widget.LinearLayout"), 2);

            openSource.click();
            UiObject progress = new UiObject(new UiSelector().resourceId("progress_view"));
            progress.waitUntilGone(5000);
            toggleBtn = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/toggle_btn"));
            toggleBtn.click();
            sleep(500);
            openSource.click();
            progress = new UiObject(new UiSelector().resourceId("progress_view"));
            progress.waitUntilGone(5000);
            mDevice.pressBack();
            sleep(1000);

            eula.click();
            progress = new UiObject(new UiSelector().resourceId("progress_view"));
            progress.waitUntilGone(5000);
            toggleBtn = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/toggle_btn"));
            toggleBtn.click();
            sleep(500);
            eula.click();
            progress = new UiObject(new UiSelector().resourceId("progress_view"));
            progress.waitUntilGone(5000);
            mDevice.pressBack();
        }
        mDevice.pressBack();
        about.clickAndWaitForNewWindow();
        toggleBtn = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/toggle_btn"));
        toggleBtn.click();
    }

    @Test
    public void Recent() throws UiObjectNotFoundException {
        openDrawer();
        clickDrawerRecent();

        UiScrollable recycler = getRecentRecyclerView();
        while (getItemCount(recycler) != 0) {
            //清除最近內容，避免干擾
            openMenu();
            UiCollection menuList = getMenuList();
            UiObject clear = getMenuItem(menuList, 1);
            clear.click();
            UiObject confirm = new UiObject(new UiSelector().resourceId("android:id/button1"));
            confirm.click();
            sleep(500);
            waitForProgress();
        }

        openDrawer();
        clickDrawerBrowser();
        boolean isEnterTestFolder = enterTestFolder(Test_Folder_Public);
        if (isEnterTestFolder){
            recycler = getMainRecyclerView();
            UiObject imageTest = getItem(recycler, imageTestFolder);
            imageTest.click();
            waitForProgress();

            if (isGridView())
                changeBrowserViewType();

            recycler = getMainRecyclerView();
            if (getItemCount(recycler) > 0){
                String itemTitle = getListItemTitle(recycler, 0).getText();
                UiObject item = getItem(recycler, 0);
                item.click();
                sleep(1000);
                DeleteItemInImageView();
                mDevice.pressBack();
                waitForProgress();

                openDrawer();
                clickDrawerRecent();
                UiScrollable recentRecycler = getRecentRecyclerView();
                if (getItemCount(recentRecycler) > 0){
                    Assert.assertEquals(null, getItem(recentRecycler, itemTitle));
                }
            }
        }
    }
}
