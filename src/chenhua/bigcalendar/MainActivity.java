package chenhua.bigcalendar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		show();
	}
	
	@Override
	protected void onResume() {
	    super.onResume();

	    show();
	}

	void show() {
		TextView txtDate = (TextView)findViewById(R.id.txtDate);
		TextView txtWeek = (TextView)findViewById(R.id.txtWeek);
		TextView txtTime = (TextView)findViewById(R.id.txtTime);
		TextView txtLunar = (TextView)findViewById(R.id.txtLunar);
		
		Calendar now = Calendar.getInstance();
		txtDate.setText(now.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + now.get(Calendar.DAY_OF_MONTH) + "»’");
		txtWeek.setText(now.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
		txtTime.setText(new SimpleDateFormat("HH:mm").format(new Date()));
		Lunar lunar = new Lunar();
		lunar.computeLunarFields();
		lunar.computeSolarTerms();
		txtLunar.setText(lunar.getLunarDate());
	}
}
