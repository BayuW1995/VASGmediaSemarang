package gmedia.net.id.vasgmediasemarang.utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class recyclerViewLoadMoreScrollListener extends RecyclerView.OnScrollListener {

    private boolean loading = false;
    private LoadListener listener;
    private boolean canLoad = true;

    public recyclerViewLoadMoreScrollListener(LoadListener listener){
        this.listener = listener;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if(recyclerView.getLayoutManager() instanceof LinearLayoutManager){
            LinearLayoutManager layoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
            RecyclerView.Adapter adapter = recyclerView.getAdapter();

            if(adapter != null){
                if(canLoad && dy > 0 && !loading && layoutManager.findLastVisibleItemPosition() == adapter.getItemCount() - 1){
                    //Melakukan load more
                    loading = true;
                    listener.onLoad();
                }
            }
        }
    }

    public void cantLoad(){
        this.canLoad = false;
    }
    public void canLoad(){
        this.canLoad = true;
    }

    public void finishLoad(){
        this.loading = false;
    }

    public interface LoadListener{
        void onLoad();
    }
}
