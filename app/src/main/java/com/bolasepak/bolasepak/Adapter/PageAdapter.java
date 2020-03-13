package com.bolasepak.bolasepak.Adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bolasepak.bolasepak.Fragment.Sebelum;
import com.bolasepak.bolasepak.Fragment.Sesudah;

public class PageAdapter extends FragmentPagerAdapter
{
    private int numoftabs ;
    String idTeam ;

    public PageAdapter(FragmentManager fm, int numoftabs, String idTeam) {
        super(fm);
        this.numoftabs = numoftabs ;
        this.idTeam = idTeam ;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0 :
                Sesudah sesudah = new Sesudah() ;
                sesudah.idTeam = idTeam ;
                return sesudah;
            case 1 :
                Sebelum sebelum = new Sebelum() ;
                sebelum.idTeam = idTeam ;
                return sebelum;
            default:
                return null ;
        }
    }

    @Override
    public int getCount() {
        return numoftabs ;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
//        return super.getItemPosition(object);
        return POSITION_NONE ;
    }

}
