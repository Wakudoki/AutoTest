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
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DriveProBody2052_BasicTest_Browser extends DriveProBodyAbstract{

    static int Phone = 0;
    static int Pad = 1;
    int Testing_device = Phone;

    private static int max_execute_time = 4;

    @Before
    public void setup() throws UiObjectNotFoundException {
        super.setup();
    }

    //TODO 1. none
    @Test
    public void First_None() throws RemoteException, UiObjectNotFoundException {
        UiScrollable recyclerView = getMainRecyclerView();

        //檢查排版
        checkComposing(recyclerView, true);

        if (getItemCount(recyclerView) > 0){
            //有檔案存在時，無檔案的圖示不能存在
            UiObject noItemViewObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/empty_view"));
            Assert.assertEquals(false, noItemViewObj.exists());

            recyclerView.scrollToBeginning(5);

            //檢查長寬，理論上長寬差不能超過一定量
            UiObject item = getItem(recyclerView, 0);
            Assert.assertEquals(true, Math.abs(item.getBounds().width() - item.getBounds().height()) < 30);

            //檢查相對位置
            UiObject item1 = getItem(recyclerView, 1);
            Assert.assertEquals(item.getBounds().centerY(), item1.getBounds().centerY());
        }
        else{
            //無檔案存在時，無檔案的圖示必須存在
            UiObject noItemViewObj = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/empty_view"));
            Assert.assertEquals(true, noItemViewObj.exists());
        }

        UiCollection tabLayout = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/tabLayout"));
        Assert.assertEquals(true, tabLayout.exists());
    }

    //TODO 2. switch&back
    @Test
    public void Second_SwitchAndBack() throws UiObjectNotFoundException, RemoteException {
        //進入首頁後回到browser，並檢查排版
        openDrawer();
        clickDrawerHome();
        openDrawer();
        clickDrawerBrowser();
        checkComposing(getMainRecyclerView(), true);

        //進入即時影像後回到browser，並檢查排版
        openDrawer();
        clickDrawerLiveView();
        openDrawer();
        clickDrawerBrowser();
        checkComposing(getMainRecyclerView(), true);

        //進入我的儲存後回到browser，並檢查排版
        openDrawer();
        clickDrawerMyStorage();
        checkComposing(getMainRecyclerView(), true);
        openDrawer();
        clickDrawerBrowser();
        checkComposing(getMainRecyclerView(), true);
    }

    //TODO 3. rotate
    @Test
    public void Third_Rotate() throws UiObjectNotFoundException, RemoteException {
        for (int i = 0; i < max_execute_time; i++){
            checkComposing(getMainRecyclerView(), true);
            sleep(1000);
        }
    }

    //TODO 4. tab
    @Test
    public void Fourth_TabCell() throws UiObjectNotFoundException {
        UiScrollable recyclerView;
        int swipeCount = 0;
        while(swipeCount < max_execute_time) {
            recyclerView = getMainRecyclerView();
            for (int i = 0; i < getItemCount(recyclerView) - 3; i++) {
                UiObject item = getItem(recyclerView, i);
                String cellTitle = getItemTitle(recyclerView, i).getText();
                item.click();
                waitForDialog();
                sleep(500);

                String singleViewTitle = new UiObject(new UiSelector().resourceId("com.transcend.bcr:id/video_toolbar_title")).getText();
                String [] titleSplit = singleViewTitle.split("\n");

                Assert.assertEquals(cellTitle, titleSplit[0]);

                mDevice.pressBack();
                waitForDialog();
            }

            recyclerView.scrollToEnd(1);
            swipeCount ++;
        }
    }

    //TODO 取得物件
    public UiScrollable getMainRecyclerView(){
        return new UiScrollable(new UiSelector().resourceId("com.transcend.bcr:id/recycler_view"));
    }

    public int getItemCount(UiScrollable recycler){
        return recycler.getChildCount(new UiSelector().resourceId("com.transcend.bcr:id/item_manage"));
    }

    public UiObject getItem(UiScrollable recycler, int instance) throws UiObjectNotFoundException {
        return recycler.getChildByInstance(new UiSelector().resourceId("com.transcend.bcr:id/item_manage"), instance);
    }

    public UiObject getItemTitle(UiScrollable recycler, int instance) throws UiObjectNotFoundException {
        return recycler.getChildByInstance(new UiSelector().resourceId("com.transcend.bcr:id/item_title"), instance);
    }

    public int getImageInViewPagerCount(){
        UiCollection viewPager = new UiCollection(new UiSelector().resourceId("com.transcend.bcr:id/photo_view_pager"));
        return viewPager.getChildCount(new UiSelector().className("com.transcend.bcr:id/imageView"));
    }
}
