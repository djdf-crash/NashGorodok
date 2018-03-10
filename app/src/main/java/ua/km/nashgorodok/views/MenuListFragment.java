package ua.km.nashgorodok.views;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import ua.km.nashgorodok.R;
import ua.km.nashgorodok.utils.CircleTransformation;
import ua.km.nashgorodok.viewmodels.MainActivityViewModel;


public class MenuListFragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView ivMenuUserProfilePhoto;
    private int selectedItem;
    private MainActivityViewModel viewModel;


    public static MenuListFragment newInstance (int selectedItem){
        MenuListFragment menuListFragment = new MenuListFragment();
        menuListFragment.selectedItem = selectedItem;
        return menuListFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu, container,false);

        final NavigationView vNavigation = view.findViewById(R.id.vNavigation);

        ivMenuUserProfilePhoto = vNavigation.inflateHeaderView(R.layout.view_global_menu_header).findViewById(R.id.ivMenuUserProfilePhoto);

        vNavigation.setNavigationItemSelectedListener(this);

        setupHeader();

        viewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);


        return view ;
    }

    private void setupHeader() {
        int avatarSize = getResources().getDimensionPixelSize(R.dimen.global_menu_avatar_size);
        String profilePhoto = getResources().getString(R.string.user_profile_photo);
        Picasso.with(getActivity())
                .load(profilePhoto)
                .placeholder(R.drawable.img_circle_placeholder)
                .resize(avatarSize, avatarSize)
                .centerCrop()
                .transform(new CircleTransformation())
                .into(ivMenuUserProfilePhoto);
    }

    public int getSelectedItem() {
        return selectedItem;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        selectedItem = item.getItemId();
        Toast.makeText(getActivity(),item.getTitle(),Toast.LENGTH_SHORT).show();
        viewModel.getItemMenuSelected().setValue(selectedItem);
        getActivity().onBackPressed();
        return false;
    }
}

