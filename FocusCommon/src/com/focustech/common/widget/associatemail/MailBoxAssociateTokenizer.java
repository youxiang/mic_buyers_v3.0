package com.focustech.common.widget.associatemail;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.MultiAutoCompleteTextView.Tokenizer;

/**********************************************************
 * @文件名称：MailBoxAssociateTokenizer.java
 * @文件作者：xiongjiangwei
 * @创建时间：2014年8月7日 下午3:00:15
 * @文件描述：邮箱联想过滤器
 * @修改历史：2014年8月7日创建初始版本
 **********************************************************/
public class MailBoxAssociateTokenizer implements Tokenizer
{

	@Override
	public int findTokenEnd(CharSequence text, int cursor)
	{
		int i = cursor;
		int len = text.length();
		while (i < len)
		{
			if (text.charAt(i) == '@')
			{
				return i;
			}
			else
			{
				i++;
			}
		}
		return len;
	}

	@Override
	public int findTokenStart(CharSequence text, int cursor)
	{
		int index = text.toString().indexOf("@");

		if (index < 0)
		{
			index = text.length();
		}
		if (index >= findTokenEnd(text, cursor))
		{
			index = 0;
		}
		return index;
	}

	@Override
	public CharSequence terminateToken(CharSequence text)
	{
		int i = text.length();

		while (i > 0 && text.charAt(i - 1) == ' ')
		{
			i--;
		}

		if (i > 0 && text.charAt(i - 1) == '@')
		{
			return text;
		}
		else
		{
			if (text instanceof Spanned)
			{
				SpannableString sp = new SpannableString(text);
				TextUtils.copySpansFrom((Spanned) text, 0, text.length(), Object.class, sp, 0);
				return sp;
			}
			else
			{
				return text;
			}
		}
	}

}
