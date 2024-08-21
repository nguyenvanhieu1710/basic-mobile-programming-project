package com.example.basicmobileprogramingproject.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.basicmobileprogramingproject.Activity.Fragment.CartFragment;
import com.example.basicmobileprogramingproject.Activity.Fragment.CategoryManagementFragment;
import com.example.basicmobileprogramingproject.Activity.Fragment.HomeFragment;
import com.example.basicmobileprogramingproject.Activity.Fragment.LoginFragment;
import com.example.basicmobileprogramingproject.Activity.Fragment.MessageFragment;
import com.example.basicmobileprogramingproject.Activity.Fragment.OrderFragment;
import com.example.basicmobileprogramingproject.Activity.Fragment.PayFragment;
import com.example.basicmobileprogramingproject.Activity.Fragment.ProductManagementFragment;
import com.example.basicmobileprogramingproject.Activity.Fragment.ProfileFragment;
import com.example.basicmobileprogramingproject.Activity.Fragment.RegisterFragment;
import com.example.basicmobileprogramingproject.Activity.Fragment.StaffManagementFragment;
import com.example.basicmobileprogramingproject.Activity.Fragment.StatisticalFragment;
import com.example.basicmobileprogramingproject.Activity.Fragment.SupplierFragment;
import com.example.basicmobileprogramingproject.Activity.Fragment.UserManagementFragment;
import com.example.basicmobileprogramingproject.Activity.Fragment.VoucherManagementFragment;
import com.example.basicmobileprogramingproject.Entity.AccountEntity;
import com.example.basicmobileprogramingproject.Model.AccountModel;
import com.example.basicmobileprogramingproject.R;
import com.example.basicmobileprogramingproject.Utils.AlertDialogUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_PRODUCT_MANAGEMENT = 1;
    private static final int FRAGMENT_CATEGORY_MANAGEMENT = 2;
    private static final int FRAGMENT_USER_MANAGEMENT = 3;
    private static final int FRAGMENT_CART = 4;
    private static final int FRAGMENT_LOGIN = 5;
    private static final int FRAGMENT_REGISTER = 6;
    private static final int FRAGMENT_MESSAGE = 7;
    private static final int FRAGMENT_ORDER = 8;
    private static final int FRAGMENT_SUPPLIER = 9;
    private static final int FRAGMENT_PROFILE = 10;
    private static final int FRAGMENT_STAFF_MANAGEMENT = 11;
    private static final int FRAGMENT_STATISTICAL = 12;
    private static final int FRAGMENT_VOUCHER_MANAGEMENT = 13;

    private int currentFragment = FRAGMENT_HOME;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    ImageView imageViewUserImage;
    TextView textViewUserName, textViewEmail;
    AccountEntity accountEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // mapping id
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        // use toolbar
        setSupportActionBar(toolbar);
        // use toggle display navigation view
        ActionBarDrawerToggle toggle = new
                ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        // when click item of navigation view
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        handleBottomNavigationView();

        handleChangeNavigationViewWhenLogin();

        defaultFragment();

        accountEntity = new AccountEntity(this);
        AccountModel accountModel = new AccountModel();
        accountModel.AccountName = "admin";
        accountModel.Password = "admin";
        accountModel.Role = "Admin";
        if (accountEntity.checkExistAccount(accountModel)) {
//            AlertDialogUtils.showInfoDialog(this, "Admin account already exist");
        } else {
            if (accountEntity.insertAccount(accountModel)) {
//                AlertDialogUtils.showSuccessDialog(this, "Add admin account successfully");
            }
        }
    }

    public void defaultFragment() {
        replaceFragment(new HomeFragment());
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
        bottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
        setTitleToolbar();
        handleNavigationView("Guest");
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.nav_cart || id == R.id.nav_profile || id == R.id.nav_order) {
            if (!checkAccountOnline()) {
                return false;
            }
        }
        if (id == R.id.nav_home) {
            if (currentFragment != FRAGMENT_HOME) {
                replaceFragment(new HomeFragment());
                bottomNavigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
                currentFragment = FRAGMENT_HOME;
            }
        } else if (id == R.id.nav_product) {
            if (currentFragment != FRAGMENT_PRODUCT_MANAGEMENT) {
                replaceFragment(new ProductManagementFragment());
//                bottomNavigationView.getMenu().findItem(R.id.nav_product).setChecked(true);
                currentFragment = FRAGMENT_PRODUCT_MANAGEMENT;
            }
        } else if (id == R.id.nav_category) {
            if (currentFragment != FRAGMENT_CATEGORY_MANAGEMENT) {
                replaceFragment(new CategoryManagementFragment());
                bottomNavigationView.getMenu().findItem(R.id.nav_category).setChecked(true);
                currentFragment = FRAGMENT_CATEGORY_MANAGEMENT;
            }
        } else if (id == R.id.nav_user) {
            if (currentFragment != FRAGMENT_USER_MANAGEMENT) {
                replaceFragment(new UserManagementFragment());
                bottomNavigationView.getMenu().findItem(R.id.nav_user).setChecked(true);
                currentFragment = FRAGMENT_USER_MANAGEMENT;
            }
        } else if (id == R.id.nav_cart) {
            checkAccountOnline();
            if (currentFragment != FRAGMENT_CART) {
                replaceFragment(new CartFragment());
                bottomNavigationView.getMenu().findItem(R.id.nav_cart).setChecked(true);
                currentFragment = FRAGMENT_CART;
            }
        } else if (id == R.id.nav_login) {
            if (currentFragment != FRAGMENT_LOGIN) {
                replaceFragment(new LoginFragment());
//                bottomNavigationView.getMenu().findItem(R.id.nav_login).setChecked(true);
                currentFragment = FRAGMENT_LOGIN;
            }
        } else if (id == R.id.nav_register) {
            if (currentFragment != FRAGMENT_REGISTER) {
                replaceFragment(new RegisterFragment());
//                bottomNavigationView.getMenu().findItem(R.id.nav_register).setChecked(true);
                currentFragment = FRAGMENT_REGISTER;
            }
        } else if (id == R.id.nav_order) {
            checkAccountOnline();
            if (currentFragment != FRAGMENT_ORDER) {
                replaceFragment(new OrderFragment());
//                bottomNavigationView.getMenu().findItem(R.id.nav_register).setChecked(true);
                currentFragment = FRAGMENT_ORDER;
            }
        } else if (id == R.id.nav_supplier) {
            if (currentFragment != FRAGMENT_SUPPLIER) {
                replaceFragment(new SupplierFragment());
//                bottomNavigationView.getMenu().findItem(R.id.nav_register).setChecked(true);
                currentFragment = FRAGMENT_SUPPLIER;
            }
        } else if (id == R.id.nav_profile) {
            if(checkAccountOnline()){
                if (currentFragment != FRAGMENT_PROFILE) {
                    replaceFragment(new ProfileFragment());
//                bottomNavigationView.getMenu().findItem(R.id.nav_register).setChecked(true);
                    currentFragment = FRAGMENT_PROFILE;
                }
            }
        } else if (id == R.id.nav_staff) {
            if (currentFragment != FRAGMENT_STAFF_MANAGEMENT) {
                replaceFragment(new StaffManagementFragment());
//                bottomNavigationView.getMenu().findItem(R.id.nav_register).setChecked(true);
                currentFragment = FRAGMENT_STAFF_MANAGEMENT;
            }
        } else if (id == R.id.nav_statistical) {
            if (currentFragment != FRAGMENT_STATISTICAL) {
                replaceFragment(new StatisticalFragment());
//                bottomNavigationView.getMenu().findItem(R.id.nav_register).setChecked(true);
                currentFragment = FRAGMENT_STATISTICAL;
            }
        } else if (id == R.id.nav_voucher) {
            if (currentFragment != FRAGMENT_VOUCHER_MANAGEMENT) {
                replaceFragment(new VoucherManagementFragment());
//                bottomNavigationView.getMenu().findItem(R.id.nav_register).setChecked(true);
                currentFragment = FRAGMENT_VOUCHER_MANAGEMENT;
            }
        } else {
            if(accountEntity.DisableAllOnlineAccounts()){
                AlertDialogUtils.showSuccessDialog(this, "Logout successfully");
                replaceFragment(new HomeFragment());
                navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
                currentFragment = FRAGMENT_HOME;
            }
            else {
                AlertDialogUtils.showErrorDialog(this, "Logout failed");
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        setTitleToolbar();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.chat) {
            AlertDialogUtils.showQuestionDialog(this, "Function is updating. Do you want to continue?",
                    (dialog, which) -> {
                        replaceFragment(new MessageFragment());
                        setTitleToolbar();
                    },
                    (dialog, which) -> {
                    });
        }
        return true;
    }

    public boolean checkAccountOnline() {
        if (!accountEntity.checkAccountActive()) {
            AlertDialogUtils.showErrorDialog(this, "Please login to continue");
            return false;
        }
        else {
            return true;
        }
    }

    public void setTitleToolbar() {
        String title = "";
        if (currentFragment == FRAGMENT_HOME) {
            title = getString(R.string.menu_home);
        } else if (currentFragment == FRAGMENT_PRODUCT_MANAGEMENT) {
            title = getString(R.string.menu_Product);
        } else if (currentFragment == FRAGMENT_CATEGORY_MANAGEMENT) {
            title = getString(R.string.menu_category);
        } else if (currentFragment == FRAGMENT_USER_MANAGEMENT) {
            title = "User Management";
        } else if (currentFragment == FRAGMENT_CART) {
            title = "Cart";
        } else if (currentFragment == FRAGMENT_LOGIN) {
            title = "Login";
        } else if (currentFragment == FRAGMENT_REGISTER) {
            title = "Register";
        } else if (currentFragment == FRAGMENT_MESSAGE) {
            title = "Message";
        } else if (currentFragment == FRAGMENT_ORDER) {
            title = "Order";
        } else if (currentFragment == FRAGMENT_SUPPLIER) {
            title = "Supplier";
        } else if (currentFragment == FRAGMENT_PROFILE) {
            title = "Profile";
        } else if (currentFragment == FRAGMENT_STAFF_MANAGEMENT) {
            title = "Staff Management";
        } else if (currentFragment == FRAGMENT_STATISTICAL) {
            title = "Statistical";
        } else if (currentFragment == FRAGMENT_VOUCHER_MANAGEMENT) {
            title = "Voucher Management";
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
    }

    public void handleBottomNavigationView() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_cart || id == R.id.nav_profile || id == R.id.nav_order) {
                    if (!checkAccountOnline()) {
                        return false;
                    }
                }
                if (id == R.id.nav_home) {
                    if (currentFragment != FRAGMENT_HOME) {
                        replaceFragment(new HomeFragment());
                        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
                        currentFragment = FRAGMENT_HOME;
                    }
                } else if (id == R.id.nav_product) {
                    if (currentFragment != FRAGMENT_PRODUCT_MANAGEMENT) {
                        replaceFragment(new ProductManagementFragment());
                        navigationView.getMenu().findItem(R.id.nav_product).setChecked(true);
                        currentFragment = FRAGMENT_PRODUCT_MANAGEMENT;
                    }
                } else if (id == R.id.nav_category) {
                    if (currentFragment != FRAGMENT_CATEGORY_MANAGEMENT) {
                        replaceFragment(new CategoryManagementFragment());
                        navigationView.getMenu().findItem(R.id.nav_category).setChecked(true);
                        currentFragment = FRAGMENT_CATEGORY_MANAGEMENT;
                    }
                } else if (id == R.id.nav_user) {
                    if (currentFragment != FRAGMENT_USER_MANAGEMENT) {
                        replaceFragment(new UserManagementFragment());
                        navigationView.getMenu().findItem(R.id.nav_user).setChecked(true);
                        currentFragment = FRAGMENT_USER_MANAGEMENT;
                    }
                } else if (id == R.id.nav_cart) {
                    if (currentFragment != FRAGMENT_CART) {
                        replaceFragment(new CartFragment());
                        navigationView.getMenu().findItem(R.id.nav_cart).setChecked(true);
                        currentFragment = FRAGMENT_CART;
                    }
                } else if (id == R.id.nav_profile) {
                    if (currentFragment != FRAGMENT_PROFILE) {
                        replaceFragment(new ProfileFragment());
                        navigationView.getMenu().findItem(R.id.nav_profile).setChecked(true);
                        currentFragment = FRAGMENT_PROFILE;
                    }
                } else if (id == R.id.nav_login) {
                    if (currentFragment != FRAGMENT_LOGIN) {
                        replaceFragment(new LoginFragment());
                        navigationView.getMenu().findItem(R.id.nav_login).setChecked(true);
                        currentFragment = FRAGMENT_LOGIN;
                    }
                } else if (id == R.id.nav_order) {
                    if (currentFragment != FRAGMENT_ORDER) {
                        replaceFragment(new OrderFragment());
                        navigationView.getMenu().findItem(R.id.nav_order).setChecked(true);
                        currentFragment = FRAGMENT_ORDER;
                    }
                } else {
                }
                setTitleToolbar();
                return true;
            }
        });
    }

    public void handleChangeNavigationViewWhenLogin() {
        getSupportFragmentManager().setFragmentResultListener("requestKeyOfUser", this, (requestKey, result) -> {
            boolean updateNavMenuOfUser = result.getBoolean("updateNavMenuOfUser");
            String username = result.getString("username");
            String email = result.getString("email");
            if (updateNavMenuOfUser) {
                // part of menu
                handleNavigationView("User");
                // part of header
                View headerView = navigationView.getHeaderView(0);
                imageViewUserImage = headerView.findViewById(R.id.imageViewUserImage);
                textViewUserName = headerView.findViewById(R.id.textViewUserName);
                textViewEmail = headerView.findViewById(R.id.textViewEmail);
                imageViewUserImage.setImageResource(R.drawable.icon_user_white);
                textViewUserName.setText(username);
                textViewEmail.setText(email);
                // part bottom navigation
                bottomNavigationView.getMenu().clear();
                bottomNavigationView.inflateMenu(R.menu.bottom_navigation_for_user);
            }
        });
        getSupportFragmentManager().setFragmentResultListener("requestKeyOfStaff", this, (requestKey, result) -> {
            boolean updateNavMenuOfStaff = result.getBoolean("updateNavMenuOfStaff");
            String username = result.getString("username");
            String email = result.getString("email");
            if (updateNavMenuOfStaff) {
                // part of menu
                handleNavigationView("Staff");
                // part of header
                View headerView = navigationView.getHeaderView(0);
                imageViewUserImage = headerView.findViewById(R.id.imageViewUserImage);
                imageViewUserImage.setImageResource(R.drawable.icon_user_white);
                textViewUserName = headerView.findViewById(R.id.textViewUserName);
                textViewUserName.setText(username);
                textViewEmail = headerView.findViewById(R.id.textViewEmail);
                textViewEmail.setText(email);
                // part bottom navigation
                bottomNavigationView.getMenu().clear();
                bottomNavigationView.inflateMenu(R.menu.bottom_navigation_for_staff);
            }
        });
        getSupportFragmentManager().setFragmentResultListener("requestKeyOfAdmin", this, (requestKey, result) -> {
            boolean updateNavMenuOfAdmin = result.getBoolean("updateNavMenuOfAdmin");
            if (updateNavMenuOfAdmin) {
                // part of menu
                handleNavigationView("Admin");
                // part of header
                View headerView = navigationView.getHeaderView(0);
                imageViewUserImage = headerView.findViewById(R.id.imageViewUserImage);
                imageViewUserImage.setImageResource(R.drawable.icon_user_white);
                textViewUserName = headerView.findViewById(R.id.textViewUserName);
                textViewUserName.setText("Admin");
                textViewEmail = headerView.findViewById(R.id.textViewEmail);
                textViewEmail.setText("admin@gmail.com");
                // part bottom navigation
                bottomNavigationView.getMenu().clear();
                bottomNavigationView.inflateMenu(R.menu.bottom_navigation_for_staff);
            }
        });

    }

    public void handleNavigationView(String role) {
        Menu navMenu = navigationView.getMenu();
        MenuItem item_home = navMenu.findItem(R.id.nav_home);
        MenuItem item_category = navMenu.findItem(R.id.nav_category);
        MenuItem item_product = navMenu.findItem(R.id.nav_product);
        MenuItem item_cart = navMenu.findItem(R.id.nav_cart);
        MenuItem item_statistical = navMenu.findItem(R.id.nav_statistical);
        MenuItem item_order = navMenu.findItem(R.id.nav_order);
        MenuItem item_user = navMenu.findItem(R.id.nav_user);
        MenuItem item_staff = navMenu.findItem(R.id.nav_staff);
        MenuItem item_login = navMenu.findItem(R.id.nav_login);
        MenuItem item_register = navMenu.findItem(R.id.nav_register);
        MenuItem item_voucher = navMenu.findItem(R.id.nav_voucher);
        MenuItem item_profile = navMenu.findItem(R.id.nav_profile);
        MenuItem item_supplier = navMenu.findItem(R.id.nav_supplier);

        if (role.equals("Guest") || role.equals("User")) {
            item_home.setVisible(true);
            item_category.setVisible(false);
            item_product.setVisible(false);
            item_cart.setVisible(true);
            item_statistical.setVisible(false);
            item_order.setVisible(true);
            item_user.setVisible(false);
            item_staff.setVisible(false);
            item_login.setVisible(true);
            item_register.setVisible(true);
            item_voucher.setVisible(false);
            item_profile.setVisible(true);
            item_supplier.setVisible(false);

            item_home.setChecked(true);
        }
        if (role.equals("Staff")) {
            item_home.setVisible(true);
            item_category.setVisible(true);
            item_product.setVisible(true);
            item_cart.setVisible(false);
            item_statistical.setVisible(true);
            item_order.setVisible(true);
            item_user.setVisible(false);
            item_staff.setVisible(false);
            item_login.setVisible(true);
            item_register.setVisible(true);
            item_voucher.setVisible(true);
            item_profile.setVisible(true);
            item_supplier.setVisible(true);

            item_home.setChecked(true);

        }
        if (role.equals("Admin")) {
            item_home.setVisible(true);
            item_category.setVisible(true);
            item_product.setVisible(true);
            item_cart.setVisible(false);
            item_statistical.setVisible(true);
            item_order.setVisible(false);
            item_user.setVisible(true);
            item_staff.setVisible(true);
            item_login.setVisible(true);
            item_register.setVisible(true);
            item_voucher.setVisible(true);
            item_profile.setVisible(true);
            item_supplier.setVisible(true);

            item_home.setChecked(true);
        }
    }
}