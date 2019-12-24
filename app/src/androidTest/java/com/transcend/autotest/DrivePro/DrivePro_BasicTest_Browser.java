package com.transcend.autotest.DrivePro;

import android.os.RemoteException;
import android.support.test.uiautomator.UiCollection;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DrivePro_BasicTest_Browser extends DriveProAbstract{

    @Before
    public void setup(){
        super.setup();
    }

    //1. None
    @Test
    public void First_None() throws UiObjectNotFoundException {
        UiScrollable recyclerView = getMainRecyclerView();

        int item_count = getItemCount(recyclerView);

        if (item_count > 0){
            //有檔案存在時，無檔案的圖示不能存在
            UiObject noItemViewObj = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/empty_view"));
            Assert.assertEquals(false, noItemViewObj.exists());

            recyclerView.scrollToBeginning(5);

            if (item_count > 1) {
                //檢查相對位置
                UiObject item = getItem(recyclerView, 0);
                UiObject item1 = getItem(recyclerView, 1);
                Assert.assertEquals(item.getBounds().centerX(), item1.getBounds().centerX());
            }
        }
        else{
            //無檔案存在時，無檔案的圖示必須存在
            UiObject noItemViewObj = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/empty_view"));
            Assert.assertEquals(true, noItemViewObj.exists());
        }

        UiCollection tabLayout = new UiCollection(new UiSelector().resourceId("com.transcend.cvr:id/tabLayout"));
        Assert.assertEquals(true, tabLayout.exists());
    }

    //TODO 2. switch&back
    @Test
    public void Second_SwitchAndBack() throws UiObjectNotFoundException, RemoteException {
        //進入首頁後回到browser，並檢查排版
        openDrawer();
        clickDrawerHome();
        openDrawer();
        clickDrawerRemote();

        //進入即時影像後回到browser，並檢查排版
        openDrawer();
        clickDrawerLiveView();
        openDrawer();
        clickDrawerRemote();

        //進入我的儲存後回到browser，並檢查排版
        openDrawer();
        clickDrawerMyStorage();

        openDrawer();
        clickDrawerRemote();
    }

    //TODO 4. tab
    @Test
    public void Fourth_TabCell() throws UiObjectNotFoundException {
        UiScrollable recyclerView = getMainRecyclerView();
        for (int i = 0; i < getItemCount(recyclerView) - 3; i++) {
            UiObject item = getItem(recyclerView, i);
            String cellTitle = getItemTitle(recyclerView, i).getText();
            item.click();
            waitForDialog();
            sleep(500);

            String singleViewTitle = new UiObject(new UiSelector().resourceId("com.transcend.cvr:id/toolbar_title")).getText();
            String [] titleSplit = singleViewTitle.split("\n");

            Assert.assertEquals(cellTitle, titleSplit[0]);

            mDevice.pressBack();
            waitForDialog();
        }
    }
}
