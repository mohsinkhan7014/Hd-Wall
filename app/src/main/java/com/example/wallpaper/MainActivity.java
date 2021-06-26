package com.example.wallpaper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    EditText searchEditText;
    ImageButton searchImageButton;
    RecyclerView recyclerView;
    WallpaperAdaptor wallpaperAdaptor;
    List<WallpaperModel> wallpaperModelList;

    int page_no=1;
    Boolean isScrolling = false;
    int currentItem,totalItem,scrollOutItems;
    String url="https://api.pexels.com/v1/curated/?/page="+page_no+"&per_page=80";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=findViewById(R.id.recyclerView);
        searchEditText=findViewById(R.id.searchEditText);
        searchImageButton=findViewById(R.id.btnsearch);


        wallpaperModelList=new ArrayList<>();
        wallpaperAdaptor=new WallpaperAdaptor(this,wallpaperModelList);
        recyclerView.setAdapter(wallpaperAdaptor);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,2);

        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged( RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                    isScrolling=true;
                }

            }

            @Override
            public void onScrolled( RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItem=gridLayoutManager.getChildCount();
                totalItem=gridLayoutManager.getItemCount();
                scrollOutItems=gridLayoutManager.findFirstVisibleItemPosition();

                if(isScrolling && (currentItem+scrollOutItems==totalItem))
                {
                    fetchWallpaper();
                }
            }
        });
        fetchWallpaper();

    }

    public void fetchWallpaper()
    {
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               try {
                   //page file
                   JSONObject jsonObject=new JSONObject(response);
                   //data
                   JSONArray jsonArray=jsonObject.getJSONArray("photos");

                   int length=jsonArray.length();
                   for(int i=0;i<length;i++)
                   {
                       JSONObject object=jsonArray.getJSONObject(i);
                       int id=object.getInt("id");
                       JSONObject objectImages=object.getJSONObject("src");
                       String originalUrl=objectImages.getString("original");
                       String mediumUrl=objectImages.getString("medium");
                       WallpaperModel wallpaperModel=new WallpaperModel(id,originalUrl,mediumUrl);
                       wallpaperModelList.add(wallpaperModel);




                   }
                   wallpaperAdaptor.notifyDataSetChanged();
                   page_no++;


               }catch (JSONException e)
               {

               }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            //authorizATION

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String ,String> par=new HashMap<>();
                par.put("Authorization","563492ad6f91700001000001ee7ad7f1f24745f7b61279e712ac021b");


                return par;


            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_search,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.nav)
        {
            AlertDialog.Builder alert=new AlertDialog.Builder(this);
            EditText editText=new EditText(this);
            editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            alert.setMessage("Enter your Category");
            alert.setTitle("Search pic");

            alert.setView(editText);
            alert.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String query=editText.getText().toString().toLowerCase();
                    url="https://api.pexels.com/v1/search/?page="+page_no+"&per_page=80&query="+query;
                    wallpaperModelList.clear();
                    fetchWallpaper();

                }
            });

            alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alert.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void SearchItemOn(View view) {
        String myQuery=searchEditText.getText().toString().trim().toLowerCase();
        url="https://api.pexels.com/v1/search/?page="+page_no+"&per_page=80&query="+myQuery;
        wallpaperModelList.clear();
        toast(myQuery+" images are Loading");
        fetchWallpaper();
    }

    private void toast(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public void natureCardClicked(View view) {

        url="https://api.pexels.com/v1/search/?page="+page_no+"&per_page=80&query="+"nature";
        wallpaperModelList.clear();
        toast("nature images are loading ");
        fetchWallpaper();

    }

    public void carCardClicked(View view) {
        url="https://api.pexels.com/v1/search/?page="+page_no+"&per_page=80&query="+"car";
        wallpaperModelList.clear();
        toast("Car images are loading ");
        fetchWallpaper();
    }

    public void loveCard(View view) {
        url="https://api.pexels.com/v1/search/?page="+page_no+"&per_page=80&query="+"love";
        wallpaperModelList.clear();
        toast("Love images are loading ");
        fetchWallpaper();
    }


    public void moonCard(View view) {
        url="https://api.pexels.com/v1/search/?page="+page_no+"&per_page=80&query="+"moon";
        wallpaperModelList.clear();
        toast("Moon images are loading ");
        fetchWallpaper();
    }

    public void techOnclicked(View view) {
        url="https://api.pexels.com/v1/search/?page="+page_no+"&per_page=80&query="+"technology";
        wallpaperModelList.clear();
        toast("Tech images are loading ");
        fetchWallpaper();
    }

    public void foodCliked(View view) {
        url="https://api.pexels.com/v1/search/?page="+page_no+"&per_page=80&query="+"food";
        wallpaperModelList.clear();
        toast("Food images are loading ");
        fetchWallpaper();
    }

    public void animCliked(View view) {
        url="https://api.pexels.com/v1/search/?page="+page_no+"&per_page=80&query="+"animal";
        wallpaperModelList.clear();
        toast("Animal images are loading ");
        fetchWallpaper();
    }

    public void flower(View view) {
        url="https://api.pexels.com/v1/search/?page="+page_no+"&per_page=80&query="+"flower";
        wallpaperModelList.clear();
        toast("flower images are loading ");
        fetchWallpaper();
    }
}