package com.thirtydegreesray.openhub.mvp.model.filter;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;

import com.thirtydegreesray.openhub.R;
import com.thirtydegreesray.openhub.ui.fragment.RepositoriesFragment;
import com.thirtydegreesray.openhub.util.ViewUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ThirtyDegreesRay on 2017/11/9 16:30:13
 */

public class RepositoriesFilter implements Parcelable {

    public final static RepositoriesFilter DEFAULT = new RepositoriesFilter();

    public final static RepositoriesFilter DEFAULT_STARRED_REPO
            = new RepositoriesFilter().setSort(Sort.Created).setSortDirection(SortDirection.Desc);

    private enum Type{
        All, Owner, Public, Private, Member
    }

    private enum Sort{
        Created, Updated, Pushed, Full_name
    }

    private final static Map<Integer, Type> TYPE_RELATION = new HashMap<>();
    static {
        TYPE_RELATION.put(R.id.nav_all, Type.All);
        TYPE_RELATION.put(R.id.nav_owner, Type.Owner);
        TYPE_RELATION.put(R.id.nav_public, Type.Public);
        TYPE_RELATION.put(R.id.nav_private, Type.Private);
        TYPE_RELATION.put(R.id.nav_member, Type.Member);
    }

    private Type type = Type.All;
    private Sort sort = Sort.Full_name;
    private SortDirection sortDirection = SortDirection.Asc;

    public static RepositoriesFilter generateFromDrawer(@NonNull NavigationView navView){
        RepositoriesFilter filter = new RepositoriesFilter();
        MenuItem typeItem = ViewUtils.getSelectedMenu(navView.getMenu().findItem(R.id.nav_type_chooser));
        if (typeItem != null){
            filter.type = TYPE_RELATION.get(typeItem.getItemId());
        }

        MenuItem sortItem = ViewUtils.getSelectedMenu(navView.getMenu().findItem(R.id.nav_sort));
        Sort sort = Sort.Full_name;
        SortDirection sortDirection = SortDirection.Asc;
        if(sortItem != null){
            switch (sortItem.getItemId()){
                case R.id.nav_full_name_asc:
                    sort = Sort.Full_name;
                    sortDirection = SortDirection.Asc;
                    break;
                case R.id.nav_full_name_desc:
                    sort = Sort.Full_name;
                    sortDirection = SortDirection.Desc;
                    break;
                case R.id.nav_recently_created:
                    sort = Sort.Created;
                    sortDirection = SortDirection.Desc;
                    break;
                case R.id.nav_previously_created:
                    sort = Sort.Created;
                    sortDirection = SortDirection.Asc;
                    break;

                case R.id.nav_recently_updated:
                    sort = Sort.Updated;
                    sortDirection = SortDirection.Desc;
                    break;
                case R.id.nav_least_recently_updated:
                    sort = Sort.Updated;
                    sortDirection = SortDirection.Asc;
                    break;
                case R.id.nav_most_pushed:
                    sort = Sort.Pushed;
                    sortDirection = SortDirection.Desc;
                    break;
                case R.id.nav_fewest_pushed:
                    sort = Sort.Pushed;
                    sortDirection = SortDirection.Asc;
                    break;
            }
        }
        filter.sort = sort;
        filter.sortDirection = sortDirection;

        return filter;
    }

    public static void initDrawer(NavigationView navView,
                                  @NonNull RepositoriesFragment.RepositoriesType type){
        if(navView == null) return;
        if(RepositoriesFragment.RepositoriesType.OWNED.equals(type)){
            //do nothing
        } else if(RepositoriesFragment.RepositoriesType.PUBLIC.equals(type)){
            navView.getMenu().findItem(R.id.nav_private).setVisible(false);
            navView.getMenu().findItem(R.id.nav_public).setVisible(false);
        } else if(RepositoriesFragment.RepositoriesType.STARRED.equals(type)){
            navView.getMenu().findItem(R.id.nav_type_chooser).setVisible(false);
            navView.getMenu().findItem(R.id.nav_full_name_asc).setVisible(false);
            navView.getMenu().findItem(R.id.nav_full_name_desc).setVisible(false);
            navView.getMenu().findItem(R.id.nav_most_pushed).setVisible(false);
            navView.getMenu().findItem(R.id.nav_fewest_pushed).setVisible(false);

            navView.getMenu().findItem(R.id.nav_full_name_asc).setChecked(false);
            navView.getMenu().findItem(R.id.nav_recently_created).setChecked(true);
            navView.getMenu().findItem(R.id.nav_recently_created).setTitle(R.string.recently_starred);
            navView.getMenu().findItem(R.id.nav_previously_created).setTitle(R.string.previously_starred);
        }
    }

    public String getType() {
        return type.name().toLowerCase();
    }

    public String getSort() {
        return sort.name().toLowerCase();
    }

    public String getSortDirection() {
        return sortDirection.name().toLowerCase();
    }

    private RepositoriesFilter setType(Type type) {
        this.type = type;
        return this;
    }

    private RepositoriesFilter setSort(Sort sort) {
        this.sort = sort;
        return this;
    }

    private RepositoriesFilter setSortDirection(SortDirection sortDirection) {
        this.sortDirection = sortDirection;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeInt(this.sort == null ? -1 : this.sort.ordinal());
        dest.writeInt(this.sortDirection == null ? -1 : this.sortDirection.ordinal());
    }

    public RepositoriesFilter() {
    }

    protected RepositoriesFilter(Parcel in) {
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : Type.values()[tmpType];
        int tmpSort = in.readInt();
        this.sort = tmpSort == -1 ? null : Sort.values()[tmpSort];
        int tmpSortDirection = in.readInt();
        this.sortDirection = tmpSortDirection == -1 ? null : SortDirection.values()[tmpSortDirection];
    }

    public static final Parcelable.Creator<RepositoriesFilter> CREATOR = new Parcelable.Creator<RepositoriesFilter>() {
        @Override
        public RepositoriesFilter createFromParcel(Parcel source) {
            return new RepositoriesFilter(source);
        }

        @Override
        public RepositoriesFilter[] newArray(int size) {
            return new RepositoriesFilter[size];
        }
    };

}
