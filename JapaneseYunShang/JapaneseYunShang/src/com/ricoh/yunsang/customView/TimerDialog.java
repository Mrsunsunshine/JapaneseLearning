package com.ricoh.yunsang.customView;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;

import com.ricoh.R;

public class TimerDialog extends Dialog {

	private Context context;

	SharedPreferences sp;
	Editor editor;
	private NumberPicker np1;
	Button ok;
	int currtime;

	public int getCurrtime() {
		return currtime;
	}

	public void setCurrtime(int currtime) {
		this.currtime = currtime;
	}

	public TimerDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO �Զ����ɵĹ��캯�����
	}

	public TimerDialog(Context context) {
		super(context);

		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO �Զ����ɵķ������
		super.onCreate(savedInstanceState);
		setContentView(R.layout.timer_dialog);
		np1 = (NumberPicker) findViewById(R.id.numberPicker1);
		sp = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
		editor = sp.edit();
		currtime = sp.getInt("currtime", 20);

		// ����np1����Сֵ�����ֵ
		np1.setMinValue(10);
		np1.setMaxValue(30);
		// ����np1�ĵ�ǰֵ

		np1.setValue(currtime);

		np1.setOnValueChangedListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
					int newVal) {
				// TODO Auto-generated method stub
				currtime = newVal;
				
			}

		});

		ok = (Button) findViewById(R.id.buttonok);
		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				editor.putInt("currtime", currtime);
				editor.commit();
				TimerDialog.this.cancel();

			}
		});

	}

}
