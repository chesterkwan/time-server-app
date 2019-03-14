package app.timeserver.helper.preferences;

import android.content.Context;

import app.timeserver.repository.location.converters.CoordinateConverter;

public class LocationCoordinateTypeStore extends StringPreferenceStore {
    public String getKey() {
        return "LOCATION_COORDINATE_TYPE";
    }

    @Override
    public String getDefault() {
        return "WGS84 (Lat/Long)";
    }

    public CoordinateConverter getConverter(Context context) {
        return CoordinateConverter.byName(this.get(context));
    }
}
