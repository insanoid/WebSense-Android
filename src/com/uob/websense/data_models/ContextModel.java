package com.uob.websense.data_models;

import android.content.ContentValues;

public class ContextModel {
	private String contextType;
	private String contextValue;
	private String position;
	private String timestamp;
	/**
	 * @return the contextType
	 */
	public String getContextType() {
		return contextType;
	}
	/**
	 * @param contextType the contextType to set
	 */
	public void setContextType(String contextType) {
		this.contextType = contextType;
	}
	/**
	 * @return the contextValue
	 */
	public String getContextValue() {
		return contextValue;
	}
	/**
	 * @param contextValue the contextValue to set
	 */
	public void setContextValue(String contextValue) {
		this.contextValue = contextValue;
	}
	/**
	 * @return the position
	 */
	public String getPosition() {
		return position;
	}
	/**
	 * @param position the position to set
	 */
	public void setPosition(String position) {
		this.position = position;
	}
	/**
	 * @return the timestamp
	 */
	public String getTimestamp() {
		return timestamp;
	}
	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	
	
	public ContentValues getContentValues(){
		
		ContentValues values = new ContentValues();

		values.put("content_type", getContextType());
		values.put("content", getContextValue());
		values.put("timestamp",getTimestamp());
		if(getPosition()!=null)
			values.put("position", String.valueOf(getPosition()));
		values.put("synced", false);
		return values;
		
	}
	
}
