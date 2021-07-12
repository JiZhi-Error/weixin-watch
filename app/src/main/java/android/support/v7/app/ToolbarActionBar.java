/*
 * Decompiled with CFR 0.151.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.res.Configuration
 *  android.content.res.Resources$Theme
 *  android.graphics.drawable.Drawable
 *  android.util.TypedValue
 *  android.view.ContextThemeWrapper
 *  android.view.KeyCharacterMap
 *  android.view.KeyEvent
 *  android.view.LayoutInflater
 *  android.view.Menu
 *  android.view.MenuItem
 *  android.view.View
 *  android.view.ViewGroup
 *  android.view.ViewGroup$LayoutParams
 *  android.view.Window$Callback
 *  android.widget.SpinnerAdapter
 */
package android.support.v7.app;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.NavItemSelectedListener;
import android.support.v7.appcompat.R;
import android.support.v7.view.WindowCallbackWrapper;
import android.support.v7.view.menu.ListMenuPresenter;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPresenter;
import android.support.v7.widget.DecorToolbar;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.ToolbarWidgetWrapper;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.SpinnerAdapter;
import java.util.ArrayList;

class ToolbarActionBar
extends ActionBar {
    private DecorToolbar mDecorToolbar;
    private boolean mLastMenuVisibility;
    private ListMenuPresenter mListMenuPresenter;
    private boolean mMenuCallbackSet;
    private final Toolbar.OnMenuItemClickListener mMenuClicker;
    private final Runnable mMenuInvalidator;
    private ArrayList<ActionBar.OnMenuVisibilityListener> mMenuVisibilityListeners = new ArrayList();
    private boolean mToolbarMenuPrepared;
    private Window.Callback mWindowCallback;

    public ToolbarActionBar(Toolbar toolbar, CharSequence charSequence, Window.Callback callback) {
        this.mMenuInvalidator = new Runnable(){

            @Override
            public void run() {
                ToolbarActionBar.this.populateOptionsMenu();
            }
        };
        this.mMenuClicker = new Toolbar.OnMenuItemClickListener(){

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                return ToolbarActionBar.this.mWindowCallback.onMenuItemSelected(0, menuItem);
            }
        };
        this.mDecorToolbar = new ToolbarWidgetWrapper(toolbar, false);
        this.mWindowCallback = new ToolbarCallbackWrapper(callback);
        this.mDecorToolbar.setWindowCallback(this.mWindowCallback);
        toolbar.setOnMenuItemClickListener(this.mMenuClicker);
        this.mDecorToolbar.setWindowTitle(charSequence);
    }

    static /* synthetic */ boolean access$202(ToolbarActionBar toolbarActionBar, boolean bl2) {
        toolbarActionBar.mToolbarMenuPrepared = bl2;
        return bl2;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void ensureListMenuPresenter(Menu menu) {
        if (this.mListMenuPresenter == null && menu instanceof MenuBuilder) {
            menu = (MenuBuilder)menu;
            Context context = this.mDecorToolbar.getContext();
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = context.getResources().newTheme();
            theme.setTo(context.getTheme());
            theme.resolveAttribute(R.attr.actionBarPopupTheme, typedValue, true);
            if (typedValue.resourceId != 0) {
                theme.applyStyle(typedValue.resourceId, true);
            }
            theme.resolveAttribute(R.attr.panelMenuListTheme, typedValue, true);
            if (typedValue.resourceId != 0) {
                theme.applyStyle(typedValue.resourceId, true);
            } else {
                theme.applyStyle(R.style.Theme_AppCompat_CompactMenu, true);
            }
            context = new ContextThemeWrapper(context, 0);
            context.getTheme().setTo(theme);
            this.mListMenuPresenter = new ListMenuPresenter(context, R.layout.abc_list_menu_item_layout);
            this.mListMenuPresenter.setCallback(new PanelMenuPresenterCallback());
            menu.addMenuPresenter(this.mListMenuPresenter);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private View getListMenuView(Menu menu) {
        this.ensureListMenuPresenter(menu);
        if (menu == null || this.mListMenuPresenter == null || this.mListMenuPresenter.getAdapter().getCount() <= 0) {
            return null;
        }
        return (View)this.mListMenuPresenter.getMenuView(this.mDecorToolbar.getViewGroup());
    }

    private Menu getMenu() {
        if (!this.mMenuCallbackSet) {
            this.mDecorToolbar.setMenuCallbacks(new ActionMenuPresenterCallback(), new MenuBuilderCallback());
            this.mMenuCallbackSet = true;
        }
        return this.mDecorToolbar.getMenu();
    }

    @Override
    public void addOnMenuVisibilityListener(ActionBar.OnMenuVisibilityListener onMenuVisibilityListener) {
        this.mMenuVisibilityListeners.add(onMenuVisibilityListener);
    }

    @Override
    public void addTab(ActionBar.Tab tab) {
        throw new UnsupportedOperationException("Tabs are not supported in toolbar action bars");
    }

    @Override
    public void addTab(ActionBar.Tab tab, int n2) {
        throw new UnsupportedOperationException("Tabs are not supported in toolbar action bars");
    }

    @Override
    public void addTab(ActionBar.Tab tab, int n2, boolean bl2) {
        throw new UnsupportedOperationException("Tabs are not supported in toolbar action bars");
    }

    @Override
    public void addTab(ActionBar.Tab tab, boolean bl2) {
        throw new UnsupportedOperationException("Tabs are not supported in toolbar action bars");
    }

    @Override
    public boolean collapseActionView() {
        if (this.mDecorToolbar.hasExpandedActionView()) {
            this.mDecorToolbar.collapseActionView();
            return true;
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void dispatchMenuVisibilityChanged(boolean bl2) {
        if (bl2 != this.mLastMenuVisibility) {
            this.mLastMenuVisibility = bl2;
            int n2 = this.mMenuVisibilityListeners.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                this.mMenuVisibilityListeners.get(i2).onMenuVisibilityChanged(bl2);
            }
        }
    }

    @Override
    public View getCustomView() {
        return this.mDecorToolbar.getCustomView();
    }

    @Override
    public int getDisplayOptions() {
        return this.mDecorToolbar.getDisplayOptions();
    }

    @Override
    public float getElevation() {
        return ViewCompat.getElevation((View)this.mDecorToolbar.getViewGroup());
    }

    @Override
    public int getHeight() {
        return this.mDecorToolbar.getHeight();
    }

    @Override
    public int getNavigationItemCount() {
        return 0;
    }

    @Override
    public int getNavigationMode() {
        return 0;
    }

    @Override
    public int getSelectedNavigationIndex() {
        return -1;
    }

    @Override
    public ActionBar.Tab getSelectedTab() {
        throw new UnsupportedOperationException("Tabs are not supported in toolbar action bars");
    }

    @Override
    public CharSequence getSubtitle() {
        return this.mDecorToolbar.getSubtitle();
    }

    @Override
    public ActionBar.Tab getTabAt(int n2) {
        throw new UnsupportedOperationException("Tabs are not supported in toolbar action bars");
    }

    @Override
    public int getTabCount() {
        return 0;
    }

    @Override
    public Context getThemedContext() {
        return this.mDecorToolbar.getContext();
    }

    @Override
    public CharSequence getTitle() {
        return this.mDecorToolbar.getTitle();
    }

    public Window.Callback getWrappedWindowCallback() {
        return this.mWindowCallback;
    }

    @Override
    public void hide() {
        this.mDecorToolbar.setVisibility(8);
    }

    @Override
    public boolean invalidateOptionsMenu() {
        this.mDecorToolbar.getViewGroup().removeCallbacks(this.mMenuInvalidator);
        ViewCompat.postOnAnimation((View)this.mDecorToolbar.getViewGroup(), this.mMenuInvalidator);
        return true;
    }

    @Override
    public boolean isShowing() {
        return this.mDecorToolbar.getVisibility() == 0;
    }

    @Override
    public boolean isTitleTruncated() {
        return super.isTitleTruncated();
    }

    @Override
    public ActionBar.Tab newTab() {
        throw new UnsupportedOperationException("Tabs are not supported in toolbar action bars");
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    @Override
    void onDestroy() {
        this.mDecorToolbar.getViewGroup().removeCallbacks(this.mMenuInvalidator);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean onKeyShortcut(int n2, KeyEvent keyEvent) {
        Menu menu = this.getMenu();
        if (menu != null) {
            int n3 = keyEvent != null ? keyEvent.getDeviceId() : -1;
            boolean bl2 = KeyCharacterMap.load((int)n3).getKeyboardType() != 1;
            menu.setQwertyMode(bl2);
            menu.performShortcut(n2, keyEvent, 0);
        }
        return true;
    }

    @Override
    public boolean onMenuKeyEvent(KeyEvent keyEvent) {
        if (keyEvent.getAction() == 1) {
            this.openOptionsMenu();
        }
        return true;
    }

    @Override
    public boolean openOptionsMenu() {
        return this.mDecorToolbar.showOverflowMenu();
    }

    void populateOptionsMenu() {
        MenuBuilder menuBuilder = null;
        Menu menu = this.getMenu();
        if (menu instanceof MenuBuilder) {
            menuBuilder = (MenuBuilder)menu;
        }
        if (menuBuilder != null) {
            menuBuilder.stopDispatchingItemsChanged();
        }
        try {
            menu.clear();
            if (!this.mWindowCallback.onCreatePanelMenu(0, menu) || !this.mWindowCallback.onPreparePanel(0, null, menu)) {
                menu.clear();
            }
            return;
        }
        finally {
            if (menuBuilder != null) {
                menuBuilder.startDispatchingItemsChanged();
            }
        }
    }

    @Override
    public void removeAllTabs() {
        throw new UnsupportedOperationException("Tabs are not supported in toolbar action bars");
    }

    @Override
    public void removeOnMenuVisibilityListener(ActionBar.OnMenuVisibilityListener onMenuVisibilityListener) {
        this.mMenuVisibilityListeners.remove(onMenuVisibilityListener);
    }

    @Override
    public void removeTab(ActionBar.Tab tab) {
        throw new UnsupportedOperationException("Tabs are not supported in toolbar action bars");
    }

    @Override
    public void removeTabAt(int n2) {
        throw new UnsupportedOperationException("Tabs are not supported in toolbar action bars");
    }

    @Override
    public boolean requestFocus() {
        ViewGroup viewGroup = this.mDecorToolbar.getViewGroup();
        if (viewGroup != null && !viewGroup.hasFocus()) {
            viewGroup.requestFocus();
            return true;
        }
        return false;
    }

    @Override
    public void selectTab(ActionBar.Tab tab) {
        throw new UnsupportedOperationException("Tabs are not supported in toolbar action bars");
    }

    @Override
    public void setBackgroundDrawable(@Nullable Drawable drawable2) {
        this.mDecorToolbar.setBackgroundDrawable(drawable2);
    }

    @Override
    public void setCustomView(int n2) {
        this.setCustomView(LayoutInflater.from((Context)this.mDecorToolbar.getContext()).inflate(n2, this.mDecorToolbar.getViewGroup(), false));
    }

    @Override
    public void setCustomView(View view) {
        this.setCustomView(view, new ActionBar.LayoutParams(-2, -2));
    }

    @Override
    public void setCustomView(View view, ActionBar.LayoutParams layoutParams) {
        if (view != null) {
            view.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        }
        this.mDecorToolbar.setCustomView(view);
    }

    @Override
    public void setDefaultDisplayHomeAsUpEnabled(boolean bl2) {
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void setDisplayHomeAsUpEnabled(boolean bl2) {
        int n2 = bl2 ? 4 : 0;
        this.setDisplayOptions(n2, 4);
    }

    @Override
    public void setDisplayOptions(int n2) {
        this.setDisplayOptions(n2, -1);
    }

    @Override
    public void setDisplayOptions(int n2, int n3) {
        int n4 = this.mDecorToolbar.getDisplayOptions();
        this.mDecorToolbar.setDisplayOptions(n2 & n3 | ~n3 & n4);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void setDisplayShowCustomEnabled(boolean bl2) {
        int n2 = bl2 ? 16 : 0;
        this.setDisplayOptions(n2, 16);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void setDisplayShowHomeEnabled(boolean bl2) {
        int n2 = bl2 ? 2 : 0;
        this.setDisplayOptions(n2, 2);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void setDisplayShowTitleEnabled(boolean bl2) {
        int n2 = bl2 ? 8 : 0;
        this.setDisplayOptions(n2, 8);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void setDisplayUseLogoEnabled(boolean bl2) {
        int n2 = bl2 ? 1 : 0;
        this.setDisplayOptions(n2, 1);
    }

    @Override
    public void setElevation(float f2) {
        ViewCompat.setElevation((View)this.mDecorToolbar.getViewGroup(), f2);
    }

    @Override
    public void setHomeActionContentDescription(int n2) {
        this.mDecorToolbar.setNavigationContentDescription(n2);
    }

    @Override
    public void setHomeActionContentDescription(CharSequence charSequence) {
        this.mDecorToolbar.setNavigationContentDescription(charSequence);
    }

    @Override
    public void setHomeAsUpIndicator(int n2) {
        this.mDecorToolbar.setNavigationIcon(n2);
    }

    @Override
    public void setHomeAsUpIndicator(Drawable drawable2) {
        this.mDecorToolbar.setNavigationIcon(drawable2);
    }

    @Override
    public void setHomeButtonEnabled(boolean bl2) {
    }

    @Override
    public void setIcon(int n2) {
        this.mDecorToolbar.setIcon(n2);
    }

    @Override
    public void setIcon(Drawable drawable2) {
        this.mDecorToolbar.setIcon(drawable2);
    }

    @Override
    public void setListNavigationCallbacks(SpinnerAdapter spinnerAdapter, ActionBar.OnNavigationListener onNavigationListener) {
        this.mDecorToolbar.setDropdownParams(spinnerAdapter, new NavItemSelectedListener(onNavigationListener));
    }

    @Override
    public void setLogo(int n2) {
        this.mDecorToolbar.setLogo(n2);
    }

    @Override
    public void setLogo(Drawable drawable2) {
        this.mDecorToolbar.setLogo(drawable2);
    }

    @Override
    public void setNavigationMode(int n2) {
        if (n2 == 2) {
            throw new IllegalArgumentException("Tabs not supported in this configuration");
        }
        this.mDecorToolbar.setNavigationMode(n2);
    }

    @Override
    public void setSelectedNavigationItem(int n2) {
        switch (this.mDecorToolbar.getNavigationMode()) {
            default: {
                throw new IllegalStateException("setSelectedNavigationIndex not valid for current navigation mode");
            }
            case 1: 
        }
        this.mDecorToolbar.setDropdownSelectedPosition(n2);
    }

    @Override
    public void setShowHideAnimationEnabled(boolean bl2) {
    }

    @Override
    public void setSplitBackgroundDrawable(Drawable drawable2) {
    }

    @Override
    public void setStackedBackgroundDrawable(Drawable drawable2) {
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void setSubtitle(int n2) {
        DecorToolbar decorToolbar = this.mDecorToolbar;
        CharSequence charSequence = n2 != 0 ? this.mDecorToolbar.getContext().getText(n2) : null;
        decorToolbar.setSubtitle(charSequence);
    }

    @Override
    public void setSubtitle(CharSequence charSequence) {
        this.mDecorToolbar.setSubtitle(charSequence);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void setTitle(int n2) {
        DecorToolbar decorToolbar = this.mDecorToolbar;
        CharSequence charSequence = n2 != 0 ? this.mDecorToolbar.getContext().getText(n2) : null;
        decorToolbar.setTitle(charSequence);
    }

    @Override
    public void setTitle(CharSequence charSequence) {
        this.mDecorToolbar.setTitle(charSequence);
    }

    @Override
    public void setWindowTitle(CharSequence charSequence) {
        this.mDecorToolbar.setWindowTitle(charSequence);
    }

    @Override
    public void show() {
        this.mDecorToolbar.setVisibility(0);
    }

    private final class ActionMenuPresenterCallback
    implements MenuPresenter.Callback {
        private boolean mClosingActionMenu;

        private ActionMenuPresenterCallback() {
        }

        @Override
        public void onCloseMenu(MenuBuilder menuBuilder, boolean bl2) {
            if (this.mClosingActionMenu) {
                return;
            }
            this.mClosingActionMenu = true;
            ToolbarActionBar.this.mDecorToolbar.dismissPopupMenus();
            if (ToolbarActionBar.this.mWindowCallback != null) {
                ToolbarActionBar.this.mWindowCallback.onPanelClosed(108, (Menu)menuBuilder);
            }
            this.mClosingActionMenu = false;
        }

        @Override
        public boolean onOpenSubMenu(MenuBuilder menuBuilder) {
            if (ToolbarActionBar.this.mWindowCallback != null) {
                ToolbarActionBar.this.mWindowCallback.onMenuOpened(108, (Menu)menuBuilder);
                return true;
            }
            return false;
        }
    }

    private final class MenuBuilderCallback
    implements MenuBuilder.Callback {
        private MenuBuilderCallback() {
        }

        @Override
        public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
            return false;
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void onMenuModeChange(MenuBuilder menuBuilder) {
            if (ToolbarActionBar.this.mWindowCallback == null) return;
            if (ToolbarActionBar.this.mDecorToolbar.isOverflowMenuShowing()) {
                ToolbarActionBar.this.mWindowCallback.onPanelClosed(108, (Menu)menuBuilder);
                return;
            } else {
                if (!ToolbarActionBar.this.mWindowCallback.onPreparePanel(0, null, (Menu)menuBuilder)) return;
                ToolbarActionBar.this.mWindowCallback.onMenuOpened(108, (Menu)menuBuilder);
                return;
            }
        }
    }

    private final class PanelMenuPresenterCallback
    implements MenuPresenter.Callback {
        private PanelMenuPresenterCallback() {
        }

        @Override
        public void onCloseMenu(MenuBuilder menuBuilder, boolean bl2) {
            if (ToolbarActionBar.this.mWindowCallback != null) {
                ToolbarActionBar.this.mWindowCallback.onPanelClosed(0, (Menu)menuBuilder);
            }
        }

        @Override
        public boolean onOpenSubMenu(MenuBuilder menuBuilder) {
            if (menuBuilder == null && ToolbarActionBar.this.mWindowCallback != null) {
                ToolbarActionBar.this.mWindowCallback.onMenuOpened(0, (Menu)menuBuilder);
            }
            return true;
        }
    }

    private class ToolbarCallbackWrapper
    extends WindowCallbackWrapper {
        public ToolbarCallbackWrapper(Window.Callback callback) {
            super(callback);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public View onCreatePanelView(int n2) {
            switch (n2) {
                default: {
                    return super.onCreatePanelView(n2);
                }
                case 0: {
                    Menu menu = ToolbarActionBar.this.mDecorToolbar.getMenu();
                    if (!this.onPreparePanel(n2, null, menu) || !this.onMenuOpened(n2, menu)) return super.onCreatePanelView(n2);
                    return ToolbarActionBar.this.getListMenuView(menu);
                }
            }
        }

        @Override
        public boolean onPreparePanel(int n2, View view, Menu menu) {
            boolean bl2 = super.onPreparePanel(n2, view, menu);
            if (bl2 && !ToolbarActionBar.this.mToolbarMenuPrepared) {
                ToolbarActionBar.this.mDecorToolbar.setMenuPrepared();
                ToolbarActionBar.access$202(ToolbarActionBar.this, true);
            }
            return bl2;
        }
    }
}

