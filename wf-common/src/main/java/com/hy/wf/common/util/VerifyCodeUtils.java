/*
 * Copyright 2017-2018 All rights reserved.
 * Support: JinXing
 * License: 
 */

package com.hy.wf.common.util;

import java.util.Random;

/**
 * Utils - 验证码 <br/>
 * 
 * @author JinXing Team
 * @email
 * @date: 2018-4-8 上午1:54:36
 * @version 1.0
 */
public class VerifyCodeUtils {

	public static final String VERIFY_CODES = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ"; // 去掉了1,0,i,o几个容易混淆的字符

	/**
	 * 使用系统默认字符源生成验证码
	 * 
	 * @param verifySize 验证码长度
	 * @return
	 */
	public static String generateVerifyCode(int verifySize) {
		return generateVerifyCode(verifySize, VERIFY_CODES);
	}

	/**
	 * 使用指定源生成验证码
	 * 
	 * @param verifySize 验证码长度
	 * @param sources 验证码字符源
	 * @return
	 */
	public static String generateVerifyCode(int verifySize, String sources) {
		if (sources == null || sources.length() == 0) {
			sources = VERIFY_CODES;
		}
		int codesLen = sources.length();
		Random rand = new Random(System.currentTimeMillis());
		StringBuilder verifyCode = new StringBuilder(verifySize);
		for (int i = 0; i < verifySize; i++) {
			verifyCode.append(sources.charAt(rand.nextInt(codesLen - 1)));
		}
		
		return verifyCode.toString();
	}
	
}
