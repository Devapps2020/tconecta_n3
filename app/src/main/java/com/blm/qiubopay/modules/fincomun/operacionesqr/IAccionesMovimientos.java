package com.blm.qiubopay.modules.fincomun.operacionesqr;

import java.util.List;

public interface IAccionesMovimientos {

    interface GUI{
        public <T> void onItemClick(T itemClicked);
    }

    interface Data{
        public     void removeAllItems();
        public     void removeItem(int position);
        public <T> void addItem(T item);
        public <T> void addList(List<T> items);
    }
}
