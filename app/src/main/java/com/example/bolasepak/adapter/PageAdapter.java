package com.example.bolasepak.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.bolasepak.fragment.Sebelum;
import com.example.bolasepak.fragment.Sesudah;

public class PageAdapter extends FragmentPagerAdapter
{
    private int numoftabs ;
    private String idTeam ;

    public PageAdapter(FragmentManager fm,int numoftabs,String idTeam) {
        super(fm);
        this.numoftabs = numoftabs ;
        this.idTeam = idTeam ;
    }

    @NonNull
    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0 :
                Sesudah tabSekarangFragment = new Sesudah() ;
                tabSekarangFragment.idTeam = idTeam ;
                return tabSekarangFragment ;
            case 1 :
                Sebelum tabSebelumFragment = new Sebelum() ;
                tabSebelumFragment.idTeam = idTeam ;
                return tabSebelumFragment ;
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
        return POSITION_NONE ;
    }

}