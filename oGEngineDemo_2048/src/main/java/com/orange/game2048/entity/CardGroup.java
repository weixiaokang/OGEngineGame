package com.orange.game2048.entity;

import com.orange.entity.group.EntityGroup;
import com.orange.entity.scene.Scene;
import com.orange.entity.text.Text;
import com.orange.game2048.util.ConstantUtil;
import com.orange.res.FontRes;

/**
 * 卡片类
 * 
 * @author lch
 * 
 */
public class CardGroup extends EntityGroup {

	private CardSprite cardSprite;// 卡片背景
	private int number = 0;// 数字
	private Text tNum; // 数字文本

	// ============get&set===================
	// 获取数字
	public int getNumber() {
		return number;
	}

	// 设置数字
	public void setNumber(int number) {
		this.number = number;
		onDrawNum(number);
	}

	// ======================================

	public CardGroup(float pX, float pY, Scene pScene) {
		// 卡片资源原图的大小为90*90，所以这个组合的宽高可以设置为90*90
		super(pX, pY, 90, 90, pScene);
		// 初始化View
		initView();

		// 自动计算成自适应宽高
		this.setWrapSize();
	}

	private void initView() {
		// 创建卡片精灵
		cardSprite = new CardSprite(this.getVertexBufferObjectManager());
		this.attachChild(cardSprite);
		// 创建文本实体用于显示卡片上的数字，文本是可变的，这里设置文本的默认值为空字符串，最大的显示位数为4
		tNum = new Text(0, 0, FontRes.getFont(ConstantUtil.FONT_CARD_NUM), "",
				4, this.getVertexBufferObjectManager());
		// 设置文本的中心Y坐标在cardSprite的Y坐标中点上
		tNum.setCentrePositionY(cardSprite.getCentreY());
		this.attachChild(tNum);
		// 画卡片上的数字并根据数字显示颜色，默认值为0
		onDrawNum(0);
	}

	// 画卡片上的数字，并根据数字设置卡片的颜色
	private void onDrawNum(int number) {
		float[] mRGBs;
		switch (number) {
		case 0:
			mRGBs = ConstantUtil.RGBS_0;
			break;
		case 2:
			mRGBs = ConstantUtil.RGBS_2;
			break;
		case 4:
			mRGBs = ConstantUtil.RGBS_4;
			break;
		case 8:
			mRGBs = ConstantUtil.RGBS_8;
			break;
		case 16:
			mRGBs = ConstantUtil.RGBS_16;
			break;
		case 32:
			mRGBs = ConstantUtil.RGBS_32;
			break;
		case 64:
			mRGBs = ConstantUtil.RGBS_64;
			break;
		case 128:
			mRGBs = ConstantUtil.RGBS_128;
			break;
		case 256:
			mRGBs = ConstantUtil.RGBS_256;
			break;
		case 512:
			mRGBs = ConstantUtil.RGBS_512;
			break;
		case 1024:
			mRGBs = ConstantUtil.RGBS_1024;
			break;
		case 2048:
			mRGBs = ConstantUtil.RGBS_2048;
			break;
		default:
			mRGBs = ConstantUtil.RGBS_0;
			break;
		}
		// 设置精灵颜色，传入的是RGB的数组形式
		cardSprite.setRGB(mRGBs);

		// 设置文本显示，因为数字的位数可能不一样，这里需要设置数字相对于卡片精灵X坐标居中
		if (number == 0) {
			tNum.setText("");
		} else {
			tNum.setText(number + "");
			tNum.setCentrePositionX(cardSprite.getCentreX());
		}

	}

	// 对比当前卡片与传进来的卡片上的数字是否相等
	public boolean equals(CardGroup pCardGroup) {
		return getNumber() == pCardGroup.getNumber();
	}
}
