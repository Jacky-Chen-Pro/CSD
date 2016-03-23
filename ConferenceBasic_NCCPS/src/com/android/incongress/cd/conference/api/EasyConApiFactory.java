package com.android.incongress.cd.conference.api;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.base.Constants;
import com.caucho.hessian.client.HessianProxyFactory;

public class EasyConApiFactory {
    private static EasyConApiFactory instance = null;

    private ConferencesAPI api = null;

    static public EasyConApiFactory instance() {
        if(null == instance) {
            instance = new EasyConApiFactory();
        }
        return instance;
    }

    public EasyConApiFactory() { 
        HessianProxyFactory factory = new HessianProxyFactory();
        factory.setDebug(false);
        factory.setChunkedPost(false);
        //factory.setReadTimeout(500000);
        factory.setHessian2Reply(false);

        try {
            api = (ConferencesAPI) factory.create(ConferencesAPI.class, Constants.get_HOST(), AppApplication.instance().getClassLoader());
        } catch (Exception e) {
            System.out.println("occur exception: " + e);
        }
    }

    public ConferencesAPI getApi() {
        return (null == api) ? null : api;
    }

}
