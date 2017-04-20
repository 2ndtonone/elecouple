package com.sulong.elecouple.entity;

import java.util.ArrayList;

/**
 * Created by ydh on 2017/4/18.
 */

public class CommonSwitchDialogEntity {

    public Data data;

    public class Data {
        public String name;
        public String id;
        public ArrayList<InnerList> inner_list;
    }

    public class InnerList {
        public String name;
        public String id;
        public ArrayList<InestList> InestList;
    }

    public class InestList {
        public String name;
        public String id;
    }
}
