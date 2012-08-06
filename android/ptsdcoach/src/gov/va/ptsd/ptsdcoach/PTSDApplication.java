package gov.va.ptsd.ptsdcoach;

import android.app.Application;

import org.ohmage.probemanager.ProbeWriter;

public class PTSDApplication extends Application {

    private static PTSDApplication self;
    private static ProbeWriter mProbeWriter;

    @Override
    public void onCreate() {
        super.onCreate();
        self = this;
        mProbeWriter = new ProbeWriter(this);
    }
    
    public static ProbeWriter getProbeWriter() {
        return mProbeWriter;
    }
}