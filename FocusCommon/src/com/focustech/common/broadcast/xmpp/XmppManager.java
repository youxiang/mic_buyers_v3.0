/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.focustech.common.broadcast.xmpp;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Type;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.util.Log;

import com.focustech.common.util.LogUtil;

/**
 * This class is to manage the XMPP connection between client and server.
 * 
 * @author Sehwan Noh (devnoh@gmail.com)
 */
public class XmppManager
{

	private static final String LOGTAG = LogUtil.makeLogTag(XmppManager.class);

	private static final String XMPP_RESOURCE_NAME = "FocusBroadcast";

	private Context context;

	private NotificationService.TaskSubmitter taskSubmitter;

	private NotificationService.TaskTracker taskTracker;

	private SharedPreferences sharedPrefs;

	private XMPPConnection connection;

	private String username;

	private String password;

	private ConnectionListener connectionListener;

	private PacketListener notificationPacketListener;

	private Handler handler;

	private List<Runnable> taskList;

	private boolean running = false;

	private Future<?> futureTask;

	private Thread reconnection;

	public XmppManager(NotificationService notificationService)
	{
		context = notificationService;
		taskSubmitter = notificationService.getTaskSubmitter();
		taskTracker = notificationService.getTaskTracker();
		sharedPrefs = notificationService.getSharedPreferences();

		username = sharedPrefs.getString(Constants.DEVICE_ID, newRandomUUID());
		password = "123456";

		connectionListener = new PersistentConnectionListener(this);
		notificationPacketListener = new NotificationPacketListener(this);

		handler = new Handler();
		taskList = new ArrayList<Runnable>();
		reconnection = new ReconnectionThread(this);
	}

	public Context getContext()
	{
		return context;
	}

	public void connect()
	{
		Log.d(LOGTAG, "connect()...");
		submitLoginTask();
	}

	public void disconnect()
	{
		Log.d(LOGTAG, "disconnect()...");
		terminatePersistentConnection();
	}

	public void terminatePersistentConnection()
	{
		Log.d(LOGTAG, "terminatePersistentConnection()...");
		Runnable runnable = new Runnable()
		{

			final XmppManager xmppManager = XmppManager.this;

			public void run()
			{
				if (xmppManager.isConnected())
				{
					Log.d(LOGTAG, "terminatePersistentConnection()... run()");
					xmppManager.getConnection().removePacketListener(xmppManager.getNotificationPacketListener());
					xmppManager.getConnection().disconnect();
				}
				xmppManager.runTask();
			}

		};
		addTask(runnable);
	}

	public XMPPConnection getConnection()
	{
		return connection;
	}

	public void setConnection(XMPPConnection connection)
	{
		this.connection = connection;
	}

	public String getUsername()
	{
		return username;
	}

	public String getPassword()
	{
		return password;
	}

	public ConnectionListener getConnectionListener()
	{
		return connectionListener;
	}

	public PacketListener getNotificationPacketListener()
	{
		return notificationPacketListener;
	}

	public void startReconnectionThread()
	{
		synchronized (reconnection)
		{
			if (!reconnection.isAlive())
			{
				reconnection.setName("Xmpp Reconnection Thread");
				reconnection.start();
			}
		}
	}

	public Handler getHandler()
	{
		return handler;
	}

	public void reregisterAccount()
	{
		removeAccount();
		submitLoginTask();
		runTask();
	}

	public List<Runnable> getTaskList()
	{
		return taskList;
	}

	public Future<?> getFutureTask()
	{
		return futureTask;
	}

	public void runTask()
	{
		Log.d(LOGTAG, "runTask()...");
		synchronized (taskList)
		{
			running = false;
			futureTask = null;
			if (!taskList.isEmpty())
			{
				Runnable runnable = (Runnable) taskList.get(0);
				taskList.remove(0);
				running = true;
				futureTask = taskSubmitter.submit(runnable);
				if (futureTask == null)
				{
					taskTracker.decrease();
				}
			}
		}
		taskTracker.decrease();
		Log.d(LOGTAG, "runTask()...done");
	}

	private String newRandomUUID()
	{
		String uuidRaw = UUID.randomUUID().toString();
		return uuidRaw.replaceAll("-", "");
	}

	private boolean isConnected()
	{
		return connection != null && connection.isConnected();
	}

	private boolean isAuthenticated()
	{
		return connection != null && connection.isConnected() && connection.isAuthenticated();
	}

	private void submitConnectTask()
	{
		Log.d(LOGTAG, "submitConnectTask()...");
		addTask(new ConnectTask());
	}

	private void submitLoginTask()
	{
		Log.d(LOGTAG, "submitLoginTask()...");
		submitConnectTask();
		addTask(new LoginTask());
	}

