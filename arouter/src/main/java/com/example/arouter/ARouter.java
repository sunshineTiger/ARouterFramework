package com.example.arouter;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dalvik.system.DexFile;

public class ARouter {
    private Map<String, Class<? extends Activity>> activityList;
    private static volatile ARouter Instance;
    private Context context;

    private ARouter() {
        activityList = new HashMap<>();
    }

    public static ARouter getInstance() {
        if (Instance == null) {
            synchronized (ARouter.class) {
                if (Instance == null) {
                    Instance = new ARouter();
                    return Instance;
                }
            }
        }
        return Instance;
    }

    public void init(Context context) {
        this.context = context;
        List<String> className = getClassName("com.zh.util");
        for (String cla : className) {
            try {
                Class<?> aClass = Class.forName(cla);
                if (IRouter.class.isAssignableFrom(aClass)) {
                    IRouter iRouter = (IRouter) aClass.newInstance();
                    iRouter.putActivity();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void putActivity(String path, Class<? extends Activity> clazz){
        if (path.isEmpty() || null == clazz) {
            return;
        }
        if (!activityList.containsKey(path))
            activityList.put(path, clazz);
    }


    private List<String> getClassName(String packName) {
        List<String> classList = new ArrayList<>();
        String path = null;
        try {
            path = context.getPackageManager().getApplicationInfo(context.getPackageName(), 0).sourceDir;
            DexFile dexFile = new DexFile(path);
            Enumeration<String> entries = dexFile.entries();
            while (entries.hasMoreElements()) {
                String name = entries.nextElement();
                if (name.contains(packName)) {
                    classList.add(name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classList;
    }

}
