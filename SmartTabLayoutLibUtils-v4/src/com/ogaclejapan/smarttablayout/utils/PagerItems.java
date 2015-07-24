/**
 * Copyright (C) 2015 ogaclejapan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ogaclejapan.smarttablayout.utils;

import java.util.ArrayList;

import android.content.Context;

@SuppressWarnings("serial")
public abstract class PagerItems<T extends PagerItem> extends ArrayList<T> {

	private final Context context;

	protected PagerItems(Context context) {
		this.context = context;
	}

	public Context getContext() {
		return context;
	}

}
