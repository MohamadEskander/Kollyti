package com.example.isco.kolite;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.isco.kolite.Groups.ViewAllGroups;
import com.example.isco.kolite.NewsUi.NewsView;
import com.example.isco.kolite.R;
import com.example.isco.kolite.model.News2view;
import com.example.isco.kolite.tabs.Friends;
import com.example.isco.kolite.tabs.Home;
import com.example.isco.kolite.tabs.Message;
import com.example.isco.kolite.tabs.Notification;
import com.example.isco.kolite.tabs.Result;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

public class MainActivity extends AppCompatActivity implements MaterialTabListener {

    Fragment fragment = null;

    private MaterialTabHost tabHost;
    private static ViewPager viewPager;
    private ViewPagerAdapter pagerAdapter;

    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabHost      = (MaterialTabHost) this.findViewById(R.id.materialTabHost);
        viewPager    = (ViewPager) this.findViewById(R.id.pager);
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        auth = FirebaseAuth.getInstance();

        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // when user do a swipe the selected tab change
                tabHost.setSelectedNavigationItem(position);
            }
        });
        // insert all tabs from pagerAdapter data
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            //.setText(pagerAdapter.getPageTitle(i))
                            .setIcon(pagerAdapter.getIcon(i))
                            .setTabListener(this)
            );
        }

        viewPager.setCurrentItem(2);
//--------------------------------------------------------------------
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (auth.getCurrentUser() == null) {
                    Intent LogoutIntent = new Intent(MainActivity.this, Login.class  );
                    LogoutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(LogoutIntent);
                }

            }
        };
        auth.addAuthStateListener(mAuthListener);
    }


    //-------> Pager Methods
    @Override
    public void onTabSelected(MaterialTab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }
    @Override
    public void onTabReselected(MaterialTab tab) {
    }
    @Override
    public void onTabUnselected(MaterialTab tab) {

    }


    //-------> Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            auth.signOut();
        }

        return super.onOptionsItemSelected(item);
    }

//--------------------------------------------------------------------------------------------------
    //---------------------- ViewPager Adapter Class

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {

        int icons[] = {R.mipmap.result, R.mipmap.friends, R.mipmap.home, R.mipmap.notification, R.mipmap.messagee};

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public Fragment getItem(int position) {
            /**
             * Set fragment to different fragments depending on position in ViewPager
             */
            switch (position) {
                case 0:
                    fragment = Result.newInstance();
                    break;
                case 1:
                    fragment = ViewAllGroups.newInstance();
                    break;
                case 2:
                    fragment = NewsView.newInstance();
                    break;
                case 3:
                    fragment = Notification.newInstance();
                    break;
                case 4:
                    fragment = Message.newInstance();
                    break;
                default:
                    fragment = NewsView.newInstance();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 5;
        }

        private Drawable getIcon(int position) {
            return getResources().getDrawable(icons[position]);
        }


    }   //---> End of viewPager Adapter Class
}      //---> End of MainClass