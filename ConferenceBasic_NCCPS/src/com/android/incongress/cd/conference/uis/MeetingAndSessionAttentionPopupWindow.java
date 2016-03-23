package com.android.incongress.cd.conference.uis;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.incongress.cd.conference.adapters.AttentionPopupAdapter;
import com.android.incongress.cd.conference.base.Constants;
import com.android.incongress.cd.conference.beans.AlertBean;
import com.android.incongress.cd.conference.beans.ClassesBean;
import com.android.incongress.cd.conference.beans.MeetingBean;
import com.android.incongress.cd.conference.beans.SessionBean;
import com.android.incongress.cd.conference.data.ConferenceSetData;
import com.android.incongress.cd.conference.data.ConferenceTableField;
import com.android.incongress.cd.conference.data.ConferenceTables;
import com.android.incongress.cd.conference.data.DbAdapter;
import com.android.incongress.cd.conference.data.ConferenceGetData;
import com.android.incongress.cd.conference.utils.AlermClock;
import com.mobile.incongress.cd.conference.basic.csccm.R;

import java.util.List;


/**
 * Created by Jacky on 2015/12/20.
 */
public class MeetingAndSessionAttentionPopupWindow extends PopupWindow {
    private int mWidth;
    private int mHeight;
    private View mConvertView;
    private TextView mTvSessionName, mTvSessionTime;
    private ListViewForScrollView mLvMeetings;
    private AttentionPopupAdapter mAdapter;
    private ImageView mIvSessionAttention;
    private LinearLayout mLlSessionContainer;

    private SessionBean mSessionBean;
    private List<MeetingBean> mMeetingsList;
    private ClassesBean mClassBean;

