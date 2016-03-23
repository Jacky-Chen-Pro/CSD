package com.android.incongress.cd.conference.data;

import java.util.ArrayList;
import java.util.List;

import android.util.Pair;

public class DbBean {
    protected List<Pair<String, String>> container;

    public DbBean() {
        container = new ArrayList<Pair<String, String>>();
    }

    public List<Pair<String, String>> getContainer() {
        return container;
    }

    public Pair<String, String> primaryColumn;

}
