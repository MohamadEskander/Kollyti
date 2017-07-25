package com.example.isco.kolite.Groups;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.isco.kolite.NewsUi.News_Comment;
import com.example.isco.kolite.R;
import com.example.isco.kolite.adapter.GroupsAdapter;
import com.example.isco.kolite.adapter.postAdapter;
import com.example.isco.kolite.model.groupsviewmodel;
import com.example.isco.kolite.model.newviewmodel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;
import java.util.List;

public class ViewAllGroups extends Fragment {
    private RecyclerView mListView;
    private DatabaseReference mDatabase;
    private String uuid;
    private FirebaseAuth auth;
    private ArrayList<groupsviewmodel> arrGroupList ;
    private GroupsViewAdapter adapter;
    private DatabaseReference mDatabaseGUsers;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_group_floatbutton,container,false);
        mListView = (RecyclerView) view.findViewById(R.id.Groups_list);
        mListView.setHasFixedSize(true);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext() , Create_Group.class);
                startActivity(intent);
            }
        });

        auth = FirebaseAuth.getInstance();
        uuid = auth.getCurrentUser().getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Group");
        mDatabaseGUsers = FirebaseDatabase.getInstance().getReference().child("Group");
        mDatabase.keepSynced(true);
        mDatabaseGUsers.keepSynced(true);


        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mListView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        }
        else{
            mListView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        }

        return view;

    }
    //---------------------------------------------------------------------------------

    @Override
    public void onStart() {
        super.onStart();
        Log.v("startisco1" , "true");
        arrGroupList = new ArrayList<>();
        adapter = new GroupsViewAdapter(getContext(), arrGroupList);
        mListView.setAdapter(adapter);
        FillList();
    }
    public void FillList()
    {
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(final DataSnapshot data, String s) {
                mDatabaseGUsers.child(data.getKey()).child("gUsers").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(uuid))
                        {
                            groupsviewmodel groupsmodel = new groupsviewmodel();
                            groupsmodel.setgId(data.getKey());
                            groupsmodel.setgName(data.child("gName").getValue().toString());
                            groupsmodel.setgImg(data.child("gImg").getValue().toString());
                            arrGroupList.add(groupsmodel);
                            adapter.notifyItemInserted(adapter.getItemCount() -1);
                            mListView.scrollToPosition(adapter.getItemCount() -1);
                        }
                        else
                        {

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private class GroupsViewAdapter extends RecyclerView.Adapter<GroupsAdapter> {

        Context context ;
        List<groupsviewmodel> groupItemList;
        public GroupsViewAdapter(Context context  , List<groupsviewmodel> groupItemList)
        {
            this.context = context;
            this.groupItemList = groupItemList;
        }

        @Override
        public GroupsAdapter onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.groups_view_row, viewGroup , false);
            GroupsAdapter viewHolder = new GroupsAdapter(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(GroupsAdapter viewHolder, int position) {

            groupsviewmodel groupItem = groupItemList.get(position);
            final String Group_Key = groupItem.getgId();

                viewHolder.setImage(getContext(), groupItem.getgImg());
                viewHolder.setName(groupItem.getgName());

                viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext() , groups_view.class);
                        Bundle b = new Bundle();
                        b.putString("Group_Key" , Group_Key);
                        intent.putExtra("Group_Home" , b );
                        startActivity(intent);
                    }
                });

        }
           @Override
            public int getItemCount() {
            return (null != groupItemList ? groupItemList.size() : 0);
        }
    }

    //----------------------------------------------------------------------------------

    /**
     * Create fragment and pass bundle with data as its' arguments
     */
    public static  ViewAllGroups newInstance() {
        ViewAllGroups fragment = new ViewAllGroups();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ViewAllGroups() {
        /* Required empty public constructor*/
    }
}

