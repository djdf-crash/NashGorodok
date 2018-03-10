package ua.km.nashgorodok.views;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;

import java.util.ArrayList;
import java.util.List;

import ua.km.nashgorodok.R;
import ua.km.nashgorodok.adapters.FeedsAdapter;
import ua.km.nashgorodok.database.DatabaseUtil;
import ua.km.nashgorodok.database.models.Feed;
import ua.km.nashgorodok.viewmodels.MainActivityViewModel;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvFeed;
    private FlowingDrawer mDrawer;
    private MainActivityViewModel viewModel;
    private FeedsAdapter feedsAdapter;
    private SwipeRefreshLayout refreshLayout;

    private String selectCategory = "";
    private String previewSelectCategory = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //UtilJobScheduler.scheduleJob(getApplicationContext());

        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        setupRefreshLayout();

        mDrawer = findViewById(R.id.drawerlayout);
        mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL);

        setupToolbar();
        setupMenu();
        setupFeed();

    }

    private void setupRefreshLayout() {
        refreshLayout = findViewById(R.id.swipe_layout);
        refreshLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light));

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Integer selectedItemId = viewModel.getItemMenuSelected().getValue();
                if (selectedItemId == null) {
                    selectedItemId = R.id.menu_news;
                }
                List<Feed> updateList = feedsAdapter.getFeedList();
                boolean isChanged = feedsAdapter.isChanged();
                if (isChanged || !updateList.isEmpty()) {
                    viewModel.updateFeedsAfterChangeCategory(updateList);
                }
                onNavigationItemSelected(selectedItemId, true);
            }
        });

        viewModel.getRefreshing().observe(MainActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean refreshing) {
                refreshLayout.setRefreshing(refreshing);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isMenuVisible()) {
            mDrawer.closeMenu();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        viewModel.clearDb();
        return true;
    }

    private void setupMenu() {
        FragmentManager fm = getSupportFragmentManager();
        MenuListFragment mMenuFragment = (MenuListFragment) fm.findFragmentById(R.id.id_container_menu);
        if (mMenuFragment == null) {
            mMenuFragment = MenuListFragment.newInstance(R.id.menu_news);
            fm.beginTransaction().add(R.id.id_container_menu, mMenuFragment).commit();
            onNavigationItemSelected(mMenuFragment.getSelectedItem(), false);
        }

        viewModel.getItemMenuSelected().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer itemId) {
                List<Feed> updateList = feedsAdapter.getFeedList();
                boolean isChanged = feedsAdapter.isChanged();
                if (isChanged) {
                    viewModel.updateFeedsAfterChangeCategory(updateList);
                }
                onNavigationItemSelected(itemId, false);
            }
        });
    }

    private void onNavigationItemSelected(Integer itemId, boolean isRefresh){
        String url = "";
        previewSelectCategory = selectCategory;
        selectCategory = "";
        switch (itemId){
            case R.id.menu_news:
                url = "http://nashgorodok.km.ua/index.php/news?format=feed&type=rss";
                selectCategory = "Новини";
                break;
            case R.id.menu_history:
                url = "http://nashgorodok.km.ua/index.php/history?format=feed&type=rss";
                selectCategory = "Історія";
                break;
            case R.id.menu_culture:
                url = "http://nashgorodok.km.ua/index.php/kultura?format=feed&type=rss";
                selectCategory = "Культура";
                break;
            case R.id.menu_sport:
                url = "http://nashgorodok.km.ua/index.php/sport?format=feed&type=rss";
                selectCategory = "Спорт";
                break;
            case R.id.menu_school:
                url = "http://nashgorodok.km.ua/index.php/shkoly?format=feed&type=rss";
                selectCategory = "Школи";
                break;
        }
        if (!TextUtils.isEmpty(url)){

            viewModel.getListLiveData(selectCategory).observe(MainActivity.this, new Observer<List<Feed>>() {
                @Override
                public void onChanged(@Nullable List<Feed> feeds) {
                    if (feeds != null) {
                        feedsAdapter.setFeedList(feeds);
                        runLayoutAnimation();
                    }
                }
            });

            if (!TextUtils.equals(selectCategory, previewSelectCategory)) {
                viewModel.getListLiveData(selectCategory).observe(MainActivity.this, new Observer<List<Feed>>() {
                    @Override
                    public void onChanged(@Nullable List<Feed> feeds) {
                        if (feeds != null) {
                            feedsAdapter.setFeedList(feeds);
                            runLayoutAnimation();
                        }
                    }
                });
            }

            if (isRefresh) viewModel.refreshFeeds(url);

            getSupportActionBar().setTitle(selectCategory);
        }
    }

    private void runLayoutAnimation() {
        final Context context = rvFeed.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

        rvFeed.setLayoutAnimation(controller);
        rvFeed.getAdapter().notifyDataSetChanged();
        rvFeed.scheduleLayoutAnimation();
    }

    protected void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawer.toggleMenu();
            }
        });
    }

    protected int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void setupFeed() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };

        rvFeed = findViewById(R.id.rvFeed);
        rvFeed.setHasFixedSize(true);

        rvFeed.setLayoutManager(linearLayoutManager);

        feedsAdapter = new FeedsAdapter();

        feedsAdapter.setFeedList(new ArrayList<Feed>());

        rvFeed.setAdapter(feedsAdapter);

//        rvFeed.addOnScrollListener(new RecyclerView.OnScrollListener(){
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                int topRowVerticalPosition =
//                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
//                refreshLayout.setEnabled(topRowVerticalPosition >= 0);
//
//            }
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//        });

    }

}
