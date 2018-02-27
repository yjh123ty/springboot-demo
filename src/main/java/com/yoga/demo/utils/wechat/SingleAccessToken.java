package com.yoga.demo.utils.wechat;

/**
 * 使用线程循环获取 token ，防止token失效
 * 
 * @author yoga
 */
public class SingleAccessToken {

	private AccessToken accessToken;
	private static SingleAccessToken singleAccessToken;

	/**
	 * 私有构造函数
	 */
	private SingleAccessToken() {

		accessToken = WeChatUtils.getAccessToken();
		initThread();
	}

	/**
	 * 获取SingleAccessToken对象
	 * 
	 * @return
	 */
	public static SingleAccessToken getInstance() {
		if (singleAccessToken == null) {
			singleAccessToken = new SingleAccessToken();
		}
		return singleAccessToken;
	}

	public AccessToken getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(AccessToken accessToken) {
		this.accessToken = accessToken;
	}

	/**
	 * 开启线程，设置SingleAccessToken为空
	 */
	private void initThread() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// 睡眠7000秒
					Thread.sleep(7000 * 1000);
					singleAccessToken = null;

				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}).start();
	}
}
