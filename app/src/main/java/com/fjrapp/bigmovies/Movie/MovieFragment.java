package com.fjrapp.bigmovies.Movie;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fjrapp.bigmovies.Movie.adapter.MovieAdapter;
import com.fjrapp.bigmovies.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import static com.fjrapp.bigmovies.Tool.API_KEY;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;


public class MovieFragment extends Fragment {


    @Nullable
    @BindView(R.id.swloading)
    SwipeRefreshLayout swlayout;

    @Nullable
    @BindView(R.id.this_recycler_view)
    RecyclerView recyclerView;

    @Nullable
    @BindView(R.id.movie_title)
    TextView movieName;

    private MovieAdapter adapter;
    private SearchView searchView;


    public static MovieFragment newInstance() {
        MovieFragment fragment = new MovieFragment();
        fragment.setFragmentManager();
        return fragment;
    }

    public void setFragmentManager() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup view,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.recycler_view, view, false);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        setupRecyclerView();
        assert swlayout != null;
        swlayout.setColorSchemeColors(R.color.colorPrimaryDark);
        MovieViewModel movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.setMovie();
        swlayout.setRefreshing(true);
        movieViewModel.getMovie().observe(this, new Observer<ArrayList<Movie>>() {
            @Override
            public void onChanged(@Nullable final ArrayList<Movie> movies) {

                if (movies != null) {
                    adapter.setListMovie(movies);
                    swlayout.setRefreshing(false);
                    swlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.setListMovie(movies);
                                    swlayout.setRefreshing(false);
                                    Toast.makeText(getContext(), "Diperbaharui", Toast.LENGTH_SHORT).show();

                                }
                            }, 1000);
                        }
                    });
                }


            }

        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) Objects.requireNonNull(getActivity()).getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) (menu.findItem(R.id.action_search)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setQueryHint(getResources().getString(R.string.search_hint));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    assert swlayout != null;
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


    public void showSelectedMovie(Movie movie) {

        Intent intentmovie = new Intent(getActivity(), DetailActivity.class);
        intentmovie.putExtra(DetailActivity.EXTRA_NAME, movie);
        startActivity(intentmovie);

    }

    private void setupRecyclerView() {

        adapter = new MovieAdapter(getContext());
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        assert recyclerView != null;
        recyclerView.setLayoutManager(horizontalLayoutManagaer);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);



        adapter.setOnItemClickCallback(new MovieAdapter.OnItemClickCallback() {
            @Override
            public void onItemClicked(Movie movie) {
                showSelectedMovie(movie);
            }
        });

        adapter.notifyDataSetChanged();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }


    public void searchMovie(String query) {

        swlayout.setRefreshing(true);
        String url = "https://api.themoviedb.org/3/search/movie?api_key=" +
                API_KEY + "&language=en-US&query=" + query;
        final MutableLiveData<ArrayList<Movie>> listSearch = new MutableLiveData<>();
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<Movie> listItems = new ArrayList<>();
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                try {
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");

                    for (int i = 0; i < list.length(); i++) {
                        JSONObject tv = list.getJSONObject(i);
                        Movie listSearch = new Movie(tv);
                        listItems.add(listSearch);
                    }
                    listSearch.postValue(listItems);

                    adapter = new MovieAdapter();
                    if (listItems.size() == 0) {
                        Toast.makeText(getActivity(), "No Result", Toast.LENGTH_SHORT).show();
                        swlayout.setRefreshing(false);
                        recyclerView.setVisibility(View.INVISIBLE);

                    } else {
//                        if (listItems!=null) {
                        swlayout.setRefreshing(false);
                        adapter.setListMovie(listItems);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
//                        }
                        adapter.setOnItemClickCallback(new MovieAdapter.OnItemClickCallback() {
                            @Override
                            public void onItemClicked(Movie Movie) {
                                showSelectedMovie(Movie);
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

}

