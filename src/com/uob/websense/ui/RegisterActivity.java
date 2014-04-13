package com.uob.websense.ui;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.uob.websense.R;
import com.uob.websense.adapter.SpinnerAdapter;
import com.uob.websense.support.Constants;
import com.uob.websense.support.DeviceUuidFactory;
import com.uob.websense.support.Util;
import com.uob.websense.web_service_manager.WebSenseRestClient;

/**
 * @author karthikeyaudupa
 *
 */

public class RegisterActivity extends FragmentActivity {

	private Spinner userTypeSpinner;
	private Boolean isRegisterView;
	private Button registerBtn = null;
	private Button loginBtn= null;
	private Button loginBtnDmy= null;
	private Button registerBtnDmy = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isRegisterView = true;
		if(Util.checkForLogin(getApplicationContext())==false){
			
			Util.removeSecurePreference(this, Constants.AUTH_KEY_TOKEN);
			setContentView(R.layout.register_view);
			userTypeSpinner = (Spinner) findViewById(R.id.user_type);
			SpinnerAdapter spinerAdapter = new SpinnerAdapter(this,getResources().getStringArray(R.array.user_type_array));
			userTypeSpinner.setAdapter(spinerAdapter);
			Util.overrideFonts(this, findViewById(android.R.id.content));

			registerBtn = (Button)findViewById(R.id.register_action_button);
			loginBtn = (Button)findViewById(R.id.login_action_button);
			registerBtnDmy = (Button)findViewById(R.id.register_action_button_dummy);
			loginBtnDmy = (Button)findViewById(R.id.login_action_button_dummy);
			
			
			(loginBtn).setTypeface( Typeface.createFromAsset(getApplicationContext().getAssets(), Constants.FONT_BOLD));
			(loginBtnDmy).setTypeface( Typeface.createFromAsset(getApplicationContext().getAssets(), Constants.FONT_BOLD));
			(registerBtn).setTypeface( Typeface.createFromAsset(getApplicationContext().getAssets(), Constants.FONT_BOLD));
			(registerBtnDmy).setTypeface( Typeface.createFromAsset(getApplicationContext().getAssets(), Constants.FONT_BOLD));
			
			
		}else{
			navigateToMain();
		}
	}

	public void onBtnClicked(View v){
		Util.hideSoftKeyboard(this);
		if(v.getId() == R.id.login_action_button){

			if(validateLoginInputs()==true){
				postLoginInformation();
			}

		}else if((v.getId() == R.id.login_action_button_dummy) || (v.getId() == R.id.register_action_button_dummy)){
			switchRegistratonOn();
		}else if((v.getId() == R.id.register_action_button)){
			if(validateViewInputs()==true){
				postInformation();
			}
		}
	}

	public boolean validateLoginInputs(){
		TextView emailTxt = (TextView)findViewById(R.id.email_address);
		TextView passwordTxt = (TextView)findViewById(R.id.password);

		if(passwordTxt.getText().toString().trim().length()>0 &&
				Util.isValidEmail(emailTxt.getText())){
			return true;
		}else{
			showAlert(getString(R.string.login_error));
			return false;
		}
	}

	public boolean validateViewInputs() {



		TextView emailTxt = (TextView)findViewById(R.id.email_address);
		TextView passwordTxt = (TextView)findViewById(R.id.password);
		RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radioSex);
		CheckBox acceptCheckBox = (CheckBox)findViewById(R.id.accept_policy_checkbox);

		if(passwordTxt.getText().toString().trim().length()>0 &&
				Util.isValidEmail(emailTxt.getText()) &&
				radioGroup.getCheckedRadioButtonId()>0 &&
				userTypeSpinner.getSelectedItemPosition()!=Spinner.INVALID_POSITION){

			if(acceptCheckBox.isChecked()==false){
				showAlert(getString(R.string.policy_error));
				return false;
			}else{
				return true;
			}

		}else{
			showAlert(getString(R.string.login_error));
			return false;
		}

	}

	public void showAlert(String alertMessage){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(alertMessage);
		builder.setNegativeButton("Close", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

			}
		});
		builder.setCancelable(true);
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void navigateToMain() {
		Intent i  = new Intent(this,MainContainerActivity.class);
		startActivity(i);
		finish();
	}

	public void postInformation() {

		final ProgressDialog progressDialog = ProgressDialog.show(this, "", "Loading...");
		TextView emailTxt = (TextView)findViewById(R.id.email_address);
		TextView passwordTxt = (TextView)findViewById(R.id.password);
		RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radioSex);
		String genderTxt = ((RadioButton)findViewById(radioGroup.getCheckedRadioButtonId())).getText().toString();

		RequestParams params = new RequestParams();
		params.put("username", emailTxt.getText().toString());
		params.put("password", passwordTxt.getText().toString());
		params.put("gender", String.valueOf(genderTxt.equalsIgnoreCase(getString(R.string.male))?1:2));
		params.put("job_type",  String.valueOf(userTypeSpinner.getSelectedItemPosition()));
		params.put("uuid",new DeviceUuidFactory(this).getDeviceUuid().toString());

		WebSenseRestClient.post(Constants.REGISTERATION_METHOD, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {
				handleResponse(responseString);
				progressDialog.dismiss();	
			}
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				progressDialog.dismiss();
				if(statusCode==400){
					showAlert("Invalid request.");
					
				}else if(statusCode == 401){
					showAlert("Email and password already exists.");
				}else{
					showAlert(getApplicationContext().getString(R.string.server_error));
				}
			}


		});

	}

	public void postLoginInformation() {

		final ProgressDialog progressDialog = ProgressDialog.show(this, "", "Loading...");
		TextView emailTxt = (TextView)findViewById(R.id.email_address);
		TextView passwordTxt = (TextView)findViewById(R.id.password);

		RequestParams params = new RequestParams();
		params.put("username", emailTxt.getText().toString());
		params.put("password", passwordTxt.getText().toString());
		params.put("uuid",new DeviceUuidFactory(this).getDeviceUuid().toString());

		WebSenseRestClient.post(Constants.AUTHENTICATE_METHOD, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {
				handleResponse(responseString);
				progressDialog.dismiss();	
			}

			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				progressDialog.dismiss();
				if(statusCode==400){
					showAlert("Invalid request.");
					
				}else if(statusCode == 401){
					showAlert("Invalid username and password.");
				}else{
					showAlert(getApplicationContext().getString(R.string.server_error));
				}
				
			}


		});

	}

