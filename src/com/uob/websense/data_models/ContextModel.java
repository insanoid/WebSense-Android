/* **************************************************
Copyright (c) 2014, University of Birmingham
Karthikeya Udupa, kxu356@bham.ac.uk

Permission to use, copy, modify, and/or distribute this software for any
purpose with or without fee is hereby granted, provided that the above
copyright notice and this permission notice appear in all copies.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 ************************************************** */

package com.uob.websense.data_models;

import android.content.ContentValues;

/**
 * Model to store contexual information.
 * @author karthikeyaudupa
 *
 */
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
