package pt.alexandre.norte;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Process;
import android.provider.Settings;
import android.util.Base64;
import com.getcapacitor.JSArray;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@CapacitorPlugin(name = "Apps")
public class AppsPlugin extends Plugin {

    private String icon(Drawable d) {
        int s = 96;
        Bitmap b = Bitmap.createBitmap(s, s, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        d.setBounds(0, 0, s, s);
        d.draw(c);
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.PNG, 100, o);
        return "data:image/png;base64," + Base64.encodeToString(o.toByteArray(), Base64.NO_WRAP);
    }

    private boolean access() {
        Context ctx = getContext();
        AppOpsManager a = (AppOpsManager) ctx.getSystemService(Context.APP_OPS_SERVICE);
        int m = a.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, Process.myUid(), ctx.getPackageName());
        return m == AppOpsManager.MODE_ALLOWED;
    }

    private void open(String action) {
        Intent i = new Intent(action);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            if (getActivity() != null) getActivity().startActivity(i);
            else getContext().startActivity(i);
        } catch (Exception e) {
            Intent d = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            d.setData(android.net.Uri.parse("package:" + getContext().getPackageName()));
            d.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(d);
        }
    }

    @PluginMethod
    public void list(PluginCall call) {
        try {
            PackageManager pm = getContext().getPackageManager();
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> l = pm.queryIntentActivities(i, 0);
            JSArray arr = new JSArray();
            Set<String> seen = new HashSet<>();
            for (ResolveInfo r : l) {
                String p = r.activityInfo.packageName;
                if (!seen.add(p)) continue;
                JSObject o = new JSObject();
                o.put("pkg", p);
                o.put("label", r.loadLabel(pm).toString());
                o.put("icon", icon(r.loadIcon(pm)));
                arr.put(o);
            }
            JSObject res = new JSObject();
            res.put("apps", arr);
            call.resolve(res);
        } catch (Exception e) {
            call.reject(String.valueOf(e.getMessage()));
        }
    }

    @PluginMethod
    public void hasAccess(PluginCall call) {
        JSObject o = new JSObject();
        o.put("value", access());
        call.resolve(o);
    }

    @PluginMethod
    public void usage(PluginCall call) {
        try {
            UsageStatsManager u = (UsageStatsManager) getContext().getSystemService(Context.USAGE_STATS_SERVICE);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Map<String, UsageStats> m = u.queryAndAggregateUsageStats(cal.getTimeInMillis(), System.currentTimeMillis());
            JSObject o = new JSObject();
            for (Map.Entry<String, UsageStats> e : m.entrySet()) {
                long mins = e.getValue().getTotalTimeInForeground() / 60000L;
                if (mins > 0) o.put(e.getKey(), mins);
            }
            JSObject r = new JSObject();
            r.put("usage", o);
            call.resolve(r);
        } catch (Exception e) {
            call.reject(String.valueOf(e.getMessage()));
        }
    }

    @PluginMethod
    public void launch(PluginCall call) {
        Intent i = getContext().getPackageManager().getLaunchIntentForPackage(call.getString("pkg"));
        if (i == null) {
            call.reject("sem intent");
            return;
        }
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getContext().startActivity(i);
        call.resolve();
    }

    @PluginMethod
    public void openUsage(PluginCall call) {
        open(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        call.resolve();
    }

    @PluginMethod
    public void openHome(PluginCall call) {
        open(Settings.ACTION_HOME_SETTINGS);
        call.resolve();
    }
}