    public MeetingAndSessionAttentionPopupWindow(Context context, SessionBean sessionBean, List<MeetingBean> mMeetings, ClassesBean classBean) {
        super(context);
        calWidthAndHeight(context);

        mConvertView = LayoutInflater.from(context).inflate(R.layout.popup_attention_meeting_session, null);

        this.mSessionBean = sessionBean;
        this.mMeetingsList = mMeetings;
        this.mClassBean = classBean;

        setContentView(mConvertView);
        setWidth(mWidth);
        setHeight(mHeight);

        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true); //外部可以点击
        setBackgroundDrawable(new BitmapDrawable());
        ;//点击外部消失
        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });

        initViews(context);
        initEvent(context);
    }

    private void initViews(Context context) {
        mLlSessionContainer = (LinearLayout) mConvertView.findViewById(R.id.ll_session_container);
        mIvSessionAttention = (ImageView) mConvertView.findViewById(R.id.iv_session_attention);
        mTvSessionName = (TextView) mConvertView.findViewById(R.id.tv_session_name);
        mTvSessionTime = (TextView) mConvertView.findViewById(R.id.tv_session_time);
        mLvMeetings = (ListViewForScrollView) mConvertView.findViewById(R.id.lv_meetings);
        mAdapter = new AttentionPopupAdapter(mMeetingsList, context);

        mTvSessionName.setText(mSessionBean.getSessionName());
        mTvSessionTime.setText(mSessionBean.getStartTime() + "-" + mSessionBean.getEndTime());

        if(mSessionBean.getAttention() == Constants.ATTENTION) {
            mIvSessionAttention.setImageResource(R.drawable.bt_collected);

            //所有meeteing加上关注
        }else {
            mIvSessionAttention.setImageResource(R.drawable.bt_uncollected);

            //取消对所有Meeting的关注
        }
    }

    private void initEvent(final Context context) {
        mLvMeetings.setAdapter(mAdapter);

        mLlSessionContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSessionBean.getAttention() == Constants.ATTENTION) {
                    mIvSessionAttention.setImageResource(R.drawable.bt_uncollected);
                    mSessionBean.setAttention(Constants.NOATTENTION);
                    addSessionAlert(mSessionBean);
                } else {
                    mIvSessionAttention.setImageResource(R.drawable.bt_collected);
                    mSessionBean.setAttention(Constants.ATTENTION);
                    deleteSessionAlert(mSessionBean);
                }
            }
        });

        mLvMeetings.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.findViewById(R.id.iv_attention).startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate_anim));
            }
        });
    }

    /**
     * 计算popupWindow的高度和宽度
     *
     * @param context
     */
    private void calWidthAndHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        mWidth = outMetrics.widthPixels;
        mHeight = (int) (outMetrics.heightPixels * 0.4);
    }


    /**
     * 关注相关的功能性代码，空了最好抽出来，独立使用
     */
    private void addSessionAlert(SessionBean bean){
        DbAdapter ada = DbAdapter.getInstance();
        ada.open();

        ConferenceSetData.updateSessionAttention(ada, bean.getSessionGroupId(), Constants.ATTENTION);
        ada.close();


        AlertBean alertbean = new AlertBean();
        alertbean.setDate(bean.getSessionDay());
        alertbean.setEnable(1);
        alertbean.setEnd(bean.getEndTime());
        alertbean.setRelativeid(String.valueOf(bean.getSessionGroupId()));
        alertbean.setRepeatdistance("5");
        alertbean.setRepeattimes("0");
        alertbean.setRoom(mClassBean.getClassesCode());
        alertbean.setStart(bean.getStartTime());
        alertbean.setTitle(bean.getSessionName() + "#@#" + bean.getSessionName_En());
        alertbean.setType(AlertBean.TYPE_SESSTION);

        ada.open();

        ConferenceSetData.addAlert(ada, alertbean);
        String sql = "select * from " + ConferenceTables.TABLE_INCONGRESS_ALERT
                + " where "	+ ConferenceTableField.ALERT_RELATIVEID
                + " = " + bean.getSessionGroupId();
        List<AlertBean> beans = ConferenceGetData.getAlert(ada, sql);

        AlertBean alertBean = beans.size() > 0 ? beans.get(0) : null;

        ada.close();
        if (bean != null)
            AlermClock.addClock(alertBean);
    }

    private void deleteSessionAlert(SessionBean bean) {
        DbAdapter ada = DbAdapter.getInstance();
        ada.open();
        ConferenceSetData.updateSessionAttention(ada, bean.getSessionGroupId(), Constants.NOATTENTION);

        String sql ="select * from "+ ConferenceTables.TABLE_INCONGRESS_ALERT+
                " where "+ ConferenceTableField.ALERT_RELATIVEID +" = "
                + bean.getSessionGroupId()+" and "+ ConferenceTableField.ALERT_TYPE+" ="
                +AlertBean.TYPE_SESSTION;
        List<AlertBean> beans = ConferenceGetData.getAlert(ada, sql);
        AlertBean alertBean = beans.size()>0?beans.get(0):null;
        ada.close();
        if(bean!=null)
            AlermClock.disableClock(alertBean);
    }

    /**
     * 添加meeting闹钟
     * @param bean
     */
    private void addMeetingAlert(MeetingBean bean){
        DbAdapter ada = DbAdapter.getInstance();
        ada.open();
        ConferenceSetData.updateMeetingAttention(
                ada, bean.getMeetingId(),
                Constants.ATTENTION);
        ada.close();
        AlertBean alertbean = new AlertBean();
        alertbean.setDate(bean.getMeetingDay());
        alertbean.setEnable(1);
        alertbean.setEnd(bean.getEndTime());
        alertbean.setRelativeid(String.valueOf(bean.getMeetingId()));
        alertbean.setRepeatdistance("5");
        alertbean.setRepeattimes("0");
        alertbean.setRoom(mClassBean.getClassesCode());
        alertbean.setStart(bean.getStartTime());
        alertbean.setTitle(bean.getTopic() + "#@#" + bean.getTopic_En());
        alertbean.setType(AlertBean.TYPE_MEETING);
        ada.open();
        ConferenceSetData.addAlert(ada, alertbean);
        String sql ="select * from "+ ConferenceTables.TABLE_INCONGRESS_ALERT+" where "+ ConferenceTableField.ALERT_RELATIVEID +" = "+ bean.getMeetingId();
        List<AlertBean> beans = ConferenceGetData.getAlert(ada, sql);
        AlertBean alertBean = beans.size()>0?beans.get(0):null;
        ada.close();
        if(bean!=null)
            AlermClock.addClock(alertBean);
    }

    private void deleteMeetingAlert(MeetingBean bean) {
        DbAdapter ada = DbAdapter.getInstance();
        ada.open();
        ConferenceSetData.updateMeetingAttention(
                ada, bean.getMeetingId(),
                Constants.NOATTENTION);
        String sql ="select * from "+ ConferenceTables.TABLE_INCONGRESS_ALERT+
                " where "+ ConferenceTableField.ALERT_RELATIVEID +" = "
                + bean.getMeetingId()+" and "+ ConferenceTableField.ALERT_TYPE+" ="
                +AlertBean.TYPE_MEETING;
        List<AlertBean> beans = ConferenceGetData.getAlert(ada, sql);
        AlertBean alertBean = beans.size()>0?beans.get(0):null;
        ada.close();
        if(bean!=null)
            AlermClock.disableClock(alertBean);
    }
}
