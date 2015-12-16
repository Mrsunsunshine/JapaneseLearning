package com.ricoh.yunsang.login;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ricoh.R;
import com.ricoh.japanesestudy.ChooseNewCourceActivity;

/**
 * @author tan
 * @see ����ļ��ܻ�ûʵ��
 * @see ��ǰ����Ϊ���û� ����Ҫ���Ӷ����û���sharedpreference ��
 */
public class RegisterActivity extends Activity
{

	private SharedPreferences sp;
	private EditText loginMail;
	private EditText password;
	private EditText confirmPassword;
	public String username;
	private Calendar c;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);

		setContentView(R.layout.register_activity);

		// ��¼����
		loginMail = (EditText) findViewById(R.id.editText1);
		password = (EditText) findViewById(R.id.editText2);
		confirmPassword = (EditText) findViewById(R.id.editText3);
		Button regist = (Button) findViewById(R.id.button1);
		regist.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				if (checkRegistMessage() == true)
				{
					String username = loginMail.getText().toString();
					String userpassword = password.getText().toString();

					// ���SharedPreferencesʵ������

					sp = RegisterActivity.this.getSharedPreferences("thefirsttime", Context.MODE_PRIVATE);
					Editor editor = sp.edit();
					editor.putString("USER_NAME", username);
					editor.putString("PASSWORD", userpassword);
					editor.putString("COURSE", "@@");

					editor.putBoolean("SEVEN_SELECTED", true);

					editor.commit();
					// ��ʾע��ɹ�
					username = sp.getString("USER_NAME", "");

					Toast.makeText(RegisterActivity.this, "ע��ɹ�", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();

					c = Calendar.getInstance();
					intent.putExtra("username", username);
					intent.putExtra("startYear", c.get(Calendar.YEAR));
					intent.putExtra("startMonth", c.get(Calendar.MONTH));
					intent.putExtra("startDay", c.get(Calendar.DAY_OF_MONTH));
					intent.putExtra("first_register", 1);
					intent.putExtra("login_days", 1);
					

					intent.setClass(RegisterActivity.this, ChooseNewCourceActivity.class);
					startActivity(intent);

				}

			}

			private Boolean checkRegistMessage()
			{
				String mail = loginMail.getText().toString();
				// ����Ƿ��������ʽ
				Boolean mailflag;
				String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
				Pattern regex = Pattern.compile(check);
				Matcher matcher = regex.matcher(mail);
				mailflag = matcher.matches();
				if (mailflag == false)
				{
					Toast.makeText(RegisterActivity.this, "������ȷ�������ʽע��", Toast.LENGTH_SHORT).show();
					return false;
				}
				// ��������Ƿ����
				Boolean exist;
				// ��Ҫ������������������û����Ƿ���ڣ���ûʵ�֣�

				// ������볤��
				Boolean passwordflag;
				String reg = "[0-9]*";
				Pattern regex2 = Pattern.compile(reg);
				Matcher matcher2 = regex2.matcher(password.getText().toString());
				passwordflag = matcher2.matches();
				if (passwordflag == false)
				{
					Toast.makeText(RegisterActivity.this, "�����벻����Ҫ��", Toast.LENGTH_SHORT).show();
					return false;

				}

				// ������������Ƿ�һ��
				if (password.getText().toString().equals(confirmPassword.getText().toString()))
				{
					return true;
				} else
				{
					Toast.makeText(RegisterActivity.this, "�����������벻һ��", Toast.LENGTH_SHORT).show();
					return false;
				}

			}
		});

	}

}
