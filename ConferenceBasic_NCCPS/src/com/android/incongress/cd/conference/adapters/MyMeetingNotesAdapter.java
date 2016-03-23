package com.android.incongress.cd.conference.adapters;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.incongress.cd.conference.base.AppApplication;
import com.android.incongress.cd.conference.data.ConferenceTableField;
import com.mobile.incongress.cd.conference.basic.csccm.R;
import com.android.incongress.cd.conference.beans.Notes;
import com.android.incongress.cd.conference.data.DbAdapter;
import com.android.incongress.cd.conference.data.ConferenceGetData;
import com.android.incongress.cd.conference.data.ConferenceSetData;
import com.android.incongress.cd.conference.data.ConferenceTables;
import com.android.incongress.cd.conference.fragments.me.NoteManageFragment;
import com.android.incongress.cd.conference.utils.CommonUtils;
import com.android.incongress.cd.conference.utils.SwipeListView;

public class MyMeetingNotesAdapter extends BaseAdapter {

    private List<Notes> datasource = new ArrayList<Notes>();
    private int mode;
    private SwipeListView mListView;
    private FragmentActivity mFragmentActivity;
    private Context mContext;

    public MyMeetingNotesAdapter(Context ctx, SwipeListView mListView,FragmentActivity mFragmentActivity) {
    	//发言
        String sql = "select * from "+ ConferenceTables.TABLE_INCONGRESS_Note +" where 1 = 1 order by "+ ConferenceTableField.NOTES_UPDATETIME+" desc";
        DbAdapter ada = DbAdapter.getInstance();
        ada.open();
        datasource = ConferenceGetData.getNoteList(ada, sql);
        mode = NoteManageFragment.MODE_NORMAL;
        ada.close();
        this.mListView = mListView;
        this.mFragmentActivity=mFragmentActivity;
        this.mContext = ctx;
    }

	public void setMode(int mode){
    	this.mode = mode;
    	notifyDataSetChanged();
    }
    
    @Override
    public int getCount() {
        return datasource.size();
    }

    @Override
    public Object getItem(int position) {
        return datasource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
            convertView = CommonUtils.initView(AppApplication.getContext(), R.layout.mycenter_note_child);
            holder = new Holder();
            TextView titleView = (TextView) convertView.findViewById(R.id.mycenter_note_child_title);
            TextView timeView = (TextView) convertView.findViewById(R.id.mycenter_note_child_time);
            TextView classview = (TextView) convertView.findViewById(R.id.mycenter_note_child_room);
            LinearLayout deleteview = (LinearLayout) convertView.findViewById(R.id.mycenter_note_cut);
            ImageView deleteImage = (ImageView) convertView.findViewById(R.id.mycenter_note_delete);
            holder.titleView = titleView;
            holder.timeView = timeView;
            holder.classView = classview;
            holder.deleteall = deleteview;
            holder.delete = deleteImage;
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        final Notes bean=datasource.get(position);
        holder.titleView.setText(bean.getTitle());
        holder.timeView.setText(CommonUtils.formatTimeYueRi(bean.getDate())+" "+bean.getStart()+"-"+bean.getEnd());
        holder.classView.setText(bean.getRoom());
        if (mode == NoteManageFragment.MODE_NORMAL) {
			holder.deleteall.setVisibility(View.GONE);
			holder.deleteall.setOnClickListener(null);
			holder.delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("提示信息").setMessage(R.string.mymeeting_note_delete_msg).setPositiveButton(R.string.positive_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteNote(bean);
                        }
                    }).setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).setCancelable(false).show();
				}
			});
		}else {
			holder.deleteall.setVisibility(View.VISIBLE);
			holder.deleteall.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("提示信息").setMessage(R.string.mymeeting_note_delete_msg).setPositiveButton(R.string.positive_button,  new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteNote(bean);
                        }
                    }).setNegativeButton(R.string.negative_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).setCancelable(false).show();
				}
			});
			holder.delete.setOnClickListener(null);
		}
        
        
        return convertView;
    }


    private class Holder {
        TextView titleView;
    	TextView timeView;
    	TextView classView;
    	LinearLayout deleteall;
    	ImageView delete;
    }
    
    private void deleteNote(Notes note){
    	DbAdapter dbAdapter = DbAdapter.getInstance();
    	dbAdapter.open();
    	ConferenceSetData.deleteNotes(dbAdapter, note);
    	dbAdapter.close();
    	datasource.remove(note);
    	mListView.closeOpenedItems();
		notifyDataSetChanged();
    }

}
