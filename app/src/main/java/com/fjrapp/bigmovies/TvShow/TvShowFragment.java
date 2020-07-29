package com.fjrapp.bigmovies.TvShow;


import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fjrapp.bigmovies.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static com.fjrapp.bigmovies.Tool.API_KEY;

public class TvShowFragment extends Fragment {

    @BindView(R.id.swloading)
    SwipeRefreshLayout swlayout;


    @BindView(R.id.this_recycler_view)
    RecyclerView recyclerView;

    private TvShowAdapter adapter;
    private FragmentManager fragmentManager;
    SearchView searchView;

    public TvShowFragment() {

    }

    public static TvShowFragment newInstance(FragmentManager fragmentManager) {
        TvShowFragment fragment = new TvShowFragment();
        fragment.setFragmentManager(fragmentManager);
        return fragment;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup view,
                             Bundle savedInstanceState) {
        View viewfrag = inflater.inflate(R.layout.recycler_view, view, false);
        setHasOptionsMenu(true);

        return viewfrag;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        swlayout.setColorSchemeColors(R.color.colorPrimaryDark);
        TvShowViewModel tvShowViewModel = ViewModelProviders.of(this).get(TvShowViewModel.class);
        tvShowViewModel.setTvShow();
        swlayout.setRefreshing(true);
        tvShowViewModel.getTvShow().observe(this, new Observer<ArrayList<TvShow>>() {
            @Override
            public void onChanged(@Nullable final ArrayList<TvShow> tvShows) {

                if (tvShows != null) {
                    adapter.setData(tvShows);
                    swlayout.setRefreshing(false);
                    swlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.setData(tvShows);
                                    swlayout.setRefreshing(false);
                                }
                            }, 5000);
                        }
                    });
                }


            }

        });
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        adapter = new TvShowAdapter(getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        adapter.setOnItemClickCallback(new TvShowAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(TvShow tvShow) {

                showSelectedTvShow(tvShow);

            }
        });

        adapter.notifyDataSetChanged();
    }

    public void showSelectedTvShow(TvShow tvShow) {
        Intent intentMovie = new Intent(getActivity(), DetailActivityTvShow.class);
        intentMovie.putExtra(DetailActivityTvShow.EXTRA_NAME, tvShow);
        startActivity(intentMovie);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) (menu.findItem(R.id.action_search)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.search_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    swlayout.setRefreshing(true);
                    searchMovie(query);
                    Toast.makeText(getContext(), query, Toast.LENGTH_SHORT).show();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
    }

    public void searchMovie(String query) {
        swlayout.setRefreshing(true);
        String url = "https://api.themoviedb.org/3/search/tv?api_key=" +
                API_KEY + "&language=en-US&query=" + query;
        final MutableLiveData<ArrayList<TvShow>> listSearch = new MutableLiveData<>();
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<TvShow> listItems = new ArrayList<>();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject tv = list.getJSONObject(i);
                        TvShow listSearch = new TvShow(tv);
                        listItems.add(listSearch);
                    }
                    listSearch.postValue(listItems);

                    adapter = new TvShowAdapter();
                    if (listItems.size() == 0) {
                        Toast.makeText(getActivity(), "No Result", Toast.LENGTH_SHORT).show();
                        recyclerView.setVisibility(View.INVISIBLE);
                    } else {
//                        if (listItems!=null) {
                        swlayout.setRefreshing(false);
                        adapter.setData(listItems);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
//                        }
                        adapter.setOnItemClickCallback(new TvShowAdapter.OnItemClickCallback() {
                            @Override
                            public void onItemClicked(TvShow tvShow) {
                                showSelectedTvShow(tvShow);
                            }
                        });
                        searchView.onActionViewCollapsed();
                    }

                } catch (Exception e) {
                    Log.d("Exception", e.getMessage());

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("Onfailure", error.getMessage());

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }


}
