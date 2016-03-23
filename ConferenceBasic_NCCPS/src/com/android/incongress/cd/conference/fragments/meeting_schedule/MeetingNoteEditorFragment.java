package com.android.incongress.cd.conference.fragments.meeting_schedule;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.incongress.cd.conference.HomeActivity;
import com.android.incongress.cd.conference.base.BaseFragment;
import com.android.incongress.cd.conference.beans.ClassesBean;
import com.android.incongress.cd.conference.beans.MeetingBean;
import com.android.incongress.cd.conference.beans.Notes;
import com.android.incongress.cd.conference.beans.SessionBean;
import com.android.incongress.cd.conference.data.ConferenceSetData;
import com.android.incongress.cd.conference.data.DbAdapter;
import com.android.incongress.cd.conference.uis.IncongressEditText;
import com.android.incongress.cd.conference.utils.ToastUtils;
import com.mobile.incongress.cd.conference.basic.csccm.R;

/**
 * 记笔记界面
 */
public class MeetingNoteEditorFragment extends BaseFragment implements OnClickListener {

    private IncongressEditText mIncongressEditText;
    private SessionBean mSessionBean;
    private ClassesBean mClassesBean;
    private Notes mNotes;

    private int mType = TYPE_NEW;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mycenter_note_editor, null);
        mIncongressEditText = (IncongressEditText) view.findViewById(R.id.mycenter_note_edior);
        if (mType == TYPE_UPDATE) {
            mIncongressEditText.setText(mNotes.getContents());
        }
        mIncongressEditText.requestFocus();
        toggleShurufa();
        return view;

    }

    @Override
    public void onClick(View v) {
        if (mType == TYPE_NEW) {
            String content = mIncongressEditText.getText().toString().trim();
            if (content == null || "".equals(content)) {
                Toast.makeText(getActivity(), R.string.metting_node_edit_no_data, Toast.LENGTH_SHORT).show();
                return;
            }
            String time = String.valueOf(System.currentTimeMillis());
            mNotes = new Notes();
            mNotes.setContents(content);
            mNotes.setCreatetime(time);
            mNotes.setDate(mSessionBean.getSessionDay());
            mNotes.setEnd(mSessionBean.getEndTime());
            mNotes.setClassid(String.valueOf(mClassesBean.getClassesId()));
            mNotes.setMeetingid(String.valueOf(mSessionBean.getSessionGroupId()));
            mNotes.setRoom(mClassesBean.getClassesCode());
            mNotes.setSessionid(String.valueOf(mSessionBean.getSessionGroupId()));
            mNotes.setStart(mSessionBean.getStartTime());
            mNotes.setTitle(mSessionBean.getSessionName());
            mNotes.setUpdatetime(time);
            DbAdapter db = DbAdapter.getInstance();
            db.open();
            ConferenceSetData.addNotes(db, mNotes);
            db.close();
        } else {
            mNotes.setUpdatetime(String.valueOf(System.currentTimeMillis()));
            mNotes.setContents(mIncongressEditText.getText().toString());
            DbAdapter dbAdapter = DbAdapter.getInstance();
            dbAdapter.open();
            ConferenceSetData.updateNotes(dbAdapter, mNotes);
            dbAdapter.close();
        }

        HomeActivity activity = (HomeActivity) getActivity();
        activity.perfromBackPressTitleEntry();
        activity.hideShurufa();
        ToastUtils.showShorToast(getString(R.string.note_save_success));
    }

    public static final String BUNDLE_TYPE = "type";
    public static final String BUNDLE_SESSION = "session";
    public static final String BUNDLE_CLASS = "class";
    public static final String BUNDLE_NOTE = "note";


    public static final int TYPE_NEW = 0;
    public static final int TYPE_UPDATE = 1;

    public static MeetingNoteEditorFragment getInstance(int type, SessionBean sessionBean, ClassesBean classesBean,Notes notes) {
        MeetingNoteEditorFragment editorFragment = new MeetingNoteEditorFragment();
        Bundle bundle = new Bundle();

        bundle.putInt(BUNDLE_TYPE, type);
        bundle.putSerializable(BUNDLE_SESSION, sessionBean);
        bundle.putSerializable(BUNDLE_CLASS, classesBean);
        bundle.putSerializable(BUNDLE_NOTE, notes);

        editorFragment.setArguments(bundle);
        return editorFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mType = getArguments().getInt(BUNDLE_TYPE);
        if (mType == TYPE_NEW) {
            mSessionBean = (SessionBean) getArguments().getSerializable(BUNDLE_SESSION);
            mClassesBean = (ClassesBean) getArguments().getSerializable(BUNDLE_CLASS);
        } else if (mType == TYPE_UPDATE) {
            mNotes = (Notes) getArguments().getSerializable(BUNDLE_NOTE);
        }
    }

    public void setRightView(View view) {
        view.setOnClickListener(this);
    }
}