void handleResponse(String responseString){
	
	Object response;
	try {
		Util.logi("Real Response:" + responseString);
		response = Util.parseResponse(responseString);

		if (response instanceof JSONObject) {
			if(((JSONObject) response).has("auth_token")==true){
			String auth_key = ((JSONObject) response).getString("auth_token");
			Util.saveSecurePreference(getApplicationContext(), auth_key, Constants.AUTH_KEY_TOKEN);
			navigateToMain();
			}else{
				 showAlert("Invalid username and password.");
			}
		}else{
			showAlert(getApplicationContext().getString(R.string.server_error));
		}

	} catch (JSONException e) {
		showAlert(getApplicationContext().getString(R.string.server_error));
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	

}

	@SuppressLint("NewApi")
	public void switchRegistratonOn(){

		if(isRegisterView==false){
			
			LinearLayout l1 = (LinearLayout)findViewById(R.id.registrationContent);
			l1.setAlpha(0f);
			l1.setVisibility(View.VISIBLE);
			l1.animate()
			.alpha(1f)
			.setDuration(1000)
			.setListener(new AnimatorListener() {
				
				@Override
				public void onAnimationStart(Animator animation) {
					registerBtnDmy.setVisibility(View.GONE);
					loginBtnDmy.setVisibility(View.VISIBLE);
					loginBtn.setVisibility(View.GONE);
					registerBtn.setVisibility(View.VISIBLE);
					
				}
				
				@Override
				public void onAnimationRepeat(Animator animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animator animation) {
					
					
				}
				
				@Override
				public void onAnimationCancel(Animator animation) {
					// TODO Auto-generated method stub
					
				}
			});
			isRegisterView = true;
			LinearLayout l2 = (LinearLayout)findViewById(R.id.top_diff_layout);
			l2.getLayoutParams().height = 1;

			
		}else{
			
			LinearLayout l1 = (LinearLayout)findViewById(R.id.registrationContent);
			l1.setVisibility(View.VISIBLE);
			l1.setAlpha(1f);
			l1.setVisibility(View.GONE);
			l1.animate()
			.alpha(0f)
			.setDuration(800)
			.setListener(new AnimatorListener() {
				
				@Override
				public void onAnimationStart(Animator animation) {
					
					registerBtnDmy.setVisibility(View.VISIBLE);
					loginBtnDmy.setVisibility(View.GONE);
					registerBtn.setVisibility(View.GONE);
					
					isRegisterView = false;
					LinearLayout l2 = (LinearLayout)findViewById(R.id.top_diff_layout);
					l2.getLayoutParams().height = 350;	
					
					
				}
				
				@Override
				public void onAnimationRepeat(Animator animation) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animator animation) {

					loginBtn.setVisibility(View.VISIBLE);
					
					
					
					
				}
				
				@Override
				public void onAnimationCancel(Animator animation) {
					// TODO Auto-generated method stub
					
				}
			});
			
			
			
			
			
			

		}
	} 

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

}
