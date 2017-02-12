package apps.dev.ndroid.blogapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerview;
    DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Blog");

        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
                        Blog.class,
                        R.layout.item_article,
                        BlogViewHolder.class,
                        mDatabaseRef) {
                    @Override
                    protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, int position) {

                        final String post_id = getRef(position).getKey();

                        viewHolder.setTitle(model.getTitle());
                        viewHolder.setDescription(model.getDescription());
                        viewHolder.setImage(getApplicationContext(), model.getImage());
                        viewHolder.itemBlog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent showIntent = new Intent(MainActivity.this, ShowArticle.class);
                                showIntent.putExtra("post_id", post_id);
                                startActivity(showIntent);
                            }
                        });
                    }
                };

        recyclerview.setAdapter(firebaseRecyclerAdapter);

    }


    public static class BlogViewHolder extends RecyclerView.ViewHolder {

        View itemBlog;


        public BlogViewHolder(View itemView) {
            super(itemView);
            this.itemBlog = itemView;
        }

        public void setTitle(String title) {
            TextView tvTitle = (TextView) itemBlog.findViewById(R.id.tvTitle);
            tvTitle.setText(title);
        }

        public void setDescription(String desc) {
            TextView tvDesc = (TextView) itemBlog.findViewById(R.id.tvDesc);
            tvDesc.setText(desc);
        }

        public void setImage(Context context, String image) {
            ImageView imageView = (ImageView) itemBlog.findViewById(R.id.imageView);
            Picasso.with(context).load(image).into(imageView);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_article) {
            startActivity(new Intent(MainActivity.this, AddArticle.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
