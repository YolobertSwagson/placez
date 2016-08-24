package wusadevelopment.albert.com.placez;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class SettingsActivity extends AppCompatActivity {

    private ImageButton SettingsActivityRemoveBtn;
    private RelativeLayout SettingsActivityLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.SettingsActivityRemoveBtn = (ImageButton) findViewById(R.id.SettingsActivityRemoveBtn);
        this.SettingsActivityLayout = (RelativeLayout) findViewById(R.id.SettingsActivityLayout);
        this.SettingsActivityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WarningAlert warningAlert = new WarningAlert();
                Bundle args = new Bundle();
                args.putInt("id", -1);
                warningAlert.setArguments(args);
                warningAlert.show(getSupportFragmentManager(),"warningAlert");
            }
        });
    }
}
