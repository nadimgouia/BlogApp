package apps.dev.ndroid.blogapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ShowArticle extends AppCompatActivity {

    private String post_id;
    DatabaseReference mDatabaseRef;
    StorageReference mStorageRef;
    String image;
    TextView tvTitle, tvDesc;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_article);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Blog");
        mStorageRef = FirebaseStorage.getInstance().getReference();

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvDesc = (TextView) findViewById(R.id.tvDesc);
        imageView = (ImageView) findViewById(R.id.imageView);

        post_id = getIntent().getStringExtra("post_id");
        Toast.makeText(ShowArticle.this, "post id : " + post_id, Toast.LENGTH_SHORT).show();

        mDatabaseRef.child(post_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String title = (String) dataSnapshot.child("title").getValue();
                String description = (String) dataSnapshot.child("description").getValue();
                image = (String) dataSnapshot.child("image").getValue();

                tvTitle.setText(title);
                tvDesc.setText(description);
                Picasso.with(ShowArticle.this).load(image).into(imageView);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.remove_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.itemremove) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Are you sure ?");

            builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //mDatabaseRef.child(post_id).removeValue();
                    // StorageReference deleteRef = mStorageRef.child(image);
                    //deleteRef.delete();
                    // startActivity(new Intent(ShowArticle.this, MainActivity.class));

                    Toast.makeText(ShowArticle.this, image, Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });


            builder.show();

        }
        return super.onOptionsItemSelected(item);
    }


}
