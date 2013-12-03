package yamba3;

import com.example.yamba3.R;
import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;


public class PrefsActivity extends Activity {  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        // TODO Auto-generated method stub  
        super.onCreate(savedInstanceState);  
        getFragmentManager().beginTransaction().replace(android.R.id.content, new PrefsFragement()).commit();  
    }  
      
      
    public static class PrefsFragement extends PreferenceFragment{  
        @Override  
        public void onCreate(Bundle savedInstanceState) {  
            // TODO Auto-generated method stub  
            super.onCreate(savedInstanceState);  
            addPreferencesFromResource(R.xml.prefs);  
        }  
    }  
}  