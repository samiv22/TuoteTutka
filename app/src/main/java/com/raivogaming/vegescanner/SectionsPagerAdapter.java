package com.raivogaming.vegescanner;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

class SectionsPagerAdapter extends FragmentPagerAdapter{

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch(position){
            case 0:
                return new TuoteinfoFragment();
            case 1:
                return new TuotearvostelutFragment();
            case 2:
                return new TuotereseptitFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    public CharSequence getPageTitle(int position){
        switch (position){
            case 0:
                return "TIETOA";
            case 1:
                return "ARVOSTELUT";
            case 2:
                return "RESEPTIT";
            default:
                return null;
        }
    }
}
