package com.transcend.autotest.StoreJetCloud;

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
public class StoreJetCloud_BasicTest_Browser extends StoreJetCloudAbstract{

    @Before
    public void setup() throws RemoteException, UiObjectNotFoundException {
        super.setup();
    }

    //TODO 1. none
    @Test
    public void First_None() throws UiObjectNotFoundException, RemoteException {
        if (!isGridView())
            changeBrowserViewType();

        //檢查排版
        checkComposing(getMainRecyclerView(), true);

        UiScrollable recyclerView = getMainRecyclerView();
        if (getItemCount(recyclerView) > 0){
            //有檔案存在時，無檔案的圖示不能存在
            UiObject noItemViewObj = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/empty_view"));
            Assert.assertEquals(false, noItemViewObj.exists());

            //檢查長寬，理論上長寬差不能超過一定量
            UiObject item = getItem(recyclerView, 0);
            Assert.assertEquals(true, Math.abs(item.getBounds().width() - item.getBounds().height()) < 30);

            //檢查相對位置
            UiObject item1 = getItem(recyclerView, 1);
            if (item1.exists()){
                if (isGridView()){
                    Assert.assertEquals(item.getBounds().centerY(), item1.getBounds().centerY());
                }
                else {
                    Assert.assertEquals(item.getBounds().centerX(), item1.getBounds().centerX());
                }
            }
        }
        else{
            //無檔案存在時，無檔案的圖示必須存在
            UiObject noItemViewObj = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/empty_view"));
            Assert.assertEquals(true, noItemViewObj.exists());
        }

        UiCollection tabLayout = new UiCollection(new UiSelector().resourceId("com.transcend.nas:id/tabLayout"));
        Assert.assertEquals(true, tabLayout.exists());
    }

    //TODO 2. switch&back
    @Test
    public void Second_SwitchAndBack() throws UiObjectNotFoundException, RemoteException {
        //進入下載後回到browser，並檢查排版
        openDrawer();
        clickDrawerDownload();
        openDrawer();
        clickDrawerBrowser();
        checkComposing(getMainRecyclerView(), true);

        //進入最近後回到browser，並檢查排版
        openDrawer();
        clickDrawerRecent();
        openDrawer();
        clickDrawerBrowser();
        checkComposing(getMainRecyclerView(), true);

        //進入device後回到browser，並檢查排版
        openDrawer();
        clickDrawerDevice();
        openDrawer();
        clickDrawerBrowser();
        checkComposing(getMainRecyclerView(), true);

        UiScrollable recyclerView = getMainRecyclerView();
        UiObject publicObj = getItem(recyclerView, "Public");
        publicObj.click();
        waitForProgress();

        recyclerView = getMainRecyclerView();
        UiObject mikeObj = getItem(recyclerView, "Mike");
        mikeObj.click();
        waitForProgress();
    }

    //TODO 3. rotate
    @Test
    public void Third_Rotate() throws UiObjectNotFoundException, RemoteException {
        if (!isGridView())
            changeBrowserViewType();

        for (int i = 0; i < action_execute_times; i++){
            checkComposing(getMainRecyclerView(), true);
            sleep(1000);
        }
    }

    //TODO 4. tab
    @Test
    public void Fourth_TabCell() throws UiObjectNotFoundException {
        if (isGridView())
            changeBrowserViewType();

        UiScrollable recyclerView;
        int swipeCount = 0;
        while(swipeCount < action_execute_times){
            recyclerView = getMainRecyclerView();
            for (int i = 0; i < getItemCount(recyclerView) - 1; i++){
                UiObject item = getListItem(recyclerView, i);
                String title = item.getText();
                String [] titleSplit = title.split("\\.");
                if (isVideo(titleSplit[titleSplit.length - 1]))
                    break;
                item.click();
                waitForProgress();
                sleep(500);

                UiObject dropdownText = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/dropdown_text"));
                UiObject singleViewTitle = new UiObject(new UiSelector().resourceId("com.transcend.nas:id/toolbar_title"));
                if (dropdownText.exists()){
                    Assert.assertEquals(title, dropdownText.getText().toString());
                }
                else if (singleViewTitle.exists()){
                    Assert.assertEquals(title, singleViewTitle.getText().toString());
                }

                mDevice.pressBack();
            }

            recyclerView.scrollToEnd(1);
            swipeCount++;
        }
    }
}
