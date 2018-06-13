package com.example.shams.bakingapplication.testWidget;

import android.content.Intent;
import android.widget.RemoteViewsService;


public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new WidgetRemoteFactory(intent, getApplicationContext());
    }

}
