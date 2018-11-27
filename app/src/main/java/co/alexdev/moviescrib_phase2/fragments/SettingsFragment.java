package co.alexdev.moviescrib_phase2.fragments;

        import android.os.Bundle;
        import android.support.v7.preference.PreferenceFragmentCompat;

        import co.alexdev.moviescrib_phase2.R;

/*Fragment used to populate the Settings Activity*/
public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.pref_movies_settings);
    }
}