	private void addTask(Runnable runnable)
	{
		Log.d(LOGTAG, "addTask(runnable)...");
		taskTracker.increase();
		synchronized (taskList)
		{
			if (taskList.isEmpty() && !running)
			{
				running = true;
				futureTask = taskSubmitter.submit(runnable);
				if (futureTask == null)
				{
					taskTracker.decrease();
				}
			}
			else
			{
				// 解决服务器端重启后,客户端不能成功连接XMPP服务器
				runTask();
				taskList.add(runnable);
			}
		}
		Log.d(LOGTAG, "addTask(runnable)... done");
	}

	private void removeAccount()
	{
		deleteAccount(connection);
		Editor editor = sharedPrefs.edit();
		editor.remove(Constants.XMPP_USERNAME);
		editor.remove(Constants.XMPP_PASSWORD);
		editor.commit();
	}

	/** 
	 * 删除当前用户 
	 * @param connection 
	 * @return 
	 */
	public boolean deleteAccount(XMPPConnection connection)
	{
		try
		{
			connection.getAccountManager().deleteAccount();
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}

	/**
	 * A runnable task to connect the server. 
	 */
	private class ConnectTask implements Runnable
	{
		final XmppManager xmppManager;

		private ConnectTask()
		{
			this.xmppManager = XmppManager.this;
		}

		public void run()
		{
			Log.i(LOGTAG, "ConnectTask.run()...");

			if (!xmppManager.isConnected())
			{
				// Create the configuration for this new connection
				ConnectionConfiguration connConfig = new ConnectionConfiguration(Constants.XMPP_HOST);
				// connConfig.setSecurityMode(SecurityMode.disabled);
				connConfig.setSecurityMode(SecurityMode.required);
				connConfig.setSASLAuthenticationEnabled(false);
				connConfig.setCompressionEnabled(false);

				XMPPConnection connection = new XMPPConnection(connConfig);
				xmppManager.setConnection(connection);

				try
				{
					// Connect to the server
					connection.connect();
					Log.i(LOGTAG, "XMPP connected successfully");
				}
				catch (XMPPException e)
				{
					Log.e(LOGTAG, "XMPP connection failed", e);
				}
				xmppManager.runTask();

			}
			else
			{
				Log.i(LOGTAG, "XMPP connected already");
				xmppManager.runTask();
			}
		}
	}

	/**
	 * A runnable task to log into the server. 
	 */
	private class LoginTask implements Runnable
	{

		final XmppManager xmppManager;

		private LoginTask()
		{
			this.xmppManager = XmppManager.this;
		}

		public void run()
		{
			Log.i(LOGTAG, "LoginTask.run()...");

			if (!xmppManager.isAuthenticated())
			{
				Log.d(LOGTAG, "username=" + username);
				Log.d(LOGTAG, "password=" + password);

				try
				{
					// xmppManager.username = "xiongjw";
					xmppManager.getConnection().login(xmppManager.getUsername(), xmppManager.getPassword(),
							XMPP_RESOURCE_NAME);
					Log.d(LOGTAG, "Loggedn in successfully");

					// connection listener
					if (xmppManager.getConnectionListener() != null)
					{
						xmppManager.getConnection().addConnectionListener(xmppManager.getConnectionListener());
					}

					// packet filter
					PacketFilter packetFilter = new PacketTypeFilter(Message.class);
					// packet listener
					PacketListener packetListener = xmppManager.getNotificationPacketListener();
					connection.addPacketListener(packetListener, packetFilter);

					Presence presence = new Presence(Type.available);
					// presence.setMode(Mode.dnd);
					connection.sendPacket(presence);

					xmppManager.runTask();

				}
				catch (XMPPException e)
				{
					Log.e(LOGTAG, "LoginTask.run()... xmpp error");
					Log.e(LOGTAG, "Failed to login to xmpp server. Caused by: " + e.getMessage());
					String INVALID_CREDENTIALS_ERROR_CODE = "401";
					String errorMessage = e.getMessage();
					if (errorMessage != null && errorMessage.contains(INVALID_CREDENTIALS_ERROR_CODE))
					{
						xmppManager.reregisterAccount();
						return;
					}
					xmppManager.startReconnectionThread();

				}
				catch (Exception e)
				{
					Log.e(LOGTAG, "LoginTask.run()... other error");
					Log.e(LOGTAG, "Failed to login to xmpp server. Caused by: " + e.getMessage());
					xmppManager.startReconnectionThread();
				}

			}
			else
			{
				Log.i(LOGTAG, "Logged in already");
				xmppManager.runTask();
			}

		}
	}

}
