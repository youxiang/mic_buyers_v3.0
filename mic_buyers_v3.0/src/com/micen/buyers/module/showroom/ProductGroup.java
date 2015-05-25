package com.micen.buyers.module.showroom;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductGroup implements Parcelable
{
	public String comId;
	public String encryptFlag;
	public String groupId;
	public String groupLevel;
	public String groupName;
	public String groupPassword;
	public String groupProdCount;
	public String parentGroupId;
	public String showroomDisplaySeq;
	public ArrayList<ProductGroup> childGroup;

	public boolean isHaveChild()
	{
		return childGroup != null && childGroup.size() > 0;
	}

	@Override
	public int describeContents()
	{
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		dest.writeString(comId);
		dest.writeString(encryptFlag);
		dest.writeString(groupId);
		dest.writeString(groupLevel);
		dest.writeString(groupName);
		dest.writeString(groupPassword);
		dest.writeString(groupProdCount);
		dest.writeString(parentGroupId);
		dest.writeString(showroomDisplaySeq);
		if (isHaveChild())
		{
			dest.writeInt(childGroup.size());
			for (ProductGroup child : childGroup)
			{
				child.writeToParcel(dest, flags);
			}
		}
		else
		{
			dest.writeInt(0);
		}
	}

	public static final Parcelable.Creator<ProductGroup> CREATOR = new Parcelable.Creator<ProductGroup>()
	{
		public ProductGroup createFromParcel(Parcel in)
		{
			ProductGroup group = new ProductGroup();
			group.comId = in.readString();
			group.encryptFlag = in.readString();
			group.groupId = in.readString();
			group.groupLevel = in.readString();
			group.groupName = in.readString();
			group.groupPassword = in.readString();
			group.groupProdCount = in.readString();
			group.parentGroupId = in.readString();
			group.showroomDisplaySeq = in.readString();
			int size = in.readInt();
			if (size > 0)
			{
				group.childGroup = new ArrayList<ProductGroup>();
				for (int i = 0; i < size; i++)
				{
					group.childGroup.add(CREATOR.createFromParcel(in));
				}
			}
			return group;

		}

		public ProductGroup[] newArray(int size)
		{
			return new ProductGroup[size];
		}
	};
}
