/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portlet.exportimport.backgroundtask;

import com.liferay.portal.kernel.backgroundtask.BackgroundTaskResult;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.model.BackgroundTask;
import com.liferay.portal.service.BackgroundTaskLocalServiceUtil;
import com.liferay.portlet.exportimport.model.ExportImportConfiguration;
import com.liferay.portlet.exportimport.service.ExportImportLocalServiceUtil;

import java.io.File;
import java.io.Serializable;

import java.util.Map;

/**
 * @author Daniel Kocsis
 * @author Michael C. Han
 * @author Akos Thurzo
 */
public class PortletExportBackgroundTaskExecutor
	extends BaseExportImportBackgroundTaskExecutor {

	public PortletExportBackgroundTaskExecutor() {
		setBackgroundTaskStatusMessageTranslator(
			new PortletExportImportBackgroundTaskStatusMessageTranslator());
	}

	@Override
	public BackgroundTaskResult execute(BackgroundTask backgroundTask)
		throws Exception {

		ExportImportConfiguration exportImportConfiguration =
			getExportImportConfiguration(backgroundTask);

		Map<String, Serializable> settingsMap =
			exportImportConfiguration.getSettingsMap();

		long userId = MapUtil.getLong(settingsMap, "userId");
		String fileName = MapUtil.getString(settingsMap, "fileName");

		File larFile = ExportImportLocalServiceUtil.exportPortletInfoAsFile(
			exportImportConfiguration);

		BackgroundTaskLocalServiceUtil.addBackgroundTaskAttachment(
			userId, backgroundTask.getBackgroundTaskId(), fileName, larFile);

		return BackgroundTaskResult.SUCCESS;
	}

}