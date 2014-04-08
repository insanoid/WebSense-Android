package com.uob.websense.ui;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.uob.websense.R;
import com.uob.websense.adapter.SpinnerAdapter;
import com.uob.websense.support.Util;
import com.uob.websense.web_service_manager.WebSenseRestClient;

/**
 * @author karthikeyaudupa
 *
 */

public class RegisterActivity extends FragmentActivity {

	Spinner userTypeSpinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(checkForLogin()==true){
			setContentView(R.layout.register_view);
			userTypeSpinner = (Spinner) findViewById(R.id.user_type);
			SpinnerAdapter spinerAdapter = new SpinnerAdapter(this,getResources().getStringArray(R.array.user_type_array));
			userTypeSpinner.setAdapter(spinerAdapter);
			Util.overrideFonts(this, findViewById(android.R.id.content));
			
		}else{
			navigateToMain();
		}
	}

	public void onBtnClicked(View v){
		if(v.getId() == R.id.login_button){

			Util.hideSoftKeyboard(this);

			if(validateViewInputs()==true){
				postInformation();
			}else{

			}
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
				userTypeSpinner.getSelectedItem()!=null){

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

	/*
	 * Checks if the user is already logged in.
	 */
	public boolean checkForLogin(){
	//	return false;	
		if(Util.getSecurePreference(getApplicationContext(), "auth_token")!=null){
			return true;
		}else{
			return false;
		}
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
		params.put("gender", genderTxt.equalsIgnoreCase(getString(R.string.male))?1:2);
		params.put("user_type", userTypeSpinner.getSelectedItemPosition());
		
		WebSenseRestClient.post("create", params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, String responseString) {

				Object response;
				try {
					response = Util.parseResponse(responseString);

					if (response instanceof JSONObject) {
						String auth_key = ((JSONObject) response).getString("auth_token");
						Util.saveSecurePreference(getApplicationContext(), auth_key, "auth_token");
						navigateToMain();

					}else{
						showAlert(getApplicationContext().getString(R.string.server_error));
					}

				} catch (JSONException e) {
					showAlert(getApplicationContext().getString(R.string.server_error));
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				progressDialog.dismiss();

			}


			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				progressDialog.dismiss();
				showAlert(getApplicationContext().getString(R.string.server_error));
			}


		});

	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	}


	
}
