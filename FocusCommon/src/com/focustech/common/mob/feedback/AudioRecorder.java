package com.focustech.common.mob.feedback;

import java.io.File;

import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.os.Environment;

/**
 * 录音功能类
 * 
 * @author chenkangpeng
 * 
 */
public class AudioRecorder
{
	/** 录音容许最长时间，超过该时间则结束录音 ,默认时间是30秒 */
	private int defalutMaxTime = 1000 * 30;
	/** 录音文件保存的路径 */
	private String defaultAudioFilePath = "mnt/sdcard/recorder/tem/";
	private MediaRecorder mediaRecorder;
	private RecorderState state = RecorderState.stop;
	/** 录音文件 */
	private File tempAudioFile;
	private String defaultAudioExtensions = ".amr";
	private RecorderListener recorderListener;

	public AudioRecorder()
	{
		super();
	}

	public void start()
	{
		new Thread()
		{
			public void run()
			{
				startRecorder();
			}
		}.start();
	}

	/**
	 * 开始录音
	 */
	private void startRecorder()
	{
		if (!isSdcardexit())
		{
			return;
		}
		try
		{
			if (state != RecorderState.recording)
			{
				state = RecorderState.recording;
				File dFile = new File(defaultAudioFilePath);
				if (!dFile.exists())
				{
					dFile.mkdirs();
				}
				mediaRecorder = new MediaRecorder();
				// 设置音源从麦克风接入
				mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				// 设置输出格式
				mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
				mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
				tempAudioFile = new File(getDefaultAudioFilePath() + System.currentTimeMillis()
						+ defaultAudioExtensions);
				mediaRecorder.setOutputFile(tempAudioFile.getAbsolutePath());
				mediaRecorder.setMaxDuration(defalutMaxTime);
				mediaRecorder.setOnInfoListener(new OnInfoListener()
				{
					@Override
					public void onInfo(MediaRecorder mr, int what, int extra)
					{
						if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED)
						{
							recorderListener.finishRecorder(tempAudioFile);
							state = RecorderState.stop;
						}
					}
				});
				mediaRecorder.prepare();
				mediaRecorder.start();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 停止录音
	 */
	public void stopRecorder()
	{
		if (state == RecorderState.recording)
		{
			state = RecorderState.stop;
			try
			{
				mediaRecorder.stop();
				mediaRecorder.release();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			mediaRecorder = null;
			if (recorderListener != null)
			{
				recorderListener.finishRecorder(tempAudioFile);
			}
		}
	}

	private boolean isSdcardexit()
	{
		return (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED));
	}

	/***
	 * 定义两种录音的两种状态
	 * 
	 * @author chenkangpeng
	 * 
	 */
	enum RecorderState
	{
		recording, stop
	}

	/***
	 * 
	 * @return
	 */
	private String getDefaultAudioFilePath()
	{
		if (defaultAudioFilePath.endsWith("/"))
		{
			return defaultAudioFilePath;
		}
		else
		{
			return defaultAudioFilePath + "/";
		}
	}

	public interface RecorderListener
	{
		public void finishRecorder(File file);
	}

	public long getDefalutMaxTime()
	{
		return defalutMaxTime;
	}

	public void setDefalutMaxTime(int defalutMaxTime)
	{
		this.defalutMaxTime = defalutMaxTime;
	}

	public String getDefaultAudioExtensions()
	{
		return defaultAudioExtensions;
	}

	public void setDefaultAudioExtensions(String defaultAudioExtensions)
	{
		this.defaultAudioExtensions = defaultAudioExtensions;
	}

	public RecorderListener getRecorderListener()
	{
		return recorderListener;
	}

	public void setRecorderListener(RecorderListener recorderListener)
	{
		this.recorderListener = recorderListener;
	}

	public void setDefaultAudioFilePath(String defaultAudioFilePath)
	{
		this.defaultAudioFilePath = defaultAudioFilePath;
	}
}
