package com.finsyswork.erp;

import android.widget.Filter;

import java.util.ArrayList;

public class filterteam extends Filter {
    String srchar="";


    public filterteam(ArrayList<team> fed, String srchtxt)
    {
        fgen.feedListmain=fed;
        srchar=srchtxt;


    }
    @Override
    protected Filter.FilterResults performFiltering(CharSequence constraint) {
        // TODO Auto-generated method stub

        Filter.FilterResults Result = new Filter.FilterResults();
        if(fgen.feedListmain==null) {fgen.feedListmain=new ArrayList<team>();}
        // if constraint is empty return the original names
        if (constraint.length() == 0) {
            Result.values = fgen.feedListmain;
            Result.count = fgen.feedListmain.size();
            fgen.feedListresult=fgen.feedListmain;
            return Result;
        }

        ArrayList<team> Filtered_Names = new ArrayList<>();
        String filterString = srchar.toLowerCase();
        team filterableString;


        for (int i = 0; i < fgen.feedListmain.size(); i++) {
            filterableString = fgen.feedListmain.get(i);
            String temp = filterString.toLowerCase().trim();
            if (filterableString.getcol1().toString().trim().toLowerCase().contains(temp) ||
                    filterableString.getcol2().toString().trim().toLowerCase().contains(temp) ||
                    filterableString.getcol3().toString().trim().toLowerCase().contains(temp) ||
                    filterableString.getcol4().toString().trim().toLowerCase().contains(temp) ||
                    filterableString.getcol5().toString().trim().toLowerCase().contains(temp)) {
                Filtered_Names.add(filterableString);
            }
        }
        Result.values = Filtered_Names;
        Result.count = Filtered_Names.size();
        fgen.feedListresult = (ArrayList<team>) Result.values;
        return Result;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        // TODO Auto-generated method stub
        fgen.feedListresult = (ArrayList<team>) results.values;

    }

}

