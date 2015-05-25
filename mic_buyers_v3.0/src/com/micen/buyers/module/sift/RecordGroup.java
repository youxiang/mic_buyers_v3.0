package com.micen.buyers.module.sift;

public class RecordGroup
{
	public int realId;
	public int groupId;
	public int childId;

	public RecordGroup(String realId, int groupId, int childId)
	{
		this.realId = Integer.parseInt(realId);
		this.groupId = groupId;
		this.childId = childId;
	}
}
