package apps.dev.ndroid.blogapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddArticle extends AppCompatActivity {

    Button btnAdd;
    EditText editTitle, editDesc;
    ImageButton imageButton;
    public static final int GALLERY_REQUEST = 1;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private ProgressDialog progressDialog;

    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_article);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Blog");

        progressDialog = new ProgressDialog(this);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        editTitle = (EditText) findViewById(R.id.editTitle);
        editDesc = (EditText) findViewById(R.id.editDesc);
        imageButton = (ImageButton) findViewById(R.id.imageButton);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imgIntent = new Intent(Intent.ACTION_GET_CONTENT);
                imgIntent.setType("image/*");
                startActivityForResult(imgIntent, GALLERY_REQUEST);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startPosting();
            }
        });
    }

    private void startPosting() {
        progressDialog.setMessage("Posting ...");
        progressDialog.show();

        StorageReference imgRef = mStorageRef.child("images").child(uri.getLastPathSegment());

        imgRef.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                        String imgName = taskSnapshot.getStorage().getName();
//                        Log.d("imgname", imgName);
                        DatabaseReference newPost  = mDatabaseRef.push();
                        newPost.child("title").setValue(editTitle.getText().toString());
                        newPost.child("description").setValue(editDesc.getText().toString());
                        newPost.child("image").setValue(downloadUrl.toString());

                        progressDialog.dismiss();

                        startActivity(new Intent(AddArticle.this, MainActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        Log.d("ex", exception.getMessage());
                        Toast.makeText(AddArticle.this, "cannot save imagep", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
             uri = data.getData();
            imageButton.setImageURI(uri);
        }
    }
}
