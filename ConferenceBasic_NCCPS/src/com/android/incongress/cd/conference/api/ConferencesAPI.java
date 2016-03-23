package com.android.incongress.cd.conference.api;


public interface ConferencesAPI {
	//参会易新版接口
	public String sendTopic(int cId,int userId,int speakerId,int sessonId,String title);//广场发表消息 
	public String sendTopicContent(int topicId,int userId,String content);//回复帖子
	public String getSendLinkTopic(int userId,int conferencesId,int startRow,int row);//获取用户发表帖子列表 分页
	public String getSpeakerLinkTopic(int userId,int startRow,int row);//获取用户向专家提问的帖子
	public String getTopicList(int cId,int userId,int startRow,int row);//获取广场帖子列表
	public String getTopicContentList(int titleId,int startRow,int row);//获取回复帖子内容列表
	public String sendTopicLaud(int titleId,int userId);//赞帖子
	public String mobileLoginV4(int cId,String mobile,String password);//登录
	public String getAd(int cId);//获取广告接口
	public String getActivity(int conferencesId,int startRow,int row);//获取展商活动接口
	public String getInitDataV2(int cId,int dataVersion,int clientType,String appVersion,String token);//打开应用初始化接口
	public String sendFeedbackV2(int cId,String phone,String email,String content);//反馈
	public String getNewsListV1(int conferencesId,int startRow,int row);//获取会议新闻列表
	public String getNews(int newsId);//获取会议新闻详细内容
	public String getNewsListV2(int conferencesId,int startRow,int row,String language);//获取会议新闻列表 中英文 zh 中文 en英文
	public String getNewsV2(int newsId,String language);//获取会议新闻详细内容 中英文
	public int getMyTopics(int userId,int cId);//获取用户发表帖子
	public String caseList(int conId,int count,int pageIndex,int type, String searchString);//获取精彩回顾的所有信息
    public String getTokenList(int conId, int count, int pageIndex, String ids); //获取消息站信息
}
