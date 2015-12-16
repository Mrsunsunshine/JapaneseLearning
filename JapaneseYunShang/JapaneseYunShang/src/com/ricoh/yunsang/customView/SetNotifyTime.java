package com.ricoh.yunsang.customView;

import java.util.Calendar;
import java.util.TimeZone;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

public class SetNotifyTime extends Service
{

	private int mMinute = 0;
	private int mHour = 0;

	@Override
	public IBinder onBind(Intent arg0)
	{

		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Intent intent2 = new Intent(SetNotifyTime.this, NotifyReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(SetNotifyTime.this, 0, intent2, 0);
		SharedPreferences sp = getSharedPreferences("dialogsettings", MODE_APPEND);
		String setTime = sp.getString("nownotifytime", "00:00");
		if (setTime.equals("----:----"))
		{

		} else
		{
			String[] str = setTime.split(":");
			mHour = Integer.parseInt(str[0]);
			mMinute = Integer.parseInt(str[1]);

			long firstTime = SystemClock.elapsedRealtime(); // ����֮�����ڵ�����ʱ��(����˯��ʱ��)

			Calendar calendar = Calendar.getInstance();

			calendar.setTimeZone(TimeZone.getTimeZone("GMT+8")); // ����ʱ����Ҫ����һ�£���Ȼ����8��Сʱ��ʱ���
			calendar.set(Calendar.MINUTE, mMinute);
			calendar.set(Calendar.HOUR_OF_DAY, mHour);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);

			// ѡ���ÿ�춨ʱʱ��
			long selectTime = calendar.getTimeInMillis();
			long systemTime = System.currentTimeMillis();
			// �����ǰʱ��������õ�ʱ�䣬��ô�ʹӵڶ�����趨ʱ�俪ʼ
			if (systemTime > selectTime)
			{
				Toast.makeText(SetNotifyTime.this, "���õ�ʱ��С�ڵ�ǰʱ�䣬����������㱳���ʵ�Ӵ", Toast.LENGTH_SHORT).show();
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				selectTime = calendar.getTimeInMillis();
			}

			// ��������ʱ�䵽�趨ʱ���ʱ���
			long time = selectTime - systemTime;
			firstTime += time;

			// ��������ע��
			AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);

			manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), sender);
			Log.i("tan", "time ==== " + time + ", selectTime ===== " + selectTime + ", systemTime ==== " + systemTime + ", firstTime === " + firstTime);

			Toast.makeText(SetNotifyTime.this, "���ñ��������ѳɹ�! ", Toast.LENGTH_LONG).show();
		}

		return super.onStartCommand(intent, flags, startId);

	}

	public void setNotificationtime()
	{

	}

}
