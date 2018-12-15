package azisalvriyanto.uinsunankalijaga;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static azisalvriyanto.uinsunankalijaga.PreferencesUtility.*;

public class SaveSharedPreference {
    static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setLoggedIn(Context context, boolean loggedIn, String nip) {
        SharedPreferences.Editor editor = getPreferences(context).edit();
        editor.putBoolean(LOGGED_IN_PREF, loggedIn);
        editor.putString(NIP_IN_PREF, nip);
        editor.apply();
    }

    public static boolean getLoggedStatus(Context context) {
        return getPreferences(context).getBoolean(LOGGED_IN_PREF, false);
    }

    public static String getNIP(Context context) {
        return getPreferences(context).getString(NIP_IN_PREF, "nip");
    }
}
