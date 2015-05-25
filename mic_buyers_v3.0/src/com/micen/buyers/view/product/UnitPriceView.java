package com.micen.buyers.view.product;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.micen.buyers.activity.R;

public class UnitPriceView extends LinearLayout
{
	
	private TextView minOrderText;
	
	private TextView priceText;
	
	private String minOrder;
	
	private String price;

	public UnitPriceView(Context context, String minOrder, String price)
	{
		super(context);
		this.minOrder = minOrder;
		this.price = price;
		init();
	}

	private void init()
	{
		LayoutInflater.from(getContext()).inflate(R.layout.unit_price_item, this);
		
		minOrderText = (TextView) findViewById(R.id.min_order);
		priceText = (TextView) findViewById(R.id.price);
		
		minOrderText.setText(minOrder);
		priceText.setText(price);
	}

}
