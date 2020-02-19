package com.elab.yourvoice;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class tabsaccesseradapter extends FragmentPagerAdapter {
    public tabsaccesseradapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {

            case 0:
                chatsfragment chatsfragment = new chatsfragment();
                return chatsfragment;
            case 1:
                   groupsfragment groupsfragment=new groupsfragment();
                   return groupsfragment;
            case 2:
                contactsfragment contactsfragment=new contactsfragment();
                return contactsfragment;
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {

            case 0:
                return "Chats";
            case 1:
                return "Groups";
            case 2:
                return "Contacts";
            default:
                return null;

        }

    }
}
