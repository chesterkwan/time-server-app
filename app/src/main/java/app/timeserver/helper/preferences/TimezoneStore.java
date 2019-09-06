package app.timeserver.helper.preferences;

import android.content.Context;
import android.icu.util.TimeZone;

import app.timeserver.helper.DateFormatter;
import app.timeserver.helper.LocaleHelper;

import java.util.Date;
import java.util.Locale;

public class TimezoneStore extends StringPreferenceStore {
    @Override
    public String getKey() {
        return "TIMEZONE";
    }

    @Override
    public String getDefault() {
        return "UTC";
    }

    @Override
    public void set(Context context, String value) {
        super.set(context, value);
        DateFormatter.setTimezonePreference(value);
    }

    public String getTimeZoneShortName(Context context) {
        return getTimeZoneShortName(context, new TimezoneStore().get(context));
    }

    public String getTimeZoneShortName(Context context, String zone) {
        if (zone.equals("UTC")) {
            return zone;
        }else if (zone.equals("Decimal Time")) {
            return "decMs";
        }else if (zone.equals("Swatch Internet Time")) {
            return ".beats";
        } else {
            Locale locale = LocaleHelper.getUserLocale(context);
            TimeZone tz = TimeZone.getDefault();
            boolean dstActive = tz.inDaylightTime(new Date());
            return tz.getDisplayName(dstActive, TimeZone.SHORT, locale);
        }
    }
}
