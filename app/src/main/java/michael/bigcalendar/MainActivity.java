package michael.bigcalendar;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {

	TextView txtTime;
	TextView txtWeek;
	TextView txtDate;
	TextView txtLunar;

	Handler handler = new Handler();

	Runnable runnable = () -> {
		show();
		handler.postDelayed(this.runnable, 1000);
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		txtTime = findViewById(R.id.txtTime);
		txtWeek = findViewById(R.id.txtWeek);
		txtDate = findViewById(R.id.txtDate);
		txtLunar = findViewById(R.id.txtLunar);
	}

	@Override
	protected void onResume() {
		super.onResume();

		runnable.run();
	}

	@Override
	protected void onPause() {
		super.onPause();

		handler.removeCallbacks(runnable);
	}

	void show() {
		Calendar now = Calendar.getInstance();
		txtTime.setText(new SimpleDateFormat("HH:mm:ss").format(new Date()));
		txtWeek.setText(now.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
		txtDate.setText(now.get(Calendar.MONTH) + 1 + " 月 " + now.get(Calendar.DAY_OF_MONTH) + " 日");
		txtLunar.setText(new Lunar(now).getLunarDate());
	}
}
